package com.esprit.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {
    private static final String SECRET_KEY = "1234567890123456"; // Clé secrète de 16 caractères
    private static final String IV = "1234567890123456"; // IV (doit être identique à PHP)

    public static String encrypt(String strToEncrypt) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 🔹 Ajout de CBC
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.out.println("Erreur de chiffrement : " + e.getMessage());
            return null;
        }
    }

    public static String decrypt(String strToDecrypt) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // 🔹 Ajout de CBC
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.out.println("Erreur de déchiffrement : " + e.getMessage());
            return null;
        }
    }
}
