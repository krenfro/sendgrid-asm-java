package com.github.krenfro.sendgrid.asm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

/**
 * SuppressionManager are email addresses that can be added to Groups to prevent
 * certain types of emails from being delivered to those addresses.
 *
 * https://sendgrid.com/docs/API_Reference/Web_API_v3/Advanced_Suppression_Manager/suppressions.html
 */
public class SuppressionManager extends SendGrid{

    public SuppressionManager(String username, String password){
        super(username, password);
    }

    public SuppressionManager(String apiKey){
        super(apiKey);
    }

    public List<String> add(Group group, String ... email) throws IOException{
        return add(group.getId(), email);
    }

    public List<String> add(int groupId, String ... email) throws IOException{
        List<String> suppressions = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("recipient_emails", email);
        String payload = jackson.writeValueAsString(map);
        Request post = Request.Post(baseUrl + "/groups/" + groupId + "/suppressions")
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
        remove(group.getId(), email);
    }

    public void remove(int groupId, String ... email) throws IOException{
        for (String individual: email){
            Request delete = Request.Delete(baseUrl + "/groups/" + groupId + "/suppressions/" + individual)
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
        return retrieve(group.getId());
    }

    public List<String> retrieve(int groupId) throws IOException{
        List<String> suppressions = new ArrayList<>();
        Request get = Request.Get(baseUrl + "/groups/" + groupId + "/suppressions")
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
        List<Suppression> suppressions = new ArrayList<>();
        Request get = Request.Get(baseUrl + "/suppressions/" + email)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = get.execute().returnContent().asString();
            TypeReference<Map<String,List<Suppression>>> typeRef = new TypeReference<Map<String,List<Suppression>>>() {};
            Map<String,List<Suppression>> map = jackson.readValue(json, typeRef);
            suppressions.addAll(map.get("suppressions"));
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
        return suppressions;
    }

    public void save(String email, List<Suppression> suppressions) throws IOException{
        Set<Integer> previouslySuppressed = new HashSet<>();
        for (Suppression s: retrieve(email)){
            if (s.isSuppressed()){
                previouslySuppressed.add(s.getId());
            }
        }
        for (Suppression s: suppressions){
            if (s.isSuppressed() && !previouslySuppressed.contains(s.getId())){
                add(s.getId(), email);
            }
            else if(!s.isSuppressed() && previouslySuppressed.contains(s.getId())){
                remove(s.getId(), email);
            }
        }
    }
}
