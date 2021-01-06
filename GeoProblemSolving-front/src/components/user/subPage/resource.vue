<template>
  <div>
    <Row>
      <Col span="20">
        <Card>
          <CheckboxGroup style="display: inline-block">
            <Checkbox label="noticeList">
              <span>Private</span>
              <span class="badge">1</span>
            </Checkbox>
            <Checkbox label="replyList">
              <span>Public</span>
              <span class="badge">2</span>
            </Checkbox>
            <!--          <Checkbox label="applyList">-->
            <!--            <span>Apply</span>-->
            <!--            <span class="badge">1</span>-->
            <!--          </Checkbox>-->
          </CheckboxGroup>
          <span style="margin: 0 20px"></span>
          <CheckboxGroup style="display: inline-block">
            <Checkbox label="read">
              <span>Owned by me</span>
              <span class="badge">3</span>
            </Checkbox>
            <Checkbox label="unRead">
              <span>Shared with me</span>
              <span class="badge">2</span>
            </Checkbox>

          </CheckboxGroup>
        </Card>

        <div>
          <Card>
            <p slot="title">Resource List</p>
            <div slot="extra">

<!--              <Icon type="md-unlock" size="25" color="green"/>-->
<!--              <Icon type="md-lock" size="25" color="red"/>-->
              <Icon type="md-cloud-upload"
                    size="25"
                    @click="uploadModal = true"
                    style="cursor: pointer"
              />
              <Icon type="md-share-alt"
                    size="25"
                    style="cursor: pointer"
              />
              <Icon type="ios-trash-outline" color="red"
                    size="25"
                    style="cursor: pointer"
                    title="Delete"
                    @click="delResItem"
              />
            </div>
            <Table
              ref="selection" stripe
              :columns="colName"
              :data="resourceList"
              @on-select="selectedRes"
            >
            </Table>
            <div>
              <Card>

              </Card>
            </div>
          </Card>
        </div>
      </Col>

      <Col span="4">
        <Card dis-hover class="historyLine">
          <p slot="title">Event line</p>
          <div class="timeLineStyle">
            <vue-scroll :ops="ops">
              <!--            <Timeline>-->
              <!--              <div v-if="userEventList.length==0">-->
              <!--                <div style="display:flex;justify-content:center">-->
              <!--                  <Icon type="md-alert" size="40" color="gray" />-->
              <!--                </div>-->
              <!--                <br />-->
              <!--                <div style="display:flex;justify-content:center">-->
              <!--                  <h3-->
              <!--                    style="text-align:center;width:80%"-->
              <!--                  >Sorry, there are no events now.</h3>-->
              <!--                </div>-->
              <!--              </div>-->
              <!--              <TimelineItem-->
              <!--                v-for="(item,index) in userEventList"-->
              <!--                :key="index"-->
              <!--                v-show="userEventList.length>0"-->
              <!--              >-->
              <!--                <strong>-->
              <!--                  <p class="time">{{item.createTime}}</p>-->
              <!--                </strong>-->
              <!--                <p class="content">{{item.description}}</p>-->
              <!--              </TimelineItem>-->
              <!--            </Timeline>-->
            </vue-scroll>
          </div>
        </Card>
      </Col>
    </Row>


    <Modal v-model="uploadModal" title="Upload file" width="600">
      <!--      :rules="uploadRuleValidate"-->
      <Form
        ref="resFormItems"
        :model="resFormItems"
        :rules="resRuleValidate"
        :label-width="100"
        label-position="left"
      >
        <FormItem label="Privacy" prop="privacy">
          <RadioGroup v-model="resFormItems.privacy" style="width:80%">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>

        <FormItem label="Type" prop="type">
          <RadioGroup v-model="resFormItems.type">
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
          <Input type="textarea" :autosize="{minRows: 2, maxRows: 5}" v-model="resFormItems.description"/>
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
        <Button type="success" @click="resourceUpload('resFormItems')">Upload</Button>
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
  </div>
</template>

<script>
  export default {
    name: "resource",
    data() {
      return {
        toUploadFiles: [],
        uploadModal: false,
        progressModalShow: false,
        uploadProgress: 0,
        ops: {
          bar: {
            background: "#808695"
          }
        },
        colName: [
          {
            type: 'selection',
            width: 60,
            align: 'center'
          },
          {
            title: "Title",
            key: 'name'
          },
          {
            title: 'Type',
            key: 'type'
          },
          {
            title: 'Author',
            key: 'uploaderName'
          },
          {
            title: 'Privacy',
            key: 'privacy'
          },
          {
            title: 'Created Time',
            key: 'uploadTime'
          }
        ],
        resourceList: [],
        selectedResList: [],
        resFormItems: {
          privacy: "private",
          type: "data",
          description: ""
        },
        resRuleValidate: {
          privacy: [
            {required: true, message: "File privacy cannot be empty", trigger: "blur"}
          ],
          type: [
            {required: true, message: "File type cannot be empty", trigger: "blur"}
          ],
        }

      }
    },
    mounted() {
      this.getUserResource();
    },
    methods: {
      selectedRes: function(selection){
        this.selectedResList = selection;
      },
      delResItem: function(){
      },
      getUserResource() {
        this.axios
          .get(
            "/GeoProblemSolving/resource/inquiry" +
            "?key=uploaderId" +
            "&value=" +
            this.$store.getters.userId
          )
          .then(res => {
            if (res.data != "None" && res.data != "Fail") {
              this.resourceList = res.data;
            } else if (res.data == "None") {
              this.resourceList = [];
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      },
      shareRes: function (sharedUserId) {
      },
      gatherFile: function (file) {
        let that = this;
        if (that.toUploadFiles.length >= 5) {
          if (this.fileCountTimer != null) {
            clearTimeout(this.fileCountTimer);
          }
          this.fileCountTimer = setTimeout(() => {
            this.$Message.info("Max upload file number: 5");
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
      delFileList: function (index) {
        this.toUploadFiles.splice(index, 1);
      },
      resourceUpload: function (resForm) {
        this.$refs[resForm].validate(valid => {
          if (valid) {
            let uploadFiles = this.toUploadFiles;
            if (uploadFiles.length > 0) {
              this.uploadModal = false;
              //FormData
              let formData = new FormData();
              for (let i = 0; i < uploadFiles.length; i++) {
                formData.append("file", uploadFiles[i]);
              }
              formData.append("description", this.resFormItems.description);
              formData.append("type", this.resFormItems.type);
              formData.append("uploaderId", this.$store.getters.userId);
              formData.append("privacy", this.resFormItems.privacy);
              this.progressModalShow = true;
              this.axios({
                url: "/GeoProblemSolving/resource/upload",
                method: "post",
                onUploadProgress: progressEvent => {
                  this.uploadProgress =
                    ((progressEvent.loaded / progressEvent.total) * 100) | 0;
                },
                data: formData
              })
                .then(res => {
                  if (res.data != "Fail") {
                    let uploadList = res.data.uploaded;
                    let failedList = res.data.failed;
                    let sizeOverList = res.data.sizeOver;
                    for (let i = 0; i < uploadList.length; i++) {
                      this.resourceList.push(uploadList[i]);
                    }
                    if (sizeOverList.length > 0) {
                      this.$Notice.warning({
                        title: "Files too large.",
                        render: h => {
                          return h("span", sizeOverList.join(";"));
                        }
                      })
                    }
                    if (failedList.length > 0) {
                      this.$Notice.error({
                        title: "Upload Fail.",
                        render: h => {
                          return h("span", failedList.join(";"))
                        }
                      })
                    }
                  } else {
                    this.$Message.error("Upload Fail.");
                  }
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                })
                .catch(err => {
                  this.progressModalShow = false;
                  this.$Message.error("Upload Fail.");
                  this.uploadProgress = 0;
                })
            } else {
              this.$Message.warning("Upload file is null.")
            }
          }
        })
      }
    },
  }
</script>

<style scoped>
  .badge {
    display: inline-block;
    min-width: 10px;
    padding: 3px 7px;
    font-size: 12px;
    font-weight: bold;
    line-height: 1;
    color: #fff;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    background-color: #999;
    border-radius: 10px;
  }
</style>
