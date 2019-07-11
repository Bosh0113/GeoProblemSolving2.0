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
.sidebar{
  width:60px;
  background-color:#515a6e;
  justify-content:center;
  position: absolute
}
</style>
<template>
  <div>
    <div class="sidebar" style="width:60px;float:left" :style="{height:windowHeight+'px'}">      
      <div style="display:flex;justify-content:center;margin-top:20px" title="Resources">
        <span @click="resourceDrawer=true" style="cursor:pointer"><Icon type="md-folder" :size="40" color="white"/></span>
        </span>
        <Drawer title="Resources" :closable="false" v-model="resourceDrawer" placement="left">
        <div style="height:45%;overflow-y:auto">
          <ul>
            <li v-for="(resource,index) in videoList" :key="index" @click="selectVideo(resource.pathURL)" style="cursor:pointer;margin-bottom:10px" :title="resource.description">{{resource.name}}</li>
          </ul>
        </div>
        </Drawer>
      </div>
    </div>
    <div :style="{height:windowHeight+'px'}" style="float:left;padding-left:100px;padding-top:40px">
      <video
        :width="videoWidth"
        :height="videoHeight"
        :src="videoUrl"
        controls
        class="videoPanel"
      >
      </video>
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
      activeItem:0,
      resourceDrawer:false,
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
    this.getVideoResource();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      if(window.innerHeight > 675){
        this.windowHeight = window.innerHeight;
      }
      else{
        this.windowHeight = 675;
      }
      this.videoWidth = window.innerWidth - 80 - 60;
      this.videoHeight = window.innerHeight - 80;
    },
    getVideoResource() {
      this.videoList = [];
      let resources = JSON.parse(sessionStorage.getItem("resources"));
      if (resources != null && resources != undefined && resources.length > 0) {
        for (let i = 0; i < resources.length; i++) {
          if (resources[i].type == "video") {
            this.videoList.push(resources[i]);
          }
        }
      } else {
        var that = this;
        this.axios
          .get(
            "/GeoProblemSolving/resource/inquiry" +
              "?key=scope.moduleId" +
              "&value=" +
              sessionStorage.getItem("moduleId")
          )
          .then(res => {
            // 写渲染函数，取到所有资源
            if (res.data !== "None") {              
              for (let i = 0; i < res.data.length; i++) {
                if (res.data[i].type == "video") {
                  that.videoList.push(res.data[i]);
                }
              }
            } else {
              that.videoList = [];
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }      
    },
    selectVideo(url) {
      this.videoUrl = url;
    },
    handleUpload(file) {
      if (!/\.(mp4)$/.test(file.name.toLowerCase())) {
        this.$Message.error("上传格式不正确，请上传xls或者xlsx格式");
        return false;
      }

      //上传数据
      let formData = new FormData();
      let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      formData.append("file", file);
      formData.append("description", "video viewer tool");
      formData.append("type", "video");
      formData.append("uploaderId", userInfo.userId);
      formData.append("belong", userInfo.userName);
      let scopeObject = {
        projectId: "",
        subProjectId: "",
        moduleId: sessionStorage.getItem("moduleId")
      };
      formData.append("scope", JSON.stringify(scopeObject));
      formData.append("privacy", "private");
      let that = this;
      this.axios
        .post("/GeoProblemSolving/resource/upload", formData)
        .then(res => {
          if(res.data == "Size over"||res.data == "Fail"||res.data == "Offline"){
            console.log(res.data);
          }
          else if (res.data.length > 0) {
            let videoName = res.data[0].fileName;
            that.videoUrl = "/GeoProblemSolving/resource/upload/" + videoName;

            let videoItem = {
              name: file.name,
              description: "video viewer tool",
              pathURL: "/GeoProblemSolving/resource/upload/" + videoName
            };
            that.videoList.push(videoItem);
            that.activeItem = that.videoList.length - 1;
          }
        })
        .catch(err => {});
      return false;
    }
  }
};
</script>
