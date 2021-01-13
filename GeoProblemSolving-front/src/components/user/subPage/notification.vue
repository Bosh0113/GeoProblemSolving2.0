<template>
  <div>
    <Row>
      <Col span="20" >
        <Card dis-hover>
          <CheckboxGroup v-model="selectedNoticeType" style="display: inline-block">
            <Checkbox label="noticeList">
              <span>Notice</span>
              <span class="badge">{{readNoticeList.length + unreadNoticeList.length}}</span>
            </Checkbox>
            <Checkbox label="replyList">
              <span>Reply</span>
              <span class="badge">{{readReplyList.length + unreadReplyList.length}}</span>
            </Checkbox>
            <Checkbox label="applyList">
              <span>Apply</span>
              <span class="badge">{{readApplyList.length + unreadApplyList.length}}</span>
            </Checkbox>
          </CheckboxGroup>
          <span style="margin: 0 20px"></span>
          <CheckboxGroup v-model="readOrUnread" style="display: inline-block">
            <Checkbox label="read">
              <span>Read</span>
              <span class="badge">{{readCount}}</span>
            </Checkbox>
            <Checkbox label="unRead">
              <span>Unread</span>
              <span class="badge">{{unReadCount}}</span>
            </Checkbox>

          </CheckboxGroup>
        </Card>
        <!--  通知详情 -->
        <div>
          <Card dis-hover>
            <h3 slot="title">Notification Detail</h3>
            <div :style="{height: contentHeight-230+'px'}">
              <vue-scroll :ops="ops">
                <div v-if="selectNoteNum!=0">
                  <!--
                  未读消息(分三种 notice, reply, apply)
                  notice: 只有已读按钮
                  reply: 只有回复按钮
                  apply: 有同意与拒绝按钮
                     -->
                  <div
                    v-if="readOrUnread.includes('unRead')"
                    v-for="(item, unreadIndex) in unReadNoteList"
                    :key="unreadIndex"
                  >
                    <!--                未读apply    -->
                    <div
                      v-if="item.type=='apply'"
                    >
                      <Card>
                        <Badge dot>
                          <h4 style="font-size: 15px">{{item.content.title}}</h4>
                        </Badge>
                        <br/>
                        <p style="font-weight: 400; font-size: 17px">{{item.content.description}}</p>
                        <small style="font-size: 13px">{{item.createTime}}</small>
                        <Icon type="md-close" class="applyBtn" style="color: #ff9900" @click="refuseApply(item)"/>
                        <Icon type="md-checkmark" class="applyBtn" style="color: #47cb89" @click="approveApply(item)"/>
                      </Card>
                    </div>
                    <!--                未读 Notice    -->
                    <div
                      v-if="item.type=='notice'"
                    >
                      <Card>
                        <Badge dot>
                          <h4 style="font-size: 15px">{{item.content.title}}</h4>
                        </Badge>
                        <br/>
                        <p style="font-weight: 400; font-size: 17px">{{item.content.description}}</p>
                        <small style="font-size: 13px">{{item.createTime}}</small>
                        <Icon type="md-checkmark" class="applyBtn" style="color: #47cb89" @click="readNotice(item.noticeId)"/>
                      </Card>
                    </div>
                    <!--               未读 Reply  -->
                    <div
                      v-if="item.type=='reply'"
                    >
                      <Card>
                        <Badge dot>
                          <h4 style="font-size: 15px">{{item.content.title}}</h4>
                        </Badge>
                        <br/>
                        <p style="font-weight: 400; font-size: 17px">{{item.content.description}}</p>
                        <small style="font-size: 13px">{{item.createTime}}</small>
                        <Icon type="md-checkmark" class="applyBtn" style="color: #47cb89"/>
                      </Card>
                    </div>
                  </div>

                  <!--            已读消息不区分具体类型   仅有删除按钮    -->
                  <div
                    v-if="readOrUnread.includes('read')"
                    v-for="(item, readIndex) in readNoteList"
                    :key="readIndex"
                  >
                    <Card>
                      <!--                  <h4 style="font-size: 13px">{{item.content.title}}</h4>-->
                      <p slot="title">{{item.content.title}}</p>
                      <p style="font-weight: 400; font-size: 15px">{{item.content.description}}</p>
                      <small style="font-size: 11px">{{item.createTime}}</small>
                      <Icon slot="extra" type="md-close" class="applyBtn" style="color: #ed4014" @click="deleteNotice(item)" />
                    </Card>
                  </div>
                </div>

                <!--          无通知消息  -->
                <div v-else>
                  <Card>
                    <h1 style="color: darkgray; text-align: center;">No Notifications</h1>
                  </Card>

                </div>
              </vue-scroll>
            </div>


          </Card>
        </div>

      </Col>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "notification",
    data() {
      return {
        unreadNoticeList: [],
        unreadReplyList: [],
        unreadApplyList: [],
        readNoticeList: [],
        readReplyList: [],
        readApplyList: [],
        noticeUnreadCount: 0,
        replyUnreadCount: 0,
        applyUnreadCount: 0,
        ops: {
          bar: {
            background: "#808695"
          }
        },
        contentHeight: "",
        readOrUnread: ["unRead"],
        selectedNoticeType: ["noticeList", "replyList", "applyList"]
      }
    },
    created() {
    },
    mounted() {
      this.loadNotifications();
      this.resizeContent();
    },
    computed: {
      unReadCount: function () {
        let unReadNum = 0;
        if (this.selectedNoticeType.includes("noticeList")) {
          unReadNum += this.noticeUnreadCount;
        }
        if (this.selectedNoticeType.includes("replyList")) {
          unReadNum += this.replyUnreadCount;
        }
        if (this.selectedNoticeType.includes("applyList")) {
          unReadNum += this.applyUnreadCount;
        }
        return unReadNum;
      },
      readCount: function () {
        let readNum = 0;
        if (this.selectedNoticeType.includes("noticeList")) {
          readNum += this.readNoticeList.length;
        }
        if (this.selectedNoticeType.includes("replyList")) {
          readNum += this.readReplyList.length;
        }
        if (this.selectedNoticeType.includes("applyList")) {
          readNum += this.readApplyList.length;
        }
        return readNum;
      },
      selectNoteNum: function () {
        let selectedNoteNum = 0;
        if (this.readOrUnread.includes("read")) {
          selectedNoteNum += this.readCount;
        }
        if (this.readOrUnread.includes("unRead")) {
          selectedNoteNum += this.unReadCount;
        }
        return selectedNoteNum;
      },
      readNoteList: function () {
        let selectedType = this.selectedNoticeType;
        let readList = [];
        if (selectedType.includes("applyList")) {
          readList = readList.concat(this.readApplyList);
        }
        if (selectedType.includes("replyList")) {
          readList = readList.concat(this.readReplyList);
        }
        if (selectedType.includes("noticeList")) {
          readList = readList.concat(this.readNoticeList);
        }
        return readList;
      },
      unReadNoteList: function () {
        let selectedType = this.selectedNoticeType;
        let unreadList = [];
        if (selectedType.includes("applyList")) {
          unreadList = unreadList.concat(this.unreadApplyList);
        }
        if (selectedType.includes("replyList")) {
          unreadList = unreadList.concat(this.unreadReplyList);
        }
        if (selectedType.includes("noticeList")) {
          unreadList = unreadList.concat(this.unreadNoticeList);
        }
        return unreadList;
      }
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
      loadNotifications() {
        this.axios
          .get(
            "/GeoProblemSolving/notice/inquiry?" +
            "key=recipientId" +
            "&value=" +
            this.$store.getters.userId
          )
          .then(res => {
            if (res.data !== "Fail") {
              let notifications = res.data;
              let unreadNoticeListTemp = [];
              let unreadReplyListTemp = [];
              let unreadApplyListTemp = [];
              let readNoticeListTemp = [];
              let readReplyListTemp = [];
              let readApplyListTemp = [];
              let noticeUnreadCount = 0;
              let replyUnreadCount = 0;
              let applyUnreadCount = 0;
              for (let i = 0; i < notifications.length; i++) {
                //  先判断是否为已读
                if (notifications[i].type == "notice" || notifications[i].type === "work") {
                  // noticeListTest.push(notifications[i]);
                  if (notifications[i].state === "unread") {
                    unreadNoticeListTemp.push(notifications[i]);
                    noticeUnreadCount++;
                  } else {
                    readNoticeListTemp.push(notifications[i]);
                  }
                } else if (notifications[i].type === "reply") {
                  // replyListTest.push(notifications[i]);
                  if (notifications[i].state === "unread") {
                    unreadReplyListTemp.push(notifications[i]);
                    replyUnreadCount++;
                  } else {
                    readReplyListTemp.push(notifications[i]);
                  }
                } else if (notifications[i].type === "apply") {
                  // applyListTest.push(notifications[i]);
                  if (notifications[i].state === "unread") {
                    unreadApplyListTemp.push(notifications[i]);
                    applyUnreadCount++;
                  } else {
                    readApplyListTemp.push(notifications[i]);
                  }
                }
              }
              //使用set方式就不必担心数组push带来的累加问题
              this.$set(this, "readApplyList", readApplyListTemp);
              this.$set(this, "readReplyList", readReplyListTemp);
              this.$set(this, "readNoticeList", readNoticeListTemp);
              this.$set(this, "unreadApplyList", unreadApplyListTemp);
              this.$set(this, "unreadNoticeList", unreadNoticeListTemp);
              this.$set(this, "unreadReplyList", unreadReplyListTemp);
              this.$set(this, "noticeUnreadCount", noticeUnreadCount);
              this.$set(this, "replyUnreadCount", replyUnreadCount);
              this.$set(this, "applyUnreadCount", applyUnreadCount);
            } else {
              this.$Message.error("load notifications fail");
            }
          }).catch(err => {
          this.$Message.error("load notifications fail");
        })
      },
      deleteNotice(notice) {
        this.axios
          .get(
            "/GeoProblemSolving/notice/delete" + "?noticeId=" + notice.noticeId
          )
          .then((res) => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data == "Success") {
              this.$Message.success("delete notification success.");
              if (notice.state == "unread") {
                this.$emit("readNotification");
              }
              this.loadNotifications();
            } else {
              this.$Message.error("delete notification fail.");
            }
          })
          .catch((err) => {
            this.$Message.error("delete notification fail.");
          });
      },
      readAllNotice() {
        var unreadList = this.noticeList.filter(function (notice) {
          if (notice.state == "unread") {
            return notice;
          }
        });
        var unreadCount = unreadList.length;
        for (var i = 0; i < unreadList.length; i++) {
          this.axios
            .get(
              "/GeoProblemSolving/notice/read" +
              "?noticeId=" +
              unreadList[i].noticeId
            )
            .then((res) => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data == "Success") {
                this.$emit("readNotification");
                this.$store.commit("getUserInfo");
                if (--unreadCount == 0) {
                  this.loadNotifications();
                }
              } else {
                this.$Message.error("update notification fail.");
              }
            })
            .catch((err) => {
              this.$Message.error("update notification fail.");
            });
        }
      },
      readNotice(noticeId) {
        this.axios
          .get("/GeoProblemSolving/notice/read" + "?noticeId=" + noticeId)
          .then((res) => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data == "Success") {
              this.$emit("readNotification");
              this.$store.commit("getUserInfo");
              this.loadNotifications();
            } else {
              this.$Message.error("update notification fail.");
            }
          })
          .catch((err) => {
            this.$Message.error("update notification fail.");
          });
      },
      gotoWork(noticeId, subProjectId) {
        //路由跳转好像和回调的关系有点问题，这样在回调之前就已经跳转了，回调的内容好像没啥用
        this.axios
          .get("/GeoProblemSolving/notice/read" + "?noticeId=" + noticeId)
          .then((res) => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data == "Success") {
              this.$emit("readNotification");
              this.loadNotifications();
            } else {
              this.$Message.error("update notification fail.");
            }
          })
          .catch((err) => {
            this.$Message.error("update notification fail.");
          });

        this.$router.push(`./project/${id}/subproject`);
      },
      refuseApply(apply) {
        let updateApply = new URLSearchParams();
        updateApply.append("noticeId", apply.noticeId);
        updateApply.append("content.approve", "false");
        updateApply.append("state", "read");
        this.axios
          .post("/GeoProblemSolving/notice/update", updateApply)
          .then((res) => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data == "Success") {
              this.$emit("readNotification");
              this.loadNotifications();
              let replyNotice = {};
              replyNotice["recipientId"] = apply.content.userId;
              replyNotice["type"] = "notice";
              replyNotice["content"] = {
                title: "Result for application",
                description:
                  "Sorry, you were refused to join the activity: " +
                  apply.content.activityName +
                  " .",
              };
              this.axios
                .post("/GeoProblemSolving/notice/save", replyNotice)
                .then((result) => {
                  if (result.data == "Success") {
                    this.$emit("sendNotice", apply.content.userId);
                  } else {
                    this.$Message.error("reply fail.");
                  }
                })
                .catch((err) => {
                  this.$Message.error("reply fail.");
                });
            } else {
              this.$Message.error("update notification fail.");
            }
          })
          .catch((err) => {
            this.$Message.error("update notification fail.");
          });
      },
      approveApply(apply) {
        let updateApply = new URLSearchParams();
        updateApply.append("noticeId", apply.noticeId);
        updateApply.append("content.approve", "true");
        updateApply.append("state", "read");
        // 更新通知
        this.axios
          .post("/GeoProblemSolving/notice/update", updateApply)
          .then((res) => {
            if (res.data == "Offline") {
              this.$store.commit("userLogout");
              this.$router.push({ name: "Login" });
            } else if (res.data == "Success") {
              this.$emit("readNotification");
              this.loadNotifications();

              //update project members
              this.joinActivity(apply);

              //reply to applicant
              let replyNotice = {};
              replyNotice["recipientId"] = apply.content.userId;
              replyNotice["type"] = "notice";
              replyNotice["content"] = {
                title: "Result for application",
                description:
                  "Congratulations for joining the activity: " +
                  apply.content.activityName +
                  " .",
              };
              this.axios
                .post("/GeoProblemSolving/notice/save", replyNotice)
                .then((result) => {
                  if (result.data == "Success") {
                    this.$emit("sendNotice", apply.content.userId);
                    let resultEmailBody = {};
                    resultEmailBody["recipient"] = apply.content.userEmail;
                    resultEmailBody["mailTitle"] = "Join project result";
                    resultEmailBody["mailContent"] =
                      "Hello, " +
                      apply.content.userName +
                      ", Congratulations for joining the activity: " +
                      apply.content.activityName +
                      " .";
                    this.axios
                      .post("/GeoProblemSolving/email/send", resultEmailBody)
                      .then((res) => {
                        if (res.data == "Success") {
                          this.$Notice.success({
                            title: "Result for application",
                            desc:
                              "The process result email has been sent,if he/she doesn't online,the email will remind the joiner in time.",
                          });
                        } else {
                          this.$Notice.error({
                            title: "Email send fail",
                            desc: "The invitation isn't be sent successfully.",
                          });
                        }
                      })
                      .catch((err) => {
                        console.log(err.data);
                      });
                  } else {
                    this.$Message.error("reply fail.");
                  }
                })
                .catch((err) => {
                  this.$Message.error("reply fail.");
                });

              // 发送审核通过的邮件
              // let resultEmailBody = {};
              // resultEmailBody["recipient"] =
            } else {
              this.$Message.error("update notification fail.");
            }
          })
          .catch((err) => {
            this.$Message.error("update notification fail.");
          });
      },
      joinActivity(apply) {
        let url = "";
        if (apply.content.activityLevel == 0) {
          url =
            "/GeoProblemSolving/project/" +
            apply.content.activityId +
            "/user?userId=" +
            apply.content.userId;
        } else if (apply.content.activityLevel == 1) {
          url =
            "/GeoProblemSolving/subproject/" +
            apply.content.activityId +
            "/user?userId=" +
            apply.content.userId;
        } else if (apply.content.activityLevel > 1) {
          url =
            "/GeoProblemSolving/activity/" +
            apply.content.activityId +
            "/user?userId=" +
            apply.content.userId;
        } else {
          return;
        }

        this.axios
          .post(url)
          .then((res) => {
            if (res.data.code == 0) {
              this.$Message.info("Successfully approval.");
            } else if (res.data.code == -3) {
              this.$Message.info(
                "The applicant has already been a member of the activity: " +
                apply.content.activityName +
                " ."
              );
            } else {
              console.log(res.data.msg);
            }
          })
          .catch((err) => {
            throw err;
          });
      },
    }
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

  .noticeDeleteBtn {
    float: right;
    color: #ff7800;
    cursor: pointer;
  }

  .detailContent {
    padding: 15px 10px;
    height: 500px;
    overflow-y: auto;
    background-color: #f7f7f7;
  }

  .applyBtn {
    float: right;
    margin: 0 10px;
    cursor: pointer;
  }
</style>
