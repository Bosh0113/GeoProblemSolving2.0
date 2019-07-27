import base from "./base"
import axios from "../http"

const cmp_item = {
  create(cmpItem){
    return axios.post(`${base.cmpItem}/createCmpItem`,cmpItem);
  },
  getItemsByIdList(idList){
    return axios.post(`${base.cmpItem}/getCmpItemsByIdList`,idList);
  },
  getCmpItem(key,value){
    return axios.get(`${base.cmpItem}/getCmpItem?key=${key}&value=${value}`);
  }
}

export default cmp_item;