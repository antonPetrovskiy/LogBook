package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("photo")
    @Expose
    private Object photo;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Object getPhoto() {
        return photo;
    }
}
