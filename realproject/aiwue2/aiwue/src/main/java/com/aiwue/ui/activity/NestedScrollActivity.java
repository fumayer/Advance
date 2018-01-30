package com.aiwue.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aiwue.R;

public class NestedScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);
        RecyclerView rv= (RecyclerView) findViewById(R.id.rv);
        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new Holder(View.inflate(parent.getContext(),R.layout.item_news,null));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 10;
            }
            class Holder extends RecyclerView.ViewHolder{

                public Holder(View itemView) {
                    super(itemView);
                }
            }

        });
    }
}
