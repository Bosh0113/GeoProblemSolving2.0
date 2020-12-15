<style scoped>
  .sideItem {
    height: 58px;
    border: 2px solid transparent;
    box-sizing: border-box;
    transition: color .3s;
    padding: 3px 0;
  }

  .sideItem:hover {
    backdrop-filter: blur(5px);
    background-color: rgba(255, 255, 255, 0.4);
    /* box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.23); */
    border-width: 1px 0 1px 1px;
    border-style: solid;
    border-color: #e0e0e0;
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
                <Icon type="ios-book-outline"/>
                <router-link to="./home"> Overview</router-link>
              </MenuItem>
              <MenuItem name="project" class="sideItem">
                <Icon type="ios-cube-outline"/>
                <router-link to="/test">Project</router-link>
              </MenuItem>
              <MenuItem name="resource" class="sideItem">
                <Icon type="ios-cloud-outline"/>
                <router-link to="./home">Resource</router-link>
              </MenuItem>
              <MenuItem name="tool" class="sideItem">
                <Icon type="ios-cog-outline"/>
                <router-link to="./home">Tool</router-link>
              </MenuItem>
              <MenuItem name="todoList" class="sideItem">
                <Icon type="ios-clipboard-outline"/>
                <router-link to="./home">TodoList</router-link>
              </MenuItem>
            </MenuGroup>
            <MenuGroup>
              <MenuItem name="userInfo" class="sideItem">
                <Icon type="ios-contact-outline"/>
                <router-link to="./home">UserInfo</router-link>
              </MenuItem>
              <MenuItem name="notification" class="sideItem">
                <Icon type="ios-list"/>
                <router-link to="./home">Notification</router-link>
              </MenuItem>
              <MenuItem name="trash" class="sideItem">
                <Icon type="ios-trash-outline"/>
                <router-link to="./home">Trash</router-link>
              </MenuItem>
            </MenuGroup>
          </Menu>
        </div>
      </Col>

      <Col span="20" offset="1">
        <notification />
      </Col>
    </Row>
  </div>

</template>

<script>
  import projectVue from "../subPage/project.vue";
  import ToWork from "../../projects/toWork";
  import notification from "../subPage/notification";

  export default {
    name: "newPersonalPage",
    components: {
      ToWork,
      projectVue,
      notification
    },
    data() {
      return {}
    },
  }
</script>

