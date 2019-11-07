<style scoped>
h1 {
  text-align: center;
  margin-top: 2.5%;
}
.toolForm {
  width: 60%;
  margin-top: 0.5%;
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
  margin-top: 5%;
}
.back {
  width: 20%;
  margin-right: 10%;
  margin-left: 20%;
  margin-top: 5%;
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
  <div class="tool form">
    <h1>New Tool</h1>
    <div>
      <Form
        ref="toolInfo"
        :model="toolInfo"
        :rules="toolInfoRule"
        :label-width="80"
        class="toolForm"
      >
        <FormItem label="Name:" prop="name" :label-width="140">
          <Input v-model="toolInfo.name" placeholder="Enter the name of your tool"></Input>
        </FormItem>
        <FormItem label="Model stateId:" prop="model_stateId" :label-width="140">
          <Input
            v-model="toolInfo.model_stateId"
            placeholder="Enter the model stateId of your tool"
          ></Input>
        </FormItem>
        <FormItem label="Model oid:" prop="model_oid" :label-width="140">
          <Input v-model="toolInfo.model_oid" placeholder="Enter the model oid of your tool"></Input>
        </FormItem>
        <FormItem label="Model mdlId:" prop="model_mdlId" :label-width="140">
          <Input v-model="toolInfo.model_mdlId" placeholder="Enter the model mdlId of your tool"></Input>
        </FormItem>
        <FormItem label="Tool description:" prop="toolsetRecords" :label-width="140">
          <Input v-model="toolInfo.description" show-word-limit type="textarea" placeholder="Enter description of your tool" />
        </FormItem>
        <FormItem label="Tool url:" prop="tool_url" :label-width="140">
          <Input v-model="toolInfo.tool_url" placeholder="Enter the tool url of your tool"></Input>
        </FormItem>
        <FormItem label="Recommended step:" prop="recomStep" :label-width="140">
          <Select
            v-model="toolInfo.recomStep"
            multiple
            placeholder="Select the recommended step of yout tool"
          >
            <Option v-for="item in stepList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
        </FormItem>
        <FormItem label="Categroy tag:" prop="categoryTag" :label-width="140">
          <Input
            v-model="inputToolTag"
            placeholder="Enter some tag to classify your tools"
            style="width: 400px"
            @keyup.enter.native="addTag(inputToolTag)"
          />
          <Button
            icon="ios-add"
            type="dashed"
            size="small"
            @click="addTag(inputToolTag)"
            style="margin-left:2.5%"
          >Add tag</Button>
          <div>
            <Tag
              color="primary"
              v-for="(item,index) in this.toolInfo.categoryTag"
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
        <FormItem label="Image:" prop="toolImg" :label-width="140">
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
          <RadioGroup v-model="toolInfo.privacy">
            <Radio label="Public">Public</Radio>
            <Radio label="Private">Private</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem>
          <div class="inline_style">
            <Button
              type="success"
              @click="createTool('toolInfo')"
              class="create"
              :disabled="clickForbidden"
            >Create</Button>
            <Button type="success" @click="Back()" class="back">Back</Button>
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
      inputToolTag: "",
      toolInfo: {
        name: "",
        model_stateId: "",
        model_oid: "",
        model_mdlId: "",
        description:"",
        tool_url: "",
        recomStep: "",
        categoryTag: [],
        toolImg: "",
        privacy: "Private"
      },
      toolInfoRule: {
        name: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur"
          }
        ],
        model_stateId: [
          {
            required: false,
            message: "The state id cannot be empty",
            trigger: "blur"
          }
        ],
        model_oid: [
          {
            required: false,
            message: "The oid cannot be empty",
            trigger: "blur"
          }
        ],
        model_mdlId: [
          {
            required: false,
            message: "The mdl id cannot be empty",
            trigger: "blur"
          }
        ],
        tool_url: [
          {
            required: true,
            message: "The tool url cannot be empty",
            trigger: "blur"
          }
        ],
        privacy: [
          {
            required: true,
            message: "Is this tool can be used by public or not?",
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
      toolsetList: [
      ],
      visible: false,
      //表示图片
      image: ""
    };
  },
  created() {
    // 加入判断，如果未登录自动跳转登录页面
    if (!this.$store.getters.userState) {
      this.$router.push({ name: "Login" });
    }
  },
  mounted() {
    this.getToolsets();
  },
  methods: {
    getToolsets() {
      this.axios
        .get(
          "/GeoProblemSolving/toolset/inquiryAll" +
            "?provider=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            this.$set(this, "toolsetList", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    createTool(tool) {
      this.$refs[tool].validate(valid => {
        if (valid) {
          this.clickForbidden = true;

          let createToolForm = {};
          createToolForm["toolName"] = this.toolInfo.name;
          createToolForm["toolUrl"] = this.toolInfo.tool_url;
          createToolForm["modelInfo"] = {
            stateId: this.toolInfo.stateId,
            oid: this.toolInfo.oid,
            mdlId: this.toolInfo.mdlId
          };
          createToolForm["description"] = this.toolInfo.description;
          createToolForm["recomStep"] = this.toolInfo.recomStep;
          createToolForm["categoryTag"] = this.toolInfo.categoryTag;
          createToolForm["provider"] = this.$store.getters.userId;
          createToolForm["toolImg"] = this.toolInfo.toolImg;
          createToolForm["privacy"] = this.toolInfo.privacy;

          this.axios
            .post("/GeoProblemSolving/tool/create", createToolForm)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data === "Fail") {
                this.$Notice.error({ desc: "Create tool fail." });
              } else if (res.data === "Duplicate naming") {
                this.$Notice.error({ desc: "The name already exists." });
              } else {
                console.log(res.data);
                this.$Notice.info({ desc: "Create successfully" });
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
    submitTool() {},
    addTag(tag) {
      if (tag != "") {
        this.toolInfo.categoryTag.push(tag);
        this.inputToolTag = "";
      }
    },
    deleteTag(index) {
      this.toolInfo.categoryTag.splice(index, 1);
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
          formData.append("toolImg", file);
          this.axios
            .post("/GeoProblemSolving/tool/picture", formData)
            .then(res => {
              if (res.data != "Fail") {
                this.toolInfo.toolImg = res.data;
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
      this.toolInfo.toolImg = "";
    }
  }
};
</script>
