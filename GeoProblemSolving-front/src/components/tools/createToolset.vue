<style scoped>
h1 {
  text-align: center;
  margin-top: 2.5%;
}
.toolsetForm {
  width: 60%;
  margin-top: 2%;
  margin-left: 20%;
  margin-right: 20%;
}
.inline_style {
  display: flex;
}
.create {
  width: 20%;
  margin-right: 20%;
  margin-left: 10%;
  margin-top: 10%;
}
.back {
  width: 20%;
  margin-right: 10%;
  margin-left: 20%;
  margin-top: 10%;
}
/* 上传图片 */
.demo-upload-list {
  display: inline-block;
  width: 60px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  /* overflow-x: hidden; */
  /* overflow-y: scroll; */
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 4px;
}
.demo-upload-list img {
  width: 100%;
  height: 100%;
}
.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
}
.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}
.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
.uploadAvatar {
  position: relative;
  width: 58px;
  height: 58px;
  top: 0;
  left: 0;
  outline: none;
  background-color: transparent;
  opacity: 0;
}
.uploadBox {
  display: inline-block;
  width: 58px;
  height: 58px;
  line-height: 58px;
  border-width: 0.75px;
  border-style: dashed;
  border-color: lightslategray;
}
/* 结束 */
</style>
<template>
  <div class="toolset form">
    <h1>New Toolset</h1>
    <div>
      <Form
        ref="toolsetInfo"
        :model="toolsetInfo"
        :rules="toolsetInfoRule"
        :label-width="80"
        class="toolsetForm"
      >
        <FormItem label="Name" prop="name" :label-width="140">
          <Input v-model="toolsetInfo.name" placeholder="Enter the name of your toolset"></Input>
        </FormItem>
        <FormItem label="Recommended step:" prop="recomStep" :label-width="140">
          <Select
            v-model="toolsetInfo.recomStep"
            placeholder="Select the recommended step of yout toolset"
          >
            <Option v-for="item in stepList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
        </FormItem>
        <FormItem label="Categroy tag:" prop="categoryTag" :label-width="140">
          <Input
            v-model="inputToolsetTag"
            placeholder="Enter some tag to classify your toolset"
            style="width: 400px"
            @keyup.enter.native="addTag(inputToolsetTag)"
          />
          <Button
            icon="ios-add"
            type="dashed"
            size="small"
            @click="addTag(inputToolsetTag)"
            style="margin-left:2.5%"
          >Add tag</Button>
          <div>
            <Tag
              color="primary"
              v-for="(item,index) in toolsetInfo.categoryTag"
              :key="index"
              closable
              @on-close="deleteTag(index)"
            >{{item}}</Tag>
          </div>
          <div>
            <span>Example:</span>
            <Tag style="cursor:default">vector</Tag>
            <Tag style="cursor:default">raster</Tag>
            <Tag style="cursor:default">evaluation</Tag>
          </div>
        </FormItem>
        <FormItem label="Image:" prop="toolsetImg" :label-width="140">
          <div class="inline_style">
            <div class="demo-upload-list" v-if="image!=''">
              <template>
                <img :src="image" />
                <div class="demo-upload-list-cover">
                  <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                  <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
                </div>
              </template>
            </div>
            <div class="uploadBox">
              <Icon type="ios-camera" size="20" style="position:absolute;margin:18px;"></Icon>
              <input
                id="choosePicture"
                @change="uploadPhoto($event)"
                type="file"
                class="uploadAvatar"
                accept="image/*"
              />
            </div>
            <br />
            <Modal title="View Image" v-model="visible">
              <img :src="image" v-if="visible" style="width: 100%" />
            </Modal>
          </div>
        </FormItem>
        <FormItem label="Privacy:" prop="privacy" :label-width="140">
          <RadioGroup v-model="toolsetInfo.privacy">
            <Radio label="public">Public</Radio>
            <Radio label="private">Private</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem>
          <div class="inline_style">
            <Button
              type="success"
              @click="createToolset('toolInfo')"
              class="create"
              :disabled="clickForbidden"
            >Create</Button>
            <Button type="success" @click="Back()" class="back" >Back</Button>
          </div>
        </FormItem>
      </Form>
    </div>
  </div>
</template>
<script>
export default {
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      $.ajax({
        url: "/GeoProblemSolving/user/state",
        type: "POST",
        async: false,
        success: function(data) {
          if (!data) {
            vm.$store.commit("userLogout");
            vm.$router.push({ name: "Login" });
          }
        },
        error: function(err) {
          console.log("Get user state fail.");
        }
      });
    });
  },
  data() {
    return {
      // 控制按钮禁用的
      clickForbidden: false,
      inputToolsetTag: "",
      toolsetInfo: {
        name: "",
        recomStep: "",
        toolsetImg:"",
        privacy: "private",
        categoryTag: []
      },
      toolsetInfoRule: {
        name: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur"
          }
        ],
        recomStep: [
          {
            required: true,
            message: "Please select the recommended step",
            trigger: "blur"
          }
        ],
        privacy: [
          {
            required: true,
            message: "Is this toolset can be used by public or not?",
            trigger: "change"
          }
        ]
      },
      stepList: [
        "General step",
        "Context definition & resource collection",
        "Data processing",
        "Modeling for geographic process",
        "Model evaluation",
        "Quantitative and qualitative analysis",
        "Simulation/Prediction",
        "Visualization & representation",
        "Decision-making & management"
      ],
      visible: false,
      //表示图片
      image: ""
    };
  },
  methods: {
    createTool(toolset) {
      this.$refs[toolset].validate(valid => {
        if (valid) {
          this.clickForbidden = true;

          let createToolsetForm = {};
          createToolsetForm["toolsetName"] = this.toolsetInfo.name;
          createToolsetForm["toolList"] = [];
          createToolsetForm["categoryTag"] = this.toolsetInfo.categoryTag;
          createToolsetForm["recomStep"] = this.toolsetInfo.recomStep;
          createToolsetForm["provider"] = this.$store.getters.userId;
          createToolForm["toolsetImg"] = this.toolsetInfo.toolsetImg;
          createToolsetForm["privacy"] = this.toolsetInfo.privacy;

          this.axios
            .post("/GeoProblemSolving/toolset/create", createToolsetForm)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data === "Fail") {
                this.$Message.error("Create toolset fail.");
              } else if (res.data === "Duplicate naming") {
                this.$Message.error("The name already exists.");
              } else {
                // this.createToolsetId = res.data;
                // this.addHistoryEvent(this.createToolsetId);
                this.$Message.error("Create successfully");
                // this.$router.push({ name: "toolCollection" });
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
        }
      });
    },
    Back() {
      this.$router.go(-1);
    },
    submitToolset() {},
    addTag(tag) {
      if (tag != "") {
        this.toolsetInfo.categoryTag.push(tag);
        this.inputTag = "";
      }
    },
    deleteTag(index) {
      this.toolsetInfo.categoryTag.splice(index, 1);
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
          // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
          let formData = new FormData();
          formData.append("toolsetImg", file);
          this.axios
            .post("/GeoProblemSolving/toolset/picture", formData)
            .then(res => {
              if (res.data != "Fail") {
                this.toolsetInfo.toolsetImg = res.data;
                this.image = e.target.result;
                $("#choosePicture").val("");
              } else {
                this.$Message.error("upload picture Fail!");
              }
            })
            .catch();
        };
      }
    },
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.image = "";
      this.toolsetInfo.toolsetImg = "";
    }
  },
  created() {
    // 加入判断，如果未登录自动跳转登录页面
    if (!this.$store.getters.userState) {
      this.$router.push({ name: "Login" });
    }
  }
};
</script>
