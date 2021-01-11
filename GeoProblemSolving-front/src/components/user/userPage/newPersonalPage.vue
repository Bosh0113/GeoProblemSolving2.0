<style scoped>
  .sideItem {
    height: 58px;
    border: 2px solid transparent;
    box-sizing: border-box;
    transition: color .3s;
    /*padding-left: 40px;*/
    /*padding-right: 65px;*/
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
        <Menu style="height: calc(100vh - 120px)" >
          <MenuGroup>
            <MenuItem name="overView" to="overView">
              <Icon type="ios-book" size="25"/>
              <label style="font-family: 'Open Sans', sans-serif; font-size: 16px">Overview</label>
            </MenuItem>

            <MenuItem name="project" to="project">
              <Icon type="ios-cube" size="25"/>
              <label style="font-family: 'Open Sans', sans-serif; font-size: 16px">Project</label>
            </MenuItem>


            <MenuItem name="resource" to="resource">
              <Icon type="ios-cloud" size="25"/>
              <label style="font-family: 'Open Sans', sans-serif; font-size: 16px">Resource</label>
            </MenuItem>

            <MenuItem name="tool" to="tool">
              <Icon type="ios-cog" size="25"/>
              <label style="font-family: 'Open Sans', sans-serif; font-size: 16px">Tool</label>

            </MenuItem>

            <MenuItem name="todoList" to="todoList">
              <Icon type="ios-clipboard" size="25"/>
              <label style="font-family: 'Open Sans', sans-serif; font-size: 16px">Todo List</label>
            </MenuItem>
          </MenuGroup>

          <MenuGroup>
            <MenuItem name="userInfo" to="userInfo">
              <Icon type="ios-contact-outline" size="25"/>
              <label style="font-family: 'Open Sans', sans-serif; font-size: 16px">My Account</label>
            </MenuItem>

            <MenuItem name="notification" to="notification">
              <Icon type="ios-list" size="25"/>
              <label style="font-family: 'Open Sans', sans-serif; font-size: 16px">Message</label>
            </MenuItem>
          </MenuGroup>
        </Menu>
      </Col>

      <Col span="19" offset="1">
        <router-view style="margin-top: 30px"></router-view>
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
      return {
      }
    },
  }
</script>

