package com.github.dieuph.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class CommonUtils.
 * 
 * @author dieuph
 */
public class CommonUtils {

    /**
     * MD5 hash generator.
     *
     * @param text the text
     * @return the string
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public static String md5(String text) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] disgest = messageDigest.digest(text.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < disgest.length; i++) {
            sb.append(Integer.toHexString((disgest[i] & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
}
