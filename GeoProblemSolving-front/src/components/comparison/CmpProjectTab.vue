<template>
  <Row>
    <Col :xs="{ span: 21, offset: 1 }" :md="{ span: 11, offset: 1 }" :lg="{ span: 6 }" v-for="project of projectShowList" :key="project.title">
      <Card style="height:auto;margin:10px -15px">
        <a
          href="#"
          class="cmpTitle"
        >{{project.title}}</a>
        <p class="cmpDesc">{{project.description}}</p>
      </Card>
    </Col>
  </Row>
</template>

<script>
export default {
  name:'cmp-project-tab',
  props:{
    projectShowList:{
      type:Array
    },
    projectType:{
      type:String
    }
  }
};
</script>

<style scoped>
.cmpTitle {
  color: #0366d6;
  text-decoration: none;
  font-weight: 600;
  font-size: 18px;
}

.cmpDesc{
  font-size: 14px;
}

</style>

