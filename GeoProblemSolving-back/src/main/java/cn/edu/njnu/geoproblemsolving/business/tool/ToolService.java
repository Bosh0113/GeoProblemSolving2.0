package cn.edu.njnu.geoproblemsolving.domain.tool;

import cn.edu.njnu.geoproblemsolving.Enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.Exception.MyException;
import cn.edu.njnu.geoproblemsolving.domain.tool.dto.AddToolEntityDTO;
import cn.edu.njnu.geoproblemsolving.domain.tool.dto.UpdateToolEntityDTO;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Zhiyi
 * @Date 2020/5/27  22:57
 * @Version 1.0.0
 */
@Service
public class ToolService  {
    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public ToolEntity createTool(AddToolEntityDTO add) {
        toolRepository.findFirstByToolName(add.getToolName()).ifPresent((el)->{
            throw new MyException(ResultEnum.EXISTS_OBJECT);
        });
        ToolEntity toolEntity = new ToolEntity();
        String tid = UUID.randomUUID().toString();
        add.setTid(tid);
        add.convertTo(toolEntity);
        return toolRepository.insert(toolEntity);
    }

    public void deleteByTid(String tid) {
        toolRepository.deleteById(tid);
    }

    public List<ToolEntity> findAllByProvider(String provider) {
        return toolRepository.findAllByProvider(provider);
    }

    public Object updateTool(String tid,UpdateToolEntityDTO update) {
        ToolEntity toolEntity = toolRepository.findFirstByTid(tid).orElseThrow(MyException::noObject);
        update.updateTo(toolEntity);
        return toolRepository.save(toolEntity);
    }

    public List<ToolEntity> readTool(String key, String value) {
        Query query = Query.query(Criteria.where(key).is(value));
        if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {
            return Collections.emptyList();
        } else {
            List<ToolEntity> ToolEntities = mongoTemplate.find(query, ToolEntity.class);
            return ToolEntities;
        }
    }

    public static String uploadPicture(HttpServletRequest request) {
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
                    break;
                }
            }
            return pathURL;
        } catch (Exception e) {
            return "Fail";
        }
    }


}
