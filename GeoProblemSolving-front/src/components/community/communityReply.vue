<template>
  <div class="main">
    <div class="empty"></div>
    <Row>
      <Col span="18" offset="3">
        <div class="topicDetail">
          <p>
            In October 2002, the Biblical Archaeology Society announced a discovery which could provide historical evidence for the existence of Jesus. An inscription had been found on an ancient bone box（ossuary）that reads "James, son of Joseph, brother of Jesus." If authentic, this container provides the only new Testament-era mention of the central figure of Christianity and is the first-ever archaeological discovery to corroborate biblical references to Jesus. This June, the Discovery Channel followed the story of the unearthing in Israel of this ancient ossuary, providing viewers with new information about the discovery of this historic relic and raising questions about Jesus' family life.

According to one of the world's leading specialist in ancient inscriptions, Andre Lemaire of the Sorbonne University in Paris, the Aramaic words etched on the box's side show a cursive form of writing used only from about 10 to 70 AD. Ancient inscriptions are typically found on royal monuments or on lavish tombs, commemorating rulers and other official figures. But Jesus, who was raised by a carpenter, was a man of the people, so finding documentation of his family is unexpected. The find is also significant in that it corroborates the existence of Joseph, Jesus' father, and James, Jesus’ brother and a leader of the early Christian church in Jerusalem. The family relationships contained on the ossuary helped experts uncover that the inscription very likely refers to the biblical James, brother of Jesus. Although all three names were common in ancient times, the statistical probability of their appearing in that combination is extremely slim. In addition, the mention of a brother is unusual, indicating that this Jesus must have been a well-known figure.
          </p>
          <p>
            I don't know whether it was because work at the office slowed during February or because the football season was over. But Valentine's Day was the time my father chose to show his love for the special people in his life. Over the years I fondly2 thought of him as my "Valentine Man."

My first recollection3 of the magic4 he could bring to Valentine's Day came when I was six. For several days I had been cutting out valentines for my classmates. Each of us was to decorate a "mailbox" and put it on our desk for others to give us cards. That box and its contents ushered in5 a succession6 of bittersweet7 memories of my entrance into a world of popularity8 contests marked by the number of cards received, the teasing about boyfriends/girlfriends and the tender care I gave to the card from the cutest boy in class.

That morning at the breakfast table I found a card and a gift- wrapped package at my chair. The card was signed "Love, Dad", and the gift was a ring with a small piece of red glass to represent my birthstone9, a ruby10. There is little difference between red glass and rubies to a child of six, and I remember wearing that ring with a pride that all the cards in the world could not surpass11.

As I grew older, the gifts gave way to heart shaped boxes filled with my favorite chocolates and always included a special card signed "Love, Dad". In those years my "thank-yous" became more of a perfunctory12 response. The cards seemed less important, and I took for granted the valentine that would always be there. Long past the days of having a "mailbox" on my desk, I had placed my hopes and dreams in receiving cards and gifts from "significant others", and "Love, Dad" just didn't seem quite enough.
          </p>
        </div>
        <div id="editor">
          <!-- <mavon-editor style="height: 100%" v-model="value">111</mavon-editor> -->
        </div>
        <div class="replyList">
          <div class="replyCount">There are
            <strong style="color:darkblue">{{topicReplyList.reply.length}}</strong>&nbspreplies about this topic
          </div>
          <div class="replyBoard" v-for="item in topicReplyList.reply">
            <div class="to" style="padding-left:20px;padding-right:20px"><strong style="color:red">@</strong>{{item.to}}</div>
            <div class="reply">{{item.content}}</div>
            <div class="footer-util">
              <div class="comment-util" style="display:flex">
                <div class="support">
                  <span :key="item.index">
                    <Icon type="md-thumbs-up" color="green" class="up"/>
                    {{item.support}}
                  </span>
                </div>
                <div class="nosupport">
                  <span :key="item.index">
                    <Icon type="md-thumbs-down" color="red"/>
                    {{item.nosupport}}
                  </span>
                </div>
                <div class="comment">
                  <span>
                    <Icon type="md-chatboxes"/>
                    {{item.comment}}
                  </span>
                </div>
              </div>
              <div class="user-info">
                  <span class="author">{{item.author}}</span>
                  <span class="time">{{item.time}}</span>
              </div>
            </div>
          </div>
        </div>
      </Col>
    </Row>
  </div>
</template>
<style scoped>
.main {
  top: 60px;
  height: 1800px;
  width: 100%;
}
.empty {
  height: 20px;
  background-color: white;
}
.topicDetail {
  min-height: 200px;
}
.topicDetail p {
  font-size: 20px;
}
#editor {
  /* position:fixed; */
  margin: 20px auto;
  bottom: 0;
  width: 80%;
  height: 400px;
  background-color: aqua;
}
.replyCount {
  margin-top: 20px;
  font-size: 25px;
}
.replyBoard {
  min-height: 40px;

  width: 100%;
  font-size:20px;
  border:1px solid black;
  margin-bottom:20px
}
.to{
  height:40px;
  line-height:40px;
  font-size:20px;
}
.reply {
  padding: 20px;
  /* background-color: lightblue; */
}
.footer-util {
  height: 40px;
  line-height: 40px;
  font-size: 20px;
  display: flex;

}
.comment-util {
  height: 100%;
  width: 100%;
  padding-left:20px;
  padding-right:20px;
  /* background-color: lightgray; */
}
.user-info {
  height: 100%;
  width: 50%;
  /* background-color: lightpink; */
}

.comment {
  width: 20%;
  margin-left: 1%;
  margin-right: 1%;
}
.support {
  width: 20%;
  margin-left: 1%;
  margin-right: 1%;
}
.support:hover,
.nosupport:hover {
  width: 20%;
  margin-left: 1%;
  margin-right: 1%;
  font-size: 25px;
  transition: 0.5s;
}

.nosupport {
  width: 20%;
  margin-left: 1%;
  margin-right: 1%;
}
.author{
  width:20%;
  margin-left:10%;
}
.time{
  width:80%;
  margin-left:20%;
}
</style>
<script>
export default {
  data() {
    return {
      value: "2222",
      topicReplyList: {
        content: "",
        reply: [
          {
            author: "lyc",
            content:
              "I was always fond of visiting new scenes, and observing strange characters and manners. Even when a mere child I began my travels, and made many tours of discovery into foreign parts and unknown regions of my native city.",
            to: "all",
            support:12,
            nosupport:3,
            time:"2018-11-12 17:22"
          },
          {
            author: "mzy",
            content:
              " to the frequent alarm of my parents, and the emolument of the town-crier. As I grew into boyhood, I extended the range of my observations.",
            to: "lyc",
            support:5,
            nosupport:7,
            time:"2018-11-13 18:55"
          },
          {
            author: "lyc",
            content:
              "My holiday afternoons were spent in rambles about the surrounding country. I made myself familiar with all its places famous in history or fable. I knew every spot where a murder or robbery had been committed, or a ghost seen.",
            to: "mzy",
            support:9,
            nosupport:4,
            time:"2018-11-13 19:00"
          }
        ]
      },
      mainstyle: {
        height: ""
      }
    };
  },

  mounted() {
    this.getTopicDetail();
    this.setMainStyle();
    // let headerWidth = window.innerWidth+'px';
  },
  methods: {
    setMainStyle() {
      this.mainstyle.height = window.innerHeight - 60 + "px";

    },
    getTopicDetail() {
      let title = this.$route.params.id;
      let formdata = new URLSearchParams();
      formdata.append("title", title);
      // this.axios.post(url, formdata)
      // .then(res=> {
      // })
      // .catch(err=> {
      // })
    }
  }
};
</script>
