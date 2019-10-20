package edu.temple.spiapp;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotiAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Uri> linkArray;

    public NotiAdapter(Context context, ArrayList<Uri> linkArray) {
        this.context = context;
        this.linkArray = linkArray;
    }

    @Override
    public int getCount() {
        return linkArray.size();
    }

    @Override
    public Object getItem(int position) {
        return linkArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(this.context);
        try {
            Picasso.get().load(linkArray.get(position)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageView;
    }
}
