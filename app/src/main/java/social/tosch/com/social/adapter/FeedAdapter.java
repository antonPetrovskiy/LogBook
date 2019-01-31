package social.tosch.com.social.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.util.List;

import social.tosch.com.social.Controller;
import social.tosch.com.social.R;
import social.tosch.com.social.entity.Feed;


public class FeedAdapter extends ArrayAdapter<Feed> {

    public static List<Feed> feedList;

    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public FeedAdapter(Context context, List<Feed> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        feedList = objects;
    }

    @Override
    public Feed getItem(int position) {
        return feedList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_feed_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            Resources r = context.getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
            int px1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
            params.setMargins(px, px, px, px1);
            vh.rootView.setLayoutParams(params);
        }else{

        }


        Feed item = getItem(position);

        for(int i = 0; i < Controller.getInstance(context).getContactList().size(); i ++){
            if(Controller.getInstance(context).getContactList().get(i).getId().equals(item.getUser_id()+"")){
                vh.textViewName.setText(Controller.getInstance(context).getContactList().get(i).getName());
                if(Controller.getInstance(context).getContactList().get(i).getPhoto()!=null && !Controller.getInstance(context).getContactList().get(i).getPhoto().equals("null")) {
                    LinkedTreeMap<String,String> map = (LinkedTreeMap) Controller.getInstance(context).getContactList().get(i).getPhoto();
                    Picasso.with(context).load(map.get("thumb")).placeholder(R.drawable.photo1).error(R.drawable.photo1).into(vh.imageView);
                }else{
                    vh.imageView.setImageResource(R.drawable.photo1);
                }
            }
            if((position+2)%2==0) {
                //vh.rootView.setBackgroundColor(Color.parseColor("#f3fbff"));
            }else{
                //vh.rootView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

        }
        vh.textViewFeed.setText(item.getText());

//        Log.i("PHOTO" , item.getPhoto().toString());

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final TextView textViewName;
        public final TextView textViewFeed;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewName, TextView textViewEmail) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewFeed = textViewEmail;
        }

        public static FeedAdapter.ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TextView textViewFeed = (TextView) rootView.findViewById(R.id.textViewFeed);
            return new FeedAdapter.ViewHolder(rootView, imageView, textViewName, textViewFeed);
        }

    }

}
