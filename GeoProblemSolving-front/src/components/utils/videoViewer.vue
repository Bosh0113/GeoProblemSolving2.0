<style scoped>
.videoPanel {
  margin: auto;
}
.ivu-btn-default {
  margin-left: 75px;
}
.ivu-menu-item {
  padding: 5px 5px 5px 5px;
}
.sidebar {
  width: 60px;
  background-color: #515a6e;
  justify-content: center;
  position: absolute;
}
</style>
<template>
  <div>
    <div class="sidebar" style="width:60px;float:left" :style="{height:windowHeight+'px'}">
      <div style="display:flex;justify-content:center;margin-top:20px" title="Resources">
        <span @click="resourceDrawer=true" style="cursor:pointer">
          <Icon type="md-folder" :size="40" color="white" />
        </span>
        <Drawer title="Resources" :closable="false" v-model="resourceDrawer" placement="left">
          <div style="height:45%;overflow-y:auto">
            <ul>
              <li
                v-for="(resource,index) in videoList"
                :key="index"
                @click="selectVideo(resource.pathURL)"
                style="cursor:pointer;margin-bottom:10px"
                :title="resource.description"
              >{{resource.name}}</li>
            </ul>
          </div>
        </Drawer>
      </div>
    </div>
    <div :style="{height:windowHeight+'px'}" style="float:left;padding-left:100px;padding-top:40px">
      <video :width="videoWidth" :height="videoHeight" :src="videoUrl" controls class="videoPanel"></video>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      windowHeight: 0,
      videoWidth: 100,
      videoHeight: 100,
      videoList: [],
      videoUrl: "",
      activeItem: 0,
      resourceDrawer: false,
      pageParams: { pageId: "", userId: "", userName: "" },
      userInfo: {}
    };
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        next();
      }
    });
  },
  mounted() {
    window.addEventListener("resize", this.initSize);
    this.initSize();
    this.getStepInfo();
    this.getUserInfo();
    this.getVideoResource();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      $("#app").css("min-width", "0");
      $("#app").css("min-height", "0");
      this.windowHeight = window.innerHeight;
      this.videoWidth = window.innerWidth - 80 - 60;
      this.videoHeight = window.innerHeight - 80;
    },
    getStepInfo() {
      if (
        this.$route.params.groupID == undefined ||
        this.$route.params.groupID == ""
      ) {
        var href = window.location.href;
        var url = href.split("&");

        for (var i = 0; i < url.length; i++) {
          if (/groupID/.test(url[i])) {
            this.pageParams.pageId = url[i].match(/groupID=(\S*)/)[1];
            continue;
          }

          if (/userID/.test(url[i])) {
            this.pageParams.userId = url[i].match(/userID=(\S*)/)[1];
            continue;
          }

          if (/userName/.test(url[i])) {
            this.pageParams.userName = url[i].match(/userName=(\S*)/)[1];
            continue;
          }
        }
      } else {
        this.pageParams.pageId = this.$route.params.groupID;
        this.pageParams.userId = this.$route.params.userID;
        this.pageParams.userName = this.$route.params.userName;
      }
    },
    getUserInfo() {
      this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      if (this.userInfo == {}) {
        this.axios
          .get(
            "/GeoProblemSolving/user/inquiry" +
              "?key=" +
              "userId" +
              "&value=" +
              this.pageParams.userId
          )
          .then(res => {
            if (res.data != "Fail" && res.data != "None") {
              this.$set(this, "userInfo", res.data);
            }
          })
          .catch(err => {});
      }
    },
    getVideoResource() {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      this.videoList = [];
      this.axios
        .get(
          "/GeoProblemSolving/folder/inquiry?folderId=" + this.pageParams.pageId
        )
        .then(res => {
          // 写渲染函数，取到所有资源
          if (res.data !== "None") {
            for (let i = 0; i < res.data.files.length; i++) {
              if (res.data.files[i].type == "video") {
                this.videoList.push(res.data.files[i]);
              }
            }
          } else {
            this.videoList = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    selectVideo(url) {
      this.videoUrl = url;
    },
    handleUpload(file) {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      if (!/\.(mp4)$/.test(file.name.toLowerCase())) {
        this.$Message.error("上传格式不正确，请上传xls或者xlsx格式");
        return false;
      }

      //上传数据
      let formData = new FormData();
      formData.append("file", file);
      formData.append("description", "video viewer tool");
      formData.append("type", "video");
      formData.append("uploaderId", this.userInfo.userId);
      formData.append("privacy", "private");
      formData.append("folderId", this.pageParams.pageId);
      this.axios
        .post("/GeoProblemSolving/folder/uploadToFolder", formData)
        .then(res => {
          if (
            res.data == "Size over" ||
            res.data == "Fail" ||
            res.data == "Offline"
          ) {
            console.log(res.data);
          } else if (res.data.length > 0) {
            let videoName = res.data[0].fileName;
            this.videoUrl = "/GeoProblemSolving/resource/upload/" + videoName;

            let videoItem = {
              name: file.name,
              description: "video viewer tool",
              pathURL: "/GeoProblemSolving/resource/upload/" + videoName
            };
            this.videoList.push(videoItem);
            this.activeItem = this.videoList.length - 1;
          }
        })
        .catch(err => {});
      return false;
    }
  }
};
</script>
