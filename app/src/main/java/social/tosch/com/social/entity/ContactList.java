package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import social.tosch.com.social.entity.Contact;

public class ContactList {

    @SerializedName("users")
    @Expose
    private ArrayList<Contact> contacts = new ArrayList<>();


    public ArrayList<Contact> getContacts() {
        return contacts;
    }

}
