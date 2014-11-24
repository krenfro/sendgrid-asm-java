package com.github.krenfro.sendgrid.asm;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

/**
 * GroupManager are individual types of email you would like your users to be able to 
 * unsubscribe from, (e.g. Newsletters, Invoices, Alerts).
 * 
 * @see https://sendgrid.com/docs/API_Reference/Web_API_v3/Advanced_Suppression_Manager/groups.html
 */
public class GroupManager extends SendGrid{
    
    public GroupManager(String username, String password){
        super(username, password);
        baseUrl = baseUrl.concat("/groups");
    }

    public Group retrieve(int id) throws IOException{
        Request get = Request.Get(baseUrl.concat("/" + id))
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);       
        try{
            String json = get.execute().returnContent().asString();
            return jackson.readValue(json, Group.class);
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
    }
    
    public Group add(String name, String description) throws IOException{        
        Map<String,String> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        String payload = jackson.writeValueAsString(map);
        Request post = Request.Post(baseUrl)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = post
                    .bodyString(payload, ContentType.APPLICATION_JSON)
                    .execute().returnContent().asString();
            return jackson.readValue(json, Group.class); 
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
    }
    
    public Group update(Group group) throws IOException{        
        Objects.requireNonNull(group);
        if (group.getId() == null){
            throw new IllegalArgumentException(
                    "Group has no id. Perhaps invoke create() instead of update()");
        }        
        
        throw new UnsupportedOperationException(
                "Update is not yet supported.  Need Apache HttpClient > 4.4+");
        /*
        Map<String,String> map = new HashMap<>();
        map.put("name", group.getName());
        map.put("description", group.getDescription());
        String payload = jackson.writeValueAsString(map);
        Request patch = Request.Patch(baseUrl.concat("/" + group.getId()))
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = patch
                .bodyString(payload, ContentType.APPLICATION_JSON)
                .execute().returnContent().asString();
            return jackson.readValue(json, Group.class);         
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
        */
    }   
    
    public boolean remove(Group group) throws IOException{
        Objects.requireNonNull(group);
        Request delete = Request.Delete(baseUrl.concat("/" + group.getId()))
                .addHeader("Authorization", authHeader);    
        try{
            HttpResponse response = delete.execute().returnResponse();
            return response.getStatusLine().getStatusCode() == 204;
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }        
    }    
    
    public List<Group> retrieve() throws IOException{
        Request get = Request.Get(baseUrl)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authHeader);
        try{
            String json = get.execute().returnContent().asString();
            TypeReference<List<Group>> typeRef = new TypeReference<List<Group>>() {};
            return jackson.readValue(json, typeRef);       
        }
        catch(HttpResponseException ex){
            throw new IOException(ex);
        }
    }
    
     
}
