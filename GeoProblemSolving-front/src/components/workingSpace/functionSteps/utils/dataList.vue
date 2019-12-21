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
  width: 100px;
  word-break: break-word;
  /* display: inline-block; */
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
#toolData {
  width: 68%;
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
    <Card dis-hover>
      <div slot="title">
        <h4>Data</h4>
      </div>
      <div slot="extra">
        <Button
          shape="circle"
          icon="md-cloud-upload"
          @click="dataUploadModalShow"
          style="margin-top:-10px"
        ></Button>
      </div>
      <div style="display: flex; justify-content: space-between;">
        <div style="width:30%; height:400px">
          <vue-scroll :ops="ops">
            <Table
              :columns="tableColName"
              :data="stepDataList"
              class="table"
              v-show="stepDataList!=[] && stepDataList!='None'"
            >
              <template slot-scope="{ row }" slot="name">
                <strong>{{ row.name }}</strong>
              </template>
              <template slot-scope="{ row, index }" slot="action">
                <Button
                  class="fileBtnHoverGreen"
                  size="small"
                  title="Check"
                  @click="checkData(row)"
                  icon="md-eye"
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
          </vue-scroll>
        </div>
        <div id="toolData">
          <div id="toolDataHeader">Data from Tools</div>
          <vue-scroll :ops="ops" style="height:360px">
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
                      style="margin: 10px 30px 0 0;"
                      @click="checkData(item)"
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
      </div>
    </Card>
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
      <Button style="margin-right:20px" @click="dataPreview">Preview</Button>
      <Button style="margin-right:20px" @click="dataVisualize">Visualization</Button>
      <br />
    </Modal>
    <Modal v-model="dataUploadModal" title="Upload data" width="600">
      <Form
        ref="uploadData"
        :model="uploadData"
        :rules="uploadDataRule"
        :label-width="100"
        label-position="left"
      >
        <FormItem label="Privacy" prop="privacy">
          <RadioGroup v-model="uploadData.privacy" style="width:80%">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>

        <FormItem label="Description" prop="description">
          <Input type="textarea" :rows="4" v-model="uploadData.description" />
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
        <Button type="success" @click="folderUpload('uploadData')">Upload</Button>
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
export default {
  components: {
    Avatar
  },
  data() {
    return {
      ops: {
        bar: {
          background: "#808695"
        }
      },
      userInfo: this.$store.getters.userInfo,
      dataUploadModal: false,
      uploadData: {
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
      fileList: [], //resources in the step
      stepDataList: [], //data in the step
      toolDataList: [
        {
          thumbnail:
            "http://94.191.49.160:8080/GeoProblemSolving/project/picture/commen1065951.jpeg",
          name: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
          description: "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
        },
        {name:"ABC"}
      ], //data from tools in the step
      checkDataModal: false,
      tableColName: [
        {
          title: "Name",
          key: "name",
          minWidth: 10,
          tooltip: true,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 125,
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
      deleteResourceId: ""
    };
  },
  props: ["stepInfo", "userRole"],
  created() {},
  mounted() {
    this.getDataList();
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
    getDataList() {
      let list = [];
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
              this.$set(this, "fileList", data.files);
            } else {
              this.fileList = [];
            }
            // this.$set(this, "stepResourceList", list);
            this.filterData();
          }
        });
      }
    },
    filterData() {
      let filterdata = this.fileList.filter(item => {
        return item.type === "data";
      });
      this.$set(this, "stepDataList", filterdata);
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
      this.uploadData = {
        privacy: "private",
        type: "data",
        description: ""
      };
      this.dataUploadModal = true;
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
            formData.append("description", this.uploadData.description);
            formData.append("type", this.uploadData.type);
            formData.append("uploaderId", this.userInfo.userId);
            formData.append("privacy", this.uploadData.privacy);
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
                      this.stepDataList.push(uploadedList[i]);
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
              for (let i = 0; i < this.stepDataList.length; i++) {
                if (this.stepDataList[i].resourceId == this.deleteResourceId) {
                  this.stepDataList.splice(i, 1);

                  // 记录信息
                  let dataRecords = {
                    type: "resource",
                    time: new Date().Format("yyyy-MM-dd HH:mm:ss"),
                    who: this.userInfo.userName,
                    content: "delete data",
                    file: this.stepDataList[i].name
                  };
                  this.$emit("dataBehavior", dataRecords);
                }
              }
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    checkData(item) {
      this.selectData = item;
      this.checkDataModal = true;
    },
    editMetadata() {
      if (this.metaDataEdit) {
        this.metaDataEdit = false;
        //     let obj = new URLSearchParams();
        //     obj.append("stepId", this.stepId);
        //     obj.append("content.metaDataInfo", this.metaDataInfo);
        //     this.axios
        //       .post("/GeoProblemSolving/step/update", obj)
        //       .then(res => {
        //         if (res.data == "Offline") {
        //           this.$store.commit("userLogout");
        //           this.$router.push({ name: "Login" });
        //         } else if (res.data != "Fail") {
        //           this.$Notice.info({
        //             desc: "Update successfully!"
        //           });
        //         } else {
        //           this.$Message.error("Update step failed.");
        //         }
        //       })
        //       .catch(err => {
        //         console.log(err.data);
        //       });
      } else {
        this.metaDataEdit = true;
      }
    },
    dataPreview() {},
    dataVisualize() {}
  }
};
</script>