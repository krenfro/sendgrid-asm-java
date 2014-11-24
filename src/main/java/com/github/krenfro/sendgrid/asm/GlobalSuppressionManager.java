package com.github.krenfro.sendgrid.asm;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

/**
 * Global Suppressions are email addresses that will not receive any emails.
 * 
 * @see https://sendgrid.com/docs/API_Reference/Web_API_v3/Advanced_Suppression_Manager/global_suppressions.html
 */
public class GlobalSuppressionManager extends SendGrid{
    
    public GlobalSuppressionManager(String username, String password){
        super(username, password);
    }
        
    public List<String> add(String ... email) throws IOException{        
        List<String> suppressions = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("recipient_emails", email);
        String payload = jackson.writeValueAsString(map);
        Request post = Request.Post(baseUrl + "/suppressions/global")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = post
                    .bodyString(payload, ContentType.APPLICATION_JSON)
                    .execute().returnContent().asString();            
            JsonNode array = jackson.readTree(json).path("recipient_emails");
            if (array.isArray()){
                for (final JsonNode entry : array) {
                    suppressions.add(entry.asText());
                }
            }
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
        return suppressions;
    }
    
    public boolean has(String email) throws IOException{                
        Request get = Request.Get(baseUrl + "/suppressions/global/" + email)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = get.execute().returnContent().asString();
            return json.contains(email);
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
    }
                
    public void remove(String ... email) throws IOException{         
        
        for (String entry: email){
            Request delete = Request.Delete(baseUrl + "/suppressions/global/" + entry)
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", authHeader);    
            try{
                delete.execute();
            }
            catch(HttpResponseException ex){
                throw new IOException(ex);
            }  
        }
    }    
}
