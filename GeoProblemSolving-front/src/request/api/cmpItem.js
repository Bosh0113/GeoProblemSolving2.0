import base from "./base"
import axios from "../http"

const cmp_item = {
  create(cmpItem){
    return axios.post(`${base.cmpItem}/createCmpItem`,cmpItem);
  }
}

export default cmp_item;