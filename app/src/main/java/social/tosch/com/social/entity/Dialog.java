package social.tosch.com.social.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.HashMap;

public class Dialog {
    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("last_message")
    @Expose
    private HashMap<String, String> last_message;



    public String getUser_id() {
        return user_id;
    }

    public HashMap<String, String> getLast_message() {
        return last_message;
    }

    public static Comparator<Dialog> COMPARE_BY_LASTMESSAGE = new Comparator<Dialog>() {
        public int compare(Dialog one, Dialog other) {
            return one.last_message.get("created_at").compareTo(other.last_message.get("created_at"));
        }
    };

}
