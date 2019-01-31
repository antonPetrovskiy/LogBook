package social.tosch.com.social.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import social.tosch.com.social.R;
import social.tosch.com.social.activities.DialogActivity;
import social.tosch.com.social.entity.Message;

public class MessageAdapter extends ArrayAdapter<Message> {

    public static List<Message> messageList;

    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MessageAdapter(Context context, List<Message> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        messageList = objects;
    }

    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MessageAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_message_view, parent, false);
            vh = MessageAdapter.ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (MessageAdapter.ViewHolder) convertView.getTag();
        }

        Message item = getItem(position);
        vh.textViewMessage.setText(item.getText());

        Date currentTime = Calendar.getInstance().getTime();
        String currentDate = new SimpleDateFormat("dd").format(currentTime);

        Date df = new java.util.Date(Long.valueOf(item.getTime())*1000);
        String dd = new SimpleDateFormat("dd").format(df);
        if(dd.equals(currentDate)){
            vh.textViewTime.setText(new SimpleDateFormat("HH:mm").format(df));
        }else{
            vh.textViewTime.setText(new SimpleDateFormat("HH:mm").format(df));
        }


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
        int px1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        params.setMargins(px1, px, px, px);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.ALIGN_PARENT_START);
        Resources r1 = context.getResources();
        int px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r1.getDisplayMetrics());
        int px3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r1.getDisplayMetrics());
        params1.setMargins(px2, px2, px3, px2);

        if(!item.getFor_user_id().equals(DialogActivity.id)){
            vh.mainView.setBackgroundResource(R.drawable.layout_bg1);
            vh.mainView.setLayoutParams(params1);
            vh.textViewTime.setTextColor(Color.parseColor("#979ca0"));
        }else{
            vh.mainView.setBackgroundResource(R.drawable.layout_bg);
            vh.mainView.setLayoutParams(params);
            vh.textViewTime.setTextColor(Color.parseColor("#64889c"));
            if(item.getStatus().equals("0")){
                vh.status.setVisibility(View.GONE);
            }else{
                vh.status.setVisibility(View.VISIBLE);
            }
        }



//        Log.i("PHOTO" , item.getPhoto().toString());

        return vh.rootView;
    }


    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final RelativeLayout mainView;
        public final TextView textViewMessage;
        public final TextView textViewTime;
        public final ImageView status;

        private ViewHolder(RelativeLayout rootView, RelativeLayout mainView, TextView textViewMessage, TextView textViewTime, ImageView status) {
            this.rootView = rootView;
            this.mainView = mainView;
            this.textViewMessage = textViewMessage;
            this.textViewTime = textViewTime;
            this.status = status;
        }

        public static MessageAdapter.ViewHolder create(RelativeLayout rootView) {
            TextView textViewMessage = (TextView) rootView.findViewById(R.id.textViewMessage);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.textView27);
            RelativeLayout mainView = (RelativeLayout) rootView.findViewById(R.id.main);
            ImageView status = rootView.findViewById(R.id.imageView26);
            return new MessageAdapter.ViewHolder(rootView, mainView, textViewMessage, textViewTime, status);
        }



    }
}
