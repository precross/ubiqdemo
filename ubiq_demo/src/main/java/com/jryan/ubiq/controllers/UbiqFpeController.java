package com.jryan.ubiq.controllers;

import com.jryan.ubiq.FPECredentails;
import com.jryan.ubiq.entities.FpeRequest;
import com.ubiqsecurity.UbiqCredentials;
import com.ubiqsecurity.UbiqFPEEncryptDecrypt;
import com.ubiqsecurity.UbiqFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v2/ubiq")
public class UbiqFpeController {
    private static Logger LOGGER = LoggerFactory.getLogger(UbiqFpeController.class);

    private static HashMap<String, String> map = new HashMap<String, String>();


    @PostMapping("/encrypteFPE")
    public String encrypteFPE(@RequestBody FpeRequest request) {
        String result;
        String plainText = request.getPlainText();
        UbiqCredentials ubiqCredentials = UbiqFactory.createCredentials(FPECredentails.ACCESS_KEY_ID, FPECredentails.SECRET_SIGNING_KEY, FPECredentails.SECRET_CRYPTO_ACCESS_KEY, FPECredentails.SERVER);

        try (UbiqFPEEncryptDecrypt ubiqEncryptDecrypt = new UbiqFPEEncryptDecrypt(ubiqCredentials)) {
            String cipher = ubiqEncryptDecrypt.encryptFPE("SSN", plainText, null);

            LOGGER.info("encrypted output: {}", cipher);
            map.put("cipher", cipher);

            result = cipher;
        } catch (Exception ex) {
            LOGGER.error("Exception: ", ex);
            result = "Failed";
        }

        return result;
    }


    @GetMapping("/decrypteFpe")
    public String decrypteFpe() {
        String result;
        String cipher = map.get("cipher");

        if (cipher != null) {
            UbiqCredentials ubiqCredentials = UbiqFactory.createCredentials(FPECredentails.ACCESS_KEY_ID, FPECredentails.SECRET_SIGNING_KEY, FPECredentails.SECRET_CRYPTO_ACCESS_KEY, FPECredentails.SERVER);
            try (UbiqFPEEncryptDecrypt ubiqEncryptDecrypt = new UbiqFPEEncryptDecrypt(ubiqCredentials)) {
                String plaintext = ubiqEncryptDecrypt.decryptFPE("SSN", cipher, null);
                result = plaintext;
            }
        } else {
            result = "Please encrypt some data first";
        }

        return result;
    }
}
