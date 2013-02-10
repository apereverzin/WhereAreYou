package com.creek.whereareyou.android.contacts;

import org.json.simple.JSONObject;

import com.creek.whereareyoumodel.message.Transformable;

/**
 * 
 * @author andreypereverzin
 */
@SuppressWarnings("serial")
public class Contact implements Transformable {
    private static final String ID = "id";
    private static final String DISPLAY_NAME = "displayName";
    private static final String EMAIL = "email";

    private final String id;
    private String displayName;
    private String email;

    public Contact(String id) {
        this.id = id;
    }

    public Contact(JSONObject jsonObject) {
        this.id = (String) jsonObject.get(ID);
        this.displayName = (String) jsonObject.get(DISPLAY_NAME);
        this.email = (String) jsonObject.get(EMAIL);
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

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject dataObject = new JSONObject();
        dataObject.put(ID, getId());
        dataObject.put(DISPLAY_NAME, getDisplayName());
        dataObject.put(EMAIL, getEmail());
        return dataObject;
    }
}
