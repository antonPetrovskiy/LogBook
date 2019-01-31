package social.tosch.com.social.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.R;
import social.tosch.com.social.activities.BoatInfoActivity;
import social.tosch.com.social.activities.DialogActivity;
import social.tosch.com.social.activities.InformationActivity;
import social.tosch.com.social.activities.LoginActivity;
import social.tosch.com.social.activities.Main2Activity;
import social.tosch.com.social.adapter.ContactAdapter;
import social.tosch.com.social.adapter.DialogAdapter;
import social.tosch.com.social.adapter.PopupAdapter;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.entity.Contact;
import social.tosch.com.social.entity.ContactList;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Friend;
import social.tosch.com.social.entity.FriendList;
import social.tosch.com.social.entity.InfoWindowData;
import social.tosch.com.social.entity.Photo;
import social.tosch.com.social.entity.PhotoList;

import static android.content.ContentValues.TAG;


public class MapFragment extends Fragment {
    static Context context;
    static MapView mMapView;
    private View rootView;
    ApiService api;
    String id;

    boolean isGalleryUpdating;

    private EditText search_field;
    private boolean isFindPressed = false;
    private boolean isRadiusPressed = false;
    private boolean isSettingCoord = false;

    String[] data = {"капитан", "команда", "работаю", "использую чартер", "спортсмен", "сервис", "ищу работу", "ищу команду", "брокер", "строитель", "преподаватель", "яхтенный агент"};
    private TextView myname;
    private ImageView myphoto;
    public static final int REQ_CODE_PICK_PHOTO_GALLERY = 1;
    private boolean isStar;
    private ImageView star;
    private ImageView sms;
    private ImageView category;
    private ImageView profile;
    private ImageView showMe;
    private ImageView edit;
    private ImageView exit;
    private ImageView save;
    private ImageView menu;
    private ImageView find;
    private ImageView plusPhoto;
    private ImageView boat;
    private ImageView bg_dark;
    private boolean isGPS = false;
    private boolean isWrite = false;
    private boolean isHand = false;
    LinearLayout lay_save;

    ImageView marker;
    SeekBar sb;
    //private ProgressBar pb;
    private boolean isEdit;
    private LatLng me;
    double myX;
    double myY;
    private static GoogleMap googleMap;
    private ArrayList<Contact> contactList;
    private ArrayList<Contact> tmpContactList;
    private ArrayList<Friend> friendsList;
    LinearLayout gs;

    //categories
    Animation show_right;
    Animation hide_right;
    boolean isCategories;
    private LinearLayout categories_lay;
    TextView tw1;
    TextView tw2;
    TextView tw3;
    TextView tw4;
    TextView tw5;
    TextView tw6;
    TextView tw7;
    TextView tw8;
    TextView tw9;
    TextView tw10;
    TextView tw11;

    //profile
    Animation show;
    Animation hide;
    boolean isProfile;
    boolean isBoat = false;
    public ArrayList<Photo> gallery;
    List<String> tags;
    List<int[]> colors;
    private RelativeLayout profile_lay;
    private ImageView photo;
    private TextView name;
    private TextView country;
    private TextView experience;
    private TagContainerLayout tag;


    public MapFragment() {
    }

    public static MapFragment newInstance(Context c) {
        MapFragment fragment = new MapFragment();
        context = c;
        return fragment;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
        buttons();
        mMapView.onCreate(savedInstanceState);
        getFriends();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                initMap();

            }
        });



        //updateMap();


        return rootView;
    }

    @Override
    public void onResume() {
        //updateMap();

        super.onResume();
    }

    public void init(){
        api = RetroClient.getApiService();
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        search_field = rootView.findViewById(R.id.editText);
        showMe = rootView.findViewById(R.id.imageView14);
        category = rootView.findViewById(R.id.imageView31);
        profile = rootView.findViewById(R.id.imageView35);
        edit = rootView.findViewById(R.id.imageView17);
        sms = rootView.findViewById(R.id.imageView18);
        star = rootView.findViewById(R.id.imageView19);
        marker = rootView.findViewById(R.id.imageView16);
        plusPhoto = rootView.findViewById(R.id.imageView20);
        boat = rootView.findViewById(R.id.imageView21);
        find = rootView.findViewById(R.id.imageView1);
        bg_dark = rootView.findViewById(R.id.bg_dark);
        tmpContactList = new ArrayList<>();

        //pb = rootView.findViewById(R.id.progressBar1);
        lay_save = rootView.findViewById(R.id.laysave);
        exit = rootView.findViewById(R.id.imageView10);
        save = rootView.findViewById(R.id.imageView11);
        menu = rootView.findViewById(R.id.imageView15);
        gs = rootView.findViewById(R.id.googlesearch);

        sb = rootView.findViewById(R.id.seekBar);
        myphoto = rootView.findViewById(R.id.myphoto);
        myname = rootView.findViewById(R.id.myname);
        show = AnimationUtils.loadAnimation(context, R.anim.show_top);
        hide = AnimationUtils.loadAnimation(context, R.anim.hide_top);
        colors = new ArrayList<int[]>();
        tags = new ArrayList<>();
        profile_lay = rootView.findViewById(R.id.profile);
        photo = rootView.findViewById(R.id.imageView);
        name = rootView.findViewById(R.id.textViewName);
        country = rootView.findViewById(R.id.textViewCountry);
        experience = rootView.findViewById(R.id.textViewExperience);
        tag = rootView.findViewById(R.id.tag);

        tw1 = rootView.findViewById(R.id.textView91);
        tw2 = rootView.findViewById(R.id.textView92);
        tw3 = rootView.findViewById(R.id.textView93);
        tw4 = rootView.findViewById(R.id.textView94);
        tw5 = rootView.findViewById(R.id.textView95);
        tw6 = rootView.findViewById(R.id.textView96);
        tw7 = rootView.findViewById(R.id.textView97);
        tw8 = rootView.findViewById(R.id.textView98);
        tw9 = rootView.findViewById(R.id.textView99);
        tw10 = rootView.findViewById(R.id.textView100);
        tw11 = rootView.findViewById(R.id.textView101);
        show_right = AnimationUtils.loadAnimation(context, R.anim.show_right_menu);
        hide_right = AnimationUtils.loadAnimation(context, R.anim.hide_right_menu);
        categories_lay = rootView.findViewById(R.id.categories_lay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public void buttons(){
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main2Activity.drawer.openDrawer(Gravity.START);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCategories){
                    categories_lay.startAnimation(hide_right);
                    categories_lay.setVisibility(View.GONE);
                    bg_dark.setVisibility(View.GONE);
                    isCategories = false;
                }else{
                    categories_lay.startAnimation(show_right);
                    categories_lay.setVisibility(View.VISIBLE);
                    bg_dark.setVisibility(View.VISIBLE);
                    isCategories = true;
                }
            }
        });

        tw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawUsers(contactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });
        tw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("брокер");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });
        tw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("ищу работу");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });
        tw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("ищу команду");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });
        tw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("строитель");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });
        tw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("преподаватель");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });
        tw7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("сервис");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });
        tw8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("друзья");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });

        tw9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("спортсмен");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });

        tw10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("капитан");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });

        tw11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort("команда");
                drawUsers(tmpContactList);
                categories_lay.startAnimation(hide_right);
                categories_lay.setVisibility(View.GONE);
                bg_dark.setVisibility(View.GONE);
                isCategories = false;
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main2Activity.mViewPager.setCurrentItem(4);
            }
        });


        showMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contactList!=null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myX, myY), 9));
                    setProfile(Main2Activity.id);
                    if (isCategories) {
                        categories_lay.startAnimation(hide_right);
                        isCategories = false;
                        categories_lay.setVisibility(View.GONE);
                        bg_dark.setVisibility(View.GONE);
                    }
                }
            }
        });

        boat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBoat){
                    boat.setImageResource(R.drawable.boat);
                    setProfile(id);
                    isBoat = false;
                }else{
                    boat.setImageResource(R.drawable.boat_used);
                    setBoat(id);
                    isBoat = true;
                }

            }
        });

        plusPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.layout_dialog_gallery, null);
                final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();
                alertD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertD.getWindow().setDimAmount(0.9f);
                final GridLayout grid = promptView.findViewById(R.id.grid);
                final ImageView exit = promptView.findViewById(R.id.imageView);

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertD.cancel();
                    }
                });
                int n;
                if(gallery==null) {
                    n = 0;
                }else{
                    n = gallery.size();
                }
                for(int i = -1; i < n; i++){
                    final int number = i;
                    final LinkedTreeMap<String, String> map;
                    if(gallery==null){
                        map = new LinkedTreeMap<>();
                    }else if (gallery.size()>Math.abs(i)){
                        map = (LinkedTreeMap) gallery.get(Math.abs(i)).getPhoto();
                    }else{
                        map = new LinkedTreeMap<>();
                    }


                    final ImageView image = new ImageView(getActivity());
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    final GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();

                    ViewTreeObserver vto = grid.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                grid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                grid.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            int width = grid.getMeasuredWidth()/3;
                            int height = width;

                            layoutParams.width = width;
                            layoutParams.height = height;

                            image.setLayoutParams(layoutParams);
                            if(number==-1 && id.equals(Main2Activity.id)){
                                image.setImageResource(R.drawable.plus_photo);
                                image.setCropToPadding(true);
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.setPadding(10,10,10,10);
                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                intent.setType("image/*");
                                                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                                                startActivityForResult(intent,  REQ_CODE_PICK_PHOTO_GALLERY);
                                        profile_lay.startAnimation(hide);
                                        isProfile=false;
                                        isGalleryUpdating = true;
                                                alertD.cancel();

                                    }
                                });
                            }else if(number!=-1){
                                Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(image);
                                image.setCropToPadding(true);
                                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                image.setPadding(10,10,10,10);
                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                                        View promptView = layoutInflater.inflate(R.layout.layout_dialog_image, null);
                                        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();
                                        alertD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        alertD.getWindow().setDimAmount(0.9f);
                                        ImageView btnAdd1 = promptView.findViewById(R.id.imageView5);
                                        final ImageView exit = promptView.findViewById(R.id.imageView);

                                        exit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                alertD.cancel();
                                            }
                                        });
                                        Picasso.with(context).load(map.get("origin")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(btnAdd1);
                                        //btnAdd1.setImageDrawable(image.getDrawable());
                                        alertD.setView(promptView);
                                        alertD.show();
                                    }
                                });
                                image.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        String title = "Удалить фото?";
                                        String button1String = "Да";
                                        String button2String = "Нет";
                                        AlertDialog.Builder ad;
                                        ad = new AlertDialog.Builder(getActivity());
                                        ad.setTitle(title);  // заголовок
                                        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int arg1) {
                                                if (((ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                                                    JSONObject jsonObject = new JSONObject();
                                                    try {
                                                        JSONArray arr = new JSONArray();
                                                        arr.put(gallery.get(number).getId());
                                                        jsonObject.put("ids", arr);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    JsonParser jsonParser = new JsonParser();
                                                    JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());

                                                    Call<ResponseBody> call = api.deleteGallery("Bearer " + LoginActivity.stringBuilder.toString(),gsonObject);
                                                    call.enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            if (response.isSuccessful()) {
                                                                Toast toast = Toast.makeText(getContext(),
                                                                        "Фото удалено", Toast.LENGTH_SHORT);
                                                                toast.show();
                                                                alertD.cancel();
                                                                setProfile(Main2Activity.id);
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
                                                                    Log.i("DLTG" , jObjError.getString("message")+" ");
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                            Log.i("DLTG" , t.getMessage()+" ");
                                                        }
                                                    });

                                                } else {
                                                    Log.i("LIST" , "no internet");
                                                }
                                            }
                                        });
                                        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int arg1) {

                                            }
                                        });
                                        ad.show();


                                        return false;
                                    }
                                });
                            }


                            grid.addView(image,number);

                        }
                    });

                }
                alertD.setView(promptView);
                alertD.show();
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pb.setVisibility(View.VISIBLE);
                if(!isStar){
                    if (((ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                        Call<ResponseBody> call = api.setFriends("Bearer " + LoginActivity.stringBuilder.toString(),id);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast toast = Toast.makeText(getContext(),
                                            "Пользователь добавлен", Toast.LENGTH_SHORT);
                                    toast.show();
                                    isStar = true;
                                    star.setImageResource(R.drawable.star_used);
                                    //pb.setVisibility(View.GONE);
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
                                        Log.i("ADDO" , jObjError.getString("message")+"1");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.i("LISTERF" , t.getMessage()+" ");
                            }
                        });

                    } else {
                        Log.i("LIST" , "no internet");
                    }
                }else {
                    String tmpId = "";
                    for(int i = 0; i < friendsList.size(); i ++){
                        if(friendsList.get(i).getUser_id().equals(id))
                            tmpId = friendsList.get(i).getInvitation_id();
                    }
                    if (((ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
                        for(int i = 0; i < friendsList.size(); i ++){
                            if(friendsList.get(i).getUser_id().equals(id))
                                tmpId = friendsList.get(i).getInvitation_id();
                        }
                        Call<ResponseBody> call = api.deleteFriends("Bearer " + LoginActivity.stringBuilder.toString(),tmpId);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    Toast toast = Toast.makeText(getContext(),
                                            "Пользователь удален", Toast.LENGTH_SHORT);
                                    toast.show();
                                    star.setImageResource(R.drawable.star);
                                    isStar=false;
                                    //pb.setVisibility(View.GONE);
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
                                        Log.i("ADDO" , jObjError.getString("message")+"2");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.i("LISTERF" , t.getMessage()+" ");
                            }
                        });

                    } else {
                        Log.i("LIST" , "no internet");
                    }
                }
                getFriends();
            }
        });


        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCategories) {
                    categories_lay.startAnimation(hide_right);
                    isCategories = false;
                    categories_lay.setVisibility(View.GONE);
                    bg_dark.setVisibility(View.GONE);
                }
                gs.setVisibility(View.GONE);
                if(isHand){
                    return;
                }

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.layout_dialog_setcoord, null);
                final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();
                ImageView btnAdd1 = promptView.findViewById(R.id.imageView1);
                ImageView btnAdd2 = promptView.findViewById(R.id.imageView2);
                ImageView btnAdd3 = promptView.findViewById(R.id.imageView3);

                btnAdd1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertD.cancel();
                        googleMap.clear();
                        isHand = true;
                        lay_save.setVisibility(View.VISIBLE);
                        edit.setImageResource(R.drawable.editplaceholder_used);
                        marker.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(context, "Укажите местоположение яхты", Toast.LENGTH_LONG);
                        toast.show();
                        //isSettingCoord = true;

                    }
                });

                btnAdd2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertD.cancel();
                        googleMap.clear();
                        gs.setVisibility(View.VISIBLE);
                        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


                        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                            @Override
                            public void onPlaceSelected(Place place) {
                                //googleMap.clear();
                                googleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 5.0f));
                            }

                            @Override
                            public void onError(Status status) {

                            }

                        });

                    }
                });

                btnAdd3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(context, "В разработке", Toast.LENGTH_LONG);
                        toast.show();
//                        alertD.cancel();
//                        googleMap.clear();
//                        if (googleMap == null) {
//                            // Try to obtain the map from the SupportMapFragment.
//                            googleMap.setMyLocationEnabled(true);
//                            // Check if we were successful in obtaining the map.
//                            if (googleMap != null) {
//                                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//                                    @Override
//                                    public void onMyLocationChange(Location arg0) {
//                                        // TODO Auto-generated method stub
//                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
//                                    }
//                                });
//
//                            }
//                        }
                    }
                });
                alertD.setView(promptView);
                alertD.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setImageResource(R.drawable.editplaceholder);
                lay_save.setVisibility(View.GONE);

                if(isHand){
                    marker.setVisibility(View.GONE);
                    updateLocation(me.latitude,me.longitude);
                    askEditInfo();
                }
                if(isGPS){
                    marker.setVisibility(View.GONE);
                    updateLocation(me.latitude,me.longitude);
                    askEditInfo();
                }
                if(isWrite){
                    marker.setVisibility(View.GONE);
                    updateLocation(me.latitude,me.longitude);
                    gs.setVisibility(View.GONE);
                    askEditInfo();
                }


                isHand = false;
                isGPS = false;
                isWrite = false;
                googleMap.clear();
                Toast toast = Toast.makeText(context,
                        "Местоположение изменено", Toast.LENGTH_LONG);
                toast.show();

                updateMap();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setImageResource(R.drawable.editplaceholder);
                lay_save.setVisibility(View.GONE);
                marker.setVisibility(View.GONE);
                isHand = false;
                isGPS = false;
                isWrite = false;
                googleMap.clear();

                updateMap();


            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //findUser(search_field.getText().toString());

                        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                        View promptView = layoutInflater.inflate(R.layout.layout_find_friends, null);
                        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();
                        final ListView listView = promptView.findViewById(R.id.list);
                        final ArrayList<Contact> tmp = new ArrayList<>();
                        final ImageView btn = promptView.findViewById(R.id.imageView25);
                        final LinearLayout lay = promptView.findViewById(R.id.categories);

                        final TextView all = promptView.findViewById(R.id.textView91);
                        final TextView broker = promptView.findViewById(R.id.textView92);
                        final TextView findwork = promptView.findViewById(R.id.textView93);
                        final TextView findteam = promptView.findViewById(R.id.textView94);
                        final TextView builder = promptView.findViewById(R.id.textView95);
                        final TextView prepod = promptView.findViewById(R.id.textView96);
                        final TextView service = promptView.findViewById(R.id.textView97);
                        final TextView sport = promptView.findViewById(R.id.textView98);
                        final TextView friend = promptView.findViewById(R.id.textView99);
                        final TextView cap = promptView.findViewById(R.id.textView100);
                        final TextView team = promptView.findViewById(R.id.textView101);
                        final float density = getResources().getDisplayMetrics().density;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.all_icon);
                        Drawable drawable2 = getResources().getDrawable(R.drawable.broker_icon);
                        Drawable drawable3 = getResources().getDrawable(R.drawable.findwork_icon);
                        Drawable drawable4 = getResources().getDrawable(R.drawable.findteam_icon);
                        Drawable drawable5 = getResources().getDrawable(R.drawable.worker_icon);
                        Drawable drawable6 = getResources().getDrawable(R.drawable.prepod_icon);
                        Drawable drawable7 = getResources().getDrawable(R.drawable.service_icon);
                        Drawable drawable8 = getResources().getDrawable(R.drawable.sport_icon);
                        Drawable drawable9 = getResources().getDrawable(R.drawable.friends_icon);
                        Drawable drawable10 = getResources().getDrawable(R.drawable.cap_icon);
                        Drawable drawable11 = getResources().getDrawable(R.drawable.boat_menu);

                        final int width = Math.round(24 * density);
                        final int height = Math.round(24 * density);
                        drawable1.setBounds(0, 0, width, height);
                        drawable2.setBounds(0, 0, width, height);
                        drawable3.setBounds(0, 0, width, height);
                        drawable4.setBounds(0, 0, width, height);
                        drawable5.setBounds(0, 0, width, height);
                        drawable6.setBounds(0, 0, width, height);
                        drawable7.setBounds(0, 0, width, height);
                        drawable8.setBounds(0, 0, width, height);
                        drawable9.setBounds(0, 0, width, height);
                        drawable10.setBounds(0, 0, width, height);
                        drawable11.setBounds(0, 0, width, height);

                        all.setCompoundDrawables(drawable1, null, null, null);
                        broker.setCompoundDrawables(drawable2, null, null, null);
                        findwork.setCompoundDrawables(drawable3, null, null, null);
                        findteam.setCompoundDrawables(drawable4, null, null, null);
                        builder.setCompoundDrawables(drawable5, null, null, null);
                        prepod.setCompoundDrawables(drawable6, null, null, null);
                        service.setCompoundDrawables(drawable7, null, null, null);
                        sport.setCompoundDrawables(drawable8, null, null, null);
                        friend.setCompoundDrawables(drawable9, null, null, null);
                        cap.setCompoundDrawables(drawable10, null, null, null);
                        team.setCompoundDrawables(drawable11, null, null, null);

                        EditText editText = promptView.findViewById(R.id.editText);
                        ImageView exit = promptView.findViewById(R.id.imageView9);
                        exit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertD.hide();
                            }
                        });
                        editText.addTextChangedListener(new TextWatcher() {


                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                tmp.clear();
                                for(int j = 0; j < contactList.size(); j ++){
                                    if(contactList.get(j).getName().toLowerCase().contains(s.toString().toLowerCase())){
                                        tmp.add(contactList.get(j));
                                    }
                                }
                                ContactAdapter adapter = new ContactAdapter(context, tmp);
                                listView.setAdapter(adapter);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }

                        });


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if(tmp.size()==0){
                                    alertD.cancel();
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(contactList.get(i).getLocation().get("lat")), Double.parseDouble(contactList.get(i).getLocation().get("lon"))), 6));
                                    setProfile(contactList.get(i).getId());
                                }else{
                                    alertD.cancel();
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(tmp.get(i).getLocation().get("lat")), Double.parseDouble(tmp.get(i).getLocation().get("lon"))), 6));
                                    setProfile(tmp.get(i).getId());
                                }
                            }
                        });

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(lay.getVisibility() == View.VISIBLE) {
                                    lay.setVisibility(View.GONE);
                                }else{
                                    lay.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        all.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ContactAdapter adapter = new ContactAdapter(context, contactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });

                        broker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("брокер");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });

                        findwork.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("щу работу");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });

                        findteam.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("щу команду");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });
                        builder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("строитель");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });
                        prepod.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("преподаватель");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });
                        service.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("сервис");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });
                        sport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("спортсмен");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });
                        friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("друзья");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });
                        cap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("капитан");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });
                        team.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sort("команда");
                                ContactAdapter adapter = new ContactAdapter(context, tmpContactList);
                                listView.setAdapter(adapter);
                                lay.setVisibility(View.GONE);
                            }
                        });

                        ContactAdapter adapter;
                        adapter = new ContactAdapter(context, contactList);
                        listView.setAdapter(adapter);
                listView.setSelectionAfterHeaderView();
                        alertD.setView(promptView);
                        alertD.show();

            }
        });


        search_field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                return false;
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, ((0.1f)*i)+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initMap(){
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if(googleMap!=null){
                    googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            //get latlng at the center by calling
                            if(isHand){
                                googleMap.clear();
                                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_boat);
                                Bitmap b=bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                                LatLng midLatLng = googleMap.getCameraPosition().target;
                                //googleMap.addMarker(new MarkerOptions().position(midLatLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                                me=midLatLng;
                            }
                        }
                    });
                    googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater(),context));
                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            if(isCategories) {
                                categories_lay.startAnimation(hide_right);
                                isCategories = false;
                                categories_lay.setVisibility(View.GONE);
                                bg_dark.setVisibility(View.GONE);
                            }

                            InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
                            setProfile(infoWindowData.getId());
                        }
                    });

                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            profile_lay.setVisibility(View.GONE);
                            gs.setVisibility(View.GONE);
                            star.setVisibility(View.INVISIBLE);
                            sms.setVisibility(View.GONE);
                            showMe.setVisibility(View.VISIBLE);
                            edit.setVisibility(View.VISIBLE);
                            category.setVisibility(View.VISIBLE);
                            if(isProfile)
                                profile_lay.startAnimation(hide);
                            isProfile=false;
                            if(isCategories) {
                                categories_lay.startAnimation(hide_right);
                                isCategories = false;
                                categories_lay.setVisibility(View.GONE);
                                bg_dark.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                try {
                    updateMap();

                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }


            }

        });


    }

    public void updateUsers(){
        Activity activity = getActivity();
        if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null && activity != null) {


            JSONObject jsonObject = new JSONObject();
            try {
                JSONObject subJsonObject = new JSONObject();
                subJsonObject.put("info", "1");
                subJsonObject.put("boat", "1");
                subJsonObject.put("categories", "1");
                jsonObject.put("params", subJsonObject);
                jsonObject.put("action", "list");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());

            Call<ContactList> call = api.getUserList("Bearer " + LoginActivity.stringBuilder.toString(), gsonObject);
            call.enqueue(new Callback<ContactList>() {
                @Override
                public void onResponse(Call<ContactList> call, Response<ContactList> response) {
                    if (response.isSuccessful()) {
                        contactList = response.body().getContacts();

                        if(googleMap!=null)
                            googleMap.clear();
                        if(Main2Activity.chatId!=null){
                            setProfile(Main2Activity.chatId);
                            Main2Activity.chatId = null;
                        }
                        for(int i = 0 ; i < contactList.size(); i++){
                            if(contactList.get(i).getName()!=null && contactList.get(i).getName().equals(Main2Activity.myname.getText().toString())){
                                myX = Double.valueOf(contactList.get(i).getLocation().get("lat"));
                                myY = Double.valueOf(contactList.get(i).getLocation().get("lon"));
                                if(myX==0 && myY ==0){
                                    edit.performClick();
                                }
                                //Main2Activity.myname.setText(Main2Activity.myname.getText());
                                //Main2Activity.myphoto.setImageDrawable(Main2Activity.myphoto.getDrawable());
                            }
                            if(contactList.get(i).getLocation().get("lon")!=null && getActivity()!=null){
                                BitmapDrawable bitmapdraw=(BitmapDrawable)getActivity().getApplicationContext().getResources().getDrawable(R.drawable.point_boat);
                                Bitmap b=bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

                                BitmapDrawable bitmapdrawMe=(BitmapDrawable)getActivity().getApplicationContext().getResources().getDrawable(R.drawable.point_boat_me);
                                Bitmap bMe=bitmapdrawMe.getBitmap();
                                Bitmap smallMarkerMe = Bitmap.createScaledBitmap(bMe, 50, 50, false);

                                InfoWindowData info = new InfoWindowData();
                                info.setImage(contactList.get(i).getPhoto());
                                info.setId(contactList.get(i).getId());
                                info.setName(contactList.get(i).getName());



                                if(contactList.get(i).getCategories()!=null){
                                    for(int j = 0; j < contactList.get(i).getCategories().size(); j ++){
                                        if(contactList.get(i).getCategories().get(j).getTitle().equals("сервис")){
                                            //bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_service);
                                            //b=bitmapdraw.getBitmap();
                                            //smallMarker = Bitmap.createScaledBitmap(b, 50, 70, false);
                                        }

                                    }
                                }

                                if(contactList.get(i).getId().equals(Main2Activity.id)){
                                    String s = contactList.get(i).getName();
                                    String s1 = s.replace(" ","\n");
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.valueOf(contactList.get(i).getLocation().get("lat")), Double.valueOf(contactList.get(i).getLocation().get("lon"))))
                                            .title(s1).snippet("id"+contactList.get(i).getId()).icon(BitmapDescriptorFactory.fromBitmap(smallMarkerMe))).setTag(info);
                                }else{
                                    String s = contactList.get(i).getName();
                                    String s1 = s.replace(" ","\n");
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.valueOf(contactList.get(i).getLocation().get("lat")), Double.valueOf(contactList.get(i).getLocation().get("lon"))))
                                            .title(s1).snippet("id"+contactList.get(i).getId()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))).setTag(info);
                                }



                            }
                        }

                    } else {
                        Log.i("MAP" , "no success");
                    }
                }

                @Override
                public void onFailure(Call<ContactList> call, Throwable t) {
                    Log.i("LIST" , t.getMessage()+ " ");
                }
            });

        } else {
            Log.i("LIST" , "no internet");
        }
    }

    public void drawUsers(ArrayList<Contact> list){
        googleMap.clear();
        for(int i = 0 ; i < list.size(); i++){
            if(list.get(i).getName()!=null && list.get(i).getName().equals(Main2Activity.myname.getText().toString())){
                myX = Double.valueOf(contactList.get(i).getLocation().get("lat"));
                myY = Double.valueOf(contactList.get(i).getLocation().get("lon"));
            }
            if(list.get(i).getLocation().get("lon")!=null){
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_boat);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                BitmapDrawable bitmapdrawMe=(BitmapDrawable)getResources().getDrawable(R.drawable.point_boat_me);
                Bitmap bMe=bitmapdrawMe.getBitmap();
                Bitmap smallMarkerMe = Bitmap.createScaledBitmap(bMe, 50, 50, false);
                InfoWindowData info = new InfoWindowData();
                info.setImage(list.get(i).getPhoto());
                info.setId(list.get(i).getId());
                info.setName(list.get(i).getName());
                if(list.get(i).getCategories()!=null){
                    for(int j = 0; j < list.get(i).getCategories().size(); j ++){
                        if(list.get(i).getCategories().get(j).getTitle().equals("сервис")){
//                            bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.point_service);
//                            b=bitmapdraw.getBitmap();
//                            smallMarker = Bitmap.createScaledBitmap(b, 50, 70, false);
                        }

                    }
                }
                if(list.get(i).getId().equals(Main2Activity.id)){
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.valueOf(list.get(i).getLocation().get("lat")), Double.valueOf(list.get(i).getLocation().get("lon"))))
                            .title(list.get(i).getName()).snippet("id"+list.get(i).getId()).icon(BitmapDescriptorFactory.fromBitmap(smallMarkerMe))).setTag(info);
                }else{
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.valueOf(list.get(i).getLocation().get("lat")), Double.valueOf(list.get(i).getLocation().get("lon"))))
                            .title(list.get(i).getName()).snippet("id"+list.get(i).getId()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))).setTag(info);
                }



            }
        }
    }

    public void updateLocation(double arg0, double arg1){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("lat", arg0+"");
            subJsonObject.put("lon", arg1+"");
            jsonObject.put("location", subJsonObject);
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

    public void updateMap(){
//                if(googleMap!=null)
//                    googleMap.clear();

                updateUsers();

    }

    public void setProfile(String id){
        this.id = id;
        isProfile=true;

        gallery = new ArrayList<>();
        tags = new ArrayList<>();
        if(tag!=null)
            tag.removeAllTags();

        int n=0;
        final int position;
        for(int i = 0; i < contactList.size(); i ++){
            if(contactList.get(i).getId().equals(id))
                n = i;
        }
        position = n;

        if(contactList.get(position).getBoatName()==null || contactList.get(position).getBoatPhoto()==null || contactList.get(position).getBoatName().equals("null") || contactList.get(position).getBoatPhoto().equals("null")) {
            boat.setVisibility(View.GONE);
        }else{
            boat.setVisibility(View.VISIBLE);
        }
        category.setVisibility(View.GONE);
        showMe.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        plusPhoto.setVisibility(View.VISIBLE);

        isBoat = false;
        profile_lay.startAnimation(show);
        profile_lay.setVisibility(View.VISIBLE);
        if(contactList.get(position).getName().equals(Main2Activity.myname.getText().toString())){
            star.setVisibility(View.INVISIBLE);
            sms.setVisibility(View.GONE);
        }else{
            if(friendsList!=null) {
                isStar = false;
                star.setImageResource(R.drawable.star);
                for(int i = 0; i < friendsList.size(); i++){
                    if(friendsList.get(i).getUser_id().equals(id)){
                        isStar = true;
                        star.setImageResource(R.drawable.star_used);
                    }
                }
            }else{
                isStar = false;
                star.setImageResource(R.drawable.star);
            }
            star.setVisibility(View.VISIBLE);
            sms.setVisibility(View.VISIBLE);
        }

        if(contactList.get(n).getBoatPhoto()!=null && !contactList.get(n).getBoatPhoto().equals("null")) {
            LinkedTreeMap<String,String> map = (LinkedTreeMap) contactList.get(n).getBoatPhoto();
            Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.boat1).error(R.drawable.boat1).into(boat);
            boat.setPadding(11,5,11,17);
        }else{
            boat.setImageResource(R.drawable.boat);
            boat.setPadding(0,0,0,0);
        }


        //set photo
        if(contactList.get(n).getPhoto()!=null && !contactList.get(n).getPhoto().equals("null")) {
            LinkedTreeMap<String,String> map = (LinkedTreeMap) contactList.get(n).getPhoto();
            Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(photo);
        }else{
            photo.setImageResource(R.drawable.photo1);
        }

        //set name
        name.setText(contactList.get(n).getName());

        //set country
        if(contactList.get(n).getCountry()!=null && !contactList.get(n).getCountry().equals("")){
            country.setText(""+contactList.get(n).getCountry());
        }else{
            country.setText("");
        }

        //set experience
        if(contactList.get(n).getAge()!=null && !contactList.get(n).getAge().equals("")){
            experience.setText("Стаж с "+contactList.get(n).getAge());
        }else{
            experience.setText("");
        }


        //set tags
        if(contactList.get(n).getCategories()!=null){
            for(int i = 0; i < contactList.get(n).getCategories().size(); i ++) {
                int[] color1 = {Color.parseColor("#096aa1"), Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
                colors.add(color1);
                tags.add(contactList.get(n).getCategories().get(i).getTitle());

            }
            tag.setTags(tags);
        }

        //set gallery
        if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            Call<PhotoList> call = api.getGalleryById("Bearer " + LoginActivity.stringBuilder.toString(),id);

            call.enqueue(new Callback<PhotoList>() {
                @Override
                public void onResponse(Call<PhotoList> call, Response<PhotoList> response) {
                    if (response.isSuccessful()) {
                        gallery = response.body().getGallery();
                        if((gallery==null || gallery.size()==0) && !contactList.get(position).getName().equals(Main2Activity.myname.getText().toString()))
                            plusPhoto.setVisibility(View.GONE);
                        if(isGalleryUpdating){
                            Toast toast = Toast.makeText(context,
                                    "Фото загружено", Toast.LENGTH_SHORT);
                            toast.show();
                            isGalleryUpdating = false;
                        }
                        Log.i("galery1", response.body().toString()+" ");
                    } else {
                        Log.i("galery1", response.toString()+" ");
                    }
                }

                @Override
                public void onFailure(Call<PhotoList> call, Throwable t) {
                    Log.i("galery1", "fail");
                }
            });
        }




    }

    public void setBoat(String id){
        this.id = id;
        isProfile=true;

        gallery = new ArrayList<>();
        tags = new ArrayList<>();
        tag.removeAllTags();

        int n=0;
        final int position;
        for(int i = 0; i < contactList.size(); i ++){
            if(contactList.get(i).getId().equals(id))
                n = i;
        }
        position = n;

        category.setVisibility(View.GONE);
        showMe.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);

        profile_lay.startAnimation(show);
        profile_lay.setVisibility(View.VISIBLE);
        if(contactList.get(position).getName().equals(Main2Activity.myname.getText().toString())){
            star.setVisibility(View.INVISIBLE);
            sms.setVisibility(View.GONE);
        }else{
            if(friendsList!=null) {
                isStar = false;
                star.setImageResource(R.drawable.star);
                for(int i = 0; i < friendsList.size(); i++){
                    if(friendsList.get(i).getUser_id().equals(id)){
                        isStar = true;
                        star.setImageResource(R.drawable.star_used);
                    }
                }
            }else{
                isStar = false;
                star.setImageResource(R.drawable.star);
            }
            star.setVisibility(View.VISIBLE);
            sms.setVisibility(View.VISIBLE);
        }

        if(contactList.get(n).getPhoto()!=null && !contactList.get(n).getPhoto().equals("null")) {
            LinkedTreeMap<String,String> map = (LinkedTreeMap) contactList.get(n).getPhoto();
            Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(boat);
            boat.setPadding(11,5,11,17);
        }else{
            boat.setImageResource(R.drawable.boat_used);
            boat.setPadding(0,0,0,0);
        }

        //set photo
        if(contactList.get(n).getBoatPhoto()!=null && !contactList.get(n).getBoatPhoto().equals("null")) {
            LinkedTreeMap<String,String> map = (LinkedTreeMap) contactList.get(n).getBoatPhoto();
            Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.boat1).error(R.drawable.boat1).into(photo);
        }else{
            photo.setImageResource(R.drawable.boat1);
        }

        //set name
        if(contactList.get(n).getBoatName()!=null && !contactList.get(n).getBoatName().equals("") && !contactList.get(n).getBoatName().equals("null")){
            name.setText(contactList.get(n).getBoatName());
        }else{
            name.setText("");
        }

        //set year
        if(contactList.get(n).getBoatYear()!=null && !contactList.get(n).getBoatYear().equals("") && !contactList.get(n).getBoatYear().equals("999")){
            country.setText("Год: "+contactList.get(n).getBoatYear());
        }else{
            country.setText("Год: пусто");
        }

        //set loa
        if(contactList.get(n).getBoatLoa()!=null && !contactList.get(n).getBoatLoa().equals("") && !contactList.get(n).getBoatLoa().equals("999")){
            experience.setText("LOA: "+contactList.get(n).getBoatLoa());
        }else{
            experience.setText("LOA: пусто");
        }

        //set tags

        int[] color1 = {Color.parseColor("#096aa1"), Color.parseColor("#ffffff"), Color.parseColor("#ffffff")};
        colors.add(color1);
        tags.add(contactList.get(n).getBoatType());
        if(contactList.get(n).getBoatModel()!=null && !contactList.get(n).getBoatModel().equals("") && !contactList.get(n).getBoatModel().equals("null")){
            colors.add(color1);
            tags.add(contactList.get(n).getBoatModel());
        }
        tag.setTags(tags);


        //set gallery
        if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            Call<PhotoList> call = api.getGalleryById("Bearer " + LoginActivity.stringBuilder.toString(),id);

            call.enqueue(new Callback<PhotoList>() {
                @Override
                public void onResponse(Call<PhotoList> call, Response<PhotoList> response) {
                    if (response.isSuccessful()) {
                        gallery = response.body().getGallery();
                        if((gallery==null || gallery.size()==0) && !contactList.get(position).getName().equals(Main2Activity.myname.getText().toString()))
                            plusPhoto.setVisibility(View.GONE);
                        if(isGalleryUpdating){
                            Toast toast = Toast.makeText(context,
                                    "Фото загружено", Toast.LENGTH_SHORT);
                            toast.show();
                            isGalleryUpdating = false;
                        }
                        Log.i("galery1", response.body().toString()+" ");
                    } else {
                        Log.i("galery1", response.toString()+" ");
                    }
                }

                @Override
                public void onFailure(Call<PhotoList> call, Throwable t) {
                    Log.i("galery1", "fail");
                }
            });
        }


    }

    public void sort(String s){

        tmpContactList = new ArrayList<>();
        for(int i = 0; i < contactList.size(); i ++){
            if(contactList.get(i).getCategories()!=null){
                for(int j = 0; j < contactList.get(i).getCategories().size(); j ++){
                    if(contactList.get(i).getCategories().get(j).getTitle().equals(s)){
                        tmpContactList.add(contactList.get(i));
                    }
                }
            }
        }
        if(s.equals("друзья")&&friendsList!=null){
            for(int i = 0; i < contactList.size(); i ++){
                    for(int j = 0; j < friendsList.size(); j ++){
                        if(contactList.get(i).getId().equals(friendsList.get(j).getUser_id())){
                            tmpContactList.add(contactList.get(i));
                        }
                    }

            }
        }
    }

    public void getFriends(){
        if (((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            Call<FriendList> call = api.getFriends("Bearer " + LoginActivity.stringBuilder.toString(), "my");
            call.enqueue(new Callback<FriendList>() {
                @Override
                public void onResponse(Call<FriendList> call, Response<FriendList> response) {
                    if (response.isSuccessful()) {


                        friendsList = response.body().getIds();
                        if(friendsList!=null && friendsList.size()!=0)
                            Log.i(friendsList.get(0)+" " , "success");
                    } else {
                        Log.i("FRIENDS" , response.body()+"no success");
                    }
                }

                @Override
                public void onFailure(Call<FriendList> call, Throwable t) {
                    Log.i("FRIENDS" , t.getMessage()+" ");
                }
            });

        } else {
            Log.i("FRIENDS" , "no internet");
        }
    }

    public void addFriends(){}

    public void askEditInfo(){
        for(int i = 0; i < contactList.size(); i++){
            if(contactList.get(i).getId().equals(Main2Activity.id)){
                if(contactList.get(i).getBoatPhoto()==null || contactList.get(i).getBoatPhoto().equals("null") || contactList.get(i).getBoatPhoto().equals("")){
                    if(contactList.get(i).getBoatName()==null || contactList.get(i).getBoatName().equals("null") || contactList.get(i).getBoatName().equals("")){
                        if(contactList.get(i).getBoatModel()==null || contactList.get(i).getBoatModel().equals("null") || contactList.get(i).getBoatModel().equals("")){
                            AlertDialog.Builder ad;
                            String message;
                            String button1String;
                            String button2String;
                            message = "Хотите указать информацию о яхте сейчас?";
                            button1String = "да";
                            button2String = "нет";

                            ad = new AlertDialog.Builder(getActivity());
                            ad.setMessage(message); // сообщение
                            ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                    Intent intent = new Intent(getActivity(), BoatInfoActivity.class);
                                    startActivity(intent);
                                }
                            });
                            ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                    showMe.performClick();
                                }
                            });
                            ad.show();
                        }
                    }
                }
                Log.i("edinfo" , contactList.get(i).getBoatPhoto()+contactList.get(i).getBoatName()+contactList.get(i).getBoatType()+" ");
                Log.i("edinfo" , contactList.get(i).getBoatPhoto()+contactList.get(i).getBoatName()+contactList.get(i).getBoatType()+" ");


            }
        }
    }

    public void findUser(String s){
        for( int i = 0; i < contactList.size(); i ++){
            if(contactList.get(i).getName().contains(s)){
                if(contactList.get(i).getLocation().get("lon")!=null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(contactList.get(i).getLocation().get("lat")), Double.valueOf(contactList.get(i).getLocation().get("lon"))), 6));
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_PICK_PHOTO_GALLERY && resultCode == Activity.RESULT_OK){
            if ((data != null) && (data.getData() != null)){
                String imagePath;
                imagePath = getFilePath(data);
                File file = new File(imagePath);
                Log.i("Gallery" , imagePath+")");
                //File file = new File(u.getEncodedPath()+".jpg");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
                Toast toast = Toast.makeText(context,
                        "Фото загрузится через несколько секунд", Toast.LENGTH_SHORT);
                toast.show();
                Call<ResponseBody> call = api.addGallery("Bearer " + LoginActivity.stringBuilder.toString(), "photo1",  photo);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            setProfile(Main2Activity.id);
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
                                Log.i("Gallery" , jObjError.getString("message")+"1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("Gallery" , t.getMessage()+" ");
                    }
                });

            }
        }

    }



    private String getFilePath(Intent data) {
        String imagePath;
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        imagePath = cursor.getString(columnIndex);
        cursor.close();

        return imagePath;

    }


}
