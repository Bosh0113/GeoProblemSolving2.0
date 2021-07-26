package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class HttpHelper {
	
	/**
	 * @param url
	 * @param param 请求参数必须是name1=value1&name2=value2 的形式
	 * @return
	 */
	public static String request_get(String url, String param){
		String urlName = "";
		if(param == null){
			urlName = url;
		}else{
			urlName = url + "?" + param;
		}
		
		try{
			URL realUrl = new URL(urlName);
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			//设置通用属性
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			
			//建立连接
			conn.connect();
			//获取内容
			BufferedReader reader = null;
			StringBuffer resultBuffer = new StringBuffer();
			String tempLine = null;
			
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			while ((tempLine = reader.readLine()) != null){
				resultBuffer.append(tempLine).append("\n");
			}
			reader.close();
			return resultBuffer.toString();
		}catch(MalformedURLException e){
			 e.printStackTrace();
		}catch(IOException e){
			System.out.println("send get request error! " + e);
			e.printStackTrace();
		}
		finally{
			
		}
		return null;
	}
	
	public static String request_post(String posturl, String params){
		try{
			URL url = new URL(posturl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//设置参数
			conn.setRequestMethod("POST");
			 // 设置允许输入流输入数据到本机， post请求必须设置为true
			conn.setDoOutput(true);
            // 设置允许输出流输出数据到服务器
			conn.setDoInput(true);
			//是否可以使用缓存， 不使用缓存
			conn.setUseCaches(false);
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			
		    //设置请求正文
			if(params!= null){
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()));
				pw.print(params);
				pw.flush();
				pw.close();
			}
			//建立连接
			conn.connect();
			
			//远程对象变为可用。远程对象的头字段和内容变为可访问
			//获取响应头
			Map<String,List<String>> headers = conn.getHeaderFields();
			System.out.println(headers);
			
			//获取响应正文
			BufferedReader reader = null;
			StringBuffer resultBuffer = new StringBuffer();
			String tempLine = null;
			
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((tempLine = reader.readLine()) != null){
				resultBuffer.append(tempLine).append("\n");
			}
			reader.close();
			return resultBuffer.toString();
		}catch (MalformedURLException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		finally{
			
		}
		return null;
	}
	
	/**
	 * @param actionURL
	 * @param fileMap  请求参数hashmap类型
	 * @param textMap  请求参数hashmap类型
	 * @return
	 */
	public static String request_post_file(String actionURL,Map<String,String> fileMap,Map<String,String> textMap){
		// 定义分界线字符串
		String boundary = "---------------------------123821742118716";
		HttpURLConnection connection = null;
		String res = "";
		try {
			URL url = new URL(actionURL);
			connection = (HttpURLConnection) url.openConnection();
			// 发送post请求需要设置下面两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 设置请求参数
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(30000);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			// 设置字节编码
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

			connection.connect();

			// 获取请求内容输出流
			OutputStream out = new DataOutputStream(connection.getOutputStream());

			// 开始写表单格式
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file

			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					// 全部以流的形式进行传输
					String contentType = "application/octet-stream";
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type: " + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回值
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Request failed!");
		} finally {
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
		return res;
	}
	
	public static String request_post_json(String actionURL, JSONObject jsonParam){
		String res = "";
		HttpURLConnection connection = null;
		try{
			URL url = new URL(actionURL);
			connection = (HttpURLConnection) url.openConnection();
			// 发送post请求需要设置下面两行
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 设置请求参数
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(30000);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			// 设置字节编码
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			//转化为字节数组
			byte[] data = (jsonParam.toString()).getBytes();
			//设置文件长度
			connection.setRequestProperty("Content-Length", String.valueOf(data.length));
			
			//设置文件类型
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			
			//设置接收类型
			connection.setRequestProperty("aceept", "application/json");
			
			//开始连接请求
			connection.connect();
			
			OutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			
			//写入请求的字符串
			outputStream.write(data);
			outputStream.flush();
			outputStream.close();
			
			
			// 读取返回值
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
					strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;			
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Request failed!");
		}finally {
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
		return res;
	}
		
	/**
	 * @param urlAddress
	 * @param filepath  保存文件的路径(为具体路径，不为目录路径)
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public static File downloadFile(String urlAddress, String filepath, String method) throws IOException{
		File file = new File(filepath);
		//判断文件是否存在
		if(!file.exists()){
        	if(!file.getParentFile().exists()){
        		file.getParentFile().mkdirs();
        	}
        	file.createNewFile();
        }
		
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try{
			URL url = new URL(urlAddress);
			conn = (HttpURLConnection) url.openConnection();
			 // 设置允许输入流输入数据到本地
			conn.setDoInput(true);
            // 设置允许输出流输出到服务器
			conn.setDoOutput(true);
			conn.setRequestMethod(method);
			conn.setUseCaches(false);
			
			conn.connect();
			//获取网络输入流
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			
			fileOut = new FileOutputStream(filepath);
			
			byte[] buf = new byte[1024];
			//定义整形变量来累计当前读取到的文件长度
			int size;
			
			while((size = bis.read(buf)) != -1){
				fileOut.write(buf, 0, size);
			}
			//关闭输出流
			fileOut.close();
			//关闭输入缓冲区
			bis.close();
			
			//返回文件对象
			return file;
			
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String request_put(String puturl,String param,String obj)throws IOException, JSONException{
		//创建连接
		String urlName = "";
		if(param == null){
			urlName = puturl;
		}else{
			urlName = puturl + "?" + param;
		}
		URL url = new URL(urlName);
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) url.openConnection();
			// 设置参数
			connection.setRequestMethod("PUT");
			// 设置允许输入流输入数据到本机， post请求必须设置为true
			connection.setDoOutput(true);
			// 设置允许输出流输出数据到服务器
			connection.setDoInput(true);
			// 是否可以使用缓存， 不使用缓存
			connection.setUseCaches(false);
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

			connection.connect();

			if (obj != null) {
				OutputStream out = connection.getOutputStream();
				out.write(obj.toString().getBytes());
				out.flush();
				out.close();
			}

			int code = connection.getResponseCode();
			if (code == 200) {
				// 获取响应正文
				BufferedReader reader = null;
				StringBuffer resultBuffer = new StringBuffer();
				String tempLine = null;

				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
				while ((tempLine = reader.readLine()) != null) {
					resultBuffer.append(tempLine).append("\n");
				}
				reader.close();
				return resultBuffer.toString();
			} else {
				System.out.println("put error");
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return null;
	}

}
