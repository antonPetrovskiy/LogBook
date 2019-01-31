package social.tosch.com.social.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.MenuButtons;
import social.tosch.com.social.MusicWorker;
import social.tosch.com.social.R;
import social.tosch.com.social.adapter.DialogAdapter;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Dialog;
import social.tosch.com.social.entity.DialogList;
import social.tosch.com.social.fragments.MessagesFragment;


public class MyService extends Service {
    private NotificationManager notificationManager;
    Timer timer;
    final ApiService api= RetroClient.getApiService();
    private static ArrayList<Dialog> dialogList;
    public static final int DEFAULT_NOTIFICATION_ID = 101;
    public ArrayList<String> sms;

    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        sms = new ArrayList<String>();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        //Send Foreground Notification
        sendNotification("Ticker","Title","Text");

        //Task
        doTask();



        //return Service.START_STICKY;
        return START_REDELIVER_INTENT;
    }

    //Send custom notification
    public void sendNotification(String Ticker,String Title,String Text) {

        //These three lines makes Notification to open main activity after clicking on it
        Intent notificationIntent = new Intent(this, Main2Activity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public void doTask(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new mainTask(), 0, 1500);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            if (((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null && LoginActivity.stringBuilder != null) {
                Call<DialogList> call = api.getDialogList("Bearer " + LoginActivity.stringBuilder.toString());
                call.enqueue(new Callback<DialogList>() {
                    @Override
                    public void onResponse(Call<DialogList> call, Response<DialogList> response) {
                        if (response.isSuccessful()) {
                            if(response.body().getIds()!=null) {
                                dialogList = new ArrayList<>();
                                dialogList = response.body().getIds();
                                if(dialogList!= null && dialogList.size()!=0){
                                    for(int i = 0; i < dialogList.size(); i++){
                                        final int n = i;
                                        if(dialogList.get(i).getLast_message().get("status").equals("0")){
                                            Call<social.tosch.com.social.entity.Message> call1 = api.getMessage("Bearer " + LoginActivity.stringBuilder.toString(), dialogList.get(i).getLast_message().get("message_id"));
                                            call1.enqueue(new Callback<social.tosch.com.social.entity.Message>() {
                                                @Override
                                                public void onResponse(Call<social.tosch.com.social.entity.Message> call, Response<social.tosch.com.social.entity.Message> response) {
                                                    if (response.isSuccessful()) {
                                                        if(!response.body().getFor_user_id().equals(dialogList.get(n).getUser_id())) {
                                                            if(!sms.contains(dialogList.get(n).getLast_message().get("message_id"))){
                                                                sms.add(dialogList.get(n).getLast_message().get("message_id"));
                                                                toastHandler.sendEmptyMessage(0);
                                                                MessagesFragment.newInstance(getApplicationContext()).update();
                                                                if(Main2Activity.isChat){
                                                                    DialogActivity.update();

                                                                }
                                                                if(MenuButtons.isMessage){
                                                                    MenuButtons.button_messages.setImageResource(R.drawable.new_message);
                                                                }else{
                                                                    MenuButtons.button_messages.setImageResource(R.drawable.new_message_used);
                                                                }
                                                                doNotification();
                                                            }

                                                        }


                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<social.tosch.com.social.entity.Message> call, Throwable t) {
                                                }
                                            });

                                            return;
                                        }
                                    }
                                    if(MenuButtons.isMessage){
                                        MenuButtons.button_messages.setImageResource(R.drawable.mail_used);
                                        MenuButtons.button_map.setImageResource(R.drawable.globe);
                                    }else{
                                        MenuButtons.button_messages.setImageResource(R.drawable.mail);
                                        MenuButtons.button_map.setImageResource(R.drawable.globe_used);
                                    }

                                }
                            }
                        } else {
                            Log.i("LISTERF" , "no success");
                        }
                    }

                    @Override
                    public void onFailure(Call<DialogList> call, Throwable t) {
                        Log.i("LISTERF" , t.getMessage()+" ");
                    }
                });

            } else {
                Log.i("LIST" , "no internet");
            }




        }
    }

    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            //Toast.makeText(getApplicationContext(), "Новое сообщение",Toast.LENGTH_SHORT).show();
        }
    };

    public void doNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Logbook")
                        .setTicker("Новое сообщение")
                        .setAutoCancel(true)
                        .setVibrate(new long[]{0, 500, 1000})
                        .setDefaults(Notification.DEFAULT_LIGHTS )
                        .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getPackageName()+"/"+R.raw.sound))
                        .setContentText("Новое сообщение");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, LoginActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Main2Activity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

        //MusicWorker.getInstance(getApplicationContext()).playSound(MusicWorker.top);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire(3000);
        wl.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Removing any notifications
        notificationManager.cancel(DEFAULT_NOTIFICATION_ID);

        //Disabling service
        stopSelf();
    }
}
