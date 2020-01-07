<style scoped>
.table table {
  table-layout: auto;
  width: 100% !important;
}
.fileBtnHoverGreen:hover {
  background-color: #19be6b;
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
  width: 100%;
  word-break: break-word;
  /* display: inline-block; */
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  max-width: 250px;
  word-break: break-word;
}
.shareLabel {
  display: block;
  width: 50px;
  float: left;
  margin-top: 8px;
  text-align: right;
  font-weight: bold;
}
#toolData {
  height: 400px;
  border: 1px solid #dcdee2;
}
#toolDataHeader {
  font-size: 12px;
  font-weight: bold;
  padding: 10px 15px;
  border-bottom: 1px solid #e8eaec;
  background-color: #f8f8f9;
}
</style>
<template>
  <div>
    <Row>
      <Col span="6">
      <Tabs size="small">
        <TabPane label="resource">
          <div style="border: 1px solid #dcdee2;padding:0 5px 5px">
            <div>
              <div style="margin-top: 15px;">
                <h4 style="display: inline-block;margin-left:10px">Resources</h4>
                <Select
                  v-model="resouceModel"
                  size="small"
                  @on-change="changeResModel"
                  style="width:150px;margin:-10px 12px 0 15px"
                >
                  <Option value="resources">All resources</Option>
                  <Option value="data">Data</Option>
                  <Option value="materials">Related materials</Option>
                </Select>
                <Button
                  shape="circle"
                  icon="md-cloud-upload"
                  @click="dataUploadModalShow"
                  style="margin-top:-10px"
                  title="Upload resources"
                ></Button>
              </div>
            </div>
            <div>
              <Table
                :columns="tableColName"
                :data="fileList"
                class="table"
                v-show="fileList!=[] && fileList!='None'"
                height="360"
                size="small"
                no-data-text="No data"
              >
                <template slot-scope="{ row }" slot="name">
                  <strong>{{ row.name }}</strong>
                </template>
                <template slot-scope="{ row }" slot="action">
                  <Button
                    class="fileBtnHoverGreen"
                    size="small"
                    title="Details"
                    @click="checkData(row)"
                    icon="md-information-circle"
                    shape="circle"
                    type="text"
                  ></Button>
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
              </Table>
            </div>
          </div>
        </TabPane>
        <TabPane label="tools">
          <div style="height:400px">
            <vue-scroll :ops="ops">
              <tool-container :stepInfo="stepInfo" :userRole="userRole"></tool-container>
            </vue-scroll>
          </div>
        </TabPane>
      </Tabs>
      </Col>
      <Col span="18">
      <div style="margin-bottom:5px;margin-left:5px">
        <Card dis-hover>
          <div slot="title">
            <h4>Historical edits</h4>
          </div>
          <div id="toolData">
            <vue-scroll :ops="ops">
              <div v-for="(item,index) in toolDataList" :key="index">
                <Card style="width:48%; height:150px; float:left; margin:5px">
                  <div style="float:left">
                    <img
                      v-if="item.thumbnail != undefined"
                      :src="item.thumbnail"
                      height="118px"
                      width="118px"
                    />
                    <avatar v-else :username="item.name" :size="118" :rounded="false"></avatar>
                  </div>
                  <div style="float:left;margin: 0 10px">
                    <div>
                      <Label class="toolDataLabel">Name:</Label>
                      <div class="toolDataText" :title="item.name">{{item.name}}</div>
                    </div>
                    <div>
                      <Label class="toolDataLabel">Description:</Label>
                      <div class="toolDataText" :title="item.description">{{item.description}}</div>
                    </div>
                    <div>
                      <Button
                        size="small"
                        title="Check"
                        icon="md-eye"
                        style="margin: 10px 20px 0 0;"
                        type="primary"
                        @click="OpenData(item)"
                      ></Button>
                      <Button
                        size="small"
                        title="Details"
                        icon="md-information-circle"
                        style="margin: 10px 20px 0 0;"
                        @click="checkData(item)"
                      ></Button>
                      <Button
                        size="small"
                        title="Share"
                        icon="ios-share-alt"
                        style="margin: 10px 20px 0 0;"
                        @click="shareData(item)"
                      ></Button>
                      <Button
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
        </Card>
      </div>
      </Col>
    </Row>
    <Modal v-model="checkDataModal" title="Data Information" width="600">
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
        <TabPane label="UDX Schema" name="udx" icon="md-browsers">
          <pre class="brush: html"></pre>
          <template>...</template>
        </TabPane>
      </Tabs>
      <br />
      <Button style="margin-right:20px" @click="dataPreview(selectData)">Preview</Button>
      <Button style="margin-right:20px" @click="dataVisualize">Visualization</Button>
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
    <Modal v-model="shareModal" title="Share workspace" footer-hide>
      <div style="margin-bottom:20px">
        <span class="shareLabel">Token:</span>
        <Input v-model="sharedToken" readonly style="width: 300px;margin-left:10px" />
        <Button
          type="primary"
          @click="generateToken()"
          style="margin-right:30px;float:right;width:80px"
        >Generate</Button>
      </div>
      <div style="margin-bottom:20px">
        <span class="shareLabel">Url:</span>
        <Input v-model="sharedUrl" readonly style="width:300px;margin-left:10px" />
        <Button
          type="primary"
          @click="copySharedUrl()"
          style="margin-right:30px;float:right;width:80px"
        >Copy</Button>
      </div>
      <div style="margin-bottom:20px">
        <span class="shareLabel">Email:</span>
        <Input
          v-model="sharingEmail"
          placeholder="Plase enter the email address"
          style="width: 300px;margin-left:10px"
        />
        <Button
          type="primary"
          @click="shareWorkspace()"
          style="margin-right:30px;float:right;width:80px"
        >Share</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import toolContainer from "./toolContainer";
export default {
  components: {
    toolContainer,
    Avatar
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695"
        }
      },
      unfold: ["tool"],
      userInfo: this.$store.getters.userInfo,
      resouceModel: "resources",
      dataUploadModal: false,
      uploadDataInfo: {
        privacy: "private",
        type: "data",
        description: ""
      },
      uploadDataRule: {
        privacy: [
          {
            required: true,
            message: "file privacy cannot be empty",
            trigger: "blur"
          }
        ],
        type: [
          {
            required: true,
            message: "file type cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: true,
            message: "file description cannot be empty",
            trigger: "blur"
          }
        ]
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
      showType: "resources",
      checkDataModal: false,
      tableColName: [
        {
          title: "Name",
          key: "name",
          minWidth: 50,
          tooltip: true,
          sortable: true
        },
        {
          title: "Type",
          key: "type",
          tooltip: true,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 90,
          align: "center"
        }
      ],
      selectData: {},
      // 编辑data描述信息
      metaDataEdit: false,
      // 元数据信息，待完善
      metaDataInfo: "",
      // 删除资源
      deleteResourceModal: false,
      deleteResourceId: "",
      //for share
      shareModal: false,
      sharedUrl: "",
      sharedToken: "",
      sharingEmail: "",
      // panel
      panel: null,
      scopeType:'subproject'
    };
  },
  props: ["stepInfo", "userRole"],
  watch: {
    checkDataModal(value) {
      if (!value) {
        this.panel.close();
      }
    },
    stepInfo() {
      this.getResList();
    }
  },
  created() {
  },
  mounted() {
    this.getResList();

    $(".__view").css("width", "inherit");

    Date.prototype.Format = function(fmt) {
      var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S+": this.getMilliseconds() //毫毛
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
    getResList() {
      if(this.stepInfo.subProjectId==null||this.stepInfo.subProjectId==""||this.stepInfo.subProjectId==undefined){
        this.scopeType = "project";
      }
      else{
        this.scopeType = "subproject";
      }
      var list = [];
      if (this.stepInfo.stepId != "" && this.stepInfo.stepId != undefined) {
        $.ajax({
          url:
            "/GeoProblemSolving/folder/inquiry" +
            "?folderId=" +
            this.stepInfo.stepId,
          type: "GET",
          async: false,
          success: data => {
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
          }
        });
      }
    },
    filterData() {
      var filterdata = this.fileList.filter(item => {
        return item.type === "data";
      });
      this.$set(this, "stepDataList", filterdata);
    },
    filterToolData() {
      var filterdata = this.fileList.filter(item => {
        if (item.type.indexOf("toolData") != -1) {
          return item;
        }
      });
      this.$set(this, "toolDataList", filterdata);
    },
    filterRelatedRes() {
      var filterdata = this.fileList.filter(item => {
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
      if (this.userRole != "Visitor" && this.userRole != "Token") {
        this.uploadDataInfo = {
          privacy: "private",
          type: "data",
          description: ""
        };
        this.dataUploadModal = true;
      } else {
        this.$Notice.info({ desc: "Please login and join this project" });
      }
    },
    folderUpload(name) {
      this.$refs[name].validate(valid => {
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
            formData.append("folderId", this.stepInfo.stepId);

            this.progressModalShow = true;

            if (
              this.stepInfo.stepId != "" ||
              this.stepInfo.stepId != undefined
            ) {
              this.axios({
                url: "/GeoProblemSolving/folder/uploadToFolder",
                method: "post",
                onUploadProgress: progressEvent => {
                  this.uploadProgress =
                    ((progressEvent.loaded / progressEvent.total) * 100) | 0;
                },
                data: formData
              })
                .then(res => {
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
                        render: h => {
                          return h("span", sizeOverList.join(";"));
                        }
                      });
                    }
                    if (failedList.length > 0) {
                      this.$Notice.error({
                        title: "Upload fail.",
                        render: h => {
                          return h("span", failedList.join(";"));
                        }
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
                      file: filelist
                    };
                    this.$emit("dataBehavior", dataRecords);
                  } else {
                    this.$Message.warning("Upload fail.");
                  }
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                })
                .catch(err => {
                  this.progressModalShow = false;
                  this.$Message.warning("Upload fail.");
                  this.uploadProgress = 0;
                });
            } else {
              this.$Notice.info({
                desc: "upload data failed!"
              });
            }
          } else {
            this.$Message.warning("Upload data is null.");
          }
        }
      });
    },
    deleteResourceModalShow(id) {
      if (this.userRole != "Visitor" && this.userRole != "Token") {
        this.deleteResourceModal = true;
        this.deleteResourceId = id;
      } else {
        this.$Notice.info({ desc: "Please login and join this project" });
      }
    },
    deleteResource() {
      if (this.deleteResourceId != "") {
        this.axios
          .get(
            "/GeoProblemSolving/folder/removeFile?" +
              "fileId=" +
              this.deleteResourceId +
              "&folderId=" +
              this.stepInfo.stepId
          )
          .then(res => {
            if (res.data == "Fail") {
              this.$Notice.error({
                title: "Process result",
                desc: "Delete fail"
              });
              // this.$Message.info("Failure");
            } else {
              this.$Notice.success({
                title: "Process result",
                desc: "Delete successfully"
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
                      file: this.fileList[i].name
                    };
                    this.$emit("dataBehavior", dataRecords);

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
              // 如果删除的是ToolData, 同步删除
              for (var i = 0; i < this.toolDataList.length; i++) {
                if (this.toolDataList[i].resourceId == this.deleteResourceId) {
                  this.toolDataList.splice(i, 1);
                }
              }
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    OpenData(item) {
      let toolInfo = {};
      let toolURL = "";
      // 检测是否有toolInfo信息
      try {
        toolInfo = JSON.parse(item.editToolInfo);
      } catch (err) {
        this.$Notice.info({ desc: "There is no record of using tools." });
        return;
      }
      if (toolInfo == undefined || toolInfo == null || toolInfo == {}) {
        this.$Notice.info({ desc: "There is no record of using tools." });
        return;
      }
      if (this.panel != null) {
        this.panel.close();
      }
      if (this.userRole != "Visitor" && this.userRole != "Token") {
        toolURL =
          '<iframe src="' +
          toolInfo.toolUrl +
          "?userName=" +
          this.userInfo.userName +
          "&userID=" +
          this.userInfo.userId +
          "&groupID=" +
          this.stepInfo.stepId +
          "&resourceID=" +
          item.resourceId +
          '" style="width: 100%;height:100%;"></iframe>';
      } else {
        toolURL =
          '<iframe src="' +
          toolInfo.toolUrl +
          "?userName=" +
          "&userID=" +
          "&groupID=" +
          this.stepInfo.stepId +
          "&resourceID=" +
          item.resourceId +
          '" style="width: 100%;height:100%;"></iframe>';
      }
      var demoPanelTimer = null;
      this.showPanel(toolURL, toolInfo.toolName);
    },
    checkData(item) {
      if (this.userRole != "Visitor" && this.userRole != "Token") {
        this.selectData = item;
        this.checkDataModal = true;
      } else {
        this.$Notice.info({ desc: "Please login and join this project" });
      }
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
              ' style="width: 100%;height:100%"></iframe>';
            var demoPanelTimer = null;
            this.showPanel(toolURL, res.name);
          },
          onCancel: () => {
            return;
          }
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
        this.showPanel(toolURL, res.name);
      } else if (/\.(pdf|json|md|gif|jpg|png)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url = "http://" + this.$store.state.IP_Port + res.pathURL;
        var toolURL =
          "<iframe src=" +
          url +
          ' style="width: 100%;height:100%" controls></iframe>';
        var demoPanelTimer = null;
        this.showPanel(toolURL, res.name);
      } else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Sorry. Unsupported file format."
        });
        return false;
      }
    },
    showPanel(url, title){
      if(this.scopeType=="project"){
        parent.vm.showToolPanel(url, title);
      }
      else{
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:5px"></p>',
          headerTitle: title,
          contentSize: "800 600",
          content: url,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true,
          onbeforeclose: function(panel, status, closedByUser) {
            return confirm("Please confirm your edit has been saved?");
          },
          // callback: function() {
          //   var that = this;
          //   demoPanelTimer = window.setInterval(function() {
          //     that.style.zIndex = "9999";
          //   }, 1);
          // }
        });
        $(".jsPanel-content").css("font-size", "0");
      }
    },
    dataVisualize() {},
    shareData(data){
      this.shareModal=true;
      this.selectData = data;
    },
    generateToken() {
      let info = {
        groupId: this.stepInfo.stepId,
        resourceId: this.selectData.resourceId
      }
      this.axios
        .post("/GeoProblemSolving/token/getShareToken?duration=0", info)
        .then(res => {
          this.sharedToken = res.data;
          this.sharedUrl =
            "http://" +
            this.$store.state.IP_Port +
            "/share/"+this.stepInfo.stepId+"?tool=mindmap&token=" +
            this.sharedToken;
        })
        .catch(err => {});
    },
    copySharedUrl() {
      let url = this.sharedUrl;
      var input = document.createElement("input");
      input.style.opacity = 0;
      input.style.position = "absolute";
      input.style.left = "-100000px";
      document.body.appendChild(input);

      input.value = url;
      input.select(); // 选择对象
      input.setSelectionRange(0, url.length);
      document.execCommand("Copy"); // 执行浏览器复制命令

      document.body.removeChild(input);
      this.$Notice.info({ desc: "Copy successfully." });
    },
    shareWorkspace() {
      if (this.sharingEmail == "") {
        this.$Notice.info({
          desc: "Please fill in the email address."
        });
        return;
      }

      var url = this.sharedUrl;

      //send url via email
      var emailFormBody = {};
      emailFormBody["recipient"] = this.sharingEmail;
      emailFormBody["mailTitle"] = "Participatory workspace sharing";
      emailFormBody["mailContent"] =
        "Open the address:( " + url + " ), and check the latest work.";
      axios
        .post("/GeoProblemSolving/email/send", emailFormBody)
        .then(res => {
          if (res.data == "Success") {
            this.$Notice.success({
              desc: "The share has been sent successfully."
            });
          } else {
            this.$Notice.error({
              desc: "Failed to send the share."
            });
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    }
  }
};
</script>