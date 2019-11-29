package edu.temple.spiapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.MyViewHolder> {
    @NonNull
    public Context context;
    public ArrayList<Uri> linkArray;
    public ArrayList<String> nameArray;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notiName;
        ImageView notiImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.notiImage = itemView.findViewById(R.id.notiImage);
            this.notiName = itemView.findViewById(R.id.notiName);
        }
    }


    public NotiAdapter(@NonNull Context context, ArrayList<Uri> linkArray, ArrayList<String> nameArray) {
        this.context = context;
        this.linkArray = linkArray;
        this.nameArray = nameArray;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noti_card_view, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        TextView notiName = holder.notiName;
        final ImageView notiImage = holder.notiImage;
        final int finalPos = position;

        notiName.setText(nameArray.get(nameArray.size() - position - 1));
        new Runnable() {
            @Override
            public void run() {
                Picasso.get().load(linkArray.get(linkArray.size() - finalPos - 1)).into(notiImage);
            }
        }.run();
    }

    @Override
    public int getItemCount() {
        return Math.min(linkArray.size(), 15);
    }
}
