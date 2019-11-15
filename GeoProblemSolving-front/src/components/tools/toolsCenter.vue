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

.btnHoverBlue:hover {
  background-color: #2db7f5;
  color: white;
}
.btnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.btnHoverOrange:hover {
  background-color: #ff9900;
  color: white;
}
.btnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
.btnHoverGray:hover {
  background-color: #808695;
  color: white;
}
.ellipsis{
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: top;
}
.toolList>>>.ivu-card-head{
  padding: 8px 6px;
}
.toolList>>>.ivu-card-extra{
  top:6px;
  right:5px;
}
.toolList>>>.ivu-card-body{
  padding: 5px;
}
.toolDescription {
  overflow: hidden;
  text-overflow:ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
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
              <Button class="btnHoverGreen" shape="circle" icon="ios-add-circle-outline" @click="createToolModalShow()">Create tool</Button>
            </div>
              <div v-if="showMenuItem=='publicTools'" style="height: inherit;min-height: fit-content;" :style="{height: contentHeight-187+'px'}" class="toolList">
                <vue-scroll :ops="ops">
                <Row>
                <Col span="8" v-for="tool in publicToolShow" :key="tool.index">
                  <div style="margin:0 5px 15px 5px">
                    <Card style="background-color: ghostwhite;">
                      <p slot="title" class="ellipsis" style="width:75%;display:inline-block;" :title="tool.toolName">{{tool.toolName}}</p>
                      <div slot="extra">
                        <Button icon="md-eye" shape="circle" class="btnHoverGray" title="Preview" size="small" @click="showTool(tool)"></Button>
                        <Button icon="md-share-alt" shape="circle" type="success" title="Add to toolset" size="small" @click="addToToolsetShow(tool)"></Button>
                      </div>
                      <div>
                        <div style="display: inline-block;algin:left;">
                            <img :src="tool.toolImg" v-if="tool.toolImg!=''" style="height:100%;max-height:50px;">
                            <avatar
                            :username="tool.toolName"
                            :size="50"
                            style="margin-bottom:6px"
                            v-else
                          ></avatar>
                        </div>
                        <div style="display: inline-block;vertical-align: top;width: 70%;" class="ellipsis">
                          <strong :title="tool.toolName">{{tool.toolName}}</strong>
                          <br/>
                          <span :title="tool.categoryTag.join('|')"><i>{{tool.categoryTag.join('|')}}</i></span>
                        </div>
                        <div style="width:100%;height: 65px;border: 0.5px dashed #8080804d;padding: 1px 3px;">
                          <p class="toolDescription" :title="tool.description">{{tool.description}}</p>
                        </div>
                      </div>
                    </Card>
                  </div>
                </Col>
                </Row>
                </vue-scroll>
              </div>
              <div v-if="showMenuItem=='personalTools'" style="height: inherit;min-height: fit-content;" :style="{height: contentHeight-187+'px'}" class="toolList">
                <vue-scroll :ops="ops">
                <Row>
                <Col span="8" v-for="tool in personalToolShow" :key="tool.index">
                  <div style="margin:0 5px 15px 5px">
                    <Card style="background-color: #faebd75c">
                      <p slot="title" class="ellipsis" style="width:50%;display:inline-block;" :title="tool.toolName">{{tool.toolName}}</p>
                      <div slot="extra">
                        <Button icon="md-eye" shape="circle" class="btnHoverGray" title="Preview" size="small" @click="showTool(tool)"></Button>
                        <Button icon="ios-create" shape="circle" class="btnHoverBlue" title="Edit" size="small" @click="editToolShow(tool)"></Button>
                        <Button icon="md-close" shape="circle" class="btnHoverRed" title="Delete" size="small" @click="removeToolShow(tool)"></Button>
                        <Button icon="md-share-alt" shape="circle" type="success" title="Add to toolset" size="small" @click="addToToolsetShow(tool)"></Button>
                      </div>
                      <div>
                        <div style="display: inline-block;algin:left;">
                            <img :src="tool.toolImg" v-if="tool.toolImg!=''" style="height:100%;max-height:50px;">
                            <avatar
                            :username="tool.toolName"
                            :size="50"
                            style="margin-bottom:6px"
                            v-else
                          ></avatar>
                        </div>
                        <div style="display: inline-block;vertical-align: top;width: 70%;" class="ellipsis">
                          <strong :title="tool.toolName">{{tool.toolName}}</strong>
                          <br/>
                          <span :title="tool.categoryTag.join('|')"><i>{{tool.categoryTag.join('|')}}</i></span>
                        </div>
                        <div style="width:100%;height: 65px;border: 0.5px dashed #8080804d;padding: 1px 3px;">
                          <p class="toolDescription" :title="tool.description">{{tool.description}}</p>
                        </div>
                      </div>
                    </Card>
                  </div>
                </Col>
                </Row>
                </vue-scroll>
              </div>
            </Card>
          </Col>
          <Col span="8">
            <div style="padding: 0 5px;margin-left: 15px;">
              <Card dis-hover>
                <h1 slot="title" style="padding-top:5px">Toolsets</h1>
                <div slot="extra">
                  <Button class="btnHoverGreen" shape="circle" icon="ios-add-circle-outline" @click="createToolsetModalShow()">Create toolset</Button>
                </div>
                <div :style="{height: contentHeight-187+'px'}">
                <vue-scroll :ops="ops">
                  <List>
                    <ListItem v-for="toolset in toolsetList" :key="toolset.index">
                    <ListItemMeta 
                      :avatar="toolset.toolsetImg" 
                      :title="toolset.toolsetName" 
                      :description="toolset.description" />
                    <template slot="action">
                        <li>
                            <a href="">Edit</a>
                        </li>
                        <li>
                            <Button icon="md-close" shape="circle" class="btnHoverRed" title="Delete" size="small" @click="removeToolsetShow(toolset)"></Button>
                        </li>
                    </template>
                    </ListItem>
                  </List>
                </vue-scroll>
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
    <Modal v-model="editToolModal" title="Edit Tool" width="800" :mask-closable="false">
      <Form
        ref="selectedTool"
        :model="selectedTool"
        :rules="toolEditRule"
        :label-width="80"
        class="toolForm"
      >
        <FormItem label="Name:" prop="toolName" :label-width="140">
          <Input v-model="selectedTool.toolName" placeholder="Enter the name of your tool"></Input>
        </FormItem>
        <FormItem label="Model stateId:" prop="model_stateId" :label-width="140">
          <Input
            v-model="selectedTool.modelInfo.stateId"
            placeholder="Enter the model stateId of your tool"
          ></Input>
        </FormItem>
        <FormItem label="Model oid:" prop="model_oid" :label-width="140">
          <Input v-model="selectedTool.modelInfo.oid" placeholder="Enter the model oid of your tool"></Input>
        </FormItem>
        <FormItem label="Model mdlId:" prop="model_mdlId" :label-width="140">
          <Input v-model="selectedTool.modelInfo.mdlId" placeholder="Enter the model mdlId of your tool"></Input>
        </FormItem>
        <FormItem label="Tool description:" prop="description" :label-width="140">
          <Input v-model="selectedTool.description" type="textarea" placeholder="Enter description of your tool" />
        </FormItem>
        <FormItem label="Tool url:" prop="toolUrl" :label-width="140">
          <Input v-model="selectedTool.toolUrl" placeholder="Enter the tool url of your tool"></Input>
        </FormItem>
        <FormItem label="Recommended step:" prop="recomStep" :label-width="140">
          <Select
            v-model="selectedTool.recomStep"
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
              v-for="(item,index) in this.selectedTool.categoryTag"
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
          </div>
        </FormItem>
        <FormItem label="Privacy:" prop="privacy" :label-width="140">
          <RadioGroup v-model="selectedTool.privacy">
            <Radio label="Public">Public</Radio>
            <Radio label="Private">Private</Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="editToolModal=false">Cancel</Button>
        <Button
          type="primary"
          @click="editTool('selectedTool')"
          class="create"
        >Edit</Button>
      </div>
    </Modal>
    <Modal v-model="removeToolModal" title="Remove Tool" width="400">
      <div style="text-align: center;">
        <h3>Are you sure to remove the tool: {{selectedTool.toolName}} ?</h3>
        <p style="color:red">This operation is irreversible!</p>
      </div>
      <div slot="footer">
        <Button @click="removeToolModal=false">Cancel</Button>
        <Button
          type="error"
          @click="removeTool()"
          class="create"
        >Remove</Button>
      </div>
    </Modal>
    <Modal v-model="removeToolsetModal" title="Remove Toolset" width="400">
      <div style="text-align: center;">
        <h3>Are you sure to remove the toolset: {{selectedToolset.toolsetName}} ?</h3>
        <p style="color:red">This operation is irreversible!</p>
      </div>
      <div slot="footer">
        <Button @click="removeToolsetModal=false">Cancel</Button>
        <Button
          type="error"
          @click="removeToolset()"
          class="create"
        >Remove</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import draggable from "vuedraggable";
import contextDefinitionVue from '../workingSpace/contextDefinition.vue';
export default {
  components: {
    draggable,
    Avatar
  },
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
      selectedTool:{
        toolName: "",
        modelInfo:{
          stateId: "",
          oid: "",
          mdlId: "",
        },
        description:"",
        toolUrl: "",
        recomStep: [],
        categoryTag: [],
        toolImg: "",
        privacy: ""
      },
      toolEditRule: {
        toolName: [
          {
            required: true,
            message: "The name cannot be empty",
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
        toolUrl: [
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
      editToolModal:false,
      removeToolModal:false,
      selectedToolset:false,
      removeToolsetModal:false
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
            stateId: this.toolInfo.model_stateId,
            oid: this.toolInfo.model_oid,
            mdlId: this.toolInfo.model_mdlId
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
                this.createToolModal=false;
                this.$Notice.info({ desc: "Create successfully" });
                this.personalTools.push(res.data);
                if(this.toolInfo.privacy=="Public"){
                  this.publicTools.push(res.data);
                }
                this.filterShowListByType();
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
    showTool(toolInfo){
        this.axios
          .post("/GeoProblemSolving/user/state")
          .then(res => {
            if (!res.data) {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              let toolURL = '<iframe src="' + toolInfo.toolUrl +"?groupID="+ this.stepId + '" style="width: 100%;height:100%;"></iframe>';
              var demoPanelTimer= null;
              let panel = jsPanel.create({
                theme: "success",
                headerTitle: toolInfo.toolName,
                footerToolbar: '<p style="height:10px"></p>',
                contentSize: "1200 600",
                content: toolURL,
                disableOnMaximized: true,
                dragit: {
                  containment: 5
                },
                id:"demoPanel",
                onclosed:function(panel, status, closedByUser){
                  window.clearTimeout(demoPanelTimer);
                },
                callback: function() {
                  var that = this;
                  demoPanelTimer = window.setInterval(function(){
                    that.style.zIndex = "9999";
                  },1);
                }
              });
              // panel.resizeit("disable");
              $(".jsPanel-content").css("font-size", "0");
            }
          })
          .catch(err => {
            console.log("Get user info fail.");
          });
    },
    editToolShow(tool){
      this.selectedTool = tool;
      this.editToolModal = true;
    },
    editTool(tool) {
      this.$refs[tool].validate(valid => {
        if (valid) {

          let editToolForm = new URLSearchParams();
          editToolForm.append("tId",this.selectedTool.tId);
          editToolForm.append("toolName",this.selectedTool.toolName);
          editToolForm.append("toolUrl",this.selectedTool.toolUrl);
          editToolForm.append("modelInfo.stateId",this.selectedTool.modelInfo.stateId);
          editToolForm.append("modelInfo.oid",this.selectedTool.modelInfo.oid);
          editToolForm.append("modelInfo.mdlId",this.selectedTool.modelInfo.mdlId);
          editToolForm.append("description",this.selectedTool.description);
          editToolForm.append("recomStep",this.selectedTool.recomStep);
          editToolForm.append("categoryTag",this.selectedTool.categoryTag);
          editToolForm.append("toolImg",this.selectedTool.toolImg);
          editToolForm.append("privacy",this.selectedTool.privacy);

          this.axios
            .post("/GeoProblemSolving/tool/update", editToolForm)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data === "Fail") {
                this.$Notice.error({ desc: "Edit tool fail." });
              } else {
                this.editToolModal=false;
                var newToolInfo = this.selectedTool;
                for(var i=0;i<this.publicTools.length;i++){
                  if(this.publicTools[i].tId==newToolInfo.tId){
                    this.publicTools.splice(i,1,newToolInfo);
                    break;
                  }
                }
                for(var i=0;i<this.personalTools.length;i++){
                  if(this.personalTools[i].tId==newToolInfo.tId){
                    this.personalTools.splice(i,1,newToolInfo);
                    break;
                  }
                }
                this.filterShowListByType();
                this.$Notice.info({ desc: "Edit successfully" });
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
        }
      });
    },
    removeToolShow(tool){
      this.selectedTool = tool;
      this.removeToolModal = true;
    },
    removeTool(){
      this.axios
          .get(
            "/GeoProblemSolving/tool/delete"+
            "?tId="+
            this.selectedTool.tId
          )
          .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data === "Fail") {
                this.$Notice.error({ desc: "Remove tool fail." });
              } else {
                this.removeToolModal = false;
                var removedTool = this.selectedTool;
                for(var i=0;i<this.publicTools.length;i++){
                  if(this.publicTools[i].tId==removedTool.tId){
                    this.publicTools.splice(i,1);
                    break;
                  }
                }
                for(var i=0;i<this.personalTools.length;i++){
                  if(this.personalTools[i].tId==removedTool.tId){
                    this.personalTools.splice(i,1);
                    break;
                  }
                }
              }
              this.filterShowListByType();
              this.$Notice.info({ desc: "The tool("+ removedTool.toolName+") has been removed."});
          })
          .cathc(err=>{
            console.log(err.data);
          })
    },
    removeToolsetShow(toolset){
      this.selectedToolset = toolset;
      this.removeToolsetModal=true;
    },
    removeToolset(){
      this.axios
          .get(
            "/GeoProblemSolving/toolset/delete"+
            "?tsId="+
            this.selectedToolset.tsId
          )
          .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data === "Fail") {
                this.$Notice.error({ desc: "Remove toolset fail." });
              } else {
                this.removeToolsetModal = false;
                var removedToolset = this.selectedToolset;
                for(var i=0;i<this.toolsetList.length;i++){
                  if(this.toolsetList[i].tsId==removedToolset.tsId){
                    this.toolsetList.splice(i,1);
                    break;
                  }
                }
              this.$Notice.info({ desc: "The toolset("+ removedToolset.toolsetName+") has been removed."});
              }
          })
          .catch(err=>{
            console.log(err.data);
          })
    },
    addToToolsetShow(){
      
    }
  }
};
</script>
