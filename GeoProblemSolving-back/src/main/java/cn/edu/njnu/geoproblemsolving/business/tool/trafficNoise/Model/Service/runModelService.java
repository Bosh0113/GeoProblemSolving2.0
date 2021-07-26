package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service;

//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.EDataType;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.OutputData;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.UserData;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.*;

import java.io.*;
import java.util.Date;
import java.util.List;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.zipUtil.unZipFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.zipUtil.zipFile;

public class runModelService {
    final static String ROAD_FILE_NAME = "RoadCenterLine";
    final static String BUILDING_FILE_NAME = "Building";
    final static String BARRIER_FILE_NAME = "Barrier";

    /*
   @function:生成模型运行所需要的shpfile文件压缩包，
             压缩包包括*.shp,*.prj,*.dbf,*.shx文件和shpfile压缩包文件的UDX配置文件
   @destFilePath:生成文件的存放路径
   @srcFilePath:shpfile文件所在路径
   @shpfileName:shpfile文件的名称
 */
    public static void genShpZipFile(String destFilePath, String srcFilePath, String shpfileName) throws IOException {
        //生成shpfile压缩包文件的UDX配置文件 *.udxcfg
        String tempFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "..\\..\\data\\temp\\configure.udxcfg";
        String allText = "";

        File templateFile = new File(tempFilePath);
        if (!templateFile.exists()) {
            System.out.println("temp file dest not exist!");
            return;
        }
        FileReader fileReader = new FileReader(templateFile);
        BufferedReader reader = new BufferedReader(fileReader);
        String tempLine;
        while ((tempLine = reader.readLine()) != null) {
            allText += tempLine + "\n";
        }
        reader.close();

        allText = allText.replace("$(fileName)", shpfileName);

        File tempFolder = new File(srcFilePath + "\\" + shpfileName);
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        File cfgFile = new File(tempFolder.getCanonicalPath() + "\\configure.udxcfg");
        if (cfgFile.exists()) {
            cfgFile.delete();
        }
        cfgFile.createNewFile();

        FileOutputStream outputStream = new FileOutputStream(cfgFile);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(allText);
        outputStream.write(stringBuffer.toString().getBytes("utf-8"));
        outputStream.close();

        String sourceFilePath = "";
        String[] extensions = {".shp", ".dbf", ".prj", ".shx"};
        for (String extension : extensions) {
            File tempFile = new File(srcFilePath + "\\" + shpfileName + extension);
            if (!tempFile.exists()) {
                continue;
            }
            sourceFilePath += tempFile.getCanonicalPath() + " ";
        }
        sourceFilePath += cfgFile.getCanonicalPath();
        new File(destFilePath).mkdirs();
        zipFile(sourceFilePath, destFilePath + "\\" + shpfileName + ".zip");

//        压缩完之后把配置文件删除
//        cfgFile.deleteOnExit();
//        if(cfgFile.exists()){
//            cfgFile.delete();
//        }
    }

    /*
           @function:生成height、boundBox、samplingSize的UDX文件
           @filePath:生成文件的存放路径
           @value:值
     */
    public static void genHeightUdxData(String filePath, String value) throws IOException {
        String tempFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "..\\..\\data\\temp\\udx_xml_Height.xml";
        String allText = "";

        File tempFile = new File(tempFilePath);
        if (!tempFile.exists()) {
            System.out.println("temp file dest not exist!");
            return;
        }
        FileReader fileReader = new FileReader(tempFile);
        BufferedReader reader = new BufferedReader(fileReader);
        String tempLine;
        while ((tempLine = reader.readLine()) != null) {
            allText += tempLine + "\n";
        }
        reader.close();

        allText = allText.replace("$(heightValue)", value);

        File file = new File(filePath);

//        如果传入的不是文件夹路径
        if (!file.isDirectory()) {
//        如果文件不存在，则创建文件和文件
            if (!file.exists()) {
                File parent = new File(file.getParent());
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
        }
//        如果传入的是文件夹路径，则在此文件夹下新建udx_xml_Height.xml文件
        else {
//            先创建文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
//            再创建文件
            file = new File(filePath + "\\udx_xml_Height.xml");
            if (!file.exists()) {
                file.createNewFile();
            }
        }

        FileOutputStream outputStream = new FileOutputStream(file);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(allText);
        outputStream.write(stringBuffer.toString().getBytes("utf-8"));
        outputStream.close();
    }

    public static void genBoundBoxUdxData(String filePath, String value) throws IOException {
        String tempFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "..\\..\\data\\temp\\udx_xml_RegionBBox.xml";
        String allText = "";

        File tempFile = new File(tempFilePath);
        if (!tempFile.exists()) {
            System.out.println("temp file dest not exist!");
            return;
        }
        FileReader fileReader = new FileReader(tempFile);
        BufferedReader reader = new BufferedReader(fileReader);
        String tempLine;
        while ((tempLine = reader.readLine()) != null) {
            allText += tempLine + "\n";
        }
        reader.close();

        allText = allText.replace("$(boundBoxValue)", value);

        File file = new File(filePath);

//        如果传入的不是文件夹路径
        if (!file.isDirectory()) {
//        如果文件不存在，则创建文件和文件
            if (!file.exists()) {
                File parent = new File(file.getParent());
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
        }
//        如果传入的是文件夹路径，则在此文件夹下新建udx_xml_RegionBBox.xml文件
        else {
//            先创建文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
//            再创建文件
            file = new File(filePath + "\\udx_xml_RegionBBox.xml");
            if (!file.exists()) {
                file.createNewFile();
            }
        }

        FileOutputStream outputStream = new FileOutputStream(file);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(allText);
        outputStream.write(stringBuffer.toString().getBytes("utf-8"));
        outputStream.close();
    }

    public static void genSamplingSizeUdxData(String filePath, String value) throws IOException {
        String tempFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "..\\..\\data\\temp\\udx_xml_SamplingSize.xml";
        String allText = "";

        File tempFile = new File(tempFilePath);
        if (!tempFile.exists()) {
            System.out.println("temp file dest not exist!");
            return;
        }
        FileReader fileReader = new FileReader(tempFile);
        BufferedReader reader = new BufferedReader(fileReader);
        String tempLine;
        while ((tempLine = reader.readLine()) != null) {
            allText += tempLine + "\n";
        }
        reader.close();

        allText = allText.replace("$(samplingSizeValue)", value);

        File file = new File(filePath);

//        如果传入的不是文件夹路径
        if (!file.isDirectory()) {
//        如果文件不存在，则创建文件和文件
            if (!file.exists()) {
                File parent = new File(file.getParent());
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
        }
//        如果传入的是文件夹路径，则在此文件夹下新建udx_xml_SamplingSize.xml文件
        else {
//            先创建文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
//            再创建文件
            file = new File(filePath + "\\udx_xml_SamplingSize.xml");
            if (!file.exists()) {
                file.createNewFile();
            }
        }

        FileOutputStream outputStream = new FileOutputStream(file);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(allText);
        outputStream.write(stringBuffer.toString().getBytes("utf-8"));
        outputStream.close();
    }
}
