package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Map;

public class DialogList {
    @SerializedName("correspondens")
    @Expose
    private ArrayList<Dialog> ids = new ArrayList<>();


    public ArrayList<Dialog> getIds() {
        return ids;
    }
}
