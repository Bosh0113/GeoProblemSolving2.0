<template>
  <div id="app" style="min-width:1200px">
     
            
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
