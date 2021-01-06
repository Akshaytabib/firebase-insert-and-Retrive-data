package com.example.insertandnotification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class InstaDataAdapter extends  RecyclerView.Adapter<InstaDataAdapter.ViewHolder> {

    Context context;
    ArrayList<Categories> arrayList;
    public InstaDataAdapter(MainActivity2 mainActivity2, ArrayList<Categories> arrayList) {
        this.context=mainActivity2;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public InstaDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new InstaDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstaDataAdapter.ViewHolder holder, int position) {
        Categories data=arrayList.get(position);

        Glide.with(context).load(data.getCategoryImage()).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        holder.textView.setText(data.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(itemView).findViewById(R.id.imageView);
            textView=(itemView).findViewById(R.id.textView);
        }
    }
}
