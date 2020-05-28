//package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;
//
//import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
//import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ModelItem.Support.Event;
//import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ModelItem.Support.IOFlagEnum;
//import cn.edu.njnu.geoproblemsolving.Entity.ToolEntity;
//import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Component;
//import springfox.documentation.spring.web.json.Json;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.Part;
//import java.beans.IntrospectionException;
//import java.beans.PropertyDescriptor;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.InetAddress;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//
//@Component
//public class ToolDaoImpl implements IToolDao {
//
//    private final MongoTemplate mongoTemplate;
//
//    @Autowired
//    public ToolDaoImpl(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    @Override
//    public Object createTool(ToolEntity tool) {
//
//        String toolName = tool.getToolName();
//        Query query = Query.query(Criteria.where("toolName").is(toolName));
//
//        if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {
//
//            String tId = UUID.randomUUID().toString();
//            tool.setTId(tId);
//            Date date = new Date();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            tool.setCreateTime(dateFormat.format(date));
//            mongoTemplate.save(tool);
//            return tool;
//        }
//        else {
//            return "Duplicate naming";
//        }
//
//    }
//
//    @Override
//    public Object readTool(String key, String value) {
//        Query query = Query.query(Criteria.where(key).is(value));
//        if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {
//            return "None";
//        } else {
//            List<ToolEntity> ToolEntities = mongoTemplate.find(query, ToolEntity.class);
//            return ToolEntities;
//        }
//    }
//
//    @Override
//    public Object readAccessibleTools(String userId) {
//        try {
//            Criteria criteriaOwnedTool = Criteria.where("provider").is(userId);
//            Query queryTools = Query.query(criteriaOwnedTool);
//            List<ToolEntity> toolEntityList = mongoTemplate.find(queryTools, ToolEntity.class);
//            if (!toolEntityList.isEmpty()) {
//                return toolEntityList;
//            } else {
//                return "None";
//            }
//        } catch (Exception e) {
//            return "Fail";
//        }
//    }
//
//    @Override
//    public void deleteTool(String key, String value) {
//        Query query = Query.query(Criteria.where(key).is(value));
//        mongoTemplate.remove(query, ToolEntity.class);
//    }
//
////    public Update getUpdate(ToolEntity toolEntity) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
////        Update update=new Update();
////        Field[] fields = toolEntity.getClass().getDeclaredFields();
////        for (Field field : fields) {
////            Method getter = new PropertyDescriptor(field.getName(), toolEntity.getClass()).getReadMethod();
////            Object invoke = getter.invoke(toolEntity);
////            if(invoke==null){
////                update.set(,)
////            }else{
////
////            }
////        }
////
////    }
//
//    @Override
//    public String updateTool(HttpServletRequest request ) {
//        Query query = new Query(Criteria.where("tId").is(request.getParameter("tId")));
//        CommonMethod method = new CommonMethod();
//        Update update = method.setUpdate(request);
//        mongoTemplate.updateFirst(query, update, ToolEntity.class);
//        return "Success";
//
////            Query query = new Query(Criteria.where("tId").is(toolEntity.getTId()));
////            Update update= new Update();
////
////            Map maps= (Map) toolEntity;
//////            for (Object obj : toolEntity) {
//////                System.out.println("key值="+obj.getClass());
////////                System.out.println("对应key值的value="+obj.getValue());
//////            }
////            for(Object key :  maps.keySet()){
////                Object val = maps.get(key.toString());
////                if(val != null){
////                    update.set(key.toString(),val);
////                }
////            }
////            ToolEntity tool = ToolEntity.builder()
////                    .toolName(toolEntity.getToolName())
////                    .description(toolEntity.getDescription())
////                    .categoryTag(toolEntity.getCategoryTag())
////                    .toolUrl(toolEntity.getToolUrl())
////                    .detail(toolEntity.getDetail())
////                    .privacy(toolEntity.getPrivacy())
////                    .modelInfo(toolEntity.getModelInfo())
////                    .recomStep(toolEntity.getRecomStep())
////                    .toolImg(toolEntity.getToolImg())
////                    .build();
////
////
////            mongoTemplate.updateFirst(query, update, ToolEntity.class);
//
//
//    }
//
//
//
//}
//
//
