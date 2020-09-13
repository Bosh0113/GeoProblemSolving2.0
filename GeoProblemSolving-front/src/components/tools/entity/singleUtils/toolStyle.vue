<template>
  <div class="sidebar" :style="{height:bodyHeight}" style="width:60px;float:left">    
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
export default {
  props: {
    resources: Array
  },
  components: {
  },
  data() {
    return {
      bodyHeight: window.innerHeight + "px",
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
