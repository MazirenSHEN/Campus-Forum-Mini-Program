package com.nuoquan.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * base64编码（用于加密），普通md5加密
 *
 * @author Boyuan YE
 * @date 2022.08.15
 */
public class EncryptUtils {

    /**
     * @Description: 对字符串进行加密
     */
    public static String base64Encode(String plainText) {
        if (StringUtils.isEmpty(plainText)){
            return plainText;
        }
        return Base64.getEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
    }
    /**
     * @Description: 对字符串进行解码
     */
    public static String base64Decode(String encryptedText) {
        if (StringUtils.isEmpty(encryptedText)){
            return encryptedText;
        }
        String decryptedText = new String(Base64.getDecoder().decode(encryptedText));
        return decryptedText;
    }

    /**
     * @Description: 对字符串进行加密
     */
    public static String md5Encode(String plainText) {
        if (StringUtils.isBlank(plainText)) {
            return null;
        }
        String output = "";
        try {
            //1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            //2 将消息变成byte数组
            byte[] input = plainText.getBytes();

            //3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);

            //4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            output = bytesToHex(buff);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * 二进制转十六进制
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        //把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if(digital < 0) {
                digital += 256;
            }
            if(digital < 16){
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }

    public static void main(String[] args) {
        String eMsg2 = base64Encode("1193874@wku.edu.cn");
        System.out.println(eMsg2);
//        String eMsg3 = md5Encode("oDwsO5FDozoraPzxqwIo9kx0RBxY");
//        System.out.println(eMsg3);
    }


}
