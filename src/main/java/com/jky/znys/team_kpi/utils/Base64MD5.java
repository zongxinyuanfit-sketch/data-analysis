package com.jky.znys.team_kpi.utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
public class Base64MD5 {
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();
    private static String keyStr = "a93#f16m";
    private static String vi = "4f*7a153";
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private final static String DES = "DES";

    /**
     * data[]进行编码
     *
     * @param data
     * @return
     */
    public static String Base64_encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);
        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;
            if (n++ >= 14) {
                n = 0;
//				buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");

        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;
            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }
        return buf.toString();
    }

    private static int Base64_decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code :" + c);
            }
    }

    /**
     * Decodes the given Base64 encoded String to a new byt e array. The byte
     * array holding the decoded data is returned.
     */
    public static byte[] Base64_decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Base64_decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            System.err
                    .println("Error while decoding BASE64 : " + ex.toString());
        }
        return decodedBytes;
    }

    private static void Base64_decode(String s, OutputStream os) throws IOException {
//		StringBuffer buffer = new StringBuffer();
//		String tmp = null;
//		while (true) {
//			int len = s.length();
//			if (len > 60) {
//				tmp = s.substring(0, 60).replaceAll(" ", "+");
//				buffer.append(tmp);
////				buffer.append(" ");
//				s = s.substring(61);
//			} else {
//				tmp = s.substring(0, len).replaceAll(" ", "+");
//				buffer.append(tmp);
//				break;
//			}
//		}
//		s = buffer.toString();
        int i = 0;
        int len = s.length();
        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;
            if (i == len)
                break;
            int tri = (Base64_decode(s.charAt(i)) << 18)
                    + (Base64_decode(s.charAt(i + 1)) << 12)
                    + (Base64_decode(s.charAt(i + 2)) << 6)
                    + (Base64_decode(s.charAt(i + 3)));
            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);
            i += 4;
        }
    }

    /**
     * 快速问医生、诊疗助手 加密方式
     */
    public static String MD5_getSignature(String appKey, String appSecret, Map<String, String> params) {

        Set<String> nameSet = params.keySet();
        int size = nameSet.size();
        String[] names = new String[size];

        Iterator<String> it = nameSet.iterator();
        int i = 0;
        while (it.hasNext()) {
            names[i++] = it.next();
        }
        Arrays.sort(names);

        StringBuilder sign = new StringBuilder();
        sign.append(appKey);
        for (int j = 0; j < size; j++) {
            String v = params.get(names[j]);

            sign.append(names[j]);
            //sign.append("=");
            sign.append(v);
            //sign.append("&");
        }

        sign.append(appSecret);
        return MD5_MD5(sign.toString()).toUpperCase();


        //return getMD5(str_final).toUpperCase();
    }

    private static String MD5_MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return MD5_toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String MD5_toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * DES算法，加密 String, 使用时调用这个方法
     *
     * @param data 待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    public static String DesUtil_encode(String data) throws Exception {
        return DesUtil_encode(data.getBytes());
    }

    /**
     * DES算法，加密 byte
     *
     * @param data 待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws Exception 异常
     */
    private static String DesUtil_encode(byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(keyStr.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(vi.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data);
            return Base64MD5.Base64_encode(bytes);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * DES算法，解密byte
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    private static byte[] DesUtil_decode(String key, byte[] data) throws Exception {
        try {
            // SecureRandom sr = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(vi.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 解密 String
     */
    public static String DesUtil_decodeValue(String data) {
        byte[] datas;
        String value = null;
        try {
            // if (System.getProperty("os.name") != null
            // && (System.getProperty("os.name").equalsIgnoreCase("sunos") ||
            // System
            // .getProperty("os.name").equalsIgnoreCase("linux"))) {
            // datas = decode(keyStr, Base64.decode(data));
            // } else {
            datas = DesUtil_decode(keyStr, Base64MD5.Base64_decode(data));
            // }
            value = new String(datas);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

    /**
     * 设置key，vi值
     */
    public static void DesUtil_setDesKeyAndVi(String keyStr, String vi) {
        Base64MD5.keyStr = keyStr;
        Base64MD5.vi = vi;
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String ToastDES_encrypt(String data, String key) throws Exception {
        byte[] bt = ToastDES_encrypt(data.getBytes(), key.getBytes());
        String strs = Base64MD5.Base64_encode(bt);
        return strs;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String ToastDES_decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        byte[] buf = Base64MD5.Base64_decode(data);
        byte[] bt = ToastDES_decrypt(buf, key.getBytes());
        return new String(bt);
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] ToastDES_encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }


    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] ToastDES_decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }
}
