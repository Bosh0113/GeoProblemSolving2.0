<template>
<div class="box">
            <div class="blankslate">
                <svg height="40" class="blankslate-icon" viewBox="0 0 14 16" version="1.1" width="35"
                    aria-hidden="true">
                    <path fill-rule="evenodd"
                        d="M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z">
                    </path>
                </svg>
                <h3>{{welcomeTitle}}</h3>
                <p>
                    {{welcomeInfo}}
                    <a href="#" @click.prevent="onLinkClick()">{{linkInfo}}</a>
                </p>
            </div>
        </div>
</template>
<script>
export default {
  name: "blank-box",
  data () {
    return {
    };
  },
  props:{
    welcomeTitle:{
      type:String,
      required:true,
    },
    welcomeInfo:{
      type:String,
      required:true,
    },
    linkInfo:{
      type:String,
      required:true,
    },

  },
  methods: {
      onLinkClick(){
        this.$emit("linkClicked");
      }
    }
}
</script>
<style scoped>
.box {
    background-color: #fff;
    border: 1px solid #d1d5da;
    border-radius: 3px;
}

.blankslate {
    padding: 80px 40px;
    background-color: #fafbfc;
    border-radius: 3px;
    box-shadow: inset 0 0 10px rgba(27, 31, 35, .05);
    text-align: center;
}

.blankslate-icon {
    color: #a3aab1;
    margin-bottom: 8px;
    margin-left: 4px;
    margin-right: 4px;
    vertical-align: text-bottom;
    display: inline-block;
    fill: currentColor;
}

.blankslate h3 {
    font-size: 20px;
    margin: 16px 0;
    font-weight: 600;
}

.blankslate a {
    color: #0366d6;
    text-decoration: none;
}

.blankslate p {
    font-size: 16px;
    margin: 0px 50px;
}
</style>