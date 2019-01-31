package social.tosch.com.social.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import social.tosch.com.social.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    List<Integer> mThumbIds;
    int x;

    // Keep all Images in array

    // Constructor
    public ImageAdapter(Context c, List<Integer> list, int n) {
        mContext = c;
        mThumbIds = list;
        x=n;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mThumbIds.size(); // длина массива
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mThumbIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(220, 210));


        return imageView;
    }
}
