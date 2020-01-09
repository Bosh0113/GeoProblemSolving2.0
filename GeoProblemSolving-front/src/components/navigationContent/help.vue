<style scoped>
img {
  width: 100%;
  height: auto;
}
</style>
<template>
  <div style="padding:40px">
    <Card>
      <div style="text-align:center">
        <h1>Get started quickly</h1>
      </div>
    </Card>
    <Card v-for="(img,index) in imageList" :key="img.index" style="margin: 5px 0">
      <h2 slot="title">{{titleList[index]}}</h2>
      <div>
        <img :src="img">
      </div>
    </Card>
  </div>
</template>
<script>
export default {
  created() {
    this.getHelpImg();
  },
  data() {
    return {
      imageList: [],
      titleList: [
        "Home page",
        "Projects page",
        "Project information",
        "Personal page",
        "Personal projects",
        "Notifications center",
        "Project detail page",
        "Project's resources",
        "Project's subprojects",
        "Subproject's detail page",
        "Subproject's tasks"
      ]
    };
  },
  methods: {
    getHelpImg() {
      for (var i = 1; i < 12; i++) {
        var imgUrl = require("@/assets/images/Help/" + "help" + i + ".png");
        this.imageList.push(imgUrl);
      }
    }
  }
};
</script>
