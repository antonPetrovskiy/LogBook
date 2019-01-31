package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contact {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("email")
    @Expose
    private String eMail;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("location")
    @Expose
    private HashMap<String, String> location;
    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories;
    @SerializedName("boat_name")
    @Expose
    private String boatName;
    @SerializedName("boat_photo")
    @Expose
    private Object boatPhoto;
    @SerializedName("boat_type")
    @Expose
    private String boatType;
    @SerializedName("boat_flag")
    @Expose
    private String boatFlag;
    @SerializedName("boat_model")
    @Expose
    private String boatModel;
    @SerializedName("boat_loa")
    @Expose
    private String boatLoa;
    @SerializedName("boat_year")
    @Expose
    private String boatYear;
    @SerializedName("build_info")
    @Expose
    private String phone;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public HashMap<String, String> getLocation() {
        return location;
    }

    public void setLocation(HashMap location) {
        this.location = location;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public String getBoatName() {
        return boatName;
    }

    public String getBoatType() {
        return boatType;
    }

    public String getBoatFlag() {
        return boatFlag;
    }

    public Object getBoatPhoto() {
        return boatPhoto;
    }

    public String getBoatModel() {
        return boatModel;
    }

    public String getBoatYear() {
        return boatYear;
    }

    public String getBoatLoa() {
        return boatLoa;
    }

    public String getPhone() {
        return phone;
    }
}
