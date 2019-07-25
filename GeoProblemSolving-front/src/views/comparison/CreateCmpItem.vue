<template>
  <div>
    <h1>Create Comparison Item</h1>
    <Form ref="item" :rules="createItemRule" :model="item" class="projectForm">
      <FormItem prop="title" label="Title" :label-width="150">
        <Input v-model="item.title" placeholder="Enter Title ..." />
      </FormItem>

      <FormItem prop="description" label="Description" :label-width="150">
        <div>
          <Input type="textarea" v-model="item.description"
            placeholder="Enter detailed introduction about this project" />
        </div>
      </FormItem>

      <FormItem prop="evaluationRules" label="Evaluation Rules" :label-width="150">
        <div>
          <Input type="textarea" v-model="item.evaluationRules"
            placeholder="Enter the evaluation rules of this project" />
        </div>
      </FormItem>

      <FormItem prop="modelList" label="Specify Model" :label-width="150">
        <Input v-model="modelTag" placeholder="Specify comparison model" style="width: 400px"
          @keyup.enter.native="addModel(modelTag)" @on-blur="addModel(modelTag)"/>
        <Button icon="ios-add" type="dashed" size="small" @click="addModel(modelTag)" style="margin-left:2.5%">Add
          Model</Button>
        <div>
          <Tag color="primary" v-for="(tag,index) in this.item.modelList" :key="index" closable
            @on-close="deleteModel(index)">{{tag}}</Tag>
        </div>
      </FormItem>

      <FormItem prop="outputDataList" label="Specify Output Data" :label-width="150">
        <Input v-model="outputDataTag" placeholder="Specify output data" style="width: 400px"
          @keyup.enter.native="addData(outputDataTag)" @on-blur="addData(outputDataTag)"/>
        <Button icon="ios-add" type="dashed" size="small" @click="addData(outputDataTag)" style="margin-left:2.5%">Add
          Data</Button>
        <div>
          <Tag color="primary" v-for="(tag,index) in this.item.outputDataList" :key="index" closable
            @on-close="deleteData(index)">{{tag}}</Tag>
        </div>
      </FormItem>

      <FormItem prop="haveStandardInputData" label="Standard Input Data" :label-width="150">
        <RadioGroup v-model="item.standardInputData" style="width:80%;margin-left:10px">
          <Radio label="Provide" value="Provide" title="The project creator needs to upload standard input data">
          </Radio>
          <Radio label="NotProvide" value="NotProvide"
            title="The project creator will not prepare standard input data.">
            Not Provide</Radio>
          <div style="color:cornflowerblue">To ensure the fairness of the comparison results, please provide standard
            input data.</div>
        </RadioGroup>
      </FormItem>
      <FormItem prop="haveEvaluatedData" label="Evaluated Data" :label-width="150">
        <RadioGroup v-model="item.evaluatedData" style="width:80%;margin-left:10px">
          <Radio label="Provide" value='Provide' title="The project creator needs to upload evaluated data"> </Radio>
          <Radio label="NotProvide" value='NotProvide' title="The project creator will not prepare evaluated data.">Not
            Provide</Radio>
          <div style="color:cornflowerblue">Evaluated data will be used to verify the model's output data. Could be
            observation data.</div>
        </RadioGroup>

      </FormItem>

      <FormItem>
        <div class="inline_style">
          <Button type="success" @click='createItem("item")' class="create">Create</Button>
        </div>
      </FormItem>
    </Form>
  </div>

</template>
<script>
export default {
  beforeRouteEnter: (to, from, next) => {
    next(vm => {
      $.ajax({
        url: "/GeoProblemSolving/user/state",
        type: "GET",
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
  name: "create-cmp-item",
  created:function(){
    let projectId = this.$route.params.id;
    this.item.projectId = projectId;
  },
  data() {
    return {
      item: {
        title: "",
        description: "",
        evaluationRules: "",
        //model
        modelList: [],
        //output data
        outputDataList: [],
        standardInputData: "Provide",
        evaluatedData: "Provide"
      },
      createItemRule: {
        title: [
          {
            required: true,
            message:
              "The title cannot be empty and no more than 200 characters",
            trigger: "blur",
            max: 200
          }
        ],
        category: [
          {
            required: true,
            message: "Please select category",
            trigger: "change"
          }
        ],
        description: [
          {
            required: true,
            message:
              "The description cannot be empty and no more than 600 characters",
            trigger: "blur",
            max: 600
          }
        ],
        evaluationRules: [
          {
            required: true,
            message:
              "The evaluation rules cannot be empty and no more than 300 characters",
            trigger: "blur",
            max: 300
          }
        ]
      },
      //用来存储模型标签
      modelTag: "",
      //用来存储输出数据标签
      outputDataTag: "",
    };
  },
  methods: {
    createItem(name) {
      this.$refs[name].validate(valid=>{
        if(valid){
          this.item["managerId"] = this.$store.getters.userId;
          this.$api.cmp_item.create(this.item)
            .then(res=>{
              this.$router.back(-1)
              console.log(res);
            })
            .catch(err=>{
              this.$Message.error(err);
            })
        }
      })
      console.log(this.item);
    },
    addModel(tag) {
      if (tag != "") {
        this.item.modelList.push(tag);
        this.modelTag = "";
      }
    },
    addData(tag) {
      if (tag != "") {
        this.item.outputDataList.push(tag);
        this.outputDataTag = "";
      }
    },
    deleteModel(index) {
      this.item.modelList.splice(index, 1);
    },
    deleteData(index) {
      this.item.outputDataList.splice(index, 1);
    },
    blur(){
      console.log("blur");
    },
    focus(){
      console.log("focus");
    }
  }
};
</script>
<style scoped>
h1 {
  margin-top: 2.5%;
  text-align: center;
}
.projectForm {
  width: 60%;
  margin: auto;
  margin-top: 20px;
}

.create {
  width: 20%;
  margin-right: 40%;
  margin-left: 40%;
}
</style>