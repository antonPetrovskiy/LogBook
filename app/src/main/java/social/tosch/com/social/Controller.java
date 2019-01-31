package social.tosch.com.social;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.activities.LoginActivity;
import social.tosch.com.social.activities.Main2Activity;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Contact;
import social.tosch.com.social.entity.ContactList;
import social.tosch.com.social.entity.Feed;
import social.tosch.com.social.fragments.MapFragment;

public class Controller {
    public final static Controller instance = new Controller();

    private static ArrayList<Contact> contactList;
    public static ArrayList<Feed> feedList;
    private static Context context;

    private Controller(){
        setContactList();
    }

    public static Controller getInstance(Context c){
        context = c;
        return instance;
    }

    public void update(){
        ApiService api = RetroClient.getApiService();

        final Context c = context;

        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("info", "1");
            subJsonObject.put("categories", "1");
            subJsonObject.put("boat", "1");
            jsonObject.put("params", subJsonObject);
            jsonObject.put("action", "list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
        /**
         * Calling JSON
         */
        Call<ContactList> call = api.getUserList("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);

        /**
         * Enqueue Callback will be call when get response...
         */
        call.enqueue(new Callback<ContactList>() {
            @Override
            public void onResponse(Call<ContactList> call, Response<ContactList> response) {

                if (response.isSuccessful()) {
                    /**
                     * Got Successfully
                     */
                    contactList = response.body().getContacts();
                    MapFragment.newInstance(c).updateUsers();
                    //TODO
                    // FriendsFragment.contactList = contactList;

                } else {
                    Log.i("LISTERF" , "no success");
                }
            }

            @Override
            public void onFailure(Call<ContactList> call, Throwable t) {
                Log.i("LISTERF" , t.getMessage()+" ");
            }
        });
    }

    public void setContactList(){

            ApiService api = RetroClient.getApiService();


            JSONObject jsonObject = new JSONObject();
            try {
                JSONObject subJsonObject = new JSONObject();
                subJsonObject.put("info", "1");
                subJsonObject.put("categories", "1");
                subJsonObject.put("boat", "1");
                jsonObject.put("params", subJsonObject);
                jsonObject.put("action", "list");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            /**
             * Calling JSON
             */
            Call<ContactList> call = api.getUserList("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);

            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<ContactList>() {
                @Override
                public void onResponse(Call<ContactList> call, Response<ContactList> response) {

                    if (response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        contactList = response.body().getContacts();
                        //TODO
                        // FriendsFragment.contactList = contactList;

                    } else {
                        Log.i("LISTERF" , "no success");
                    }
                }

                @Override
                public void onFailure(Call<ContactList> call, Throwable t) {
                    Log.i("LISTERF" , t.getMessage()+" ");
                }
            });
    }


    public ArrayList<Contact> getContactList(){
        return contactList;
    }

}
