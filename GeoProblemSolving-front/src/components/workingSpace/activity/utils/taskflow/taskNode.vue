<template>
  <div class="card-devices">
    <div class="drawflow-node-title" v-if="type == 'task'">
      <h3>{{ name }}</h3>
    </div>
    <div class="drawflow-node-body">
      <div
        v-if="type == 'task' && !unfold"
        @click="operationShow"
        style="
          cursor: pointer;
          text-align: center;
          background-color: #f9fdf1;
          padding: 10px 0;
        "
      >
        See more <Icon type="md-more" size="20" />
      </div>
      <div v-else-if="type == 'task' && unfold" style="cursor: default">
        <Timeline style="padding: 10px; height: 350px">
          <vue-scroll :ops="scrollOps">
            <!-- <TimelineItem v-for="record in operationRecords" :key="record.id">
              <p class="time">{{ record.time }}</p>
              <div class="content" v-if="record.type === 'resource'">
                Resource operation:
                <span class="behavior">{{ record.behavior }}</span>
                <div>
                  <img
                    :src="dataUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-if="record.resource.type == 'data'"
                  />
                  <img
                    :src="modelUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'model'"
                  />
                  <img
                    :src="documentUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'document'"
                  />
                  <img
                    :src="paperUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'paper'"
                  />
                  <img
                    :src="imageUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'iamge'"
                  />
                  <img
                    :src="videoUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'video'"
                  />
                  <img
                    :src="otherUrl"
                    height="42px"
                    width="42px"
                    :title="record.resource.name"
                    v-else-if="record.resource.type == 'others'"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    :username="record.operator.name"
                    :size="16"
                    :rounded="true"
                    :title="record.operator.name"
                  />
                </div>
              </div>
              <div class="content" v-else-if="record.type === 'tool'">
                <p class="time">{{ record.time }}</p>
                <div class="content">
                  Tool operation:
                  <span class="behavior">{{ record.behavior }}</span>
                  <div>
                    <img
                      :src="toolUrl"
                      height="36px"
                      width="36px"
                      :title="record.tool.name"
                      class="tool"
                    />
                  </div>
                  <div class="participant">
                    <span style="margin-right: 5px">People: </span>
                    <avatar
                      class="person"
                      :username="record.operator.name"
                      :size="16"
                      :rounded="true"
                      :title="record.operator.name"
                    />
                  </div>
                </div>
              </div>
              <div class="content" v-else-if="record.type === 'communication'">
                <p class="time">{{ record.time }}</p>
                <div class="content">
                  <span class="behavior">Communication</span>
                  <div>
                    <img
                      :src="chatUrl"
                      height="36px"
                      width="36px"
                      title="Communication records"
                      class="resource"
                    />
                  </div>
                  <div class="participant">
                    <span style="margin-right: 5px">People: </span>
                    <avatar
                      v-for="person in record.operators"
                      :key="person.id"
                      class="person"
                      :username="person.name"
                      :size="16"
                      :rounded="true"
                      :title="person.name"
                    />
                  </div>
                </div>
              </div>
              <div class="content" v-else-if="record.type === 'geo-analysis'">
                <p class="time">{{ record.time }}</p>
                <div class="content">
                  <span class="behavior">Geo-analysis by using tools</span>
                  <div>
                    <img
                      :src="toolUrl"
                      height="36px"
                      width="36px"
                      :title="record.tool.name"
                      class="resource"
                    />
                    <Divider
                      type="vertical"
                      style="height: 36px; margin-top: -30px"
                    />
                    <img
                      v-for="result in record.resources.outputs"
                      :key="result.name"
                      :src="dataUrl"
                      height="36px"
                      width="36px"
                      style="margin-right: 5px"
                      title="Result"
                      class="resource"
                    />
                  </div>
                  <div class="participant">
                    <span style="margin-right: 5px">People: </span>
                    <avatar
                      class="person"
                      :username="record.operator"
                      :size="16"
                      :rounded="true"
                      :title="record.operator"
                    />
                  </div>
                </div>
              </div>
              <Divider />
            </TimelineItem> -->
            <TimelineItem>
              <p class="time">2021-1-1 20:00:00</p>
              <div class="content">
                Resource <span class="behavior">upload</span>
                <div>
                  <img
                    :src="dataUrl"
                    height="36px"
                    width="36px"
                    title="Data"
                    class="resource"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
            <TimelineItem>
              <p class="time">2021-1-1</p>
              <div class="content">
                <span class="behavior">Tool add</span>
                <div>
                  <img
                    :src="toolUrl"
                    height="36px"
                    width="36px"
                    title="Data"
                    class="resource"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="mzy"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
            <TimelineItem>
              <p class="time">2021-1-1</p>
              <div class="content">
                <span class="behavior">communication</span>
                <div>
                  <img
                    :src="otherUrl"
                    height="36px"
                    width="36px"
                    title="Data"
                    class="resource"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
            <TimelineItem>
              <p class="time">2021-1-1</p>
              <div class="content">
                <span class="behavior"
                  >xxxx xxx xxxxxxxx xxxxxxx xxxxxxx xxx xxx xxxxx xxxxx</span
                >
                <div>
                  <img
                    :src="toolUrl"
                    height="36px"
                    width="36px"
                    title="Tool"
                    class="resource"
                  />
                  <Divider
                    type="vertical"
                    style="height: 36px; margin-top: -30px"
                  />
                  <img
                    :src="dataUrl"
                    height="36px"
                    width="36px"
                    title="Result"
                    class="resource"
                  />
                </div>
                <div class="participant">
                  <span style="margin-right: 5px">People: </span>
                  <avatar
                    class="person"
                    username="1XXX"
                    :size="16"
                    :rounded="true"
                    title="xxx"
                  />
                </div>
              </div>
              <Divider />
            </TimelineItem>
          </vue-scroll>
        </Timeline>
        <div
          style="
            cursor: pointer;
            text-align: center;
            background-color: #f9fdf1;
            padding: 10px 0;
          "
          @click="unfold = false"
        >
          Hide <Icon type="ios-arrow-up" size="20" style="margin-top: -2px" />
        </div>
      </div>
      <div v-else-if="type == 'operation'" style="padding: 10px"></div>
    </div>
  </div>
</template>
<script>
import Avatar from "vue-avatar";
export default {
  components: {
    Avatar,
  },
  props: ["type", "taskId", "name", "operations"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey",
        },
      },
      unfold: false,
      operationRecords: [],
      // 资源avatar 图片路径
      dataUrl: require("@/assets/images/data.png"),
      modelUrl: require("@/assets/images/model.png"),
      paperUrl: require("@/assets/images/paper.png"),
      documentUrl: require("@/assets/images/document.png"),
      imageUrl: require("@/assets/images/image.png"),
      videoUrl: require("@/assets/images/video.png"),
      otherUrl: require("@/assets/images/otherfile.png"),
      // 工具avatar 图片路径
      toolUrl: require("@/assets/images/toolbox.png"),
      chatUrl: require("@/assets/images/chat.png"),
    };
  },
  methods: {
    operationShow() {
      this.unfold = true;
    //   for (var i = 0; i < this.operations.length; i++) {
    //     let operation = this.operationApi.getOperationInfo(this.operations[i]);
    //     if (operation.type === "resource") {
    //       // resource 
    //       let resInfo = this.operationApi.getResInfo(operation.resRef);
    //       operation.resource = resInfo;
    //       let operatorInfo = this.operationApi.getMemberInfo(
    //         operation.operator
    //       );
    //       operation.operator = operatorInfo;
    //     } else if (operation.type === "tool") {
    //       // tool
    //       let toolInfo = this.operationApi.getResInfo(operation.toolRef);
    //       operation.tool = toolInfo;
    //       let operatorInfo = this.operationApi.getMemberInfo(
    //         operation.operator
    //       );
    //       operation.operator = operatorInfo;
    //     } else if (operation.type === "communication") {
    //       // communication
    //       let toolInfo = this.operationApi.getResInfo(operation.toolRef);
    //       operation.tool = toolInfo;
    //       let resInfo = this.operationApi.getResInfo(operation.resRef);
    //       operation.resource = resInfo;
    //       // participants
    //       let participants = [];
    //       for (var j = 0; j < operation.operators.length; j++) {
    //         let personInfo = this.operationApi.getMemberInfo(
    //           operation.operators[j]
    //         );
    //         participants.push(personInfo);
    //       }
    //       operation.operators = participants;
    //     } else if (operation.type === "geo-analysis") {
    //       // geo-analysis
    //       let toolInfo = this.operationApi.getResInfo(operation.toolRef);
    //       operation.tool = toolInfo;
    //       let operatorInfo = this.operationApi.getMemberInfo(
    //         operation.operator
    //       );
    //       operation.operator = operatorInfo;
    //       // results
    //       let results = [];
    //       for (var j = 0; j < operation.resources.outputs.length; j++) {
    //         let resInfo = this.operationApi.getResInfo(
    //           operation.resources.outputs[j]
    //         );
    //         results.push(resInfo);
    //       }
    //       operation.resources.outputs = results;
    //     }
    //     this.operationRecords.push(operation);
    //   }
    },
  },
};
</script>
<style scoped>
.time {
  font-size: 14px;
  font-weight: bold;
}
.content {
  width: 200px;
  word-break: break-word;
}
.tool {
  cursor: pointer;
}
.resource {
  cursor: pointer;
}
.participant {
  display: flex;
  position: relative;
}
.participant .person {
  margin-right: 5px;
  cursor: pointer;
}
.ivu-divider-horizontal {
  margin: 10px 0 0 0;
}
.drawflow-node-title {
  height: 50px;
  line-height: 50px;
  background: #f7f7f7;
  border-bottom: 1px solid #e9e9e9;
  border-radius: 4px 4px 0px 0px;
  padding-left: 15px;
}
.drawflow-node-body {
  font-size: 14px;
  color: #555555;
}
</style>