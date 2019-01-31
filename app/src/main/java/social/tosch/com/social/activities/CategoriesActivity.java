package social.tosch.com.social.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.R;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Boat;
import social.tosch.com.social.entity.Category;
import social.tosch.com.social.entity.CategoryList;
import social.tosch.com.social.entity.Contact;
import social.tosch.com.social.fragments.MapFragment;

public class CategoriesActivity extends AppCompatActivity {

    ApiService api;
    private LinearLayout lay_owner;
    private LinearLayout lay_worker;
    private LinearLayout lay_service;
    private LinearLayout lay_findwork;
    private LinearLayout lay_findteam;
    private LinearLayout lay_sell;
    private LinearLayout lay_create;

    private ImageView back;
    private ImageView save;




    private EditText setcategory;
    String toremove;


    ArrayList<String> categories;
    ArrayList<String> categoriesId;

    String[] data = {"капитан", "работаю", "использую чартер", "спортсмен", "сервис", "ищу работу", "ищу команду", "брокер", "строитель", "преподаватель", "яхтенный агент"};
    String[] data_owner_type = {"моторная", "парусная"};
    String[] data_owner_manufacturer = {"производитель 1", "производитель 2", "производитель 3", "производитель 4",};
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter_data_owner_manufacturer;

    //owner info
    private Spinner owner_type;
    private Spinner owner_manufacturer;
    private EditText owner_model;
    private EditText owner_loa;
    private EditText owner_year;
    private EditText owner_flag;
    private EditText owner_name;
    private EditText owner_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        init();
        button();

        //showCategory();

    }

    public void init(){
        api = RetroClient.getApiService();


        lay_owner = findViewById(R.id.owner_info);
        lay_worker = findViewById(R.id.worker_info);
        lay_service = findViewById(R.id.service_info);
        lay_findwork = findViewById(R.id.findwork_info);
        lay_findteam = findViewById(R.id.findteam_info);
        lay_sell = findViewById(R.id.sell_info);
        lay_create = findViewById(R.id.create_info);
        lay_owner.setVisibility(View.GONE);
        lay_worker.setVisibility(View.GONE);
        lay_service.setVisibility(View.GONE);
        lay_findwork.setVisibility(View.GONE);
        lay_findteam.setVisibility(View.GONE);
        lay_sell.setVisibility(View.GONE);
        lay_create.setVisibility(View.GONE);


        back = findViewById(R.id.imageView22);
        save = findViewById(R.id.imageView4);
        setcategory = findViewById(R.id.setcategory);


        owner_type = findViewById(R.id.type);
        adapter = new ArrayAdapter<String>(CategoriesActivity.this, R.layout.spinner_item, data_owner_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        owner_type.setAdapter(adapter);

        owner_manufacturer = findViewById(R.id.manufacturer);
        adapter = new ArrayAdapter<String>(CategoriesActivity.this, R.layout.spinner_item, data_owner_manufacturer);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        owner_manufacturer.setAdapter(adapter);



    }

    public void button(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad;
                String message;
                String button1String;
                String button2String;
                message = "Сохранить данные?";
                button1String = "да";
                button2String = "нет";



                ad = new AlertDialog.Builder(CategoriesActivity.this);
                ad.setMessage(message); // сообщение
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        //addCategory(arr);
                        onBackPressed();
                    }
                });
                ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });

                ad.show();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad;
                String message;
                String button1String;
                String button2String;
                message = "Несохраненные данные будут потеряны";
                button1String = "да";
                button2String = "нет";



                ad = new AlertDialog.Builder(CategoriesActivity.this);
                ad.setMessage(message); // сообщение
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                        onBackPressed();
                    }
                });
                ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });

                ad.show();

            }
        });

        setcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }





    public void dialog(int x){
        AlertDialog.Builder ad;
        final int n = x;
        String message;
        String button1String;
        String button2String;
        message = "Удалить категорию?";
        button1String = "да";
        button2String = "нет";

    }

    public void addCategory(ArrayList<String> s){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jArry=new JSONArray();
            for (int i = 0; i < s.size(); i ++)
            {
                JSONObject jObjd=new JSONObject();
                jObjd.put("title", s.get(i));
                jArry.put(jObjd);
            }
            jsonObject.put("categories", jArry);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());

        Call<ResponseBody> call = api.addCategory("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("CATEGORY" , "success");

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
                        Log.i("CATEGORY" , jObjError.getString("message")+"1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("CATEGORY" , t.getMessage());
            }
        });
    }

    public void deleteCategory(String s){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jArry=new JSONArray();
            jArry.put(s);
            Log.i("DELETE" , s+"");
            jsonObject.put("categories_ids", jArry);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());

        Call<ResponseBody> call = api.deleteCategory("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("DELETE" , "success");

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
                        Log.i("DELETE" , jObjError.getString("message")+"1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("DELETE" , t.getMessage());
            }
        });
    }

}
