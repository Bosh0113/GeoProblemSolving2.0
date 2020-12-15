<template>
  <div>
    <Row>
      <Card>
        <CheckboxGroup v-model="selectedNoticeType" style="display: inline-block">
          <Checkbox label="noticeList">
            <span>Notice</span>
            <span class="badge">{{noticeList.length}}</span>
          </Checkbox>
          <Checkbox label="replyList">
            <span>Reply</span>
            <span class="badge">{{replyList.length}}</span>
          </Checkbox>
          <Checkbox label="applyList">
            <span>Apply</span>
            <span class="badge">{{applyList.length}}</span>
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
        <Card>
          <h3 slot="title">Notification Detail</h3>
          <a slot="extra">All Read</a>
          <div v-if="selectNoteNum != 0">
            <div
              v-for="(item, index) in readNoteList"
              :key="index"
            >
              <Card></Card>

            </div>
          </div>
          <div v-else>
            <Card>
              <h1 style="color: darkgray; text-align: center;">No Notifications</h1>
            </Card>

          </div>
        </Card>
      </div>

    </Row>
  </div>
</template>

<script>
  export default {
    name: "notification",
    data() {
      return {
        noticeList: [],
        replyList: [],
        applyList: [],
        noticeUnreadCount: 0,
        replyUnreadCount: 0,
        applyUnreadCount: 0,
        readOrUnread: ["unRead", "read"],
        selectedNoticeType: ["noticeList", "replyList", "applyList"]
      }
    },
    mounted() {
      this.loadNotifications();
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
          readNum += this.noticeList.length - this.noticeUnreadCount;
        }
        if (this.selectedNoticeType.includes("replyList")) {
          readNum += this.replyList.length - this.replyUnreadCount;
        }
        if (this.selectedNoticeType.includes("applyList")) {
          readNum += this.applyList.length - this.applyUnreadCount;
        }
        return readNum;
      },
      selectNoteNum: function () {
        return  this.readCount + this.unReadCount;
      },
      readNoteList: function () {

      },
      unReadNoteList: function () {
      }
    },
    methods: {
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
              let noticeListTest = [];
              let replyListTest = [];
              let applyListTest = [];
              let noticeUnreadCount = 0;
              let replyUnreadCount = 0;
              let applyUnreadCount = 0;
              for (let i = 0; i < notifications.length; i++) {
                //  先判断是否为已读
                if (notifications[i].type == "notice" || notifications[i].type === "work") {
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
              //使用set方式就不必担心值可能出现的累加等问题
              this.$set(this, "noticeUnreadCount", noticeUnreadCount);
              this.$set(this, "replyUnreadCount", replyUnreadCount);
              this.$set(this, "applyUnreadCount", applyUnreadCount);
              this.$set(this, "noticeList", noticeListTest);
              this.$set(this, "replyList", replyListTest);
              this.$set(this, "applyList", applyListTest);
            } else {
              this.$Message.error("load notifications fail");
            }
          }).catch(err => {
          this.$Message.error("load notifications fail");
        })
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
</style>
