package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhotoList {
    @SerializedName("gallery")
    @Expose
    private ArrayList<Photo> gallery = new ArrayList<>();


    public ArrayList<Photo> getGallery() {
        return gallery;
    }
}
