package cn.edu.njnu.geoproblemsolving.business.resource.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResCommonUtil {
    // public static String calcMD5(InputStream stream) {
    //     try {
    //         MessageDigest digest = MessageDigest.getInstance("MD5");
    //         try {
    //             byte[] buffer = new byte[1024];
    //             int len;
    //             while ((len = stream.read(buffer)) > 0) {
    //                 digest.update(buffer, 0, len);
    //             }
    //             return ";";
    //         }catch (IOException e){
    //             return "";
    //         }
    //
    //     } catch (NoSuchAlgorithmException e) {
    //         e.printStackTrace();
    //         return "";
    //     }
    // }
    // public static String toHexStr(byte[] data){
    //     StringBuilder builder = new StringBuilder(data.length * 2);
    //     for (byte b: data){
    //         builder.append(toHexStr())
    //     }
    // }
}
