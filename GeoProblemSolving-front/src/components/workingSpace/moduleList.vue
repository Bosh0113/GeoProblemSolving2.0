<style scoped>
.detail {
  display: flex;
}
.sidebar {
  font-weight: bold;
  width: 15%;
}
.right {
  width: 90%;
  margin-left: 5%;
  height: 100%;
}
.single_part {
  margin-left: 2.5%;
  margin-right: 2.5%;
}
.addNodeStyle,
.editNodeStyle {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 2.5%;
}
.addBtn,
.removeBtn,
.editBtn .createTaskBtn {
  font-size: 10px;
}
.addBtn:hover {
  color: white;
  background: #47cb89;
}
.removeBtn:hover {
  color: white;
  background: #f16643;
}
.editBtn:hover {
  color: white;
  background: #2d8cf0;
}
.createTaskBtn:hover {
  color: white;
  background: #47cb89;
}
.title {
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid lightgray;
}
.member-desc {
  height: 60px;
  margin: 0 20px 0 10px;
  display: flex;
}
.member-image {
  width: 60px;
  height: 60px;
  padding: 5px;
}
.memebr-work {
  width: 65%;
  height: 60px;
}
.userName {
  margin-top: 10px;
  height: 20px;
  display: flex;
  align-items: center;
}
.organization {
  height: 30px;
  display: flex;
  align-items: center;
}

.util-panel {
  height: 140px;
  width: 60px;
  box-shadow: 2px 2px 2px 2px gray;
  position: fixed;
  cursor: move;
  left: 0px;
  right: 200px;
  z-index: 100;
}
.util-btn-group {
  width: 60px;
  cursor: default;
}
.util-btn {
  /* 使按钮垂直居中即可 */
  display: block;
  margin: 20px auto;
  width: 40px;
  height: 40px;
  text-align: center;
  transition: all 1s;
  /* 使按钮中的元素垂直居中 */
  display: flex;
  justify-content: center;
  align-content: center;
}
.util-btn:hover {
  transform: scale(1.15);
  transition: all 0.5s;
}
.member-panel {
  transition: all 1s;
}
.memberOrganization {
  height: 40px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}
.subProjectDesc {
  text-indent: 2em;
  padding: 10px;
  word-break: break-all;
}
.resource {
  transition: all 1s;
}
.tool-panel {
  display: flex;
  height: auto;
  flex-wrap: wrap;
  /* justify-content: center; */
  align-items: center;
}
/* 工具库中抽屉的工具样式*/
.singl_tool_style {
  margin: 10px;
  cursor: pointer;
  display: flex;
  justify-content: center;
}
.singl_tool_style:hover {
  transition: all 1s;
  background-color: lightgray;
}
.taskFormItem {
  display: flex;
  align-items: center;
}
.taskFormItem span {
  text-align: center;
}
.operatePanel {
  display: flex;
  justify-content: flex-end;
}
.operatePanel button {
  margin-right: 2.5%;
}
.noModule h1 {
  color: darkgray;
  text-align: center;
}
.moduleShow {
  width: 30px;
  height: 36px;
  border: white;
  cursor: pointer;
  background-color: white;
  opacity: 0.3;
}
</style>
<template>
  <div style="background-color:#dcdee2">
    <Row>
      <Col span="22" offset="1">
        <Card>
          <Row>
            <Col span="6" style="height:40px">
              <Breadcrumb>
                <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem>
                <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Working panel</BreadcrumbItem>
              </Breadcrumb>
            </Col>
            <Col span="12" style="text-align:center;font-size:1.5rem;height:20px;">
              <strong>{{subProjectInfo.title}}</strong>
            </Col>
            <Col
              span="5"
              offset="1"
              style="height:40px;display:flex;align-items:center"
              class="operatePanel"
            >
              <template v-if="processStructure.length>0">
                <Button @click="showSteps" class="addBtn">Steps</Button>
                <template v-if="currentStep.activeStatus && userRole != 'Visitor'">
                  <Button
                    type="default"
                    @click="editModalShow()"
                    icon="ios-create"
                    class="editBtn"
                    title="Edit this module"
                  >Edit</Button>
                </template>
                <template v-else-if="!currentStep.activeStatus && userRole != 'Visitor'">
                  <Button
                    type="default"
                    @click="activateModal = true"
                    icon="md-bulb"
                    class="editBtn"
                    title="Activate this module"
                  >Activate</Button>
                </template>
              </template>
            </Col>
          </Row>
        </Card>
      </Col>
    </Row>
    <div v-if="this.processStructure.length == 0" class="workspaceContent">
      <Row style="margin-top:20px" :style="{height:sidebarHeight+30+'px'}">
        <Col span="22" offset="1">
          <Card class="noModule">
            <div
              @click="addModal = true"
              style="cursor:pointer"
              v-if="$store.getters.userInfo.userId == this.subProjectInfo.managerId"
            >
              <h1>Start your work!</h1>
            </div>
            <div v-else>
              <h1>Start your work!</h1>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
    <div v-else class="workspaceContent">
      <template v-if="currentStep.activeStatus">
        <Row style="margin-top:20px">
          <Col
            :xs="8"
            :sm="7"
            :md="6"
            :lg="5"
            v-bind="this.olParticipants"
            offset="1"
            :style="{height:sidebarHeight+13+'px'}"
          >
            <div :style="{height:sidebarHeight-6+'px'}">
              <Card style="background-color:white">
                <h2 slot="title">Online participants</h2>
                <div :style="{height:sidebarHeight-100+'px'}">
                  <div class="member-desc" v-for="member in olParticipants" :key="member.id">
                    <template style="margin-top:5px">
                      <div
                        class="member-image"
                        @click="gotoPersonalSpace(member.userId)"
                        style="cursor:pointer;display:flex;justify-content:center;align-ittems:center"
                      >
                        <img
                          v-if="member.avatar != '' && member.avatar!='undefined' && member.avatar!='null'"
                          :src="member.avatar"
                          style="width:100%;height:100%"
                        />
                        <avatar
                          :username="member.userName"
                          :size="50"
                          style="width:100%;height:100%"
                          :title="member.userName"
                          v-else
                        ></avatar>
                      </div>
                      <div class="memebr-work" style="display:flex;align-items:center">
                        <div style="height:40px;width:100%">
                          <div>
                            <span style="padding:0 5px" :title="member.userName">{{member.userName}}</span>
                          </div>
                          <div>
                            <span
                              style="padding:0 5px"
                              class="memberOrganization"
                              :title="member.organization"
                            >{{member.organization}}</span>
                          </div>
                        </div>
                      </div>
                    </template>
                  </div>
                </div>
              </Card>
            </div>
          </Col>
          <template>
            <Col
              :xs="15"
              :sm="16"
              :md="17"
              :lg="17"
              :style="{height:sidebarHeight/3*1+'px'}"
              style="margin-bottom:20px"
            >
              <Col span="16">
                <div style="height:100%;margin-left:30px">
                  <Card :style="{height:sidebarHeight/3*1 +'px'}">
                    <h2 slot="title">{{this.currentModule.title}}</h2>
                    <div style="width:100%;padding:10px">
                      <span
                        style="word-break: break-all;text-indent:2em;padding:10px"
                      >{{this.currentModule.description}}</span>
                    </div>
                  </Card>
                </div>
              </Col>
              <Col span="8">
                <div style="height:100%;margin-left:30px">
                  <Card :style="{height:sidebarHeight/3*1 +'px'}">
                    <h2 slot="title">Announcement</h2>
                    <div
                      slot="extra"
                      style="display:flex;align-items:center"
                      v-if="$store.getters.userInfo.userId == this.subProjectInfo.managerId"
                    >
                      <span
                        @click="noticeModalShow=true"
                        style="cursor:pointer"
                        title="Add a notice"
                      >
                        <Icon type="md-add" />
                      </span>
                      <span
                        @click="noticeDetailShow()"
                        style="cursor:pointer;margin-left:10px"
                        title="Edit"
                      >
                        <Icon type="ios-create" />
                      </span>
                      <span
                        @click="deleteNotice()"
                        style="cursor:pointer;margin-left:10px"
                        title="Remove"
                      >
                        <Icon type="ios-trash" />
                      </span>
                    </div>
                    <div>
                      <div v-if="this.currentModuleNoticeList.length!=0">
                        <h3
                          style="text-align:center"
                        >{{this.currentModuleNoticeList[this.currentModuleNoticeList.length-1].title}}</h3>
                        <p
                          style="text-indent:2em;overflow:hidden;break-word:word-break"
                        >{{this.currentModuleNoticeList[this.currentModuleNoticeList.length-1].description}}</p>
                      </div>
                      <div v-if="this.currentModuleNoticeList.length==0">
                        <p
                          style="text-indent:2em;overflow:hidden;break-word:word-break"
                        >There is no notice recently</p>
                      </div>
                    </div>
                  </Card>
                  <Modal
                    width="400px"
                    v-model="noticeModalShow"
                    title="Create a new notice"
                    @on-ok="createNotice"
                    ok-text="Confirm"
                    cancel-text="Cancel"
                  >
                    <Form :model="formItem" :label-width="60">
                      <FormItem label="Title">
                        <Input v-model="formItem.title" placeholder="Enter bulletin title"></Input>
                      </FormItem>
                      <FormItem label="Content">
                        <Input
                          v-model="formItem.content"
                          placeholder="Enter bulletin content"
                          type="textarea"
                        ></Input>
                      </FormItem>
                    </Form>
                  </Modal>
                  <Modal
                    width="400px"
                    v-model="noticeDetailShowModal"
                    title="Notice detail update"
                    @on-ok="editNotice()"
                    ok-text="Confirm"
                    cancel-text="Cancel"
                  >
                    <Form :model="editFormItem" :label-width="60">
                      <FormItem label="title">
                        <Input v-model="editFormItem.title" placeholder="Enter bulletin title"></Input>
                      </FormItem>
                      <FormItem label="content">
                        <Input
                          v-model="editFormItem.description"
                          placeholder="Enter bulletin content"
                          type="textarea"
                        ></Input>
                      </FormItem>
                    </Form>
                  </Modal>
                </div>
              </Col>
            </Col>
          </template>
          <template>
            <Col :xs="15" :sm="16" :md="17" :lg="17">
              <Col span="11">
                <div style="border:1px solid lightgray;background-color:white;margin-left:30px">
                  <Card>
                    <h2 slot="title">Timeline</h2>
                    <div
                      class="recordLine"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                      style="overflow-y:auto"
                    >
                      <Timeline style="padding:10px">
                        <TimelineItem v-for="(item,index) in allRecords" :key="index">
                          <template v-if="item.type == 'participants'">
                            <span class="time" style="color:gray">{{item.time}}</span>
                            <span class="time" style="color:gray; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:gray; margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                          </template>
                          <template v-if="item.type == 'resource'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2;margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2;margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                            <a
                              style="cursor:pointer;color:green;margin-left:5px"
                              :href="'http://'+$store.state.IP_Port+'/GeoProblemSolving/resource/upload/'+item.file"
                              target="_blank"
                            >download</a>
                          </template>
                          <template v-if="item.type == 'tools'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2; margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                            <span
                              style="cursor:pointer;color:green;margin-left:5px"
                              @click="toolPanel(item.toolType)"
                            >check</span>
                          </template>
                        </TimelineItem>
                      </Timeline>
                    </div>
                  </Card>
                </div>
              </Col>
              <Col span="13">
                <div style="background-color:white;margin-left:30px" class="resourcePanel">
                  <Card>
                    <h2 slot="title">Resource</h2>
                    <div slot="extra" style="display:flex;align-items:center" v-show="userRole!='Visitor'">
                      <span
                        id="upload"
                        type="default"
                        @click="uploadFileModal = true"
                        class="uploadBtn"
                        title="Upload resource"
                        style="cursor:pointer"
                      >
                        <Icon type="md-cloud-upload" size="20" />
                      </span>
                      <span
                        slot="extra"
                        class="moreBtn"
                        type="default"
                        style="margin-left:15px;cursor:pointer"
                        @click="toResourceList()"
                        title="More"
                      >
                        <Icon type="md-more" />
                      </span>
                    </div>
                    <div
                      style="overflow-y:auto;padding:0px 10px 10px 10px"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                    >
                      <Table
                        style="overflow:auto"
                        :columns="tableColName"
                        :data="this.resourceList"
                        v-show="this.resourceList!=[] && this.resourceList!='None'"
                      >
                        <template slot-scope="{ row }" slot="name">
                          <strong>{{ row.name }}</strong>
                        </template>
                        <template slot-scope="{ row, index }" slot="action">
                          <Button
                            type="success"
                            size="small"
                            style="margin-right: 5px"
                            :href="resourceList[index].pathURL"
                            @click="show(index)"
                            title="Download"
                          >
                            <Icon type="md-download" />
                          </Button>
                          <Button
                            type="warning"
                            size="small"
                            style="margin-right: 5px"
                            title="Preview"
                            @click="previewRes(index)"
                          >
                            <Icon type="md-eye" />
                          </Button>
                        </template>
                      </Table>
                    </div>
                  </Card>
                </div>
              </Col>
            </Col>
          </template>
          <div class="util-panel" @mousedown="toolContainerMove">
            <div class="util-btn-group">
              <Button type="info" class="util-btn" shape="circle" @click="toolPanel('chat')">
                <Icon type="md-contacts" size="20" class="util-btn-icon" />
              </Button>
              <Button type="info" class="util-btn" shape="circle" @click="drawerOpen = true">
                <Icon type="ios-albums" size="20" class="util-btn-icon" />
              </Button>
              <Drawer :closable="false" v-model="drawerOpen" width="360" style="font-size:30px">
                <!-- tab contains collaborative and non-collaborative -->
                <Tabs value="General">
                  <TabPane label="General tools" name="General">
                    <h2>Collaborative Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline" />
                      <span>This tools support collaborative functions for multi-participants working together.</span>
                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('draw')"
                          title="DrawBoard"
                          color="green"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-map"
                          size="60"
                          @click.native="toolPanel('map')"
                          title="Map"
                          color="lightblue"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-podium"
                          size="60"
                          @click.native="toolPanel('chart')"
                          title="Chart"
                          color="lightgreen"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-grid"
                          size="60"
                          @click.native="toolPanel('tableEditor')"
                          title="Table editor"
                          color="#2d8cf0"
                        />
                      </div>
                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="md-cube"
                          size="60"
                          @click.native="toolPanel('3DmodelViewer')"
                          title="3D model Viewer"
                          color="#561cec"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('graphEditor')"
                          title="Graph Editor"
                          color="#eca01c"
                        />
                      </div>
                    </div>
                    <h2>Non-collaborative Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline" />
                      <span>This tool can't support collaborative functions.</span>
                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('nc-draw')"
                          title="DrawBoard"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-map"
                          size="60"
                          @click.native="toolPanel('nc-map')"
                          title="Map"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-podium"
                          size="60"
                          @click.native="toolPanel('nc-chart')"
                          title="Chart"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-grid"
                          size="60"
                          @click.native="toolPanel('cn-tableEditor')"
                          title="Table editor"
                          color="gray"
                        />
                      </div>
                    </div>
                    <div class="tool-panel">
                      <div class="singl_tool_style">
                        <Icon
                          type="md-cube"
                          size="60"
                          @click.native="toolPanel('nc-3DmodelViewer')"
                          title="3D model viewer"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-videocam"
                          size="60"
                          @click.native="toolPanel('nc-video')"
                          title="Video player"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-book"
                          size="60"
                          @click.native="toolPanel('nc-pdf')"
                          title="PDF viewer"
                          color="gray"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-clipboard"
                          size="60"
                          @click.native="toolPanel('Doc Edit')"
                          title="doc editor"
                          color="gray"
                        />
                      </div>
                      <!-- <div class="singl_tool_style">
                        <Icon
                          type="logo-youtube"
                          size="60"
                          @click.native="toolPanel('Video Tool')"
                          title="Video Tool"
                          color="gray"
                        />
                      </div>-->
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-water"
                          size="60"
                          @click.native="toolPanel('Web-SWMM')"
                          title="Web-SWMM"
                          color="gray0"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-car"
                          size="60"
                          @click.native="toolPanel('TrafficNoise')"
                          title="Traffic Noise Simulation"
                          color="gray"
                        />
                      </div>
                      <a class="singl_tool_style" href="http://134.175.111.77/note" target="_blank">
                        <Icon type="md-code" size="60" title="Jupyter Notebook" color="gray" />
                      </a>
                    </div>
                  </TabPane>
                  <TabPane label="Special tools" name="Special">
                    <h2>Special Tools</h2>
                    <div style="display:flex;align-items:center">
                      <Icon type="ios-information-circle-outline" />
                      <span>For different solving process of geographic problem</span>
                    </div>
                    <br />
                    <div class="tool-panel" v-show="this.currentModule.type == 'Preparation'">
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('draw')"
                          title="DrawBoard"
                          color="green"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-map"
                          size="60"
                          @click.native="toolPanel('map')"
                          title="Map"
                          color="lightblue"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-grid"
                          size="60"
                          @click.native="toolPanel('tableEditor')"
                          title="Table editor"
                          color="#2d8cf0"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-cube"
                          size="60"
                          @click.native="toolPanel('3DmodelViewer')"
                          title="3D model Viewer"
                          color="#561cec"
                        />
                      </div>
                    </div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Analysis'">
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-podium"
                          size="60"
                          @click.native="toolPanel('chart')"
                          title="Chart"
                          color="lightgreen"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-grid"
                          size="60"
                          @click.native="toolPanel('tableEditor')"
                          title="Table editor"
                          color="#2d8cf0"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-create"
                          size="60"
                          @click.native="toolPanel('graphEditor')"
                          title="Graph Editor"
                          color="#eca01c"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-water"
                          size="60"
                          @click.native="toolPanel('Web-SWMM')"
                          title="Web-SWMM"
                          color="#2d8cf0"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-car"
                          size="60"
                          @click.native="toolPanel('TrafficNoise')"
                          title="Traffic Noise Simulation"
                          color="#19be6b"
                        />
                      </div>
                    </div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Modeling'">
                      <a class="singl_tool_style" href="http://134.175.111.77/note" target="_blank">
                        <Icon type="md-code" size="60" title="Jupyter Notebook" color="#f37726" />
                      </a>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-bonfire"
                          size="60"
                          @click.native="toolPanel('ConceptualModel')"
                          title="Conceptual Modeling"
                          color="#2d8cf0"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-git-commit"
                          size="60"
                          @click.native="toolPanel('LogicalModel')"
                          title="Logical Modeling"
                          color="#19be6b"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="md-pulse"
                          size="60"
                          @click.native="toolPanel('ComputationalModel')"
                          title="Computational Modeling"
                          color="#f90"
                        />
                      </div>
                    </div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Simulation'">
                      <a class="singl_tool_style" href="http://134.175.111.77/note" target="_blank">
                        <Icon type="md-code" size="60" title="Jupyter Notebook" color="#f37726" />
                      </a>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-water"
                          size="60"
                          @click.native="toolPanel('Web-SWMM')"
                          title="Web-SWMM"
                          color="#2d8cf0"
                        />
                      </div>
                      <div class="singl_tool_style">
                        <Icon
                          type="ios-car"
                          size="60"
                          @click.native="toolPanel('TrafficNoise')"
                          title="Traffic Noise Simulation"
                          color="#19be6b"
                        />
                      </div>
                    </div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'validation'">
                      <a class="singl_tool_style" href="http://134.175.111.77/note" target="_blank">
                        <Icon type="md-code" size="60" title="Jupyter Notebook" color="#f37726" />
                      </a>
                    </div>
                    <div class="tool-panel" v-show="this.currentModule.type == 'Comparison'">
                      <a class="singl_tool_style" href="http://134.175.111.77/note" target="_blank">
                        <Icon type="md-code" size="60" title="Jupyter Notebook" color="#f37726" />
                      </a>
                    </div>
                  </TabPane>
                </Tabs>
              </Drawer>
            </div>
          </div>
        </Row>
      </template>
      <template v-else>
        <Row style="margin-top:20px" :style="{height:sidebarHeight+13+'px'}">
          <template>
            <Col span="22" offset="1" :style="{height:sidebarHeight/3*1+'px'}">
              <Card :style="{height:sidebarHeight/3*1 +'px'}">
                <h2 slot="title">{{this.currentModule.title}}</h2>
                <div style="width:100%;padding:10px">
                  <span
                    style="word-break: break-all;text-indent:2em;padding:10px"
                  >{{this.currentModule.description}}</span>
                </div>
              </Card>
            </Col>
          </template>
          <template>
            <Col span="22" offset="1" style="margin-top:20px;">
              <Col span="12">
                <div style="border:1px solid lightgray;background-color:white;margin-right:15px">
                  <Card>
                    <h2 slot="title">Timeline</h2>
                    <div
                      class="recordLine"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                      style="overflow-y:auto"
                    >
                      <Timeline style="padding:10px">
                        <TimelineItem v-for="(item,index) in historyRecords" :key="index">
                          <template v-if="item.type == 'participants'">
                            <span class="time" style="color:gray">{{item.time}}</span>
                            <span class="time" style="color:gray; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:gray; margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                          </template>
                          <template v-if="item.type == 'resource'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2;margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2;margin-left:10px; word-break:break-word"
                            >{{item.content}}</span>
                            <a
                              style="cursor:pointer;color:green;margin-left:5px"
                              :href="'http://'+$store.state.IP_Port+'/GeoProblemSolving/resource/upload/'+item.file"
                              target="_blank"
                            >download</a>
                          </template>
                          <template v-if="item.type == 'tools'">
                            <span class="time" style="color:#0664a2">{{item.time}}</span>
                            <span class="time" style="color:#0664a2; margin-left:10px">{{item.who}}</span>
                            <span
                              class="content"
                              style="color:#0664a2; margin-left:10px; word-break:break-word; cursor:pointer"
                              @click="toolPanel(item.toolType)"
                            >{{item.content}}</span>
                          </template>
                        </TimelineItem>
                      </Timeline>
                    </div>
                  </Card>
                </div>
              </Col>
              <Col span="12">
                <div style="background-color:white;margin-left:15px">
                  <Card>
                    <h2 slot="title">Resource</h2>
                    <div slot="extra" style="display:flex;align-items:center">
                      <span
                        id="upload"
                        type="default"
                        @click="uploadFileModal = true"
                        class="uploadBtn"
                        title="upload resource"
                        style="cursor:pointer"
                      >
                        <Icon type="md-cloud-upload" size="20" />
                      </span>
                      <span
                        slot="extra"
                        class="moreBtn"
                        type="default"
                        style="margin-left:15px;cursor:pointer"
                        @click="toResourceList()"
                        title="more"
                      >
                        <Icon type="md-more" />
                      </span>
                    </div>
                    <div
                      style="overflow-y:auto;padding:0px 10px 10px 10px"
                      :style="{height:sidebarHeight/3*2 - 120 + 'px'}"
                    >
                      <Table
                        style="overflow:auto"
                        :columns="tableColName"
                        :data="this.resourceList"
                        v-show="this.resourceList!=[] && this.resourceList!='None'"
                      >
                        <template slot-scope="{ row }" slot="name">
                          <strong>{{ row.name }}</strong>
                        </template>
                        <template slot-scope="{ row, index }" slot="action">
                          <Button
                            type="success"
                            size="small"
                            style="margin-right: 5px"
                            :href="resourceList[index].pathURL"
                            @click="show(index)"
                            title="Download"
                          >
                            <Icon type="md-download" />
                          </Button>
                          <Button
                            type="warning"
                            size="small"
                            style="margin-right: 5px"
                            title="Preview"
                            @click="previewRes(index)"
                          >
                            <Icon type="md-eye" />
                          </Button>
                        </template>
                      </Table>
                    </div>
                  </Card>
                </div>
              </Col>
            </Col>
          </template>
        </Row>
      </template>
    </div>
    <Modal
      v-model="delModal"
      title="Delete this process"
      @on-ok="delModule"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <p>Do you really want to delete this step?</p>
    </Modal>
    <Modal
      width="600px"
      v-model="editModal"
      title="Update this process"
      @on-ok="updateModule('formValidate2')"
    >
      <Form
        ref="formValidate2"
        :model="formValidate2"
        :rules="ruleValidate2"
        :label-width="80"
        style="margin-left: 30px"
      >
        <FormItem label="Name" prop="updateModuleTitle">
          <Input
            v-model="formValidate2.updateModuleTitle"
            placeholder="Enter something..."
            style="width: 400px"
          />
        </FormItem>
        <FormItem label="Type" prop="updateModuleType">
          <Select
            v-model="formValidate2.updateModuleType"
            style="width:400px"
            placeholder="please select module type..."
          >
            <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div class="editNodeStyle">
        <span style="width:10%">Detail</span>
        <textarea
          v-model="updateModuleDescription"
          style="width:400px"
          :rows="6"
          :placeholder="moduleDescription"
        ></textarea>
      </div>
    </Modal>
    <Modal
      width="600px"
      v-model="addModal"
      title="Start a new step"
      @on-ok="addModule('formValidate1')"
      ok-text="Confirm"
      cancel-text="Cancel"
    >
      <Form
        ref="formValidate1"
        :model="formValidate1"
        :rules="ruleValidate1"
        :label-width="80"
        style="margin-left: 30px"
      >
        <FormItem label="Name" prop="moduleTitle">
          <Input
            v-model="formValidate1.moduleTitle"
            placeholder="Enter something..."
            style="width: 400px"
          />
        </FormItem>
        <FormItem label="Type" prop="moduleType">
          <Select
            v-model="formValidate1.moduleType"
            style="width:400px"
            placeholder="please select module type..."
          >
            <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
        </FormItem>
      </Form>
      <div class="addNodeStyle">
        <span style="margin:0 10px 0 10px">Detail</span>
        <textarea v-model="moduleDescription" style="width:400px" :rows="6"></textarea>
      </div>
    </Modal>
    <Modal
      width="800"
      v-model="stepsModal"
      title="The process of geo-problem solving"
      :mask-closable="false"
      footer-hide
    >
      <div style="width:780px;height:400px" id="steps"></div>
      <template v-if="$store.getters.userInfo.userId == this.subProjectInfo.managerId">
        <div style="width: 765px; height: 25px">
          <label style="margin-left:20px">New step name:</label>
          <Input
            v-model="formValidate1.moduleTitle"
            placeholder="Enter something..."
            style="width: 200px"
          />
          <label style="margin-left:20px">New step type:</label>
          <Select
            v-model="formValidate1.moduleType"
            style="width:100px"
            placeholder="please select module type..."
          >
            <Option v-for="item in typeList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
          <template v-if="this.processStructure.length > 0">
            <Button
              type="default"
              @click="removeStep()"
              icon="md-remove"
              class="removeBtn"
              title="Remove this module"
              style="float:right;margin-left:10px"
            >Remove</Button>
          </template>
          <Button
            type="default"
            @click="addNewStep()"
            icon="md-add"
            class="addBtn"
            title="Add a new module"
            style="float:right;margin-left:10px"
          >Add</Button>
        </div>
      </template>
    </Modal>
    <Modal
      v-model="inheritData"
      title="Choose data to next process"
      @on-ok="createModule()"
      ok-text="Confirm"
      cancel-text="Cancel"
    >
      <Transfer
        :data="inheritResource"
        :target-keys="targetKeys"
        :list-style="listStyle"
        :render-format="resourceRender"
        :titles="['This process', 'The next process']"
        filter-placeholder="Enter key words..."
        filterable
        :filter-method="filterMethod"
        @on-change="handleChange"
      ></Transfer>
    </Modal>
    <Modal
      width="600px"
      v-model="activateModal"
      title="Activate this process"
      @on-ok="activateModule()"
      ok-text="Ok"
      cancel-text="Cancel"
    >
      <p>Do you want to activate this process?</p>
    </Modal>
    <Modal
      v-model="uploadFileModal"
      title="Upload resource"
      @on-ok="submitFile('formValidate3')"
      ok-text="Submit"
      cancel-text="Cancel"
      width="800px"
      :mask-closable="false"
    >
      <Form
        ref="formValidate3"
        :model="formValidate3"
        :rules="ruleValidate3"
        :label-width="80"
        style="margin-left:20px"
      >
        <FormItem label="Privacy" prop="filePrivacy">
          <RadioGroup v-model="formValidate3.filePrivacy">
            <Radio label="private">Private</Radio>
            <Radio label="public">Public</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem label="File type: " prop="fileType">
          <RadioGroup v-model="formValidate3.fileType">
            <Radio label="data">Data</Radio>
            <Radio label="image">Images</Radio>
            <Radio label="video">Videos</Radio>
            <Radio label="paper">Papers</Radio>
            <Radio label="document">Documents</Radio>
            <Radio label="model">Models</Radio>
            <Radio label="others">Others</Radio>
          </RadioGroup>
        </FormItem>
      </Form>
      <div style="display:flex;text-align:center;align-items:center;justify-content:center">
        <span style="width:20%">Description</span>
        <Input type="textarea" :rows="2" v-model="fileDescription" />
      </div>
      <br />
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
import { VueFlowy, FlowChart } from "vue-flowy";
import * as socketApi from "./../../api/socket";
import Avatar from "vue-avatar";
import echarts from "echarts";
export default {
  updated() {
    $(".userAvatar sup").css("margin", "15px 15px 0 0");
    $(".ivu-steps-title").css("cursor", "pointer");
    $(".ivu-steps-title").css("overflow", "hidden");
    $(".ivu-steps-title").css("white-space", "nowrap");
    $(".ivu-steps-title").css("text-overflow", "ellipsis");
    $(".ivu-steps-title").css("max-width", "120px");
  },
  components: {
    VueFlowy,
    Avatar
  },
  data() {
    return {
      // 步骤逻辑图
      stepChart: null,
      // info of subproject --by mzy
      subProjectInfo: [],
      // 关于邀请的模态框
      sidebarHeight: 800,
      participants: [],
      // current: 0,
      addModal: false,
      delModal: false,
      inheritData: false,
      //编辑的模态框
      editModal: false,
      activateModal: false,
      stepsModal: false,
      // chart适用
      chart: new FlowChart(),
      //现在点击的module
      currentModule: {},
      //typeList是选择模块种类的列表，select从这里渲染
      typeList: [
        "Preparation",
        "Analysis",
        "Modeling",
        "Simulation",
        "Validation",
        "Comparison"
      ],
      // 添加/编辑module
      formValidate1: {
        moduleTitle: "",
        moduleType: ""
      },
      ruleValidate1: {
        moduleTitle: [
          { required: true, message: "Please enter name...", trigger: "blur" }
        ],
        moduleType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ]
      },
      formValidate2: {
        updateModuleTitle: "",
        updateModuleType: ""
      },
      ruleValidate2: {
        updateModuleTitle: [
          { required: true, message: "Please enter name...", trigger: "blur" }
        ],
        updateModuleType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ]
      },
      formValidate3: {
        fileType: "data",
        filePrivacy: "private"
      },
      ruleValidate3: {
        fileType: [
          { required: true, message: "Please select type...", trigger: "blur" }
        ],
        filePrivacy: [
          { required: true, message: "Please set privacy...", trigger: "blur" }
        ]
      },
      // moduleDescription指的是节点的详情信息
      moduleDescription: "",
      updateModuleDescription: "",
      // 抽屉的控制开关
      drawerOpen: false,
      // 选择的模块
      selectedModule: [],
      // web socket for module
      subprojectSocket: null,
      timer: null,
      // 动态记录相关
      // 所有module的记录
      allRecords: [],
      historyRecords: [],
      allHistRecords: [],
      // 当前参与者
      olParticipants: [],
      ofParticipants: [],
      uploadFileModal: false,
      resourceList: [],
      tableColName: [
        {
          title: "Name",
          key: "name",
          sortable: true,
          tooltip: true
        },
        {
          title: "Type",
          key: "type",
          sortable: true,
          width: 90
        },
        {
          title: "Action",
          slot: "action",
          align: "center",
          width: 120
        }
      ],
      fileDescription: "",
      resourceHeight: 400,
      //资源继承
      inheritResource: [],
      targetKeys: [],
      selectResource: [],
      listStyle: { width: "210px", height: "300px" },
      //通知相关的变量
      noticeModalShow: false,
      formItem: {
        title: "",
        content: ""
      },
      editFormItem: {
        title: "",
        description: ""
      },
      currentModuleNoticeList: [{ title: "", description: "" }],
      // 控制点击notice后模态框显示的modal
      noticeDetailShowModal: false,
      // 当前选中通知条目的详情
      currentNoticeDetail: [],
      panelList: [],
      panel: null,
      toProjectPage: "",
      toSubProjectPage: "",
      // 上传的文件
      file: [],
      // 上传文件个数限制定时器
      fileCountTimer: null,
      // 显示进度条的模态框
      progressModalShow: false,
      // 文件上传的进度
      uploadProgress: 0,
      // 问题解决流程结构
      processStructure: [],
      currentStep: {},
      // 用户角色
      userRole: ""
    };
  },
  created() {
    this.init();
  },
  mounted() {
    window.addEventListener("resize", this.reSize);
    this.openModuleSocket();
    this.getHistoryRecords();
    this.getProcessSteps();
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
        // vm.$router.push({ name: "Login" });
      } else if (vm.userRole == "Visitor") {
        next();
      } else {
        var isManager = false;
        var isMember = false;
        var subProjectInfo = vm.subProjectInfo;
        if (subProjectInfo.managerId == vm.$store.getters.userId) {
          isManager = true;
        } else {
          var members = subProjectInfo.members;
          for (var i = 0; i < members.length; i++) {
            if (members[i].userId == vm.$store.getters.userId) {
              isMember = true;
              break;
            }
          }
        }
        if (!(isManager || isMember)) {
          vm.$Message.error("You have no property to access it");
          // next(`/project/${vm.$store.getters.currentProjectId}`);
          vm.$router.go(-1);
        }
      }
    });
  },
  beforeRouteLeave(to, from, next) {
    this.removeTimer();
    this.closeModuleSocket();
    this.closePanel();
    if (this.stepChart != null) {
      this.stepChart.dispose();
    }
    next();
  },
  beforeDestroy: function() {
    window.removeEventListener("resize", this.reSize);
    this.closeModuleSocket();
  },
  updated: function() {
    this.$nextTick(function() {
      var div = document.getElementsByClassName("recordLine");
      for (let i = 0; i < div.length; i++) {
        div[i].scrollTop = div[i].scrollHeight;
      }
    });
  },
  methods: {
    initSize() {
      //侧边栏的高度随着屏幕的高度自适应
      this.sidebarHeight = window.innerHeight - 227;
      this.resourceHeight = this.sidebarHeight - 358;
      //通知栏的属性设置，top表示距离顶部的距离，duration表示持续的时间
    },
    reSize() {
      this.initSize();
      this.toolContainerLeft = window.innerWidth - 60;
      this.toolContainerTop = window.innerHeight - 140;
      document.getElementsByClassName("util-panel")[0].style.left = 0 + "px";
      document.getElementsByClassName("util-panel")[0].style.top = 200 + "px";
    },
    //初始化函数，作用是控制侧边栏的高度，设置右边通知栏弹出时候的距顶高度以及延迟的时间
    init() {
      this.initSize();
      var that = this;
      let subProjectId = this.$route.params.id;
      let subProjectInfo = this.$store.getters.subProject;
      if (
        JSON.stringify(subProjectInfo) != "{}" &&
        subProjectInfo.subProjectId == subProjectId
      ) {
        this.$set(this, "subProjectInfo", subProjectInfo);
        this.toProjectPage = "/project/" + subProjectInfo.projectId;
        this.toSubProjectPage =
          "/project/" + subProjectInfo.subProjectId + "/subproject";
      } else {
        $.ajax({
          url:
            "/GeoProblemSolving/subProject/inquiry" +
            "?key=subProjectId" +
            "&value=" +
            subProjectId,
          type: "GET",
          async: false,
          success: data => {
            if (data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (data != "None" && data != "Fail") {
              subProjectInfo = data[0];
              this.$set(this, "subProjectInfo", subProjectInfo);
              sessionStorage.setItem(
                "subProjectId",
                subProjectInfo.subProjectId
              );
              sessionStorage.setItem("subProjectName", subProjectInfo.title);

              this.managerIdentity(subProjectInfo.managerId);
              this.memberIdentity(subProjectInfo.members);
              this.$store.commit("setSubProjectInfo", subProjectInfo);
              this.toProjectPage = "/project/" + subProjectInfo.projectId;
              this.toSubProjectPage =
                "/project/" + subProjectInfo.subProjectId + "/subproject";
            }
          },
          error: function(err) {
            console.log("Get manager name fail.");
          }
        });
      }
      // 判断用户权限
      if (
        !this.subProjectInfo.isMember &&
        this.subProjectInfo.managerId != this.$store.getters.userId
      ) {
        this.userRole = "Visitor";
      }
    },
    managerIdentity(managerId) {
      if (managerId === this.$store.getters.userId) {
        this.subProjectInfo.isManager = true;
      }
    },
    memberIdentity(members) {
      for (let i = 0; i < members.length; i++) {
        if (members[i].userId === this.$store.getters.userId) {
          this.subProjectInfo.isMember = true;
          break;
        }
      }
    },
    showSteps() {
      this.selectedModule = [];
      let option = {
        animationDurationUpdate: 500,
        animationEasingUpdate: "quinticInOut",
        legend: {
          show: true,
          data: [
            {
              name: "Preparation",
              icon: "circle"
            },
            {
              name: "Analysis",
              icon: "circle"
            },
            {
              name: "Modeling",
              icon: "circle"
            },
            {
              name: "Simulation",
              icon: "circle"
            },
            {
              name: "Verification",
              icon: "circle"
            },
            {
              name: "Comparison",
              icon: "circle"
            }
          ]
        },
        series: [
          {
            type: "graph",
            layout: "none",
            legendHoverLink: true,
            roam: true,
            label: {
              normal: {
                show: true
              }
            },
            edgeSymbol: ["circle", "arrow"],
            edgeSymbolSize: [4, 10],
            focusNodeAdjacency: true,
            data: [],
            categories: [
              {
                name: "Preparation"
              },
              {
                name: "Analysis"
              },
              {
                name: "Simulation"
              },
              {
                name: "Modeling"
              },
              {
                name: "Verification"
              },
              {
                name: "Comparison"
              }
            ],
            links: [],
            lineStyle: {
              normal: {
                opacity: 1,
                width: 3,
                curveness: 0
              }
            }
          }
        ]
      };

      if (this.processStructure.length > 0) {
        this.selectedModule = [];
        for (let i = 0; i < this.processStructure.length; i++) {
          //get data
          if (this.processStructure[i].stepID == this.currentModule.moduleId) {
            option.series[0].data.push({
              name: this.processStructure[i].name,
              index: this.processStructure[i].id,
              moduleId: this.processStructure[i].stepID,
              x: this.processStructure[i].x,
              y: this.processStructure[i].y,
              category: this.processStructure[i].category,
              symbolSize: 45
            });
            this.selectedModule.push({
              moduleId: this.processStructure[i].stepID,
              index: this.processStructure[i].id,
              name: this.processStructure[i].name
            });
          } else {
            option.series[0].data.push({
              name: this.processStructure[i].name,
              index: this.processStructure[i].id,
              moduleId: this.processStructure[i].stepID,
              x: this.processStructure[i].x,
              y: this.processStructure[i].y,
              category: this.processStructure[i].category,
              symbolSize: 30
            });
          }

          //get links
          for (let j = 0; j < this.processStructure[i].next.length; j++) {
            option.series[0].links.push({
              source: this.processStructure[i].name,
              target: this.processStructure[i].next[j].name
            });
          }
        }
      }

      if (this.stepChart == null) {
        this.stepChart = echarts.init(document.getElementById("steps"));
      } else {
        this.stepChart.off("click");
        this.stepChart.off("dblclick");
      }
      this.stepChart.setOption(option);
      let _this = this;
      // 单击选择步骤
      this.stepChart.on("click", function(params) {
        if (option.series[0].data[params.data.index].symbolSize == 30) {
          option.series[0].data[params.data.index].symbolSize = 45;

          // record the selected step nodes
          _this.selectedModule.push({
            moduleId: params.data.moduleId,
            index: params.data.index,
            name: params.data.name
          });
        } else if (option.series[0].data[params.data.index].symbolSize == 45) {
          option.series[0].data[params.data.index].symbolSize = 30;

          // remove these not selected step nodes
          for (let i = 0; i < _this.selectedModule.length; i++) {
            if (_this.selectedModule[i].moduleId == params.data.moduleId) {
              _this.selectedModule.splice(i, 1);
              break;
            }
          }
        }
        _this.stepChart.setOption(option);
      });
      // 双击切换当前步骤
      this.stepChart.on("dblclick", function(params) {
        _this.currentStep = _this.processStructure[params.data.index];
        _this.getModuleInfo(params.data.moduleId);

        _this.selectedModule = [];
        option.series[0].data = [];
        for (let i = 0; i < _this.processStructure.length; i++) {
          //get data
          if (_this.processStructure[i].stepID == params.data.moduleId) {
            option.series[0].data.push({
              name: _this.processStructure[i].name,
              index: _this.processStructure[i].id,
              moduleId: _this.processStructure[i].stepID,
              x: _this.processStructure[i].x,
              y: _this.processStructure[i].y,
              category: _this.processStructure[i].category,
              symbolSize: 45
            });
            _this.selectedModule.push({
              moduleId: _this.processStructure[i].stepID,
              index: _this.processStructure[i].id,
              name: _this.processStructure[i].name
            });
          } else {
            option.series[0].data.push({
              name: _this.processStructure[i].name,
              index: _this.processStructure[i].id,
              moduleId: _this.processStructure[i].stepID,
              x: _this.processStructure[i].x,
              y: _this.processStructure[i].y,
              category: _this.processStructure[i].category,
              symbolSize: 30
            });
          }
        }
        _this.stepChart.setOption(option);
      });
      this.stepsModal = true;
    },
    addNewStep() {
      // 重复命名检测
      for (let i = 0; i < this.processStructure.length; i++) {
        if (this.formValidate1.moduleTitle == this.processStructure[i].name) {
          this.$Notice.info({
            desc: "The name of new step should not be different!"
          });
          return;
        }
      }
      if (
        this.formValidate1.moduleTitle != "" &&
        this.formValidate1.moduleType != ""
      ) {
        if (this.selectedModule.length > 0) {
          //  计算新增节点的属性信息
          let lastNode = [];
          let nodeLevel = 0;
          let nodeY = 0;
          let nodeCategory = 0;
          for (let i = 0; i < this.selectedModule.length; i++) {
            lastNode.push({
              name: this.selectedModule[i].name,
              id: this.selectedModule[i].index
            });

            if (
              this.processStructure[this.selectedModule[i].index].level >=
              nodeLevel
            ) {
              nodeLevel =
                this.processStructure[this.selectedModule[i].index].level + 1;
            }

            // modify original step node
            this.processStructure[this.selectedModule[i].index].next.push({
              name: this.formValidate1.moduleTitle,
              id: this.processStructure.length
            });
            this.processStructure[this.selectedModule[i].index].end = false;

            // calculate y
            if (this.processStructure[i].last == []) {
              nodeY = 200;
            } else {
              let sumY = 0;
              for (let j = 0; j < this.selectedModule.length; j++) {
                sumY += this.processStructure[this.selectedModule[j].index].y;
              }
              nodeY = sumY / this.selectedModule.length;
            }
          }

          let isOverlap = false;
          // 统计每层的节点数 并 非激活现有的step
          let levelNum = [];
          for (let i = 0; i < this.processStructure.length; i++) {
            if (this.processStructure[i].level == nodeLevel) {
              levelNum.push(this.processStructure[i].id);
            }
            this.processStructure[i].activeStatus = false;
          }
          // 节点重复检测
          for (let i = 0; i < levelNum.length; i++) {
            if (Math.abs(this.processStructure[levelNum[i]].y - nodeY) < 30) {
              isOverlap = true;
              break;
            }
          }

          // 新步骤的类别
          if (this.formValidate1.moduleType == "Preparation") {
            nodeCategory = 0;
          } else if (this.formValidate1.moduleType == "Analysis") {
            nodeCategory = 1;
          } else if (this.formValidate1.moduleType == "Modeling") {
            nodeCategory = 2;
          } else if (this.formValidate1.moduleType == "Simulation") {
            nodeCategory = 3;
          } else if (this.formValidate1.moduleType == "Validation") {
            nodeCategory = 4;
          } else if (this.formValidate1.moduleType == "Comparison") {
            nodeCategory = 5;
          }

          // create step node
          let newStepNode = {
            id: this.processStructure.length,
            stepID: "",
            name: this.formValidate1.moduleTitle,
            category: nodeCategory,
            last: lastNode,
            next: [],
            x: 0,
            y: nodeY,
            level: nodeLevel,
            end: true,
            activeStatus: true
          };
          this.processStructure.push(newStepNode);
          levelNum.push(newStepNode.id);

          // 如果重叠，修改y坐标
          if (isOverlap) {
            for (let i = 0; i < levelNum.length; i++) {
              this.processStructure[levelNum[i]].y =
                (400 / (levelNum.length + 1)) * (i + 1);
            }
            isOverlap = false;
          }

          // calculate x
          let maxLevel = 0;
          for (let i = 0; i < this.processStructure.length; i++) {
            if (this.processStructure[i].level > maxLevel) {
              maxLevel = this.processStructure[i].level;
            }
          }
          for (let i = 0; i < this.processStructure.length; i++) {
            this.processStructure[i].x =
              (800 / (maxLevel + 1)) * (this.processStructure[i].level + 1);
          }

          this.stepChart.dispose();
          this.stepChart = null;
          //关闭当前模态框
          this.stepsModal = false;
          //选择资源
          this.chooseResource();
        } else {
          this.$Notice.info({
            desc: "There is no step node being selected!"
          });
        }
      } else if (this.formValidate1.moduleTitle == "") {
        this.$Notice.info({
          desc: "The name of new step should not be empty!"
        });
      } else if (this.formValidate1.moduleType == "") {
        this.$Notice.info({
          desc: "The type of new step should not be empty!"
        });
      }
    },
    removeStep() {
      if (this.selectedModule.length > 0) {
        this.stepsModal = false;
        this.delModal = true;
      } else {
        this.$Notice.info({
          desc: "There is no step selected! "
        });
      }
    },
    closeModuleSocket() {
      if (this.subprojectSocket != null) {
        this.removeTimer();
        this.subprojectSocket.close();
      }
    },
    openModuleSocket() {
      if (this.subprojectSocket != null) {
        this.subprojectSocket = null;
      }
      let subProjectId = this.subProjectInfo.subProjectId;
      var subprojectSocketURL = "ws://" + this.$store.state.IP_Port + "/GeoProblemSolving/Module/" + subProjectId;
      if(this.$store.state.IP_Port == "localhost:8080"){
        subprojectSocketURL = "ws://localhost:8081/GeoProblemSolving/Module/" + subProjectId;
      }
      this.subprojectSocket = new WebSocket(subprojectSocketURL);
      this.subprojectSocket.onopen = this.onOpen;
      this.subprojectSocket.onmessage = this.onMessage;
      this.subprojectSocket.onclose = this.onClose;
      this.subprojectSocket.onerror = this.onError;
      this.setTimer();
    },
    onOpen() {
      console.log("ModuleSocket连接成功！");
    },
    // 更新人员，更新数据，更新records
    onMessage(e) {
      let messageJson = JSON.parse(e.data);
      let record = {
        type: "",
        time: "",
        who: "",
        content: ""
      };

      // 资源记录
      if (messageJson.type == "resource") {
        let record = {};
        record["who"] = messageJson.who;
        record["content"] = messageJson.content;
        record["time"] = messageJson.time;
        this.allRecords.push(messageJson);
        this.getAllResource();
      }
      // 工具记录
      else if (messageJson.type == "tools") {
        let record = {};
        record["who"] = messageJson.who;
        record["content"] = messageJson.content;
        record["time"] = messageJson.time;
        this.allRecords.push(messageJson);
      }
      // module 更新
      else if (messageJson.type == "step") {
        this.processStructure = messageJson.content;
      } else if (messageJson.type == "members") {
        // 比较 判断人员动态 更新records

        let members = messageJson.message
          .replace("[", "")
          .replace("]", "")
          .replace(/\s/g, "")
          .split(",");

        this.olParticipantChange(members);
      }
    },
    onClose(e) {
      this.removeTimer();
      console.log("ModuleSocket连接断开！");
    },
    onError(e) {
      this.removeTimer();
      console.log("ModuleSocket连接错误！");
    },
    setTimer() {
      var that = this;
      this.timer = setInterval(() => {
        var messageJson = {};
        messageJson["type"] = "ping";
        messageJson["message"] = "ping";
        if (
          that.subprojectSocket != null &&
          that.subprojectSocket != undefined
        ) {
          that.subprojectSocket.send(JSON.stringify(messageJson));
        }
      }, 20000);
    },
    removeTimer() {
      clearInterval(this.timer);
    },
    olParticipantChange(members) {
      let userIndex = -1;
      var record = {
        type: "participants",
        time: new Date().toLocaleString(),
        who: "",
        content: ""
      };

      // 自己刚上线，olParticipants空
      if (this.olParticipants.length == 0) {
        var that = this;
        for (let i = 0; i < members.length; i++) {
          this.axios
            .get(
              "/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                members[i]
            )
            .then(res => {
              if (res.data != "None" && res.data != "Fail") {
                that.olParticipants.push(res.data);
                record.content =
                  "welcome to here, " + that.$store.getters.userName;
              } else if (res.data == "None") {
              }
            });
        }
      } else {
        // members大于olParticipants，有人上线；小于olParticipants，离线
        if (members.length > this.olParticipants.length) {
          for (var i = 0; i < members.length; i++) {
            for (var j = 0; j < this.olParticipants.length; j++) {
              if (members[i] == this.olParticipants[j].userId) {
                break;
              }
            }
            if (j == this.olParticipants.length) {
              userIndex = i;
              break;
            }
          }

          // 人员渲染
          var that = this;
          this.axios
            .get(
              "/GeoProblemSolving/user/inquiry" +
                "?key=" +
                "userId" +
                "&value=" +
                members[userIndex]
            )
            .then(res => {
              if (res.data != "None" && res.data != "Fail") {
                that.olParticipants.push(res.data);
                if (userIndex != -1) {
                  record.who = res.data.userName;
                  record.content = "enter this process module.";
                }
              } else if (res.data == "None") {
              }
            });
        } else if (members.length < this.olParticipants.length) {
          for (var i = 0; i < this.olParticipants.length; i++) {
            for (var j = 0; j < members.length; j++) {
              if (this.olParticipants[i].userId == members[j]) {
                break;
              }
            }
            if (j == members.length) {
              userIndex = i;
              break;
            }
          }
          record.who = this.olParticipants[userIndex].userName;
          record.content = "leave this process module.";
          this.olParticipants.splice(userIndex, 1);
        }
      }
      //records 更新
      this.allRecords.push(record);
    },
    getHistoryRecords() {
      this.allHistRecords = [];
      let that = this;
      this.axios
        .get(
          "/GeoProblemSolving/history/inquiry" +
            "?eventType=record" +
            "&key=scopeId" +
            "&value=" +
            this.subProjectInfo.subProjectId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "None" && res.data != "Fail") {
            for (let i = 0; i < res.data.length; i++) {
              let record = JSON.parse(res.data[i].description);
              that.allHistRecords.push(record);
            }
          }
        });
    },
    chooseResource() {
      this.inheritResource = this.getMockData();
    },
    createModule() {
      this.selectResource = [];
      this.selectResource = this.getTargetKeys();

      // 创建项目
      let index = this.processStructure.length - 1;
      let Module = {};
      Module["subProjectId"] = this.$route.params.id;
      Module["title"] = this.formValidate1.moduleTitle;
      Module["description"] = "There is no description of this module.";
      Module["creator"] = this.$store.getters.userId;
      Module["type"] = this.formValidate1.moduleType;
      this.axios
        .post("/GeoProblemSolving/module/create", Module)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data === "Fail") {
            this1.$Message.info("Fail");
          } else {
            // new StepID
            this.processStructure[index].stepID = res.data;

            // current step and module
            this.currentStep = this.processStructure[index];
            Module["moduleId"] = res.data;
            this.currentModule = Module;
            // 存储Step
            this.updateSteps();

            // collaborative
            let socketMsg = {
              type: "step",
              operate: "update",
              content: JSON.stringify(this.processStructure)
            };
            this.subprojectSocket.send(socketMsg);

            //更新新module的资源---------------------静态化之后完成
            // this.copyResource(res.data);

            this.createModuleSuccess(Module["title"]);
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    getMockData() {
      let mockData = [];
      for (let i = 0; i < this.selectedModule.length; i++) {
        let selectedModuleId = this.selectedModule[i].moduleId;
        if (selectedModuleId == this.currentModule.moduleId) {
          for (let i = 0; i < this.resourceList.length; i++) {
            mockData.push({
              key: i,
              name: this.resourceList[i].name,
              type: this.resourceList[i].type,
              resourceId: this.resourceList[i].resourceId
            });
          }
        } else {
          let selectedRes = [];
          $.ajax({
            url:
              "/GeoProblemSolving/resource/inquiry" +
              "?key=scope.moduleId" +
              "&value=" +
              selectedModuleId,
            type: "GET",
            async: false,
            success: function(data) {
              if (data !== "None") {
                selectedRes = data;
                for (let i = 0; i < selectedRes.length; i++) {
                  mockData.push({
                    key: i,
                    name: selectedRes.name,
                    type: selectedRes.type,
                    resourceId: selectedRes.resourceId
                  });
                }
              } else {
                selectedRes = [];
              }
            },
            error: function(err) {
              selectedRes = [];
              console.log("err!");
            }
          });
        }
      }
      this.selectedModule = [];

      if (mockData.length > 0) {
        this.inheritData = true;
      } else {
        this.inheritData = false;
        this.createModule();
      }

      return mockData;
    },
    getTargetKeys() {
      let mockData = [];
      if (this.inheritResource.length > 0) {
        for (let i = 0; i < this.targetKeys.length; i++) {
          mockData.push({
            key: this.targetKeys[i],
            name: this.inheritResource[this.targetKeys[i]].name,
            type: this.inheritResource[this.targetKeys[i]].type,
            resourceId: this.inheritResource[this.targetKeys[i]].resourceId
          });
        }
      }
      return mockData;
    },
    handleChange(newTargetKeys) {
      this.targetKeys = newTargetKeys;
    },
    filterMethod(data, query) {
      if (data.length > 0) {
        return data.type.indexOf(query) > -1;
      } else {
        return false;
      }
    },
    resourceRender(item) {
      return item.type + " - " + item.name;
    },
    getProcessSteps() {
      this.processStructure = [];
      if (
        this.subProjectInfo.solvingProcess == undefined ||
        this.subProjectInfo.solvingProcess.length == 0
      ) {
        // 为了兼容
        this.axios
          .get(
            "/GeoProblemSolving/module/inquiry" +
              "?key=subProjectId" +
              "&value=" +
              this.subProjectInfo.subProjectId
          )
          .then(res => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data != "None" && res.data != "Fail") {
              let moduleList = res.data;

              let nodeCategory = 0;
              if (moduleList[0].type == "Preparation") {
                nodeCategory = 0;
              } else if (moduleList[0].type == "Analysis") {
                nodeCategory = 1;
              } else if (moduleList[0].type == "Modeling") {
                nodeCategory = 2;
              } else if (moduleList[0].type == "Simulation") {
                nodeCategory = 3;
              } else if (moduleList[0].type == "Validation") {
                nodeCategory = 4;
              } else if (moduleList[0].type == "Comparison") {
                nodeCategory = 5;
              }

              this.processStructure.push({
                id: 0,
                stepID: moduleList[0].moduleId,
                name: moduleList[0].title,
                category: nodeCategory,
                last: [],
                next: [{ name: moduleList[1].title, id: 1 }],
                x: 0,
                y: 200,
                level: 0,
                end: false,
                activeStatus: moduleList[0].activeStatus
              });

              for (let i = 1; i < moduleList.length - 1; i++) {
                if (moduleList[i].type == "Preparation") {
                  nodeCategory = 0;
                } else if (moduleList[i].type == "Analysis") {
                  nodeCategory = 1;
                } else if (moduleList[i].type == "Modeling") {
                  nodeCategory = 2;
                } else if (moduleList[i].type == "Simulation") {
                  nodeCategory = 3;
                } else if (moduleList[i].type == "Validation") {
                  nodeCategory = 4;
                } else if (moduleList[i].type == "Comparison") {
                  nodeCategory = 5;
                }

                this.processStructure.push({
                  id: i,
                  stepID: moduleList[i].moduleId,
                  name: moduleList[i].title,
                  category: nodeCategory,
                  last: [{ name: moduleList[i - 1].title, id: i - 1 }],
                  next: [{ name: moduleList[i + 1].title, id: i + 1 }],
                  x: i * (800 / moduleList.length),
                  y: 200,
                  level: i,
                  end: false,
                  activeStatus: moduleList[i].activeStatus
                });
              }

              if (moduleList[moduleList.length - 1].type == "Preparation") {
                nodeCategory = 0;
              } else if (moduleList[moduleList.length - 1].type == "Analysis") {
                nodeCategory = 1;
              } else if (moduleList[moduleList.length - 1].type == "Modeling") {
                nodeCategory = 2;
              } else if (
                moduleList[moduleList.length - 1].type == "Simulation"
              ) {
                nodeCategory = 3;
              } else if (
                moduleList[moduleList.length - 1].type == "Validation"
              ) {
                nodeCategory = 4;
              } else if (
                moduleList[moduleList.length - 1].type == "Comparison"
              ) {
                nodeCategory = 5;
              }

              this.processStructure.push({
                id: moduleList.length - 1,
                stepID: moduleList[moduleList.length - 1].moduleId,
                name: moduleList[moduleList.length - 1].title,
                category: nodeCategory,
                last: [
                  {
                    name: moduleList[moduleList.length - 1].title,
                    id: moduleList.length - 1
                  }
                ],
                next: [],
                x: (moduleList.length - 1) * (800 / moduleList.length),
                y: 200,
                level: moduleList.length - 1,
                end: true,
                activeStatus: moduleList[moduleList.length - 1].activeStatus
              });

              for (let i = 0; i < this.processStructure.length; i++) {
                if (this.processStructure[i].activeStatus) {
                  this.currentStep = this.processStructure[i];
                  this.currentModule = moduleList[i];
                  break;
                } else if (
                  i == this.processStructure.length - 1 &&
                  this.processStructure[i].activeStatus == undefined
                ) {
                  this.processStructure[i].activeStatus = true;
                  this.currentStep = this.processStructure[i];
                  this.currentModule = moduleList[i];
                }
              }
            } else if (res.data == "None" || res.data == "Fail") {
              this.currentModule = [];
            }
          })
          .catch(err => {});
      } else if (this.subProjectInfo.solvingProcess.length > 0) {
        this.processStructure = JSON.parse(this.subProjectInfo.solvingProcess);

        for (let i = 0; i < this.processStructure.length; i++) {
          if (this.processStructure[i].activeStatus) {
            this.currentStep = this.processStructure[i];
            // 请求module数据
            this.getModuleInfo(this.processStructure[i].stepID);
          }
        }
      }
    },
    getModuleInfo(moduleId) {
      this.axios
        .get(
          "/GeoProblemSolving/module/inquiry" +
            "?key=moduleId" +
            "&value=" +
            moduleId
        )
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "None" && res.data != "Fail") {
            this.currentModule = res.data[0];
          } else if (res.data == "None") {
            this.currentModule = [];
          }
        })
        .catch(err => {});
    },
    addModule(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          if (
            this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
          ) {
            // 创建module
            let Module = {};
            Module["subProjectId"] = this.$route.params.id;
            Module["title"] = this.formValidate1.moduleTitle;
            Module["description"] = this.moduleDescription;
            Module["creator"] = this.$store.getters.userId;
            Module["type"] = this.formValidate1.moduleType;
            this.axios
              .post("/GeoProblemSolving/module/create", Module)
              .then(res => {
                if (res.data == "Offline") {
                  this.$store.commit("userLogout");
                  this.$router.push({ name: "Login" });
                } else if (res.data === "Fail") {
                  this.$Message.info("Fail");
                } else {
                  this.currentModule = Module;

                  // 创建步骤
                  if (this.processStructure.length === 0) {
                    let nodeCategory = 0;
                    if (this.formValidate1.moduleType == "Preparation") {
                      nodeCategory = 0;
                    } else if (this.formValidate1.moduleType == "Analysis") {
                      nodeCategory = 1;
                    } else if (this.formValidate1.moduleType == "Modeling") {
                      nodeCategory = 2;
                    } else if (this.formValidate1.moduleType == "Simulation") {
                      nodeCategory = 3;
                    } else if (this.formValidate1.moduleType == "Validation") {
                      nodeCategory = 4;
                    } else if (this.formValidate1.moduleType == "Comparison") {
                      nodeCategory = 5;
                    }

                    // create step node
                    let newStepNode = {
                      id: this.processStructure.length,
                      stepID: res.data,
                      name: this.formValidate1.moduleTitle,
                      category: nodeCategory,
                      last: [],
                      next: [],
                      x: 400,
                      y: 200,
                      level: 0,
                      end: true,
                      activeStatus: true
                    };
                    this.processStructure.push(newStepNode);
                    this.currentStep = newStepNode;

                    // 存储Step
                    this.updateSteps();

                    // collaborative
                    let socketMsg = {
                      type: "step",
                      operate: "update",
                      content: JSON.stringify(this.processStructure)
                    };
                    this.subprojectSocket.send(socketMsg);
                  }

                  this.createModuleSuccess(Module["title"]);
                  this.formValidate1.moduleTitle = "";
                  this.moduleDescription = "";
                  this.formValidate1.moduleType = "";
                }
              })
              .catch(err => {
                console.log(err.data);
              });
          }
        } else {
          this.$Message.error("Please enter the necessary information!");
        }
      });
    },
    activateModule() {
      var this1 = this;
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        if (!this.currentStep.activeStatus) {
          for (let i = 0; i < this.processStructure.length; i++) {
            this.processStructure[i].activeStatus = false;
          }
          this.processStructure[this.currentStep.id].activeStatus = true;
          this.currentStep = this.processStructure[this.currentStep.id];

          // 保存 step
          this.updateSteps();

          // collaborative
          let socketMsg = {
            type: "step",
            operate: "update",
            content: JSON.stringify(this.processStructure)
          };
          this.subprojectSocket.send(socketMsg);
        }
      }
    },
    delModule() {
      if (
        this.$store.getters.userInfo.userId == this.subProjectInfo.managerId
      ) {
        for (let i = 0; i < this.selectedModule.length; i++) {
          let currentIndex = this.selectedModule[i].index;
          if (
            this.processStructure[currentIndex].end &&
            this.processStructure[currentIndex].name != this.currentStep.name
          ) {
            // 删除module
            this.axios
              .get(
                "/GeoProblemSolving/module/delete" +
                  "?moduleId=" +
                  this.processStructure[currentIndex].stepID
              )
              .then(res => {
                if (res.data === "Success") {
                }
              })
              .catch(err => {
                console.log(err.data);
              });

            // 删除step节点
            if (currentIndex > 0) {
              // 处理被删除节点的前驱节点
              for (
                let j = 0;
                j < this.processStructure[currentIndex].last.length;
                j++
              ) {
                let lastIndex = this.processStructure[currentIndex].last[j].id;
                if (this.processStructure[lastIndex].next.length === 1) {
                  this.processStructure[lastIndex].next = [];
                } else if (this.processStructure[lastIndex].next.length > 1) {
                  for (
                    let s = 0;
                    s < this.processStructure[lastIndex].next.length;
                    s++
                  ) {
                    if (
                      this.processStructure[lastIndex].next[s].name ===
                      this.selectedModule[i].name
                    ) {
                      this.processStructure[lastIndex].next.splice(s, 1);
                    }
                  }
                }
                this.processStructure[lastIndex].end = true;
              }

              // 删除节点
              this.processStructure.splice(currentIndex, 1);

              // 处理后继节点, 统一步骤id与step在数组里的位置index
              for (
                let j = currentIndex;
                j < this.processStructure.length;
                j++
              ) {
                if (this.processStructure[j].id !== j) {
                  this.processStructure[j].id = j;
                }
              }
            } else if (currentIndex === 0) {
              // 删除节点
              this.processStructure.splice(currentIndex, 1);
            }
            this.updateSteps();

            // collaborative
            let socketMsg = {
              type: "step",
              operate: "update",
              content: JSON.stringify(this.processStructure)
            };
            this.subprojectSocket.send(socketMsg);
          } else {
            this.$Notice.info({
              desc:
                "The selected step " +
                this.selectedModule[i].name +
                "can not be removed. Because it has the next step, or it is a active step."
            });
          }
        }
      }
    },
    updateModule(name) {
      let _this = this;
      let this1 = this;
      this.$refs[name].validate(valid => {
        if (valid) {
          if (
            _this.$store.getters.userInfo.userId ==
            _this.subProjectInfo.managerId
          ) {
            let updateObject = new URLSearchParams();
            updateObject.append("moduleId", _this.currentModule.moduleId);
            updateObject.append("title", _this.formValidate2.updateModuleTitle);
            updateObject.append("description", _this.updateModuleDescription);
            updateObject.append("type", _this.formValidate2.updateModuleType);
            updateObject.append("creater", _this.$store.getters.userId);
            _this.axios
              .post("/GeoProblemSolving/module/update", updateObject)
              .then(res => {
                if (res.data == "Success") {
                  // module update
                  this1.currentModule.title =
                    this1.formValidate2.updateModuleTitle;
                  this1.currentModule.description =
                    this1.updateModuleDescription;
                  this1.currentModule.type =
                    this1.formValidate2.updateModuleType;

                  // step update
                  if (
                    this.formValidate2.updateModuleTitle !=
                    this.currentStep.name
                  ) {
                    // 更新后继步骤
                    for (let i = 0; i < this.currentStep.next.length; i++) {
                      let index = this.currentStep.next[i].id;
                      for (
                        let j = 0;
                        i < this.processStructure[index].last.length;
                        j++
                      ) {
                        if (
                          this.processStructure[index].last[j].name ==
                          this.currentStep.name
                        ) {
                          this.processStructure[index].last[
                            j
                          ].name = this.formValidate2.updateModuleTitle;
                        }
                      }
                    }
                    // 更新前驱步骤
                    for (let i = 0; i < this.currentStep.last.length; i++) {
                      let index = this.currentStep.last[i].id;
                      for (
                        let j = 0;
                        j < this.processStructure[index].next.length;
                        j++
                      ) {
                        if (
                          this.processStructure[index].next[j].name ==
                          this.currentStep.name
                        ) {
                          this.processStructure[index].next[
                            j
                          ].name = this.formValidate2.updateModuleTitle;
                        }
                      }
                    }
                  }
                  // 更新当前步骤
                  let nodeCategory = 0;
                  if (this.formValidate2.updateModuleType == "Preparation") {
                    nodeCategory = 0;
                  } else if (
                    this.formValidate2.updateModuleType == "Analysis"
                  ) {
                    nodeCategory = 1;
                  } else if (
                    this.formValidate2.updateModuleType == "Modeling"
                  ) {
                    nodeCategory = 2;
                  } else if (
                    this.formValidate2.updateModuleType == "Simulation"
                  ) {
                    nodeCategory = 3;
                  } else if (
                    this.formValidate2.updateModuleType == "Validation"
                  ) {
                    nodeCategory = 4;
                  } else if (
                    this.formValidate2.updateModuleType == "Comparison"
                  ) {
                    nodeCategory = 5;
                  }
                  this.currentStep.name = this.formValidate2.updateModuleTitle;
                  this.currentStep.category = nodeCategory;
                  let index = this.currentStep.index;
                  this.processStructure[index] = this.currentStep;

                  this.updateSteps();

                  // collaborative
                  let socketMsg = {
                    type: "step",
                    operate: "update",
                    content: JSON.stringify(this.processStructure)
                  };
                  this.subprojectSocket.send(socketMsg);
                }
              })
              .catch(err => {
                console.log(err.data);
              });
          }
        } else {
          this.$Message.error("Please enter the necessary information !");
        }
      });
    },
    updateSteps() {
      let obj = new URLSearchParams();
      obj.append("subProjectId", this.subProjectInfo.subProjectId);
      obj.append("solvingProcess", JSON.stringify(this.processStructure));
      this.axios
        .post("/GeoProblemSolving/subProject/update", obj)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            this.$Notice.info({
              desc: "Update successfully!"
            });
          } else {
            this.$Message.error("Update sub-project failed.");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    copyResource(newModuleId) {
      // for (let i = 0; i < this.selectResource.length; i++) {
      //   for (let j = 0; j < this.resourceList.length; j++) {
      //     if (
      //       this.resourceList[j].resourceId == this.selectResource[i].resourceId
      //     ) {
      //       let resourceInfo = this.resourceList[j];
      //       let shareForm = new FormData();
      //       shareForm.append("name", resourceInfo.name);
      //       shareForm.append("description", resourceInfo.description);
      //       shareForm.append("belong", resourceInfo.belong);
      //       shareForm.append("type", resourceInfo.type);
      //       shareForm.append("fileSize", resourceInfo.fileSize);
      //       shareForm.append("pathURL", resourceInfo.pathURL);
      //       shareForm.append("uploaderId", resourceInfo.uploaderId);
      //       let scopeObject = {
      //         projectId: "",
      //         subProjectId: "",
      //         moduleId: newModuleId
      //       };
      //       shareForm.append("scope", JSON.stringify(scopeObject));
      //       if (
      //         newModuleId != null &&
      //         newModuleId != undefined &&
      //         newModuleId.length > 0
      //       ) {
      //         this.axios
      //           .post("/GeoProblemSolving/resource/share", shareForm)
      //           .then(res => {
      //             if (res.data != "Fail") {
      //               //...
      //             }
      //           })
      //           .catch(err => {
      //             console.log(err);
      //           });
      //       }
      //       break;
      //     }
      //   }
      // }
    },
    toResourceList() {
      this.$router.push({ path: "/resourceList" });
    },
    getFile(event) {
      this.file = event.target.files[0];
    },
    // 这里模仿项目层面上传文件的写法
    // gatherFile
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
    submitFile(name) {
      this.uploadProgress = 0;
      this.$refs[name].validate(valid => {
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
              formData.append("description", this.fileDescription);
              formData.append("type", this.formValidate3.fileType);
              formData.append("uploaderId", this.$store.getters.userId);
              formData.append("belong", this.currentModule.title);
              let scopeObject = {
                projectId: "",
                subProjectId: "",
                moduleId: this.currentModule.moduleId
              };
              formData.append("scope", JSON.stringify(scopeObject));
              formData.append("privacy", this.formValidate3.filePrivacy);
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
                    that.$store.commit("userLogout");
                    that.$router.push({ name: "Login" });
                  } else if (res.data != "Size over" && res.data.length > 0) {
                    this.$Notice.open({
                      title: "Upload notification title",
                      desc: "File uploaded successfully"
                    });
                    that.progressModalShow = false;
                    that.uploadProgress = 0;
                    that.getAllResource();
                    that.file = [];
                    that.fileDescription = "";
                    that.filePrivacy = "private";
                    that.fileType = "data";
                    // 同步
                    let record = {
                      who: that.$store.getters.userName,
                      whoid: that.$store.getters.userId,
                      type: "resource",
                      content:
                        "upload a/an " +
                        that.formValidate3.fileType +
                        " : " +
                        that.file.name,
                      moduleId: this.currentModule.moduleId,
                      time: new Date().toLocaleString(),
                      file: res.data[0].fileName
                    };
                    that.subprojectSocket.send(JSON.stringify(record));
                  }
                })
                .catch(err => {
                  this.progressModalShow = false;
                  this.uploadProgress = 0;
                });
            }
          }
        } else {
          this.$Message.error(
            "size over,all the file size must smaller than 1 GB one time!"
          );
        }
      });
    },

    sleep(time) {
      return new Promise(resolve => setTimeout(resolve, time));
    },
    //获取全部资源的方法
    getAllResource() {
      this.axios
        .get(
          "/GeoProblemSolving/resource/inquiry" +
            "?key=scope.moduleId" +
            "&value=" +
            window.sessionStorage.getItem("moduleId")
        )
        .then(res => {
          if (res.data !== "None") {
            this.$set(this, "resourceList", res.data);
            //存入store
          } else {
            this.resourceList = [];
          }
          sessionStorage.setItem("resources", JSON.stringify(res.data));
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    backtoSubproject() {
      this.$router.push(`./subproject`);
    },
    editModalShow() {
      this.editModal = true;
      this.formValidate2.updateModuleTitle = this.currentModule.title;
      this.formValidate2.updateModuleType = this.currentModule.type;
      this.updateModuleDescription = this.currentModule.description;
    },
    ok() {
      this.$Message.info("Clicked ok");
    },
    cancel() {},
    show(index) {
      window.open(this.resourceList[index].pathURL);
    },
    toolContainerMove(e) {
      let odiv = e.target;
      let disX = e.clientX - odiv.offsetLeft;
      let disY = e.clientY - odiv.offsetTop;
      document.onmousemove = e => {
        let left = e.clientX - disX;
        let top = e.clientY - disY;

        if (
          !(
            left < 0 ||
            top < 60 ||
            left > window.innerWidth - 60 ||
            top > window.innerHeight - 140
          )
        ) {
          //移动当前元素
          odiv.style.left = left + "px";
          odiv.style.top = top + "px";
        }
      };
      document.onmouseup = e => {
        document.onmousemove = null;
        document.onmouseup = null;
      };
    },
    createModuleSuccess(title) {
      this.$Notice.success({
        title: "Create Notification",
        desc:
          "The process module" +
          `<span style="color:lightblue"><strong>` +
          "&nbsp" +
          title +
          "&nbsp" +
          `</strong></span>` +
          "has been created successfully"
      });
    },
    deleteModuleSuccess() {
      this.$Notice.info({
        title: "Delete Notification",
        desc: "The module has been deleted successfully"
      });
    },
    quitSubProject() {
      this.axios
        .get(
          "/GeoProblemSolving/subProject/quit" +
            "?subProjectId=" +
            this.$route.params.id +
            "&userId=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data == "Success") {
            let projectId = sessionStorage.getItem("projectId");
            this.$router.push({
              name: "ProjectDetail",
              params: { id: projectId }
            });
          } else {
            this.$Message.error("Fail!");
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
    // jspanel工具
    toolPanel(type) {
      if (this.userRole != "Visitor") {
        this.axios
          .post("/GeoProblemSolving/user/state")
          .then(res => {
            if (!res.data) {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else {
              var toolURL = "";
              let toolName = "";
              if (type == "map") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/map" style="width: 100%;height:100%"></iframe>';
                toolName = "Map";
              } else if (type == "draw") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/draw" style="width: 100%;height:100%"></iframe>';
                toolName = "Drawing";
              } else if (type == "chart") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/charts" style="width: 100%;height:100%"></iframe>';
                toolName = "Chart";
              } else if (type == "chat") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/chat" style="width: 100%;height:100%"></iframe>';
                toolName = "Chatroom";
              } else if (type == "graphEditor") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/GraphEditor/index.html' +
                  "?groupID=" +
                  this.currentModule.moduleId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Sketchpad";
              } else if (type == "3DmodelViewer") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/3DmodelViewer/index.html' +
                  "?groupID=" +
                  this.currentModule.moduleId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "3D model viewer";
              } else if (type == "LogicalModel") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/LogicalModel/index.html' +
                  "?groupID=" +
                  this.currentModule.moduleId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Logical modeling";
              } else if (type == "ConceptualModel") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/ConceptualModel/index.html' +
                  "?groupID=" +
                  this.currentModule.moduleId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Conceptual modeling";
              } else if (type == "ComputationalModel") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/ComputationalModel/index.html' +
                  "?groupID=" +
                  this.currentModule.moduleId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Computational modeling";
              } else if (type == "tableEditor") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/jexcelTool/index.html' +
                  "?groupID=" +
                  this.currentModule.moduleId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Table editor";
              } else if (type == "nc-map") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/nc/map" style="width: 100%;height:100%"></iframe>';
                toolName = "Map";
              } else if (type == "nc-draw") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/nc/draw" style="width: 100%;height:100%"></iframe>';
                toolName = "Drawing";
              } else if (type == "nc-chart") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/nc/charts" style="width: 100%;height:100%"></iframe>';
                toolName = "Chart";
              } else if (type == "cn-tableEditor") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/jexcelTool/excelToolSingle.html' +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Table editor";
              } else if (type == "nc-3DmodelViewer") {
                toolURL =
                  '<iframe src="/GeoProblemSolving/Collaborative/3DmodelViewer/indexSingle.html' +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "3D model viewer";
              } else if (type == "nc-video") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/video" style="width: 100%;height:100%"></iframe>';
                toolName = "Video player";
              } else if (type == "nc-pdf") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/pdfview" style="width: 100%;height:100%"></iframe>';
                toolName = "Pdf viewer";
              } else if (type == "Doc Edit") {
                toolURL =
                  '<iframe src="' +
                  "http://" +
                  this.$store.state.IP_Port +
                  '/GeoProblemSolving/tinymce" style="width: 100%;height:100%"></iframe>';
                toolName = "Text editor";
              } else if (type == "Video Tool") {
                var userId = this.$store.getters.userId;
                var moduleId = this.currentModule.moduleId;
                var reg = /(\S)*:80/;
                var IP_Port = this.$store.state.IP_Port;
                var videoIP_Port = IP_Port.match(reg)[1];
                toolURL =
                  '<iframe src="https://' +
                  videoIP_Port +
                  ":8083/GeoProblemSolving/Collaborative/vedioChat/WebRtcTest.html" +
                  "?roomId=" +
                  moduleId +
                  "&userId=" +
                  userId +
                  '" style="width: 100%;height:100%"></iframe>';
                toolName = "Video Tool";
              } else if (type == "Web-SWMM") {
                toolURL =
                  '<iframe src="http://geomodeling.njnu.edu.cn/hydro-model-integration/webswmm" style="width: 100%; height:100%"></iframe>';
                toolName = "Web-SWMM";
              } else if (type == "TrafficNoise") {
                toolURL =
                  '<iframe src="http://geomodeling.njnu.edu.cn/TrafficNoiseTheme/trafficNoise.html" style="width: 100%; height:100%"></iframe>';
                toolName = "Traffic Noise";
              }

              let panel = jsPanel.create({
                theme: "success",
                headerTitle: toolName,
                footerToolbar: '<p style="height:10px"></p>',
                contentSize: "1200 600",
                content: toolURL,
                disableOnMaximized: true,
                dragit: {
                  containment: 5
                },
                callback: function() {
                  // this.content.style.padding = "20px";
                }
              });
              // panel.resizeit("disable");
              $(".jsPanel-content").css("font-size", "0");
              this.panelList.push(panel);
              // 生成records, 同步
              let record = {
                who: this.$store.getters.userName,
                whoid: this.$store.getters.userId,
                type: "tools",
                toolType: type,
                content: "used a tool: " + type,
                moduleId: this.currentModule.moduleId,
                time: new Date().toLocaleString()
              };
              this.subprojectSocket.send(JSON.stringify(record));
            }
          })
          .catch(err => {
            console.log("Get user info fail.");
          });
      }
      else {
        this.$Notice.info({
        desc: "Please join this project first!"
      });
      }
    },
    closePanel() {
      for (let i = 0; i < this.panelList.length; i++) {
        this.panelList[i].close();
      }
    },
    createNotice() {
      let noticeForm = {};
      noticeForm["title"] = this.formItem.title;
      noticeForm["description"] = this.formItem.content;
      noticeForm["moduleId"] = this.currentModule.moduleId;
      this.axios
        .post("/GeoProblemSolving/bulletin/save", noticeForm)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            this.$Notice.success({
              title: "Create notice result",
              desc: "The notice has been created successfully!"
            });
            this.inquiryNotice();
          }
        })
        .catch(err => {
          console.log(err.data);
        });
    },
    inquiryNotice() {
      this.axios
        .get(
          "/GeoProblemSolving/bulletin/inquiry?key=moduleId&value=" +
            window.sessionStorage.getItem("moduleId")
        )
        .then(res => {
          if (res.data != "Fail") {
            this.currentModuleNoticeList = res.data;
          }
        })
        .catch(err => {});
    },
    editNotice() {
      let updateForm = new URLSearchParams();
      updateForm.append("bulletinId", this.currentNoticeDetail["bulletinId"]);
      updateForm.append("title", this.editFormItem["title"]);
      updateForm.append("description", this.editFormItem["description"]);
      this.axios
        .post("/GeoProblemSolving/bulletin/update", updateForm)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            this.$Notice.info({
              title: "update result",
              desc: "update announcement successfully!"
            });
            this.inquiryNotice();
          }
        })
        .catch(err => {});
    },
    deleteNotice() {
      var index = this.currentModuleNoticeList.length - 1;
      let bulletinId = this.currentModuleNoticeList[index]["bulletinId"];
      this.axios
        .get("/GeoProblemSolving/bulletin/delete" + "?bulletinId=" + bulletinId)
        .then(res => {
          if (res.data == "Offline") {
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          } else if (res.data != "Fail") {
            this.$Notice.info({
              title: "delete result",
              desc: "delete announcement successfully!"
            });
            this.inquiryNotice();
          }
        })
        .catch(err => {});
    },
    noticeDetailShow() {
      this.noticeDetailShowModal = true;
      let index = this.currentModuleNoticeList.length - 1;
      this.currentNoticeDetail = this.currentModuleNoticeList[index];
      this.editFormItem.title = this.currentNoticeDetail.title;
      this.editFormItem.description = this.currentNoticeDetail.description;
      // this.currentModuleNoticeList[index];显示在模态框里面的内容
    },
    previewRes(index) {
      let name = this.resourceList[index].name;

      if (/\.(doc|docx|xls|xlsx|csv|ppt|pptx|zip)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://172.21.212.72:8012/previewFile?url=" +
          "http://" +
          this.$store.state.IP_Port +
          this.resourceList[index].pathURL;
        var toolURL =
          "<iframe src=" + url + ' style="width: 100%;height:100%"></iframe>';
        let panel = jsPanel.create({
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
        this.panelList.push(panel);
      } else if (/\.(mp4)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://" +
          this.$store.state.IP_Port +
          this.resourceList[index].pathURL;
        var toolURL =
          "<video src=" +
          url +
          ' style="width: 100%;height:100%" controls></video>';
        let panel = jsPanel.create({
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
        this.panelList.push(panel);
      } else if (/\.(pdf|xml|json|md|gif|jpg|png)$/.test(name.toLowerCase())) {
        if (this.panel != null) {
          this.panel.close();
        }
        var url =
          "http://" +
          this.$store.state.IP_Port +
          this.resourceList[index].pathURL;
        var toolURL =
          "<iframe src=" +
          url +
          ' style="width: 100%;height:100%" controls></iframe>';
        let panel = jsPanel.create({
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
        this.panelList.push(panel);
      } else {
        this.$Notice.error({
          title: "Open failed",
          desc: "Not supported file format."
        });
        return false;
      }
    }
  }
};
</script>
