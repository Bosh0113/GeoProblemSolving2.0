<style scoped>
</style>
<template>
  <span class="stepChange">
    <Button @click="stepChanged = true" style="margin-left:20px" ghost>Swith step</Button>
    <Modal v-model="stepChanged"></Modal>
  </span>
</template>
<script>
export default {
  data() {
    return {
      stepChanged: false
    };
  },
  props: [],
  created() {},
  mounted() {},
  methods: {}
};
</script>