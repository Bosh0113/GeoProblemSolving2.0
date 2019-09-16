package cn.edu.njnu.geoproblemsolving.comparison.service;

import cn.edu.njnu.geoproblemsolving.comparison.constant.HttpContant;
import cn.edu.njnu.geoproblemsolving.comparison.utils.MyHttpUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import javax.servlet.http.Part;
import java.io.IOException;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 16:25 2019/9/14
 * @Modified By:
 **/
public class CmpDataService {

    public static String matchDataMd5(String md5) throws IOException {
        String url = "http://"+ HttpContant.DATA_CONTAINER_IP+":"+HttpContant.DATA_CONTAINER_PORT+"/file/fastUpload/"+md5;
        JSONObject md5Json = new JSONObject();
        md5Json.put("md5",md5);
        String res = MyHttpUtils.post_JSONStr(url, md5Json);
        return res;
    }

    public static String uploadDataToDC(String md5, Part filePart, String author, String type) throws IOException {
        String url = "http://"+ HttpContant.DATA_CONTAINER_IP+":"+HttpContant.DATA_CONTAINER_PORT+"/file/upload/store_dataResource_files";
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addBinaryBody("file",filePart.getInputStream(), ContentType.MULTIPART_FORM_DATA,filePart.getSubmittedFileName());
        multipartEntityBuilder.addTextBody("md5", md5);
        multipartEntityBuilder.addTextBody("type","store_dataResource_files");
        String sourceStoreId = MyHttpUtils.port_MEB(url, multipartEntityBuilder);
        if(sourceStoreId==null){
            return null;
        }
        String fileName = filePart.getSubmittedFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String res = toDataResource(author, fileName, sourceStoreId, suffix, type);
        return res;
    }

    public static String uploadDataToDCFast(String md5,String fileName,String author,String sourceStoreId,String suffix,String type) throws IOException {
        String res = toDataResource(author, fileName, sourceStoreId, suffix, type);
        return res;
    }

    private static String toDataResource(String author,String fileName,String sourceStoreId,String suffix,String type) throws IOException {
        String url = "http://"+ HttpContant.DATA_CONTAINER_IP+":"+HttpContant.DATA_CONTAINER_PORT+"/dataResource";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("author",author);
        jsonObject.put("fileName",fileName);
        jsonObject.put("sourceStoreId",sourceStoreId);
        jsonObject.put("suffix",suffix);
        jsonObject.put("type",type);
        String res = MyHttpUtils.post_JSONStr(url, jsonObject);
        return res;
    }
}
