package com.creek.whereareyou.android.contacts;

import org.json.simple.JSONObject;

import com.creek.whereareyoumodel.message.Transformable;

/**
 * 
 * @author andreypereverzin
 */
@SuppressWarnings("serial")
public class ContactToInform extends Contact implements Transformable {
    private static final String CHECKED = "checked";

    private boolean checked;
    
    public ContactToInform(String id) {
        super(id);
    }

    public ContactToInform(JSONObject jsonObject) {
        super(jsonObject);
        this.checked = Boolean.parseBoolean((String) jsonObject.get(CHECKED));
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject dataObject = super.toJSON();
        dataObject.put(CHECKED, isChecked());
        return dataObject;
    }
}
