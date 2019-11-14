<style scoped>
/* import { try } from 'q'; */
.selector {
  width: 250px;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.fileBtnHoverGray:hover {
  background-color: #808695;
  color: white;
}
.leftMenuItem {
  margin: 0 0 10px 0;
}
.changeGreenColor:hover{
    background-color: #19be6b;
    color: white;
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
  <div :style="{height: contentHeight+'px'}" style="min-width:1200px;">
    <Card dis-hover>
      <h1 slot="title">
        <span style="margin-left:15px">Tools Center</span>
      </h1>
      <div>
        <Row>
          <Col span="16">
          <div span="2" style="height: inherit;width: 90px;position: absolute;" :style="{height: contentHeight-93+'px'}">
            <Menu
              active-name="publicTools"
              @on-select="changeMenuItem"
              style="height: inherit;width: fit-content;z-index:1"
            >
              <MenuItem name="publicTools" class="leftMenuItem">
                <Tooltip content="Public tools" placement="right">
                <Icon type="logo-dropbox" size="35"></Icon>
                </Tooltip>
              </MenuItem>
              <MenuItem name="personalTools" class="leftMenuItem">
                <Tooltip content="Personal tools" placement="right">
                <Icon type="ios-cube" size="35"></Icon>
                </Tooltip>
              </MenuItem>
            </Menu>
          </div>
            <Card dis-hover style="margin-left: 80px">
              <h1 slot="title" style="padding-top:5px;color: #2d8cf099" v-if="showMenuItem=='publicTools'">Public tools</h1>
              <h1 slot="title" style="padding-top:5px;color: #2d8cf099" v-if="showMenuItem=='personalTools'">Personal tools</h1>
            <div slot="extra">
              <Select
                v-model="typeSelected"
                @on-change="filterShowListByType"
                style="width:160px"
              >
                <Option v-for="item in typeOptions" :key="item.index" :value="item">{{ item }}</Option>
              </Select>
              <Button class="changeGreenColor" shape="circle" icon="ios-add-circle-outline" @click="createToolModalShow()">Create tool</Button>
            </div>
              <div v-if="showMenuItem=='publicTools'" style="height: inherit;min-height: fit-content;" :style="{height: contentHeight-187+'px'}">
                <div v-for="tool in publicToolShow" :key="tool.index">
                  <p>{{tool.toolName}}</p>
                </div>
              </div>
              <div v-if="showMenuItem=='personalTools'" style="height: inherit;min-height: fit-content;" :style="{height: contentHeight-187+'px'}">
                <div v-for="tool in personalToolShow" :key="tool.index">
                  <p>{{tool.toolName}}</p>
                </div>
              </div>
            </Card>
          </Col>
          <Col span="8">
            <div style="padding: 0 5px;margin-left: 15px;">
              <Card dis-hover>
                <h1 slot="title" style="padding-top:5px">Toolsets</h1>
                <div slot="extra">
                  <Button class="changeGreenColor" shape="circle" icon="ios-add-circle-outline" @click="createToolsetModalShow()">Create toolset</Button>
                </div>
                <div :style="{height: contentHeight-187+'px'}">
                    <div v-for="toolset in toolsetList" :key="toolset.index">
                      <p>{{toolset.toolsetName}}</p>
                    </div>
                </div>
              </Card>
            </div>
          </Col>
        </Row>
      </div>
    </Card>
    <Modal v-model="createToolModal" title="Create Tool" width="800" :mask-closable="false">
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
        <FormItem label="Tool description:" prop="description" :label-width="140">
          <Input v-model="toolInfo.description" type="textarea" placeholder="Enter description of your tool" />
        </FormItem>
        <FormItem label="Tool url:" prop="tool_url" :label-width="140">
          <Input v-model="toolInfo.tool_url" placeholder="Enter the tool url of your tool"></Input>
        </FormItem>
        <FormItem label="Recommended step:" prop="recomStep" :label-width="140">
          <Select
            v-model="toolInfo.recomStep"
            multiple
            placeholder="Select the recommended step of your tool"
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
      </Form>
      <div slot="footer">
        <Button @click="createToolModal=false">Cancel</Button>
        <Button
          type="success"
          @click="createTool('toolInfo')"
          class="create"
          :disabled="clickForbidden"
        >Create</Button>
      </div>
    </Modal>
    <Modal v-model="createToolsetModal" title="Create Toolset" width="600" :mask-closable="false">
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
        <FormItem label="Description" prop="description" :label-width="140">
          <Input v-model="toolsetInfo.description" type="textarea" placeholder="Enter the description of your toolset"></Input>
        </FormItem>
        <FormItem label="Recommended step:" prop="recomStep" :label-width="140">
          <Select
            v-model="toolsetInfo.recomStep"
            multiple
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
                @change="uploadtoolsetPhoto($event)"
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
            <Radio label="Public">Public</Radio>
            <Radio label="Private">Private</Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="createToolsetModal=false">Cancel</Button>
        <Button
          type="success"
          @click="createToolset('toolsetInfo')"
          class="create"
          :disabled="clickForbidden"
        >Create</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  components: {},
  mounted() {
    this.resizeContent();
    this.getToolsets();
    this.getPublicTools();
    this.getPersonalTools();
  },
  data() {
    return {
      contentHeight: "",
      userInfo: this.$store.getters.userInfo,
      showMenuItem: "publicTools",
      // 待用
      ops: {
        bar: {
          background: "#808695"
        }
      },
      clickForbidden: false,
      inputToolTag: "",
      toolsetList: [],
      publicTools: [],
      publicToolShow:[],
      personalTools: [],
      personalToolShow:[],
      createToolModal:false,
      toolInfo: {
        name: "",
        model_stateId: "",
        model_oid: "",
        model_mdlId: "",
        description:"",
        tool_url: "",
        recomStep: [],
        categoryTag: [],
        toolImg: "",
        privacy: "Private"
      },
      typeSelected:"All",
      typeOptions: [
        "All",
        "General step",
        "Context definition & resource collection",
        "Data processing",
        "Modeling for geographic process",
        "Model evaluation",
        "Quantitative and qualitative analysis",
        "Simulation/Prediction",
        "Visualization & representation",
        "Decision-making & management",
        "Others"
      ],
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
        description: [
          {
            required: true,
            message: "The tool description cannot be empty",
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
      visible: false,
      //表示图片
      image: "",
      createToolsetModal:false,
      inputToolsetTag: "",
      toolsetInfo: {
        name: "",
        description:"",
        recomStep: [],
        toolsetImg:"",
        privacy: "Private",
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
        description: [
          {
            required: true,
            message: "The toolset description cannot be empty",
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
    };
  },
  methods: {
    resizeContent() {
      if (window.innerHeight > 675) {
        this.contentHeight = window.innerHeight - 120;
      } else {
        this.contentHeight = 555;
      }
      window.onresize = () => {
        return (() => {
          this.resizeContent();
        })();
      };
    },
    getToolsets() {
      this.axios
        .get(
          "/GeoProblemSolving/toolset/inquiryAll" +
            "?provider=" +
            this.userInfo.userId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tool fail." });
          } else if (res.data === "None") {
            this.$Notice.error({ desc: "There is no existing toolset" });
          } else {
            this.$set(this, "toolsetList", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getPublicTools(){
      this.axios
        .get(
          "/GeoProblemSolving/tool/inquiry" +
            "?key="+"privacy" +
            "&value="+"Public"
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this.$Notice.error({ desc: "Loading tools fail." });
          } else if (res.data === "None") {
            this.$Notice.error({ desc: "There is no existing tool" });
          } else {
            this.$set(this, "publicTools", res.data);
            var that = this;
            var timer = window.setInterval(function(){
              if(!that.publicTools.length<1&&!that.personalTools.length<1){
                that.filterShowListByType();
                window.clearInterval(timer);
              }
            },10);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getPersonalTools() {
      this.axios
        .get(
          "/GeoProblemSolving/tool/inquiryAll" +
            "?provider=" +
            this.userInfo.userId
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
            this.$set(this, "personalTools", res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    changeMenuItem(name) {
      this.showMenuItem = name;
    },
    createToolModalShow(){
      this.inputToolTag = "";
      this.toolInfo= {
        name: "",
        model_stateId: "",
        model_oid: "",
        model_mdlId: "",
        description:"",
        tool_url: "",
        recomStep: [],
        categoryTag: [],
        toolImg: "",
        privacy: "Private"
      };
      this.createToolModal=true;
      this.clickForbidden = false;
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
                this.createToolModal=false;
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
    addTag(tag) {
      if (tag != "") {
        this.toolInfo.categoryTag.push(tag);
        this.inputToolTag = "";
        this.toolsetInfo.categoryTag.push(tag);
        this.inputToolsetTag = "";
      }
    },
    deleteTag(index) {
      this.toolInfo.categoryTag.splice(index, 1);
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
    },
    createToolsetModalShow() {
      this.image = "";
      this.inputToolsetTag = "";
      this.toolsetInfo= {
        name: "",
        description:"",
        recomStep: [],
        toolsetImg:"",
        privacy: "Private",
        categoryTag: []
      };
      this.createToolsetModal = true;
      this.clickForbidden = false;
    },
    createToolset(toolset) {
      this.$refs[toolset].validate(valid => {
        if (valid) {
          this.clickForbidden = true;

          let createToolsetForm = {};
          createToolsetForm["toolsetName"] = this.toolsetInfo.name;
          createToolsetForm["description"] = this.toolsetInfo.description;
          createToolsetForm["categoryTag"] = this.toolsetInfo.categoryTag;
          createToolsetForm["recomStep"] = this.toolsetInfo.recomStep;
          createToolsetForm["provider"] = this.$store.getters.userId;
          createToolsetForm["toolsetImg"] = this.toolsetInfo.toolsetImg;
          createToolsetForm["privacy"] = this.toolsetInfo.privacy;

          this.axios
            .post("/GeoProblemSolving/toolset/create", createToolsetForm)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data === "Fail") {
                this.$Notice.error({desc: "Create toolset fail."});
              } else if (res.data === "Duplicate naming") {
                this.$Notice.error({desc: "The name already exists."});
              } else {
                console.log(res.data);
                this.createToolsetModal = false;
                this.$Notice.info({desc: "Create successfully"});
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
        }
      });
    },
    uploadtoolsetPhoto(e) {
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
    filterShowListByType(){
      this.publicToolShow = this.getFilterResult(this.publicTools);
      this.personalToolShow = this.getFilterResult(this.personalTools);
    },
    getFilterResult(foreList){
      var selectedType = this.typeSelected;
      var resultList=foreList.filter(function(item){
        switch(selectedType){
          case "All":{
            return item;
            break;
          }
          case "General step":
          case "Context definition & resource collection":
          case "Data processing":
          case "Modeling for geographic process":
          case "Model evaluation":
          case "Quantitative and qualitative analysis":
          case "Simulation/Prediction":
          case "Visualization & representation":
          case "Decision-making & management":{
            var stepTypes = item.recomStep;
            for(var i=0;i<stepTypes.length;i++){
              if(stepTypes[i]==selectedType){
                return item;
                break;
              }
            }
            break;
          }
          case "Others":{
            if(item.recomStep.length<1){
              return item;
            }
            break;
          }
        }
      });
      return resultList;
    },
  }
};
</script>
