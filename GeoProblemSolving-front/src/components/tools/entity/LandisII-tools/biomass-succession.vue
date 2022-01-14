<template>
  <div>
    <div id="collab-tool-head"></div>
    <div id="collab-tool-sidebar"></div>
    <div id="collab-tool-content">
      <div id="edit-mask" title="The other participant is operating."></div>

      <!-- coding for your tools // begin-->
      <div style="padding: 10px">
        <div style="margin-bottom: 5px">
          <Button @click="add()">Add</Button>
          <Button @click="edit()">Edit</Button>
          <Button @click="remove()">Remove</Button>
          <Button @click="submit()" type="success" style="float: right">Submit</Button>
        </div>
        <Table border ref="selection" :columns="fields" :data="species">
          <template slot-scope="{ row }" slot="name">
            <strong>{{ row.name }}</strong>
          </template>
        </Table>
        <div style="margin-top: 5px">
          <Button @click="handleSelectAll(true)">Set all selected</Button>
          <Button @click="handleSelectAll(false)">Cancel all selected</Button>
        </div>
      </div>
      <!-- // end -->
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
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
          _disabled: true
        },
      ],
    };
  },
  methods: {
    add() {},
    edit() {},
    remove() {
      this.species.splice(index, 1);
    },
    handleSelectAll(status) {
      this.$refs.selection.selectAll(status);
      if(status){
        this.selectSpecies = Object.assign([], this.species);
      } else {
        this.selectSpecies = [];
      }
    },
  },
};
</script>
<style scoped>
</style>