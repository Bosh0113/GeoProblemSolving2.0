<template>
<div>
  <Row>
    <Col span="20">
      <Card>
        <CheckboxGroup  style="display: inline-block">
          <Checkbox label="noticeList">
            <span>Private</span>
            <span class="badge">1</span>
          </Checkbox>
          <Checkbox label="replyList">
            <span>Public</span>
            <span class="badge">2</span>
          </Checkbox>
<!--          <Checkbox label="applyList">-->
<!--            <span>Apply</span>-->
<!--            <span class="badge">1</span>-->
<!--          </Checkbox>-->
        </CheckboxGroup>
        <span style="margin: 0 20px"></span>
        <CheckboxGroup  style="display: inline-block">
          <Checkbox label="read">
            <span>Owned by me</span>
            <span class="badge">3</span>
          </Checkbox>
          <Checkbox label="unRead">
            <span>Shared with me</span>
            <span class="badge">2</span>
          </Checkbox>

        </CheckboxGroup>
      </Card>
    </Col>
    <Col span="3" offset="1">
      <Card dis-hover class="historyLine" >
        <p slot="title">Event line</p>
        <div class="timeLineStyle">
          <vue-scroll :ops="ops">
<!--            <Timeline>-->
<!--              <div v-if="userEventList.length==0">-->
<!--                <div style="display:flex;justify-content:center">-->
<!--                  <Icon type="md-alert" size="40" color="gray" />-->
<!--                </div>-->
<!--                <br />-->
<!--                <div style="display:flex;justify-content:center">-->
<!--                  <h3-->
<!--                    style="text-align:center;width:80%"-->
<!--                  >Sorry, there are no events now.</h3>-->
<!--                </div>-->
<!--              </div>-->
<!--              <TimelineItem-->
<!--                v-for="(item,index) in userEventList"-->
<!--                :key="index"-->
<!--                v-show="userEventList.length>0"-->
<!--              >-->
<!--                <strong>-->
<!--                  <p class="time">{{item.createTime}}</p>-->
<!--                </strong>-->
<!--                <p class="content">{{item.description}}</p>-->
<!--              </TimelineItem>-->
<!--            </Timeline>-->
          </vue-scroll>
        </div>
      </Card>
    </Col>
  </Row>
</div>
</template>

<script>
    export default {
        name: "resource",
      data(){
          return {

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
    color: #fff;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    background-color: #999;
    border-radius: 10px;
  }
</style>
