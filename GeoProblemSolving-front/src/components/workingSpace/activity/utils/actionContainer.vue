<template>
  <div>
    <div id="drawflow"></div>
  </div>
</template>
<script>
import Vue from "vue";
export default {
  components: {},
  data() {
    return {
      editor: null,
      actionFlow: {
        drawflow: {
          Home: {
            data: {
              1: {
                id: 1,
                name: "welcome",
                data: {},
                class: "welcome",
                html:
                  '\n    <div>\n      <div class="title-box">👏 Welcome!!</div>\n      <div class="box">\n        <p>Simple flow library <b>demo</b>\n        <a href="https://github.com/jerosoler/Drawflow" target="_blank">Drawflow</a> by <b>Jero Soler</b></p><br>\n\n        <p>Multiple input / outputs<br>\n           Data sync nodes<br>\n           Import / export<br>\n           Modules support<br>\n           Simple use<br>\n           Type: Fixed or Edit<br>\n           Events: view console<br>\n           Pure Javascript<br>\n        </p>\n        <br>\n        <p><b><u>Shortkeys:</u></b></p>\n        <p>🎹 <b>Delete</b> for remove selected<br>\n        💠 Mouse Left Click == Move<br>\n        ❌ Mouse Right == Delete Option<br>\n        🔍 Ctrl + Wheel == Zoom<br>\n        📱 Mobile support<br>\n        ...</p>\n      </div>\n    </div>\n    ',
                typenode: false,
                inputs: {},
                outputs: {},
                pos_x: 50,
                pos_y: 50,
              },
              2: {
                id: 2,
                name: "slack",
                data: {},
                class: "slack",
                html:
                  '\n          <div>\n            <div class="title-box"><i class="fab fa-slack"></i> Slack chat message</div>\n          </div>\n          ',
                typenode: false,
                inputs: {
                  input_1: { connections: [{ node: "7", input: "output_1" }] },
                },
                outputs: {},
                pos_x: 1028,
                pos_y: 87,
              },
              3: {
                id: 3,
                name: "telegram",
                data: { channel: "channel_2" },
                class: "telegram",
                html:
                  '\n          <div>\n            <div class="title-box"><i class="fab fa-telegram-plane"></i> Telegram bot</div>\n            <div class="box">\n              <p>Send to telegram</p>\n              <p>select channel</p>\n              <select df-channel>\n                <option value="channel_1">Channel 1</option>\n                <option value="channel_2">Channel 2</option>\n                <option value="channel_3">Channel 3</option>\n                <option value="channel_4">Channel 4</option>\n              </select>\n            </div>\n          </div>\n          ',
                typenode: false,
                inputs: {
                  input_1: { connections: [{ node: "7", input: "output_1" }] },
                },
                outputs: {},
                pos_x: 1032,
                pos_y: 184,
              },
              4: {
                id: 4,
                name: "email",
                data: {},
                class: "email",
                html:
                  '\n            <div>\n              <div class="title-box"><i class="fas fa-at"></i> Send Email </div>\n            </div>\n            ',
                typenode: false,
                inputs: {
                  input_1: { connections: [{ node: "5", input: "output_1" }] },
                },
                outputs: {},
                pos_x: 1033,
                pos_y: 439,
              },
              5: {
                id: 5,
                name: "template",
                data: { template: "Write your template" },
                class: "template",
                html:
                  '\n            <div>\n              <div class="title-box"><i class="fas fa-code"></i> Template</div>\n              <div class="box">\n                Ger Vars\n                <textarea df-template></textarea>\n                Output template with vars\n              </div>\n            </div>\n            ',
                typenode: false,
                inputs: {
                  input_1: { connections: [{ node: "6", input: "output_1" }] },
                },
                outputs: {
                  output_1: {
                    connections: [
                      { node: "4", output: "input_1" },
                      { node: "11", output: "input_1" },
                    ],
                  },
                },
                pos_x: 607,
                pos_y: 304,
              },
              6: {
                id: 6,
                name: "github",
                data: { name: "https://github.com/jerosoler/Drawflow" },
                class: "github",
                html:
                  '\n          <div>\n            <div class="title-box"><i class="fab fa-github "></i> Github Stars</div>\n            <div class="box">\n              <p>Enter repository url</p>\n            <input type="text" df-name>\n            </div>\n          </div>\n          ',
                typenode: false,
                inputs: {},
                outputs: {
                  output_1: { connections: [{ node: "5", output: "input_1" }] },
                },
                pos_x: 341,
                pos_y: 191,
              },
              7: {
                id: 7,
                name: "facebook",
                data: {},
                class: "facebook",
                html:
                  '\n        <div>\n          <div class="title-box"><i class="fab fa-facebook"></i> Facebook Message</div>\n        </div>\n        ',
                typenode: false,
                inputs: {},
                outputs: {
                  output_1: {
                    connections: [
                      { node: "2", output: "input_1" },
                      { node: "3", output: "input_1" },
                      { node: "11", output: "input_1" },
                    ],
                  },
                },
                pos_x: 347,
                pos_y: 87,
              },
              11: {
                id: 11,
                name: "log",
                data: {},
                class: "log",
                html:
                  '\n            <div>\n              <div class="title-box"><i class="fas fa-file-signature"></i> Save log file </div>\n            </div>\n            ',
                typenode: false,
                inputs: {
                  input_1: {
                    connections: [
                      { node: "5", input: "output_1" },
                      { node: "7", input: "output_1" },
                    ],
                  },
                },
                outputs: {},
                pos_x: 1031,
                pos_y: 363,
              },
            },
          },
          Other: {
            data: {
              8: {
                id: 8,
                name: "personalized",
                data: {},
                class: "personalized",
                html:
                  "\n            <div>\n              Personalized\n            </div>\n            ",
                typenode: false,
                inputs: {
                  input_1: {
                    connections: [
                      { node: "12", input: "output_1" },
                      { node: "12", input: "output_2" },
                      { node: "12", input: "output_3" },
                      { node: "12", input: "output_4" },
                    ],
                  },
                },
                outputs: {
                  output_1: { connections: [{ node: "9", output: "input_1" }] },
                },
                pos_x: 764,
                pos_y: 227,
              },
              9: {
                id: 9,
                name: "dbclick",
                data: { name: "Hello World!!" },
                class: "dbclick",
                html:
                  '\n            <div>\n            <div class="title-box"><i class="fas fa-mouse"></i> Db Click</div>\n              <div class="box dbclickbox" ondblclick="showpopup(event)">\n                Db Click here\n                <div class="modal" style="display:none">\n                  <div class="modal-content">\n                    <span class="close" onclick="closemodal(event)">&times;</span>\n                    Change your variable {name} !\n                    <input type="text" df-name>\n                  </div>\n\n                </div>\n              </div>\n            </div>\n            ',
                typenode: false,
                inputs: {
                  input_1: { connections: [{ node: "8", input: "output_1" }] },
                },
                outputs: {
                  output_1: {
                    connections: [{ node: "12", output: "input_2" }],
                  },
                },
                pos_x: 209,
                pos_y: 38,
              },
              12: {
                id: 12,
                name: "multiple",
                data: {},
                class: "multiple",
                html:
                  '\n            <div>\n              <div class="box">\n                Multiple!\n              </div>\n            </div>\n            ',
                typenode: false,
                inputs: {
                  input_1: { connections: [] },
                  input_2: { connections: [{ node: "9", input: "output_1" }] },
                  input_3: { connections: [] },
                },
                outputs: {
                  output_1: { connections: [{ node: "8", output: "input_1" }] },
                  output_2: { connections: [{ node: "8", output: "input_1" }] },
                  output_3: { connections: [{ node: "8", output: "input_1" }] },
                  output_4: { connections: [{ node: "8", output: "input_1" }] },
                },
                pos_x: 179,
                pos_y: 272,
              },
            },
          },
        },
      },
    };
  },
  mounted() {
    const id = document.getElementById("drawflow");
    this.editor = new Drawflow(id, Vue);
    this.editor.drawflow = this.actionFlow;
    this.editor.start();
  },
  methods: {},
};
</script>
<style>
#drawflow {
  width: 1000px;
  height: 600px;
}
:root {
  --dfBackgroundColor: rgba(255, 255, 255, 1);
  --dfBackgroundSize: 20px;
  --dfBackgroundImage: linear-gradient(to right, rgba(239, 239, 239, 1) 1px, transparent 1px), linear-gradient(to bottom, rgba(239, 239, 239, 1) 1px, transparent 1px);

  --dfNodeType: flex;
  --dfNodeTypeFloat: none;
  --dfNodeBackgroundColor: #ffffff;
  --dfNodeTextColor: #000000;
  --dfNodeBorderSize: 1px;
  --dfNodeBorderColor: rgba(0, 0, 0, 0.74);
  --dfNodeBorderRadius: 3px;
  --dfNodeMinHeight: 30px;
  --dfNodeMinWidth: 100px;
  --dfNodePaddingTop: 10px;
  --dfNodePaddingBottom: 10px;
  --dfNodeBoxShadowHL: 0px;
  --dfNodeBoxShadowVL: 3px;
  --dfNodeBoxShadowBR: 8px;
  --dfNodeBoxShadowS: 1px;
  --dfNodeBoxShadowColor: rgba(0, 0, 0, 0.56);

  --dfNodeHoverBackgroundColor: #ffffff;
  --dfNodeHoverTextColor: rgba(0, 0, 0, 1);
  --dfNodeHoverBorderSize: 1px;
  --dfNodeHoverBorderColor: rgba(78, 169, 255, 1);
  --dfNodeHoverBorderRadius: 3px;

  --dfNodeHoverBoxShadowHL: 0px;
  --dfNodeHoverBoxShadowVL: 3px;
  --dfNodeHoverBoxShadowBR: 8px;
  --dfNodeHoverBoxShadowS: 1px;
  --dfNodeHoverBoxShadowColor: rgba(78, 169, 255, 0.66);

  --dfNodeSelectedBackgroundColor: rgba(0, 0, 0, 0.25);
  --dfNodeSelectedTextColor: rgba(6, 6, 6, 1);
  --dfNodeSelectedBorderSize: 2px;
  --dfNodeSelectedBorderColor: #000000;
  --dfNodeSelectedBorderRadius: 3px;

  --dfNodeSelectedBoxShadowHL: 0px;
  --dfNodeSelectedBoxShadowVL: 3px;
  --dfNodeSelectedBoxShadowBR: 8px;
  --dfNodeSelectedBoxShadowS: 1px;
  --dfNodeSelectedBoxShadowColor: rgba(0, 0, 0, 0.56);

  --dfInputBackgroundColor: #ffffff;
  --dfInputBorderSize: 2px;
  --dfInputBorderColor: rgba(0, 0, 0, 1);
  --dfInputBorderRadius: 50px;
  --dfInputLeft: -28px;
  --dfInputHeight: 20px;
  --dfInputWidth: 20px;

  --dfInputHoverBackgroundColor: rgba(191, 214, 235, 1);
  --dfInputHoverBorderSize: 2px;
  --dfInputHoverBorderColor: rgba(78, 169, 255, 1);
  --dfInputHoverBorderRadius: 50px;

  --dfOutputBackgroundColor: #ffffff;
  --dfOutputBorderSize: 2px;
  --dfOutputBorderColor: #000000;
  --dfOutputBorderRadius: 50px;
  --dfOutputRight: -3px;
  --dfOutputHeight: 20px;
  --dfOutputWidth: 20px;

  --dfOutputHoverBackgroundColor: rgba(191, 214, 235, 1);
  --dfOutputHoverBorderSize: 2px;
  --dfOutputHoverBorderColor: rgba(78, 169, 255, 1);
  --dfOutputHoverBorderRadius: 50px;

  --dfLineWidth: 3px;
  --dfLineColor: #4682b4;
  --dfLineHoverColor: #4682b4;
  --dfLineSelectedColor: #43b993;

  --dfRerouteBorderWidth: 1px;
  --dfRerouteBorderColor: #000000;
  --dfRerouteBackgroundColor: #ffffff;

  --dfRerouteHoverBorderWidth: 2px;
  --dfRerouteHoverBorderColor: #000000;
  --dfRerouteHoverBackgroundColor: #ffffff;

  --dfDeleteDisplay: block;
  --dfDeleteColor: #ffffff;
  --dfDeleteBackgroundColor: rgba(0, 0, 0, 1);
  --dfDeleteBorderSize: 2px;
  --dfDeleteBorderColor: #ffffff;
  --dfDeleteBorderRadius: 50px;
  --dfDeleteTop: -20px;

  --dfDeleteHoverColor: #000000;
  --dfDeleteHoverBackgroundColor: #ffffff;
  --dfDeleteHoverBorderSize: 2px;
  --dfDeleteHoverBorderColor: #000000;
  --dfDeleteHoverBorderRadius: 50px;

}

#drawflow {
  background: var(--dfBackgroundColor);
  background-size: var(--dfBackgroundSize) var(--dfBackgroundSize);
  background-image: var(--dfBackgroundImage);
}

.drawflow .drawflow-node {
  display: var(--dfNodeType);
  background: var(--dfNodeBackgroundColor);
  color: var(--dfNodeTextColor);
  border: var(--dfNodeBorderSize)  solid var(--dfNodeBorderColor);
  border-radius: var(--dfNodeBorderRadius);
  min-height: var(--dfNodeMinHeight);
  width: auto;
  min-width: var(--dfNodeMinWidth);
  padding-top: var(--dfNodePaddingTop);
  padding-bottom: var(--dfNodePaddingBottom);
  -webkit-box-shadow: var(--dfNodeBoxShadowHL) var(--dfNodeBoxShadowVL) var(--dfNodeBoxShadowBR) var(--dfNodeBoxShadowS) var(--dfNodeBoxShadowColor);
  box-shadow:  var(--dfNodeBoxShadowHL) var(--dfNodeBoxShadowVL) var(--dfNodeBoxShadowBR) var(--dfNodeBoxShadowS) var(--dfNodeBoxShadowColor);
}

.drawflow .drawflow-node:hover {
  background: var(--dfNodeHoverBackgroundColor);
  color: var(--dfNodeHoverTextColor);
  border: var(--dfNodeHoverBorderSize)  solid var(--dfNodeHoverBorderColor);
  border-radius: var(--dfNodeHoverBorderRadius);
  -webkit-box-shadow: var(--dfNodeHoverBoxShadowHL) var(--dfNodeHoverBoxShadowVL) var(--dfNodeHoverBoxShadowBR) var(--dfNodeHoverBoxShadowS) var(--dfNodeHoverBoxShadowColor);
  box-shadow:  var(--dfNodeHoverBoxShadowHL) var(--dfNodeHoverBoxShadowVL) var(--dfNodeHoverBoxShadowBR) var(--dfNodeHoverBoxShadowS) var(--dfNodeHoverBoxShadowColor);
}

.drawflow .drawflow-node.selected {
  background: var(--dfNodeSelectedBackgroundColor);
  color: var(--dfNodeSelectedTextColor);
  border: var(--dfNodeSelectedBorderSize)  solid var(--dfNodeSelectedBorderColor);
  border-radius: var(--dfNodeSelectedBorderRadius);
  -webkit-box-shadow: var(--dfNodeSelectedBoxShadowHL) var(--dfNodeSelectedBoxShadowVL) var(--dfNodeSelectedBoxShadowBR) var(--dfNodeSelectedBoxShadowS) var(--dfNodeSelectedBoxShadowColor);
  box-shadow:  var(--dfNodeSelectedBoxShadowHL) var(--dfNodeSelectedBoxShadowVL) var(--dfNodeSelectedBoxShadowBR) var(--dfNodeSelectedBoxShadowS) var(--dfNodeSelectedBoxShadowColor);
}

.drawflow .drawflow-node .input {
  left: var(--dfInputLeft);
  background: var(--dfInputBackgroundColor);
  border: var(--dfInputBorderSize)  solid var(--dfInputBorderColor);
  border-radius: var(--dfInputBorderRadius);
  height: var(--dfInputHeight);
  width: var(--dfInputWidth);
}

.drawflow .drawflow-node .input:hover {
  background: var(--dfInputHoverBackgroundColor);
  border: var(--dfInputHoverBorderSize)  solid var(--dfInputHoverBorderColor);
  border-radius: var(--dfInputHoverBorderRadius);
}

.drawflow .drawflow-node .outputs {
  float: var(--dfNodeTypeFloat);
}

.drawflow .drawflow-node .output {
  right: var(--dfOutputRight);
  background: var(--dfOutputBackgroundColor);
  border: var(--dfOutputBorderSize)  solid var(--dfOutputBorderColor);
  border-radius: var(--dfOutputBorderRadius);
  height: var(--dfOutputHeight);
  width: var(--dfOutputWidth);
}

.drawflow .drawflow-node .output:hover {
  background: var(--dfOutputHoverBackgroundColor);
  border: var(--dfOutputHoverBorderSize)  solid var(--dfOutputHoverBorderColor);
  border-radius: var(--dfOutputHoverBorderRadius);
}

.drawflow .connection .main-path {
  stroke-width: var(--dfLineWidth);
  stroke: var(--dfLineColor);
}

.drawflow .connection .main-path:hover {
  stroke: var(--dfLineHoverColor);
}

.drawflow .connection .main-path.selected {
  stroke: var(--dfLineSelectedColor);
}

.drawflow .connection .point {
  stroke: var(--dfRerouteBorderColor);
  stroke-width: var(--dfRerouteBorderWidth);
  fill: var(--dfRerouteBackgroundColor);
}

.drawflow .connection .point:hover {
  stroke: var(--dfRerouteHoverBorderColor);
  stroke-width: var(--dfRerouteHoverBorderWidth);
  fill: var(--dfRerouteHoverBackgroundColor);
}

.drawflow-delete {
  display: var(--dfDeleteDisplay);
  color: var(--dfDeleteColor);
  background: var(--dfDeleteBackgroundColor);
  border: var(--dfDeleteBorderSize) solid var(--dfDeleteBorderColor);
  border-radius: var(--dfDeleteBorderRadius);
}

.parent-node .drawflow-delete {
  top: var(--dfDeleteTop);
}

.drawflow-delete:hover {
  color: var(--dfDeleteHoverColor);
  background: var(--dfDeleteHoverBackgroundColor);
  border: var(--dfDeleteHoverBorderSize) solid var(--dfDeleteHoverBorderColor);
  border-radius: var(--dfDeleteHoverBorderRadius);
}

</style>