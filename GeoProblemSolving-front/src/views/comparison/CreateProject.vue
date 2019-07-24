<template>
  <div class="project form">
    <h1>Create Comparison Projcet</h1>
    <div>
      <Form ref="project" :rules="createProjectRule" :model="project" class="projectForm">
        <!-- 选择类别 -->
        <FormItem prop="category" label="Category" :label-width="150">
          <RadioGroup v-model="project.category" style="width:80%">
            <Radio label="Terrestrial">Terrestrial System</Radio>
            <Radio label="Coastal">Coastal System</Radio>
            <Radio label="Marine">Marine System</Radio>
            <Radio label="Climate">Climate System</Radio>
            <Radio label="Ecological">Ecological System</Radio>
            <Radio label="Geological">Geological System</Radio>
            <Radio label="Human">Human-Activity</Radio>
            <Radio label="GISRS">GIS & RS</Radio>
            <Radio label="General">General</Radio>
          </RadioGroup>
        </FormItem>

        <FormItem prop="title" label="Title" :label-width="150">
          <Input v-model="project.title" placeholder="Enter Title ..."/>
        </FormItem>

        <FormItem prop="description" label="Description" :label-width="150">
          <div>
            <Input
              type="textarea"
              v-model="project.description"
              placeholder="Enter detailed introduction about this project"
            />
          </div>
        </FormItem>

        <FormItem prop="evaluationRules" label="Evaluation Rules" :label-width="150">
          <div>
            <Input
              type="textarea"
              v-model="project.evaluationRules"
              placeholder="Enter the evaluation rules of this project"
            />
          </div>
        </FormItem>

                <FormItem prop="modelList" label="Specify Model" :label-width="150">
          <Input
            v-model="modelTag"
            placeholder="Specify comparison model"
            style="width: 400px"
            @keyup.enter.native="addModel(modelTag)"
          />
          <Button
            icon="ios-add"
            type="dashed"
            size="small"
            @click="addModel(modelTag)"
            style="margin-left:2.5%"
          >Add Model</Button>
          <div>
            <Tag
              color="primary"
              v-for="(item,index) in this.project.modelList"
              :key="index"
              closable
              @on-close="deleteModel(index)"
            >{{item}}</Tag>
          </div>
        </FormItem>

        <FormItem prop="outputDataList" label="Specify Output Data" :label-width="150">
          <Input
            v-model="outputDataTag"
            placeholder="Specify output data"
            style="width: 400px"
            @keyup.enter.native="addData(outputDataTag)"
          />
          <Button
            icon="ios-add"
            type="dashed"
            size="small"
            @click="addData(outputDataTag)"
            style="margin-left:2.5%"
          >Add Data</Button>
          <div>
            <Tag
              color="primary"
              v-for="(item,index) in this.project.outputDataList"
              :key="index"
              closable
              @on-close="deleteData(index)"
            >{{item}}</Tag>
          </div>
        </FormItem>


        <FormItem prop="haveStandardInputData" label="Standard Input Data" :label-width="150">
          <RadioGroup v-model="project.standardInputData"  style="width:80%;margin-left:10px">
            <Radio label="Provide" value="Provide" title="The project creator needs to upload standard input data"></Radio>
            <Radio label="NotProvide" value="NotProvide" title="The project creator will not prepare standard input data.">Not Provide</Radio>
            <div style="color:cornflowerblue">To ensure the fairness of the comparison results, please provide standard input data.</div>
          </RadioGroup>
        </FormItem>
        <FormItem prop="haveEvaluatedData" label="Evaluated Data" :label-width="150">
          <RadioGroup v-model="project.evaluatedData"  style="width:80%;margin-left:10px">
            <Radio label="Provide" value='Provide' title="The project creator needs to upload evaluated data">            </Radio>
            <Radio label="NotProvide" value='NotProvide' title="The project creator will not prepare evaluated data.">Not Provide</Radio>
          </RadioGroup>
          <div style="color:cornflowerblue">Evaluated data will be used to verify the model's output data. Could be observation data.</div>
        </FormItem>

        <FormItem>
          <div class="inline_style">
            <Button type="success" @click='createProject("project")' class="create">Create</Button>
          </div>
        </FormItem>
      </Form>
    </div>
  </div>
</template>

<script>

export default {
    beforeRouteEnter: (to, from, next) => {
    next(vm=>{
      $.ajax({
        url: "/GeoProblemSolving/user/state",
        type: "GET",
        async: false,
        success: function (data) {
            if (!data) {
              vm.$store.commit("userLogout");
              vm.$router.push({ name: "Login" });
            }
        },
        error: function (err) {
            console.log("Get user state fail.");
        }
      });
    });
  },
  data(){
    return {
      project:{
        title:"",
        description:"",
        category:"",
        evaluationRules:"",
        //model
        modelList:[],
        //output data
        outputDataList:[],
        standardInputData:"NotProvide",
        evaluatedData:"NotProvide",
        startTime:"",
        endTime:"",
        location:"",
        timeInterval:"",
        scale:"",
        resolution:"",
      },
      createProjectRule:{
        title:[
          {
            required:true,
            message:"The title cannot be empty and no more than 200 characters",
            trigger:"blur",
            max:200
          }
        ],
        category:[
          {
            required:true,
            message: "Please select category",
            trigger: "change"
          }
        ],
        description:[
          {
            required: true,
            message: "The description cannot be empty and no more than 600 characters",
            trigger: "blur",
            max: 600
          }
        ],
        evaluationRules:[
          {
            required: true,
            message: "The evaluation rules cannot be empty and no more than 300 characters",
            trigger: "blur",
            max: 300
          }
        ]
      },
      //用来存储模型标签
      modelTag:"",
      //用来存储输出数据标签
      outputDataTag:"",
    }
  },
  methods:{
    createProject(name){
      this.$refs[name].validate(valid=>{
        if(valid){
          this.project["managerId"] = this.$store.getters.userId;
          console.log(this.project);
          this.$api.cmp_project.create(this.project)
            .then(res=>{
               this.$Message.info(res);
            }).catch(error=>{
              this.$Message.error(error);
            })
        }
      })
    },
    addModel(tag) {
      if(tag!=""){
        this.project.modelList.push(tag);
        this.modelTag = "";
      }
    },
    addData(tag){
      if(tag!=""){
        this.project.outputDataList.push(tag);
        this.outputDataTag = "";
      }
    },
    deleteModel(index){
      this.project.modelList.splice(index,1);
    },
    deleteData(index){
      this.project.outputDataList.splice(index,1);
    }
  }
};
</script>

<style scoped>

h1{
  margin-top: 2.5%;
  text-align: center
}
.projectForm {
  width: 60%;
  margin-top: 0.5%;
  margin-left: 20%;
  margin-right: 20%;
}

.create {
  width: 20%;
  margin-right: 40%;
  margin-left: 40%;
}

</style>
