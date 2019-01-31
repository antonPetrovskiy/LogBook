package social.tosch.com.social.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import social.tosch.com.social.Controller;
import social.tosch.com.social.R;
import social.tosch.com.social.entity.InfoWindowData;

public class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;
    private Context context;

    public PopupAdapter(LayoutInflater inflater, Context context) {
        this.inflater=inflater;
        this.context = context;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoWindow(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.layout_popup, null);
        }

        ImageView photo = popup.findViewById(R.id.imageView);
        TextView name = popup.findViewById(R.id.textViewName);
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        if(infoWindowData.getImage()!=null && !infoWindowData.getImage().equals("null")) {
            LinkedTreeMap<String,String> map = (LinkedTreeMap) infoWindowData.getImage();
            Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(photo);
        }else{
            photo.setImageResource(R.drawable.photo1);
        }
//        for(int i = 0; i < Controller.getInstance(context).getContactList().size(); i++){
//            if(Controller.getInstance(context).getContactList().get(i).getId().equals(infoWindowData.getId())){
//                if(Controller.getInstance(context).getContactList().get(i).getPhoto()!=null && !Controller.getInstance(context).getContactList().get(i).getPhoto().equals("null")) {
//                    LinkedTreeMap<String,String> map = (LinkedTreeMap) infoWindowData.getImage();
//                    Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(photo);
//                }else{
//                    photo.setImageResource(R.drawable.photo1);
//                }
//            }
//        }

        String [] s = infoWindowData.getName().split(" ");
        if(s.length>1) {
            name.setText(s[0] + "\n" + s[1]);
        }else{
            name.setText(s[0]);
        }
        //name.setText(infoWindowData.getName());


        return(popup);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
