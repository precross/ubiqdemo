package com.jryan.ubiq.controllers;


import com.jryan.ubiq.FPECredentails;
import com.jryan.ubiq.entities.EncryptFileRequest;
import com.jryan.ubiq.entities.EncryptionRequest;
import com.jryan.ubiq.entities.EncryptionResponse;
import com.ubiqsecurity.UbiqCredentials;
import com.ubiqsecurity.UbiqDecrypt;
import com.ubiqsecurity.UbiqEncrypt;
import com.ubiqsecurity.UbiqFactory;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping("/api/v1/ubiq")
public class UbiqController {
    private static Logger LOGGER = LoggerFactory.getLogger(UbiqController.class);

    private static HashMap<String, byte[]> map = new HashMap<String, byte[]>();


    @PostMapping("/encrypt")
    public EncryptionResponse encrypt(@RequestBody EncryptionRequest request) {
        EncryptionResponse response = new EncryptionResponse();
        UbiqCredentials credentials = UbiqFactory.createCredentials(FPECredentails.ACCESS_KEY_ID, FPECredentails.SECRET_SIGNING_KEY, FPECredentails.SECRET_CRYPTO_ACCESS_KEY, FPECredentails.SERVER);

        try {
            if (request != null) {
                byte[] plainBytes = request.getInput().getBytes();
                LOGGER.info("plainBytes: {}", plainBytes);
                byte[] encryptedBytes = UbiqEncrypt.encrypt(credentials, plainBytes);
                LOGGER.info("encryptedBytes: {}", encryptedBytes);

                if (encryptedBytes != null) {
                    String encryptedString = new String(encryptedBytes);

                    response.setRespCode("00");
                    response.setRespDescription("Success");
                    response.setOutput(encryptedString);

                    map.clear();
                    map.put("encryptedBytes", encryptedBytes);
                } else {
                    response.setRespCode("-1");
                    response.setRespDescription("Failed");
                    response.setOutput(null);
                }
            } else {
                response.setRespCode("-2");
                response.setRespDescription("Invalid Request");
                response.setOutput(null);
            }
        } catch (InvalidCipherTextException ex) {
            response.setRespCode("-1");
            response.setRespDescription(ex.getLocalizedMessage());
            response.setOutput(null);
        }

        return response;
    }

    @GetMapping("/decrypt")
    public EncryptionResponse decrypt() {
        EncryptionResponse response = new EncryptionResponse();
        UbiqCredentials credentials = UbiqFactory.createCredentials(FPECredentails.ACCESS_KEY_ID, FPECredentails.SECRET_SIGNING_KEY, FPECredentails.SECRET_CRYPTO_ACCESS_KEY, FPECredentails.SERVER);

        try {
            if (map.size() != 0) {
                byte[] encryptedBytes = map.get("encryptedBytes");
                byte[] decryptedBytes = UbiqDecrypt.decrypt(credentials, encryptedBytes);

                if (decryptedBytes != null) {
                    String decryptedOutput = new String(decryptedBytes);

                    response.setRespCode("00");
                    response.setRespDescription("Success");
                    response.setOutput(decryptedOutput);
                } else {
                    response.setRespCode("-1");
                    response.setRespDescription("Failed");
                    response.setOutput(null);
                }
            } else {
                response.setRespCode("-4");
                response.setRespDescription("Please encrypt some data first");
                response.setOutput(null);
            }
        } catch (IllegalStateException ex) {
            response.setRespCode("-3");
            response.setRespDescription(ex.getLocalizedMessage());
            response.setOutput(null);
        } catch (InvalidCipherTextException ex) {
            response.setRespCode("-1");
            response.setRespDescription(ex.getLocalizedMessage());
            response.setOutput(null);
        } catch (Exception ex) {
            LOGGER.error("Exception: ", ex);

            response.setRespCode("-1");
            response.setRespDescription(ex.getLocalizedMessage());
            response.setOutput(null);
        }

        return response;
    }

    @PostMapping("/encryptFile")
    public String encryptFile(@RequestBody EncryptFileRequest request) throws Exception {
        UbiqCredentials credentials = UbiqFactory.createCredentials(FPECredentails.ACCESS_KEY_ID, FPECredentails.SECRET_SIGNING_KEY, FPECredentails.SECRET_CRYPTO_ACCESS_KEY, null);
        String inputFile = request.getInputFilePath();
        String encryptedFile = request.getOutputFilePath();
        piecewiseEncryption(inputFile, encryptedFile, credentials);

        return "Encrypted Successfully";
    }

    @PostMapping("/decryptFile")
    public String decryptFile(@RequestBody EncryptFileRequest request) throws Exception {
        UbiqCredentials credentials = UbiqFactory.createCredentials(FPECredentails.ACCESS_KEY_ID, FPECredentails.SECRET_SIGNING_KEY, FPECredentails.SECRET_CRYPTO_ACCESS_KEY, null);
        String encryptedFile = request.getInputFilePath();
        String decryptedFile = request.getOutputFilePath();
        piecewiseDecryption(encryptedFile, decryptedFile, credentials);

        return "Decrypted Successfully";
    }

    static void piecewiseEncryption(String inFile, String outFile, UbiqCredentials ubiqCredentials)
            throws IOException, IllegalStateException, InvalidCipherTextException {
        try (FileInputStream plainStream = new FileInputStream(inFile)) {
            try (FileOutputStream cipherStream = new FileOutputStream(outFile)) {
                try (UbiqEncrypt ubiqEncrypt = new UbiqEncrypt(ubiqCredentials, 1)) {
                    // start the encryption
                    byte[] cipherBytes = ubiqEncrypt.begin();
                    cipherStream.write(cipherBytes);

                    // process 128KB at a time
                    var plainBytes = new byte[0x20000];

                    // loop until the end of the input file is reached
                    int bytesRead = 0;
                    while ((bytesRead = plainStream.read(plainBytes, 0, plainBytes.length)) > 0) {
                        cipherBytes = ubiqEncrypt.update(plainBytes, 0, bytesRead);
                        cipherStream.write(cipherBytes);
                    }

                    // finish the encryption
                    cipherBytes = ubiqEncrypt.end();
                    cipherStream.write(cipherBytes);
                }
            }
        }
    }

    static void piecewiseDecryption(String inFile, String outFile, UbiqCredentials ubiqCredentials)
            throws FileNotFoundException, IOException, IllegalStateException, InvalidCipherTextException {
        try (FileInputStream cipherStream = new FileInputStream(inFile)) {
            try (FileOutputStream plainStream = new FileOutputStream(outFile)) {
                try (UbiqDecrypt ubiqDecrypt = new UbiqDecrypt(ubiqCredentials)) {
                    // start the decryption
                    byte[] plainBytes = ubiqDecrypt.begin();
                    plainStream.write(plainBytes);

                    // process 128KB at a time
                    var cipherBytes = new byte[0x20000];

                    // loop until the end of the input file is reached
                    int bytesRead = 0;
                    while ((bytesRead = cipherStream.read(cipherBytes, 0, cipherBytes.length)) > 0) {
                        plainBytes = ubiqDecrypt.update(cipherBytes, 0, bytesRead);
                        plainStream.write(plainBytes);
                    }

                    // finish the decryption
                    plainBytes = ubiqDecrypt.end();
                    plainStream.write(plainBytes);
                }
            }
        }
    }
}
