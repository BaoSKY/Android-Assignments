package com.example.studycourse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.studycourse.Adapter.SearchRecyclerviewAdapter;
import com.example.studycourse.MainActivity;
import com.example.studycourse.R;
import com.example.studycourse.Util.MoreTypeBean;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String response;
    private List<MoreTypeBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        response = bundle.getString("response");
        System.out.println(response);
        initData();

        recyclerView = (RecyclerView)findViewById(R.id.rv_materials) ;
        //给recyclerview里的item设置布局，一行一个
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        SearchRecyclerviewAdapter sra = new SearchRecyclerviewAdapter(SearchActivity.this,mData);
        recyclerView.setAdapter(sra);

    }

    public void initData(){
        mData = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(response);
        if(jsonArray.size()>0){
            for(int i=0;i<jsonArray.size();i++){
                JSONObject json = jsonArray.getJSONObject(i);
                String videoUrl = json.getString("videoUrl");
                String audioUrl = json.getString("audioUrl");
                String imageUrl = json.getString("imageUrl");

                if(videoUrl!=null){
                    MoreTypeBean mtb1 = new MoreTypeBean();
                    mtb1.material = videoUrl;
                    mtb1.type = 0;
                    mData.add(mtb1);
                }

                if(audioUrl!=null){
                    MoreTypeBean mtb2 = new MoreTypeBean();
                    mtb2.material = audioUrl;
                    mtb2.type = 1;
                    mData.add(mtb2);
                }

                if(imageUrl!=null){
                    MoreTypeBean mtb3 = new MoreTypeBean();
                    mtb3.material = imageUrl;
                    mtb3.type = 2;
                    mData.add(mtb3);
                }
            }
        }
    }

    public void onReturnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
