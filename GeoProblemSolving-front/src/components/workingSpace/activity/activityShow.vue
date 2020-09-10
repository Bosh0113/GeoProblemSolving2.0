<template>
  <div style="padding: 5px 10px">
    <div style="height: calc(100vh - 110px); width:70%; float:left; padding: 20px;">
      <div>
        <div>
          <h2>{{activityInfo.name}}</h2>
        </div>
        <div style="heigth:50%; width:60%; float:left">
          <div style=" margin: 10px 0;">
            <Label>Purpose:</Label>
            <span style="margin-left: 10px">{{activityInfo.purpose}}</span>
          </div>
          <div style=" margin: 10px 0;">
            <Label>Creator:</Label>
            <span style="margin-left: 10px; cursor: pointer; color:#2d8cf0">{{creatorInfo.name}}</span>
          </div>
          <div style=" margin: 10px 0;">
            <Label>Permission:</Label>
            <span style="margin-left: 10px; cursor: pointer; color:#2d8cf0">
              <Icon type="ios-create" />
            </span>
          </div>
          <div style=" margin: 10px 0; margin-bottom: 30px">
            <Label>Created time:</Label>
            <span style="margin-left: 10px">{{activityInfo.createdTime}}</span>
          </div>
        </div>
        <div style="heigth:50%; width:40%; float:left">
          <div style=" margin: 10px 0;">
            <Label>Total resources:</Label>
            <span style="margin-left: 10px; cursor: pointer; color:#2d8cf0">1000</span>
          </div>
          <div style=" margin: 10px 0;">
            <Label>Total tasks:</Label>
            <span style="margin-left: 10px; cursor: pointer; color:#2d8cf0">100</span>
          </div>
          <div style=" margin: 10px 0;">
            <Label>Total child activities:</Label>
            <span
              style="margin-left: 10px; cursor: pointer; color:#2d8cf0"
            >{{activityInfo.children.length}}</span>
          </div>
          <div style=" margin: 10px 0; margin-bottom: 30px">
            <Label>Total participants:</Label>
            <span
              style="margin-left: 10px; cursor: pointer; color:#2d8cf0"
            >{{activityInfo.members.length}}</span>
          </div>
        </div>
      </div>
      <Divider></Divider>
      <div>
        <h3>Description</h3>
        <div
          style="height: calc(100vh - 400px); overflow-y: auto; padding: 10px 5px; word-break: break-word;"
        >{{activityInfo.description}}</div>
      </div>
    </div>
    <div
      style="width:30%; float:left; padding: 0 20px; margin: 20px 0; border-left: 1px solid lightgray; "
    >
      <div>
        <span style="font-size: 1.17em; font-weight: bold;">Participants</span>
        <Icon
          type="md-person-add"
          size="16"
          title="Invite users"
          style="float: right; margin: 5px 20px 0 0; cursor:pointer; color:#2d8cf0"
        />
      </div>
      <vue-scroll :ops="scrollOps" style="margin-top: 10px; height:calc(100vh - 190px)">
        <Card
          v-for="(member, index) in participants"
          :key="'online' + index"
          style="margin:5px 0"
          :padding="5"
        >
          <div
            style="display:flex;align-items:center;cursor:pointer"
            @click="gotoPersonalSpace(member.userId)"
          >
            <div class="memberImg" style="position:relative">
              <img
                v-if="member.avatar != '' && member.avatar!='undefined'"
                :src="member.avatar"
                style="width:40px;height:40px"
              />
              <avatar v-else :username="member.userName" :size="40" :rounded="false"></avatar>
              <div class="onlinecircle"></div>
            </div>
            <div class="memberDetail">
              <div class="memberName">
                <span>{{member.name}}</span>
              </div>
              <div class="memberRole">
                <span>{{member.role}}</span>
              </div>
            </div>
          </div>
        </Card>
      </vue-scroll>
    </div>
  </div>
</template>
<script>
export default {
    props: ["activityInfo"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
      creatorInfo: { name: "XXX", email: "XXX@XX.com" },
      participants: [
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
        { name: "AAA", role: "manager" },
      ],
    };
  },
  methods: {
    getParticipants() {},
    getAllTasks() {},
    getAllResource() {},
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
  },
};
</script>
<style scoped>
.memberImg {
  width: 40px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.memberName {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberDetail {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.memberRole {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberName span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.memberRole span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 50%;
}
</style>