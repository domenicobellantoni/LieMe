package com.bellantoni.chetta.lieme.generalclasses;

/**
 * Created by alessandro on 6/2/15.
 */
/**
 * Contact representation
 */
public class Contact {
    private String id;
    private String name;
    private String facebook_id;
    private String timestamp;

    public Contact(String id, String name, String facebook_id, String timestamp) {
        this.id = id;
        this.name = name;
        this.facebook_id = facebook_id;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
