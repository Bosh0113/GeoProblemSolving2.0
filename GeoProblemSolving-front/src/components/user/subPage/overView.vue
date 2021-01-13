<template>
  <div>
    <Row>
      <Col span="5" offset="6">
        <Card class="customCard">
          <p slot="title">Project</p>
          <div slot="extra">
            <router-link to="project">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span class="customFont">Manage project created and joined by yourself</span>

        </Card>
      </Col>
      <Col span="5" offset="2">
        <Card class="customCard">
          <p slot="title">Resource</p>
          <div slot="extra">
            <router-link to="resource">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage resource upload by yourself</span>
        </Card>
      </Col>
    </Row>

    <Row style="margin-top: 50px">
      <Col span="5" offset="6">
        <Card class="customCard">
          <p slot="title">Tool</p>
          <div slot="extra">
            <router-link to="tool">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage tool created by yourself</span>
        </Card>
      </Col>
      <Col span="5" offset="2">
        <Card class="customCard">
          <p slot="title">Todo List</p>
          <div slot="extra">
            <router-link to="todoList">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage your todo list</span>
        </Card>
      </Col>
    </Row>

    <Row style="margin-top: 50px">
      <Col span="5" offset="6">
        <Card class="customCard">
          <p slot="title">My Account</p>
          <div slot="extra">
            <router-link to="userInfo">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage tool created by yourself</span>
        </Card>
      </Col>
      <Col span="5" offset="2">
        <Card class="customCard">
          <p slot="title">Message</p>
          <div slot="extra">
            <router-link to="notification">
              <Icon type="ios-arrow-dropright" size="20" color="blue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage your message</span>
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
        this.$router.push({name: 'tool'})
      }
    }
  }
</script>

<style scoped>
  .customCard {
    /*width: 335px;*/
  }

  .customFont {
    font-family: 'Lato', sans-serif;
  }
</style>
