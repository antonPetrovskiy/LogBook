package social.tosch.com.social.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.Controller;
import social.tosch.com.social.R;
import social.tosch.com.social.adapter.FeedAdapter;
import social.tosch.com.social.adapter.MessageAdapter;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Feed;
import social.tosch.com.social.entity.FeedList;
import social.tosch.com.social.entity.Message;
import social.tosch.com.social.entity.MessageList;
import social.tosch.com.social.fragments.MessagesFragment;

public class DialogActivity extends AppCompatActivity {

    private static ListView listView;
    public static ArrayList<Message> messageList;
    public static ArrayList<Feed> feedList;
    public static String id;
    private static MessageAdapter adapter;
    private static FeedAdapter adapter1;
    ApiService api;

    private EditText input;
    private ImageView sms;
    private ImageView photo;
    private TextView name;
    private ImageView back;
    private static boolean dialog;
    private static boolean news;
    private ConstraintLayout backg;
    private LinearLayout arr;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Main2Activity.isChat = true;
        context = getApplicationContext();

        init();
        buttons();
        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Main2Activity.isChat = true;
    }

    public void init(){
        listView = findViewById(R.id.list);
        photo = findViewById(R.id.imageView);
        name = findViewById(R.id.textViewName);
        id = getIntent().getStringExtra("id");
        sms = findViewById(R.id.imageView7);
        input = findViewById(R.id.text);
        back = findViewById(R.id.imageView6);
        backg = findViewById(R.id.back);
        arr = findViewById(R.id.linearLayout2);
        api = RetroClient.getApiService();

        if(id.equals("sell_buy")||id.equals("service")||id.equals("sport")||id.equals("else")){
            news = true;
            dialog = false;
        }else{
            news = false;
            dialog = true;
        }
        if(dialog){
            name.setText("Removed user");
            for(int i = 0; i < Controller.getInstance(getApplicationContext()).getContactList().size(); i++){
                if(Controller.getInstance(getApplicationContext()).getContactList().get(i).getId().equals(id)){
                    name.setText(Controller.getInstance(getApplicationContext()).getContactList().get(i).getName());
                    if(Controller.getInstance(getApplicationContext()).getContactList().get(i).getPhoto()!=null && !Controller.getInstance(getApplicationContext()).getContactList().get(i).getPhoto().equals("null")) {
                        LinkedTreeMap<String,String> map = (LinkedTreeMap) Controller.getInstance(getApplicationContext()).getContactList().get(i).getPhoto();
                        Picasso.with(getApplicationContext()).load(map.get("thumb")).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(photo);
                    } else {
                        photo.setImageResource(R.drawable.photo1);
                    }
                }
            }
        }
        if(news){
                switch (id){
                    case "sell_buy":
                        name.setText("Покупка/продажа");
                        photo.setImageResource(0);
                        break;
                    case "service":
                        name.setText("Сервис");
                        photo.setImageResource(0);
                        break;
                    case "sport":
                        name.setText("Парусный спорт");
                        photo.setImageResource(0);
                        break;
                    case "else":
                        name.setText("Общая");
                        photo.setImageResource(0);
                        break;

            }
        }



    }

    public void buttons(){

        final View activityRootView = findViewById(R.id.back);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(getApplicationContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
                    backg.setBackgroundResource(R.drawable.news_bg_half);
                }else{
                    backg.setBackgroundResource(R.drawable.news_bg);
                }
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                    if (heightDiff > dpToPx(getApplicationContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }else{

                    }

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(messageList.get(i).getUser_id().equals(Main2Activity.id))
                    editMessage(i);
                return false;
            }
        });


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main2Activity.chatId = id;
                Intent intent = new Intent(DialogActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog){
                    if (!input.getText().toString().equals("")) {
                        if (((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                            //Creating an object of our api interface
                            Call<ResponseBody> call = api.sendMessage("Bearer " + LoginActivity.stringBuilder.toString(), id, input.getText().toString());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {

                                        input.setText("");
                                        //input.clearFocus();
                                        update();
                                        input.setGravity(Gravity.CENTER_VERTICAL);

                                    } else {
                                        Log.i("LISTERF", "no success");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.i("LISTERF", t.getMessage()+" ");
                                }
                            });

                        } else {
                            Log.i("LIST", "no internet");
                        }
                    }
                }
                if(news){
                    if (!input.getText().toString().equals("")) {
                        if (((ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                            //Creating an object of our api interface
                            Call<ResponseBody> call = api.setFeed("Bearer " + LoginActivity.stringBuilder.toString(), "news", input.getText().toString(), id);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {

                                        input.setText("");
                                        input.clearFocus();
                                        update();

                                    } else {
                                        Log.i("LISTERF", "no success");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.i("LISTERF", t.getMessage()+" ");
                                }
                            });

                        } else {
                            Log.i("LIST", "no internet");
                        }
                    }
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setGravity(Gravity.CENTER_VERTICAL);
                onBackPressed();
            }
        });




    }

    public static void update(){
        if(dialog){
            if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                final ApiService api = RetroClient.getApiService();
                Call<MessageList> call = api.getMessages("Bearer " + LoginActivity.stringBuilder.toString(), id);
                call.enqueue(new Callback<MessageList>() {
                    @Override
                    public void onResponse(Call<MessageList> call, Response<MessageList> response) {

                        if (response.isSuccessful()) {
                            messageList = response.body().getMessages();
                            if(messageList!=null){
                                Log.i("lastmessage" , messageList.get(0).getText()+" ");
                                Log.i("lastmessage" , messageList.get(0).getId()+" ");
                                Call<ResponseBody> call1 = api.readMessage("Bearer " + LoginActivity.stringBuilder.toString(), messageList.get(0).getId());
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });

                                Collections.reverse(messageList);
                                adapter = new MessageAdapter(context, messageList);
                                listView.setAdapter(adapter);
                                Log.i("lastmessage" , messageList.get(0).getText()+" ");

                            }


                        } else {
                            Log.i("LISTERF" , "no success");
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageList> call, Throwable t) {
                        Log.i("LISTERF" , t.getMessage()+" ");
                    }
                });

            } else {
                Log.i("LIST" , "no internet");
            }
        }
        if(news){
            if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                ApiService api = RetroClient.getApiService();
                Call<FeedList> call = api.getFeedList("Bearer " + LoginActivity.stringBuilder.toString(), id);
                call.enqueue(new Callback<FeedList>() {
                    @Override
                    public void onResponse(Call<FeedList> call, Response<FeedList> response) {
                        if (response.isSuccessful()) {
                            feedList = response.body().getFeeds();
                            if(feedList!=null){
                                Collections.reverse(feedList);
                                adapter1 = new FeedAdapter(context, feedList);
                                listView.setAdapter(adapter1);
                            }

                        } else {
                            Log.i("LISTERF" , "no success");
                        }
                    }
                    @Override
                    public void onFailure(Call<FeedList> call, Throwable t) {
                        Log.i("LISTERF" , t.getMessage()+" ");
                    }
                });
            } else {
                Log.i("LIST" , "no internet");
            }
        }

    }

    public void editMessage(int n){
        final int i = n;
        String title = "Редактировать сообщение";
        String button1String = "Изменить";
        String button2String = "Удалить";
        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(this);
        ad.setTitle(title);  // заголовок
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                renameMessage(i);
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                deleteMessage(i);
            }
        });
        ad.show();
    }

    public void renameMessage(int n){
        final int i = n;
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.layout_dialog_editmessage, null);
        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        final EditText text = promptView.findViewById(R.id.editText2);
        text.setText(messageList.get(i).getText());
        Button btnAdd1 = promptView.findViewById(R.id.imageView1);
        Button btnAdd2 = promptView.findViewById(R.id.imageView2);

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertD.cancel();

            }
        });

        btnAdd2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertD.cancel();
                if (((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                    ApiService api = RetroClient.getApiService();

                    Call<ResponseBody> call = api.editMessage("Bearer " + LoginActivity.stringBuilder.toString(),messageList.get(i).getId(),text.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.i("REMOVEDIALOG" , "SUCCESS");
                                update();
                            } else {
                                JSONObject jObjError = null;
                                try {
                                    jObjError = new JSONObject(response.errorBody().string());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Log.i("REMOVEDIALOG" , jObjError.getString("message")+" ");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                } else {
                    Log.i("LIST" , "no internet");
                }

            }
        });


        alertD.setView(promptView);
        alertD.show();


    }

    public void deleteMessage(int n){
        final int i = n;
                if (((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                    ApiService api = RetroClient.getApiService();

                    Call<ResponseBody> call = api.deleteMessage("Bearer " + LoginActivity.stringBuilder.toString(),messageList.get(i).getId());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.i("REMOVEMESSAGE" , "SUCCESS");
                                update();
                            } else {
                                JSONObject jObjError = null;
                                try {
                                    jObjError = new JSONObject(response.errorBody().string());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    Log.i("REMOVEMESSAGE" , jObjError.getString("message")+" ");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                } else {
                    Log.i("LIST" , "no internet");
                }

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

}
