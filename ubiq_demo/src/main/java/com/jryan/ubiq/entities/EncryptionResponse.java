package com.jryan.ubiq.entities;

public class EncryptionResponse {
    private String respCode;
    private String respDescription;
    private String output;

    public EncryptionResponse(){

    }

    public EncryptionResponse(String respCode, String respDescription, String encryptedOutput) {
        this.respCode = respCode;
        this.respDescription = respDescription;
        this.output = encryptedOutput;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDescription() {
        return respDescription;
    }

    public void setRespDescription(String respDescription) {
        this.respDescription = respDescription;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "EncryptionResponse{" +
                "respCode='" + respCode + '\'' +
                ", respDescription='" + respDescription + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}
