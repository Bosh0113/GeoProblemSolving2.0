import base from "./base"
import axios from "../http"

const cmp_project = {
  create(project){
    return axios.post(`${base.cmpProjcet}/createProject`,project);
  }
}

export default cmp_project;