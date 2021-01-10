<template>
    <div>
      <Row>
        <Col span="5" offset="6">
          <Card class="customCard">
          <p slot="title">Project</p>
          <span>You can manage project created and joined by yourself</span>
            <router-link to="project">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;" />
            </router-link>
        </Card>
        </Col>
        <Col span="5" offset="2">
          <Card class="customCard">
            <p slot="title">Resource</p>
            <span>You can manage resource upload by yourself</span>
            <router-link to="resource">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;" />
            </router-link>
          </Card>
        </Col>
      </Row>

      <Row style="margin-top: 50px">
        <Col span="5" offset="6" >
          <Card class="customCard">
            <p slot="title">Tool</p>
            <span>You can manage tool created by yourself</span>
            <router-link to="tool">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;" />
            </router-link>
          </Card>
        </Col>
        <Col span="5" offset="2">
          <Card class="customCard">
            <p slot="title">Todo List</p>
            <span>You can manage your todo list</span>
            <router-link to="todoList">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;" />
            </router-link>
          </Card>
        </Col>
      </Row>

      <Row style="margin-top: 50px">
        <Col span="5" offset="6" >
          <Card class="customCard">
            <p slot="title">User Info</p>
            <span>You can manage tool created by yourself</span>
            <router-link to="userInfo">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;" />
            </router-link>
          </Card>
        </Col>
        <Col span="5" offset="2">
          <Card class="customCard">
            <p slot="title">Notification</p>
            <span>You can manage your todo list</span>
            <router-link to="notification">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;" />
            </router-link>
          </Card>
        </Col>
      </Row>
    </div>
</template>

<script>
    export default {
        name: "overView",
      methods: {
          test: function () {
            this.$router.push({ name: 'tool' })
          }
      }
    }
</script>

<style scoped>
.customCard {
  /*width: 335px;*/
}
</style>
