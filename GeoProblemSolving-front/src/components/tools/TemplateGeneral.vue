<style scoped>
.modelList {
  height: 80px;
  margin-bottom: 5px;
  /* width: 400px; */
  /* padding: 1px 0; */
  /* border: 1px solid #cfcfcfb6;
  border-radius: 5px; */
  padding: 0%;
}
.modelList:hover {
  box-shadow: 0px 0px 3px 1px #2d72f1;
}

.modelTitle {
  /* margin-bottom: 5px; */
}
.modelName {
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.modelDes {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  font-style: italic;
}
.createToolBtn {
  text-align: center;
  margin: 15px 45%;
  font-weight: 800;
  height: 50px;
  font-size: 20px;
}
.selectInput {
  /* height: 60px; */
  font-size: 16px;
  /* background-color: rgb(111, 167, 204); */
}
</style>
<template>
  <div>
    <!-- <Col span="16" offset="4"> -->
    <Form
      ref="toolInfo"
      :model="toolInfo"
      :rules="toolInfoRule"
      :label-width="80"
      class="toolForm"
      label-position="left"
    >
      <FormItem label="Tool name:" prop="toolName" :label-width="140">
        <Input v-model="toolInfo.toolName" placeholder="Enter the toolName of your tool"></Input>
      </FormItem>

      <FormItem label="Tool Url:" prop="toolUrl" :label-width="140">
      
          <Input v-model="toolInfo.toolUrl" placeholder="Enter the url of your tool" />
        <!-- </div>
        <div v-else>
          <Input v-model="toolInfo.toolUrl">{{selectModel.md5}}</Input>
        </div> -->
        <!-- <div style="margin-top:5px">
          <Button type="info" target="https://geomodeling.njnu.edu.cn/modelItem/repository">Select Model From ...</Button>
        </div> -->
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
            v-for="(item, index) in this.toolInfo.categoryTag"
            :key="index"
            closable
            @on-close="deleteCreateToolTag(index)"
          >{{ item }}</Tag>
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

      <FormItem label="Detail:" prop="detail" :label-width="140">
        <tinymce ref="editor" v-model="toolInfo.detail" :height="300" />
      </FormItem>
    </Form>
    <!-- <Col span="8" offset="4">
      <Button type="success" class="createToolBtn" long @click="createTool">Create</Button>
    </Col> -->

    <!-- model modal -->
    <Modal v-model="modelModal" width="1000px">
      <p slot="header" style="color:#2d8cf0;text-align:center;font-size:20px">
        <Icon type="ios-information-circle"></Icon>
        <span>Choose the model exists</span>
      </p>
      <div>
        <Row style="margin:5px 0 10px">
          <Col class="searchModel">
            <Input search enter-button="Search" placeholder="Enter the model name ..." />
          </Col>
        </Row>
        <Row>
          <Col span="12" v-for="(model,index) in modelList" :key="index">
            <Card class="modelList" @click.native="selectModelFunction(model.name,model.md5)">
              <Row>
                <Col class="modelAvatar" span="3">
                  <avatar :username="model.name" :size="40" :rounded="false"></avatar>
                </Col>
                <Col class="modelInfo" span="21">
                  <Row>
                    <div class="modeTitle">
                      <Col span="22">
                        <div class="modelName">{{model.name}}</div>
                      </Col>
                      <Col span="2">
                        <Poptip placement="bottom" trigger="hover">
                          <Icon type="ios-arrow-down" size="16"></Icon>
                          <div slot="content" class="tipButton">
                            <div>
                              <Button
                                name="modelIntro"
                                @click="toModelIntro(model.relatedModelInfoId)"
                              >Model introduction</Button>
                            </div>
                            <div>
                              <Button
                                name="modelProcessIntro"
                                @click="toModelProcessIntro(model.id)"
                              >Model invoke</Button>
                            </div>
                          </div>
                        </Poptip>
                      </Col>
                    </div>
                  </Row>
                  <Row>
                    <div class="modelDes">{{model.des}}</div>
                  </Row>
                </Col>
              </Row>
            </Card>
          </Col>
        </Row>
        <Row style="text-align:center;margin-top:10px">
          <Page :current="2" :total="50" simple />
        </Row>
      </div>

      <div slot="footer" style="text-align:left">
        <Row>
          <Col span="4">
            <div>The model selected:</div>
          </Col>
          <Col span="20">
            <Tag class="selectInput" color="primary">{{selectModel.name}}</Tag>
          </Col>
        </Row>
      </div>
    </Modal>
  </div>
</template>
<script>
import tinymce from "./../tinymce";
import Avatar from "vue-avatar";
export default {
  components: {
    tinymce,
    Avatar
  },
  data() {
    return {
      toolInfo: {
        toolName: "",
        description: "",
        toolUrl: "",
        recomStep: [],
        categoryTag: [],
        privacy: "Private",
        detail: ""
      },
      toolInfoChange: false, //form表单内容是否改变
      // pageChange: false, //是否翻页
      // pageChangeSave: false,
      toolInfoRule: {
        toolName: [
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
      modelModal: false, //选择的模型模态框
      modelList: [
        {
          name: "SWAT_Model",
          des:
            "The Soil & Water Assessment Tool is a small watershed to river basin-scale model used to simulate the quality and quantity of surface and ground water and predict the environmental impact of land use, land management practices, and climate change.",
          md5: 1,
          relatedModelInfoId: 2
        },
        {
          name: "TaiHu_Fvcom",
          des:
            "FVCOM is a prognostic, unstructured-grid, finite-volume, free-surface, 3-D primitive equation coastal ocean circulation model developed by UMASSD-WHOI joint efforts.",
          md5: 1,
          relatedModelInfoId: 2
        },
        {
          name: "grid_tools_15-Reclassify_Grid_Values",
          des: "Reclassify Grid Values",
          md5: 1,
          relatedModelInfoId: 2
        },
        {
          name: "SWMM",
          des:
            "The EPA Storm Water Management Model (SWMM) is a dynamic rainfall-runoff simulation model used for single event or long-term (continuous) simulation of runoff quantity and quality from primarily urban areas(EPA SWMM Help).",
          md5: 1,
          relatedModelInfoId: 2
        },
        {
          name: "GCAM-CA",
          des:
            "The EPA Storm Water Management Model (SWMM) is a dynamic rainfall-runoff simulation model used for single event or long-term (continuous) simulation of runoff quantity and quality from primarily urban areas(EPA SWMM Help).",
          md5: 1,
          relatedModelInfoId: 2
        },
        {
          name: "Biome - Bio Geochemical Cycles",
          des:
            "The Biome-BGC (BioGeochemical Cycles) model simulates NPP for multiple biomes. Because NPP is computed as the difference between simulated GPP and autotrophic respiration, environmental controls operate on both the process of photosynthesis and respiration. Although nitrogen dynamics have been added, Biome-BGC relies primarily on the hydrologic cycle and how water availability controls C uptake and storage. The response of NPP to elevated CO2 is determined mainly by changes in transpiration associated with reduced leaf conductance, rather than feedbacks from nutrient cycling.",
          md5: 1,
          relatedModelInfoId: 2
        },
        {
          name: "DBH_QYP",
          des:
            "It is a model which can predict the individual-tree growth of diameter at breast height (DBH).",
          md5: 1,
          relatedModelInfoId: 2
        },

        {
          name: "Integrated Terrestrial Ecosystem Carbon-budget Model",
          des:
            "The InTEC model is a process-based biogeochemical C-budget model and considers all major C cycle components. This model adopts a distinct approach to simulate C components by combining (a) Farquhar’s leaf-level biochemical model , (b) a soil biochemical model CENTURY modified to include forest-specific C pools such as coarse roots and woody detritus, and (c) a set of empirical NPP and age relationships derived from forest growth and yield data.",
          md5: 1,
          relatedModelInfoId: 2
        }
      ], //所有的模型信息 List
      selectModel: {
        name: "",
        md5: ""
      },
      createToolFlag:null
    };
  },

  methods: {
    addCreateToolTag(tag) {
      if (tag != "") {
        this.toolInfo.categoryTag.push(tag);
        this.inputToolTag = "";
      }
    },

    deleteCreateToolTag(index) {
      this.toolInfo.categoryTag.splice(index, 1);
    },
    selectModelFunction(modelName, modelMd5) {
      console.log(modelName);
      console.log(modelMd5);
      this.selectModel.name = modelName;
      this.selectModel.md5 = modelMd5;
      this.toolInfo.toolUrl = modelMd5
    },

    // createTool(){
    //   console.log(this.toolInfo.toolName);
    //       let createToolForm = {};
    //       createToolForm["toolName"] = this.toolInfo.toolName;
    //       createToolForm["description"] = this.toolInfo.description;
    //       createToolForm["toolUrl"] = this.toolInfo.toolUrl;
    //       createToolForm["categoryTag"] = this.toolInfo.categoryTag;
    //       createToolForm["privacy"] = this.toolInfo.privacy;
    //       createToolForm["detail"] = this.toolInfo.detail;

    //       this.axios
    //         .post("/GeoProblemSolving/tool/create", createToolForm)
    //         .then(res => {
    //           // if(res.data == "Offline"){
    //           //   this.$store.commit("userLogout");
    //           //   this.$router.push({ name: "Login" });
    //           // }
    //           // else 
    //           if (res.data === "Fail") {
    //             this.$Message.error("Create tool fail.");
    //           } else {
    //             // this.createProjectId = res.data;
    //             // this.addHistoryEvent(this.createProjectId);
    //             console.log(res.data);
    //           }
    //         })
    //         .catch(err => {
    //           console.log(err);
    //         });
    // }
  },

  watch: {
    toolInfo: {
      handler(val) {
        this.$emit("generalInfo", this.toolInfo);
      },
      deep: true
    }

    // generalChange: {
    //   immediate: true, //初始化立即执行
    //   handler(val) {
    //     // this.toolInfoChange
    //     // if (this.toolInfoChange = true) {
    //     //   console.log(this.generalChange);
    //     //   this.pageChange = this.generalChange;
    //     //   console.log(this.pageChange);
    //     // } else {
    //     //   this.pageChange = false;
    //     // }
    //   }
    // }
  }
};
</script>
