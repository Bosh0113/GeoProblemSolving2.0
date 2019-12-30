
</style>
<template>
  <div>
    <Row>
      <Col span="24">
        <Carousel v-model="setting.value" :dots="setting.dots" :trigger="setting.trigger" loop>
          <CarouselItem>
            <div class="demo-carousel">General Information</div>
            <template-general :toolInfoGeneral="toolInfoGeneral"></template-general>
          </CarouselItem>
          <CarouselItem>
            <tool-from-model @getSelectModel="getSelectModel"></tool-from-model>
          </CarouselItem>
          <CarouselItem>
            <template-detail :toolInfoDetail="toolInfoDetail"></template-detail>
          </CarouselItem>
        </Carousel>
      </Col>
    </Row>
  </div>
</template>

<script>
import Avatar from "vue-avatar";
import TemplateGeneral from "./TemplateGeneral";
import TemplateDetail from "./TemplateDetail";
import ToolFromModel from "./ToolFromModel";
export default {
  components: {
    Avatar,
    TemplateGeneral,
    ToolFromModel,
    TemplateDetail
  },
  data() {
    return {
      selectModelItem: {},
      setting: {
        value: "0",
        dots: "outside",
        trigger: "hover"
      },
      toolInfoGeneral: {},
      toolInfoDetail:{}
    };
  },
  methods: {
    getSelectModel(model) {
      this.selectModelItem = model;
      console.log(this.selectModelItem);
    }
  },

  created() {}
};
</script>
