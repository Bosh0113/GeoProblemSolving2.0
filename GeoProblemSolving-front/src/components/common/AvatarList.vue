<template>
  <div class="avatarList">
    <Avatar class="avatar" v-for="(item,index) in avatarList" :src="item.avatar" :key="index" :title="item.name"/>
  </div>
</template>

<script>
  import Avatar from "vue-avatar";

  export default {
    name: "AvatarList",
    props: {
      list: Array
    },
    watch: {
      list: {
        deep: true,
        handler(val, oldVal){
          let newVal = JSON.parse(JSON.stringify(val));
          this.avatarList = newVal;
          for (let i = 0; i < newVal.length; i++) {
            if (val[i].avatar == undefined || val[i].avatar == ''){
              continue;
            }
            let newAvatar = this.$store.getters.userServer + val[i].avatar;
            this.$set(this.avatarList[i], "avatar", newAvatar);
        }
        }
      }
    },
    data() {
      return {
        avatarList: []
      }
    },
    mounted() {
      this.avatarList = this.list;
    }
  }
</script>

<style scoped>
  .avatarList {
    display: inline-block;
  }
  .avatar {
    margin-right: -8px;
    border: 1px solid white;
  }

</style>
