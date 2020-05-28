<style scoped>
</style>
<template>
  <div style="display:flex">
    <div>
      <toolStyle :style="{height:windowHeight+'px'}"></toolStyle>
    </div>
    <div id="main" style="flex:4;margin-left:60px;overflow:hidden">
      <tinymce id="d1" v-model="data" :other_options="options"></tinymce>
      <iframe id="form_target" name="form_target" style="display:none" frameborder="0"></iframe>
      <form
        id="my_form"
        action="/upload/"
        target="form_target"
        method="post"
        enctype="multipart/form-data"
        style="width:0px;height:0;overflow:hidden"
      >
        <input
          name="image"
          type="file"
          id="choosePicture"
          @change="uploadPhoto($event)"
          accept="image/*"
        />
      </form>
      <Button type="primary" @click="saveModalShow" style="margin-top:20px;margin-left:10px">Save</Button>
    </div>
    <Modal v-model="saveModal" title="Upload txt file" @on-ok="uploadFile">
      <Form :model="mdFile" :label-width="80">
        <FormItem label="name">
          <Input v-model="mdFile.name" placeholder="Enter something..."></Input>
        </FormItem>
        <FormItem label="description">
          <Input v-model="mdFile.description" placeholder="Enter something..."></Input>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>
<script>
import tinymce from "vue-tinymce-editor";
import toolStyle from "./toolStyle";
export default {
  components: { tinymce, toolStyle },
  data() {
    return {
      data: "",
      windowHeight: "",
      saveModal: false,
      mdFile: {
        name: "",
        description: ""
      },
      inster_field_name: "",
      options: {
        width: "100%",
        height: "600",
        autoresize_min_height: 350,
        file_browser_callback: (field_name, url, type, win) => {
          this.inster_field_name = field_name;
          if (type == "image") $("#my_form input").click();
        },
        convert_urls: false
      },
      Editor: null,
      pictureUrl: "",
      pageParams: { pageId: "", userId: "", userName: "" },
      userInfo: {}
    };
  },
  mounted() {
    this.init();
    this.initSize();
    this.getStepInfo();
    this.getUserInfo();
    window.addEventListener("resize", this.initSize);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.initSize);
  },
  methods: {
    initSize() {
      $("#app").css("min-width", "0");
      $("#app").css("min-height", "0");
      this.windowHeight = window.innerHeight;
    },
    init() {
      const self = this;
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
    saveModalShow() {
      this.saveModal = true;
    },
    uploadFile() {
      if (this.pageParams.pageId == undefined || this.pageParams.pageId == "") {
        this.$Message.error("Lose the information of current step.");
        return false;
      }

      var blob = new Blob(['<meta charset="UTF-8">' + this.data], {
        type: " text/plain;charset=UTF-8"
      });
      let fileOfBlob = new File([blob], this.mdFile.name + ".html", {
        type: "text/plain; charset=UTF-8"
      });
      var formData = new FormData();
      formData.append("description", this.mdFile.description);
      formData.append("file", fileOfBlob);
      formData.append("type", "document");
      formData.append("uploaderId", this.userInfo.userId);
      formData.append("privacy", "private");
      formData.append("folderId", this.pageParams.pageId);
      this.axios.defaults.headers = {
        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
      };
      this.axios
        .post("/GeoProblemSolving/folder/uploadToFolder", formData)
        .then(res => {
          if (
            res.data.sizeOver.length > 0 ||
              res.data.failed.length > 0 ||
              res.data == "Offline"
          ) {
            console.log(res.data);
          } else if (res.data.uploaded.length > 0) {
            this.$Notice.open({
              title: "Upload notification title",
              desc: "File uploaded successfully"
              // duration: 0
            });
          }
        })
        .catch(err => {});
    },
    uploadPhoto(e) {
      // 利用fileReader对象获取file
      var file = e.target.files[0];
      var filesize = file.size;
      // 2,621,440   2M
      if (filesize > 2101440) {
        // 图片大于2MB
        this.$Message.error("size > 2MB");
      } else {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = e => {
          $("#choosePicture").val("");
          // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
          let formData = new FormData();
          formData.append("picture", file);
          this.axios
            .post("/GeoProblemSolving/project/picture", formData)
            .then(res => {
              if (res.data != "Fail") {
                this.pictureUrl =
                  "http://" + this.$store.state.IP_Port + res.data;
                document.getElementById(
                  this.inster_field_name
                ).value = this.pictureUrl;
              } else {
                this.$Message.error("upload picture Fail!");
              }
            })
            .catch();
        };
      }
    }
  }
};
</script>
