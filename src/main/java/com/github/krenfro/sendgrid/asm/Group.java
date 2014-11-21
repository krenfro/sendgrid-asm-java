package com.github.krenfro.sendgrid.asm;

import java.util.Objects;

public class Group {
    
    private Integer id;
    private String name;
    private String description;
    private String lastEmailSentAt;
    private Integer unsubscribes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastEmailSentAt() {
        return lastEmailSentAt;
    }

    public void setLastEmailSentAt(String lastEmailSentAt) {
        this.lastEmailSentAt = lastEmailSentAt;
    }    
    
    public Integer getUnsubscribes() {
        return unsubscribes;
    }

    public void setUnsubscribes(Integer unsubscribes) {
        this.unsubscribes = unsubscribes;
    }

    @Override
    public String toString() {
        return "SuppressionGroup{" + "id=" + id + ", name=" + name + ", description=" + description + '}';
    }

    @Override
    public int hashCode(){
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final Group other = (Group) obj;
        if (!Objects.equals(this.id, other.id)){
            return false;
        }
        if (!Objects.equals(this.name, other.name)){
            return false;
        }
        if (!Objects.equals(this.description, other.description)){
            return false;
        }
        return true;
    }    
}
