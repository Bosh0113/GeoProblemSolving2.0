<template>
  <div id="app">
    <router-view/>
    <BackTop></BackTop>
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
