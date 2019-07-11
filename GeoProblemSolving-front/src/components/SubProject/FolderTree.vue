<style scoped>
.folderContent {
  height: 330px;
  overflow-y: auto;
  padding: 5px;
  margin: 3px;
}
.folderDeleteBtn {
  cursor: pointer;
  color: red;
  float: right;
  margin: 0 1%;
}
.folderRenameBtn {
  cursor: pointer;
  color: blue;
  float: right;
  margin: 0 1%;
}
.fileDownloadBtn {
  color: #9999a5;
  float: right;
  margin: 0 1%;
}
.filePreviewBtn {
  cursor: pointer;
  color: #32d64f;
  float: right;
  margin: 0 1%;
}
.fileDeleteBtn {
  cursor: pointer;
  color: #d65f2f;
  float: right;
  margin: 0 1%;
}
.resourceTitle {
  font-size: 18px;
  height: 20px;
  line-height: 20px;
}
.resourceBtnDiv {
  display: flex;
  align-items: center;
  height: 20px;
  padding: 5px;
}
.fileBtn {
  margin: 0px 3px;
}
.itemIcon {
  margin-right: 5px;
}
.fileItemName {
  width: 35%;
  margin-right: 5%;
  display: inline-block;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: top;
  cursor: pointer;
  overflow: hidden;
}
.fileItemSize {
  width: 10%;
  margin-right: 5%;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  height: 16px;
}
.demo-spin-icon-load {
  animation: ani-demo-spin 1s linear infinite;
}
@keyframes ani-demo-spin {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}
.demo-spin-col {
  height: 100px;
  position: relative;
  /* border: 1px solid #eee; */
}
</style>
<template>
  <div class="fileSpace">
    <Card :padding="1">
      <div slot="title" class="resourceTitle">
        <strong>Resources</strong>
      </div>
      <div slot="extra" class="resourceBtnDiv" v-show="role != 'Visitor'">
        <Tooltip content="Back" placement="bottom" class="fileBtn">
          <Button @click="backforeFolder">
            <Icon type="md-arrow-round-back" size="20"/>
          </Button>
        </Tooltip>
        <Tooltip content="New folder" placement="bottom" class="fileBtn">
          <Button @click="addFolderModalShow">
            <Icon type="ios-folder" size="20"/>
          </Button>
        </Tooltip>
        <Tooltip content="Upload files" placement="bottom" class="fileBtn">
          <Button @click="uploadModalShow" title="upload resource">
            <Icon type="md-cloud-upload" size="20"/>
          </Button>
        </Tooltip>
      </div>
      <div class="folderContent">
        <Card v-if="folderNameStack.length>0" :padding="5" dis-hover>
          <div style="display:flex;align-items:center">
            <div style="min-width:60px"v-show="currentFileList.length>0">
              <Checkbox
                :indeterminate="indeterminate"
                :value="checkAll"
                @click.prevent.native="handleCheckAll"
                v-show="currentFileList.length>0"
                style="align-items:center"
                >
                All files
            </Checkbox>
            </div>
            <div style="flex:1;margin-left:3px;">
              <Breadcrumb>
              <BreadcrumbItem>
                <Icon type="md-folder"/>
              </BreadcrumbItem>
              <BreadcrumbItem
                v-for="(folderName,index) in folderNameStack"
                :key="index"
                v-if="index!=0"
              >{{folderName}}</BreadcrumbItem>
            </Breadcrumb>
            </div>
            <div style="align-items:flex-end">
              <Button @click="downloadSelectFile" v-show="currentFileList.length>0" title="Download" style="width:60px;height:30px;">
                <Icon type="md-cloud-download" size="20"/>
              </Button>
            </div>
          </div>
        </Card>
        <div v-if="currentFolder.folders.length>0 || currentFileList.length>0">
          <Card v-for="(folder,index) in currentFolder.folders" :key="folder.index" :padding="5">
            <div>
              <Icon type="ios-folder-open" class="itemIcon" size="25"/>
              <a
                @click="enterFolder(folder)"
                class="fileItemName"
                :title="folder.name"
              >{{folder.name}}</a>
              <span @click="deleteFolder(folder)" class="folderDeleteBtn">
                <Icon type="ios-trash-outline" title="Delete" size="25"/>
              </span>
              <span @click="renameFolderModalShow(folder)" class="folderRenameBtn">
                <Icon type="ios-create-outline" title="Rename" size="25"/>
              </span>
            </div>
          </Card>


          <CheckboxGroup v-model="chooseFilesArray" @on-change="checkAllGroupChange">
            <Card v-for="(file,index) in currentFileList" :key="file.index" :padding="5">
              <Checkbox :label="file.pathURL">&nbsp;</Checkbox>
              <Icon type="ios-document-outline" class="itemIcon" size="25"/>
              <span @click="getFileInfo(file)" class="fileItemName" :title="file.name">{{file.name}}</span>
              <span class="fileItemSize">{{file.fileSize}}</span>
              <span style="width:20%;margin-right:5%">{{file.uploadTime}}</span>
              <span @click="fileDelete(file)" class="fileDeleteBtn">
                <Icon type="ios-trash" title="Remove" size="25"/>
              </span>
              <a :href="file.pathURL" :download="file.name" class="fileDownloadBtn">
                <Icon type="ios-cloud-download" title="Download" size="25"/>
              </a>
              <span @click="filePreview(file)" class="filePreviewBtn">
                <Icon type="md-eye" title="Preview" size="25"/>
              </span>
            </Card>
          </CheckboxGroup>
        </div>
        <div v-else style="text-align:center">
          <div style="color:lightgray;font-size:2em; font-weight:bold">No file or folder</div>
        </div>
        <div>
          <Row>
            <Col class="demo-spin-col" span="8" offset="8" v-show="spinAnimate==true">
                <Spin fix v-show="spinAnimate==true">
                    <Icon type="ios-loading" size=40 class="demo-spin-icon-load"></Icon>
                    <div>Loading</div>
                </Spin>
            </Col>
          </Row>
        </div>
      </div>
    </Card>
    <Modal v-model="renameFolderModal" title="Rename folder" ok-text="Assure" cancel-text="Cancel">
      <Form
        ref="renameValidate"
        :model="renameValidate"
        :rules="renameRuleValidate"
        :label-width="80"
      >
        <FormItem label="New name" prop="newName">
          <Input
            v-model="renameValidate.newName"
            :rows="4"
            placeholder="Enter the name for folder..."
          />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="renameFolderModal=false">Cancel</Button>
        <Button type="success" @click="renameFolder('renameValidate')">Rename</Button>
      </div>
    </Modal>
    <Modal v-model="newFolderModal" title="New folder">
      <Form
        ref="newValidate"
        :model="newValidate"
        :rules="newRuleValidate"
        :label-width="80"
        @submit.native.prevent
      >
        <FormItem label="Set name" prop="setName">
          <Input
            v-model="newValidate.setName"
            :rows="4"
            placeholder="Enter the name for folder..."
          />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="newFolderModal=false">Cancel</Button>
        <Button type="success" @click="addFolder('newValidate')">New</Button>
      </div>
    </Modal>
    <Modal v-model="uploadModal" title="Upload file" width="600">
      <Form
        ref="uploadValidate"
        :model="uploadValidate"
        :rules="uploadRuleValidate"
        :label-width="100"
        label-position="left"
      >
        <FormItem label="Privacy" prop="privacy">
          <RadioGroup v-model="uploadValidate.privacy" style="width:80%">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="Type" prop="type">
          <RadioGroup v-model="uploadValidate.type">
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
          <Input type="textarea" :rows="4" v-model="uploadValidate.description"/>
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
        <Button @click="uploadModal=false">Cancel</Button>
        <Button type="success" @click="subProjectUpload('uploadValidate')">Upload</Button>
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
    <Modal v-model="fileInfoModal" title="File Info">
      <Table
        :columns="selectedFileColumns"
        :data="selectedFileData"
        stripe
        border
        :show-header="false"
      ></Table>
      <div slot="footer">
        <Button type="primary" @click="fileInfoModal=false">OK</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  props: ["subProjectId","role"],
  created() {
    this.getSubProjectfileStruct();
  },
  data() {
    return {
      subProjectFileStruct: {},
      currentFolder: {
        folders: []
      },
      currentFileList: [],
      folderUIDStack: [],
      folderNameStack: [],
      newFolderModal: false,
      setFolderName: "",
      newValidate: {
        setName: ""
      },
      newRuleValidate: {
        setName: [
          {
            required: true,
            message: "The name can't be null.",
            trigger: "blur"
          }
        ]
      },
      renameFolderInfo: {},
      renameFolderModal: false,
      renameValidate: {
        newName: ""
      },
      renameRuleValidate: {
        newName: [
          {
            required: true,
            message: "The name can't be null.",
            trigger: "blur"
          }
        ]
      },
      uploadModal: false,
      uploadValidate: {
        privacy: "private",
        type: "data",
        description: ""
      },
      uploadRuleValidate: {
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
      fileInfoModal: false,
      selectedFileColumns: [
        {
          title: "key",
          key: "key",
          minWidth: 10,
          width: 100
        },
        {
          title: "value",
          key: "value"
        }
      ],
      selectedFileData: [],
      panel: null,
      // 单选选中的名称数组
      chooseFilesArray: [],
      // loading动画
      spinAnimate: false,
      // 关于单选多选的按钮
      indeterminate: true,
      checkAll: false
    };
  },
  methods: {
    closePanel() {
      if (this.panel != null) {
        this.panel.close();
      }
    },
    getSubProjectfileStruct() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/getFileStrcut" +
            "?subProjectId=" +
            this.subProjectId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            var fileStruct = res.data;
            this.subProjectFileStruct = fileStruct;
            this.currentFolder = fileStruct;
            this.enterFolder(this.currentFolder);
          } else {
            this.$Message.warning("Get sub-project's info fail.");
          }
        })
        .catch(err => {
          this.$Message.warning("Get sub-project's info fail.");
        });
    },
    enterFolder(folder) {
      this.chooseFilesArray = [];
      this.checkAll = false;
      this.indeterminate = false;
      this.currentFolder = folder;
      this.folderUIDStack.push(this.currentFolder.uid);
      this.folderNameStack.push(this.currentFolder.name);
      this.getCurrentFilesInfo();
    },
    getCurrentFilesInfo() {
      var files = this.currentFolder.files;
      var count = files.length;
      var filesInfoList = [];
      if (files.length > 0) {
        this.axios
          .get(
            "/GeoProblemSolving/resource/inquiry" +
              "?key=scope.subProjectId" +
              "&value=" +
              this.subProjectId
          )
          .then(res => {
            if (res != "Fail") {
              var allFiles = res.data;
              for (var i = 0; i < allFiles.length; i++) {
                var subProjectFile = allFiles[i];
                for (var j = 0; j < files.length; j++) {
                  var folderFile = files[j];
                  if (subProjectFile.resourceId == folderFile.uid) {
                    subProjectFile.uploadTime = subProjectFile.uploadTime.substring(
                      0,
                      10
                    );
                    filesInfoList.push(subProjectFile);
                  }
                }
              }
              this.$set(this, "currentFileList", filesInfoList);
            } else {
              console.log("Get file info fail.");
            }
          })
          .catch(err => {
            console.log("Get file info fail.");
          });
      } else {
        this.$set(this, "currentFileList", filesInfoList);
      }
    },
    backforeFolder() {
      this.chooseFilesArray = [];
      this.checkAll = false;
      this.indeterminate = false;
      if (this.folderUIDStack.length > 1) {
        var foreFolderUid = this.folderUIDStack.pop();
        var foreForlderName = this.folderNameStack.pop();
        this.refreshCurrentAll(
          this.subProjectFileStruct,
          this.folderUIDStack[this.folderUIDStack.length - 1]
        );
      } else {
        this.$Message.warning("This is the root folder.");
      }
    },
    getFileInfo(file) {
      this.selectedFileData = [
        {
          key: "File name",
          value: file.name
        },
        {
          key: "Description",
          value: file.description
        },
        {
          key: "Type",
          value: file.type
        },
        {
          key: "File size",
          value: file.fileSize
        },
        {
          key: "Uploader",
          value: file.uploaderName
        }
      ];
      this.fileInfoModal = true;
    },
    addFolderModalShow() {
      this.newValidate.setName = "";
      this.newFolderModal = true;
    },
    addFolder(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var currentFolderUid = this.currentFolder.uid;
          var subProjectId = this.subProjectId;
          var newFolderName = this.newValidate.setName;
          this.axios
            .post(
              "/GeoProblemSolving/subProject/createFolder" +
                "?subProjectId=" +
                subProjectId +
                "&parentId=" +
                currentFolderUid +
                "&folderName=" +
                newFolderName
            )
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.subProjectFileStruct = res.data;
                this.refreshCurrentAll(res.data, this.currentFolder.uid);
                this.newFolderModal = false;
              } else {
                this.$Message.warning("New folder fail.");
              }
            })
            .catch(err => {
              this.$Message.warning("New folder fail.");
            });
        }
      });
    },
    refreshCurrentFolder(folder, uid) {
      if (folder.uid == uid) {
        this.currentFolder = Object.assign({}, folder);
        return;
      } else {
        var subFolder = folder.folders;
        for (let i = 0; i < subFolder.length; i++) {
          this.refreshCurrentFolder(subFolder[i], uid);
        }
      }
    },
    refreshCurrentAll(folder, uid) {
      this.refreshCurrentFolder(folder, uid);
      this.getCurrentFilesInfo();
    },
    deleteFolder(folder) {
      if (confirm("Are you sure to delete this folder?")) {
        var currentFolderUid = this.currentFolder.uid;
        var subProjectId = this.subProjectId;
        var deleteFolderUid = folder.uid;
        this.axios
          .post(
            "/GeoProblemSolving/subProject/deleteFolder" +
              "?subProjectId=" +
              subProjectId +
              "&parentId=" +
              currentFolderUid +
              "&folderUid=" +
              deleteFolderUid
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data != "Fail") {
              this.subProjectFileStruct = res.data;
              this.refreshCurrentAll(res.data, this.currentFolder.uid);
              //此处添加从项目内删除
            } else {
              this.$Message.warning("Delete folder fail.");
            }
          })
          .catch(err => {
            this.$Message.warning("Delete folder fail.");
          });
      }
    },
    renameFolderModalShow(folder) {
      this.renameFolderInfo = folder;
      this.renameValidate.newName = "";
      this.renameFolderModal = true;
    },
    renameFolder(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var currentFolderUid = this.currentFolder.uid;
          var subProjectId = this.subProjectId;
          var oldFolderInfo = this.renameFolderInfo;
          var newName = this.renameValidate.newName;
          this.axios
            .post(
              "/GeoProblemSolving/subProject/renameFolder" +
                "?subProjectId=" +
                subProjectId +
                "&parentId=" +
                currentFolderUid +
                "&folderName=" +
                newName +
                "&folderUid=" +
                oldFolderInfo.uid
            )
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.subProjectFileStruct = res.data;
                this.refreshCurrentAll(res.data, this.currentFolder.uid);
              } else {
                this.$Message.warning("Rename fail.");
              }
            })
            .catch(err => {
              this.$Message.warning("Rename fail.");
            });
          this.renameFolderModal = false;
        }
      });
    },
    uploadModalShow() {
      this.uploadValidate = {
        privacy: "private",
        type: "data",
        description: ""
      };
      this.toUploadFiles = [];
      this.uploadModal = true;
    },
    gatherFile(file) {
      let that = this;
      if (that.toUploadFiles.length >= 5) {
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
        that.toUploadFiles.push(file);
      }
      return false;
    },
    delFileList(index) {
      this.toUploadFiles.splice(index, 1);
    },
    subProjectUpload(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var uploadFiles = this.toUploadFiles;
          if (uploadFiles.length > 0) {
            this.uploadModal = false;
            var formData = new FormData();
            for (var i = 0; i < uploadFiles.length; i++) {
              formData.append("file", uploadFiles[i]);
            }
            formData.append("description", this.uploadValidate.description);
            formData.append("type", this.uploadValidate.type);
            formData.append("uploaderId", this.$store.getters.userInfo.userId);
            formData.append("belong", sessionStorage.getItem("subProjectName"));
            let scopeObject = {
              projectId: "",
              subProjectId: this.$store.getters.subProject.subProjectId,
              moduleId: ""
            };
            formData.append("scope", JSON.stringify(scopeObject));
            formData.append("privacy", this.uploadValidate.privacy);
            formData.append(
              "subProjectId",
              this.$store.getters.subProject.subProjectId
            );
            formData.append("parentId", this.currentFolder.uid);
            this.progressModalShow = true;
            this.axios({
              url: "/GeoProblemSolving/resource/subProjectUpload",
              method: "post",
              onUploadProgress: progressEvent => {
                this.uploadProgress =
                  ((progressEvent.loaded / progressEvent.total) * 100) | 0;
              },
              data: formData
            })
              .then(res => {
                if (res.data != "Fail") {
                  this.subProjectFileStruct = res.data;
                  this.refreshCurrentAll(res.data, this.currentFolder.uid);
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
            this.$Message.warning("Upload file is null.");
          }
        }
      });
    },
    filePreview(fileInfo) {
      if (
        /\.(doc|docx|xls|xlsx|csv|ppt|pptx|zip)$/.test(
          fileInfo.name.toLowerCase()
        )
      ) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://172.21.212.72:8012/previewFile?url=" +
          "http://" +
          this.$store.state.IP_Port +
          fileInfo.pathURL;
        var toolURL =
          "<iframe src=" + url + ' style="width: 100%;height:100%"></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      } else if (/\.(mp4)$/.test(fileInfo.name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url = "http://" + this.$store.state.IP_Port + fileInfo.pathURL;
        var toolURL =
          "<video src=" +
          url +
          ' style="width: 100%;height:100%" controls></video>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      } else if (
        /\.(pdf|xml|json|md|gif|jpg|png)$/.test(fileInfo.name.toLowerCase())
      ) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url = "http://" + this.$store.state.IP_Port + fileInfo.pathURL;
        var toolURL =
          "<iframe src=" +
          url +
          ' style="width: 100%;height:100%" controls></iframe>';
        this.panel = jsPanel.create({
          headerControls: {
            smallify: "remove"
          },
          theme: "primary",
          footerToolbar: '<p style="height:10px"></p>',
          headerTitle: "Preview",
          contentSize: "800 600",
          content: toolURL,
          disableOnMaximized: true,
          dragit: {
            containment: 5
          },
          closeOnEscape: true
        });
        $(".jsPanel-content").css("font-size", "0");
      } else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Not supported file format."
        });
        return false;
      }
    },
    fileDelete(fileInfo) {
      if (confirm("Are you sure to delete this file?")) {
        var currentFolderUid = this.currentFolder.uid;
        var subProjectId = this.subProjectId;
        var deleteFileUid = fileInfo.resourceId;
        this.axios
          .post(
            "/GeoProblemSolving/subProject/deleteFile" +
              "?subProjectId=" +
              subProjectId +
              "&parentId=" +
              currentFolderUid +
              "&fileUid=" +
              deleteFileUid
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data != "Fail") {
              this.subProjectFileStruct = res.data;
              this.refreshCurrentAll(res.data, this.currentFolder.uid);
            } else {
              this.$Message.warning("Delete file fail.");
            }
          })
          .catch(err => {
            this.$Message.warning("Delete file fail.");
          });
      }
    },
    download(blobUrl) {
      const a = document.createElement("a");
      a.style.display = "none";
      a.download = "package.zip";
      a.href = blobUrl;
      a.click();
      document.body.removeChild(a);
    },
    handleCheckAll() {
      if (this.indeterminate) {
        this.checkAll = false;
      } else {
        this.checkAll = !this.checkAll;
      }
      this.indeterminate = false;
      if (this.checkAll) {
        this.currentFileList.forEach(item => {
          this.chooseFilesArray.push(item["pathURL"]);
        });
      } else {
        this.chooseFilesArray = [];
      }
    },
    checkAllGroupChange(data) {
      if (data.length == this.currentFileList.length) {
        this.indeterminate = false;
        this.checkAll = true;
      } else if (data.length > 0) {
        this.indeterminate = true;
        this.checkAll = false;
      } else {
        this.indeterminate = false;
        this.checkAll = false;
      }
    },
    downloadSelectFile() {
      let choosefileUrls = this.chooseFilesArray.toString();
      if (choosefileUrls != "") {
        this.spinAnimate = true;
        this.axios({
          method: "post",
          url:
            "/GeoProblemSolving/resource/packageZIP?fileURLs=" + choosefileUrls,
          responseType: "blob"
        })
          .then(res => {
            if (res.status == 200) {
              this.spinAnimate = false;
              const blobUrl = window.URL.createObjectURL(res.data);
              if (blobUrl != "") {
                this.download(blobUrl);
              }
            }
          })
          .catch(err => {});
      } else {
        alert("you don't choose any file!");
      }
    }
  }
};
</script>
