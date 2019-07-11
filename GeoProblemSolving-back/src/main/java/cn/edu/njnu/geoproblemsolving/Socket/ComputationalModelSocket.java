package cn.edu.njnu.geoproblemsolving.Socket;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ComputationalModel/{groupID}")
public class ComputationalModelSocket {
    private Session session=null;
    private static final Map<String, Set<ComputationalModelSocket>> groups=new ConcurrentHashMap<>();
    private static JSONObject members=new JSONObject();
    private static JSONObject Controller=new JSONObject();
    private static final Map<String, ArrayList<String>> requireLists=new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(@PathParam("groupID") String groupID, Session session){
        this.session=session;
        if (!groups.containsKey(groupID)){
            Set<ComputationalModelSocket> group=new HashSet<>();
            group.add(this);
            groups.put(groupID,group);
        }
        else {
            groups.get(groupID).add(this);
        }
        if (!requireLists.containsKey(groupID)){//群组若不存在请求队列则新建
            ArrayList<String> list=new ArrayList<>();
            requireLists.put(groupID,list);
        }
    }

    @OnMessage
    public void onMessage(@PathParam("groupID") String groupID,String message)
    {
        JSONObject messageObject=JSONObject.parseObject(message);
//        System.out.println("客户端发送的数据:"+messageObject.getString("message"));
        String messageType=messageObject.getString("messageType");
        if (messageType.equals("Message")){
            try
            {
                //向客户端发送消息
                for (ComputationalModelSocket server:groups.get(groupID))
                {
                    if (!this.equals(server)){
                        server.session.getBasicRemote().sendText(message);
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if (messageType.equals("Join")){
            String userName=messageObject.getString("message");
            if(groups.get(groupID).size()<2){//建组成员为演示者
                Controller.put(groupID,userName);
            }
            members.put(this.session.getId(),userName);
            publicMembers(groupID,"Join",requireLists);//公布新的在线数组和演示者
        }
        else if (messageType.equals("Authority")){
            if (messageObject.getString("message").equals("Require")){
                requireLists.get(groupID).add(messageObject.getString("userName"));
                if(Controller.getString(groupID).equals("7014115d-2054-4c5e-99ed-ed786574cd32")){
                    ReGrant(groupID);//重新赋予权限
                }
                else {
                    publicMembers(groupID,"Authority",requireLists);//公布新的请求队列和演示者
                }
            }
            else if (messageObject.getString("message").equals("Release")){
                ReGrant(groupID);//重新赋予权限
            }
        }
    }
    @OnClose
    public void onClose(@PathParam("groupID") String groupID)
    {
        groups.get(groupID).remove(this);
        System.out.println("连接已经关闭,sessionID:"+this.session.getId()+" 用户名："+members.getString(this.session.getId()));
        String nowUser=members.getString(this.session.getId());
//        members.remove(this.session.getId());
        if(Controller.getString(groupID).equals(nowUser)){//若退出的用户为演示者则重新分配权限
            ReGrant(groupID);
        }
        publicMembers(groupID,"Left",requireLists);//发布在线用户群组及演示者
    }
    @OnError
    public void onError(Session session,Throwable error)
    {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    //发送在线用户清单
    private void publicMembers(@PathParam("groupID") String groupID,String messageType,Map requireLists){
        List<String> memberName=new ArrayList<>();
        JSONObject messageMember=new JSONObject();
        for (ComputationalModelSocket server:groups.get(groupID))
        {
            memberName.add(members.getString(server.session.getId()));
        }
        messageMember.put("messageType",messageType);
        messageMember.put("controller",Controller.getString(groupID));
        messageMember.put("requireList",requireLists.get(groupID).toString());
        messageMember.put("message", Arrays.toString(memberName.toArray()));
        try
        {
            //向客户端发送消息
            for (ComputationalModelSocket server:groups.get(groupID))
            {
                server.session.getBasicRemote().sendText(messageMember.toString());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void ReGrant(String groupID){//重新设置演示者
        if (!requireLists.get(groupID).isEmpty()){//若请求队列的请求用户不为空
            String firstUser=requireLists.get(groupID).get(0);
            boolean exist=false;
            for (ComputationalModelSocket server:groups.get(groupID)){//从群组的在线人员里找是否有申请者
                if (firstUser.equals(members.getString(server.session.getId()))){
                    exist=true;//用户存在
                    break;
                }
            }
            if (exist){
                Controller.put(groupID,requireLists.get(groupID).get(0));//将权限赋予队首用户
                requireLists.get(groupID).remove(0);
                publicMembers(groupID,"Authority",requireLists);//公布新的请求队列和演示者
            }
            else {//用户不存在，删除队首用户重新赋予权限
                requireLists.get(groupID).remove(0);
                ReGrant(groupID);//再来一遍判断请求队列
            }
        }
        else {//若请求队列的请求用户为空，则权限搁置
            Controller.put(groupID,"7014115d-2054-4c5e-99ed-ed786574cd32");//设置权限者为空
            publicMembers(groupID,"Authority",requireLists);//公布新的请求队列和演示者
        }
    }
}
