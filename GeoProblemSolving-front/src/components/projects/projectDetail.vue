<style scoped>
.main {
  background-color: rgb(219, 213, 213);
  width: 100%;
}
.detail {
  height: auto;
}
.detail_image {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 300px;
}
.detail_image img {
  max-width: 90%;
  max-height: 300px;
}
.detail_description {
  padding: 20px;
  max-height: auto;
}
.memberPanel {
  padding: 20px;
}
.subprojectPanel {
  height: auto;
  display: flex;
  align-items: center;
}
.subProjectListStyle {
  height: auto;
  align-items: center;
  width: 100%;
}
.subProjectTitle:hover {
  cursor: pointer;
}
.subProjectDescription {
  text-indent: 25px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  /*! autoprefixer: off */
  -webkit-box-orient: vertical;
  /* autoprefixer: on */
  -webkit-line-clamp: 5;
}
.subProjectStyle {
  height: auto;
  margin: 10px;
}
/* 按钮旋浮样式，创建子项目、邀请成员、上传文件的按钮悬浮时的效果 */
.subProjectBtn:hover,
.inviteBtn:hover,
.uploadBtn:hover {
  background-color: #47cb89;
  color: white;
}
/* 移除成员时鼠标悬浮的效果 */
.deleteBtn:hover {
  background-color: #ed4014;
  color: white;
}
.subProjectBtn,
.uploadBtn,
.inviteBtn {
  color: black;
}
/* 资源上传more按钮的悬浮样式 */
.moreBtn:hover {
  background-color: #57a3f3;
  color: white;
}
/* 子项目标题的样式 */
.subProjectTitle {
  height: 40px;
  line-height: 40px;
  font-size: 20px;
  /* max-width: 70px; */
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 60%;
}
/* 子项目卡片的标题样式 */
.projectCardTitle {
  font-size: 25px;
  height: 40px;
  line-height: 40px;
  max-width: 50%;
}
/* 子项目卡片操作界面按钮的样式 */
.projectCardTitleExtra {
  height: 40px;
  line-height: 40px;
  display: flex;
  width: 50%;
}
.projectCardTitleExtra span {
  cursor: pointer;
}
.projectCardTitleExtra span:last-child {
  margin-left: 20px;
}
.projectDescription {
  height: 80px;
}
.projectIntroduction {
  height: 220px;
  padding-top: 10px;
}
/* 项目描述的内容显示样式 */
.projectDescriptionContent {
  height: 56px;
  overflow-y: auto;
  text-indent: 2em;
  padding: 0 5px;
  word-break: break-word;
}
/* 项目介绍的内容样式 */
.projectIntroductionContent {
  height: 196px;
  overflow-y: auto;
  text-indent: 2em;
  padding: 0 5px;
  word-break: break-word;
}
.memberPanelTitle {
  font-size: 25px;
  height: 40px;
  line-height: 40px;
}
.memberList {
  /* display: flex; */
  align-items: center;
}
.subProjectsPanel {
  padding: 20px;
}
.subProjectsTitle {
  font-size: 25px;
  height: 40px;
  line-height: 40px;
}
.subProjectTitleOperatePanel {
  width: 30%;
  display: flex;
  align-items: center;
  height: 40px;
}
.subProjectTextInfo {
  height: 20px;
  align-items: center;
  display: flex;
  justify-content: flex-start;
}
.subProjectTextInfo:last-child {
  margin-top: 5px;
}
.subProjectTextInfo span {
  height: 20px;
  margin-left: 5%;
}
/* 图片修改的样式 */
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
.uploadBox {
  display: inline-block;
  width: 58px;
  height: 58px;
  line-height: 58px;
  border-width: 0.75px;
  border-style: dashed;
  border-color: lightslategray;
}
.inline_style {
  display: flex;
}
/* 图片修改的样式结束 */
</style>
<template>
  <div class="main">
    <Row>
      <Col span="22" offset="1">
        <div class="detail_description">
          <Card>
            <p slot="title" style class="projectCardTitle">{{currentProjectDetail["title"]}}</p>
            <div slot="extra" class="projectCardTitleExtra">
              <span
                type="default"
                title="edit"
                @click="editModalShow(currentProjectDetail['projectId'])"
                v-show="judgeIsManager(projectManager.userId)"
              >
                <Icon type="ios-create" :size="30"></Icon>
              </span>
              <span
                type="default"
                title="remove"
                @click="deleteProjectShow()"
                v-show="judgeIsManager(projectManager.userId)"
              >
                <Icon type="md-close" :size="30"></Icon>
              </span>
            </div>
            <Row>
              <Col :xs="12" :sm="10" :md="9" :lg="8">
                <div class="detail_image">
                  <img
                    :src="currentProjectDetail.picture"
                    v-if="currentProjectDetail.picture!=''&&currentProjectDetail.picture!='undefined'"
                  />
                  <avatar
                    :username="currentProjectDetail.title"
                    :size="260"
                    :title="currentProjectDetail.title"
                    :rounded="false"
                    v-else
                  ></avatar>
                </div>
              </Col>
              <Col :xs="12" :sm="14" :md="15" :lg="16">
                <div class="projectDescription">
                  <h3>Description</h3>
                  <div class="projectDescriptionContent">{{currentProjectDetail.description}}</div>
                </div>
                <div class="projectIntroduction">
                  <h3>Introduction</h3>
                  <div class="projectIntroductionContent">{{currentProjectDetail.introduction}}</div>
                </div>
              </Col>
            </Row>
          </Card>
        </div>
        <div class="memberPanel">
          <Card :bordered="false">
            <p slot="title" class="memberPanelTitle">Members</p>
            <div slot="extra" style="height:40px">
              <Poptip trigger="hover" content="Invite other members" placement="right">
                <Button
                  class="inviteBtn"
                  v-show="this.currentProjectDetail.isManager"
                  @click="inviteModalShow()"
                >
                  <Icon type="md-person-add" size="20" />
                </Button>
              </Poptip>
              <Poptip trigger="hover" content="Remove members" placement="right">
                <Button
                  class="deleteBtn"
                  type="default"
                  v-show="this.currentProjectDetail.isManager"
                  @click="deleteMemberModal = true"
                >
                  <Icon type="md-remove" size="20" />
                </Button>
              </Poptip>
            </div>
            <div class="memberList">
              <Button
                type="success"
                @click="gotoPersonalPage(projectManager.userId)"
                style="margin: 5px"
              >{{this.projectManager.userName}}</Button>
              <Button
                v-for="member in currentProjectDetail.members"
                type="primary"
                :key="member.index"
                style="margin: 5px"
                @click="gotoPersonalPage(member.userId)"
              >{{member.userName}}</Button>
            </div>
          </Card>
        </div>
        <!-- 关于删除成员的modal -->
        <Modal
          v-model="deleteMemberModal"
          title="Remove members from project"
          @on-ok="removeAlertShow(removeMemberId)"
          @on-cancel
          ok-text="Ok"
          cancel-text="Cancel"
          width="600"
        >
          <div>
            <RadioGroup v-model="removeMemberName">
              <span
                v-for="(member,index) in currentProjectDetail.members"
                @click="getRemoveMemberId(member.userId)"
              >
                <Radio :key="member.index" :label="member.userName"></Radio>
              </span>
            </RadioGroup>
            <!-- <Tag v-for="member in currentProjectDetail.members" :key="member.index">{{member.userName}}</Tag> -->
          </div>
        </Modal>
        <Modal
          v-model="alertModalShow"
          title="Alert"
          @on-ok="removeMember"
          @on-cancel
          ok-text="assure"
          cancel-text="cancel"
        >
          <p>Do you really want to delete this member form your project?</p>
        </Modal>
        <!-- 关于邀请其他成员的modal,禁用点击其他空白处误关模态框 -->
        <Modal
          v-model="inviteModal"
          @on-ok="invite('emailInfo')"
          @on-cancel
          ok-text="Assure"
          cancel-text="Cancel"
          title="Invite others join in the Project"
          :mask-closable="false"
          width="800"
        >
          <Form ref="emailInfo" :model="emailInfo" :rules="emailInfoRule" :label-width="200" inline>
            <FormItem label="Recipient" prop="inputEmail" style="width:100%">
              <AutoComplete
                v-model="emailInfo.inputEmail"
                @on-search="emialAutoFill"
                placeholder="input email and press enter button the email will be added below"
                style="width:70%"
              >
                <Option v-for="item in prompt" :value="item" :key="item">{{ item }}</Option>
              </AutoComplete>
            </FormItem>
            <FormItem label="Title" prop="emailTitle" style="width:100%">
              <Input
                v-model="emailInfo.emailTitle"
                placeholder="set the email title to recivers by your self"
                style="width:70%"
              />
            </FormItem>
            <FormItem label="Content" prop="emailContent" style="width:100%">
              <Input type="textarea" style="width:70%" :rows="4" v-model="emailInfo.emailContent" />
            </FormItem>
          </Form>
        </Modal>
        <div class="subProjectsPanel">
          <Card :bordered="false">
            <p slot="title" class="subProjectsTitle">Subprojects</p>
            <div slot="extra">
              <Poptip trigger="hover" content="Create a new subproject" placement="right">
                <Button
                  class="subProjectBtn"
                  @click="subProjectModal = true"
                  v-show="this.currentProjectDetail.isManager||this.currentProjectDetail.isMember"
                >
                  <Icon type="md-add" size="20" />
                </Button>
              </Poptip>
              <Modal
                v-model="subProjectModal"
                ok-text="Create"
                cancel-text="Cancel"
                @on-ok="createSubProject('createSubProjectForm')"
                title="Create a new subproject"
                width="800px"
                :mask-closable="false"
              >
                <div>
                  <Form
                    ref="createSubProjectForm"
                    :model="createSubProjectForm"
                    :rules="createSubprojectRuleValidate"
                    :label-width="120"
                    @submit.native.prevent
                  >
                    <FormItem label="Title" prop="subProjectTitle">
                      <Input
                        type="text"
                        v-model="createSubProjectForm.subProjectTitle"
                        placeholder="Enter subproject title (less than 60 characters) ..."
                        @keyup.enter.native
                      ></Input>
                    </FormItem>
                    <FormItem label="Description" prop="subProjectDescription">
                      <Input
                        v-model="createSubProjectForm.subProjectDescription"
                        placeholder="Enter subproject description..."
                        :rows="4"
                        type="textarea"
                      ></Input>
                    </FormItem>
                  </Form>
                </div>
              </Modal>
            </div>
            <div class="subprojectPanel">
              <div class="subProjectListStyle">
                <div v-for="(subProject,index) in subProjectList" :key="subProject.index">
                  <Col :lg="{span:6}" :md="24" :sm="24" :xs="24">
                    <div @click="goWorkspace(subProject)" style="cursor:pointer">
                      <Card class="subProjectStyle">
                        <div class="subProjectTitle" slot="title" :title="subProject.title">
                          <span>{{subProject["title"]}}</span>
                        </div>
                        <div slot="extra" class="subProjectTitleOperatePanel">
                          <div style="display:flex">
                            <template>
                              <span
                                v-show="subProject.isManager == true"
                                @click.stop="handOverSubProjectShow(index)"
                                title="Exchange manager"
                              >
                                <Icon type="md-person" :size="20" />
                              </span>
                              <span
                                v-show="subProject['isMember'] == false && subProject['isManager'] == false && currentProjectDetail.privacy != 'Public'"
                                @click.stop="joinSubProject(subProject)"
                                title="Apply to be a member of this subproject"
                              >
                                <Icon type="md-add" />
                              </span>
                              <span
                                style="margin-left:10px"
                                v-show="subProject['isManager'] == true"
                                @click.stop="editSubProjectShow(index)"
                                title="Edit"
                              >
                                <Icon type="ios-create" :size="20" />
                              </span>
                              <span
                                style="margin-left:10px"
                                v-show="subProject['isManager'] == true"
                                @click.stop="deleteSubProjectShow(index)"
                                title="Delete"
                              >
                                <Icon type="md-close" :size="20" />
                              </span>
                            </template>
                          </div>
                        </div>
                        <div style="height:100px">
                          <p
                            class="subProjectDescription"
                            :title="subProject['description']"
                          >{{subProject["description"]}}</p>
                        </div>
                        <br />
                        <div class="subProjectTextInfo">
                          <Icon type="md-body" :size="20" />Manager
                          <span>{{subProject.managerName}}</span>
                        </div>
                        <div class="subProjectTextInfo">
                          <Icon type="md-clock" :size="20" />Creation Time
                          <span>{{subProject.createTime.split(' ')[0]}}</span>
                        </div>
                      </Card>
                    </div>
                  </Col>
                </div>
              </div>
              <Modal
                v-model="handOverSubProjectModal"
                title="Appoint new manager"
                ok-text="Ok"
                cancel-text="Cancel"
                @on-ok="handOverSubProject()"
                width="500px"
                :mask-closable="false"
              >
                <div style="height:100px;background:azure">
                  <RadioGroup v-model="newManagerId">
                    <Radio
                      v-for="(member,index) in subProjectMembers"
                      :key="member.index"
                      :label="member.userId"
                    >
                      <span>{{member.userName}}</span>
                    </Radio>
                  </RadioGroup>
                </div>
              </Modal>
              <Modal
                v-model="editSubProjectModal"
                ok-text="Submit"
                cancel-text="Cancel"
                @on-ok="editSubProject('editSubProjectForm')"
                title="Edit the information of this subproject"
                width="800px"
                :mask-closable="false"
              >
                <div>
                  <Form
                    ref="editSubProjectForm"
                    :model="editSubProjectForm"
                    :rules="editSubProjectFormRuleValidate"
                    :label-width="120"
                  >
                    <FormItem label="Name" prop="subProjectTitleEdit">
                      <Input v-model="editSubProjectForm.subProjectTitleEdit"></Input>
                    </FormItem>
                    <FormItem label="Description" prop="subProjectDescriptionEdit">
                      <Input
                        v-model="editSubProjectForm.subProjectDescriptionEdit"
                        :rows="4"
                        type="textarea"
                      ></Input>
                    </FormItem>
                  </Form>
                </div>
              </Modal>
              <Modal
                v-model="deleteSubProjectModal"
                title="Delete sub project"
                ok-text="Confirm"
                cancel-text="Cancel"
                @on-ok="deleteSubProject()"
                width="800px"
                :mask-closable="false"
              >
                <p>Once the deletion is confirmed, all step and resource information under the subsystem will be deleted. Please choose carefully.</p>
              </Modal>
            </div>
          </Card>
        </div>
        <div class="resourcePanel" style="padding:20px">
          <Card>
            <p slot="title" style="font-size:25px;height:40px;line-height:40px;">Resources</p>
            <div slot="extra" style="display:flex;align-items:center;height:40px" class="popCenter" v-show="currentProjectDetail.isManager||currentProjectDetail.isMember">
              <Button
                id="upload"
                type="default"
                @click="uploadFileModalShow()"
                class="uploadBtn"
                title="Upload resource"
              >
                <Icon type="md-cloud-upload" size="20" />
              </Button>
              <Button
                class="moreBtn"
                type="default"
                style="margin-left: 10px"
                @click="toResourceList()"
                title="more"
              >
                <Icon type="md-more" />
              </Button>
            </div>
            <div style="text-align:center">
              <!-- <Form ref="formCustom" :model="formCustom" :rules="ruleCustom" :label-width="80">
                <FormItem label="Search specified resource" prop="searchText">
                    <Input type="password" v-model="formCustom.passwd"></Input>
                </FormItem>
              </Form>-->
            </div>
            <div style="height:300px;overflow-y:scroll">
              <Table
                :columns="resourceTableColName"
                :data="this.projectResourceList"
                v-show="this.projectResourceList!=[]&&this.projectResourceList!='None'"
              >
                <template slot-scope="{ row }" slot="name">
                  <strong>{{ row.name }}</strong>
                </template>

                <template slot-scope="{ row, index }" slot="action">
                  <a
                    :href="projectResourceList[index].pathURL"
                    :download="projectResourceList[index].name"
                    title="Download"
                  >
                    <Icon type="md-download" :size="20" />
                  </a>
                  <span @click="show(index)" style="margin-left:10px" title="Preview">
                    <Icon type="md-eye" :size="20" color="#2d8cf0" style="cursor:pointer" />
                  </span>
                </template>
              </Table>
            </div>
          </Card>
        </div>
        <!-- 上传文件按钮的模态框 -->
        <Modal
          v-model="uploadFileModal"
          title="Upload file"
          width="800px"
          :mask-closable="false"
          @on-ok="filesUpload('fileUploadForm')"
          ok-text="Submit"
          cancel-text="Cancel"
        >
          <div>
            <Form
              ref="fileUploadForm"
              :model="fileUploadForm"
              :rules="fileUploadFormRuleValidate"
              :label-width="100"
            >
              <FormItem label="Privacy" prop="privacy">
                <RadioGroup v-model="fileUploadForm.privacy" style="width:80%">
                  <Radio label="private">Private</Radio>
                  <Radio label="public">Public</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="Type" prop="type">
                <RadioGroup v-model="fileUploadForm.type">
                  <Radio label="data">Data</Radio>
                  <Radio label="image">Images</Radio>
                  <Radio label="video">Videos</Radio>
                  <Radio label="paper">Papers</Radio>
                  <Radio label="document">Documents</Radio>
                  <Radio label="model">Models</Radio>
                  <Radio label="others">Others</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem label="Description" prop="description">
                <Input
                  type="textarea"
                  :rows="4"
                  v-model="fileUploadForm.description"
                  placeholder="Enter file description"
                />
              </FormItem>
            </Form>
          </div>
          <Upload :max-size="1024*1024" multiple type="drag" :before-upload="gatherFile" action="-">
            <div style="padding: 20px 0">
              <Icon type="ios-cloud-upload" size="52" style="color: #3399ff"></Icon>
              <p>
                Click or drag files here to upload(The file size must control in
                <span
                  style="color:red"
                >1GB</span>)
              </p>
            </div>
          </Upload>

          <div style="padding:0 10px 0 10px">
            <ul v-for="(list,index) in file" :key="index">
              <li style="display:flex">
                File name:
                <span
                  style="font-size:10px;margin: 0 5px 0 5px"
                  :title="list.name"
                >{{ list.name }}</span>
                (Size:
                <span
                  v-if="list.size<(1024*1024)"
                  style="font-size:10px;margin-right:10px"
                >{{(list.size/1024).toFixed(2)}}KB)</span>
                <span
                  v-else
                  style="font-size:10px;margin-right:10px"
                >{{(list.size/1024/1024).toFixed(2)}}MB)</span>
                <Icon
                  type="ios-close"
                  size="20"
                  @click="delFileList(index)"
                  style="display:flex;justify-content:flex-end;cursor:pointer"
                  title="Cancel"
                ></Icon>
              </li>
            </ul>
          </div>
          <!-- <h6 style="text-align:center;color:red">The file's size must control smaller than 1 GB.</h6> -->
        </Modal>
        <br />
      </Col>
      <Modal
        v-model="removeProjectModal"
        title="Delete warning "
        @on-ok="deleteProject"
        ok-text="Submit"
        cancel-text="Cancel"
      >
        <p>Do you want to delete this project? Please think twice before you choose.</p>
      </Modal>
      <!-- removeProjectModal -->
    </Row>
    <Modal
      v-model="editProjectModal"
      title="Edit project"
      @on-ok="editProjectSubmit('projectEditForm')"
      ok-text="Confirm"
      cancel-text="Cancel"
      :mask-closable="false"
      width="900px"
    >
      <div>
        <Form
          ref="projectEditForm"
          :model="projectEditForm"
          :rules="editProjectFormRuleValidate"
          :label-width="100"
        >
          <FormItem label="Category" prop="category">
            <RadioGroup v-model="projectEditForm.category">
              <Radio label="Terrestrial">Terrestrial System</Radio>
              <Radio label="Coastal">Coastal System</Radio>
              <Radio label="Marine">Marine System</Radio>
              <Radio label="Climate">Climate System</Radio>
              <Radio label="Ecological">Ecological System</Radio>
              <Radio label="Geological">Geological System</Radio>
              <Radio label="Human">Human-Activity</Radio>
              <Radio label="GISRS">GIS & RS</Radio>
              <Radio label="General">General</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="Title" prop="title">
            <Input v-model="projectEditForm.title" placeholder="Enter something..." />
          </FormItem>
          <FormItem label="Description" prop="description">
            <Input v-model="projectEditForm.description"></Input>
          </FormItem>
          <FormItem label="Introduction" prop="introduction">
            <Input v-model="projectEditForm.introduction" type="textarea" :rows="4"></Input>
          </FormItem>
          <FormItem label="Tag" prop="tag">
            <Input
              v-model="inputTag"
              placeholder="Enter some tag to introduce the project"
              style="margin-left:0.5%;width: 200px"
              @keyup.enter.native="addTag(inputTag)"
            />
            <Button
              icon="ios-add"
              type="dashed"
              size="small"
              style="margin-left:2.5%"
              @click="addTag(inputTag)"
            >Add Tag</Button>
            <br />
            <Tag
              color="primary"
              v-for="(tag,index) in projectEditForm.editTags"
              :key="index"
              closable
              @on-close="deleteTag(index)"
            >{{tag}}</Tag>
          </FormItem>
          <FormItem label="Privacy" prop="privacy">
            <RadioGroup v-model="projectEditForm.privacy">
              <Radio
                label="Public"
                title="Other users can find the group and see who has membership."
              ></Radio>
              <Radio
                label="Discoverable"
                title="Other users can find this group, but membership information is hidden."
              ></Radio>
              <Radio label="Private" title="Other users can not find this group."></Radio>
            </RadioGroup>
          </FormItem>
          <FormItem prop="image" label="Image" :label-width="100">
            <div class="inline_style">
              <div class="demo-upload-list" v-show="pictureUrl!=''">
                <template>
                  <img v-bind:src="pictureUrl" />
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
                <img :src="pictureUrl" v-if="visible" style="width: 100%" />
              </Modal>
            </div>
          </FormItem>
          <!-- img结束 -->
        </Form>
      </div>
      <div style="flex"></div>
    </Modal>
    <Modal
      v-model="progressModalShow"
      title="Upload Progress"
      :mask-closable="false"
      :closable="false"
    >
      <Progress :percent="uploadProgress"></Progress>
    </Modal>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
import VueClipboards from "./../../utils/VueClipboards";
export default {
  components: {
    Avatar
  },
  data() {
    return {
      projectEditForm: {
        description: "",
        introduction: "",
        title: "",
        privacy: "",
        category: "",
        editTags: ""
      },
      editProjectFormRuleValidate: {
        category: [
          {
            required: true,
            message: "The project description cannot be empty",
            trigger: "blur"
          }
        ],
        title: [
          {
            required: true,
            message: "The title cannot be empty and no more than 60 characters",
            trigger: "blur",
            max: 60
          }
        ],
        description: [
          {
            required: true,
            message:
              "The description cannot be empty and no more than 360 characters",
            trigger: "blur",
            max: 360
          }
        ],
        privacy: [
          {
            required: true,
            message: "The project privacy cannot be empty",
            trigger: "blur"
          }
        ],
        introduction: [
          {
            required: true,
            message: "The project introduction cannot be empty",
            trigger: "blur"
          }
        ]
      },
      //子项目表单
      createSubProjectForm: {
        subProjectTitle: "",
        subProjectDescription: ""
      },
      editSubProjectForm: {
        subProjectTitleEdit: "",
        subProjectDescriptionEdit: ""
      },
      fileUploadForm: {
        type: "data",
        privacy: "private",
        description: ""
      },
      fileUploadFormRuleValidate: {
        type: [
          {
            required: true,
            message: "The type cannot be empty",
            trigger: "blur"
          }
        ],
        privacy: [
          {
            required: true,
            message: "The privacy cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: true,
            type: "string",
            message: "Please inpput file description",
            trigger: "blur"
          }
        ]
      },
      editSubProjectFormRuleValidate: {
        subProjectTitleEdit: [
          {
            required: true,
            message: "The title can't be empty and no more than 60 characters",
            trigger: "blur",
            max: 60
          },
          {
            required: true,
            message: "The title can't be empty",
            trigger: "blur"
          }
        ],
        subProjectDescriptionEdit: [
          {
            required: true,
            message: "The description can't be empty",
            trigger: "blur"
          },
          {
            required: true,
            message: "The description can't be empty",
            trigger: "blur"
          }
        ]
      },
      createSubprojectRuleValidate: {
        subProjectTitle: [
          {
            required: true,
            message: "The title can't be empty and no more than 60 characters",
            trigger: "blur",
            type: "string",
            max: 60
          },
          {
            required: true,
            message: "The title can't be empty",
            trigger: "blur"
          }
        ],
        subProjectDescription: [
          {
            required: true,
            message: "The description can't be empty",
            trigger: "blur"
          },
          {
            required: true,
            message: "The description can't be empty",
            trigger: "blur"
          }
        ]
      },
      /*编辑项目专用的字段*/
      // tag为array类型
      editTags: [],
      inputTag: "",
      editProjectId: "",
      /* 编辑项目字段结束*/
      projectManager: {},
      //确定用户是否有更新项目的权限，控制是否显示编辑的按钮，只有创建者才有权对项目进行编辑
      copyProjectId: "",
      copyProjectTitle: "",
      currentProjectDetail: {},
      //移交权限给新的管理者
      handOverSubProjectModal: false,
      // 新管理者的Id
      newManagerId: "",
      // 子项目的列表
      subProjectList: [],
      // 创建子项目的模态框
      subProjectModal: false,
      // 子项目标题
      subProjectTitle: "",
      // 子项目描述
      subProjectDescription: "",
      // 子项目标题的编辑值
      subProjectTitleEdit: "",
      // 子项目描述的编辑值
      subProjectDescriptionEdit: "",
      // 点击编辑子项目按钮的模态框
      editSubProjectModal: false,
      // 点击删除子项目按钮的模态框
      deleteSubProjectModal: false,
      // 编辑的子项目再子项目列表的索引值
      editSubProjectindex: 0,
      // 点击邀请他人加入时的模态框
      inviteModal: false,
      // 发送邮件邀请时需要填写的对象（email，邮件标题，邮件内容）
      emailInfo: {
        inputEmail: "",
        emailTitle: "",
        emailContent: ""
      },
      // 填写邮件表单时验证的规则
      emailInfoRule: {
        inputEmail: [
          {
            required: true,
            message: "Mailbox cannot be empty",
            trigger: "blur"
          },
          {
            type: "email",
            message: "Incorrect email format",
            trigger: "blur"
          }
        ],
        emailTitle: [
          {
            required: true,
            message: "The title cannot be empty",
            trigger: "blur"
          }
        ],
        emailContent: [
          {
            required: true,
            message: "The content cannot be empty",
            trigger: "blur"
          }
        ]
      },
      // 发送邮件的格式
      emailAddStr:
        "<br>Please copy the url and access it to join us: <br>" +
        "http://" +
        this.$store.state.IP_Port +
        "/GeoProblemSolving/join/" +
        this.$route.params.id +
        "/",
      // 点击上传文件按钮时弹出的模态框
      uploadFileModal: false,
      // 上传的文件数组（支持多文件）
      file: [],
      // 获取的项目下已上传的资源的列表
      projectResourceList: [],
      // 项目下资源表格的表头信息
      resourceTableColName: [
        {
          title: "Name",
          key: "name",
          tooltip: true,
          sortable: true
        },
        {
          title: "Type",
          key: "type",
          width: 100,
          sortable: true
        },
        {
          title: "Size",
          key: "fileSize",
          width: 100,
          sortable: true
        },
        {
          title: "Description",
          key: "description",
          tooltip: true,
          sortable: true
        },
        {
          title: "Upload time",
          key: "uploadTime",
          width: 160,
          sortable: true
        },
        {
          title: "Action",
          slot: "action",
          width: 120,
          align: "center"
        }
      ],
      // 点击项目编辑按钮弹出的模态框
      editProjectModal: false,
      // 点击删除项目按钮弹出的模态框
      removeProjectModal: false,
      // 子项目成员数组（用于子项目权限转移时使用）
      subProjectMembers: [],
      // 邮箱输入尾缀格式自动补全的提示变量
      prompt: [],
      // 显示进度条的模态框
      progressModalShow: false,
      // 文件上传的进度
      uploadProgress: 0,
      // 本页面使用的jspanel预览工具的控制变量
      panel: null,
      // 上传文件个数限制定时器
      fileCountTimer: null,
      // 点击删除成员按钮时弹出的模态框
      deleteMemberModal: false,
      // 点击assure后弹出的删除警告模态框
      alertModalShow: false,
      // 删除成员的ID
      removeMemberId: "",
      // 选中的要删除的成员的名字
      removeMemberName: "",
      // 控制编辑项目时显示项目原图片显示的变量
      visible: false,
      // 项目的图片
      img: "",
      // 更换项目图片后后台返回的新图片的文件地址
      pictureUrl: "",
      // 当前准备加入子项目的详情
      currentSubProjectInfo: []
    };
  },
  mounted() {
    this.getAllSubProject();
    this.getAllResource();
    this.getProjectDetail();
    // 配置本页通知触发时距离顶部的高度，以及持续的时间
    this.$Message.config({
      top: 150,
      duration: 2
    });
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else if (vm.currentProjectDetail.privacy == "Public") {
        next();
      } else {
        var userId = vm.$store.getters.userId;
        var members = vm.currentProjectDetail.members;
        var isMember = false;
        for (var i = 0; i < members.length; i++) {
          if (members[i].userId == userId) {
            isMember = true;
            break;
          }
        }
        vm.currentProjectDetail.isMember = isMember;
        if (vm.currentProjectDetail.managerId == userId) {
          vm.currentProjectDetail.isManager = true;
        }
        if (!(isMember || vm.currentProjectDetail.isManager)) {
          vm.$Message.error("You have no property to access it");
          next("/projectlist");
          // vm.$router.go(-1);
        }
      }
    });
  },
  beforeRouteLeave(to, from, next) {
    if (this.panel != null) {
      this.panel.close();
    }
    next();
  },
  methods: {
    getProjectDetail() {
      let projectInfo = this.$store.getters.project;
      let pid = this.$route.params.id;
      if (JSON.stringify(projectInfo) != "{}" && projectInfo.projectId == pid) {
        this.currentProjectDetail = projectInfo;
        this.updateRelatedInfo();
      } else {
        let that = this;
        let queryObject = { key: "projectId", value: pid };
        try {
          $.ajax({
            url:
              "/GeoProblemSolving/project/inquiry" +
              "?key=" +
              queryObject["key"] +
              "&value=" +
              queryObject["value"],
            type: "GET",
            async: false,
            success: function(data) {
              if (data === "None") {
                that.currentProjectDetail = {
                  members: [],
                  introduction: "",
                  projectId: ""
                };
                that.updateRelatedInfo();
              } else {
                let projectInfo = data[0];
                projectInfo.isManager = that.managerIdentity(
                  projectInfo.managerId
                );
                projectInfo.isMember = that.memberIdentity(projectInfo.members);
                that.currentProjectDetail = projectInfo;
                that.$store.commit("setProjectInfo", projectInfo);
                that.updateRelatedInfo();
              }
            },
            error: function(err) {
              console.log("Get manager name fail.");
            }
          });
        } catch (err) {
          console.log(err);
        }
      }
    },
    updateRelatedInfo() {
      // 邀请他人加入项目的form的复制项目id与项目名的按钮
      this.copyProjectId = this.currentProjectDetail.projectId;
      this.copyProjectTitle = this.currentProjectDetail.title;
      this.projectManager.userId = this.currentProjectDetail.managerId;
      this.projectManager.userName = this.currentProjectDetail.managerName;

      sessionStorage.setItem("projectId", this.currentProjectDetail.projectId);
      sessionStorage.setItem("projectName", this.currentProjectDetail.title);
    },
    // identify the manager of project
    managerIdentity(managerId) {
      if (managerId === this.$store.getters.userId) {
        return true;
      } else {
        return false;
      }
    },
    memberIdentity(members) {
      let isMember;
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.getters.userId) {
          isMember = true;
          break;
        }
      }
      if (isMember) {
        return true;
      } else {
        return false;
      }
    },
    //前往工作空间
    goWorkspace(subProject) {
      this.$store.commit("setSubProjectInfo", subProject);
      var id = subProject.subProjectId;
      var memberList = subProject.members;
      var isManager = subProject.isManager;
      var isMember;
      if (!isManager && memberList != []) {
        for (let i = 0; i < memberList.length; i++) {
          if (memberList[i].userId == this.$store.getters.userId) {
            isMember = true;
            break;
          } else {
            isMember = false;
          }
        }
      }
      if (this.$store.getters.userState) {
        if (
          isManager ||
          isMember ||
          this.currentProjectDetail.privacy == "Public"
        ) {
          this.$router.push({ path: `${id}/subproject` });
        } else {
          this.$Message.error("You have no property to access it");
        }
      } else {
        this.$router.push({ path: "/login" });
      }
    },
    //创建子项目
    createSubProject(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          let SubProject = {};
          SubProject[
            "description"
          ] = this.createSubProjectForm.subProjectDescription;
          SubProject["title"] = this.createSubProjectForm.subProjectTitle;
          SubProject["projectId"] = this.currentProjectDetail.projectId;
          SubProject["managerId"] = this.$store.getters.userId;
          this.axios
            .post("/GeoProblemSolving/subProject/create", SubProject)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.$Notice.success({
                  title: "Create result",
                  desc: "Subproject has been created successfully."
                });
                this.createSubProjectForm.subProjectTitle = "";
                this.createSubProjectForm.subProjectDescription = "";
                this.subProjectList.push(res.data);
                this.identity(this.subProjectList);
              } else {
                this.$Message.info("fail");
              }
            })
            .catch(err => {});
        } else {
          this.$Message.error("Please check your form again!");
        }
      });
    },
    inviteModalShow() {
      this.inviteModal = true;
      this.emailInfo.emailContent =
        "hello, it's my pleasure to invite you join in the project called " +
        this.currentProjectDetail.title +
        ".";
    },
    handOverSubProjectShow(index) {
      this.editSubProjectindex = index;
      this.subProjectMembers = this.subProjectList[index].members;
      // if it is the member of the sub-project
      this.isSubMember = this.memberIdentity(this.subProjectMembers);
      this.handOverSubProjectModal = true;
    },
    invite(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          var emailFormBody = {};
          emailFormBody["recipient"] = this.emailInfo.inputEmail;
          emailFormBody["mailTitle"] = this.emailInfo.emailTitle;
          emailFormBody["mailContent"] =
            this.emailInfo.emailContent + this.emailAddStr;
          this.axios
            .post("/GeoProblemSolving/email/invite", emailFormBody)
            .then(res => {
              if (res.data == "Success") {
                this.$Notice.success({
                  title: "Email send title",
                  desc:
                    "The invitation has been sent,the receiver will process this request later."
                });
              } else {
                this.$Notice.error({
                  title: "Email send fail",
                  desc: "The invitation isn't be sent successfully."
                });
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        } else {
          this.$Message.info("Form check!");
        }
      });
    },
    handOverSubProject() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/manager?" +
            "subProjectId=" +
            this.subProjectList[this.editSubProjectindex].subProjectId +
            "&userId=" +
            this.newManagerId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            var newSubProject = res.data;
            var length = this.subProjectList.length;
            var subProjectInfoList = this.subProjectList;
            for (var i = 0; i < length; i++) {
              if (
                subProjectInfoList[i].subProjectId == newSubProject.subProjectId
              ) {
                subProjectInfoList.splice(i, 1, res.data);
                this.identity(subProjectInfoList);
                this.$Message.success("Handover management authority success!");
                break;
              }
            }
            //此处缺少权限移交后的通知
            let replyNotice = {};
            replyNotice["recipientId"] = this.newManagerId;
            replyNotice["type"] = "notice";
            replyNotice["content"] = {
              title: "Subproject manager",
              description:
                "You have already become the manager of subproject: " +
                this.subProjectList[this.editSubProjectindex].title +
                "."
            };
            this.axios
              .post("/GeoProblemSolving/notice/save", replyNotice)
              .then(result => {
                if (result.data == "Success") {
                  this.$emit("sendNotice", this.newManagerId);
                } else {
                  this.$Message.error("notice fail.");
                }
              })
              .catch(err => {
                this.$Message.error("notice fail.");
              });
          } else {
            this.$Message.error("Handover management authority failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    editSubProjectShow(index) {
      this.editSubProjectindex = index;
      this.editSubProjectModal = true;
      this.editSubProjectForm.subProjectTitleEdit = this.subProjectList[
        index
      ].title;
      this.editSubProjectForm.subProjectDescriptionEdit = this.subProjectList[
        index
      ].description;
    },
    editSubProject(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          this.editSubProjectModal = false;
          let obj = new URLSearchParams();
          obj.append(
            "subProjectId",
            this.subProjectList[this.editSubProjectindex].subProjectId
          );
          obj.append("title", this.editSubProjectForm.subProjectTitleEdit);
          obj.append(
            "description",
            this.editSubProjectForm.subProjectDescriptionEdit
          );
          this.axios
            .post("/GeoProblemSolving/subProject/update", obj)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                var newSubProject = res.data;
                for (var i = 0; i < this.subProjectList.length; i++) {
                  if (
                    this.subProjectList[i].subProjectId ==
                    newSubProject.subProjectId
                  ) {
                    this.subProjectList.splice(i, 1, res.data);
                    break;
                  }
                }
                this.identity(this.subProjectList);
              } else {
                this.$Message.error("Update sub-project failed.");
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        } else {
          this.$Message.warning("Please check your form again!");
        }
      });
    },
    deleteSubProjectShow(index) {
      this.editSubProjectindex = index;
      this.deleteSubProjectModal = true;
    },
    deleteSubProject() {
      var deletedSubProjectId = this.subProjectList[this.editSubProjectindex]
        .subProjectId;
      this.axios
        .get(
          "/GeoProblemSolving/subProject/delete?" +
            "subProjectId=" +
            deletedSubProjectId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data == "Success") {
            var length = this.subProjectList.length;
            for (var i = 0; i < length; i++) {
              if (this.subProjectList[i].subProjectId == deletedSubProjectId) {
                this.subProjectList.splice(i, 1);
                this.$Message.success("Delete sub-project success!");
                break;
              }
            }
          } else {
            this.$Message.error("Delete sub-project failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //获取所有子项目
    getAllSubProject() {
      let pid = this.$route.params.id;
      let queryObject = { key: "projectId", value: pid };
      var that = this;
      this.axios
        .get(
          "/GeoProblemSolving/subProject/inquiry" +
            "?key=" +
            queryObject["key"] +
            "&value=" +
            queryObject["value"]
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data == "None" || res.data == "Fail") {
            console.log(res.data);
          } else {
            //改变this的指向，此时this需要赋值给其他变量
            that.subProjectList = res.data;
            that.identity(that.subProjectList);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    identity(list) {
      for (var i = 0; i < list.length; i++) {
        list[i]["isManager"] = this.managerIdentity(list[i]["managerId"]);
        list[i]["isMember"] = this.memberIdentity(list[i]["members"]);
      }
      this.$set(this, "subProjectList", list);
    },
    //上传文件的模态框打开
    uploadFileModalShow() {
      this.uploadFileModal = true;
    },

    //获取全部资源的方法
    getAllResource() {
      // url是请求的网址
      //查询的形式是key-value格式
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=scope.projectId" +
            "&value=" +
            this.$route.params.id
        )
        .then(res => {
          //写渲染函数，取到所有资源
          if (res.data !== "None") {
            this.$set(this, "projectResourceList", res.data);
          } else {
            this.projectResourceList = [];
          }
          //渲染函数，将列表展现出来，下载
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    show(index) {
      let name = this.projectResourceList[index].name;

      if (/\.(doc|docx|xls|xlsx|csv|ppt|pptx|zip)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://172.21.212.72:8012/previewFile?url=" +
          "http://" +
          this.$store.state.IP_Port +
          this.projectResourceList[index].pathURL;
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
      } else if (/\.(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://" +
          this.$store.state.IP_Port +
          this.projectResourceList[index].pathURL;
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
      } else if (/\.(pdf|xml|json|md|gif|jpg|png)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://" +
          this.$store.state.IP_Port +
          this.projectResourceList[index].pathURL;
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
    gotoPersonalPage(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    editModalShow(id) {
      this.editProjectModal = true;
      let editProjectInfo = this.currentProjectDetail;
      this.projectEditForm.title = editProjectInfo.title;
      this.projectEditForm.introduction = editProjectInfo.introduction;
      this.projectEditForm.description = editProjectInfo.description;
      this.projectEditForm.category = editProjectInfo.category;
      this.projectEditForm.privacy = editProjectInfo.privacy;
      this.pictureUrl = editProjectInfo.picture;

      var tags = editProjectInfo["tag"].split(",");
      if (tags[0] == "") {
        this.projectEditForm.editTags = [];
      } else {
        this.projectEditForm.editTags = tags;
      }
      this.editProjectId = editProjectInfo.projectId;
    },
    editProjectSubmit(form) {
      this.$refs[form].validate(valid => {
        if (valid) {
          let form = new URLSearchParams();
          form.append("title", this.projectEditForm.title);
          form.append("category", this.projectEditForm.category);
          form.append("introduction", this.projectEditForm.introduction);
          form.append("description", this.projectEditForm.description);
          form.append("tag", this.projectEditForm.editTags);
          form.append("privacy", this.projectEditForm.privacy);
          form.append("projectId", this.editProjectId);
          form.append("managerId", this.$store.getters.userId);
          form.append("picture", this.pictureUrl);
          this.axios
            .post("/GeoProblemSolving/project/update ", form)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data != "Fail") {
                this.getProjectDetail(); //更新后的回调处理有待优化，应该根据返回的新信息重新更新前端数据并渲染，而不是再重新请求数据
              }
            })
            .catch(err => {
              console.log(err.data);
            });
        } else {
          this.$Message.warning("please check your form again!");
        }
      });
      // 将项目变更的信息进行提交
    },
    // 判断项目详情页面是否具备编辑权限，根据userId与projectId来比较
    judgeIsManager(projectManagerId) {
      if (projectManagerId === this.$store.getters.userId) {
        return true;
      } else {
        return false;
      }
    },
    // 编辑项目的添加tag标签
    addTag(tag) {
      if (tag != "") {
        this.projectEditForm.editTags.push(tag);
      }
      this.inputTag = "";
    },
    // 删除指定的标签
    deleteTag(index) {
      this.projectEditForm.editTags.splice(index, 1);
    },
    toResourceList() {
      this.$router.push({ path: "/resourceList" });
    },
    addUploadEvent(scopeId) {
      let form = {};
      let description =
        this.$store.getters.userName +
        " uploaded a " +
        this.fileUploadForm.type +
        " file in " +
        " project called " +
        this.currentProjectDetail.title;
      form["description"] = description;
      form["scopeId"] = scopeId;
      form["eventType"] = "project";
      form["userId"] = this.$store.getters.userId;
      this.axios
        .post("/GeoProblemSolving/history/save", form)
        .then(res => {
          console.log(res.data);
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    //删除项目的函数
    deleteProjectShow() {
      this.removeProjectModal = true;
    },
    deleteProject() {
      this.axios
        .get(
          "/GeoProblemSolving/project/delete?" +
            "projectId=" +
            this.currentProjectDetail.projectId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data == "Fail") {
            this.$Notice.error({
              title: "Error",
              desc: "Delete project fail."
            });
          }
          if (res.data == "Success") {
            this.$Notice.success({
              title: "Success",
              desc: "Delete project successfully."
            });
            window.location.href="/GeoProblemSolving/projectList";
          }
        })
        .catch(err => {});
    },
    joinSubProject(subProject) {
      let projectPrivacy = this.$store.getters.project.privacy;
      if (projectPrivacy == "Public") {
        // this.axios.get("/GeoProblemSolving/subProject/join?subProjectId="+ subProject.subProjectId + '&userId=' + this.$store.getters.userId)
        // .then(res=> {
        //   var that = this;
        //   console.log(res.data);
        //   if(res.data !== ""){
        //       this.$Notice.open({
        //           title: 'Notification title',
        //           desc: 'Success,now you are a member in this sub project.',
        //           // duration: 0
        //       });
        //       // this.subProject.isMember = true;
        //       that.getAllSubProject();
        //     }
        // })
        // .catch(err=>{
        //   console.log(err.data);
        // })
      } else if (projectPrivacy == "Discoverable") {
        let joinSubPForm = {};
        joinSubPForm["recipientId"] = subProject.managerId;
        joinSubPForm["type"] = "apply";
        let userDetail = this.$store.getters.userInfo;
        joinSubPForm["content"] = {
          userEmail: userDetail.email,
          userName: this.$store.getters.userName,
          userId: this.$store.getters.userId,
          title: "Group application",
          description:
            "User " +
            this.$store.getters.userName +
            " apply to join in your subproject: " +
            subProject.title +
            " of project: " +
            this.currentProjectDetail.title +
            " .",
          projectId: subProject.subProjectId,
          projectTitle: subProject.title,
          scope: "subProject",
          approve: "unknow"
        };

        this.axios
          .post("/GeoProblemSolving/notice/save", joinSubPForm)
          .then(res => {
            this.$Message.info("Apply Successfully");
            this.$emit("sendNotice", subProject.managerId);
          })
          .catch(err => {
            console.log("申请失败的原因是：" + err.data);
          });
        let joinSubProjectEmail = {};
        joinSubProjectEmail["recipient"] = subProject.managerId;
        joinSubProjectEmail["mailTitle"] = "Join sub project application";
        joinSubProjectEmail["mailContent"] =
          "User " +
          this.$store.getters.userName +
          " apply to join in your subproject: " +
          subProject.title +
          " of project: " +
          this.currentProjectDetail.title +
          " ." +
          " And you can access the subproject from this platform. " +
          "The url is " +
          "http://" +
          this.$store.state.IP_Port +
          "/GeoProblemSolving/home";
        this.axios
          .post("/GeoProblemSolving/project/applyByMail", joinSubProjectEmail)
          .then(res => {
            if (res.data == "Success") {
              this.$Notice.success({
                title: "Email send title",
                desc:
                  "The join email has been sent,if he/she doesn't online,the email will remind the mamnager in time."
              });
            } else {
              this.$Notice.error({
                title: "Email send fail",
                desc: "The invitation isn't be sent successfully."
              });
            }
          })
          .catch(err => {
            console.log(err.data);
          });
      }
    },
    emialAutoFill(value) {
      this.prompt =
        !value || value.indexOf("@") >= 0
          ? []
          : [
              value + "@gmail.com",
              value + "@sina.com",
              value + "@163.com",
              value + "@126.com",
              value + "@qq.com"
            ];
    },
    filesUpload(form) {
      this.uploadProgress = 0;
      this.$refs[form].validate(valid => {
        if (valid) {
          let that = this;
          if (that.file.length != 0) {
            var formData = new FormData();
            var total = 0;
            for (var i = 0; i < that.file.length; i++) {
              if (that.file[i].fileSize < Math.pow(1024, 2)) {
                formData.append("file", that.file[i]); // 文件对象
              } else {
              }
              total += that.file[i].fileSize;
            }
            if (total < Math.pow(1024, 2)) {
              let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
              formData.append("description", this.fileUploadForm.description);
              formData.append("type", this.fileUploadForm.type);
              formData.append(
                "uploaderId",
                this.$store.getters.userInfo.userId
              );
              formData.append("belong", this.currentProjectDetail.title);
              let scopeObject = {
                projectId: this.currentProjectDetail.projectId,
                subprojectId: "",
                moduleId: ""
              };
              formData.append("scope", JSON.stringify(scopeObject));
              formData.append("privacy", this.fileUploadForm.privacy);
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
                  if (res.data == "Offline") {
                    this.$store.commit("userLogout");
                    this.$router.push({ name: "Login" });
                  } else if (res.data != "Size over" && res.data.length > 0) {
                    this.$Notice.open({
                      title: "Success",
                      desc: "File uploaded successfully."
                    });
                  } else {
                    this.$Notice.error({
                      title: "Error",
                      desc: "File uploaded fail."
                    });
                  }
                  //这里重新获取一次该项目下的全部资源
                  this.addUploadEvent(this.currentProjectDetail.projectId);
                  this.getAllResource();
                  this.file = [];
                  this.fileUploadForm.description = "";
                  this.fileUploadForm.privacy = "private";
                  this.fileUploadForm.type = "data";
                  // 创建一个函数根据pid去后台查询该项目下的资源
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                })
                .catch(err => {
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                });
            } else {
              this.$Message.error(
                "size over,all the file size must smaller than 1 GB one time!"
              );
            }
          }
        } else {
          this.$Message.warning("please check your form!");
        }
      });
    },
    gatherFile(file) {
      let that = this;
      if (that.file.length >= 5) {
        if (this.fileCountTimer != null) {
          clearTimeout(this.fileCountTimer);
        }
        this.fileCountTimer = setTimeout(() => {
          this.$Message.info("you can upload 5 files one time!");
        }, 500);
      } else {
        that.file.push(file);
        that.file.map(element => {
          element["fileSize"] = Math.round((element.size / 1024) * 100) / 100;
        });
      }
      return false;
    },
    delFileList(index) {
      let that = this;
      that.file.splice(index, 1);
    },
    removeAlertShow(id) {
      if (this.removeMemberName != "") {
        this.alertModalShow = true;
      }
    },
    removeMember() {
      let quitProjectId = sessionStorage.getItem("projectId");
      this.axios
        .get(
          "/GeoProblemSolving/project/quit" +
            "?projectId=" +
            quitProjectId +
            "&userId=" +
            this.removeMemberId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data == "None") {
            this.$Notice.warning({
              title: "Operate result",
              desc: "The project doesn't exist."
            });
          } else {
            this.$Notice.success({
              title: "Operate result",
              desc: "The member has been removed from the project successfully."
            });
            var members = this.currentProjectDetail.members;
            for (var i = 0; i < members.length; i++) {
              if (members[i].userId == this.removeMemberId) {
                this.currentProjectDetail.members.splice(i, 1);
                break;
              }
            }
            this.$store.commit("setProjectInfo", this.currentProjectDetail);
          }
        })
        .catch(err => {});
      // 删除成员即可
    },
    getRemoveMemberId(id) {
      this.removeMemberId = id;
    },
    // 处理更换项目图片
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
          formData.append("picture", file);
          this.axios
            .post("/GeoProblemSolving/project/picture", formData)
            .then(res => {
              if (res.data != "Fail") {
                this.pictureUrl = res.data;
                var imgcode = e.target.result;
                this.img = imgcode;
                $('#choosePicture').val('');
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
      this.img = "";
      this.pictureUrl = "";
    }
  }
};
</script>
