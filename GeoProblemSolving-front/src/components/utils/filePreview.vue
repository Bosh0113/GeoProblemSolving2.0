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
.sidebar{
  width:60px;
  background-color:#515a6e;
  justify-content:center;
  position: absolute
}
.toolContent{
  flex:1;
  height:100%
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
        <span @click="resourceDrawer=true" style="cursor:pointer"><Icon type="md-folder" :size="40" color="white"/></span>
        </span>
        <Drawer title="Resources" :closable="false" v-model="resourceDrawer" placement="left">
        <div style="height:45%;overflow-y:auto">
        <div style="font-size:1rem;margin-bottom:10px">Papers:</div>
        <ul>
          <li v-for="(resource,index) in paperList" :key="index" @click="selectResource(resource.pathURL)" style="cursor:pointer;margin-bottom:10px" :title="resource.description">{{resource.name}}</li>
        </ul>
        </div>
         <div style="height:45%;overflow-y:auto">
        <div style="font-size:1rem;margin-bottom:10px">Documents:</div>
        <ul>
          <li v-for="(resource,index) in documentList" :key="index" @click="selectResource(resource.pathURL)" style="cursor:pointer;margin-bottom:10px" :title="resource.description">{{resource.name}}</li>
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
    <template v-else  class="pfdPanel">
      <div :style="{height:fileHeight+'px', width:fileWidth+'px'}" >
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
  components: { },
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
    this.getResource();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      if(window.innerHeight > 675) {
        this.windowHeight = window.innerHeight;
        this.fileHeight = window.innerHeight;
      }
      else{
        this.windowHeight = 675;
        this.fileHeight = 675;
      }
      if(window.innerWidth >1200){
        this.fileWidth = window.innerWidth - 60;
      }
      else{
        this.fileWidth = 1200;
      }
    },
    getResource() {
      this.paperList = [];
      this.documentList = [];
      let resources = JSON.parse(sessionStorage.getItem("resources"));
      if (resources != null && resources != undefined && resources.length > 0) {
        for (let i = 0; i < resources.length; i++) {
          if (resources[i].type == "paper") {
            this.paperList.push(resources[i]);
          } else if (resources[i].type == "document") {
            this.documentList.push(resources[i]);
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
                if (res.data[i].type == "paper") {
                  that.paperList.push(res.data[i]);
                } else if (res.data[i].type == "document") {
                  that.documentList.push(res.data[i]);
                }
              }
            } else {
              that.paperList = [];
              that.documentList = [];
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    selectFile(url, name) {
      if (!/\.(pdf)$/.test(name.toLowerCase())) {
        this.$Message.error("Worry format");
        return false;
      }
      this.fileURL = url;
    },
    handleUpload(file) {
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
      let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
      formData.append("file", file);
      formData.append("description", "file preview tool");
      formData.append("type", fileType);
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
            let fileName = res.data[0].fileName;
            that.fileURL = "/GeoProblemSolving/resource/upload/" + fileName;

            let fileItem = {
              name: file.name,
              description: "file preview tool",
              pathURL: "/GeoProblemSolving/resource/upload/" + fileName
            };
            if (that.isPaper) {
              that.paperList.push(fileItem);
              that.activeItem = "1-" + (that.paperList.length - 1);
            } else {
              that.documentList.push(fileItem);
              that.activeItem = "2-" + (that.documentList.length - 1);
            }
          }
        })
        .catch(err => {});
      return false;
    },
    selectResource(url){
      this.fileURL = 'http://'+this.$store.state.IP_Port+url;
    }
  }
};
</script>
