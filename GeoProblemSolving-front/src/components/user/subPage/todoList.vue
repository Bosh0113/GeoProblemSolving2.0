<template>
  <div>
    <!--  Fiter  -->
    <Row>
      <Col span="22" offset="1">
        <Card>
          <CheckboxGroup v-model="selectedTaskType">
            <Checkbox label="Doing">
              <sapn>Doing</sapn>
              <span class="badge">1</span>
            </Checkbox>
            <Checkbox label="Done">
              <span>Done</span>
              <span class="badge">5</span>
            </Checkbox>
            <Checkbox label="Importance">
              <span>Importance</span>
              <span class="badge">2</span>
            </Checkbox>
            <Checkbox label="Assigned">
              <span>Assigned to you</span>
              <span class="badge">10</span>
            </Checkbox>
          </CheckboxGroup>
        </Card>
      </Col>
    </Row>
    <Row>
      <Col span="22" offset="1">

        <Card>
          <!--         新建任务   -->
          <div>
            <Input prefix="ios-contact" suffix="md-calendar"/>
          </div>
          <!--      未完成            -->
          <div>
            <Card>
              <p slot="title">Doing</p>
              <div>
                <Card>
                  This is doing task space.On the path towards power systems with high renewable penetrations and ultimately carbon-neutral, more and more synchronous generation is being displaced by variable renewable generation that does not currently provide system inertia nor reactive power support. This could create serious issues of power system stability in the near future, and countries with high renewable penetrations such as Ireland are already facing these challenges. Therefore, this paper aims at answering the questions of whether and how explicitly including inertia and reactive power constraints in generation expansion planning would affect the optimal capacity mix of the power system of the future. Towards this end, we propose the novel Low-carbon Expansion Generation Optimization model, which explicitly accounts for: unit commitment constraints, Rate of Change of Frequency inertia requirements and virtual inertia provision, and, a second-order cone programming approximation of the AC power flow, accounting for reactive power constraints. An illustrative case study underlines that disregarding inertia and reactive power constraints in generation expansion planning can result in additional system cost, system infeasibilities, a distortion of optimal resource allocation and inability to reach established policy goals.
                  <p slot="title">
                    x
                  <div style="display: inline-block;float: left">
                    <Icon type="ios-star-outline"/>
                    <Icon type="ios-star"/>
                  </div>
                  </p>
                  <!--                  标记与否 -->


                </Card>
              </div>
            </Card>
          </div>
          <!--        已完成      -->
          <div>
            <Card>
              <p slot="title">Done</p>
              <!--            显示内容                -->
              <div>
                <Card>
                  This is done task space.

                </Card>

              </div>
            </Card>
          </div>
        </Card>
      </Col>
    </Row>
  </div>
</template>

<script>
  export default {
    name: "todoList",
    data() {
      return {
        selectedTaskType: [],
        doneList: [],
        doingList: []
      }
    },
    mounted() {
    },
    methods: {
      initTodoList: function () {
        this.axios
          .get()
          .then()
          .catch(err => {
            this.$Message.error("Todo Task Loading Fail.")
          })
      }
    },
  }
</script>

<style scoped>
  .badge {
    display: inline-block;
    min-width: 10px;
    padding: 3px 7px;
    font-size: 12px;
    font-weight: bold;
    line-height: 1;
    color: #ffffff;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    background-color: #999999;
    border-radius: 10px;
  }

</style>
