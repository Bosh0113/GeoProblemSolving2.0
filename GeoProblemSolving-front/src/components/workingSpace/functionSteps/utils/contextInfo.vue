<style scoped>
</style>
<template>
  <Card dis-hover>
    <vue-scroll :ops="scrollOps" style="height:400px">
      <div class="condefContent">
        <Form
          ref="contextForm"
          :rules="contextFormValidate"
          :model="contextForm"
          :label-width="180"
        >
          <FormItem label="Problem boundary" prop="boundary">
            <Input
              v-model="contextForm.boundary"
              placeholder="Please enter the boundry of the problem"
              type="textarea"
              :autosize="{minRows: 1,maxRows: 5}"
              clearable
            />
          </FormItem>
          <FormItem label="Spatiotemporal scale" prop="scale">
            <Input
              v-model="contextForm.scale"
              placeholder="Please enter the spatiotemporal scale"
              type="textarea"
              :autosize="{minRows: 1,maxRows: 5}"
              clearable
            />
          </FormItem>
          <FormItem label=" Main methods" prop="methods">
            <Input
              v-model="contextForm.methods"
              placeholder="Please enter the main methods"
              type="textarea"
              :autosize="{minRows: 1,maxRows: 5}"
              clearable
            />
          </FormItem>
          <FormItem label="Goals or Purposes" prop="goals">
            <Input
              v-model="contextForm.goals"
              placeholder="Please enter your goal"
              type="textarea"
              :autosize="{minRows: 1,maxRows: 5}"
              clearable
            />
          </FormItem>
          <FormItem label=" Difficulties or Limitations" prop="difficulties">
            <Input
              v-model="contextForm.difficulties"
              placeholder="Please enter the difficulties"
              type="textarea"
              :autosize="{minRows: 1,maxRows: 5}"
              clearable
            />
          </FormItem>
          <FormItem label="Others" prop="others">
            <Input
              v-model="contextForm.others"
              placeholder="Supplement"
              type="textarea"
              :autosize="{minRows: 1,maxRows: 5}"
              clearable
            />
          </FormItem>
          <FormItem>
            <template v-if="userRole == 'Manager'">
              <Button @click="submit('contextForm')">Submit</Button>
            </template>
          </FormItem>
        </Form>
      </div>
    </vue-scroll>
  </Card>
</template>
<script>
export default {
  props: ["stepInfo", "userRole"],
  data() {
    return {
      scrollOps: {
        bar: {
          background: "lightgrey"
        }
      },
      contextForm: {
        boundary: "",
        scale: "",
        difficulties: "",
        goals: "",
        methods: "",
        others: ""
      },
      contextFormValidate: {
        boundary: [
          {
            required: true,
            message: "The boundary of problem cannot be empty",
            trigger: "blur"
          }
        ],
        methods: [
          {
            required: true,
            message: "The main methods cannot be empty",
            trigger: "blur"
          }
        ],
        scale: [
          {
            required: false,
            message: "Please enter the spatiotemporal scale",
            trigger: "blur"
          }
        ],
        difficulties: [
          {
            required: false,
            message: "Please enter something to explain the difficulties",
            trigger: "blur"
          }
        ],
        goals: [
          {
            required: true,
            message: "The goals cannot be empty",
            trigger: "blur"
          }
        ],
        others: [
          {
            required: false,
            message: "Please enter something to add the context definition",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit(contextform) {
      let creatorId = this.$store.getters.userId;
      this.$refs[contextform].validate(valid => {
        // 提交表单
        if (valid) {
          let contextDefinition = new URLSearchParams();
          // contextDefinition.append("creator", creatorId);
          contextDefinition.append("type", "contextDefinition");

          contextDefinition.append(
            "content.boundary",
            this.contextForm.boundary
          );
          contextDefinition.append("content.scale", this.contextForm.scale);
          contextDefinition.append("content.methods", this.contextForm.methods);
          contextDefinition.append(
            "content.difficulties",
            this.contextForm.difficulties
          );
          contextDefinition.append("content.goals", this.contextForm.goals);
          contextDefinition.append("content.others", this.contextForm.others);
          contextDefinition.append("stepId", this.stepId);

          this.axios
            .post("/GeoProblemSolving/step/update", contextDefinition)
            .then(res => {
              if (res.data == "Offline") {
                this.$store.commit("userLogout");
                this.$router.push({
                  name: "Login"
                });
              } else if (res.data != "Fail") {
                this.$Notice.info({
                  desc: "Update successfully!"
                });
              } else {
                this.$Message.error("Update subproject failed.");
              }
            });
        }
      });
    }
  }
};
</script>