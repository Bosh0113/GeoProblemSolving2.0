package cn.edu.njnu.geoproblemsolving.business.tool;

import cn.hutool.core.lang.UUID;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author ：Zhiyi
 * @Date ：2020/12/11 14:45
 * @modified By：
 * @version: 1.0.0
 */
public class FileUtil {
    //静态方法：三个参数：文件的二进制，文件路径，文件名
    //通过该方法将在指定目录下添加指定文件
    public static String uploadFile(MultipartFile upload, String filePath) throws Exception {
        File file = new File(filePath);

        if (!file.exists()) {
            file.mkdirs();
        }

        String originalFileName = upload.getOriginalFilename();//获取原始图片的扩展名
        String newFileName = UUID.randomUUID() + originalFileName;
        String newFilePath = filePath + newFileName; //新文件的路径

        upload.transferTo(new File(newFilePath));  //将传来的文件写入新建的文件
        return newFileName;


    }

    /**
     * 删除单个文件
     * @param   fileName    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        Boolean flag = false;
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

}

