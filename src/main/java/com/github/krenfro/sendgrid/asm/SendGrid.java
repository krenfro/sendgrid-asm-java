package com.github.krenfro.sendgrid.asm;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

public abstract class SendGrid {

    public static final String DEFAULT_URL = "https://api.sendgrid.com/v3/asm";
    protected String baseUrl = DEFAULT_URL;
    protected final String authHeader;
    protected ObjectMapper jackson;

    public SendGrid(String username, String password){
        authHeader = "Basic " + Base64.encodeBase64String(
                String.format("%s:%s", username, password).getBytes());
        setupJackson();
    }

    public SendGrid(String apiKey) {
        authHeader = "Bearer " + Base64.encodeBase64String(apiKey.getBytes());
        setupJackson();
    }

    private void setupJackson() {
        jackson = new ObjectMapper();
        jackson.setPropertyNamingStrategy(
                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        jackson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void setBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl(){
        return baseUrl;
    }
}
