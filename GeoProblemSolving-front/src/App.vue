<template>
  <div id="app" style="">
     
            
    <router-view />
  </div>
</template>
<style scoped>
</style>
<script>
export default {
  name: "App",
  created() {
    this.$store.commit("getUserInfo");
  }
};
</script>
