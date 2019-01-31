package social.tosch.com.social.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.Controller;
import social.tosch.com.social.MenuButtons;
import social.tosch.com.social.R;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Contact;
import social.tosch.com.social.fragments.MapFragment;
import social.tosch.com.social.fragments.MessagesFragment;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Main2Activity.SectionsPagerAdapter mSectionsPagerAdapter;
    public static ViewPager mViewPager;
    public static DrawerLayout drawer;
    public static MenuButtons menuButtons;

    public static TextView myname;
    public static ImageView myphoto;
    ApiService api;
    public static String id;
    public static boolean isChat = false;
    public static String chatId;
    Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intentService = new Intent(this,MyService.class);
        startService(intentService);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.getHeaderView(0);
        myname = headerView.findViewById(R.id.myname);
        myphoto = headerView.findViewById(R.id.myphoto);
        navigationView.setNavigationItemSelectedListener(this);
        headerView.findViewById(R.id.myphoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, InformationActivity.class);
                startActivity(intent);
            }
        });
        checkUserPermission();
        Controller.getInstance(getApplicationContext()).getContactList();
        api = RetroClient.getApiService();
        mSectionsPagerAdapter = new Main2Activity.SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);
        menuButtons = new MenuButtons(this, mViewPager);



        update();



    }

    @Override
    protected void onResume() {
        super.onResume();
        isChat=false;
        MessagesFragment.newInstance(this).update();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
    }

    public void update(){
        if (((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null) {
            Call<Contact> call = api.getUser("Bearer " + LoginActivity.stringBuilder.toString());
            Log.i("GETS" , "Bearer " + LoginActivity.stringBuilder.toString());
            call.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if (response.isSuccessful()) {

                        id = response.body().getId();
                        Log.i("categorieeees", id+"ц");
                        myname.setText(response.body().getName());
                        if(response.body().getPhoto()!=null && !response.body().getPhoto().equals("null")) {
                            LinkedTreeMap<String,String> map = (LinkedTreeMap) response.body().getPhoto();
                            Picasso.with(getApplicationContext()).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(myphoto);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(Main2Activity.this, InformationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dialog) {
            mViewPager.setCurrentItem(1);
            MenuButtons.button_map.setImageResource(R.drawable.globe);
            MenuButtons.button_messages.setImageResource(R.drawable.mail_used);
        } else if (id == R.id.nav_map) {
            mViewPager.setCurrentItem(0);
            MenuButtons.button_map.setImageResource(R.drawable.globe_used);
            MenuButtons.button_messages.setImageResource(R.drawable.mail);
        }else if (id == R.id.nav_news) {
            mViewPager.setCurrentItem(1);
            MenuButtons.button_map.setImageResource(R.drawable.globe);
            MenuButtons.button_messages.setImageResource(R.drawable.mail_used);
        }else if (id == R.id.nav_boat) {
            Intent intent = new Intent(Main2Activity.this, BoatInfoActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return MapFragment.newInstance(getApplicationContext());
                case 1:
                    return MessagesFragment.newInstance(getApplicationContext());
                default:
                    return null;//Это для того, что бы что-то вернулось, если порядковый номер вдруг будет больше 2. И в данном случае приложение закроется с ошибкой.

            }

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    public void click_Service(View v) {
        if (!isMyServiceRunning(MyService.class)) {
            startService(intentService);
        } else {
            stopService(intentService);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
