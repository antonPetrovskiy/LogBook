package social.tosch.com.social.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.chipview.ChipView;
import com.allyants.chipview.SimpleChipAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.Controller;
import social.tosch.com.social.R;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Boat;
import social.tosch.com.social.entity.Category;
import social.tosch.com.social.entity.CategoryList;
import social.tosch.com.social.entity.Contact;
import social.tosch.com.social.fragments.MapFragment;


public class InformationActivity extends AppCompatActivity {
    ApiService api;
    //main info
    private String id;
    private ImageView photo_view;
    private EditText name;
    private EditText phone;
    private EditText country;
    private EditText experience;
    private String nameFirst;
    private String phoneFirst;
    private String countryFirst;
    Uri destination;
    ArrayList<Category> category;
    String[] data = {"капитан", "команда", "использую чартерные суда", "парусный спорт", "сервис и обслуживание яхт", "ищу работу", "набираю команду", "брокераж, продажа аренда яхт", "строю яхты", "преподаватель, яхтенная школа"};
    String[] tmpdata = {"капитан", "команда", "использую чартерные суда", "парусный спорт", "сервис и обслуживание яхт", "ищу работу", "набираю команду", "брокераж, продажа аренда яхт", "строю яхты", "преподаватель, яхтенная школа"};
    private ImageView back;
    private ImageView save;
    private int categories_num = 0;

    File directory;
    File myFile;
    public static final int REQ_CODE_PICK_PHOTO = 0;
    public static final int REQ_CODE_PICK_PHOTO_GALLERY = 1;
    public static final int REQ_CODE_CAPTURE = 2;

    //categories
    private TextView tw1;
    private TextView tw2;
    private TextView tw3;
    private TextView tw4;
    private TextView tw5;
    private TextView tw6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        init();
        createDirectory();
        button();


        update();
        updateId();
    }

    public void init(){
        api = RetroClient.getApiService();
        back = findViewById(R.id.imageView22);
        save = findViewById(R.id.imageView4);
        photo_view = findViewById(R.id.imageProfile);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.address);
        country = findViewById(R.id.country);
        experience = findViewById(R.id.experience);
        category = new ArrayList<>();
        tw1 = findViewById(R.id.textView12);
        tw2 = findViewById(R.id.textView14);
        tw3 = findViewById(R.id.textView16);
        tw4 = findViewById(R.id.textView17);
        tw5 = findViewById(R.id.textView18);
        tw6 = findViewById(R.id.textView19);
        nameFirst = " ";
        phoneFirst = " ";
        countryFirst = " ";
    }

    public void button(){

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



                ad = new AlertDialog.Builder(InformationActivity.this);
                ad.setMessage(message); // сообщение
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        editInfo();
                        Intent intent = new Intent(InformationActivity.this, Main2Activity.class);
                        startActivity(intent);
                    }
                });
                ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Intent intent = new Intent(InformationActivity.this, Main2Activity.class);
                        startActivity(intent);
                    }
                });

                editInfo();
                Toast.makeText(getApplicationContext(), "Обновление информации", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InformationActivity.this, Main2Activity.class);
                startActivity(intent);

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



                ad = new AlertDialog.Builder(InformationActivity.this);
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


                    onBackPressed();


            }
        });

        photo_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(InformationActivity.this);
                View promptView = layoutInflater.inflate(R.layout.imagechooser, null);
                final AlertDialog alertD = new AlertDialog.Builder(InformationActivity.this).create();

                ImageView btnAdd1 = promptView.findViewById(R.id.imageView1);
                ImageView btnAdd2 = promptView.findViewById(R.id.imageView2);

                btnAdd1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertD.cancel();
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri());
                        startActivityForResult(intent, REQ_CODE_CAPTURE);
                    }
                });

                btnAdd2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertD.cancel();
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        startActivityForResult(intent,  REQ_CODE_PICK_PHOTO);

                        //Crop.pickImage(InformationActivity.this);
                    }
                });

                alertD.setView(promptView);
                alertD.show();
            }
        });
        experience.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    experience.setText("");
                }
            }
        });


        tw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tw1.getText().toString().equals("пусто")){
                    dialog(1);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setTitle("Добавьте категорию");
                    final String []dsf = new String[getTempArr().size()];
                    getTempArr().toArray(dsf);
                    builder.setItems(dsf, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> ar = new ArrayList<>();
                            ar.add(dsf[which]);
                            addCategory(ar);
                            //updateCategory();
                        }
                    });
                    builder.show();
                }
            }
        });
        tw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tw2.getText().toString().equals("пусто")){
                    dialog(2);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setTitle("Добавьте категорию");
                    final String []dsf = new String[getTempArr().size()];
                    getTempArr().toArray(dsf);
                    builder.setItems(dsf, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> ar = new ArrayList<>();
                            ar.add(dsf[which]);
                            addCategory(ar);
                            //updateCategory();
                        }
                    });
                    builder.show();
                }
            }
        });
        tw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tw3.getText().toString().equals("пусто")){
                    dialog(3);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setTitle("Добавьте категорию");
                    final String []dsf = new String[getTempArr().size()];
                    getTempArr().toArray(dsf);
                    builder.setItems(dsf, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> ar = new ArrayList<>();
                            ar.add(dsf[which]);
                            addCategory(ar);
                            //updateCategory();
                        }
                    });
                    builder.show();
                }
            }
        });
        tw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tw4.getText().toString().equals("пусто")){
                    dialog(4);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setTitle("Добавьте категорию");
                    final String []dsf = new String[getTempArr().size()];
                    getTempArr().toArray(dsf);
                    builder.setItems(dsf, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> ar = new ArrayList<>();
                            ar.add(dsf[which]);
                            addCategory(ar);
                            //updateCategory();
                        }
                    });
                    builder.show();
                }
            }
        });
        tw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tw5.getText().toString().equals("пусто")){
                    dialog(5);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setTitle("Добавьте категорию");
                    final String []dsf = new String[getTempArr().size()];
                    getTempArr().toArray(dsf);
                    builder.setItems(dsf, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> ar = new ArrayList<>();
                            ar.add(dsf[which]);
                            addCategory(ar);
                            //updateCategory();
                        }
                    });
                    builder.show();
                }
            }
        });
        tw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tw6.getText().toString().equals("пусто")){
                    dialog(6);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setTitle("Добавьте категорию");
                    final String []dsf = new String[getTempArr().size()];
                    getTempArr().toArray(dsf);
                    builder.setItems(dsf, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> ar = new ArrayList<>();
                            ar.add(dsf[which]);
                            addCategory(ar);
                            //updateCategory();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    public void update(){
        if (((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            Call<Contact> call = api.getUser("Bearer " + LoginActivity.stringBuilder.toString());
            Log.i("GETS" , "Bearer " + LoginActivity.stringBuilder.toString());

            call.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if (response.isSuccessful()) {
                        name.setText(response.body().getName());
                        phone.setText(response.body().getPhone());
                        country.setText(response.body().getCountry());
                        if(response.body().getAge()!=null)
                            experience.setText("С "+response.body().getAge()+" года");
                        nameFirst = name.getText().toString();
                        phoneFirst = phone.getText().toString();
                        countryFirst = country.getText().toString();


                        //MapFragment.newInstance(getApplicationContext()).updateMap();
                        //Controller.getInstance(getApplicationContext()).update();
                        //MapFragment.newInstance(getApplicationContext()).updateUsers();

                        if(response.body().getPhoto()!=null && !response.body().getPhoto().equals("null")) {
                            LinkedTreeMap<String,String> map = (LinkedTreeMap) response.body().getPhoto();
                            Picasso.with(InformationActivity.this).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(photo_view);

                        }
                    } else {
                        Log.i("GETS" , response.toString()+" ");
                    }
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    //Snackbar.make(parentView, "fail", Snackbar.LENGTH_LONG).show_right();
                }
            });

        } else {
            //Snackbar.make(parentView, "error", Snackbar.LENGTH_LONG).show_right();
        }
    }

    public void updatePhoto(){
        if (((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            Call<Contact> call = api.getUser("Bearer " + LoginActivity.stringBuilder.toString());
            Log.i("GETS" , "Bearer " + LoginActivity.stringBuilder.toString());

            call.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getPhoto()!=null && !response.body().getPhoto().equals("null")) {
                            LinkedTreeMap<String,String> map = (LinkedTreeMap) response.body().getPhoto();
                            Picasso.with(InformationActivity.this).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(photo_view);
                        }
                    } else {
                        Log.i("GETS" , response.toString()+" ");
                    }
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    //Snackbar.make(parentView, "fail", Snackbar.LENGTH_LONG).show_right();
                }
            });

        } else {
            //Snackbar.make(parentView, "error", Snackbar.LENGTH_LONG).show_right();
        }
    }

    public void updateId(){
        if (((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            Call<Contact> call = api.getUser("Bearer " + LoginActivity.stringBuilder.toString());
            Log.i("GETS" , "Bearer " + LoginActivity.stringBuilder.toString());
            call.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if (response.isSuccessful()) {
                        id = response.body().getId();
                        Log.i("myid", id+"ц");
                        updateCategory();
                    } else {
                        Log.i("myid" , response.toString()+" ");
                    }
                }
                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    Log.i("myid" , call.toString()+" ");
                }
            });


        } else {
            Log.i("myid" , "wtf");
        }
    }

    public void updateCategory(){
        if (((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                JSONObject subJsonObject = new JSONObject();
                subJsonObject.put("categories", "1");
                jsonObject.put("params", subJsonObject);
                jsonObject.put("action", "by-id");
                Log.i("myid" , id+"p");
                jsonObject.put("id", Integer.valueOf(id));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());
            Call<CategoryList> call = api.getCategories("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);

            call.enqueue(new Callback<CategoryList>() {
                @Override
                public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                    if (response.isSuccessful()) {
                        category = response.body().getCategories();
                        categories_num = category.size();
                        showCategory();

                    } else {
                        Log.i("categorieeees", response.toString()+" ");
                    }
                }

                @Override
                public void onFailure(Call<CategoryList> call, Throwable t) {
                    Log.i("categorieeees", "fail"+" ");
                }
            });
        }
    }

    public void showCategory(){
        for(int i = 0; i < category.size(); i ++){
            if(i==0) {
                tw1.setText(category.get(i).getTitle());
                tw1.setBackgroundResource(R.drawable.layout_bg5);
            }
            if(i==1) {
                tw2.setText(category.get(i).getTitle());
                tw2.setBackgroundResource(R.drawable.layout_bg5);
            }
            if(i==2) {
                tw3.setText(category.get(i).getTitle());
                tw3.setBackgroundResource(R.drawable.layout_bg5);
            }
            if(i==3) {
                tw4.setText(category.get(i).getTitle());
                tw4.setBackgroundResource(R.drawable.layout_bg5);
            }
            if(i==4) {
                tw5.setText(category.get(i).getTitle());
                tw5.setBackgroundResource(R.drawable.layout_bg5);
            }
            if(i==5) {
                tw6.setText(category.get(i).getTitle());
                tw6.setBackgroundResource(R.drawable.layout_bg5);
            }
        }
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



        ad = new AlertDialog.Builder(InformationActivity.this);
        ad.setMessage(message); // сообщение
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                switch (n){
                    case 1:
                        for(int i = 0; i < category.size(); i ++){
                            if(category.get(i).getTitle().equals(tw1.getText().toString())) {
                                deleteCategory(category.get(i).getId());
                                category.remove(i);
                                tw1.setText("пусто");
                                tw1.setBackgroundResource(R.drawable.layout_bg6);
                            }
                        }
                        break;
                    case 2:
                        for(int i = 0; i < category.size(); i ++){
                            Log.i("categori", category.size()+" ");
                            if(category.get(i).getTitle().equals(tw2.getText().toString())) {
                                deleteCategory(category.get(i).getId());
                                category.remove(i);
                                tw2.setText("пусто");
                                tw2.setBackgroundResource(R.drawable.layout_bg6);
                            }
                        }
                        break;
                    case 3:
                        for(int i = 0; i < category.size(); i ++){
                            if(category.get(i).getTitle().equals(tw3.getText().toString())) {
                                deleteCategory(category.get(i).getId());
                                category.remove(i);
                                tw3.setText("пусто");
                                tw3.setBackgroundResource(R.drawable.layout_bg6);
                            }
                        }
                        break;
                    case 4:
                        for(int i = 0; i < category.size(); i ++){
                            if(category.get(i).getTitle().equals(tw4.getText().toString())) {
                                deleteCategory(category.get(i).getId());
                                category.remove(i);
                                tw4.setText("пусто");
                                tw4.setBackgroundResource(R.drawable.layout_bg6);
                            }
                        }
                        break;
                    case 5:
                        for(int i = 0; i < category.size(); i ++){
                            if(category.get(i).getTitle().equals(tw5.getText().toString())) {
                                deleteCategory(category.get(i).getId());
                                category.remove(i);
                                tw5.setText("пусто");
                                tw5.setBackgroundResource(R.drawable.layout_bg6);
                            }
                        }
                        break;
                    case 6:
                        for(int i = 0; i < category.size(); i ++){
                            if(category.get(i).getTitle().equals(tw6.getText().toString())) {
                                deleteCategory(category.get(i).getId());
                                category.remove(i);
                                tw6.setText("пусто");
                                tw6.setBackgroundResource(R.drawable.layout_bg6);
                            }
                        }
                        break;
                }
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });

        ad.show();
    }

    public void editInfo(){
        JSONObject jsonObject = new JSONObject();
        try {
            if(!name.getText().toString().equals(""))
                jsonObject.put("name", name.getText().toString());
            if(!country.getText().toString().equals(""))
                jsonObject.put("country", country.getText().toString());
            if(!phone.getText().toString().equals(""))
                jsonObject.put("build_info", phone.getText().toString());
            if(!experience.getText().toString().equals(""))
                if(experience.getText().toString().length()==4){
                    jsonObject.put("age", experience.getText().toString());
                }else{
                    jsonObject.put("age", experience.getText().toString().substring(2,7));
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());


        Call<ResponseBody> call = api.setCoord("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("COORD" , "success");
                } else {
                    Log.i("COORD" , response.message()+" ");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("COORD" , t.getMessage()+" ");
            }
        });
    }

    public void addCategory(ArrayList<String> s){
        JSONObject jsonObject = new JSONObject();
        String s1 = "";
        try {
            JSONArray jArry=new JSONArray();
            for (int i = 0; i < s.size(); i ++)
            {
                switch (s.get(i)){
                    case "капитан":
                        s1 = "капитан";
                        break;
                    case "команда":
                        s1 = "команда";
                        break;
                    case "использую чартерные суда":
                        s1 = "чартер";
                        break;
                    case "парусный спорт":
                        s1 = "спортсмен";
                        break;
                    case "сервис и обслуживание яхт":
                        s1 = "сервис";
                        break;
                    case "ищу работу":
                        s1 = "ищу работу";
                        break;
                    case "набираю команду":
                        s1 = "набираю команду";
                        break;
                    case "брокераж, продажа аренда яхт":
                        s1 = "брокер";
                        break;
                    case "строю яхты":
                        s1 = "строитель";
                        break;
                    case "преподаватель, яхтенная школа":
                        s1 = "преподаватель";
                        break;

                }
                JSONObject jObjd=new JSONObject();
                jObjd.put("title", s1);
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
                    updateCategory();
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
                Log.i("CATEGORY" , t.getMessage()+" ");
            }
        });
    }

    public void deleteCategory(String s){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jArry=new JSONArray();
            jArry.put(s);
            Log.i("DELETE" , s+" ");
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
                    updateCategory();
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
                Log.i("DELETE" , t.getMessage()+" ");
            }
        });
    }

    public ArrayList<String> getTempArr(){
        ArrayList<String> ar = new ArrayList<>();
        ArrayList<String> arTmp = new ArrayList<>();


        arTmp.add(tw1.getText().toString());
        arTmp.add(tw2.getText().toString());
        arTmp.add(tw3.getText().toString());
        arTmp.add(tw4.getText().toString());
        arTmp.add(tw5.getText().toString());
        arTmp.add(tw6.getText().toString());
        String[] tmpdata = {"капитан", "команда", "использую чартерные суда", "парусный спорт", "сервис и обслуживание яхт", "ищу работу", "набираю команду", "брокераж, продажа аренда яхт", "строю яхты", "преподаватель, яхтенная школа"};
        if(!arTmp.get(0).equals("капитан") && !arTmp.get(1).equals("капитан") &&!arTmp.get(2).equals("капитан") &&!arTmp.get(3).equals("капитан") &&!arTmp.get(4).equals("капитан") &&!arTmp.get(5).equals("капитан")){
            ar.add("капитан");
        }
        if(!arTmp.get(0).equals("команда") && !arTmp.get(1).equals("команда") &&!arTmp.get(2).equals("команда") &&!arTmp.get(3).equals("команда") &&!arTmp.get(4).equals("команда") &&!arTmp.get(5).equals("команда")){
            ar.add("команда");
        }
        if(!arTmp.get(0).equals("чартер") && !arTmp.get(1).equals("чартер") &&!arTmp.get(2).equals("чартер") &&!arTmp.get(3).equals("чартер") &&!arTmp.get(4).equals("чартер") &&!arTmp.get(5).equals("чартер")){
            ar.add("использую чартерные суда");
        }
        if(!arTmp.get(0).equals("спортсмен") && !arTmp.get(1).equals("спортсмен") &&!arTmp.get(2).equals("спортсмен") &&!arTmp.get(3).equals("спортсмен") &&!arTmp.get(4).equals("спортсмен") &&!arTmp.get(5).equals("спортсмен")){
            ar.add("парусный спорт");
        }
        if(!arTmp.get(0).equals("сервис") && !arTmp.get(1).equals("сервис") &&!arTmp.get(2).equals("сервис") &&!arTmp.get(3).equals("сервис") &&!arTmp.get(4).equals("сервис") &&!arTmp.get(5).equals("сервис")){
            ar.add("сервис и обслуживание яхт");
        }
        if(!arTmp.get(0).equals("ищу работу") && !arTmp.get(1).equals("ищу работу") &&!arTmp.get(2).equals("ищу работу") &&!arTmp.get(3).equals("ищу работу") &&!arTmp.get(4).equals("ищу работу") &&!arTmp.get(5).equals("ищу работу")){
            ar.add("ищу работу");
        }
        if(!arTmp.get(0).equals("набираю команду") && !arTmp.get(1).equals("набираю команду") &&!arTmp.get(2).equals("набираю команду") &&!arTmp.get(3).equals("набираю команду") &&!arTmp.get(4).equals("набираю команду") &&!arTmp.get(5).equals("набираю команду")){
            ar.add("набираю команду");
        }
        if(!arTmp.get(0).equals("брокер") && !arTmp.get(1).equals("брокер") &&!arTmp.get(2).equals("брокер") &&!arTmp.get(3).equals("брокер") &&!arTmp.get(4).equals("брокер") &&!arTmp.get(5).equals("брокер")){
            ar.add("брокераж, продажа аренда яхт");
        }
        if(!arTmp.get(0).equals("строитель") && !arTmp.get(1).equals("строитель") &&!arTmp.get(2).equals("строитель") &&!arTmp.get(3).equals("строитель") &&!arTmp.get(4).equals("строитель") &&!arTmp.get(5).equals("строитель")){
            ar.add("строю яхты");
        }
        if(!arTmp.get(0).equals("преподаватель") && !arTmp.get(1).equals("преподаватель") &&!arTmp.get(2).equals("преподаватель") &&!arTmp.get(3).equals("преподаватель") &&!arTmp.get(4).equals("преподаватель") &&!arTmp.get(5).equals("преподаватель")){
            ar.add("преподаватель, яхтенная школа");
        }

        return ar;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        }
        else if (requestCode == Crop.REQUEST_CROP) {
            //handleCrop(resultCode, data);
        }

        if (requestCode == REQ_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){

                beginCrop(data.getData());
                //--
                String imagePath;
                imagePath = getFilePath(data);
                File file = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                photo_view.setImageBitmap(myBitmap);
                Main2Activity.myphoto.setImageBitmap(myBitmap);


                //File file = new File(u.getEncodedPath()+".jpg");
                Log.i("Gallery" , imagePath+")");

                Call<ResponseBody> call = api.setPhoto("Bearer " + LoginActivity.stringBuilder.toString(), photo);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            updatePhoto();
                            //Controller.getInstance(getApplicationContext()).update();
                            //MapFragment.newInstance(getApplicationContext()).updateMap();
                            //finish();
                            //startActivity(getIntent());
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
                                Log.i("LOG1" , jObjError.getString("message")+"1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("LOG1" , t.getMessage()+" ");
                    }
                });

            }
        }


        if (requestCode == REQ_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(myFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath(), options);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            try (FileOutputStream out = new FileOutputStream(myFile)) {
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap myBitmap = BitmapFactory.decodeFile(myFile.getAbsolutePath());
            photo_view.setImageBitmap(myBitmap);
            Main2Activity.myphoto.setImageBitmap(myBitmap);

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", myFile.getName(), requestFile);
            Log.i("LOG1" , myFile.getName()+"3");
            //File file = new File(u.getEncodedPath()+".jpg");
            //Log.i("camera" , imagePath+")");

            Call<ResponseBody> call = api.setPhoto("Bearer " + LoginActivity.stringBuilder.toString(), photo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
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
                            Log.i("LOG1" , jObjError.getString("message")+"1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("LOG1" , t.getMessage()+" ");
                }
            });


        }
    }

    private String getFilePath(Intent data) {
        String imagePath;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;

    }

    private Uri generateFileUri() {
        File file = null;
        file = new File(directory.getPath() + "/" + "photo_" + System.currentTimeMillis() + ".jpg");
        Log.i("PHOTO", "fileName = " + file);
        myFile = file;
        return Uri.fromFile(file);
    }

    private void createDirectory() {
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyFolder");
        if (!directory.exists())
            directory.mkdirs();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void beginCrop(Uri source) {
        destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            photo_view.setImageURI(destination);
            Main2Activity.myphoto.setImageURI(destination);

            Bitmap bitmap = photo_view.getDrawingCache();

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fname = "Shutta_"+ timeStamp +".jpg";

            File file = new File(myDir, fname);
            if (file.exists()) file.delete ();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            generateFileUri();
            myFile = new File(destination.getPath());
//            try
//            {
//                FileOutputStream ostream = new FileOutputStream(myFile);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
//                ostream.close();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
            Log.i("LOG1" , file.getAbsolutePath()+"1");
            Log.i("LOG1" , file.getName()+"2");
            photo_view.setImageURI(destination);


            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);


            //Log.i("camera" , imagePath+")");

            Call<ResponseBody> call = api.setPhoto("Bearer " + LoginActivity.stringBuilder.toString(), photo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
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
                            Log.i("LOG1" , jObjError.getString("message")+"1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("LOG1" , t.getMessage()+" ");
                }
            });

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
