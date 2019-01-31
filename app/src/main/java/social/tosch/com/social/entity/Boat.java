package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Boat {

    @SerializedName("manufacturer")
    @Expose
    private String boatManufacturer;
    @SerializedName("model")
    @Expose
    private String boatModel;
    @SerializedName("year")
    @Expose
    private String boatYear;
    @SerializedName("loa")
    @Expose
    private String boatLoa;
    @SerializedName("flag")
    @Expose
    private String boatFlag;
    @SerializedName("name")
    @Expose
    private String boatName;
    @SerializedName("marina")
    @Expose
    private String marina;
    @SerializedName("boat_type")
    @Expose
    private String type;
    @SerializedName("photo")
    @Expose
    private Object photo;

    public String getBoatManufacturer() {
        return boatManufacturer;
    }

    public void setBoatManufacturer(String boatManufacturer) {
        this.boatManufacturer = boatManufacturer;
    }

    public String getBoatModel() {
        return boatModel;
    }

    public void setBoatModel(String boatModel) {
        this.boatModel = boatModel;
    }

    public String getBoatYear() {
        return boatYear;
    }

    public void setBoatYear(String boatYear) {
        this.boatYear = boatYear;
    }

    public String getBoatLoa() {
        return boatLoa;
    }

    public void setBoatLoa(String boatLoa) {
        this.boatLoa = boatLoa;
    }

    public String getBoatFlag() {
        return boatFlag;
    }

    public void setBoatFlag(String boatFlag) {
        this.boatFlag = boatFlag;
    }

    public String getBoatName() {
        return boatName;
    }

    public void setBoatName(String boatName) {
        this.boatName = boatName;
    }

    public String getMarina() {
        return marina;
    }

    public void setMarina(String marina) {
        this.marina = marina;
    }

    public String getType() {
        return type;
    }

    public Object getPhoto() {
        return photo;
    }
}
