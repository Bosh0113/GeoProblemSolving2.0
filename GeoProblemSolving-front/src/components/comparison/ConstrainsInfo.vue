<template>
  <Tabs size="default" :animated="false">
    <TabPane label="Data Info">
      <table>
        <tr>
          <td class="constrain_tb_name">Specified Model：</td>
          <td class="constrain_tb_value"><span class="member">IBIS</span></td>
        </tr>
        <tr>
          <td class="constrain_tb_name">Specified Output Data：</td>
          <td class="constrain_tb_value">
            <span class="member">NPP</span>
          </td>
        </tr>
        <tr>
          <td class="constrain_tb_name">Standard Input Data：</td>
          <td class="constrain_tb_value">To upload</td>
        </tr>
        <tr>
          <td class="constrain_tb_name">Evaluated Data：</td>
          <td class="constrain_tb_value">To upload</td>
        </tr>
      </table>
    </TabPane>
    <TabPane label="Other Info">
      <table>
        <tr>
          <td class="constrain_tb_name">Start Time：</td>
          <td class="constrain_tb_value">2000.01.01</td>
        </tr>
        <tr>
          <td class="constrain_tb_name">End Time：</td>
          <td class="constrain_tb_value">2000.01.01</td>
        </tr>
        <tr>
          <td class="constrain_tb_name">Location：</td>
          <td class="constrain_tb_value">global</td>
        </tr>
        <tr>
          <td class="constrain_tb_name">Resolution：</td>
          <td class="constrain_tb_value">None</td>
        </tr>
        <tr>
          <td class="constrain_tb_name">Time Interval：</td>
          <td class="constrain_tb_value">None</td>
        </tr>
      </table>
    </TabPane>
  </Tabs>
</template>
<script>
export default {
  name: "constrains-info",
  data() {
    return {};
  }
};
</script>
<style scoped>
.constrain_tb_name {
  width: 45%;
  font-size: 14px;
}

.constrain_tb_value {
  width: 50%;
  font-size: 14px;
}

table {
  margin-bottom: 10px;
}

.member {
  font-size: 12px;
  border: 1px solid #d1d5da;
  border-radius: 8px;
  padding: 3px;
  margin-right: 5px;
  margin-left: 5px;
  margin-bottom: 5px;
}
</style>