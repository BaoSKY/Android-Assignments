package com.example.studycourse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.studycourse.R;
import com.example.studycourse.Util.MoreTypeBean;
import com.example.studycourse.activity.AudioPlayActivity;
import com.example.studycourse.activity.VideoPlayActiivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SearchRecyclerviewAdapter extends RecyclerView.Adapter {
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_IMAGE = 2;

    private Context context;
    private List<MoreTypeBean> mData;
    String imageUrl="http://a4.att.hudong.com/05/71/01300000057455120185716259013.jpg";

    public SearchRecyclerviewAdapter(Context context,List<MoreTypeBean> mData){
        super();
        this.context = context;
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if(viewType == TYPE_VIDEO){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video, parent, false);
            VideoHolder vh = new VideoHolder(v);
            return vh;
        }else if(viewType == TYPE_AUDIO){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_audio, parent, false);
            AudioHolder vh = new AudioHolder(v);
            return vh;
        }else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_image, parent, false);
            ImageHolder vh = new ImageHolder(v);
            return vh;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String resource = "";
        if(holder instanceof VideoHolder){
            resource = mData.get(position).material;
        }else if(holder instanceof AudioHolder){
            resource = mData.get(position).material;
        }else{
            resource = mData.get(position).material;
            Picasso.get()
                    .load(resource)
                    .into(((ImageHolder)holder).imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.getItemViewType()==TYPE_VIDEO){
                    Intent intent = new Intent(context, VideoPlayActiivity.class);
                    String url = mData.get(position).material;
                    intent.putExtra("url",url);
                    context.startActivity(intent);
                }else if(holder.getItemViewType()==TYPE_AUDIO){
                    Intent intent = new Intent(context, AudioPlayActivity.class);
                    String url = mData.get(position).material;
                    intent.putExtra("url",url);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        System.out.println("数据大小"+mData.size());
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        MoreTypeBean moreTypeBean = mData.get(position);
        if (moreTypeBean.type == 0) {
            return TYPE_VIDEO;
        } else if (moreTypeBean.type == 1) {
            return TYPE_AUDIO;
        } else {
            return TYPE_IMAGE;
        }
    }


    public static class VideoHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public VideoHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.iv_video);
        }
    }

    public static class AudioHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public AudioHolder(View v) {
            super(v);
        }
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;

        public ImageHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.iv_image);
        }
    }

}

