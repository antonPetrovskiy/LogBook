package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PortList {

    @SerializedName("ports")
    @Expose
    private ArrayList<Port> ports = new ArrayList<>();


    public ArrayList<Port> getPorts() {
        return ports;
    }


    public void setPorts(ArrayList<Port> ports) {
        this.ports = ports;
    }
}
