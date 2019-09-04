<template>
  <div>
    <h1>Create Solution</h1>
    <Form ref="solutionInfo" :rules="rules" :model="solutionInfo" class="projectForm">
      <FormItem prop="name" label="Name" :label-width="150">
        <Input v-model="solutionInfo.name" placeholder="Enter Name ..." />
      </FormItem>

      <FormItem prop="description" label="Description" :label-width="150">
        <div>
          <Input type="textarea" v-model="solutionInfo.description"
            placeholder="Enter detailed introduction about this solution" />
        </div>
      </FormItem>
      <FormItem>
        <div class="inline_style">
          <Button class="create" type="success" @click='createSolution("solutionInfo")'>Create</Button>
        </div>
      </FormItem>

    </Form>
  </div>
</template>
<script>
import Util from "@/utils/comparison/cmpUtils";
import base from "@/request/api/base";
import { resolve, reject } from "q";
export default {
  created() {
    this.baseUrl = base.cmpSolution;
    let projectId = this.$route.params.id;
    this.solutionInfo.projectId = projectId;
  },
  data() {
    return {
      solutionInfo: {
        name: "",
        ownerId: this.$store.getters.userId,
        ownerName: this.$store.getters.userName,
        description: "",
        objType: "model",
        privacy: "Public",
        projectId:"",
        // spatialDimension: 2,
        // scale: "",
        // spatialReference: "",
        // resolutionConstraint: {
        //   x: { value: "", unit: "" },
        //   y: { value: "", unit: "" },
        //   z: { value: "", unit: "" }
        // },
        // spatialExtents: {
        //   type: "",
        //   value: "",
        //   envelopeValue: []
        // },
        // timeStep: { value: "", unit: "" },
        // startTime: "",
        // endTime: "",
        // programmingLang: []
      },
      rules: {
        name: [
          {
            required: true,
            message: "The name cannot be empty and no more than 100 characters",
            trigger: "blur",
            max: 100
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
        ]
      }
    };
  },
  methods: {
    createSolution(solution) {
      this.$refs[solution].validate(valid => {
        if (valid) {
          this.$api.cmp_solution
            .create(this.solutionInfo)
            .then(res => {
              this.$router.back(-1);
              this.$Message.success("success");
              console.log("solution:",res);
            })
            .catch(err => {
              this.$Message.error(err);
            });
        }
      });
    }
  },
  computed: {}
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