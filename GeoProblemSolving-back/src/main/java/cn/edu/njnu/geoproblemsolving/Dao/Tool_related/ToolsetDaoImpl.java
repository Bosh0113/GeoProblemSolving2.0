package cn.edu.njnu.geoproblemsolving.Dao.Tool_related;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ToolsetDaoImpl implements IToolsetDao {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ToolsetDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String createToolset(ToolsetEntity toolset) {

        String toolsetName = toolset.getToolsetName();
        Query query = Query.query(Criteria.where("toolsetName").is(toolsetName));

        if (mongoTemplate.find(query, ToolsetEntity.class).isEmpty()) {

            String tsid = UUID.randomUUID().toString();
            toolset.setTsid(tsid);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            toolset.setCreateTime(dateFormat.format(date));

            mongoTemplate.save(toolset);
            return tsid;
        }
        else {
            return "Duplicate naming";
        }
    }

    @Override
    public Object readToolset(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, ToolsetEntity.class).isEmpty()) {
            return "None";
        } else {
            List<ToolsetEntity> ToolsetEntities = mongoTemplate.find(query, ToolsetEntity.class);
            return ToolsetEntities;
        }
    }

    @Override
    public void deleteToolset(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query, ToolsetEntity.class);
    }

    @Override
    public String updateToolset(HttpServletRequest request) {
        try {
            Query query = new Query(Criteria.where("tsid").is(request.getParameter("tsid")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, ToolsetEntity.class);
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
                if (part.getName().equals("toolsetImg")) {
                    String fileNames = part.getSubmittedFileName();
                    String fileName = fileNames.substring(0, fileNames.lastIndexOf("."));
                    String suffix = fileNames.substring(fileNames.lastIndexOf(".") + 1);
                    String regexp = "[^A-Za-z_0-9\\u4E00-\\u9FA5]";
                    String saveName = fileName.replaceAll(regexp, "");
                    String folderPath = servicePath + "toolset/picture";
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

    @Override
    // 有待测试---------------------------------------------------------
    public String updateToolsetbyToolset(ToolsetEntity toolset) {
        try {
            Query query = new Query(Criteria.where("tsid").is(toolset.getTsid()));
            CommonMethod method = new CommonMethod();
            Update update = new Update();
            update.set("toolList",toolset.getToolList());
            mongoTemplate.updateFirst(query, update, ToolsetEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }
}
