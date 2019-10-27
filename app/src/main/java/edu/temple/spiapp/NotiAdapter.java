package edu.temple.spiapp;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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
        LinearLayout linearLayout = new LinearLayout(this.context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(this.context);
        try {
            Picasso.get().load(linkArray.get(position)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView textView = new TextView(this.context);
        textView.setText(nameArray.get(position));
        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        return linearLayout;
    }
}
