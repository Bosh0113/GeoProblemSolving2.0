package cn.edu.njnu.geoproblemsolving.Dao.Method;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class FileCopyThread implements Runnable {
    private File srcFile;
    private File destFile;

    public void setSrcFile(File srcFile){
        this.srcFile=srcFile;
    }

    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }

    @Override
    public void run(){
        try {
            FileUtils.copyFile(this.srcFile,this.destFile);
        }catch (Exception ignored){
            System.out.println("Copy file fail.");
        }
    }
}
