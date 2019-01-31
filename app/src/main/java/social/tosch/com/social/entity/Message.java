package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("for_user_id")
    @Expose
    private String for_user_id;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFor_user_id() {
        return for_user_id;
    }

    public String getTime() {
        return created_at;
    }

    public String getStatus() {
        return status;
    }
}
