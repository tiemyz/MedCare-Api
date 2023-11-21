package br.com.fiap.MedCare.util;

import java.util.Base64;

public class ChaveUtil {

    public static byte[] decodificarChaveBase64(String base64EncodedSecret) {
        return Base64.getDecoder().decode(base64EncodedSecret);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String base64EncodedSecret = "0BISKOURxxzM8cvqhbfwaj9/zCvRS8VTMKosgMs3BsU=";

        byte[] decodedSecret = decodificarChaveBase64(base64EncodedSecret);
        String hexSecret = bytesToHex(decodedSecret);

        System.out.println("Chave Decodificada (Hex): " + hexSecret);
    }
}