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

              <MenuItem name="overview" class="sideItem">
                <Icon type="ios-book" size="25"/>
                <router-link to="overView" class="customFont">Overview</router-link>
              </MenuItem>

              <MenuItem name="project" class="sideItem">
                <Icon type="ios-cube" size="25"/>
                <router-link to="project" class="customFont">Project</router-link>
              </MenuItem>

              <MenuItem name="resource" class="sideItem">
                <Icon type="ios-cloud" size="25"/>
                <router-link to="resource" class="customFont">Resource</router-link>
              </MenuItem>

              <MenuItem name="tool" class="sideItem">
                <Icon type="ios-cog" size="25"/>
                <router-link to="tool" class="customFont">Tool</router-link>
              </MenuItem>

              <MenuItem name="todoList" class="sideItem">
                <Icon type="ios-clipboard" size="25"/>
                <router-link to="todoList" class="customFont">TodoList</router-link>
              </MenuItem>
            </MenuGroup>

            <MenuGroup>
              <MenuItem name="userInfo" class="sideItem">
                <Icon type="ios-contact-outline" size="25"/>
                <router-link to="userInfo" class="customFont">UserInfo</router-link>
              </MenuItem>

              <MenuItem name="notification" class="sideItem">
                <Icon type="ios-list" size="25"/>
                <router-link to="notification" class="customFont">Notification</router-link>
              </MenuItem>

              <MenuItem name="trash" class="sideItem">
                <Icon type="ios-trash-outline" size="25"/>
                <router-link to="todoList" class="customFont">Trash</router-link>
              </MenuItem>

            </MenuGroup>
          </Menu>
        </div>
      </Col>

      <Col span="19" offset="1">
        <Row style="margin-top: 30px"></Row>
        <Row>
          <router-view></router-view>
        </Row>
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
  import Project from "../subPage/project";
  import tool from "../subPage/tool";

  export default {
    name: "newPersonalPage",
    components: {
      Project,
      ToWork,
      projectVue,
      notification,
      resource,
      "todo-list": todoList,
      tool
    },
    data() {
      return {}
    },
  }
</script>

