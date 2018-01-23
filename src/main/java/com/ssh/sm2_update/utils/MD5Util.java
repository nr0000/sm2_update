package com.ssh.sm2_update.utils;

import com.sm2.bcl.common.util.ColUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Description：
 * Created by fyx on 4/19/2017.
 * Version：
 */
public class MD5Util {

    static final char[] VALID_CHAR = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '2', '3', '4', '5', '6', '8', '9'};
    private static Charset utf8 = Charset.forName("utf-8");
    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();
    private static Logger logger = Logger.getLogger(com.sm2.bcl.common.util.StringUtils.class);
    private static char[] charsOfPswd = new String("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ`1234567890-=~!@#$%^&*()_+[];',./{}:\"<>?").toCharArray();

    /**
     * 获取一个字符串的md5
     *
     * @param originalString 源字符串
     * @return md5字符串
     */
    public static String get32Md5(String originalString) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(originalString.getBytes());
            return byteArrayToHex(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("", e);
            return null;
        }
    }

    public static String get16Md5(String originalString){
        return get32Md5(originalString).substring(8,24);
    }


    static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

}
