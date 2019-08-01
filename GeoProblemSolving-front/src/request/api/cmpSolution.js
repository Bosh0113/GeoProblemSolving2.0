import base from "./base"
import axios from "../http"

const cmp_solution = {
  create(solution){
    return axios.post(`${base.cmpSolution}/createSolution`,solution);
  },
  deployModel(formData){
    return axios.post(`${base.cmpSolution}/deployModel`,formData);
  },
  matchMd5(md5){
    return axios.get(`${base.cmpSolution}/matchMd5?md5=${md5}`);
  }
}

export default cmp_solution;