<template>
  <div class="sidebar" :style="{height:bodyHeight}" style="width:60px;float:left">
    <div
      id="toolMembers"
      style="display:flex;justify-content:center;margin-top:20px"
      title="Online participants"
    >
      <span @click="membersDrawer=true" style="cursor:pointer">
        <Icon type="ios-contacts" :size="40" color="white"/>
      </span>
      <Drawer
        title="Online participants"
        :closable="false"
        v-model="membersDrawer"
        placement="left"
      >
        <Card
          v-for="(participant,index) in participants"
          :key="index"
          style="cursor:pointer"
          :padding="5"
          class="memberPanel"
        >
          <div style="display:flex">
            <div class="memberImg">
              <img
                v-if="participant.avatar != '' && participant.avatar!='undefined' && participant.avatar!='null'"
                :src="member.avatar"
              >
              <avatar
                :username="participant.userName"
                :size="40"
                :title="participant.userName"
                v-else
              ></avatar>
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
      </Drawer>
    </div>
    <div
      id="toolResources"
      style="display:flex;justify-content:center;margin-top:20px"
      title="Resources"
    >
      <span @click="resourceDrawer=true" style="cursor:pointer">
        <Icon type="md-folder" :size="40" color="white"/>
      </span>
      <Drawer title="Resources" :closable="false" v-model="resourceDrawer" placement="left">
        <Card
          v-for="(resource,index) in resources"
          :key="index"
          style="cursor:pointer"
          :padding="5"
        >
          <div class="resourcePanel" :title="resource.description" @click="selectResource(resource.pathURL)"><span>{{resource.name}}</span></div>
        </Card>
      </Drawer>
    </div>
  </div>
</template>
<style scoped>
.sidebar {
  width: 60px;
  background-color: #515a6e;
  height: 100%;
  justify-content: center;
  position: absolute;
}
.toolContent {
  flex: 1;
  height: 100%;
}
.memberPanel {
  display: flex;
  height: 50px;
  margin-bottom: 5px;
}
.memberPanel:last-child {
  margin-bottom: 0px;
}
.memberImg {
  width: 40px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.memberDetail {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.memberName {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberOrganization {
  height: 20px;
  padding: 0px 10px;
  width: 100%;
}
.memberName span{
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
  max-width:100%;
}
.resourcePanel {
  height: 40px;
  display: flex;
  align-items: center;
  width: 100%;
  height: 100%;
}
.resourcePanel span{
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width:80%;
}
</style>
<script>
import Avatar from "vue-avatar";
export default {
  props: {
    participants: Array,
    resources: Array
  },
  components: {
    Avatar
  },
  data() {
    return {
      bodyHeight: window.innerHeight + "px",
      membersDrawer: false,
      resourceDrawer: false
    };
  },
  mounted() {
  },
  beforeDestroy: function() {
  },
  methods:{
    selectResource(url){
      this.$emit("resourceUrl",url);
    }
  }
};
</script>
