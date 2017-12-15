package com.hrmp.util;

import android.text.TextUtils;

/**
 * Created by Ly on 2017/4/30.
 * 负责加密的工具类
 */

public class SecurityUtils {
    /**
     * 异或加密
     *
     * @param str
     * @param key
     * @return
     */
    public static String encryptOrDencrypt(String str, int key) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        if (key == 0) {
            key = 2016;
        }
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) (array[i] ^ key);//对每个数组元素进行异或运算
        }
        str = String.valueOf(array);
        return str;
    }

    public static String encryptAndUncrypt(String value, char secret) {
        // 对value加密，secret密文字符
        byte[] bt = value.getBytes();
        // 将需要加密的内容转换为字节数组
        for (int i = 0; i < bt.length; i++) {
            // 通过异或运算进行加密
            bt[i] = (byte) (bt[i] ^ (int)secret);
        }
        return new String(bt, 0, bt.length);
    }
}
