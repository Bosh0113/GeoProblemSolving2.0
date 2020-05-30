import axios from "axios";
import sf from "string-format";
import router from "@/router";
import {
  Message
} from 'element-ui';

const baseRequestUrl = "";

const axiosInstance = axios.create({
  baseURL: baseRequestUrl,
  timeout: 200000
});

axiosInstance.interceptors.request.use(
  config => {
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);
axiosInstance.interceptors.response.use(
  res => {
    //身份异常
    if (res.data == "Offline") {
      this.$store.commit("userLogout");
      router.push({
        name: "Login"
      });
    }
    // 状态码小于0属于异常情况
    if (res.data.code == -3) {
      Message({
        message: "Duplicate Naming",
        type: 'error'
      });
      throw new Error(res.data.msg);
    } else if (res.data.code < 0) {
      throw new Error(res.data.msg);
    }
    return res.data.data;
  },
  error => {
    throw new Error("请求服务器失败，请检查服务是否正常！");
  }
);



export const get = (url, params, pathVariable = null, loading = true) => {
  return axiosInstance.get(sf(url, pathVariable), {
    params: params,
    loading
  });
};

export const postFile = (url, data, pathVariable = null, loading = true) =>
  axiosInstance.post(sf(url, pathVariable), data, {
    loading,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });

export const post = (url, data, pathVariable = null, loading = true) =>
  axiosInstance.post(sf(url, pathVariable), data, {
    loading
  });

export const put = (url, data, pathVariable = null, loading = true) =>
  axiosInstance.put(sf(url, pathVariable), data, {
    loading
  });

export const patch = (url, data, pathVariable = null, loading = true) =>
  axiosInstance.patch(sf(url, pathVariable), data, {
    loading
  });

export const del = (url, params, pathVariable = null, loading = true) =>
  axiosInstance.delete(sf(url, pathVariable), {
    params: params,
    loading
  });
