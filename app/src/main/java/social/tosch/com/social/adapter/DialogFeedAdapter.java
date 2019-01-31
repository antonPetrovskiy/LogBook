package social.tosch.com.social.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import social.tosch.com.social.R;

public class DialogFeedAdapter extends ArrayAdapter<String> {
    public static List<String> dialogList;
    public static List<String> messageList;

    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public DialogFeedAdapter(Context context, List<String> objects, List<String> objects1) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        dialogList = objects;
        messageList = objects1;
    }

    @Override
    public String getItem(int position) {
        return dialogList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DialogFeedAdapter.ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_dialog_view, parent, false);
            vh = DialogFeedAdapter.ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (DialogFeedAdapter.ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 25, 0, 0); // llp.setMargins(left, top, right, bottom);
        vh.textViewName.setLayoutParams(llp);
        vh.imageView.setPadding(15,15,15,15);
        vh.textViewName.setTextColor(Color.parseColor("#95aab7"));
            if(item.equals("sell_buy")) {
                vh.textViewName.setText("Покупка/продажа");
                vh.imageView.setImageResource(R.drawable.trade);
            }
            if(item.equals("service")) {
                vh.textViewName.setText("Сервис");
                vh.imageView.setImageResource(R.drawable.service);
            }
            if(item.equals("sport")) {
                vh.textViewName.setText("Парусный cпорт");
                vh.imageView.setImageResource(R.drawable.sail);
            }
            if(item.equals("else")) {
                vh.textViewName.setText("Общая");
                vh.imageView.setImageResource(R.drawable.feed);
            }
            vh.textViewMessage.setText(" ");
            vh.textViewTime.setText(" ");


//        Log.i("PHOTO" , item.getPhoto().toString());

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final ImageView imageView;
        public final TextView textViewName;
        public final TextView textViewMessage;
        public final TextView textViewTime;

        private ViewHolder(LinearLayout rootView, ImageView imageView, TextView textViewName, TextView textViewEmail, TextView textViewTime) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewName = textViewName;
            this.textViewMessage = textViewEmail;
            this.textViewTime = textViewTime;
        }

        public static DialogFeedAdapter.ViewHolder create(LinearLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewName);
            TextView textViewMessage = (TextView) rootView.findViewById(R.id.textViewMessage);
            TextView textViewTime = (TextView) rootView.findViewById(R.id.textViewTime);
            return new DialogFeedAdapter.ViewHolder(rootView, imageView, textViewName, textViewMessage, textViewTime);
        }

    }
}
