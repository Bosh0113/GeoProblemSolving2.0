package cn.edu.njnu.geoproblemsolving.comparison.utils;

import cn.edu.njnu.geoproblemsolving.comparison.constant.HttpContant;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 22:15 2019/9/4
 * @Modified By:
 **/
public class MyHttpUtils {

    public static JSONObject myGet(String urlString) throws IOException, URISyntaxException {
        String body="";
        CloseableHttpClient client = HttpClients.createDefault();
//        String requestUrl = url;
        URL url=new URL(urlString);
        URI uri=new URI(url.getProtocol(), url.getHost()+":"+url.getPort(), url.getPath(), url.getQuery(),null);
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity responseEntity = response.getEntity();
        int statusCode= response.getStatusLine().getStatusCode();
        if(statusCode == 200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
            StringBuffer buffer = new StringBuffer();
            String str = "";
            while(!StringUtils.isEmpty(str = reader.readLine())) {
                buffer.append(str);
            }
            body = buffer.toString();
        }else{
            String content = EntityUtils.toString(responseEntity);
            System.out.println(content);
        }
        //释放链接
        EntityUtils.consume(responseEntity);
        response.close();
        client.close();
        if("".equals(body)){
            return null;
        }
        JSONObject resObj = JSONObject.parseObject(body);
        if(!"1".equals(resObj.getString("code"))){
            return null;
        }
        return resObj.getJSONObject("data");
    }

    public static String post_JSONObj(String urlString, JSONObject jsonObj) throws IOException {
        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlString);
        httpPost.setHeader("Content-Type","application/json");
        StringEntity stringEntity = new StringEntity(jsonObj.toJSONString(),"UTF-8");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse httpResponse = client.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        int statusCode= httpResponse.getStatusLine().getStatusCode();
        if(statusCode == 200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
            StringBuffer buffer = new StringBuffer();
            String str = "";
            while(!StringUtils.isEmpty(str = reader.readLine())) {
                buffer.append(str);
            }
            body = buffer.toString();
        }
        //释放链接
        httpResponse.close();
        client.close();
        if("".equals(body)){
            return null;
        }
        JSONObject resObj = JSONObject.parseObject(body);
        if(!"1".equals(resObj.getString("code"))){
            System.out.println(resObj.getString("msg"));
            return null;
        }
        return resObj.getString("data");
    }

    public static String post_JSONStr(String urlString, JSONObject jsonObj) throws IOException {
        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlString);
        httpPost.setHeader("Content-Type","application/json");
        StringEntity stringEntity = new StringEntity(jsonObj.toJSONString(),"UTF-8");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse httpResponse = client.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        int statusCode= httpResponse.getStatusLine().getStatusCode();
        if(statusCode == 200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
            StringBuffer buffer = new StringBuffer();
            String str = "";
            while(!StringUtils.isEmpty(str = reader.readLine())) {
                buffer.append(str);
            }
            body = buffer.toString();
        }
        //释放链接
        httpResponse.close();
        client.close();
        return body;
    }

    public static String port_MEB(String urlString, MultipartEntityBuilder multipartEntityBuilder) throws IOException {
        String body = "";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlString);
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse httpRes = client.execute(httpPost);
        HttpEntity responseEntity = httpRes.getEntity();
        int statusCode= httpRes.getStatusLine().getStatusCode();
        if(statusCode == 200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
            StringBuffer buffer = new StringBuffer();
            String str = "";

            while(!StringUtils.isEmpty(str = reader.readLine())) {
                buffer.append(str);
            }
            body = buffer.toString();
        }
        //释放链接
        httpRes.close();
        client.close();
        if("".equals(body)){
            return null;
        }
        JSONObject resObj = JSONObject.parseObject(body);
        if(!"1".equals(resObj.getString("code"))){
            System.out.println(resObj.getString("msg"));
            return null;
        }
        return resObj.getString("data");
    }

    public static String POST(String url, String encode, Map<String, String> headers, Map<String, String> params,
                              List<MultipartFile> multipartFiles, String... m) throws IOException {
        String body = "";
        CloseableHttpClient client =null;
        if (client == null) {
            return "验证输入参数存在问题";
        }
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        //设置header
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(),entry.getValue());
            }
        }
        //构建body
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //文件数据
        if (multipartFiles != null&& multipartFiles.size() > 0) {
            for(MultipartFile multipartFile:multipartFiles){
                builder.addBinaryBody("file", multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, multipartFile.getOriginalFilename());
            }
        }
        //parameter
        ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));//解决中文乱码
        if (params != null&& params.size() > 0) {
            for (Map.Entry<String, String> key : params.entrySet()) {
                builder.addTextBody(key.getKey(), key.getValue(),contentType);
            }
        }

        HttpEntity entityIn = builder.build();
        //设置参数到请求对象中
        httpPost.setEntity(entityIn);


        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entityOut = response.getEntity();
        if (entityOut != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entityOut, encode);
        }
        EntityUtils.consume(entityOut);
        //释放链接
        response.close();
        client.close();
        return body;
    }







//    public static String postToDataResource(String author,String fileName,String sourceStoreId,String suffix,String type) throws IOException, JSONException {
//        String body = "";
//        CloseableHttpClient client = HttpClients.createDefault();
//        String requestUrl ="http://" + Constant.DATA_CONTAINER_IP+":"+Constant.DATA_CONTAINER_PORT+Constant.DATA_CONTAINER_DATARESOURCE;
//        HttpPost httpPost = new HttpPost(requestUrl);
//        httpPost.setHeader("Content-Type","application/json");
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("author",author);
//        jsonObject.put("fileName",fileName);
//        jsonObject.put("sourceStoreId",sourceStoreId);
//        jsonObject.put("suffix",suffix);
//        jsonObject.put("type",type);
//
//        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),"UTF-8");
//
//        httpPost.setEntity(stringEntity);
//        CloseableHttpResponse httpResponse = client.execute(httpPost);
//        HttpEntity entity = httpResponse.getEntity();
//        if (entity != null) {
//            //按指定编码转换结果实体为String类型
//            body = EntityUtils.toString(entity, "UTF-8");
//        }
//        EntityUtils.consume(entity);
//        //释放链接
//        httpResponse.close();
//        client.close();
//
//        org.json.JSONObject bodyJson = new org.json.JSONObject(body);
//        int code = bodyJson.getInt("code");
//        if(code!=0){
//            return "";
//        }else{
//            return bodyJson.getString("data");
//        }
//    }
//
//    public static String postOutputFiletoDC(String recordId) throws IOException, JSONException {
//        String body = "";
//        CloseableHttpClient client = HttpClients.createDefault();
//        String requestUrl ="http://" + Constant.SAGA_SERVER_IP+":"+Constant.SAGA_SERVER_PORT+Constant.MODEL_RUN_OUTPUT+"/"+recordId;
//        HttpPost httpPost = new HttpPost(requestUrl);
//        httpPost.setHeader("Content-Type","application/json");
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("ip",Constant.DATA_CONTAINER_IP);
//        jsonObject.put("port",Constant.DATA_CONTAINER_PORT);
//        StringEntity stringEntity = new StringEntity(jsonObject.toJSONString(),"UTF-8");
//
//        httpPost.setEntity(stringEntity);
//        CloseableHttpResponse httpResponse = client.execute(httpPost);
//        HttpEntity entity = httpResponse.getEntity();
//        if (entity != null) {
//            //按指定编码转换结果实体为String类型
//            body = EntityUtils.toString(entity, "UTF-8");
//            System.out.println("post data to DC: "+body);
//        }
//        EntityUtils.consume(entity);
//        //释放链接
//        httpResponse.close();
//        client.close();
//
//        org.json.JSONObject bodyJson = new org.json.JSONObject(body);
//        int code = bodyJson.getInt("code");
//        if(code!=1){
//            return "";
//        }else{
//            return bodyJson.getString("data");
//        }
//    }
//
//
//
//
//
//    public static String getMeta(String id) throws IOException, URISyntaxException {
//        String body = "";
//        CloseableHttpClient client =HttpClients.createDefault();
//        String reqUrl = "http://"+Constant.DATA_CONTAINER_IP+":"+Constant.DATA_CONTAINER_PORT+Constant.DATA_CONTAINER_DATARESOURCE+"/"+id+Constant.DATA_CONTAINER_GETMETA;
//        URL url=new URL(reqUrl);
//        URI uri=new URI(url.getProtocol(), url.getHost()+":"+url.getPort(), url.getPath(), url.getQuery(),null);
//        HttpGet httpGet = new HttpGet(uri);
//        CloseableHttpResponse response = client.execute(httpGet);
//        HttpEntity entity = response.getEntity();
//        if (entity != null) {
//            //按指定编码转换结果实体为String类型
//            body = EntityUtils.toString(entity, "utf-8");
//        }
//        EntityUtils.consume(entity);
//
//        //释放链接
//        response.close();
//        client.close();
//
//        return body;
//    }
//
//
//
//
//    /**
//     * 将response的InputStream写到指定basicPath路径中去
//     *
//     * @param basicPath 存储的目录
//
//     * @return 返回Reponse中的文件名
//     * @throws IOException
//     */
//    public static String getMyFile(String basicPath, String urlString) throws IOException {
//        URL url=new URL(urlString);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
//        connection.setDoInput(true);
//        connection.setDoOutput(true);
//        connection.setRequestMethod("GET");
//        connection.setConnectTimeout(30000);
//        connection.setReadTimeout(30000);
//
//        //attachment; filename=fileName
//        String Content = connection.getHeaderField("Content-Disposition");
//        String filename = Content.substring(StringUtils.lastIndexOf(Content, "=") + 1);
//        MyFileUtils.writeInputStreamToFile(connection.getInputStream(), new File(basicPath + filename));
//        return filename;
//    }


}
