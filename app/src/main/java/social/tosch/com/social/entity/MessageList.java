package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageList {
    @SerializedName("messages")
    @Expose
    private ArrayList<Message> messages = new ArrayList<>();


    public ArrayList<Message> getMessages() {
        return messages;
    }
}
