package social.tosch.com.social.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import social.tosch.com.social.R;
import social.tosch.com.social.entity.Category;
import social.tosch.com.social.entity.Contact;


public class ContactAdapter extends ArrayAdapter<Contact> {

    List<Contact> contactList;
    Context       context;
    private LayoutInflater mInflater;

    // Constructors
    public ContactAdapter(Context context, List<Contact> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        contactList = objects;
    }

    @Override
    public Contact getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Contact item = getItem(position);


        //set name
        vh.textViewName.setText(item.getName());




        //set photo
        Log.i("PHOTO1" , item.getName() + "  " + item.getPhoto().toString());
        if(item.getPhoto()!=null && !item.getPhoto().equals("null")) {
            LinkedTreeMap<String,String> map = (LinkedTreeMap) item.getPhoto();
            Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(vh.imageView);
        }else{
            vh.imageView.setImageResource(R.drawable.photo1);
        }
        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView      imageView;
        public final TextView       textViewName;
        public final TagContainerLayout tags;


        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName, TagContainerLayout tags) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.tags = tags;

        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TagContainerLayout tags = rootView.findViewById(R.id.tag);

            return new ViewHolder(rootView, imageView, textViewName, tags);
        }

    }

}