package social.tosch.com.social.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.FileWorker;
import social.tosch.com.social.R;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import android.provider.Settings.Secure;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    Intent intent;
    ApiService api;
    private ProgressBar pb;

    //registration1
    private EditText name;
    private EditText mail;
    private CountryCodePicker code;
    private CheckBox law;
    private Button registration;
    Group g;

    private ConstraintLayout layout_reg;


    public static StringBuilder stringBuilder;
    FileWorker fw;
    private ArrayList<String> arr;

    private static String phone_number;
    private static String phone_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }else{
            init();
            buttons();
            checkUserPermission();
            phone_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            Log.i("PHONEd" , phone_id+" ");

            //phone_id = "toschapetrovski@gmail.com";
            //phone_number = "123456789";
            authorization();
        }




        //fw = FileWorker.getInstance();
        //send();








    }

    public void init(){
        api = RetroClient.getApiService();
        pb = findViewById(R.id.progressBar);

        //registration1
        name = findViewById(R.id.reg_name);
        mail = findViewById(R.id.reg_address);
        code = findViewById(R.id.ccp);
        law = findViewById(R.id.checkBox);
        registration = findViewById(R.id.registrationButton);
        g = findViewById(R.id.group);
        layout_reg = findViewById(R.id.reg);


    }

    @Override
    protected void onResume() {
        //pb.setVisibility(View.GONE);
        super.onResume();
    }

    public void buttons(){

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!law.isChecked()) {
                    Toast.makeText(LoginActivity.this, "Подтвердите соглашение", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mail.getText().toString().equals("") && !name.getText().toString().equals("")){
                        sendPost();
                    }
                }


            }
        });


    }

    public void sendPost() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Call<ResponseBody> call = api.registartion("registration", code.getFullNumber()+mail.getText().toString(),phone_id,name.getText().toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            phone_number = mail.getText().toString();
                            authorization();
                            Log.i("create" , "success");
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
                                Log.i("create" , jObjError.getString("message")+"1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("create" , t.getMessage()+" ");
                    }
                });
            }
        });

        thread.start();
    }


    public void authorization() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://yacht.tesord.tk/api/auth");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    JSONObject jo = new JSONObject();



                    jo.put("action", "authorization");
                    jo.put("phone_id", phone_id);

                    Log.i("JSON", jo.toString()+" ");
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jo.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode())+" ");
                    Log.i("MSG" , conn.getResponseMessage()+" ");


                    stringBuilder = new StringBuilder();
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        InputStreamReader streamReader = new InputStreamReader(conn.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);
                        String response = null;
                        while ((response = bufferedReader.readLine()) != null) {
                            stringBuilder.append(response + "\n");
                        }
                        bufferedReader.close();

                        Log.i("GET" , stringBuilder.toString()+" ");
                        stringBuilder.delete(0,10);
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);
                        stringBuilder.deleteCharAt(stringBuilder.length()-1);

                        Log.i("GET" , stringBuilder.toString()+" ");

                        //arr.add(email.getText().toString());
                        //arr.add(password.getText().toString());
                        //fw.writeProfile(arr);

                        intent = new Intent(LoginActivity.this, Main2Activity.class);
                        startActivity(intent);


                    } else {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                pb.setVisibility(View.GONE);
                                layout_reg.setVisibility(View.VISIBLE);

                            }
                        });
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //pb.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        thread.start();
    }


    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 123);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 123);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }



        }
    }

    private void internet(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 12);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    internet();

                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }


}
