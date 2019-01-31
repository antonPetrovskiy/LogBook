package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Port {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


}
