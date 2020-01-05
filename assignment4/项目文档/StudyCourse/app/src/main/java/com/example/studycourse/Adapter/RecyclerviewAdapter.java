package com.example.studycourse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studycourse.R;
import com.example.studycourse.Util.Course;
import com.example.studycourse.activity.CourseDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerviewAdapter";
    private Context context;
    private List<Course> mCourseList;
    Course course;

    public RecyclerviewAdapter(Context context,List<Course> mCourseList){
        super();
        this.context = context;
        this.mCourseList = mCourseList;
    }

    @Override
    public RecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerviewAdapter.ViewHolder holder,final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        course = mCourseList.get(position);
        //holder.imageView.setImageResource(course.getImageId());

        holder.tvTitle.setText(course.getcourseName());
        holder.tvTime.setText(course.getTeacher());
        holder.tvContext.setText(course.getSchool());
        Picasso.get()
                .load(course.getImageURL())
                .into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(context, VideoDetailActivity.class);
                //将当前选中的试卷信息进行传到下一个界面
                String newsContent = mCourseList.get(position).getNewsContent();
                intent.putExtra("newsContent", newsContent);
                context.startActivity(intent);
                 */

                Intent intent = new Intent(context, CourseDetailsActivity.class);
                String idContent = mCourseList.get(position).getCourseID();
                intent.putExtra("idContent",idContent);
                context.startActivity(intent);
            }
        });
    }

    //获取列表长度
    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;
        public TextView tvTitle,tvTime,tvContext;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.IV_list_Id);
            tvTitle = (TextView)v.findViewById(R.id.TV_listTitle_Id);
            tvTime = (TextView)v.findViewById(R.id.TV_listTime_Id);
            tvContext = (TextView)v.findViewById(R.id.TV_listContext_Id);
        }
    }

    /*
    public static class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView newsNameTextView;
        TextView newsContenTextView;
        TextView newsColumnTextView;
        TextView newsDaTextView;
        ImageView newsImageView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            newsNameTextView = (TextView) itemView.findViewById(R.id.item_News_id);
            newsContenTextView = (TextView) itemView.findViewById(R.id.item_News_desc);
            newsColumnTextView = (TextView) itemView.findViewById(R.id.item_news_column);
            newsDaTextView = (TextView) itemView.findViewById(R.id.itemn_news_date);
            newsImageView = (ImageView) itemView.findViewById(R.id.item_News_img);
        }
    }
     */
}

