<style scoped>
.detailContent {
  padding: 15px 10px;
  height: 500px;
  overflow-y: auto;
  background-color: #f7f7f7;
}
.noDetail h1 {
  color: darkgray;
  text-align: center;
}
.noticeDetail {
  min-width: 310px;
  width: 33%;
  float: left;
  height: 180px;
  padding: 5px;
}
.noticeDeleteBtn {
  float: right;
  color: #ff7800;
  cursor: pointer;
}
.noticeDescription {
  display: block;
  text-indent: 2em;
  height: 80px;
  margin: 5px 0;
  background-color: #f7f7f7;
  word-break: break-word;
  overflow-y: auto;
}
.noticeReadBtn {
  float: right;
  color: cornflowerblue;
}
.approveApplyBtn {
  float: right;
  color: #19be6b;
  margin: 0px 2px;
}
.refuseApplyBtn {
  float: right;
  color: #ed4014;
  margin: 0px 2px;
}
</style>
<template>
<div>
  <Row>
    <Col span="22" offset="1">
      <h1 style="margin-top:20px">Notifications</h1>
      <Tabs type="card" value="notice" style="margin:20px 0">
        <TabPane :label="noticeTab" name="notice">
          <Card>
            <h3 slot="title">Notice Detail</h3>
            <a slot="extra" @click="readAllNotice">All read</a>
            <div class="detailContent">
              <Card class="noDetail" v-if="this.noticeList.length<1">
                <h1>No Notice Notifications</h1>
              </Card>
              <template v-else-if="this.noticeList.length>0">
                <div class="noticeDetail" v-for="notice in noticeList" :key="notice.index">
                  <template v-if="notice.type =='work'">
                    <Card style="height:100%">
                      <template v-if="notice.state=='unread'">
                        <Badge dot>
                          <h4>{{notice.content.title}}</h4>
                        </Badge>
                        <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                      </template>
                      <h4 v-else>{{notice.content.title}}
                        <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                      </h4>
                      <small class="noticeDescription">{{notice.content.description}}</small>
                      <small>{{notice.createTime}}</small>
                      <Button class="noticeReadBtn" v-if="notice.state=='unread'" @click="gotoWork(notice.noticeId, notice.content.subProjectId)">Go</Button>
                    </Card>
                  </template>
                  <template v-else>
                    <Card style="height:100%">
                      <template v-if="notice.state=='unread'">
                        <Badge dot>
                          <h4>{{notice.content.title}}</h4>
                        </Badge>
                        <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                      </template>
                      <h4 v-else>{{notice.content.title}}
                        <span class="noticeDeleteBtn" @click="deleteNotice(notice)">×</span>
                      </h4>
                      <small class="noticeDescription">{{notice.content.description}}</small>
                      <small>{{notice.createTime}}</small>
                      <Button class="noticeReadBtn" v-if="notice.state=='unread'" @click="readNotice(notice.noticeId)">Got it</Button>
                    </Card>
                  </template>
                </div>
              </template>
            </div>
          </Card>
        </TabPane>
        <TabPane :label="replyTab" name="reply">
          <Card>
            <h3 slot="title">Reply Detail</h3>
            <div class="detailContent">
              <Card class="noDetail" v-if="this.replyList.length<1">
                <h1>No Reply Notifications</h1>
              </Card>
              <template v-else-if="this.replyList.length>0">
                <div class="noticeDetail" v-for="reply in this.replyList" :key="reply.index">
                  <Card style="height:100%">
                  Reply Datail
                  <!-- 此处添加回复类型通知的Card显示 -->
                </Card>
                </div>
              </template>
            </div>
          </Card>
        </TabPane>
        <TabPane :label="applyTab" name="apply">
          <Card>
            <h3 slot="title">Apply Detail</h3>
            <div class="detailContent">
              <Card class="noDetail" v-if="this.applyList.length<1">
                <h1>No Apply Notifications</h1>
              </Card>
              <template v-else-if="this.applyList.length>0">
                <div class="noticeDetail" v-for="apply in this.applyList" :key="apply.index">
                  <Card style="height:100%">
                    <template v-if="apply.state=='unread'">
                      <Badge dot>
                        <h4>{{apply.content.title}}</h4>
                      </Badge>
                      <span class="noticeDeleteBtn" @click="deleteNotice(apply)">×</span>
                    </template>
                    <h4 v-else>{{apply.content.title}}
                      <span class="noticeDeleteBtn" @click="deleteNotice(apply)">×</span>
                    </h4>
                    <small class="noticeDescription">{{apply.content.description}}</small>
                    <small>{{apply.createTime}}</small>
                    <template v-if="apply.content.approve=='unknow'">
                      <Button class="approveApplyBtn" v-if="apply.state=='unread'" @click="refuseApply(apply)">×</Button>
                      <Button class="refuseApplyBtn" v-if="apply.state=='unread'" @click="approveApply(apply)">√</Button>
                    </template>
                    <template v-else-if="apply.content.approve=='true'">
                      <Button disabled style="float:right;">√</Button>
                    </template>
                    <template v-else-if="apply.content.approve=='false'">
                      <Button disabled style="float:right;">×</Button>
                    </template>
                  </Card>
                </div>
              </template>
            </div>
          </Card>
        </TabPane>
      </Tabs>
    </Col>
  </Row>
</div>
</template>
<script>
export default {
  mounted() {
    this.loadNotifications();
  },
  data() {
    return {
      noticeUnreadCount: 0,
      replyUnreadCount: 0,
      applyUnreadCount: 0,
      noticeList: [],
      replyList: [],
      applyList: [],
      noticeTab: h => {
        return h("div", [
          h("span", "Notice"),
          h("Badge", {
            props: {
              count: this.noticeUnreadCount
            }
          })
        ]);
      },
      replyTab: h => {
        return h("div", [
          h("span", "Reply"),
          h("Badge", {
            props: {
              count: this.replyUnreadCount
            }
          })
        ]);
      },
      applyTab: h => {
        return h("div", [
          h("span", "Apply"),
          h("Badge", {
            props: {
              count: this.applyUnreadCount
            }
          })
        ]);
      }
    };
  },
  // add by mzy for navigation guards
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState) {
        next("/login");
      } else {
        next();
      }
    });
  },
  methods: {
    loadNotifications() {
      this.axios
        .get(
          "/PExploration/notice/inquiry?" +
            "key=recipientId" +
            "&value=" +
            this.$store.getters.userId
        )
        .then(res => {
          if (res.data !== "Fail") {
            let noticeListTest = [];
            let noticeUnreadCount = 0;
            let replyListTest = [];
            let replyUnreadCount = 0;
            let applyListTest = [];
            let applyUnreadCount = 0;
            let notifications = res.data;
            for (let i = 0; i < notifications.length; i++) {
              if (notifications[i].type === "notice"|| notifications[i].type === "work") {
                noticeListTest.push(notifications[i]);
                if (notifications[i].state === "unread") {
                  noticeUnreadCount++;
                }
              } else if (notifications[i].type === "reply") {
                replyListTest.push(notifications[i]);
                if (notifications[i].state === "unread") {
                  replyUnreadCount++;
                }
              } else if (notifications[i].type === "apply") {
                applyListTest.push(notifications[i]);
                if (notifications[i].state === "unread") {
                  applyUnreadCount++;
                }
              }
            }
            noticeListTest.reverse();
            replyListTest.reverse();
            applyListTest.reverse();
            this.$set(this, "noticeUnreadCount", noticeUnreadCount);
            this.$set(this, "noticeList", noticeListTest);
            this.$set(this, "replyUnreadCount", replyUnreadCount);
            this.$set(this, "replyList", replyListTest);
            this.$set(this, "applyUnreadCount", applyUnreadCount);
            this.$set(this, "applyList", applyListTest);
          }
          else{
            this.$Message.error("load notifications fail");
          }
        })
        .catch(err => {
          this.$Message.error("load notifications fail");
        });
    },
    deleteNotice(notice) {
      this.axios
        .get("/PExploration/notice/delete" + "?noticeId=" + notice.noticeId)
        .then(res => {
          if(res.data == "Offline"){
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          }
          else if (res.data == "Success") {
            this.$Message.success("delete notification success.");
            if(notice.state=="unread"){
              this.$emit("readNotification");
            }
            this.loadNotifications();
          } else {
            this.$Message.error("delete notification fail.");
          }
        })
        .catch(err => {
          this.$Message.error("delete notification fail.");
        });
    },
    readAllNotice(){
      var unreadList = this.noticeList.filter(function(notice){
        if(notice.state=='unread'){
          return notice;
        }
      });
      var unreadCount = unreadList.length;
      for(var i=0;i<unreadList.length;i++){
        this.axios
        .get("/PExploration/notice/read" + "?noticeId=" + unreadList[i].noticeId)
        .then(res => {
          if(res.data == "Offline"){
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          }
          else if (res.data == "Success") {
            this.$emit("readNotification");
            this.$store.commit("getUserInfo");
            if(--unreadCount==0){
              this.loadNotifications();
            }
          } else {
            this.$Message.error("update notification fail.");
          }
        })
        .catch(err => {
          this.$Message.error("update notification fail.");
        });
      }
    },
    readNotice(noticeId) {
      this.axios
        .get("/PExploration/notice/read" + "?noticeId=" + noticeId)
        .then(res => {
          if(res.data == "Offline"){
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          }
          else if (res.data == "Success") {
            this.$emit("readNotification");
            this.$store.commit("getUserInfo");
            this.loadNotifications();
          } else {
            this.$Message.error("update notification fail.");
          }
        })
        .catch(err => {
          this.$Message.error("update notification fail.");
        });
    },
    gotoWork(noticeId,subProjectId){
      //路由跳转好像和回调的关系有点问题，这样在回调之前就已经跳转了，回调的内容好像没啥用
      this.axios
        .get("/PExploration/notice/read" + "?noticeId=" + noticeId)
        .then(res => {
          if(res.data == "Offline"){
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          }
          else if (res.data == "Success") {
            this.$emit("readNotification");
            this.loadNotifications();
          } else {
            this.$Message.error("update notification fail.");
          }
        })
        .catch(err => {
          this.$Message.error("update notification fail.");
        });

      this.$router.push( `./project/${id}/subproject`);
    },
    refuseApply(apply) {
      let updateApply = new URLSearchParams();
      updateApply.append("noticeId", apply.noticeId);
      updateApply.append("content.approve", "false");
      updateApply.append("state", "read");
      this.axios
        .post("/PExploration/notice/update", updateApply)
        .then(res => {
          if(res.data == "Offline"){
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          }
          else if (res.data == "Success") {
            this.$emit("readNotification");
            this.loadNotifications();
            let replyNotice = {};
            replyNotice["recipientId"] = apply.content.userId;
            replyNotice["type"] = "notice";
            replyNotice["content"] = {
              title: "Result for application",
              description:
                "Sorry, you were refused to join the project: " +
                apply.content.projectTitle +
                " ."
            };
            this.axios
              .post("/PExploration/notice/save", replyNotice)
              .then(result => {
                if (result.data == "Success") {
                  this.$emit("sendNotice", apply.content.userId);
                } else {
                  this.$Message.error("reply fail.");
                }
              })
              .catch(err => {
                this.$Message.error("reply fail.");
              });
          } else {
            this.$Message.error("update notification fail.");
          };
        })
        .catch(err => {
          this.$Message.error("update notification fail.");
        });
    },
    approveApply(apply) {
      let updateApply = new URLSearchParams();
      updateApply.append("noticeId", apply.noticeId);
      updateApply.append("content.approve", "true");
      updateApply.append("state", "read");
      this.axios
        .post("/PExploration/notice/update", updateApply)
        .then(res => {
          if(res.data == "Offline"){
            this.$store.commit("userLogout");
            this.$router.push({ name: "Login" });
          }
          else if (res.data == "Success") {
            this.$emit("readNotification");
            this.loadNotifications();
            //update project members
            this.axios
              .get(
                "/PExploration/"+apply.content.scope+"/join?" +
                  apply.content.scope+"Id=" +
                  apply.content.projectId +
                  "&userId=" +
                  apply.content.userId
              )
              .then(res => {
                if (res.data === "Fail") {
                  this.$Message.error("Join fail.");
                } else if (res.data === "Exist") {
                  this.$Message.info(
                    "He/she is already a member of the "+ apply.content.scope +"."
                  );
                }
              })
              .catch(err => {
                this.$Message.error("Join fail");
              });
            //reply to applicant
            let replyNotice = {};
            replyNotice["recipientId"] = apply.content.userId;
            replyNotice["type"] = "notice";
            replyNotice["content"] = {
              title: "Result for application",
              description:
                "Congratulations for joining the "+apply.content.scope+": " +
                apply.content.projectTitle +
                " ."
            };
            this.axios
              .post("/PExploration/notice/save", replyNotice)
              .then(result => {
                if (result.data == "Success") {
                  this.$emit("sendNotice", apply.content.userId);
                  let resultEmailBody = {};
                  resultEmailBody["recipient"] = apply.content.userEmail;
                  resultEmailBody["mailTitle"] = "Join project result";
                  resultEmailBody["mailContent"] = "Hello, "+ apply.content.userName + ", Congratulations for joining the "+apply.content.scope+": " + apply.content.projectTitle + " .";
                  this.axios.post("/PExploration/email/send", resultEmailBody)
                  .then(res=>{
                    if (res.data == "Success") {
                        this.$Notice.success({
                          title: "Result for application",
                          desc:
                            "The process result email has been sent,if he/she doesn't online,the email will remind the joiner in time."
                        });
                      } else {
                        this.$Notice.error({
                          title: "Email send fail",
                          desc: "The invitation isn't be sent successfully."
                        });
                      }
                  })
                  .catch(err=>{
                    console.log(err.data);
                  })
                } else {
                  this.$Message.error("reply fail.");
                }
              })
              .catch(err => {
                this.$Message.error("reply fail.");
              });

              // 发送审核通过的邮件
              // let resultEmailBody = {};
              // resultEmailBody["recipient"] =



          } else {
            this.$Message.error("update notification fail.");
          }
        })
        .catch(err => {
          this.$Message.error("update notification fail.");
        });
    }
  }
};
</script>
