package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryList {
    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories = new ArrayList<>();


    public ArrayList<Category> getCategories() {
        return categories;
    }
}
