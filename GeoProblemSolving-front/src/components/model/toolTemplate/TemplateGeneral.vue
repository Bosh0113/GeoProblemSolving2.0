<style scoped>
</style>
<template>
  <div>
    <Form ref="toolInfo" :model="toolInfo" :rules="toolInfoRule" :label-width="80" class="toolForm">
      <FormItem label="Tool name:" prop="name" :label-width="140">
        <Input v-model="toolInfo.name" placeholder="Enter the name of your tool"></Input>
      </FormItem>
      <FormItem label="Tool description:" prop="description" :label-width="140">
        <Input
          v-model="toolInfo.description"
          type="textarea"
          placeholder="Enter description of your tool"
        />
      </FormItem>

      <FormItem label="Categroy tag:" prop="categoryTag" :label-width="140">
        <Input
          v-model="inputToolTag"
          placeholder="Enter some tag to classify your tools"
          style="width: 400px"
          @keyup.enter.native="addCreateToolTag(inputToolTag)"
        />
        <Button
          icon="ios-add"
          type="dashed"
          size="small"
          @click="addCreateToolTag(inputToolTag)"
          style="margin-left:2.5%"
        >Add tag</Button>
        <div>
          <Tag
            color="primary"
            v-for="(item,index) in this.toolInfo.categoryTag"
            :key="index"
            closable
            @on-close="deleteCreateToolTag(index)"
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
          <Radio label="Public">Public</Radio>
          <Radio label="Private">Private</Radio>
        </RadioGroup>
      </FormItem>
      <Button type="primary" @click="saveInfo('toolInfo')" class="save">Save</Button>
    </Form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      selectModelRoute: this.modelRoute,
      selectModel: this.modelItem,
      selectModelName: "",
      selectModelDes: "",
      selectModelUrl: "",
      toolInfo: {
        name: "",
        description: "",
        tool_url: "",
        recomStep: [],
        categoryTag: [],
        toolImg: "",
        privacy: "Private"
      },
      toolInfoRule: {
        name: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur"
          }
        ],
        description: [
          {
            required: true,
            message: "The tool description cannot be empty",
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
      userId: "testUserId",
      inputToolTag: "",
      visible: false,
      //表示图片
      image: ""
    };
  },

  methods: {
    saveInfo(tool) {
      let createToolForm = {};
      createToolForm["toolName"] = this.toolInfo.name;
      createToolForm["toolUrl"] = this.toolInfo.tool_url;
      createToolForm["description"] = this.toolInfo.description;
      createToolForm["provider"] = this.userId;
      createToolForm["privacy"] = this.toolInfo.privacy;
      //   this.axios
      //     .post("/GeoProblemSolving/tool/create", createToolForm)
      //     .then(res => {
      //       if (res.data == "Offline") {
      //         this.$store.commit("userLogout");
      //         this.$router.push({ name: "Login" });
      //       } else if (res.data === "Fail") {
      //         this.$Notice.error({ desc: "Create tool fail." });
      //       } else if (res.data === "Duplicate naming") {
      //         this.$Notice.error({ desc: "The name already exists." });
      //       } else {
      //         this.createToolModal = false;
      //         this.$Notice.info({ desc: "Create successfully" });
      //         this.personalTools.push(res.data);
      //         if (this.toolInfo.privacy == "Public") {
      //           this.publicTools.push(res.data);
      //         }
      //         this.filterShowListByType();
      //       }
      //     });
    },

    getModelDetail() {
      let modelItemId = this.selectModel.id;
      let route = `/modelItem?id=${modelItemId}`;

      let url = window.location.href;
      let index = url.indexOf("toolTemplate");
      let urlPrefix = url.slice(0, index - 1);
      this.selectModelUrl = `${urlPrefix}${route}`;

      this.toolInfo.tool_url = this.selectModelUrl;
      this.toolInfo.name = this.selectModel.name;
      this.toolInfo.description = this.selectModel.des;
      console.log(this.toolInfo);
    },
    addCreateToolTag(tag) {
      if (tag != "") {
        this.toolInfo.categoryTag.push(tag);
        this.inputToolTag = "";
      }
    },
    deleteCreateToolTag(index) {
      this.toolInfo.categoryTag.splice(index, 1);
    }
  },
  props: {
    toolInfoGeneral: {
      type: Object
    }
  }

  //   watch: {
  //     modelItem(val) {
  //       this.selectModel = val;
  //       this.getModelDetail();
  //     }
  //   }
};
</script>
