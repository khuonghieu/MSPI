package edu.temple.spiapp;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotiAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Uri> linkArray;
    public ArrayList<String> nameArray;

    public NotiAdapter(Context context, ArrayList<Uri> linkArray, ArrayList<String> nameArray) {
        this.context = context;
        this.linkArray = linkArray;
        this.nameArray = nameArray;
    }

    @Override
    public int getCount() {
        return Math.min(linkArray.size(), 10);
    }

    @Override
    public Object getItem(int position) {
        return linkArray.get(linkArray.size()-position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(this.context);
        try {
            Picasso.get().load(linkArray.get(linkArray.size() - position -1)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textView = new TextView(this.context);
        textView.setText(nameArray.get(nameArray.size() - position -1));
        textView.setTextSize(20);

        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        
        return linearLayout;
    }
}
