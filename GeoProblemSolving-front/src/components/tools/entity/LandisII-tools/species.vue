<template>
  <div>
    <div id="collab-tool-head"></div>
    <div id="collab-tool-sidebar"></div>
    <div id="collab-tool-content">
      <div id="edit-mask" title="The other participant is operating."></div>

      <!-- coding for your tools // begin-->      
      <vue-scroll :ops="ops" style="height: calc(100vh - 40px)">
      <div style="padding: 10px">
        <div style="margin-bottom: 5px">
          <Button @click="add">Add</Button>
          <Button @click="edit" :disabled="this.selectSpecies.length != 1"
            >Edit</Button
          >
          <Button @click="remove">Remove</Button>
          <Button @click="submit" type="success" style="float: right"
            >Submit</Button
          >
        </div>
        <Table
          border
          ref="selection"
          :columns="fields"
          :data="species"
          @on-selection-change="select"
        >
          <template slot-scope="{ row }" slot="name">
            <strong>{{ row.name }}</strong>
          </template>
        </Table>
        <div style="margin-top: 5px">
          <Button @click="handleSelectAll(true)">Set all selected</Button>
          <Button @click="handleSelectAll(false)">Cancel all selected</Button>
        </div>
      </div>
      </vue-scroll>
      <Modal
        v-model="addSpeciesModal"
        width="800"
        title="Add a tree species information"
      >
        <Form
          ref="speciesValidate"
          :model="speciesValidate"
          :rules="ruleValidate"
          :label-width="210"
        >
          <div style="display: flex">
            <FormItem label="Name" prop="name">
              <Input
                v-model="speciesValidate.name"
                placeholder="Enter the name"
              ></Input>
            </FormItem>
            <FormItem
              label="Longevity"
              prop="longevity"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.longevity"
                placeholder="Enter the longevity"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Sexual maturity" prop="maturity">
              <Input
                v-model="speciesValidate.maturity"
                placeholder="Enter the sexual maturity"
              ></Input>
            </FormItem>
            <FormItem
              label="Shade tolerance"
              prop="s_tolerance"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.s_tolerance"
                placeholder="Enter the shade tolerance"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Fire tolerance" prop="f_tolerance">
              <Input
                v-model="speciesValidate.f_tolerance"
                placeholder="Enter the fire tolerance"
              ></Input>
            </FormItem>
            <FormItem
              label="Effective seeding distance"
              prop="e_seeding"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.e_seeding"
                placeholder="Enter the effective seeding distance"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Maximum seeding distance" prop="m_seeding">
              <Input
                v-model="speciesValidate.m_seeding"
                placeholder="Enter the maximum seeding distance"
              ></Input>
            </FormItem>
            <FormItem
              label="Vegetative reproduction probability"
              prop="reproduction"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.reproduction"
                placeholder="Enter the vegetative reproduction probability"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Minimum resprouting age" prop="min_resprouting">
              <Input
                v-model="speciesValidate.min_resprouting"
                placeholder="Enter the minimum resprouting age"
              ></Input>
            </FormItem>
            <FormItem
              label="Maximum resprouting age"
              prop="max_resprouting"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.max_resprouting"
                placeholder="Enter the maximum resprouting age"
              ></Input>
            </FormItem>
          </div>
          <FormItem label="Post-fire regeneration" prop="post_fire">
            <Input
              v-model="speciesValidate.post_fire"
              placeholder="Enter the post-fire regeneration"
            ></Input>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit('add', 'speciesValidate')"
            >Submit</Button
          >
          <Button
            @click="handleReset('speciesValidate')"
            style="margin-left: 8px"
            >Reset</Button
          >
        </div>
      </Modal>
      <Modal
        v-model="editSpeciesModal"
        width="800"
        title="Edit a tree species information"
      >
        <Form
          ref="speciesValidate"
          :model="speciesValidate"
          :rules="ruleValidate"
          :label-width="210"
        >
          <div style="display: flex">
            <FormItem label="Name" prop="name">
              <Input
                v-model="speciesValidate.name"
                placeholder="Enter the name"
                readonly
              ></Input>
            </FormItem>
            <FormItem
              label="Longevity"
              prop="longevity"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.longevity"
                placeholder="Enter the longevity"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Sexual maturity" prop="maturity">
              <Input
                v-model="speciesValidate.maturity"
                placeholder="Enter the sexual maturity"
              ></Input>
            </FormItem>
            <FormItem
              label="Shade tolerance"
              prop="s_tolerance"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.s_tolerance"
                placeholder="Enter the shade tolerance"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Fire tolerance" prop="f_tolerance">
              <Input
                v-model="speciesValidate.f_tolerance"
                placeholder="Enter the fire tolerance"
              ></Input>
            </FormItem>
            <FormItem
              label="Effective seeding distance"
              prop="e_seeding"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.e_seeding"
                placeholder="Enter the effective seeding distance"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Maximum seeding distance" prop="m_seeding">
              <Input
                v-model="speciesValidate.m_seeding"
                placeholder="Enter the maximum seeding distance"
              ></Input>
            </FormItem>
            <FormItem
              label="Vegetative reproduction probability"
              prop="reproduction"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.reproduction"
                placeholder="Enter the vegetative reproduction probability"
              ></Input>
            </FormItem>
          </div>
          <div style="display: flex">
            <FormItem label="Minimum resprouting age" prop="min_resprouting">
              <Input
                v-model="speciesValidate.min_resprouting"
                placeholder="Enter the minimum resprouting age"
              ></Input>
            </FormItem>
            <FormItem
              label="Maximum resprouting age"
              prop="max_resprouting"
              style="margin-left: 20px"
            >
              <Input
                v-model="speciesValidate.max_resprouting"
                placeholder="Enter the maximum resprouting age"
              ></Input>
            </FormItem>
          </div>
          <FormItem label="Post-fire regeneration" prop="post_fire">
            <Input
              v-model="speciesValidate.post_fire"
              placeholder="Enter the post-fire regeneration"
            ></Input>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button
            type="primary"
            @click="handleSubmit('edit', 'speciesValidate')"
            >Submit</Button
          >
        </div>
      </Modal>
      <!-- // end -->
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
      selectSpecies: [],
      fields: [
        {
          type: "selection",
          width: 60,
          align: "center",
        },
        {
          title: "Name",
          slot: "name",
        },
        {
          title: "Longevity",
          key: "longevity",
        },
        {
          title: "Sexual maturity",
          key: "maturity",
        },
        {
          title: "Shade tolerance",
          key: "s_tolerance",
        },
        {
          title: "Fire tolerance",
          key: "f_tolerance",
        },
        {
          title: "Effective seeding distance",
          key: "e_seeding",
        },
        {
          title: "Maximum seeding distance",
          key: "m_seeding",
        },
        {
          title: "Vegetative reproduction probability",
          key: "reproduction",
        },
        {
          title: "Minimum resprouting age",
          key: "min_resprouting",
        },
        {
          title: "Maximum resprouting age",
          key: "max_resprouting",
        },
        {
          title: "Post-fire regeneration",
          key: "post_fire",
        },
      ],
      species: [
        {
          name: "Example",
          longevity: 200,
          maturity: 25,
          s_tolerance: 5,
          f_tolerance: 1,
          e_seeding: 130,
          m_seeding: 160,
          reproduction: 0.0,
          min_resprouting: 0,
          max_resprouting: 0,
          post_fire: "none",
          _disabled: true,
        },
      ],
      addSpeciesModal: false,
      editSpeciesModal: false,
      speciesValidate: {
        name: "",
        longevity: "",
        maturity: "",
        s_tolerance: "",
        f_tolerance: "",
        e_seeding: "",
        m_seeding: "",
        reproduction: "",
        min_resprouting: "",
        max_resprouting: "",
        post_fire: "",
      },
      ruleValidate: {
        name: [
          {
            required: true,
            message: "The name cannot be empty",
            trigger: "blur",
          },
        ],
        longevity: [
          {
            required: true,
            message: "The longevity cannot be empty",
            trigger: "blur",
          },
        ],
        maturity: [
          {
            required: true,
            message: "The sexual maturity cannot be empty",
            trigger: "blur",
          },
        ],
        s_tolerance: [
          {
            required: true,
            message: "The shade tolerance cannot be empty",
            trigger: "blur",
          },
        ],
        f_tolerance: [
          {
            required: true,
            message: "The fire tolerance cannot be empty",
            trigger: "blur",
          },
        ],
        e_seeding: [
          {
            required: true,
            message: "The effective seeding distance cannot be empty",
            trigger: "blur",
          },
        ],
        m_seeding: [
          {
            required: true,
            message: "The maximum seeding distance cannot be empty",
            trigger: "blur",
          },
        ],
        reproduction: [
          {
            required: true,
            message: "The vegetative reproduction probability cannot be empty",
            trigger: "blur",
          },
        ],
        min_resprouting: [
          {
            required: true,
            message: "The minimum resprouting age cannot be empty",
            trigger: "blur",
          },
        ],
        max_resprouting: [
          {
            required: true,
            message: "The maximum resprouting age cannot be empty",
            trigger: "blur",
          },
        ],
        post_fire: [
          {
            required: true,
            message: "The post-fire regeneration cannot be empty",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {},
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
      } else {
        let _this = this;
        setTimeout(function () {
          _this.getStepInfo();
        }, 1000);
      }
    },
    getSocketComputation(data) {},
    getSocketOperation(data) {},
    getSocketData(data) {},
    add() {
      this.speciesValidate = {
        name: "",
        longevity: "",
        maturity: "",
        s_tolerance: "",
        f_tolerance: "",
        e_seeding: "",
        m_seeding: "",
        reproduction: "",
        min_resprouting: "",
        max_resprouting: "",
        post_fire: "",
      };
      this.addSpeciesModal = true;
    },
    edit() {
      this.speciesValidate = this.selectSpecies[0];
      this.editSpeciesModal = true;
    },
    remove() {
      for (let i = 0; i < this.selectSpecies.length; i++) {
        for (let j = 0; j < this.species.length; j++) {
          if(this.selectSpecies[i].name === this.species[j].name){
            this.species.splice(j, 1);
            break;
          }
        }
      }      
    },
    submit() {},
    select(selection) {
      this.selectSpecies = Object.assign([], selection);
    },
    handleSelectAll(status) {
      this.$refs.selection.selectAll(status);
      if (status) {
        this.selectSpecies = Object.assign([], this.species);
      } else {
        this.selectSpecies = [];
      }
    },
    handleSubmit(behavior, name) {
      let _this = this;
      this.$refs[name].validate((valid) => {
        if (valid) {
          let row = this.arrayContains(_this.species, _this.speciesValidate);
          if (behavior === "add") {
            if (row >= 0) {
              this.$Message.error("Duplicate tree species information!");
            } else {
              _this.species.push(_this.speciesValidate);
              _this.addSpeciesModal = false;
            }
          } else if (behavior === "edit") {
            if (row >= 0) {
              _this.species.splice(row, 1, _this.speciesValidate);
              _this.editSpeciesModal = false;
            } else {
              this.$Message.error("Information error!");
            }
          }
        } else {
          this.$Message.error(
            "Please provide complete information of species!"
          );
        }
      });
    },
    handleReset(name) {
      this.$refs[name].resetFields();
    },
    arrayContains(array, data) {
      var i = array.length;
      while (i--) {
        if (array[i].name != undefined && array[i].name === data.name) {
          return i;
        }
      }
      return i;
    },
  },
};
</script>
<style scoped>
</style>