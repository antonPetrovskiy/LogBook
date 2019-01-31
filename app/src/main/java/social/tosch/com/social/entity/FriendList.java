package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FriendList {
    @SerializedName("ids")
    @Expose
    private ArrayList<Friend> ids = new ArrayList<>();

    public ArrayList<Friend> getIds() {
        return ids;
    }
}
