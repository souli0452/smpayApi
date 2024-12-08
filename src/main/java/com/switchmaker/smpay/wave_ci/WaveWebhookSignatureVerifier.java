package com.switchmaker.smpay.wave_ci;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class WaveWebhookSignatureVerifier {
    private static final byte[] HEX_ARRAY = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);

    public static boolean verifySignature(String waveSignature, String body, String webhookSecret) throws GeneralSecurityException {
        final String[] waveSignatureParts = waveSignature.split(",");
        String timestamp = "";
        List<String> signatures = new ArrayList<>();

        for (String elem : waveSignatureParts) {
            String[] keyval = elem.split("=");
            String key = keyval[0];
            String val = keyval[1];
            // Uncomment this if you want to see which values were extracted:
            // System.out.printf("key: %s, val: %s %n", key, val);
            if (key.equals("t")) {
                timestamp = val;
            } else {
                signatures.add(val);
            }
        }

        String data = timestamp + body;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));


        String calculatedSignature = bytesToHex(hash);
        // Uncomment this if you want to see what was calculated:
        // System.out.printf("Signature: %s %n", calculatedSignature);

        return signatures.contains(calculatedSignature);
    }


    private static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
