package com.hse.app.reader.comic.comicreader.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.hse.app.reader.comic.comicreader.ChapterActivity;
import com.hse.app.reader.comic.comicreader.Interface.IRecyclerOnClick;
import com.hse.app.reader.comic.comicreader.R;
import com.hse.app.reader.comic.comicreader.common.Common;
import com.hse.app.reader.comic.comicreader.model.Comic;

import java.util.List;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {


    Activity activity;
    Context context;
    List<Comic> comicList;

    public MyComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.comic_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Picasso.get().load(comicList.get(i).getImage()).into(myViewHolder.imageView);
        myViewHolder.textView.setText(comicList.get(i).getName());

        myViewHolder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(View view, int position) {

                Intent myIntent =  new Intent(context, ChapterActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(myIntent);

                Common.selected_comic = comicList.get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        IRecyclerOnClick iRecyclerOnClick;

        public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            textView = (TextView)itemView.findViewById(R.id.manga_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerOnClick.onClick(v, getAdapterPosition());
        }
    }
}
