package com.jryan.ubiq.entities;

public class FpeRequest {
    private String credentialsFilePath;
    private String plainText;

    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public void setCredentialsFilePath(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    @Override
    public String toString() {
        return "FpeRequest{" +
                "credentialsFilePath='" + credentialsFilePath + '\'' +
                ", plainText='" + plainText + '\'' +
                '}';
    }
}
