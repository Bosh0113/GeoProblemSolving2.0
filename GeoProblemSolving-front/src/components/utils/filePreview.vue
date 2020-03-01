<style scoped>
.pfdPanel {
  margin: auto;
  float: left;
}
.ivu-btn-default {
  margin-left: 10px;
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
.toolContent {
  flex: 1;
  height: 100%;
}
.noDetail h1 {
  color: darkgray;
  text-align: center;
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
            <div style="font-size:1rem;margin-bottom:10px">Papers:</div>
            <ul>
              <li
                v-for="(resource,index) in paperList"
                :key="index"
                @click="selectResource(resource.pathURL)"
                style="cursor:pointer;margin-bottom:10px"
                :title="resource.description"
              >{{resource.name}}</li>
            </ul>
          </div>
          <div style="height:45%;overflow-y:auto">
            <div style="font-size:1rem;margin-bottom:10px">Documents:</div>
            <ul>
              <li
                v-for="(resource,index) in documentList"
                :key="index"
                @click="selectResource(resource.pathURL)"
                style="cursor:pointer;margin-bottom:10px"
                :title="resource.description"
              >{{resource.name}}</li>
            </ul>
          </div>
        </Drawer>
      </div>
    </div>
    <div :style="{height:windowHeight+'px'}" style="margin-left:60px">
      <template v-if="fileURL != ''" class="pfdPanel">
        <iframe :width="fileWidth" :height="fileHeight-5" :src="fileURL">
          This browser does not support Files. Please download the file to view it:
          <a
            :href="fileURL"
          >Download file</a>
        </iframe>
      </template>
      <template v-else class="pfdPanel">
        <div :style="{height:fileHeight+'px', width:fileWidth+'px'}">
          <Card class="noDetail">
            <h1>No file selected</h1>
          </Card>
        </div>
      </template>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  data() {
    return {
      windowHeight: window.innerHeight,
      fileWidth: window.innerWidth - 60,
      fileHeight: window.innerHeight,
      paperList: [],
      documentList: [],
      fileURL: "",
      isPaper: false,
      activeItem: "",
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
    this.getResource();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      $("#app").css("min-width", "0");
      $("#app").css("min-height", "0");
      this.windowHeight = window.innerHeight;
      this.fileHeight = window.innerHeight;
      this.fileWidth = window.innerWidth - 60;
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
    getResource() {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      this.paperList = [];
      this.documentList = [];

      this.axios
        .get(
          "/GeoProblemSolving/folder/inquiry?folderId=" + this.pageParams.pageId
        )
        .then(res => {
          // 写渲染函数，取到所有资源
          if (res.data !== "None") {
            for (let i = 0; i < res.data.files.length; i++) {
              if (res.data.files[i].type == "paper") {
                this.paperList.push(res.data.files[i]);
              } else if (res.data.files[i].type == "document") {
                this.documentList.push(res.data.files[i]);
              }
            }
          } else {
            this.paperList = [];
            this.documentList = [];
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    selectFile(url, name) {
      if (!/\.(pdf)$/.test(name.toLowerCase())) {
        this.$Message.error("Worry format");
        return false;
      }
      this.fileURL = url;
    },
    handleUpload(file) {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      if (!/\.(pdf)$/.test(file.name.toLowerCase())) {
        this.$Message.error("Worry format");
        return false;
      }

      let fileType = "";
      if (this.isPaper) {
        fileType = "paper";
      } else {
        fileType = "document";
      }
      //上传数据
      let formData = new FormData();
      formData.append("file", file);
      formData.append("description", "file preview tool");
      formData.append("type", fileType);
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
            let fileName = res.data[0].fileName;
            this.fileURL = "/GeoProblemSolving/resource/upload/" + fileName;

            let fileItem = {
              name: file.name,
              description: "file preview tool",
              pathURL: "/GeoProblemSolving/resource/upload/" + fileName
            };
            if (this.isPaper) {
              this.paperList.push(fileItem);
              this.activeItem = "1-" + (this.paperList.length - 1);
            } else {
              this.documentList.push(fileItem);
              this.activeItem = "2-" + (this.documentList.length - 1);
            }
          }
        })
        .catch(err => {});
      return false;
    },
    selectResource(url) {
      this.fileURL = "http://" + this.$store.state.IP_Port + url;
    }
  }
};
</script>
