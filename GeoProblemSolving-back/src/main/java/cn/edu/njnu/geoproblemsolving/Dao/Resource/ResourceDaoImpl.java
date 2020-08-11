package cn.edu.njnu.geoproblemsolving.Dao.Resource;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.UploadResult;
import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Component
public class ResourceDaoImpl implements IResourceDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ResourceDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Object saveResource(HttpServletRequest request) {
        String folderName = request.getParameter("uploaderId");
        UploadResult uploadInfos = new UploadResult();
        uploadInfos.uploaded = new ArrayList<>();
        uploadInfos.sizeOver = new ArrayList<>();
        uploadInfos.failed = new ArrayList<>();
        try {
            if (!ServletFileUpload.isMultipartContent(request)) {
                System.out.println("File is not multimedia.");
                return "Fail";
            }
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String uploadTime = dateFormat.format(date);

            String servicePath = request.getSession().getServletContext().getRealPath("/");//获得绝对路径。
            Collection<Part> parts = request.getParts();

            // 保存缩略图，获取路径
            String thumbnailPath = "";
            for (Part part : parts) {
                if(part.getName().equals("thumbnail")){
                    if (part.getSize() < 1024 * 1024) {
                        String fileNames = part.getSubmittedFileName();

                        thumbnailPath = saveFiles(part, fileNames, servicePath, folderName);
                        if(thumbnailPath.equals("ERROR")){
                            uploadInfos.failed.add(part.getSubmittedFileName());
                            continue;
                        }

                    } else {
                        uploadInfos.sizeOver.add(part.getSubmittedFileName());
                    }
                }
            }
            // 保存文件
            for (Part part : parts) {
                try {
                    if (part.getName().equals("file")) {
                        if (part.getSize() < 1024 * 1024 * 1024) {
                            String fileNames = part.getSubmittedFileName();

                            // 保存文件，获取返回文件路径
                            String pathURL = saveFiles(part, fileNames, servicePath, folderName);
                            if(pathURL.equals("ERROR")){
                                uploadInfos.failed.add(part.getSubmittedFileName());
                                continue;
                            }

                            // 如果是zip文件
//                            if(suffix.equals("zip")){
//                                try {
//                                    ArrayList<String> fileInZip = new ArrayList<>();
//                                    ZipFile zipFile = new ZipFile(localPath);
//                                    Enumeration enumeration = zipFile.entries();
//                                    while (enumeration.hasMoreElements()){
//                                        ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
//                                        String fn = zipEntry.getName();
//                                        String[] nameArray = fn.split("\\\\");
//                                        fn = nameArray[nameArray.length - 1];
//                                        fileInZip.add(fn);
//                                    }
//                                }
//                                catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }

                            // 生成resourceId
                            String resourceId = UUID.randomUUID().toString();
                            // 计算文件大小
                            String fileSize;
                            DecimalFormat df = new DecimalFormat("##0.00");
                            if (part.getSize() > 1024 * 1024) {
                                fileSize = df.format((float) part.getSize() / (float) (1024 * 1024)) + "MB";
                            } else {
                                fileSize = df.format((float) part.getSize() / (float) (1024)) + "KB";
                            }


                            ResourceEntity resourceEntity = new ResourceEntity();
                            resourceEntity.setResourceId(resourceId);

                            Query queryUser = Query.query(Criteria.where("userId").is(request.getParameter("uploaderId")));
                            UserEntity uploader = mongoTemplate.findOne(queryUser, UserEntity.class);

                            resourceEntity.setName(fileNames);
                            resourceEntity.setDescription(request.getParameter("description"));
                            resourceEntity.setPrivacy(request.getParameter("privacy"));
                            resourceEntity.setType(request.getParameter("type"));
                            resourceEntity.setThumbnail(thumbnailPath);
                            resourceEntity.setFileSize(fileSize);
                            resourceEntity.setPathURL(pathURL);
                            resourceEntity.setUploaderId(uploader.getUserId());
                            resourceEntity.setUploaderName(uploader.getUserName());
                            resourceEntity.setUploadTime(uploadTime);
                            resourceEntity.setEditToolInfo(request.getParameter("editToolInfo"));
                            mongoTemplate.save(resourceEntity);
                            uploadInfos.uploaded.add(resourceEntity);
                        } else {
                            uploadInfos.sizeOver.add(part.getSubmittedFileName());
                        }
                    }
                } catch (Exception e) {
                    uploadInfos.failed.add(part.getSubmittedFileName());
                }
            }
            return uploadInfos;
        } catch (Exception e) {
            return uploadInfos;
        }
    }

    @Override
    public Object readResource(String key, String value) {
        try {
            Query query = new Query(Criteria.where(key).is(value));

            if (!mongoTemplate.find(query, ResourceEntity.class).isEmpty()) {

                List<ResourceEntity> resourceEntities = mongoTemplate.find(query, ResourceEntity.class);

                return resourceEntities;
            } else {
                return "None";
            }
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object readPublicResource() {
        try {
            Query query = Query.query(Criteria.where("privacy").is("public"));
            return mongoTemplate.find(query, ResourceEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object updateResource(HttpServletRequest request) {

        UploadResult uploadInfos = new UploadResult();
        uploadInfos.uploaded = new ArrayList<>();
        uploadInfos.sizeOver = new ArrayList<>();
        uploadInfos.failed = new ArrayList<>();

        String resourceId = request.getParameter("resourceId");
        Query query = new Query(Criteria.where("resourceId").is(resourceId));
        Update update = new Update();

        try {
            // 判断请求中是否包含文件流，如果包含，保存文件，更新资源的文件路径
            if (ServletFileUpload.isMultipartContent(request)) {

                String servicePath = request.getSession().getServletContext().getRealPath("/");//获得绝对路径。
                ResourceEntity resourceEntity = mongoTemplate.findOne(query, ResourceEntity.class);
                String folderName = resourceEntity.getUploaderId();

                Collection<Part> parts = request.getParts();
                // 保存缩略图，获取路径
                String thumbnailPath = resourceEntity.getThumbnail();
                for (Part part : parts) {
                    if(part.getName().equals("thumbnail")){
                        if (part.getSize() < 1024 * 1024) {

                            thumbnailPath = saveFiles(part, part.getSubmittedFileName(), servicePath, folderName);
                            if(thumbnailPath.equals("ERROR")){
                                uploadInfos.failed.add(part.getSubmittedFileName());
                                continue;
                            }
                        } else {
                            uploadInfos.sizeOver.add(part.getSubmittedFileName());
                        }
                    }
                }
                // 保存文件
                for (Part part : parts) {
                    try {
                        if (part.getName().equals("file")) {
                            if (part.getSize() < 1024 * 1024 * 1024) {
                                String fileNames = part.getSubmittedFileName();

                                String pathURL = saveFiles(part, fileNames, servicePath, folderName);
                                if(pathURL.equals("ERROR")) {
                                    uploadInfos.failed.add(part.getSubmittedFileName());
                                    continue;
                                }
                                // 如果是zip文件
                                //                            if(suffix.equals("zip")){
                                //                                try {
                                //                                    ArrayList<String> fileInZip = new ArrayList<>();
                                //                                    ZipFile zipFile = new ZipFile(localPath);
                                //                                    Enumeration enumeration = zipFile.entries();
                                //                                    while (enumeration.hasMoreElements()){
                                //                                        ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
                                //                                        String fn = zipEntry.getName();
                                //                                        String[] nameArray = fn.split("\\\\");
                                //                                        fn = nameArray[nameArray.length - 1];
                                //                                        fileInZip.add(fn);
                                //                                    }
                                //                                }
                                //                                catch (Exception ex) {
                                //                                    ex.printStackTrace();
                                //                                }
                                //                            }

                                String fileSize;
                                DecimalFormat df = new DecimalFormat("##0.00");
                                if (part.getSize() > 1024 * 1024) {
                                    fileSize = df.format((float) part.getSize() / (float) (1024 * 1024)) + "MB";
                                } else {
                                    fileSize = df.format((float) part.getSize() / (float) (1024)) + "KB";
                                }

                                Query queryUser = Query.query(Criteria.where("userId").is(request.getParameter("uploaderId")));
                                UserEntity uploader = mongoTemplate.findOne(queryUser, UserEntity.class);

                                Date date = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String uploadTime = dateFormat.format(date);

                                update.set("name", fileNames);
                                update.set("fileSize", fileSize);
                                update.set("pathURL", pathURL);
                                update.set("uploaderId", uploader.getUserId());
                                update.set("uploaderName", uploader.getUserName());
                                update.set("uploadTime", uploadTime);
                                update.set("thumbnail", thumbnailPath);
                                update.set("editToolInfo", request.getParameter("editToolInfo"));
                                mongoTemplate.updateFirst(query,update,ResourceEntity.class);
                                resourceEntity = mongoTemplate.findOne(query, ResourceEntity.class);
                                uploadInfos.uploaded.add(resourceEntity);

                                break;
                            }
                            else {
                                uploadInfos.sizeOver.add(part.getSubmittedFileName());
                            }
                        }
                    } catch (Exception e) {
                        uploadInfos.failed.add(part.getSubmittedFileName());
                    }
                }
                return uploadInfos;
            }
            else {
                CommonMethod commonMethod = new CommonMethod();
                update=commonMethod.setUpdate(request);
                mongoTemplate.updateFirst(query,update,ResourceEntity.class);
                return mongoTemplate.findOne(query, ResourceEntity.class);
            }

        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String deleteResource(String key, String value) {
        try {
            //文件系统内删除
            Query query = new Query(Criteria.where(key).is(value));
            mongoTemplate.remove(query, ResourceEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public void getZipResource(HttpServletRequest request, HttpServletResponse response, String zipName, String fileName) {
        response.setContentType("application/octet-stream");
        try {
            OutputStream outputStream = response.getOutputStream();
            String servicePath = request.getSession().getServletContext().getRealPath("/");
            String folderPath = servicePath + "resource/upload";
            File temp = new File(folderPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            String localPath = temp + "/" + zipName;
            ZipFile zipFile = new ZipFile(localPath);
            InputStream in = new BufferedInputStream(new FileInputStream(localPath));
            ZipInputStream zipInputStream = new ZipInputStream(in);
            ZipEntry ze;
            while ((ze = zipInputStream.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    if (ze.getName().contains(fileName)) {
                        InputStream zeInputStream = zipFile.getInputStream(ze);
                        IOUtils.copy(zeInputStream, outputStream);
                        break;
                    }
                }
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void packageToZip(HttpServletRequest request, HttpServletResponse response) {
        String fileURLsStr = request.getParameter("fileURLs");
        String[] fileURLs = fileURLsStr.split(",");
        List files = new ArrayList();
        for (String fileURL : fileURLs) {
            String regexGetUrl = "/GeoProblemSolving/([\\S]*)";
            Pattern regexPattern = Pattern.compile(regexGetUrl);
            Matcher matcher = regexPattern.matcher(fileURL);
            if (matcher.find()) {
                fileURL = matcher.group(1);
            }
//            fileURL = fileURL.replace("/","\\");
            String localPath = request.getSession().getServletContext().getRealPath("/") + fileURL;
            File file = new File(localPath);
            files.add(file);
        }
        try {
            String servicePath = request.getSession().getServletContext().getRealPath("/");
            String folderPath = servicePath + "resource/tempZIP/";
            File tempFolder = new File(folderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }
            String tempZIP = folderPath + UUID.randomUUID() + ".zip";
            File zipFile = new File(tempZIP);
            zipFile.createNewFile();
            response.reset();
            FileOutputStream fOut = new FileOutputStream(tempZIP);
            ZipOutputStream zipOut = new ZipOutputStream(fOut);
            for (Object file : files) {
                zipFile((File) file, zipOut);
            }
            zipOut.close();
            fOut.close();
            downloadZip(zipFile, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(File inputFile, ZipOutputStream outputStream) {
        try {
            if (inputFile.exists()) {
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    outputStream.putNextEntry(entry);
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, nNumber);
                    }
                    bins.close();
                    IN.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadZip(File file, HttpServletResponse response) {
        if (!file.exists()) {
            System.out.println("待压缩目录" + file + "不存在");
        } else {
            try {
                InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                response.reset();
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    File temp = new File(file.getPath());
                    temp.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String saveFiles(Part part, String fileNames, String servicePath, String folderName){

        String fileName = fileNames.substring(0, fileNames.lastIndexOf("."));
        String suffix = fileNames.substring(fileNames.lastIndexOf(".") + 1);
        String regexp = "[^A-Za-z_0-9\\u4E00-\\u9FA5]";
        String saveName = fileName.replaceAll(regexp, "");
        String folderPath = servicePath + "resource/" + folderName;
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
        System.out.println("资源上传到本地路径：" + localPath);
        File file = new File(localPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStream = part.getInputStream();
            byte[] buffer = new byte[1024 * 1024];
            int byteRead;
            while ((byteRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileOutputStream.close();
            inputStream.close();
        }
        catch (IOException ex){
            return "ERROR";
        }

        String pathURL = "/GeoProblemSolving/resource/" + folderName + "/" + newFileTitle;

        return pathURL;
    }

    @Override
    public String uploadProjectPic(HttpServletRequest request) {
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
                if (part.getName().equals("picture")) {
                    String fileNames = part.getSubmittedFileName();
                    String fileName = fileNames.substring(0, fileNames.lastIndexOf("."));
                    String suffix = fileNames.substring(fileNames.lastIndexOf(".") + 1);
                    String regexp = "[^A-Za-z_0-9\\u4E00-\\u9FA5]";
                    String saveName = fileName.replaceAll(regexp, "");
                    String folderPath = servicePath + "project/picture";
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