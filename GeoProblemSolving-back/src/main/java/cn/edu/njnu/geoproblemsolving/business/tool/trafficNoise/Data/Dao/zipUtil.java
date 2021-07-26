package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class zipUtil {

    /*
        @function:压缩指定文件夹或文件
        @sourceFilePath:原始文件的路径，可以是文件夹或文件
        @zipFilePath:压缩后的文件的存放路径
     */
    public static void zipFile(String sourceFilePath, String zipFilePath) throws IOException {
        String appDir = new File(zipUtil.class.getClassLoader().getResource("").getPath() + "..\\lib\\winrar").getCanonicalPath();
        File zipFile = new File(zipFilePath);
        if(zipFile.exists()){
            zipFile.delete();
        }
//        判断文件是否存在放在拼字符串的时候
        String cmdZipString = "cmd /c " + appDir + "\\WinRAR.exe a -ep1 -m3" + " -r -afzip -ibck "
                + zipFile.getCanonicalPath() + " " + sourceFilePath;
        try {
            Runtime.getRuntime().exec(cmdZipString);
//            等待压缩完成
            long waitBegin = new Date().getTime();
            while (!zipFile.exists()){
//                等待时间超过5秒则判定文件压缩有问题
                if((new Date().getTime()-waitBegin)>5000)
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
       @function:解压指定压缩文件
       @zipFilePath:待解压文件路径
       @destDir:解压后文件的存放路径，必须是文件夹！
    */
    public static void unZipFile(String zipFilePath, String destDir) throws IOException {
        String appDir = new File(zipUtil.class.getClassLoader().getResource("").getPath() + "..\\lib\\winrar").getCanonicalPath();

		File zipFile = new File(zipFilePath);
        File dest = new File(destDir);

        if (!dest.exists()) {
            dest.mkdirs();
        }
        if(!dest.isDirectory()){
            System.out.println("the destination is not directory!");
            return;
        }

		if (zipFile.exists()) {
			String cmdUnZipString = "cmd /c " + appDir + "\\WinRAR.exe x -y -ibck \"" + zipFile.getCanonicalPath()
					+ "\" \"" + dest.getCanonicalPath() + "\"";
			try {
				Runtime.getRuntime().exec(cmdUnZipString);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
        else {
            System.out.println("source file dose not exist!");
        }
    }
}
