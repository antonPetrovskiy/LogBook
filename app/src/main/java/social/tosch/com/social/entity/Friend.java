package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Friend {
    @SerializedName("invitation_id")
    @Expose
    private String invitation_id;
    @SerializedName("user_id")
    @Expose
    private String user_id;


    public String getInvitation_id() {
        return invitation_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
