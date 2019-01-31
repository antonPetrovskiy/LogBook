package social.tosch.com.social.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import social.tosch.com.social.Controller;
import social.tosch.com.social.R;
import social.tosch.com.social.activities.LoginActivity;
import social.tosch.com.social.activities.Main2Activity;
import social.tosch.com.social.api.ApiService;
import social.tosch.com.social.api.RetroClient;
import social.tosch.com.social.entity.Dialog;
import social.tosch.com.social.entity.Message;
import social.tosch.com.social.fragments.MapFragment;

public class DialogAdapter  extends ArrayAdapter<Dialog> {
    public static List<Dialog> dialogList;
    ApiService api;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public DialogAdapter(Context context, List<Dialog> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        dialogList = objects;
        api = RetroClient.getApiService();
    }

    @Override
    public Dialog getItem(int position) {
        return dialogList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DialogAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_dialog_view, parent, false);
            vh = DialogAdapter.ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (DialogAdapter.ViewHolder) convertView.getTag();
        }

        final Dialog item = getItem(position);
        vh.imageView.setImageResource(R.drawable.photo1);
        vh.textViewName.setText("Removed user");
        if(Controller.getInstance(context).getContactList()!=null) {
            for (int i = 0; i < Controller.getInstance(context).getContactList().size(); i++) {
                if (Controller.getInstance(context).getContactList().get(i).getId().equals(item.getUser_id() + "")) {
                    vh.textViewName.setText(Controller.getInstance(context).getContactList().get(i).getName());
                    if (Controller.getInstance(context).getContactList().get(i).getPhoto() != null && !Controller.getInstance(context).getContactList().get(i).getPhoto().equals("null")) {
                        LinkedTreeMap<String, String> map = (LinkedTreeMap) Controller.getInstance(context).getContactList().get(i).getPhoto();
                        Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(vh.imageView);
                    } else {
                        vh.imageView.setImageResource(R.drawable.photo1);
                    }
                }
            }
        }
            //if(messageList!=null && messageList.size()!=0)
            vh.textViewMessage.setText(dialogList.get(position).getLast_message().get("text"));


        Date df = new java.util.Date(Long.valueOf(item.getLast_message().get("created_at"))*1000);
        String dd = new SimpleDateFormat("dd").format(df);
        vh.textViewTime.setText(new SimpleDateFormat("HH:mm").format(df));

        Log.i("state", item.getLast_message().get("status")+" ");
        if(item.getLast_message().get("status").equals("0")) {

                            vh.textViewNew.setVisibility(View.VISIBLE);
                            vh.rootView.setBackgroundColor(Color.parseColor("#dbe9ff"));


        }else if(item.getLast_message().get("status").equals("1")) {
            vh.textViewNew.setVisibility(View.GONE);
            vh.rootView.setBackgroundColor(Color.TRANSPARENT);
        }


        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageView;
        public final TextView textViewName;
        public final TextView textViewMessage;
        public final TextView textViewTime;
        public final TextView textViewNew;

        private ViewHolder(LinearLayout rootView, ImageView imageView, TextView textViewName, TextView textViewEmail, TextView textViewTime, TextView textViewNew) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewMessage = textViewEmail;
            this.textViewTime = textViewTime;
            this.textViewNew = textViewNew;
        }

        public static DialogAdapter.ViewHolder create(LinearLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TextView textViewMessage = (TextView) rootView.findViewById(R.id.textViewMessage);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.textViewTime);
            TextView textViewNew = (TextView) rootView.findViewById(R.id.textViewNotification);
            return new DialogAdapter.ViewHolder(rootView, imageView, textViewName, textViewMessage, textViewTime, textViewNew);
        }

    }
}
