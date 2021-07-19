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

  label {
    font-family: Lato,sans-serif;
    font-size: 16px
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
  <div style="display: flex">
      <div style="width:200px">
        <Menu style="height: calc(100vh - 120px);z-index: 2;" width="auto" :active-name="$route.name">
          <MenuGroup>
            <MenuItem name="overView" to="overView" class="sideItem">
              <Icon type="ios-book" size="25"/>
              <label>Overview</label>
            </MenuItem>

            <MenuItem name="project" to="project" class="sideItem">
              <Icon type="ios-cube" size="25"/>
              <label>Project</label>
            </MenuItem>


            <MenuItem name="resourceList" to="resourceList" class="sideItem">
              <Icon type="ios-cloud" size="25"/>
              <label>Resource</label>
            </MenuItem>

            <MenuItem name="tool" to="tool" class="sideItem">
              <Icon type="ios-cog" size="25"/>
              <label>Tool</label>

            </MenuItem>

            <MenuItem name="todoList" to="todoList" class="sideItem">
              <Icon type="ios-clipboard" size="25"/>
              <label>Todo List</label>
            </MenuItem>
          </MenuGroup>

          <MenuGroup >
            <MenuItem name="userInfo" to="userInfo" class="sideItem">
              <Icon type="ios-contact-outline" size="25"/>
              <label>My Account</label>
            </MenuItem>

            <MenuItem name="notification" to="notification" class="sideItem">
              <!-- <Icon type="ios-list" size="25"/> -->
              <Icon type="ios-mail-open-outline" size="25" />
              <label>Message</label>
            </MenuItem>
          </MenuGroup>
        </Menu>
      </div>

      <div style="width: calc(100vw - 200px)">
        <router-view style="margin-top: 1%"></router-view>
      </div>

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
  import resourceList from "../subPage/resourceList";

  export default {
    beforeRouteEnter: (to, from, next) => {
      next(vm => {
        if (!vm.$store.getters.userState || vm.$store.getters.userId == "") {
          vm.$router.push({ name: "Login" });
        }
      });
    },
    name: "newPersonalPage",
    components: {
      Project,
      ToWork,
      projectVue,
      notification,
      resource,
      "todo-list": todoList,
      tool,
      resourceList
    },
    data() {
      return {
        menuWidth: "",
      }
    },

    methods: {
      resizeContent() {
        if (window.innerHeight > 1440) {
          this.menuWidth = 240;
        } else {
          this.menuWidth = 170;
        }
        window.onresize = () => {
          return (() => {
            this.resizeContent();
          })();
        };
      },
    }
  }
</script>
