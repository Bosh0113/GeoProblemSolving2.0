<style scoped>
.modelList {
  height: 80px;
  margin-bottom: 5px;
  padding: 0%;
}
.modelList:hover {
  box-shadow: 0px 0px 3px 1px #2d72f1;
}

.modelTitle {
  /* margin-bottom: 5px; */
}
.modelName {
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.modelDes {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  font-style: italic;
}
.createToolBtn {
  text-align: center;
  margin: 15px 45%;
  font-weight: 800;
  height: 50px;
  font-size: 20px;
}
.selectInput {
  /* height: 60px; */
  font-size: 16px;
  /* background-color: rgb(111, 167, 204); */
}
.inline_style {
  display: flex;
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
  overflow: hidden;
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

/* 结束 */
</style>
<template>
  <div>
    <Row style="margin-bottom:2%;margin-left:20%">
      <Steps :current="currentStep">
        <Step title="Basic information"></Step>
        <Step title="Details"></Step>
      </Steps>
    </Row>
    <Row>
      <Form
        ref="toolInfo"
        :model="toolInfo"
        :rules="toolInfoRule"
        :label-width="110"
        class="toolForm"
      >
        <div v-show="this.currentStep === 0">
          <FormItem label="Name:" prop="toolName">
            <Input v-model="toolInfo.toolName" placeholder="Enter the toolName of your tool"></Input>
          </FormItem>

          <FormItem label="Url:" prop="toolUrl">
            <Input v-model="toolInfo.toolUrl" placeholder="Enter the url of your tool" />
            <p
              style="font-style:italic"
            >If you copy the doi from Open Geographic Modeling System, please enter the ... first</p>
          </FormItem>

          <FormItem label="Step:" prop="recomStep">
            <Select
              v-model="toolInfo.recomStep"
              multiple
              placeholder="Select the recommended step of your tool"
            >
              <Option v-for="item in stepList" :key="item.index" :value="item">{{ item }}</Option>
            </Select>
          </FormItem>

          <FormItem label="Description:" prop="description">
            <Input
              v-model="toolInfo.description"
              type="textarea"
              placeholder="Enter description of your tool"
            />
          </FormItem>

          <FormItem label="tag:" prop="categoryTag">
            <Input
              v-model="inputToolTag"
              placeholder="Enter some tag to classify your tools"
              style="width: 400px"
              @keyup.enter.native="addCreateToolTag(inputToolTag)"
            />
            <Button
              icon="ios-add"
              type="dashed"
              size="small"
              @click="addCreateToolTag(inputToolTag)"
              style="margin-left:2.5%"
            >Add tag</Button>
            <div>
              <Tag
                color="primary"
                v-for="(item, index) in this.toolInfo.categoryTag"
                :key="index"
                closable
                @on-close="deleteCreateToolTag(index)"
              >{{ item }}</Tag>
            </div>
            <div>
              <span>Example:</span>
              <Tag style="cursor:default">vector</Tag>
              <Tag style="cursor:default">raster</Tag>
              <Tag style="cursor:default">evaluation</Tag>
            </div>
          </FormItem>

          <FormItem label="Privacy:" prop="privacy">
            <RadioGroup v-model="toolInfo.privacy">
              <Radio label="Public">Public</Radio>
              <Radio label="Private">Private</Radio>
            </RadioGroup>
          </FormItem>

          <FormItem label="Image:" prop="toolImg">
            <div class="inline_style">
              <div class="demo-upload-list" v-if="toolInfo.toolImg!=''">
                <template>
                  <img :src="toolInfo.toolImg" />
                  <div class="demo-upload-list-cover">
                    <Icon type="ios-eye-outline" @click.native="handleView()"></Icon>
                    <Icon type="ios-trash-outline" @click.native="handleRemove()"></Icon>
                  </div>
                </template>
              </div>
              <div class="uploadBox">
                <Upload
                  ref="upload"
                  :show-upload-list="false"
                  :format="['jpg','jpeg','png', 'gif']"
                  :max-size="2048"
                  :before-upload="handleBeforeUpload"
                  action
                  style="display: inline-block;width:58px;"
                  type="drag"
                >
                  <div style="width: 58px;height:58px;line-height: 58px;">
                    <Icon type="ios-camera" size="20"></Icon>
                  </div>
                </Upload>
              </div>
              <Modal title="View Image" v-model="visible">
                <img :src="toolInfo.toolImg" v-if="visible" style="width: 100%" />
              </Modal>
            </div>
          </FormItem>
        </div>
        <div v-show="this.currentStep === 1">
          <FormItem label="Detail:" prop="detail" :label-width="0">
            <tinymce ref="editor" v-model="toolInfo.detail" :height="300" />
          </FormItem>
        </div>
      </Form>
    </Row>
  </div>
</template>
<script>
import tinymce from "./../tinymce";
import Avatar from "vue-avatar";
import { post } from "../../axios";
export default {
  components: {
    tinymce,
    Avatar
  },
  props: {
    step: {
      type: Number
    }
  },
  data() {
    return {
      toolInfo: {
        toolName: "",
        description: "",
        toolUrl: "",
        recomStep: [],
        categoryTag: [],
        privacy: "Private",
        detail: "",
        toolImg: ""
      },
      stepList: [
        "General step",
        "Context definition & resource collection",
        "Data processing",
        "Data analysis",
        "Data visualization",
        "Geo-analysis model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Decision-making for management"
      ],
      toolInfoRule: {
        toolName: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur"
          }
        ],
        toolUrl: [
          {
            required: true,
            message: "The url cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: true,
            message: "The tool description cannot be empty",
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
      inputToolTag: "",
      visible: false,
      createToolFlag: null,
      pageParams: { pageId: "", userId: "", userName: "" },
      //step
      currentStep: this.step
    };
  },

  methods: {
    addCreateToolTag(tag) {
      if (tag != "") {
        this.toolInfo.categoryTag.push(tag);
        this.inputToolTag = "";
      }
    },

    deleteCreateToolTag(index) {
      this.toolInfo.categoryTag.splice(index, 1);
    },

    async handleBeforeUpload(file) {
      let formData = new FormData();
      formData.append("toolImg", file);
      let data = await post("/GeoProblemSolving/tool/picture", formData);
      this.toolInfo.toolImg = data;
    },

    handleView() {
      this.visible = true;
    },

    handleRemove() {
      this.toolInfo.toolImg = "";
    }
  },

  watch: {
    toolInfo: {
      handler(val) {
        this.$emit("generalInfo", this.toolInfo);
      },
      deep: true
    },
    //监听step切换
    step: {
      handler(val) {
        this.currentStep = val;
      },
      deep: true
    }
  }
};
</script>
