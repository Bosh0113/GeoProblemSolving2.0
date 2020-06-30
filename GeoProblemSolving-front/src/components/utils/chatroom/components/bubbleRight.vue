<!-- bubble-left green -->
<template>
  <div class>
    <template>
      <template v-if="list.type === 'notice'">
        <Row>
          <div class="chat-notice">{{list.content}}</div>
        </Row>
      </template>

      <template v-else>
        <Row>
          <div style="float:right;">
            <Row style="margin:8px 12px 0 0">
              <div class="user_name">{{list.srcUserName}}</div>
            </Row>
            <Row>
              <div class="chat-bubble-r chat-bubble-right">
                <template v-if="list.type === 'message_pic'" class="picOuter">
                  <img :src="list.content" class="send_pic" />
                </template>
                <template v-else-if="list.type === 'message_tool'" class="picOuter">
                  <simple-public-card :toolFrom="list.content" :isOpenTool="isOpenTool"></simple-public-card>
                </template>
                <template v-else>{{list.content}}</template>
              </div>
            </Row>
          </div>
        </Row>
      </template>
    </template>
  </div>
</template>

<script>
import simplePublicCard from "@/components/common/card/simplePublicToolsCard";
import toolModal from "@/components/common/tools/toolModal";
export default {
  props: {
    message: {
      type: Object
    }
  },

  components: {
    simplePublicCard,
    toolModal
  },

  data() {
    return {
      list: this.message,
      isOpenTool: true
    };
  },
  methods: {}
};
</script>


<style lang='scss' scoped>
//send notice
.chat-notice {
  color: darkgreen;
  width: 90%;
  text-align: center;
}

//user
.user_detail {
  height: 60px;
  .user_avatar_outer {
    width: 60px;
    height: 60px;
    .user_avatar {
      width: 100%;
      height: 100%;
    }
    .user_img {
      background-color: lightblue;
      margin-top: 2px;
      margin-left: 5px;
    }
  }
}

.user_name {
  text-align: right;
  /* text-shadow: rgb(75, 75, 75) 0px 0px 2px; */
  color: rgb(63, 63, 63);
  font-size: 10px;
  line-height: 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

//bubble green
.chat-bubble-r {
  position: relative;
  margin: 12px;
  padding: 8px;
  word-break: break-all;
  background: rgb(96, 206, 120);
  border: 1px solid transparent;
  color: black;
  border-radius: 5px;
  font-size: 18px;
  float: right;
}

// .chat-bubble-right:before {
//   content: "";
//   position: absolute;
//   width: 0;
//   height: 0;
//   right: -20px;
//   top: 5px;
//   border: 10px solid;
//   border-color: transparent transparent transparent rgb(96, 206, 120);
// }

// .chat-bubble-right:after {
//   content: "";
//   position: absolute;
//   width: 0;
//   height: 0;
//   right: -16px;
//   top: 7px;
//   border: 8px solid;
//   border-color: transparent transparent transparent rgb(96, 206, 120);
// }

//send picture
.picOuter {
  max-height: 500px;
  max-width: 600px;
}

.send_pic {
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
}
</style>