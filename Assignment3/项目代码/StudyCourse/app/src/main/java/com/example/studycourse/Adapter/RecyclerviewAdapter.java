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
import com.example.studycourse.Util.MoreTypeBean;
import com.example.studycourse.activity.CourseDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter {
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_IMAGE = 0;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View v;
        if(viewType == TYPE_VIDEO){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_video_layout, parent, false);
            MoreViewHolder vh = new MoreViewHolder(v);
            return vh;
        }else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        course = mCourseList.get(position);

        if (holder instanceof MoreViewHolder) {
            ((MoreViewHolder)holder).tvCourse.setText(course.getcourseName());
            ((MoreViewHolder)holder).tvTeacher.setText(course.getTeacher());
            ((MoreViewHolder)holder).tvSchool.setText(course.getSchool());
            Picasso.get()
                    .load(course.getImageURL())
                    .into(((MoreViewHolder)holder).imageBook);

        }else if(holder instanceof ViewHolder){
            ((ViewHolder)holder).tvTitle.setText(course.getcourseName());
            ((ViewHolder)holder).tvTime.setText(course.getTeacher());
            ((ViewHolder)holder).tvContext.setText(course.getSchool());
            Picasso.get()
                    .load(course.getImageURL())
                    .into(((ViewHolder)holder).imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseDetailsActivity.class);
                String idContent = mCourseList.get(position).getCourseID();
                intent.putExtra("idContent",idContent);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mCourseList.get(position).getType() == 1) {
            return TYPE_VIDEO;
        } else {
            return TYPE_IMAGE;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

    public static class MoreViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageVideo;
        public ImageView imageBook;
        public TextView tvCourse,tvSchool,tvTeacher;

        public MoreViewHolder(View v) {
            super(v);
            imageVideo = (ImageView)v.findViewById(R.id.IV_video);
            imageBook = (ImageView)v.findViewById(R.id.IV_book);
            tvCourse = (TextView)v.findViewById(R.id.TV_course);
            tvSchool = (TextView)v.findViewById(R.id.TV_school);
            tvTeacher = (TextView)v.findViewById(R.id.TV_teacher);
        }
    }
}

