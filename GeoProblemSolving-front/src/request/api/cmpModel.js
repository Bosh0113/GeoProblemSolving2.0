import base from "./base"
import axios from "../http"

const cmp_model = {
  getModelInfo(modelId){
    return axios.get(`${base.cmpModel}/getModelInfo?modelId=${modelId}`);
  },
  create(model){
    return axios.post(`${base.cmpModel}/createModel`,model);
  },
  deployModel(formData){
    return axios.post(`${base.cmpModel}/deployModel`,formData);
  },
  matchMd5(md5){
    return axios.get(`${base.cmpModel}/matchMd5?md5=${md5}`);
  }
}

export default cmp_model;