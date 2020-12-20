<style scoped>
  .sideItem {
    height: 58px;
    border: 2px solid transparent;
    box-sizing: border-box;
    transition: color .3s;
    padding-left: 40px;
    padding-right: 65px;
  }

  .sideItem:hover {
    backdrop-filter: blur(5px);
    background-color: rgba(255, 255, 255, 0.4);
    box-shadow: 0px 0px 5px 0px rgba(0, 0, 0, 0.23);
    border-width: 1px 0 1px 1px;
    border-style: solid;
    border-color: #e0e0e0;
    padding-left: 30px;
  }

  .sideContainer {
    height: 100%;
    padding-bottom: 150px;
  }

  a {
    text-decoration: none;
    color: black;
  }

  .router-link-active {
    text-decoration: none;
  }

  .customFont {
    font-size: 16px;
    margin: 0;
    font-family: 'Open Sans', sans-serif;
  }
</style>
<template>
  <div>
    <Row>
      <Col span="3">
        <div
        >
          <Menu style="height:calc(100vh - 120px);">
            <MenuGroup>
              <router-link to="./home" >
              <MenuItem name="overview" class="sideItem">
                <Icon type="ios-book" size="25"/>
                <sapn class="customFont"> Overview </sapn>
              </MenuItem>
              </router-link>

              <router-link to="/test">
              <MenuItem name="project" class="sideItem">
                <Icon type="ios-cube" size="25"/>
                <sapn class="customFont">Project</sapn>
              </MenuItem>
              </router-link>

              <router-link to="./home">
              <MenuItem name="resource" class="sideItem">
                <Icon type="ios-cloud" size="25"/>
                <sapn class="customFont">Resource</sapn>
              </MenuItem>
              </router-link>
              <router-link to="./home" >
              <MenuItem name="tool" class="sideItem">
                <Icon type="ios-cog" size="25"/>
                <span class="customFont">Tool</span>
              </MenuItem>
              </router-link>
              <MenuItem name="todoList" class="sideItem">
                <Icon type="ios-clipboard" size="25"/>
                <router-link to="./home" class="customFont">TodoList</router-link>
              </MenuItem>
            </MenuGroup>

            <MenuGroup>
              <router-link to="./home">
              <MenuItem name="userInfo" class="sideItem">
                <Icon type="ios-contact-outline" size="25"/>
<!--                <router-link to="./home" ></router-link>-->
                <span class="customFont">UserInfo</span>
              </MenuItem>
              </router-link>

              <router-link to="./home">
              <MenuItem name="notification" class="sideItem">
                <Icon type="ios-list" size="25"/>
                <span class="customFont">Notification</span>
              </MenuItem>
              </router-link>

              <router-link to="./home" >
              <MenuItem name="trash" class="sideItem">
                <Icon type="ios-trash-outline" size="25"/>
                <span class="customFont">Trash</span>
              </MenuItem>
              </router-link>
            </MenuGroup>
          </Menu>
        </div>
      </Col>

      <Col span="19" offset="1">
        <Row style="margin-top: 30px"></Row>
        <Row>
          <todo-list></todo-list>
        </Row>

<!--                <notification />-->
<!--        <resource/>-->
      </Col>
    </Row>
  </div>

</template>

<script>
  import projectVue from "../subPage/project.vue";
  import ToWork from "../../projects/toWork";
  import notification from "../subPage/notification";
  import resource from "../subPage/resource";
  import todoList from "../subPage/todoList";

  export default {
    name: "newPersonalPage",
    components: {
      ToWork,
      projectVue,
      notification,
      resource,
      "todo-list": todoList,
    },
    data() {
      return {}
    },
  }
</script>

