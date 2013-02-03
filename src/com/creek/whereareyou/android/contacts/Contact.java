package com.creek.whereareyou.android.contacts;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author andreypereverzin
 */
public class Contact {
    private final String id;
    private String displayName;
    private String email;

    public Contact(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getId() {
        return id;
    }
}
