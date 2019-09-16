<template>
  <div class="home">
    <div class="home-screen">
      <!-- <img src="../../assets/images/new/bg.jpg"> -->
      <div class="bg"></div>
      <div class="mask"></div>
      <div class="content">
        <div>
          <span class="title">Solving complex geo-problems? Collaboration.</span>
          <span class="sub-title">Build Projects | Assemble Participants | Communicate Ideas | Explore Solutions</span>
          <span class="desc">The platform is full of vitality, where resesrchers can exchange their views, collide their thoughts,<br />and establish their collabarations.</span>
          <Button
            class="home-start-btn"
            @click="start"
          >Try it now</Button>
        </div>
      </div>
    </div>
    <div class="main-container">
      <div class="flow">
        <!-- <div class="gray-cover">
          <div class="flow-text">
            hahaha
          </div>
        </div>
        <div class="color-cover">
          <div class="flow-text">
            hahaha
          </div> -->
        <div class="flow-content">
          <span class="flow-title">Solve a Problem Collaboratively</span>
          <div class="flow-item-container">
            <span class="flow-item">
              <span class="flow-item-icon blue">
                <span class="flow-item-bg"></span>
                <Icon type="md-build" />
              </span>
              <span class="flow-item-text">Register as a participant</span>
            </span>
            <span class="flow-item">

              <span class="flow-item-icon yellow">
                <span class="flow-item-bg"></span>
                <Icon type="md-search" />
              </span>
              <span class="flow-item-text">Create or join a project</span>
            </span>

            <span class="flow-item">

              <span class="flow-item-icon green">
                <span class="flow-item-bg"></span>
                <Icon type="md-share" />
              </span>
              <span class="flow-item-text">Communicate ideas in a team</span>

            </span>
          </div>
        </div>
      </div>

      <div class="cases">
        <span class="case-title">Demo and Popular Projects</span>
        <Row class="case-content">
          <Col
            class="case-item"
            span="8"
          >
          <div class="case-img img1">

          </div>
          <div class="mask">
            <div class="bg"></div>
            <div class="case-text">
              <Icon type="md-planet" />
              <div>Planet</div>
            </div>
          </div>
          </Col>
          <Col
            class="case-item"
            span="8"
          >
          <div class="case-img img2">
          </div>
          <div class="mask">
            <div class="bg"></div>
            <div class="case-text">
              <Icon type="md-flame" />
              <div>Fire</div>
            </div>
          </div>
          </Col>
          <Col
            class="case-item"
            span="8"
          >
          <div class="case-img img3">
          </div>
          <div class="mask">
            <div class="bg"></div>
            <div class="case-text">
              <Icon type="md-leaf" />
              <div>Leaf</div>
            </div>
          </div>
          </Col>
        </Row>
      </div>

      <div class="tools">
        <span class="tools-title">Explore More</span>
        <Row
          class="tools-container"
          type="flex"
          justify="space-around"
        >
          <Col
            class="card"
            span="4"
          >
          <span class="bg img-map"></span>
          <span class="card-title">Map</span>
          <span class="card-desc">The fastest tools for visualizing geospatial data</span>
          <router-link to="#">Learn More</router-link>
          </Col>
          <Col
            class="card"
            span="4"
          >
          <span class="bg img-chart"></span>
          <span class="card-title">Chart</span>
          <span class="card-desc">Give a deeper understanding of your table data</span>
          <router-link to="#">Learn More</router-link>
          </Col>

          <Col
            class="card"
            span="4"
          >
          <span class="bg img-video"></span>
          <span class="card-title">Video</span>
          <span class="card-desc">One-stop Video and Text Chat Tool for convenient communication</span>
          <router-link to="#">Learn More</router-link>
          </Col>
          <Col
            class="card"
            span="4"
          >
          <span class="bg img-edit"></span>
          <span class="card-title">Edit</span>
          <span class="card-desc">Deeply tapping the potential value of resources</span>
          <router-link to="#">Learn More</router-link>
          </Col>
          <Col
            class="card"
            span="4"
          >
          <span class="bg img-todo"></span>
          <span class="card-title">TODO</span>
          <span class="card-desc">Life goes on, needs to keep moving forward.</span>
          <router-link to="#">Learn More</router-link>
          </Col>

        </Row>
      </div>
    </div>
  </div>

</template>
<script>
export default {
  data() {
    return {};
  },
  methods: {
    start() {
      if (this.$store.getters.userState) {
        window.location.href="/GeoProblemSolving/projectList";
      } else {
        this.$router.push({ name: "Login" });
      }
    }
  }
};
</script>

<style lang="scss" scoped>
$firstHeight: 700px;

.home-screen {
  position: relative;
  transform: translateY(-60px);
  img {
    max-width: 100%;
    min-height: 100%;
  }
  .bg {
    background-image: url("../../assets/images/new/bg.jpg");
    background-size: cover;
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgb(139, 139, 139);
    left: 0;
    top: 0;
  }
  .mask {
    background-size: cover;
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgb(59, 59, 59);
    opacity: 0.5;
    left: 0;
    top: 0;
  }
}

.content,
.flow,
.cases,
.tools {
  position: relative;
  text-align: center;
  margin-bottom: 3rem;
}

.content {
  min-height: $firstHeight;
  display: flex;
  justify-content: center;
  align-items: center;
  align-content: center;
  color: white;
  .title {
    font-size: 3.8rem;
    font-family: Georgia, sans-serif, serif;
  }

  .sub-title {
    margin: 1rem;
    display: block;
    font-size: 1.6rem;
    font-family: sans-serif, serif;
  }
  .desc {
    margin: 1rem;
    font-size: 1.2rem;
    display: block;
  }

  .home-start-btn {
    margin: 1rem;
    font-size: 1rem;
    border-radius: 6px;
  }
}

.main-container {
  .blue {
    border-color: #5cadff;
    color: #5cadff;
  }
  .green {
    border-color: #19be6b;
    color: #19be6b;
  }
  .yellow {
    border-color: #ff9900;
    color: #ff9900;
  }

  .tools {
    padding: 2rem 0 5rem 0;
    .tools-title {
      display: block;
      font-family: Georgia, sans-serif, serif;
      font-size: 2.5rem;
      padding: 1rem 0 3rem 0;
    }
    .tools-container {
      min-height: 350px;
      .card {
        border-top-right-radius: 10px;
        position: relative;
        border: solid 1px #161a1d44;
        overflow: hidden;
        text-align: left;
        .bg {
          display: block;
          width: 100%;
          height: 150px;
          background-size: cover;
          position: relative;
          left: 0;
          right: 0;
        }

        .img-map {
          background-image: url("../../assets/images/new/map.jpg");
        }
        .img-chart {
          background-image: url("../../assets/images/new/chart.jpg");
        }
        .img-video {
          background-image: url("../../assets/images/new/video.jpg");
        }
        .img-edit {
          background-image: url("../../assets/images/new/edit.jpg");
        }
        .img-todo {
          background-image: url("../../assets/images/new/notes.jpg");
        }

        .card-title {
          display: block;
          font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
            "Lucida Sans", Arial, sans-serif;
          padding: 1rem 1rem;
          font-size: 1.4rem;
        }
        .card-desc {
          display: block;
          padding: 0.5rem 1rem;
        }
        a {
          display: inline-block;
          margin: 2rem 1rem;
        }
      }
    }
  }
  .cases {
    .case-title {
      display: block;
      font-family: Georgia, sans-serif, serif;
      font-size: 2.5rem;
      padding: 1rem 0 3rem 0;
    }
    .case-content {
      min-height: 500px;
      .case-item {
        min-height: inherit;
        position: relative;
        .case-img {
          position: absolute;
          width: 100%;
          height: 100%;
          background-size: cover;
        }

        .img1 {
          background-image: url("../../assets/images/new/planet.jpg");
        }

        .img2 {
          background-image: url("../../assets/images/new/fire.jpg");
        }

        .img3 {
          background-image: url("../../assets/images/new/leaft1.jpg");
        }

        .mask {
          .bg {
            position: absolute;
            width: 100%;
            height: 100%;
            background-color: #161a1d;
            opacity: 0.5;
            color: white;
            transition: all 0.6s 0s cubic-bezier(0.02, 0.67, 0.45, 0.93);
            &:hover {
              background-color: #5cadff;
              opacity: 0.9;
            }
            &:hover + .case-text {
              opacity: 1;

              transform: translate3d(-50%, -60%, 0);
            }
          }
          .case-text {
            font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
              "Lucida Sans", Arial, sans-serif;
            transition: all 0.6s 0s cubic-bezier(0.02, 0.67, 0.45, 0.93);
            pointer-events: none;
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate3d(-50%, -50%, 0);
            color: white;
            opacity: 0.7;
            i {
              font-size: 6rem;
            }
            div {
              font-size: 1.2rem;
            }
          }
        }
      }
    }
  }
  .flow {
    margin: 5rem auto;
    width: 80%;
  
    // .color-cover {
    //   position: absolute;
    //   width: 50%;
    //   height: 100%;
    //   background-image: url("../../assets/images/new/guoqu.svg");
    //   background-position: -6rem 0;
    //   background-size: cover;
    //   overflow: hidden;
    //   .flow-text {
    //     position: relative;
    //     width: 1264px;
    //     height: 100%;
    //     color: red;
    //   }
    // }
    // .gray-cover {
    //   position: absolute;
    //   background-image: url("../../assets/images/new/meiguoqu.svg");
    //   background-size: cover;
    //   background-position: -6rem 0;
    //   width: 100%;
    //   height: 100%;
    //   .flow-text {
    //     color: black;
    //   }
    // }
    .flow-content {
      .flow-title {
        font-family: Georgia, sans-serif, serif;
        font-size: 2.5rem;
        padding: 1rem 0 3rem 0;
      }
      .flow-line {
        background: linear-gradient(left, #5cadff, #5cadff);
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate3d(-50%, -50%, 0);
        width: 100%;
        height: 0.4rem;
      }
      .flow-item-container {
        margin: 5rem 0 0;
        display: flex;
        justify-content: space-around;

        .flow-item {
          z-index: 10;
          display: inline-block;
         
          vertical-align: middle;
          color: #161a1dc0;
          .flow-item-icon {
            display: inline-block;
            padding: 2rem 3rem;
            font-size: 4.5rem;
            position: relative;

            &:hover {
              .flow-item-bg {
                transform: translate3d(-50%, -55%, 0);
              }
            }

            .flow-item-bg {
              transition: all 0.4s 0s cubic-bezier(0.02, 0.67, 0.45, 0.93);
              display: inline-block;
              position: absolute;
              left: 50%;
              top: 50%;
              transform: translate3d(-50%, -50%, 0);
              width: 100%;
              height: 100%;
              border: solid 1px #5cadff;
              border-radius: 10px;
              z-index: -1;
              // transform: rotate3d(0, 0, 1, 45deg);
            }
          }
          .flow-item-text {
            padding: 1rem 0;
            font-size: 1.2rem;
            display: block;
            font-family: "Gill Sans", "Gill Sans MT", Calibri, "Trebuchet MS",
              sans-serif;
          }
        }
      }
    }
  }
}
</style>
