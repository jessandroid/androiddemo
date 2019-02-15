package com.qihoo.kidswatch.utils;

public class Utils {
    public static String byteArrayToHexString(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : array) {
            int intVal = b & 0xff;
            if (intVal < 0x10) {
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(intVal));
        }
        return hexString.toString();
    }

    public static void testByte(){
        byte[] test = new byte[456];
        for (int i =0;i<test.length;i++){
            test[i] = -60;
        }
    }
}
