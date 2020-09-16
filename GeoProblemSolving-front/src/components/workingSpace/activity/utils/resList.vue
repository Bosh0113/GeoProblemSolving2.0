<style scoped>
.table table {
  table-layout: auto;
  width: 100% !important;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
  color: white;
}
.fileBtnHoverBlue:hover {
  background-color: dodgerblue;
  color: white;
}
.fileBtnHoverRed:hover {
  background-color: #ed4014;
  color: white;
}
.dataInfo {
  margin: 5px 0;
}
.dataLabel {
  width: 90px;
  display: inline-block;
  font-size: 13px;
  font-weight: bold;
  vertical-align: top;
  color: dodgerblue;
}
.dataContent {
  width: 180px;
  display: inline-block;
  padding-left: 10px;
}
.dataText {
  padding-left: 10px;
  display: inline-block;
  word-break: break-word;
  width: 470px;
}
.toolDataLabel {
  font-weight: bold;
  vertical-align: top;
  color: dodgerblue;
}
.toolDataText {
  width: 100px;
  word-break: break-word;
  /* display: inline-block; */
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
#toolData {
  width: 100%;
  height: 100;
  border: 1px solid #dcdee2;
}
#toolDataHeader {
  font-size: 12px;
  font-weight: bold;
  padding: 10px 15px;
  border-bottom: 1px solid #e8eaec;
  background-color: #f8f8f9;
}
.resCard {
    height: calc(100vh - 120px);
}
.resCard >>> .ivu-card-head {
    padding: 6px 16px;
}
</style>
<template>
  <div>
    <Card class="resCard" dis-hover>
      <div slot="title">
        <Icon
          v-if="dataListStyle"
          type="md-list"
          size="24"
          @click="dataListStyle = false"
          title="Resources list style"
          style="margin-right:10px; cursor: pointer"
        />
        <Icon
          v-else
          type="md-apps"
          size="24"
          @click="dataListStyle = true"
          title="Resources list style"
          style="margin-right:10px; cursor: pointer"
        />
        <Button
          v-if="userRole != 'Visitor'"
          shape="circle"
          icon="md-shuffle"
          @click="getPreviousRes()"
          style="margin-right:10px"
          title="Get resources from the previous activities"
        ></Button>
        <Button
          v-if="userRole != 'Visitor'"
          shape="circle"
          icon="md-cloud-upload"
          @click="dataUploadModalShow"
          title="Upload resources"
        ></Button>
      </div>
      <Label>Select:</Label>
      <Select
        v-model="resouceModel"
        size="small"
        @on-change="changeResModel"
        style="width:150px;margin:5px 10px"
      >
        <Option value="resources">All resources</Option>
        <Option value="data">Data</Option>
        <Option value="materials">Other resources</Option>
        <Option value="toolData">Results</Option>
      </Select>
      <div style="display: flex; justify-content: space-between;">
        <div style="width:99%; height:100" v-if="dataListStyle">
          <Table
            :columns="tableColName"
            :data="fileList"
            class="table"
            v-show="fileList!=[] && fileList!='None'"
            height="400"
            no-data-text="No resource"
          >
            <template slot-scope="{ row }" slot="name">
              <strong>{{ row.name }}</strong>
            </template>
            <template slot-scope="{ row }" slot="action">
              <Button
                class="fileBtnHoverGreen"
                size="small"
                title="Check"
                @click="checkData(row)"
                icon="md-eye"
                shape="circle"
                type="text"
              ></Button>
              <template
                v-if="permissionIdentity(activityInfo.permission, 'use_resource') || (permissionIdentity(activityInfo.permission, 'upload_resource') && item.uploaderId == userInfo.userId)"
              >
                <a :href="row.pathURL" :download="row.name" title="Download">
                  <Button
                    class="fileBtnHoverBlue"
                    size="small"
                    shape="circle"
                    type="text"
                    icon="md-download"
                    title="Download"
                  ></Button>
                </a>
                <Button
                  class="fileBtnHoverRed"
                  size="small"
                  shape="circle"
                  type="text"
                  icon="md-close"
                  title="Remove"
                  @click="deleteResourceModalShow(row.resourceId)"
                ></Button>
              </template>
            </template>
          </Table>
        </div>
        <div id="toolData" v-else>
          <Card style="width:30%; height:150px; float:left; margin:5px" v-if="fileList.length == 0">
            <div>There is no resource.</div>
          </Card>
          <vue-scroll :ops="ops" style="height: calc(100vh - 200px)">
            <div v-for="(item,index) in fileList" :key="index">
              <Card style="width:150px; height:150px; float:left; margin:5px">
                <div style="float:left">
                  <template v-if="item.thumbnail == ''||item.thumbnail == undefined">
                    <img
                      v-if="item.type == 'data'"
                      :src="dataUrl"
                      height="72px"
                      width="72px"
                      title="Data"
                    />
                    <img
                      v-else-if="item.type == 'model'"
                      :src="modelUrl"
                      height="72px"
                      width="72px"
                      title="Model"
                    />
                    <img
                      v-else-if="item.type == 'paper'"
                      :src="paperUrl"
                      height="72px"
                      width="72px"
                      title="Paper"
                    />
                    <img
                      v-else-if="item.type == 'document'"
                      :src="documentUrl"
                      height="72px"
                      width="72px"
                      title="Document"
                    />
                    <img
                      v-else-if="item.type == 'image'"
                      :src="imageUrl"
                      height="72px"
                      width="72px"
                      title="Image"
                    />
                    <img
                      v-else-if="item.type == 'video'"
                      :src="videoUrl"
                      height="72px"
                      width="72px"
                      title="Video"
                    />
                    <img
                      v-else-if="item.type == 'others'"
                      :src="otherUrl"
                      height="72px"
                      width="72px"
                      title="Others"
                    />
                  </template>
                  <template v-else>
                    <img :src="item.thumbnail" height="118px" width="118px" />
                  </template>
                </div>
                <div style="float:left;margin: 0 10px">
                  <div>
                    <div class="toolDataText" :title="item.name">{{item.name}}</div>
                  </div>
                  <!-- <div>
                    <Label class="toolDataLabel">Description:</Label>
                    <div class="toolDataText" :title="item.description">{{item.description}}</div>
                  </div> -->
                  <div>
                    <Button
                      size="small"
                      title="Check"
                      icon="md-eye"
                      style="margin: 10px 20px 0 0;"
                      @click="checkData(item)"
                    ></Button>
                    <a :href="item.pathURL" :download="item.name">
                      <Button
                        v-if="permissionIdentity(activityInfo.permission, 'use_resource')"
                        size="small"
                        title="Download"
                        icon="md-download"
                        style="margin: 10px 20px 0 0;"
                      ></Button>
                    </a>
                    <Button
                      v-if="permissionIdentity(activityInfo.permission, 'manage_resource') || (permissionIdentity(activityInfo.permission, 'upload_resource') && item.uploaderId == userInfo.userId)"
                      size="small"
                      title="Delete"
                      icon="md-close"
                      style="margin-top: 10px;"
                      @click="deleteResourceModalShow(item.resourceId)"
                    ></Button>
                  </div>
                </div>
              </Card>
            </div>
          </vue-scroll>
        </div>
      </div>
    </Card>
    <Modal
      v-model="checkDataModal"
      title="Data Information"
      width="600"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <Tabs>
        <TabPane label="Information" name="metadata" icon="md-home">
          <div style>
            <div class="dataInfo">
              <Label class="dataLabel">Name:</Label>
              <span class="dataText">{{selectData.name}}</span>
            </div>
            <div class="dataInfo">
              <Label class="dataLabel">Type:</Label>
              <span class="dataContent">{{selectData.type}}</span>
              <Label class="dataLabel">Provider:</Label>
              <span class="dataContent">{{selectData.uploaderName}}</span>
            </div>
            <div class="dataInfo">
              <Label class="dataLabel">File size:</Label>
              <span class="dataContent">{{selectData.fileSize}}</span>
              <Label class="dataLabel">Creation time:</Label>
              <span class="dataContent">{{selectData.uploadTime}}</span>
            </div>
            <div class="dataInfo">
              <Label class="dataLabel">Description:</Label>
              <span class="dataText">{{selectData.description}}</span>
            </div>
          </div>
        </TabPane>
        <!-- <TabPane label="UDX Schema" name="udx" icon="md-browsers">
          <pre class="brush: html"></pre>
          <template>...</template>
        </TabPane>-->
      </Tabs>
      <br />
      <!-- <Button style="margin-right:20px" @click="dataPreview(selectData)">Preview</Button>
      <Button style="margin-right:20px" @click="dataVisualize">Visualization</Button>-->
    </Modal>
    <Modal
      width="800px"
      v-model="inheritResModal"
      title="Get resources from previous activities"
      :styles="{top: '20px'}"
      @on-ok="saveResources()"
      ok-text="Save"
      cancel-text="Cancel"
    >
      <div style="margin-left:75px">
        <div style="font-size:14px">Select the needed data:</div>
        <Transfer
          :data="existingResources"
          :target-keys="targetKeys"
          :list-style="listStyle"
          :render-format="resourceRender"
          :titles="['The previous activities', 'The new activity']"
          filter-placeholder="Enter key words..."
          filterable
          :filter-method="filterMethod"
          @on-change="handleChange"
        ></Transfer>
      </div>
    </Modal>
    <Modal v-model="dataUploadModal" title="Upload data" width="600">
      <Form
        ref="uploadDataInfo"
        :model="uploadDataInfo"
        :rules="uploadDataRule"
        :label-width="100"
        label-position="left"
      >
        <FormItem label="Privacy" prop="privacy">
          <RadioGroup v-model="uploadDataInfo.privacy" style="width:80%">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="Type" prop="type">
          <RadioGroup v-model="uploadDataInfo.type">
            <Radio label="data"></Radio>
            <Radio label="paper"></Radio>
            <Radio label="document"></Radio>
            <Radio label="model"></Radio>
            <Radio label="image"></Radio>
            <Radio label="video"></Radio>
            <Radio label="others"></Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="Description" prop="description">
          <Input type="textarea" :rows="4" v-model="uploadDataInfo.description" />
        </FormItem>
      </Form>
      <Upload :max-size="1024*1024" multiple type="drag" :before-upload="gatherFile" action="-">
        <div style="padding: 20px 0">
          <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
          <p>
            Click or drag files here to upload (The file size must control in
            <span
              style="color:red"
            >1GB</span>)
          </p>
        </div>
      </Upload>
      <div style="padding:0 10px 0 10px;max-height:200px;overflow-y:auto">
        <ul v-for="(list,index) in toUploadFiles" :key="index">
          <li style="display:flex">
            File name:
            <span
              style="font-size:10px;margin: 0 5px 0 5px"
            >{{ list.name }} ( {{list.fileSize}} )</span>
            <Icon
              type="ios-close"
              size="20"
              @click="delFileList(index)"
              style="display:flex;justify-content:flex-end;cursor:pointer"
            ></Icon>
          </li>
        </ul>
      </div>
      <div slot="footer">
        <Button @click="dataUploadModal=false">Cancel</Button>
        <Button type="success" @click="folderUpload('uploadDataInfo')">Upload</Button>
      </div>
    </Modal>
    <Modal
      v-model="progressModalShow"
      title="Upload Progress"
      :mask-closable="false"
      :closable="false"
    >
      <Progress :percent="uploadProgress"></Progress>
      <div slot="footer"></div>
    </Modal>
    <Modal
      v-model="deleteResourceModal"
      @on-ok="deleteResource()"
      ok-text="Assure"
      cancel-text="Cancel"
    >
      <h3>Do you really want to delete this resource?</h3>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import * as userRoleJS from "./../../../../api/userRole.js";
export default {
  props: ["activityInfo"],
  components: {
    Avatar,
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695",
        },
      },
      userInfo: this.$store.getters.userInfo,
      userRole: "visitor",
      resouceModel: "resources",
      //资源继承
      preActivities: [],
      existingResources: [],
      targetKeys: [],
      inheritResModal: false,
      listStyle: { width: "280px", height: "375px" },
      // 上传资源
      dataUploadModal: false,
      uploadDataInfo: {
        privacy: "private",
        type: "data",
        description: "",
      },
      uploadDataRule: {
        privacy: [
          {
            required: true,
            message: "file privacy cannot be empty",
            trigger: "blur",
          },
        ],
        type: [
          {
            required: true,
            message: "file type cannot be empty",
            trigger: "blur",
          },
        ],
        description: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur",
          },
        ],
      },
      toUploadFiles: [],
      fileCountTimer: null,
      progressModalShow: false,
      uploadProgress: 0,
      fileList: [], //showed resources
      stepResList: [], //resources in the step
      stepDataList: [], //data in the step
      relatedResList: [], //related materials in the step
      toolDataList: [], //data from tools in the step
      dataListStyle: false, // 数据列表展示样式
      // 资源avatar 图片路径
      dataUrl: require("@/assets/images/data.png"),
      modelUrl: require("@/assets/images/model.png"),
      paperUrl: require("@/assets/images/paper.png"),
      documentUrl: require("@/assets/images/document.png"),
      imageUrl: require("@/assets/images/image.png"),
      videoUrl: require("@/assets/images/video.png"),
      otherUrl: require("@/assets/images/otherfile.png"),
      //
      showType: "resources",
      checkDataModal: false,
      tableColName: [
        {
          title: "Name",
          key: "name",
          minWidth: 30,
          tooltip: true,
          sortable: true,
        },
        // {
        //   title: "Description",
        //   key: "description",
        //   tooltip: true,
        // },
        {
          title: "Action",
          slot: "action",
          width: 120,
          align: "center",
        },
      ],
      selectData: {},
      // 编辑data描述信息
      metaDataEdit: false,
      // 元数据信息，待完善
      metaDataInfo: "",
      // 删除资源
      deleteResourceModal: false,
      deleteResourceId: "",
      panel: null,
    };
  },
  watch: {
    checkDataModal(value) {
      if (!value) {
        this.panel.close();
      }
    },
    activityInfo() {
      this.getResList();
    },
  },
  created() {},
  mounted() {
    this.getResList();

    $(".__view").css("width", "inherit");

    Date.prototype.Format = function (fmt) {
      var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S+": this.getMilliseconds(), //毫毛
      };
      if (/(y+)/.test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          (this.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
      return fmt;
    };
  },
  methods: {
    roleIdentity() {
      this.userRole = userRoleJS.roleIdentify(
        this.activityInfo.members,
        this.userInfo.userId
      );
    },
    permissionIdentity(permission, operation) {
      return userRoleJS.permissionIdentity(
        JSON.parse(permission),
        this.userRole,
        operation
      );
    },
    getResList() {
      var list = [];
      if (this.activityInfo.aid != "" && this.activityInfo.aid != undefined) {
        $.ajax({
          url:
            "/GeoProblemSolving/folder/inquiry" +
            "?folderId=" +
            this.activityInfo.aid,
          type: "GET",
          async: false,
          success: (data) => {
            if (data !== "None") {
              list = data.files;
            } else {
              list = [];
            }
            this.$set(this, "stepResList", list);
            this.$set(this, "fileList", list);
            //filte
            this.filterData();
            this.filterToolData();
            this.filterRelatedRes();
          },
        });
      }
    },
    filterData() {
      var filterdata = this.fileList.filter((item) => {
        return item.type === "data";
      });
      this.$set(this, "stepDataList", filterdata);
    },

    filterToolData() {
      var filterdata = this.fileList.filter((item) => {
        if (item.type.indexOf("toolData") != -1) {
          return item;
        }
      });
      this.$set(this, "toolDataList", filterdata);
    },
    filterRelatedRes() {
      var filterdata = this.fileList.filter((item) => {
        return item.type !== "data";
      });
      this.$set(this, "relatedResList", filterdata);
    },
    changeResModel(value) {
      if (value == "resources") {
        this.fileList = this.stepResList;
        this.showType = value;
      } else if (value == "data") {
        this.fileList = this.stepDataList;
        this.showType = value;
      } else if (value == "materials") {
        this.fileList = this.relatedResList;
        this.showType = value;
      } else if (value == "toolData") {
        this.fileList = this.toolDataList;
        this.showType = value;
      }
    },
    gatherFile(file) {
      if (this.toUploadFiles.length >= 5) {
        if (this.fileCountTimer != null) {
          clearTimeout(this.fileCountTimer);
        }
        this.fileCountTimer = setTimeout(() => {
          this.$Message.info("最多只能上传5个文件");
        }, 500);
      } else {
        var fileSize = file.size;
        if (fileSize < 1024) {
          file.fileSize = fileSize + "b";
        } else if (fileSize < 1024 * 1024) {
          file.fileSize = Math.round((fileSize / 1024) * 100) / 100 + "Kb";
        } else {
          file.fileSize =
            Math.round((fileSize / (1024 * 1024)) * 100) / 100 + "Mb";
        }
        this.toUploadFiles.push(file);
      }
      return false;
    },
    delFileList(index) {
      this.toUploadFiles.splice(index, 1);
    },
    dataUploadModalShow() {
      this.uploadDataInfo = {
        privacy: "private",
        type: "data",
        description: "",
      };
      this.dataUploadModal = true;
    },
    folderUpload(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          var uploadFiles = this.toUploadFiles;
          if (uploadFiles.length > 0) {
            this.dataUploadModal = false;
            var formData = new FormData();
            for (var i = 0; i < uploadFiles.length; i++) {
              formData.append("file", uploadFiles[i]);
            }
            formData.append("description", this.uploadDataInfo.description);
            formData.append("type", this.uploadDataInfo.type);
            formData.append("uploaderId", this.userInfo.userId);
            formData.append("privacy", this.uploadDataInfo.privacy);
            formData.append("folderId", this.activityInfo.aid);

            this.progressModalShow = true;

            if (
              this.activityInfo.aid != "" ||
              this.activityInfo.aid != undefined
            ) {
              this.axios({
                url: "/GeoProblemSolving/folder/uploadToFolder",
                method: "post",
                onUploadProgress: (progressEvent) => {
                  this.uploadProgress =
                    ((progressEvent.loaded / progressEvent.total) * 100) | 0;
                },
                data: formData,
              })
                .then((res) => {
                  if (res.data != "Fail") {
                    var uploadedList = res.data.uploaded;
                    var failedList = res.data.failed;
                    var sizeOverList = res.data.sizeOver;
                    for (var i = 0; i < uploadedList.length; i++) {
                      this.fileList.push(uploadedList[i]);

                      if (
                        this.showType == "data" ||
                        this.showType == "materials"
                      ) {
                        this.stepResList.push(uploadedList[i]);
                      } else if (this.uploadDataInfo.type == "data") {
                        this.stepDataList.push(uploadedList[i]);
                      } else {
                        this.relatedResList.push(uploadedList[i]);
                      }
                    }
                    if (sizeOverList.length > 0) {
                      this.$Notice.warning({
                        title: "Files too large.",
                        render: (h) => {
                          return h("span", sizeOverList.join(";"));
                        },
                      });
                    }
                    if (failedList.length > 0) {
                      this.$Notice.error({
                        title: "Upload fail.",
                        render: (h) => {
                          return h("span", failedList.join(";"));
                        },
                      });
                    }

                    // 初始化上传数据列表
                    this.toUploadFiles = [];

                    // 记录信息
                    let filelist = "";
                    for (let i = 0; i < uploadedList.length - 1; i++) {
                      filelist += uploadedList[i].name + ", ";
                    }
                    filelist +=
                      uploadedList[uploadedList.length - 1].name + ". ";
                    let dataRecords = {
                      type: "resource",
                      time: new Date().Format("yyyy-MM-dd HH:mm:ss"),
                      who: this.userInfo.userName,
                      content: "upload data",
                      file: filelist,
                    };
                    // 保存记录
                    this.$emit("dataBehavior", dataRecords);
                    // this.addHistoryEvent(this.activityInfo.aid, dataRecords);
                  } else {
                    this.$Message.warning("Upload fail.");
                  }
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                })
                .catch((err) => {
                  this.progressModalShow = false;
                  this.$Message.warning("Upload fail.");
                  this.uploadProgress = 0;
                });
            } else {
              this.$Notice.info({
                desc: "upload data failed!",
              });
            }
          } else {
            this.$Message.warning("Upload data is null.");
          }
        }
      });
    },
    deleteResourceModalShow(id) {
      this.deleteResourceModal = true;
      this.deleteResourceId = id;
    },
    deleteResource() {
      if (this.deleteResourceId != "") {
        this.axios
          .get(
            "/GeoProblemSolving/folder/removeFile?" +
              "fileId=" +
              this.deleteResourceId +
              "&folderId=" +
              this.activityInfo.aid
          )
          .then((res) => {
            if (res.data == "Fail") {
              this.$Notice.error({
                title: "Process result",
                desc: "Delete fail",
              });
              // this.$Message.info("Failure");
            } else {
              this.$Notice.success({
                title: "Process result",
                desc: "Delete successfully",
              });

              //从列表中删除
              var deleteResType = "";
              for (let i = 0; i < this.fileList.length; i++) {
                if (this.fileList[i].resourceId == this.deleteResourceId) {
                  // 记录信息
                  try {
                    let dataRecords = {
                      type: "resource",
                      time: new Date().Format("yyyy-MM-dd HH:mm:ss"),
                      who: this.userInfo.userName,
                      content: "delete data",
                      file: this.fileList[i].name,
                    };
                    this.$emit("dataBehavior", dataRecords);
                    // this.addHistoryEvent(this.activityInfo.aid, dataRecords);

                    deleteResType = this.fileList[i].type;
                    this.fileList.splice(i, 1);
                  } catch (err) {
                    console.log(err);
                  }
                }
              }
              // 同步删除其他数组内的资源
              if (this.showType == "data" || this.showType == "materials") {
                for (var i = 0; i < this.stepResList.length; i++) {
                  if (this.stepResList[i].resourceId == this.deleteResourceId) {
                    this.stepResList.splice(i, 1);
                  }
                }
              } else if (deleteResType == "data") {
                for (var i = 0; i < this.stepDataList.length; i++) {
                  if (
                    this.stepDataList[i].resourceId == this.deleteResourceId
                  ) {
                    this.stepDataList.splice(i, 1);
                  }
                }
              } else {
                for (var i = 0; i < this.relatedResList.length; i++) {
                  if (
                    this.relatedResList[i].resourceId == this.deleteResourceId
                  ) {
                    this.relatedResList.splice(i, 1);
                  }
                }
              }
              // // 如果删除的是ToolData, 同步删除
              // for (var i = 0; i < this.toolDataList.length; i++) {
              //   if (this.toolDataList[i].resourceId == this.deleteResourceId) {
              //     this.toolDataList.splice(i, 1);
              //   }
              // }
            }
          })
          .catch((err) => {
            console.log(err.data);
          });
      }
    },
    // addHistoryEvent(scopeId, record) {
    //   let form = {};
    //   form["description"] = JSON.stringify(record);
    //   form["scopeId"] = scopeId;
    //   form["eventType"] = "step";
    //   form["userId"] = this.$store.getters.userId;
    //   this.axios
    //     .post("/GeoProblemSolving/history/save", form)
    //     .then(res => {
    //       console.log(res.data);
    //     })
    //     .catch(err => {
    //       console.log(err.data);
    //     });
    // },
    checkData(item) {
      this.selectData = item;
      this.checkDataModal = true;
    },
    editMetadata() {
      if (this.metaDataEdit) {
        this.metaDataEdit = false;
      } else {
        this.metaDataEdit = true;
      }
    },
    dataPreview(res) {
      let name = res.name;
      if (/\.(doc|docx|xls|xlsx|ppt|pptx)$/.test(name.toLowerCase())) {
        this.$Modal.confirm({
          title: "Note",
          content:
            "<p>You selected file will be previewed through</p><p style='font-size:16px;font-weight:bold'>Microsoft office online service</p>",
          onOk: () => {
            if (this.panel != null) {
              this.panel.close();
            }
            var url =
              "http://view.officeapps.live.com/op/view.aspx?src=" +
              "http://" +
              this.$store.state.IP_Port +
              res.pathURL;
            var toolURL =
              "<iframe src=" +
              url +
              ' style="width: 100%;height:100%" frameborder="0"></iframe>';
            var demoPanelTimer = null;
            this.panel = jsPanel.create({
              headerControls: {
                smallify: "remove",
              },
              theme: "primary",
              footerToolbar: '<p style="height:5px"></p>',
              headerTitle: "Preview",
              contentSize: "800 600",
              content: toolURL,
              disableOnMaximized: true,
              dragit: {
                containment: 5,
              },
              closeOnEscape: true,
              // callback: function() {
              //   var that = this;
              //   demoPanelTimer = window.setInterval(function() {
              //     that.style.zIndex = "9999";
              //   }, 1);
              // }
            });
            $(".jsPanel-content").css("font-size", "0");
          },
          onCancel: () => {
            return;
          },
        });
      } else if (/\.(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url = "http://" + this.$store.state.IP_Port + res.pathURL;
        var toolURL =
          "<video src=" +
          url +
          ' style="width: 100%;height:100%" controls></video>';
        var demoPanelTimer = null;
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove",
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5,
          },
          closeOnEscape: true,
          // callback: function() {
          //   var that = this;
          //   demoPanelTimer = window.setInterval(function() {
          //     that.style.zIndex = "9999";
          //   }, 1);
          // }
        });
        $(".jsPanel-content").css("font-size", "0");
      } else if (/\.(pdf|json|md|gif|jpg|png)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url = "http://" + this.$store.state.IP_Port + res.pathURL;
        var toolURL =
          "<iframe src=" +
          url +
          ' style="width: 100%;height:100%" frameborder="0" controls></iframe>';
        var demoPanelTimer = null;
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove",
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5,
          },
          closeOnEscape: true,
          // callback: function() {
          //   var that = this;
          //   demoPanelTimer = window.setInterval(function() {
          //     that.style.zIndex = "9999";
          //   }, 1);
          // }
        });
        $(".jsPanel-content").css("font-size", "0");
      } else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Sorry. Unsupported file format.",
        });
        return false;
      }
    },
    getPreviousRes() {
      // 获取可继承的资源
      this.getPreActivities();
      this.inheritResModal = true;
    },
    getPreActivities() {
      // this.preActivities = [];
      // let processStructure = [];
      // // get solving process
      // if (
      //   this.projectInfo.solvingProcess == null ||
      //   this.projectInfo.solvingProcess == undefined ||
      //   JSON.parse(this.projectInfo.solvingProcess).length == 0
      // ) {
      //     $.ajax({
      //       url:
      //         "/GeoProblemSolving/subProject/inquiry" +
      //         "?key=aid" +
      //         "&value=" +
      //         this.activityInfo.aid,
      //       type: "GET",
      //       async: false,
      //       success: data => {
      //         if (data == "Offline") {
      //           this.$store.commit("userLogout");
      //           this.$router.push({ name: "Login" });
      //         } else if (data != "None" && data != "Fail") {
      //           let subprojectInfo = data[0];
      //           processStructure = JSON.parse(subprojectInfo.solvingProcess);
      //         } else {
      //           console.log(data);
      //         }
      //       },
      //       error: function(err) {
      //         console.log("Get manager name fail.");
      //       }
      //     });
      //   }
      // for (var i = 0; i < processStructure.length; i++) {
      //   if (processStructure[i].aid == this.activityInfo.aid) {
      //     let last = processStructure[i].last;
      //     if (last.length > 0) {
      //       for (var j = 0; j < last.length; j++) {
      //         this.preActivities.push(processStructure[last[j].id]);
      //       }
      //       this.getInheritResource();
      //     }
      //   }
      // }
    },
    getInheritResource() {
      this.existingResources = this.getMockData();
    },
    getMockData() {
      let mockData = [];
      let selectedRes = [];

      // 前驱步骤的资源
      for (var i = 0; i < this.preActivities.length; i++) {
        let selectedStepId = this.preActivities[i].aid;
        let selectedStepName = this.preActivities[i].name;
        let getResUrl =
          "/GeoProblemSolving/folder/findByFileType?" +
          "scopeId=" +
          selectedStepId +
          "&type=all";

        $.ajax({
          url: getResUrl,
          type: "GET",
          async: false,
          success: function (data) {
            if (data !== "Fail") {
              selectedRes = data;
              for (var j = 0; j < selectedRes.length; j++) {
                mockData.push({
                  key: mockData.length.toString(),
                  name: selectedRes[j].name,
                  type: selectedRes[j].type,
                  resourceId: selectedRes[j].resourceId,
                  source: selectedStepName,
                });
              }
            } else {
              selectedRes = [];
            }
          },
          error: function (err) {
            selectedRes = [];
            console.log("err!");
          },
        });
      }
      return mockData;
    },
    getTargetKeys() {
      let mockData = [];
      if (this.existingResources.length > 0) {
        for (var i = 0; i < this.targetKeys.length; i++) {
          mockData.push({
            key: this.targetKeys[i],
            name: this.existingResources[this.targetKeys[i]].name,
            type: this.existingResources[this.targetKeys[i]].type,
            resourceId: this.existingResources[this.targetKeys[i]].resourceId,
            source: this.existingResources[this.targetKeys[i]].source,
          });
        }
      }
      return mockData;
    },
    handleChange(newTargetKeys) {
      this.targetKeys = newTargetKeys;
    },
    resourceRender(item) {
      // return item.type + " - " + item.name;
      return `<span title="${item.type} - ${item.source}">${item.name}</span>`;
    },
    filterMethod(data, query) {
      return data.type.indexOf(query) > -1;
    },
    saveResources() {
      let selectResource = this.getTargetKeys();
      let addFileList = [];
      for (var i = 0; i < selectResource.length; i++) {
        addFileList.push(selectResource[i].resourceId);
      }
      let addFileListStr = addFileList.toString();

      this.axios
        .get(
          "/GeoProblemSolving/folder/shareToFolder" +
            "?addFileList=" +
            addFileListStr +
            "&folderId=" +
            this.activityInfo.aid
        )
        .then((res) => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data == "Fail") {
            this.$Message.error(
              "Failed to get resources from previous activities."
            );
          } else {
            this.getResList();
          }
        })
        .catch((err) => {
          // console.log(err.data);
        });
    },
    dataVisualize() {},
  },
};
</script>