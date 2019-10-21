<style scoped>
h1 {
  text-align: center;
  margin-top: 2.5%;
}
.toolForm {
  width: 60%;
  margin-top: 0.5%;
  margin-left: 20%;
  margin-right: 20%;
}
.inline_style {
  display: flex;
}
.create {
  width: 20%;
  margin-right: 20%;
  margin-left: 10%;
  margin-top: 5%;
}
.back {
  width: 20%;
  margin-right: 10%;
  margin-left: 20%;
  margin-top: 5%;
}
/* 结束 */
</style>
<template>
  <div class="tool form">
    <h1>New Tool</h1>
    <div>
      <Form
        ref="toolInfo"
        :model="toolInfo"
        :rules="toolInfoRule"
        :label-width="80"
        class="toolForm"
      >
        <FormItem label="Name:" prop="name" :label-width="140">
          <Input v-model="toolInfo.name" placeholder="Enter the name of your tool"></Input>
        </FormItem>
        <FormItem label="Model stateId:" prop="model_stateId" :label-width="140">
          <Input
            v-model="toolInfo.model_stateId"
            placeholder="Enter the model stateId of your tool"
          ></Input>
        </FormItem>
        <FormItem label="Model oid:" prop="model_oid" :label-width="140">
          <Input v-model="toolInfo.model_oid" placeholder="Enter the model oid of your tool"></Input>
        </FormItem>
        <FormItem label="Model mdlId:" prop="model_mdlId" :label-width="140">
          <Input v-model="toolInfo.model_mdlId" placeholder="Enter the model mdlId of your tool"></Input>
        </FormItem>
        <FormItem label="Toolset name:" prop="toolsetRecords" :label-width="140">
          <Select
            v-model="toolInfo.toolsetRecords"
            multiple
            placeholder="The tool will be put in default toolset if no special toolset selected."
          >
            <Option v-for="item in toolsetList" :key="item.tsid" :value="item">{{ item.toolsetName }}</Option>
          </Select>
          <div style="cursor:pointer" @click="createToolset()">
            Add a new toolset
            <Icon type="ios-add-circle-outline" />
          </div>
        </FormItem>
        <FormItem label="Tool url:" prop="tool_url" :label-width="140">
          <Input v-model="toolInfo.tool_url" placeholder="Enter the tool url of your tool"></Input>
        </FormItem>
        <FormItem label="Recommended step:" prop="recomStep" :label-width="140">
          <Select
            v-model="toolInfo.recomStep"
            placeholder="Select the recommended step of yout tool"
          >
            <Option v-for="item in stepList" :key="item.index" :value="item">{{ item }}</Option>
          </Select>
        </FormItem>
        <FormItem label="Categroy tag:" prop="categoryTag" :label-width="140">
          <Input
            v-model="inputToolTag"
            placeholder="Enter some tag to classify your tools"
            style="width: 400px"
            @keyup.enter.native="addTag(inputToolTag)"
          />
          <Button
            icon="ios-add"
            type="dashed"
            size="small"
            @click="addTag(inputToolTag)"
            style="margin-left:2.5%"
          >Add tag</Button>
          <div>
            <Tag
              color="primary"
              v-for="(item,index) in this.toolInfo.categoryTag"
              :key="index"
              closable
              @on-close="deleteTag(index)"
            >{{item}}</Tag>
          </div>
          <div>
            <span>Example:</span>
            <Tag style="cursor:default">vector</Tag>
            <Tag style="cursor:default">raster</Tag>
            <Tag style="cursor:default">evaluation</Tag>
          </div>
        </FormItem>
        <FormItem label="Privacy:" prop="privacy" :label-width="140">
          <RadioGroup v-model="toolInfo.privacy">
            <Radio label="public">Public</Radio>
            <Radio label="pirvate">Private</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem>
          <div class="inline_style">
            <Button
              type="success"
              @click="createTool('toolInfo')"
              class="create"
              :disabled="clickForbidden"
            >Create</Button>
            <Button type="success" @click="Back()" class="back">Back</Button>
          </div>
        </FormItem>
      </Form>
    </div>
  </div>
</template>
<script>
export default {
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      $.ajax({
        url: "/GeoProblemSolving/user/state",
        type: "POST",
        async: false,
        success: function(data) {
          if (!data) {
            vm.$store.commit("userLogout");
            vm.$router.push({ name: "Login" });
          }
        },
        error: function(err) {
          console.log("Get user state fail.");
        }
      });
    });
  },
  data() {
    return {
      // 控制按钮禁用的
      clickForbidden: false,
      inputToolTag: "",
      toolInfo: {
        name: "",
        model_stateId: "",
        model_oid: "",
        model_mdlId: "",
        toolsetRecords: [],
        tool_url: "",
        recomStep: "",
        categoryTag: [],
        privacy: "pirvate"
      },
      toolInfoRule: {
        name: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur"
          }
        ],
        model_stateId: [
          {
            required: true,
            message: "The state id cannot be empty",
            trigger: "blur"
          }
        ],
        model_oid: [
          {
            required: true,
            message: "The oid cannot be empty",
            trigger: "blur"
          }
        ],
        model_mdlId: [
          {
            required: true,
            message: "The mdl id cannot be empty",
            trigger: "blur"
          }
        ],
        tool_url: [
          {
            required: true,
            message: "The tool url cannot be empty",
            trigger: "blur"
          }
        ],
        recomStep: [
          {
            required: true,
            message: "Please select the recommended step",
            trigger: "blur"
          }
        ],
        privacy: [
          {
            required: true,
            message: "Is this tool can be used by public or not?",
            trigger: "change"
          }
        ]
      },
      stepList: [
        "General step",
        "Context definition & resource collection",
        "Data processing",
        "Modeling for geographic process",
        "Model evaluation",
        "Quantitative and qualitative analysis",
        "Simulation/Prediction",
        "Visualization & representation",
        "Decision-making & management"
      ],
      toolsetList: [
        {
          tsid: "1",
          toolsetName: "test1"
        },
        {
          tsid: "2",
          toolsetName: "test2"
        }
      ]
    };
  },
  methods: {
    createTool(tool) {
      this.$refs[tool].validate(valid => {
        if (valid) {
          this.clickForbidden = true;

          let createToolForm = {};
          createToolForm["toolName"] = this.toolInfo.name;
          createToolForm["toolUrl"] = this.toolInfo.tool_url;
          createToolForm["modelInfo"] = {
            stateId: this.toolInfo.stateId,
            oid: this.toolInfo.oid,
            mdlId: this.toolInfo.mdlId
          };
          createToolForm["toolsetInfo"] = this.toolInfo.toolsetRecords;
          createToolForm["recomStep"] = this.toolInfo.recomStep;
          createToolForm["categoryTag"] = this.toolInfo.categoryTag;
          createToolForm["provider"] = this.$store.getters.userId;
          createToolForm["privacy"] = this.toolInfo.privacy;

          this.axios
            .post("/GeoProblemSolving/tool/create", createToolForm)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({ name: "Login" });
              } else if (res.data === "Fail") {
                this.$Message.error("Create tool fail.");
              } else if (res.data === "Duplicate naming") {
                this.$Message.error("The name already exists.");
              } else {
                // this.createToolId = res.data;
                // this.addHistoryEvent(this.createToolId);
                this.$Message.error("Create successfully");
              }
            })
            .catch(err => {
              console.log(err);
            });
        } else {
        }
      });
    },
    Back() {
      this.$router.go(-1);
    },
    submitTool() {},
    addTag(tag) {
      if (tag != "") {
        this.toolInfo.categoryTag.push(tag);
        this.inputTag = "";
      }
    },
    deleteTag(index) {
      this.toolInfo.categoryTag.splice(index, 1);
    },
    createToolset() {
      this.$router.push({ path: "createToolset" });
    }
  },
  created() {
    // 加入判断，如果未登录自动跳转登录页面
    if (!this.$store.getters.userState) {
      this.$router.push({ name: "Login" });
    }
  }
};
</script>
