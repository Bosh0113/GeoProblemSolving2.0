<style scoped>
/* member部分样式 */
.memberPanel {
  width: 200px;
  /* background-color: rgba(0,0,0,0.1); */
}

.participants {
  height: auto;
  margin-top: 10px;
}

.participants h4 {
  padding: 10px;
  font-size: 20px;
  line-height: 20px;
  text-align: center;
  color: #515a6e;
}

.name {
  height: 28px;
  margin-top: 2px;
  line-height: 28px;
  padding-left: 5%;
  padding-right: 5%;
  margin-bottom: 2px;
  width: 100%;
  font-size: 18px;
  font-family: Helvetica;
}

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
  /* display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap; */
}

.memberDetail {
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.memberOrganization {
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

.memberOrganization span {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 50%;
}
</style>
<template>
  <div>
    <div class="memberPanel">
      <!-- 参与者 -->
      <div class="participants">
        <div>
          <Card
            v-for="(participant,index) in onlineParticipants"
            :key="'online' +index"
            style="margin:2.5%"
            :padding="5"
          >
            <div style="display:flex;align-items:center;cursor:pointer" @click="gotoPersonalSpace(participant.userId)">
              <div class="memberImg" style="position:relative">
                <img
                  v-if="participant.avatar != '' && participant.avatar!='undefined'"
                  :src="participant.avatar"
                  style="width:40px;height:40px"
                />
                <avatar v-else :username="participant.userName" :size="40" :rounded="false"></avatar>
                <div class="onlinecircle"></div>
              </div>
              <div class="memberDetail">
                <div class="memberName">
                  <span>{{participant.userName}}</span>
                </div>
                <div class="memberOrganization">
                  <span>{{participant.organization}}</span>
                </div>
              </div>
            </div>
          </Card>
        </div>
        <div>
          <Card
            v-for="(participant,index) in offlineParticipants"
            :key=" 'offline' + index"
            style="margin:2.5%"
            :padding="5"
          >
            <div style="display:flex;align-items:center;cursor:pointer" @click="gotoPersonalSpace(participant.userId)">
              <div class="memberImg" style="position:relative">
                <img
                  v-if="participant.avatar != '' && participant.avatar!='undefined'"
                  :src="participant.avatar"
                  style="width:40px;height:40px"
                />
                <avatar v-else :username="participant.userName" :size="40" :rounded="false"></avatar>
                <div class="offlinecircle"></div>
              </div>
              <div class="memberDetail">
                <div class="memberName">
                  <span>{{participant.userName}}</span>
                </div>
                <div class="memberOrganization">
                  <span>{{participant.organization}}</span>
                </div>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import Avatar from "vue-avatar";

export default {
  components: {
    Avatar
  },
  props: ["onlineParticipants", "offlineParticipants"],

  data() {
    return {
    };
  },
  mounted() {
  },
  beforeDestroy() {
  },
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
        vm.$router.push({
          name: "Login"
        });
      } else {
      }
    });
  },
  updated: function() {},
  methods: {    
    gotoPersonalSpace(id) {
      if (id == this.$store.getters.userId) {
        this.$router.push({ name: "PersonalPage" });
      } else {
        this.$router.push({ name: "MemberDetailPage", params: { id: id } });
      }
    },
  }
};
</script>
