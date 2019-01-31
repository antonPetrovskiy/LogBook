package social.tosch.com.social.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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


public class BoatInfoActivity extends AppCompatActivity {
    ApiService api;
    //main info
    private String id;
    private ImageView photo_view;
    private EditText name;
    private Spinner type;
    private EditText loa;
    private EditText year;
    private EditText verf;
    String [] mTempArray = {"Парусное","Моторное"};

    private ImageView back;
    private ImageView save;

    File directory;
    File myFile;
    public static final int REQ_CODE_PICK_PHOTO = 0;
    public static final int REQ_CODE_PICK_PHOTO_GALLERY = 1;
    public static final int REQ_CODE_CAPTURE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_information);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        init();
        createDirectory();
        button();


        update();
    }

    public void init(){
        api = RetroClient.getApiService();
        back = findViewById(R.id.imageView22);
        save = findViewById(R.id.imageView4);
        photo_view = findViewById(R.id.imageProfile);
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        loa = findViewById(R.id.loa);
        year = findViewById(R.id.year);
        verf = findViewById(R.id.verf);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BoatInfoActivity.this, android.R.layout.simple_spinner_item, mTempArray);
        type.setAdapter(adapter);
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



                ad = new AlertDialog.Builder(BoatInfoActivity.this);
                ad.setMessage(message); // сообщение
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Intent intent = new Intent(BoatInfoActivity.this, Main2Activity.class);
                        startActivity(intent);
                    }
                });
                ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Intent intent = new Intent(BoatInfoActivity.this, Main2Activity.class);
                        startActivity(intent);
                    }
                });
                Toast.makeText(getApplicationContext(), "Обновление информации", Toast.LENGTH_SHORT).show();
                editInfo();
                Intent intent = new Intent(BoatInfoActivity.this, Main2Activity.class);
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



                ad = new AlertDialog.Builder(BoatInfoActivity.this);
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
                LayoutInflater layoutInflater = LayoutInflater.from(BoatInfoActivity.this);
                View promptView = layoutInflater.inflate(R.layout.imagechooser, null);
                final AlertDialog alertD = new AlertDialog.Builder(BoatInfoActivity.this).create();

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
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        startActivityForResult(intent,  REQ_CODE_PICK_PHOTO);
                    }
                });

                alertD.setView(promptView);
                alertD.show();
            }
        });

    }

    public void updatePhoto(){
        Call<Boat> call = api.getBoat("Bearer " + LoginActivity.stringBuilder.toString());
        call.enqueue(new Callback<Boat>() {
            @Override
            public void onResponse(Call<Boat> call, Response<Boat> response) {
                if (response.isSuccessful()) {
                    if(response.body().getPhoto()!=null && !response.body().getPhoto().equals("null")) {
                        LinkedTreeMap<String,String> map = (LinkedTreeMap) response.body().getPhoto();
                        Picasso.with(getApplicationContext()).load(map.get("thumb")).placeholder(R.drawable.boat1).error(R.drawable.boat1).into(photo_view);
                    }else{
                        photo_view.setImageResource(R.drawable.boat1);
                    }
                } else {
                    Log.i("LISTERF" , "no success");
                }
            }

            @Override
            public void onFailure(Call<Boat> call, Throwable t) {
                Log.i("LISTERF" , t.getMessage()+" ");
            }
        });
    }

    public void update(){
        Call<Boat> call = api.getBoat("Bearer " + LoginActivity.stringBuilder.toString());
        call.enqueue(new Callback<Boat>() {
            @Override
            public void onResponse(Call<Boat> call, Response<Boat> response) {
                if (response.isSuccessful()) {
                    if(response.body().getType()!=null && response.body().getType().equals("Парусное")){
                        type.setSelection(0);
                    }else{
                        type.setSelection(1);
                    }
                    if(response.body().getBoatName()==null || response.body().getBoatName().equals("null")){
                        name.setText("");
                    }else{
                        name.setText(response.body().getBoatName());
                    }
                    if(response.body().getBoatModel()==null || response.body().getBoatModel().equals("null")){
                        verf.setText("");
                    }else{
                        verf.setText(response.body().getBoatModel());
                    }
                    if(response.body().getBoatYear()==null || response.body().getBoatYear().equals("999")){
                        year.setText("");
                    }else{
                        year.setText(response.body().getBoatYear());
                    }
                    if(response.body().getBoatLoa()==null || response.body().getBoatLoa().equals("999")){
                        loa.setText("");
                    }else{
                        loa.setText(response.body().getBoatLoa());
                    }



                    if(response.body().getPhoto()!=null && !response.body().getPhoto().equals("null")) {
                        LinkedTreeMap<String,String> map = (LinkedTreeMap) response.body().getPhoto();
                        Picasso.with(getApplicationContext()).load(map.get("thumb")).placeholder(R.drawable.boat1).error(R.drawable.boat1).into(photo_view);
                    }else{
                        photo_view.setImageResource(R.drawable.boat1);
                    }
                } else {
                    Log.i("LISTERF" , "no success");
                }
            }

            @Override
            public void onFailure(Call<Boat> call, Throwable t) {
                Log.i("LISTERF" , t.getMessage()+" ");
            }
        });
    }

    public void editInfo(){
        JSONObject jsonObject = new JSONObject();
        try {
            if (!year.getText().toString().equals("")) {
                jsonObject.put("year", year.getText().toString());
            } else {
                jsonObject.put("year", "999");
            }
            if (!verf.getText().toString().equals("")){
                jsonObject.put("model", verf.getText().toString());
            }else{
                jsonObject.put("model", "null");
            }
            if(!name.getText().toString().equals("")) {
                jsonObject.put("name", name.getText().toString());
            }else{
                jsonObject.put("name", "null");
            }
            if(!loa.getText().toString().equals("")) {
                jsonObject.put("loa", loa.getText().toString());
            }else{
                jsonObject.put("loa", "999");
            }
            if(!type.getSelectedItem().toString().equals("")) {
                jsonObject.put("boat_type", type.getSelectedItem().toString());
            }else{
                jsonObject.put("boat_type", "null");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());



        Call<ResponseBody> call = api.setBoat("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("COORD" , "success");
                    //Controller.getInstance(getApplicationContext()).setContactList();
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
                        Log.i("BOATINFO" , jObjError.getString("message")+"1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("COORD" , t.getMessage()+" ");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_PHOTO && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                String imagePath;
                imagePath = getFilePath(data);
                File file = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                //photo_view.setImageBitmap(myBitmap);
                Controller.getInstance(getApplicationContext()).setContactList();
                MapFragment.newInstance(this).updateMap();

                //File file = new File(u.getEncodedPath()+".jpg");
                Log.i("Gallery" , imagePath+")");

                Call<ResponseBody> call = api.setPhotoBoat("Bearer " + LoginActivity.stringBuilder.toString(), photo);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            updatePhoto();

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

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), myFile);
            MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", myFile.getName(), requestFile);

            //File file = new File(u.getEncodedPath()+".jpg");
            //Log.i("camera" , imagePath+")");

            Call<ResponseBody> call = api.setPhotoBoat("Bearer " + LoginActivity.stringBuilder.toString(), photo);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        updatePhoto();
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

}
