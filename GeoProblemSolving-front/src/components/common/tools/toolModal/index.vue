<!--  -->
<template>
  <div>
    <Row>
      <Col class="leftContainer" :span="16">
        <Card dis-hover>
          <vue-scroll :ops="ops" style="height:400px;">
            <Row :gutter="16">
              <draggable element="ul" :options="{group:'tool'}" v-model="filterPublicTools">
                <div v-for="(tool,index) in filterPublicTools" :key="index">
                  <Col :span="8" style="margin-bottom:10px">
                    <simple-public-card :toolFrom="tool"></simple-public-card>
                  </Col>
                </div>
              </draggable>
            </Row>
          </vue-scroll>
        </Card>
      </Col>
      <Col class="rightContainer" :span="8">
        <Card dis-hover>
          <vue-scroll :ops="ops" style="height:400px;">
            <draggable
              element="ul"
              :group="{name:'tool', put:true, pull:false}"
              v-model="sentTools"
              @add="addSentTool"
              style="min-height:400px"
            >
              <div v-for="(tool,index) in sentTools" :key="tool.index" style="margin-bottom:5px">
                <Col>
                  <Card style="width:100%">
                    <div class="ellipsis" style="width:150px">{{tool.toolName}}</div>
                    <Button
                      shape="circle"
                      icon="md-remove"
                      class="changeRedColor"
                      size="small"
                      style="float:right"
                      @click="removeSelectedTools(index)"
                    ></Button>
                  </Card>
                </Col>
              </div>
            </draggable>
          </vue-scroll>
        </Card>
      </Col>
    </Row>
    <Row>
      <Button @click="submitSendTools">Submit</Button>
    </Row>
  </div>
</template>

<script>
import simplePublicCard from "@/components/common/card/simplePublicToolsCard";
import { get, del, post, put } from "@/axios";
import draggable from "vuedraggable";

export default {
  props: {
    userId: {
      type: String
    }
  },
  components: { simplePublicCard, draggable },
  data() {
    return {
      publicTools: [],
      user: this.userId,
      sentTools: [], //需要发送的工具
      ops: {
        bar: {
          background: "#808695"
        }
      },
      typeSelected: "All",
      typeOptions: [
        "All",
        "General step",
        "Context definition & resource collection",
        "Data processing",
        "Data visualization",
        "Geographic model construction",
        "Model effectiveness evaluation",
        "Geographical simulation",
        "Quantitative and qualitative analysis",
        "Decision-making for management",
        "Others"
      ]
    };
  },

  computed: {
    filterPublicTools: {
      get() {
        let tools = this.publicTools;
        let type = this.typeSelected;
        if (type == "All") {
          return tools;
        } else {
          return tools.filter(tool => {
            return tool.recomStep.includes(type);
          });
        }
      },
      set(val) {
        let tools = (this.publicTools = val);
        console.log(tools);
        let type = this.typeSelected;
        if (type == "All") {
          return tools;
        } else {
          return tools.filter(tool => {
            return tool.recomStep.includes(type);
          });
        }
      }
    }
  },

  mounted() {
    this.getPublicTools();
  },

  methods: {
    async getPublicTools() {
      let data = await get(
        "/GeoProblemSolving/tool/inquiry/?key=privacy&value=Public"
      );
      this.$set(this, "publicTools", data);
    },

    //add需要发送的tools
    addSentTool(evt) {
      let addedToolId = this.sentTools[evt.newDraggableIndex].tid;
      return this.publicTools.filter(tool => {
        return tool.tid != addedToolId;
      });
      console.log(this.publicTools);
    },
    removeSelectedTools(index) {
      var removeToolInfo = this.sentTools[index];
      this.sentTools.splice(index, 1);
      this.publicTools.push(removeToolInfo);
    },
    submitSendTools() {
      this.$emit("selectedTools", this.sentTools);
    }
  }
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
.ellipsis {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: top;
}
</style>