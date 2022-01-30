package com.jryan.ubiq.entities;

public class EncryptionRequest {
    private String input;

    public EncryptionRequest(){

    }

    public EncryptionRequest(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "EncryptionRequest{" +
                "input='" + input + '\'' +
                '}';
    }
}
