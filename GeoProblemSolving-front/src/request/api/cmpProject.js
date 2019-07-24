import base from "./base"
import axios from "../http"

const cmp_project = {
  create(project){
    return axios.post(`${base.cmpProjcet}/createProject`,project);
  },
  getAllProjects(){
    return axios.get(`${base.cmpProjcet}/getAllProjects`);
  },
  getProject(key,value){
    return axios.get(`${base.cmpProjcet}/getProject?key=${key}&value=${value}`);
  }
}

export default cmp_project;