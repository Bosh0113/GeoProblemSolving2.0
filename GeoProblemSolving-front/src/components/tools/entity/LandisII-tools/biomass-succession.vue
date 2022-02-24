<template>
  <div>
    <div id="collab-tool-head"></div>
    <div id="collab-tool-sidebar"></div>
    <div id="collab-tool-content">
      <div id="edit-mask" title="The other participant is operating."></div>

      <vue-scroll :ops="ops" style="height: calc(100vh - 40px)">
        <!-- coding for your tools // begin-->
        <Divider orientation="left">REQUIRED INPUTS</Divider>
        <!-- Timestep -->
        <div style="margin: 10px 20px">
          <Label class="labelTile">Timestep:</Label>
          <Input
            v-model="timeStep"
            placeholder="Enter the time step..."
            style="width: 100px"
          />
        </div>
        <!-- SeedingAlgorithm  -->
        <div style="margin: 10px 20px">
          <Label class="labelTile">SeedingAlgorithm:</Label>
          <Select v-model="seedingAlgorithm" style="width: 200px">
            <Option value="WardSeedDispersal">WardSeedDispersal</Option>
            <Option value="NoDispersal">NoDispersal</Option>
            <Option value="UniversalDispersal">UniversalDispersal</Option>
          </Select>
        </div>
        <!-- InitialCommunities and InitialCommunitiesMap -->
        <div style="margin: 10px 20px">
          <div>
            <Button
              type="info"
              style="width: 200px"
              :loading="boolInitialCommunities"
              @click="selectInitialCommunities"
            >
              <span v-if="!boolInitialCommunities"
                >Select initial communities</span
              >
              <span v-else>Importing data...</span>
            </Button>
            <Label class="labelTile" style="margin-left: 20px"
              >InitialCommunities:
              {{ initialCommunities.name + initialCommunities.suffix }}</Label
            >
            <Label class="labelTile" style="margin-left: 20px"
              >InitialCommunitiesMap:
              {{
                initialCommunitiesMap.name + initialCommunitiesMap.suffix
              }}</Label
            >
          </div>
        </div>
        <!-- ClimateConfigFile -->
        <div style="margin: 10px 20px">
          <Button
            type="info"
            style="width: 200px"
            :loading="boolClimateConfigFile"
            @click="selectClimateConfigFile"
          >
            <span v-if="!boolClimateConfigFile"
              >Select climate config file</span
            >
            <span v-else>Importing data...</span>
          </Button>
          <Label class="labelTile" style="margin-left: 20px"
            >ClimateConfigFile:
            {{ climateConfigFile.name + climateConfigFile.suffix }}</Label
          >
        </div>
        <!-- CalibrateMode  -->
        <div style="margin: 10px 20px">
          <Label class="labelTile">CalibrateMode:</Label>
          <i-switch @on-change="calibrateModeChange">
            <span slot="open">Yes</span>
            <span slot="close">No</span>
          </i-switch>
        </div>
        <!-- SpinUpMortalityFraction  -->
        <div style="margin: 10px 20px">
          <Label class="labelTile">SpinUpMortalityFraction:</Label>
          <Input
            v-model="spinUpMortalityFraction"
            placeholder="Enter the spin up mortality fraction..."
            style="width: 100px"
          />
        </div>

        <Divider orientation="left">LIFE HISTORY PARAMETERS</Divider>
        <!-- MinRelativeBiomass -->
        <div style="margin: 10px 20px">
          <div style="display: flex">
            <Label class="labelTile">MinRelativeBiomass:</Label>
            <div style="margin-bottom: 5px; margin-left: 10px">
              <Button @click="addMRB()">Add</Button>
              <Button @click="removeMRB()">Remove</Button>
            </div>
          </div>
          <Table
            border
            ref="selection"
            :columns="shadeClasses"
            :data="ecoregionsMRB"
            @on-selection-change="selectMRB"
          >
            <template slot-scope="{ row }" slot="name">
              <strong>{{ row.name }}</strong>
            </template>
          </Table>
          <div style="margin-top: 5px">
            <Button @click="handleSelectAllMRB(true)">Set all selected</Button>
            <Button @click="handleSelectAllMRB(false)">Cancel all selected</Button>
          </div>
        </div>
        <!-- SufficientLight -->
        <div style="margin: 10px 20px">
          <div style="display: flex">
            <Label class="labelTile">SufficientLight:</Label>
            <div style="margin-bottom: 5px; margin-left: 10px">
              <Button @click="editSL()" :disabled="this.selectedSL.length != 1"
                >Edit</Button
              >
            </div>
          </div>
          <Table
            border
            ref="selection"
            :columns="lightConditions"
            :data="SEP"
            @on-selection-change="selectSL"
          >
          </Table>
        </div>
        <!-- SpeciesParameters -->
        <div style="margin: 10px 20px">
          <div style="display: flex">
            <Label class="labelTile">SpeciesParameters:</Label>
            <div style="margin-bottom: 5px; margin-left: 10px">
              <Button @click="addSP()">Add</Button>
              <Button @click="removeSP()">Remove</Button>
            </div>
          </div>
          <Table
            border
            ref="selection"
            :columns="speciesInfo"
            :data="speciesSP"
            @on-selection-change="selectSP"
          >
          </Table>
          <div style="margin-top: 5px">
            <Button @click="handleSelectAllSP(true)">Set all selected</Button>
            <Button @click="handleSelectAllSP(false)">Cancel all selected</Button>
          </div>
        </div>
        <!-- EcoregionParameters -->
        <div style="margin: 10px 20px">
          <div style="display: flex">
            <Label class="labelTile">EcoregionParameters:</Label>
            <div style="margin-bottom: 5px; margin-left: 10px">
              <Button @click="addEP()">Add</Button>
              <Button @click="removeEP()">Remove</Button>
            </div>
          </div>
          <Table
            border
            ref="selection"
            :columns="ecoregionParameters"
            :data="AET"
            @on-selection-change="selectEP"
          >
          </Table>
          <div style="margin-top: 5px">
            <Button @click="handleSelectAllEP(true)">Set all selected</Button>
            <Button @click="handleSelectAllEP(false)">Cancel all selected</Button>
          </div>
        </div>
        <!-- DynamicInputFile   -->
        <div style="margin: 10px 20px">
          <Button
            type="info"
            style="width: 250px"
            :loading="boolDynamicInputFile"
            @click="selectDynamicInputFile"
          >
            <span v-if="!boolDynamicInputFile">Select dynamic input file</span>
            <span v-else>Importing data...</span>
          </Button>
          <Label class="labelTile" style="margin-left: 20px"
            >DynamicInputFile:
            {{ dynamicInputFile.name + dynamicInputFile.suffix }}</Label
          >
        </div>
        <!-- FireReductionParameters -->
        <div style="margin: 10px 20px">
          <div style="display: flex">
            <Label class="labelTile">FireReductionParameters:</Label>
            <div style="margin-bottom: 5px; margin-left: 10px">
              <Button
                @click="editFRP()"
                :disabled="this.selectedFRP.length != 1"
                >Edit</Button
              >
            </div>
          </div>
          <Table
            border
            ref="selection"
            :columns="fireReductionParams"
            :data="fireRows"
            @on-selection-change="selectFRP"
          >
          </Table>
        </div>
        <!-- HarvestReductionParameters -->
        <div style="margin: 10px 20px">
          <div style="display: flex">
            <Label class="labelTile">HarvestReductionParameters:</Label>
            <div style="margin-bottom: 5px; margin-left: 10px">
              <Button @click="addHRP()">Add</Button>
              <Button @click="removeHRP()">Remove</Button>
            </div>
          </div>
          <Table
            border
            ref="selection"
            :columns="harvestReductionParams"
            :data="harvestRows"
            @on-selection-change="selectHRP"
          >
          </Table>
          <div style="margin-top: 5px">
            <Button @click="handleSelectAllHRP(true)">Set all selected</Button>
            <Button @click="handleSelectAllHRP(false)">Cancel all selected</Button>
          </div>
        </div>
        <Divider></Divider>
        <Button
          @click="submit()"
          type="success"
          style="margin: 0 20px 20px; width: 200px"
          >Submit</Button
        >
        <Button
          @click="resetAll()"
          type="warning"
          style="margin: 0 20px 20px; width: 200px"
          >Clear All</Button
        >
      </vue-scroll>
      <!-- // end -->
      <!-- MinRelativeBiomass Modal -->
      <Modal
        v-model="addMinRelativeBiomassModal"
        width="600"
        title="Add the minimum relative biomass for shade classes 1 - 5"
      >
        <Form
          ref="minRelativeBiomassVal"
          :model="minRelativeBiomassVal"
          :rules="ruleValidate1"
          :label-width="100"
        >
          <div style="display: flex">
            <FormItem label="Name" prop="ecoregionName">
              <Input
                v-model="minRelativeBiomassVal.ecoregionName"
                placeholder="Enter the ecoregion name"
              ></Input>
            </FormItem>
            <FormItem label="Class 1" prop="class1" style="margin-left: 20px">
              <Input
                v-model="minRelativeBiomassVal.class1"
                placeholder="Enter the minimum relative biomass for shade class 1"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Class 2" prop="class2">
              <Input
                v-model="minRelativeBiomassVal.class2"
                placeholder="Enter the minimum relative biomass for shade class 2"
              ></Input>
            </FormItem>
            <FormItem label="Class 3" prop="class3" style="margin-left: 20px">
              <Input
                v-model="minRelativeBiomassVal.class3"
                placeholder="Enter the minimum relative biomass for shade class 3"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Class 4" prop="class4">
              <Input
                v-model="minRelativeBiomassVal.class4"
                placeholder="Enter the minimum relative biomass for shade class 4"
              ></Input>
            </FormItem>
            <FormItem label="Class 5" prop="class5" style="margin-left: 20px">
              <Input
                v-model="minRelativeBiomassVal.class5"
                placeholder="Enter the minimum relative biomass for shade class 5"
              ></Input>
            </FormItem>
          </div>
        </Form>
        <div slot="footer">
          <Button
            type="primary"
            @click="handleSubmitMRB('add', 'minRelativeBiomassVal')"
            >Submit</Button
          >
          <Button
            @click="handleReset('minRelativeBiomassVal')"
            style="margin-left: 8px"
            >Reset</Button
          >
        </div>
      </Modal>
      <!-- SufficientLight -->
      <Modal
        v-model="editSufficientLightModal"
        width="750"
        title="Edit the probability of establishment for each species shade tolerance class"
      >
        <Form
          ref="sufficientLightVal"
          :model="sufficientLightVal"
          :rules="ruleValidate2"
          :label-width="150"
        >
          <div style="display: flex">
            <FormItem label="Shade class" prop="shadeclass">
              <Input v-model="sufficientLightVal.shadeclass" readonly></Input>
            </FormItem>
            <FormItem
              label="Light condition 0"
              prop="light0"
              style="margin-left: 20px"
            >
              <Input
                v-model="sufficientLightVal.light0"
                placeholder="Enter the light condition 0"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Light condition 1" prop="light1">
              <Input
                v-model="sufficientLightVal.light1"
                placeholder="Enter the light condition 1"
              ></Input>
            </FormItem>
            <FormItem
              label="Light condition 2"
              prop="light2"
              style="margin-left: 20px"
            >
              <Input
                v-model="sufficientLightVal.light2"
                placeholder="Enter the light condition 2"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Light condition 3" prop="light3">
              <Input
                v-model="sufficientLightVal.light3"
                placeholder="Enter the light condition 3"
              ></Input>
            </FormItem>
            <FormItem
              label="Light condition 4"
              prop="light4"
              style="margin-left: 20px"
            >
              <Input
                v-model="sufficientLightVal.light4"
                placeholder="Enter the light condition 4"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Light condition 5" prop="light5">
              <Input
                v-model="sufficientLightVal.light5"
                placeholder="Enter the light condition 5"
              ></Input>
            </FormItem>
          </div>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmitSL('sufficientLightVal')"
            >Submit</Button
          >
        </div>
      </Modal>
      <!-- SpeciesParameters -->
      <Modal
        v-model="addSpeciesParametersModal"
        width="800"
        title="Add the species’ biomass parameters"
      >
        <Form
          ref="speciesParametersVal"
          :model="speciesParametersVal"
          :rules="ruleValidate3"
          :label-width="150"
        >
          <div style="display: flex">
            <FormItem label="Name" prop="Name">
              <Input
                v-model="speciesParametersVal.name"
                placeholder="Enter the species name"
              ></Input>
            </FormItem>
            <FormItem
              label="Leaf longevity"
              prop="leaf_longevity"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesParametersVal.leaf_longevity"
                placeholder="Enter the leaf longevity"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Woody decay rate" prop="woody_decay_rate">
              <Input
                v-model="speciesParametersVal.woody_decay_rate"
                placeholder="Enter the woody decay rate"
              ></Input>
            </FormItem>
            <FormItem
              label="Mortality"
              prop="mortality"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesParametersVal.mortality"
                placeholder="Enter the mortality curve – shape parameter"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Growth" prop="growth">
              <Input
                v-model="speciesParametersVal.growth"
                placeholder="Enter the growth curve – shape parameter"
              ></Input>
            </FormItem>
            <FormItem
              label="Leaf lignin"
              prop="leaf_lignin"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesParametersVal.leaf_lignin"
                placeholder="Enter the leaf lignin"
              ></Input>
            </FormItem>
          </div>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmitSP('speciesParametersVal')"
            >Submit</Button
          >
          <Button
            @click="handleReset('speciesParametersVal')"
            style="margin-left: 8px"
            >Reset</Button
          >
        </div>
      </Modal>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      ops: {
        bar: {
          background: "#808695",
        },
      },
      // basic info
      activityInfo: {},
      userInfo: {},
      resources: [],
      //
      timeStep: 10,
      seedingAlgorithm: "WardSeedDispersal",
      boolInitialCommunities: false,
      initialCommunities: {},
      initialCommunitiesMap: {},
      boolClimateConfigFile: false,
      climateConfigFile: {},
      calibrateMode: "No",
      spinUpMortalityFraction: 0.0,

      // MinRelativeBiomass data
      shadeClasses: [
        {
          type: "selection",
          width: 60,
          align: "center",
        },
        {
          title: "Ecoregions\\Shade",
          key: "ecoregionName",
        },
        {
          title: "1",
          key: "class1",
        },
        {
          title: "2",
          key: "class2",
        },
        {
          title: "3",
          key: "class3",
        },
        {
          title: "4",
          key: "class4",
        },
        {
          title: "5",
          key: "class5",
        },
      ],
      ecoregionsMRB: [],
      minRelativeBiomassVal: {
        ecoregionName: "",
        class1: "",
        class2: "",
        class3: "",
        class4: "",
        class5: "",
      },
      ruleValidate1: {
        ecoregionName: [
          {
            required: true,
            message: "The ecoregion name cannot be empty",
            trigger: "blur",
          },
        ],
        class1: [
          {
            required: true,
            message:
              "The minimum relative biomass for shade class 1 cannot be empty",
            trigger: "blur",
          },
        ],
        class2: [
          {
            required: true,
            message:
              "The minimum relative biomass for shade class 2 cannot be empty",
            trigger: "blur",
          },
        ],
        class3: [
          {
            required: true,
            message:
              "The minimum relative biomass for shade class 3 cannot be empty",
            trigger: "blur",
          },
        ],
        class4: [
          {
            required: true,
            message:
              "The minimum relative biomass for shade class 4 cannot be empty",
            trigger: "blur",
          },
        ],
        class5: [
          {
            required: true,
            message:
              "The minimum relative biomass for shade class 5 cannot be empty",
            trigger: "blur",
          },
        ],
      },
      selectedMRB: [],
      addMinRelativeBiomassModal: false,
      // SufficientLight data
      lightConditions: [
        {
          type: "selection",
          width: 60,
          align: "center",
        },
        {
          title: "Shade\\SEP",
          key: "shadeclass",
        },
        {
          title: "0",
          key: "light0",
        },
        {
          title: "1",
          key: "light1",
        },
        {
          title: "2",
          key: "light2",
        },
        {
          title: "3",
          key: "light3",
        },
        {
          title: "4",
          key: "light4",
        },
        {
          title: "5",
          key: "light5",
        },
      ],
      SEP: [
        {
          shadeclass: 1,
          light0: 0.0,
          light1: 0.0,
          light2: 0.0,
          light3: 0.0,
          light4: 0.0,
          light5: 0.0,
        },
        {
          shadeclass: 2,
          light0: 0.0,
          light1: 0.0,
          light2: 0.0,
          light3: 0.0,
          light4: 0.0,
          light5: 0.0,
        },
        {
          shadeclass: 3,
          light0: 0.0,
          light1: 0.0,
          light2: 0.0,
          light3: 0.0,
          light4: 0.0,
          light5: 0.0,
        },
        {
          shadeclass: 4,
          light0: 0.0,
          light1: 0.0,
          light2: 0.0,
          light3: 0.0,
          light4: 0.0,
          light5: 0.0,
        },
        {
          shadeclass: 5,
          light0: 0.0,
          light1: 0.0,
          light2: 0.0,
          light3: 0.0,
          light4: 0.0,
          light5: 0.0,
        },
      ],
      sufficientLightVal: {
        shadeclass: 1,
        light0: 0.0,
        light1: 0.0,
        light2: 0.0,
        light3: 0.0,
        light4: 0.0,
        light5: 0.0,
      },
      ruleValidate2: {
        light0: [
          {
            required: true,
            message: "The SEP at site-level light condition 0 cannot be empty",
            trigger: "blur",
          },
        ],
        light1: [
          {
            required: true,
            message: "The SEP at site-level light condition 1 cannot be empty",
            trigger: "blur",
          },
        ],
        light2: [
          {
            required: true,
            message: "The SEP at site-level light condition 2 cannot be empty",
            trigger: "blur",
          },
        ],
        light3: [
          {
            required: true,
            message: "The SEP at site-level light condition 3 cannot be empty",
            trigger: "blur",
          },
        ],
        light4: [
          {
            required: true,
            message: "The SEP at site-level light condition 4 cannot be empty",
            trigger: "blur",
          },
        ],
        light5: [
          {
            required: true,
            message: "The SEP at site-level light condition 5 cannot be empty",
            trigger: "blur",
          },
        ],
      },
      selectedSL: {},
      editSufficientLightModal: false,
      // SpeciesParameters data
      speciesInfo: [
        {
          type: "selection",
          width: 60,
          align: "center",
        },
        {
          title: "Species",
          key: "name",
        },
        {
          title: "Leaf Longevity",
          key: "leaf_longevity",
        },
        {
          title: "Woody Decay Rate",
          key: "woody_decay_rate",
        },
        {
          title: "Mortality",
          key: "mortality",
        },
        {
          title: "Growth",
          key: "growth",
        },
        {
          title: "Leaf Lignin",
          key: "leaf_lignin",
        },
      ],
      speciesSP: [
        {
          name: "Example",
          leaf_longevity: 3,
          woody_decay_rate: 0.1,
          mortality: 10,
          growth: 0.25,
          leaf_lignin: 0.252,
        },
      ],
      speciesParametersVal: {
        name: "",
        leaf_longevity: 0,
        woody_decay_rate: 0,
        mortality: 0,
        growth: 0,
        leaf_lignin: 0,
      },
      ruleValidate3: {
        name: [
          {
            required: true,
            message: "The species name cannot be empty",
            trigger: "blur",
          },
        ],
        leaf_longevity: [
          {
            required: true,
            message: "The leaf longevity cannot be empty",
            trigger: "blur",
          },
        ],
        woody_decay_rate: [
          {
            required: true,
            message: "The woody decay rate cannot be empty",
            trigger: "blur",
          },
        ],
        mortality: [
          {
            required: true,
            message: "The mortality parameter cannot be empty",
            trigger: "blur",
          },
        ],
        growth: [
          {
            required: true,
            message: "The growth parameter cannot be empty",
            trigger: "blur",
          },
        ],
        leaf_lignin: [
          {
            required: true,
            message: "The leaf lignin cannot be empty",
            trigger: "blur",
          },
        ],
      },
      selectedSP: [],
      addSpeciesParametersModal: false,
      // EcoregionParameters data
      ecoregionParameters: [
        {
          type: "selection",
          width: 60,
          align: "center",
        },
        {
          title: "Ecoregions\\AET",
          key: "shadeclass",
        },
        {
          title: "AET",
          key: "light0",
        },
      ],
      AET: [],
      selectedEP: [],
      //
      boolDynamicInputFile: false,
      dynamicInputFile: {},
      // FireReductionParameters data
      fireReductionParams: [
        {
          type: "selection",
          width: 60,
          align: "center",
        },
        {
          title: "Fire Severity",
          key: "severity",
        },
        {
          title: "Wood Reduction",
          key: "wood_reduction",
        },
        {
          title: "Litter Reduction",
          key: "litter_reduction",
        },
      ],
      fireRows: [
        {
          severity: 1,
          wood_reduction: 0.0,
          litter_reduction: 0.0,
        },
        {
          severity: 2,
          wood_reduction: 0.0,
          litter_reduction: 0.0,
        },
        {
          severity: 3,
          wood_reduction: 0.0,
          litter_reduction: 0.0,
        },
      ],
      selectedFRP: {},
      editFireReductionParametersModal: false,
      // HarvestReductionParameters data
      harvestReductionParams: [
        {
          type: "selection",
          width: 60,
          align: "center",
        },
        {
          title: "Prescription Name",
          key: "name",
        },
        {
          title: "Dead Wood Reduction",
          key: "wood_reduction",
        },
        {
          title: "Dead Litter Reduction",
          key: "litter_reduction",
        },
        {
          title: "Cohort Wood Removal",
          key: "wood_removal",
        },
        {
          title: "Cohort Leaf Removal",
          key: "leaf_romval",
        },
      ],
      harvestRows: [],
      selectedHRP: [],
      addHarvestReductionParametersModal: false,
    };
  },
  mounted() {
    // 加载协同组件
    loadCollabComponent();
    this.getStepInfo();
  },
  methods: {
    getStepInfo() {
      if (componentStatus) {
        // 获取数据
        this.activityInfo = activityInfo;
        this.userInfo = userInfo;
        this.resources = resources;

        // 绑定函数
        buildSocketChannel(
          this.getSocketOperation,
          this.getSocketData,
          this.getSocketComputation
        );
        loadResChannel = this.loadResources;
      } else {
        let _this = this;
        setTimeout(function () {
          _this.getStepInfo();
        }, 1000);
      }
    },
    getSocketComputation(data) {},
    getClimateConfigFile(data) {},
    getSocketData(data) {},
    loadResources(resList) {
      for (let i = 0; i < resList.length; i++) {
        // your function
        if (this.boolInitialCommunities) {
          this.initialCommunities = resList[0];
          this.initialCommunitiesMap = resList[1];
          this.boolInitialCommunities = true;
        } else if (this.boolClimateConfigFile) {
          this.climateConfigFile = resList[0];
          this.boolClimateConfigFile = true;
        } else if (this.boolDynamicInputFile) {
          this.dynamicInputFile = resList[0];
          this.boolDynamicInputFile = true;
        }
        //

        let send_content = {
          type: "resource",
          sender: this.userInfo.userId,
          behavior: "select",
          content: this.selectData,
        };
        sendCustomOperation(send_content);
      }
    },
    selectInitialCommunities() {
      this.boolInitialCommunities = !this.boolInitialCommunities;
      this.boolClimateConfigFile = false;
      this.boolDynamicInputFile = false;
    },
    selectClimateConfigFile() {
      this.boolClimateConfigFile = !this.boolClimateConfigFile;
      this.boolInitialCommunities = false;
      this.boolDynamicInputFile = false;
    },
    selectDynamicInputFile() {
      this.boolDynamicInputFile = !this.boolDynamicInputFile;
      this.boolInitialCommunities = false;
      this.boolClimateConfigFile = false;
    },
    calibrateModeChange(status) {
      if (status) {
        this.calibrateMode = "Yes";
      } else {
        this.calibrateMode = "No";
      }
    },

    // MinRelativeBiomass method
    addMRB() {
      this.minRelativeBiomassVal = {
        ecoregionName: "",
        class1: "",
        class2: "",
        class3: "",
        class4: "",
        class5: "",
      };
      this.addMinRelativeBiomassModal = true;
    },
    removeMRB() {
      for (let i = 0; i < this.selectedMRB.length; i++) {
        for (let j = 0; j < this.ecoregionsMRB.length; j++) {
          if (this.selectedMRB[i].name === this.ecoregionsMRB[j].name) {
            this.ecoregionsMRB.splice(j, 1);
            break;
          }
        }
      }
    },
    selectMRB(selection) {
      this.selectedMRB = Object.assign([], selection);
    },
    handleSelectAllMRB(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedMRB = Object.assign([], this.ecoregionsMRB);
      } else {
        this.selectedMRB = [];
      }
    },
    handleSelectAllMRB(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedMRB = Object.assign([], this.ecoregionsMRB);
      } else {
        this.selectedMRB = [];
      }
    },
    handleSubmitMRB(behavior, name) {
      let _this = this;
      this.$refs[name].validate((valid) => {
        if (valid) {
          let row = this.arrayContains(
            _this.ecoregionsMRB,
            _this.minRelativeBiomassVal
          );
          if (behavior === "add") {
            if (row >= 0) {
              this.$Message.error("Duplicate ecoregion information!");
            } else {
              _this.ecoregionsMRB.push(_this.minRelativeBiomassVal);
              _this.addMinRelativeBiomassModal = false;
            }
          }
        } else {
          this.$Message.error(
            "Please provide complete the minimum relative biomass for shade classes 1 - 5."
          );
        }
      });
    },

    // SufficientLight method
    editSL() {
      this.sufficientLightVal = this.selectedSL[0];
      this.editSufficientLightModal = true;
    },
    selectSL(selection) {
      this.selectedSL = Object.assign([], selection);
    },
    handleSelectAllSL(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedSL = Object.assign([], this.SEP);
      } else {
        this.selectedSL = [];
      }
    },
    handleSubmitSL(name) {
      let _this = this;
      this.$refs[name].validate((valid) => {
        if (valid) {
          let row = this.arrayContains(_this.SEP, _this.sufficientLightVal);
          if (row >= 0) {
            _this.SEP.splice(row, 1, _this.sufficientLightVal);
            _this.editSufficientLightModal = false;
          } else {
            this.$Message.error("Information error!");
          }
        } else {
          this.$Message.error(
            "Please provide complete the probability of establishment for each species shade tolerance class."
          );
        }
      });
    },

    // SpeciesParameters method
    addSP() {
      this.speciesParametersVal = {
        name: "",
        leaf_longevity: 0,
        woody_decay_rate: 0,
        mortality: 0,
        growth: 0,
        leaf_lignin: 0,
      };
      this.addSpeciesParametersModal = true;
    },
    removeSP() {
      for (let i = 0; i < this.selectedSP.length; i++) {
        for (let j = 0; j < this.speciesSP.length; j++) {
          if (this.selectedSP[i].name === this.speciesSP[j].name) {
            this.speciesSP.splice(j, 1);
            break;
          }
        }
      }
    },
    selectSP(selection) {
      this.selectedSP = Object.assign([], selection);
    },
    handleSelectAllSP(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedSP = Object.assign([], this.speciesSP);
      } else {
        this.selectedSP = [];
      }
    },
    handleSelectAllSP(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedSP = Object.assign([], this.speciesSP);
      } else {
        this.selectedSP = [];
      }
    },
    handleSubmitSP(name) {
      let _this = this;
      this.$refs[name].validate((valid) => {
        if (valid) {
          let row = this.arrayContains(
            _this.speciesSP,
            _this.speciesParametersVal
          );
          if (row >= 0) {
            this.$Message.error("Duplicate species information!");
          } else {
            _this.speciesSP.push(_this.speciesParametersVal);
            _this.addSpeciesParametersModal = false;
          }
        } else {
          this.$Message.error(
            "Please provide complete the minimum relative biomass for shade classes 1 - 5."
          );
        }
      });
    },

    // EcoregionParameters method
    addEP() {},
    removeEP() {
      this.AET.splice(index, 1);
    },
    selectEP(selection) {
      this.selectedEP = Object.assign([], selection);
    },
    handleSelectAllEP(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedEP = Object.assign([], this.AET);
      } else {
        this.selectedEP = [];
      }
    },

    // FireReductionParameters method
    editFRP() {},
    selectFRP(selection) {
      this.selectedFRP = Object.assign([], selection);
    },
    handleSelectAllFRP(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedFRP = Object.assign([], this.fireRows);
      } else {
        this.selectedFRP = [];
      }
    },

    // HarvestReductionParameters method
    addHRP() {},
    removeHRP() {
      this.harvestRows.splice(index, 1);
    },
    handleSelectAllHRP(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectedMRB = Object.assign([], this.harvestRows);
      } else {
        this.selectedMRB = [];
      }
    },
    selectHRP(selection) {
      this.selectedHRP = Object.assign([], selection);
    },

    //common
    handleReset(name) {
      this.$refs[name].resetFields();
    },
    arrayContains(array, data) {
      var i = array.length;
      while (i--) {
        if (
          (array[i].shadeclass != undefined &&
            array[i].shadeclass === data.shadeclass) ||
          (array[i].ecoregionName != undefined &&
            array[i].ecoregionName === data.ecoregionName) ||
          (array[i].name != undefined && array[i].name === data.name)
        ) {
          return i;
        }
      }
      return i;
    },
  },
};
</script>
<style scoped>
.labelTile {
  font-size: larger;
  font-weight: bold;
  margin: 5px 0;
}
</style>