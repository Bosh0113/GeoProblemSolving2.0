package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Controller;


import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Domain.Node;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebServlet(name = "parseOsmDataServlet",urlPatterns = "/parseOsmDataServlet")
public class parseOsmDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("utf-8");

        JSONObject respJson = new JSONObject();
        PrintWriter out = resp.getWriter();
        String osmDataString = req.getParameter("osmData");
//        String dataDir = req.getParameter("dataDir");

        String dataDir = "E:\\WebApp\\TrafficNoiseTheme\\out\\artifacts\\TrafficNoiseTheme_war_exploded\\data\\";
        String times = String.valueOf(new Date().getTime());

        Document document = null;

        try {
            document = DocumentHelper.parseText(osmDataString);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        Map<String, Node> allNode = new HashMap<String, Node>();

//		将所有节点加入字典
        for (Iterator<Element> it = root.elementIterator("node"); it.hasNext();) {
            Element node = it.next();
            double nodeLat = Double.parseDouble(node.attributeValue("lat"));
            double nodeLon = Double.parseDouble(node.attributeValue("lon"));
            allNode.put(node.attributeValue("id"), new Node(nodeLat, nodeLon));
        }

//		处理道路
        File roadJSONFile = new File(dataDir + "road_"+ times +".json");
        roadJSONFile.deleteOnExit();
        roadJSONFile.createNewFile();
        FileOutputStream roadOutputStream = new FileOutputStream(roadJSONFile, true);
        StringBuffer roadStringBuffer = new StringBuffer();
        StringBuffer buildingStringBuffer = new StringBuffer();
//		geojson的头
        String roadJSONString = "{\"type\":\"FeatureCollection\", \"features\": [";

//		处理建筑物
        File buildingJSONFile = new File(dataDir + "building_"+ times +".json");
        buildingJSONFile.deleteOnExit();
        buildingJSONFile.createNewFile();
        FileOutputStream buildingOutputStream = new FileOutputStream(buildingJSONFile, true);
//		geojson的头
        String buildingJSONString = "{\"type\":\"FeatureCollection\", \"features\": [";

//		遍历所有way节点，如果是道路，直接拼成geojson，否则加入allway字典，供后续building使用
        for (Iterator<Element> it = root.elementIterator("way"); it.hasNext();) {
            Element way = it.next();
            String wayID = way.attributeValue("id");
            int isHighway = 0;
            for (Iterator<Element> tagIt = way.elementIterator("tag"); tagIt.hasNext();) {
                String k = tagIt.next().attributeValue("k");
//				如果这个way元素时道路，则直接拼接geojson字符串
                if (k.equals("highway")) {
                    isHighway = 1;
                    roadJSONString += "{\"type\":\"Feature\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[";
                    roadStringBuffer.append(roadJSONString);
                    roadOutputStream.write(roadStringBuffer.toString().getBytes("utf-8"));
//						清空buffer和string
                    roadStringBuffer.setLength(0);
                    roadJSONString = "";
                    for (Iterator<Element> ndIt = way.elementIterator("nd"); ndIt.hasNext();) {
                        String nodeId = ndIt.next().attributeValue("ref");
                        Node node = allNode.get(nodeId);
                        if (node != null) {
                            String lat = String.valueOf(node.getLat());
                            String lon = String.valueOf(node.getLon());
                            roadJSONString += "[" + lon + "," + lat + "],";
                        }
//						只要有点不存在，就不要这条道路
                        else {
                            roadJSONString = "";
                            break;
                        }
                    }
                    roadJSONString = roadJSONString.substring(0, roadJSONString.length() - 1);
                    roadJSONString += "]}}";
                    roadStringBuffer.append(roadJSONString);
                    roadOutputStream.write(roadStringBuffer.toString().getBytes("utf-8"));
//						清空buffer和string
                    roadStringBuffer.setLength(0);
                    roadJSONString = ",";
                    break;
                }
//				如果是建筑物
                else if(k.equals("building")) {
                    buildingJSONString +="{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[";
                    buildingStringBuffer.append(buildingJSONString);
                    buildingOutputStream.write(buildingStringBuffer.toString().getBytes("utf-8"));
//						清空buffer和string
                    buildingStringBuffer.setLength(0);
                    buildingJSONString = "";
                    for (Iterator<Element> ndIt = way.elementIterator("nd"); ndIt.hasNext();) {
                        String nodeId = ndIt.next().attributeValue("ref");
                        Node node = allNode.get(nodeId);
                        if (node != null) {
                            String lat = String.valueOf(node.getLat());
                            String lon = String.valueOf(node.getLon());
                            buildingJSONString += "[" + lon + "," + lat + "],";
                        }
//						只要有点不存在，就不要这条道路
                        else {
                            buildingJSONString = "";
                            break;
                        }
                    }
                    buildingJSONString = buildingJSONString.substring(0, buildingJSONString.length()-1);
                    buildingJSONString += "]]}}";
                    buildingStringBuffer.append(buildingJSONString);
                    buildingOutputStream.write(buildingStringBuffer.toString().getBytes("utf-8"));
//						清空buffer和string
                    buildingStringBuffer.setLength(0);
                    buildingJSONString = ",";
                    break;
                }
            }
        }
        roadJSONString = roadJSONString.substring(0, roadJSONString.length() - 1);
        roadJSONString += "]}";

        roadStringBuffer.append(roadJSONString);
        roadOutputStream.write(roadStringBuffer.toString().getBytes("utf-8"));

        buildingJSONString = buildingJSONString.substring(0, buildingJSONString.length()-1);
        buildingJSONString += "]}";
        buildingStringBuffer.append(buildingJSONString);
        buildingOutputStream.write(roadStringBuffer.toString().getBytes("utf-8"));

        respJson.put("respCode", 1);
        respJson.put("msg", "success.");
        respJson.put("times", times);
        out.write(respJson.toString());
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
