package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FeedList {
    @SerializedName("news")
    @Expose
    private ArrayList<Feed> feeds = new ArrayList<>();


    public ArrayList<Feed> getFeeds() {
        return feeds;
    }
}
