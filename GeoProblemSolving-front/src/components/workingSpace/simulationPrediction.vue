<style scoped>
.picscreen {
  position: relative;
  padding-top: 10px;
}
.picbg {
  background-image: url("../../assets/images/condefbg.png");
  background-size: cover;
  position: absolute;
  height: 100px;
  width: 100%;
  background-color: #8b8b8b;
  top: 0;
  left: 0;
}
.homeContent >>> .ivu-breadcrumb {
  color: rgb(180, 197, 207);
}
.homeContent >>> .ivu-breadcrumb > span:last-child {
  color: rgb(180, 197, 207);
}
</style>
<template>
  <div style="background-color:#e8eaec;height:auto">
    <Row>
      <div class="picscreen">
        <div class="picbg"></div>
        <div class="homeContent">
          <Row>
            <!-- 需要修改样式 -->
            <Col span="6" style="height:40px;">
            <Breadcrumb>
              <!-- <BreadcrumbItem :to="toProjectPage">Project</BreadcrumbItem> -->
                <BreadcrumbItem :to="toSubProjectPage">Subproject</BreadcrumbItem>
                <BreadcrumbItem>Modeling for Geographic Process</BreadcrumbItem>
            </Breadcrumb>
            </Col>
            <Col span="12" style="text-align:center;font-size:1.5rem;height:20px;color:white;margin-top:1%">
            <strong>Modeling for Geographic Process</strong>
            </Col>
          </Row>
        </div>
      </div>
    </Row>


    </div>
</template>
<script>
export default {
    data(){
        return{
            stepId: this.$route.params.id,
        }
    }
};
</script>
