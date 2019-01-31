package social.tosch.com.social;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import social.tosch.com.social.fragments.MessagesFragment;

public class MenuButtons {


    public static ImageView button_map;
    public static ImageView button_messages;
    public static boolean isMessage = false;


    private Intent intent;
    private boolean color = false;

    private ViewPager mViewPager;
    private Activity rootActivity;


    public MenuButtons(Activity a, ViewPager p){
        this.rootActivity = a;
        this.mViewPager = p;
        initButtons();
        buttonsAction();

    }

    public void initButtons(){

        button_map = rootActivity.findViewById(R.id.imageView8);
        button_messages = rootActivity.findViewById(R.id.imageView13);
        button_map.setImageResource(R.drawable.globe_used);
    }

    public void buttonsAction(){

        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
                button_map.setImageResource(R.drawable.globe_used);
                button_messages.setImageResource(R.drawable.mail);
                isMessage = false;
            }
        });

        button_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
                button_map.setImageResource(R.drawable.globe);
                button_messages.setImageResource(R.drawable.mail_used);
                MessagesFragment.update();
                isMessage = true;
            }
        });

    }







}
