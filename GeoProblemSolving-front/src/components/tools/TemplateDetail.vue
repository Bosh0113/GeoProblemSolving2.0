
<template>
  <div>
    <Col span="22" offset="1">
      <tinymce ref="editor" v-model="toolInfo.content" :height="300" />
      <!-- <Button type="success" @click="createTool('toolInfo')" class="create">Create</Button> -->
    </Col>
  </div>
</template>
<script>
import tinymce from "./../../common/tinymce";
export default {
  components: {
    tinymce
  },

  data() {
    return {
      selectModelRoute: this.modelRoute,
      selectModel: this.modelItem,
      selectModelName: "",
      selectModelDes: "",
      selectModelUrl: "",
      toolInfo: {
        toolImg: "",
        content: ""
      },
      userId: "testUserId",
      inputToolTag: "",
      visible: false,
      //表示图片
      image: ""
    };
  },

  methods: {
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.image = "";
      this.toolInfo.toolImg = "";
    },
    uploadPhoto(e) {
      // 利用fileReader对象获取file
      var file = e.target.files[0];
      var filesize = file.size;
      // 2,621,440   2M
      if (filesize > 21014400) {
        // 图片大于2MB,gif动图较大
        this.$Message.error("size > 20MB");
      } else {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = e => {
          // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
          let formData = new FormData();
          formData.append("toolImg", file);
          this.axios
            .post("/GeoProblemSolving/tool/picture", formData)
            .then(res => {
              if (res.data != "Fail") {
                this.toolInfo.toolImg = res.data;
                // this.selectedTool.toolImg = res.data;
                this.image = e.target.result;
                $("#choosePicture").val("");
              } else {
                this.$Message.error("upload picture Fail!");
              }
            })
            .catch();
        };
      }
    }
  },
  props: {
    toolInfoDetail: {
      type: Object
    }
  }
};
</script>
