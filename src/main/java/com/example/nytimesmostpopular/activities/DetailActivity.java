package com.example.nytimesmostpopular.activities;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.nytimesmostpopular.R;
import com.example.nytimesmostpopular.utils.CommonObjects;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView tvDetail=findViewById(R.id.tvDetail);
        setTitle("Details");
        int i=Integer.valueOf(getIntent().getStringExtra("position"));
        tvDetail.setText(CommonObjects.getResponse().getResults().get(i).getTitle());
    }
}
