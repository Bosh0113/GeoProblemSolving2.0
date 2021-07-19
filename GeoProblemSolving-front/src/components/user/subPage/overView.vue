<template>
  <div>
    <div id="header" style="margin-top: 5%;"></div>
    <Row>
      <Col span="9" offset="2">
        <Card class="customCard">
          <p slot="title">
            <Icon type="ios-cube" size="20"></Icon>
            <big>Project</big>
           </p>
          <div slot="extra">
            <router-link to="project">
              <Icon type="ios-arrow-dropright" size="25" color="deepskyblue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span class="customFont">Manage project created and joined by yourself</span>

        </Card>
      </Col>
      <Col span="9" offset="1">
        <Card class="customCard">
          <p slot="title">
            <Icon type="ios-cloud" size="20"></Icon>
            <big>Resource</big>
           </p>
          <div slot="extra">
            <router-link to="resourceList">
              <Icon type="ios-arrow-dropright" size="25" color="deepskyblue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage resource upload by yourself</span>
        </Card>
      </Col>
    </Row>

    <Row style="margin-top: 50px">
      <Col span="9" offset="2">
        <Card class="customCard">
          <p slot="title">
            <Icon type="ios-cog" size="20"></Icon>
            <big>Tool</big>
           </p>
          <div slot="extra">
            <router-link to="tool">
              <Icon type="ios-arrow-dropright" size="25" color="deepskyblue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage tool created by yourself</span>
        </Card>
      </Col>
      <Col span="9" offset="1">
        <Card class="customCard">
          <p slot="title">
            <Icon type="ios-clipboard" size="20"></Icon>
            <big>Todo List</big>
           </p>
          <div slot="extra">
            <router-link to="todoList">
              <Icon type="ios-arrow-dropright" size="25" color="deepskyblue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage your todo list</span>
        </Card>
      </Col>
    </Row>

    <Row style="margin-top: 50px">
      <Col span="9" offset="2">
        <Card class="customCard">
          <p slot="title">
            <Icon type="ios-contact" size="20"></Icon>
            <big>My Account</big>
           </p>
          <div slot="extra">
            <router-link to="userInfo">
              <Icon type="ios-arrow-dropright" size="25" color="deepskyblue" style="cursor:pointer;"/>
            </router-link>
          </div>
          <span>You can manage tool created by yourself</span>
        </Card>
      </Col>
      <Col span="9" offset="1">
        <Card class="customCard">
          <p slot="title">
            <Icon type="ios-mail-open" size="20"></Icon>
            <big>Message</big>
           </p>
          <div slot="extra">
            <router-link to="notification">
              <Icon type="ios-arrow-dropright" size="25" color="deepskyblue" style="cursor:pointer;"/>
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
    /* width: 335px; */
    /* background-color:#f0faff; */
    border-radius: 15px;
  }


  .customFont {
    font-family: 'Lato', sans-serif;
  }

</style>
