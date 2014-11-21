package com.github.krenfro.sendgrid.asm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

/**
 * Suppressions are email addresses that can be added to Groups to prevent 
 * certain types of emails from being delivered to those addresses.
 * @see https://sendgrid.com/docs/API_Reference/Web_API_v3/Advanced_Suppression_Manager/suppressions.html
 */
public class Suppressions extends SendGrid{
    public Suppressions(String username, String password) throws IOException{
        super(username, password);
    }

    public List<String> add(Group group, String ... email) throws IOException{        
        List<String> suppressions = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("recipient_emails", email);
        String payload = jackson.writeValueAsString(map);
        Request post = Request.Post(baseUrl + "/groups/" + group.getId() + "/suppressions")
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
        
    public void remove(Group group, String ... email) throws IOException{        
        
        for (String individual: email){            
            Request delete = Request.Delete(baseUrl + "/groups/" + group.getId() + "/suppressions/" + individual)
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
        
    public List<String> retrieve(Group group) throws IOException{        
        List<String> suppressions = new ArrayList<>();
        Request get = Request.Get(baseUrl + "/groups/" + group.getId() + "/suppressions")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = get.execute().returnContent().asString();            
            JsonNode array = jackson.readTree(json);
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
     
    public List<Suppression> retrieve(String email) throws IOException{
        List<Suppression> suppressed = new ArrayList<>();
        Request get = Request.Get(baseUrl + "/suppressions/" + email)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = get.execute().returnContent().asString();
            TypeReference<Map<String,List<Suppression>>> typeRef = new TypeReference<Map<String,List<Suppression>>>() {};
            Map<String,List<Suppression>> map = jackson.readValue(json, typeRef);
            for (Suppression s: map.get("suppressions")){
                if (s.isSuppressed()){
                    suppressed.add(s);
                }
            }
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
        return suppressed;
    }
}
