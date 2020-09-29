package cn.edu.njnu.geoproblemsolving.Socket.VideoChatSignalSocket;

import com.alibaba.fastjson.JSONObject;

public class VCSMessage {

    private String type;

    private JSONObject userInfo;

    private JSONObject content;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    public void setContent(JSONObject content) {
        this.content = content;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setUserInfo(JSONObject userInfo) {
        this.userInfo = userInfo;
    }

    public JSONObject getUserInfo() {
        return userInfo;
    }
}