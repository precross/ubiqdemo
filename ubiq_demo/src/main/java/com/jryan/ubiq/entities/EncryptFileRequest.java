package com.jryan.ubiq.entities;

public class EncryptFileRequest {
    private String inputFilePath;
    private String outputFilePath;

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public String toString() {
        return "EncryptFileRequest{" +
                "inputFilePath='" + inputFilePath + '\'' +
                ", outputFilePath='" + outputFilePath + '\'' +
                '}';
    }
}
