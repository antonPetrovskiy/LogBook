package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
