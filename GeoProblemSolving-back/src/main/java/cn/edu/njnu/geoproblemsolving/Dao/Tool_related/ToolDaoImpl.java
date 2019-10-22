package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.ToolEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class ToolDaoImpl implements IToolDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ToolDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String createTool(ToolEntity tool) {

        String toolName = tool.getToolName();
        Query query = Query.query(Criteria.where("toolName").is(toolName));

        if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {

            String tid = UUID.randomUUID().toString();
            tool.setTid(tid);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            tool.setCreateTime(dateFormat.format(date));

            // 有待测试-----------------------------------------------------------------------------
            // 创建tool时，如果选择了toolset，更新toolset离的toolList
            JSONArray toolsetList = tool.getToolsetInfo();
            for(int i=0;i<toolsetList.size();i++){

                ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);

                //遍历创建tool时选择的toolsetList的Id，找到对应的Toolset实体
                JSONObject toolObj = toolsetList.getJSONObject(i);
                List<ToolsetEntity> toolsets = (List<ToolsetEntity>)toolsetDao.readToolset("tsid", toolObj.getString("tsid"));
                ToolsetEntity updataToolset = toolsets.get(0);
                // 更新toolset
                JSONArray newToolList = updataToolset.getToolList();
                newToolList.add(toolObj);
                updataToolset.setToolList(newToolList);

                toolsetDao.updateToolsetbyToolset(updataToolset);
            }

            mongoTemplate.save(tool);
            return tid;
        }
        else {
            return "Duplicate naming";
        }

    }

    @Override
    public Object readTool(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {
            return "None";
        } else {
            List<ToolEntity> ToolEntities = mongoTemplate.find(query, ToolEntity.class);
            return ToolEntities;
        }
    }

    @Override
    public void deleteTool(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, ToolEntity.class);
    }

    @Override
    public String updateTool(HttpServletRequest request) {
        try {
            Query query = new Query(Criteria.where("tid").is(request.getParameter("tid")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, ToolEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String uploadPicture(HttpServletRequest request) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String ip = address.getHostAddress();
            String servicePath = request.getSession().getServletContext().getRealPath("/");
            if (!ServletFileUpload.isMultipartContent(request)) {
                System.out.println("File is not multimedia.");
                return "Fail";
            }
            Collection<Part> parts = request.getParts();
            String pathURL = "Fail";
            for (Part part : parts) {
                if (part.getName().equals("toolImg")) {
                    String fileNames = part.getSubmittedFileName();
                    String fileName = fileNames.substring(0, fileNames.lastIndexOf("."));
                    String suffix = fileNames.substring(fileNames.lastIndexOf(".") + 1);
                    String regexp = "[^A-Za-z_0-9\\u4E00-\\u9FA5]";
                    String saveName = fileName.replaceAll(regexp, "");
                    String folderPath = servicePath + "tool/picture";
                    File temp = new File(folderPath);
                    if (!temp.exists()) {
                        temp.mkdirs();
                    }
                    int randomNum = (int) (Math.random() * 10 + 1);
                    for (int i = 0; i < 5; i++) {
                        randomNum = randomNum * 10 + (int) (Math.random() * 10 + 1);
                    }
                    String newFileTitle = saveName + randomNum + "." + suffix;
                    String localPath = temp + "/" + newFileTitle;
                    System.out.println("图片上传到本地路径：" + localPath);
                    File file = new File(localPath);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    InputStream inputStream = part.getInputStream();
                    byte[] buffer = new byte[1024 * 1024];
                    int byteRead;
                    while ((byteRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();

                    String reqPath = request.getRequestURL().toString();
                    pathURL = reqPath.replaceAll("localhost", ip) + "/" + newFileTitle;
                    String regexGetUrl = "(/GeoProblemSolving[\\S]*)";
                    Pattern regexPattern = Pattern.compile(regexGetUrl);
                    Matcher matcher = regexPattern.matcher(pathURL);
                    if (matcher.find()) {
                        pathURL = matcher.group(1);
                    }
//                    System.out.println("图片的请求地址："+pathURL);
                    break;
                }
            }
            return pathURL;
        } catch (Exception e) {
            return "Fail";
        }
    }
}


