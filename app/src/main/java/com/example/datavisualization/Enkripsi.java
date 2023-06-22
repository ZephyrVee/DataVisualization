package com.example.datavisualization;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Enkripsi extends AppCompatActivity {
    private static SecretKeySpec secretKey;

    //AES
    public static String encrypt(String stringText){
        return encrypt(stringText, false);
    }

    public static String encrypt(String stringText, boolean isPass){
        try {
            if (isPass) {
                secretKey = generateKey(stringText);
            } else {
                secretKey = generateKey();
            }

            byte[] byteText = stringText.getBytes();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteCipherText = cipher.doFinal(byteText);
            return byteToStrNumbers(byteCipherText);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static String decrypt(String stringCipherText){
        try {
            SecretKeySpec secretKey = generateKey();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] byteCipherText = strNumbersToByte(stringCipherText);
            byte[] byteText = cipher.doFinal(byteCipherText);
            String stringText = new String(byteText);
            return stringText;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //AES



    private static SecretKeySpec generateKey(String key){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            messageDigest.update(byteKey, 0, byteKey.length);
            byte[] digestKey = messageDigest.digest();
            SecretKeySpec secretKey = new SecretKeySpec(digestKey, "AES");
            return secretKey;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static SecretKeySpec generateKey(){
        String key = "AplikasiVisualisasiDataKetenagakerjaanJakarta";
        return generateKey(key);
    }

    private static String byteToStrNumbers(byte[] cipherByteText){
        String str = null;
        for(int i = 0; i < cipherByteText.length; i++){
            if (i == 0){
                str = Integer.toString(cipherByteText[i]);
            }
            else {
                str += ",";
                str += Integer.toString(cipherByteText[i]);
            }
        }
        return str;
    }

    private static byte[] strNumbersToByte(String str){
        String[] arrayStr = str.split(",");
        byte[] bytes = new byte[arrayStr.length];
        for (int i = 0; i < bytes.length; i++){
            bytes[i] =(byte) Integer.parseInt(arrayStr[i]);
        }
        return bytes;
    }
}
