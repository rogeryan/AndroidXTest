package com.scujcc.androidxtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView channelList;
    private ChannelListAdapter listAdapter;
    private List<Channel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        channelList =  findViewById(R.id.channelList);

        listAdapter = new ChannelListAdapter(this.data, new ChannelClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("FFPLAY", "Clicked "+view+" on "+position);
                if (position < data.size()) {
                    Channel c = data.get(position);
                    Intent intent = new Intent(MainActivity.this, LiveActivity.class);
                    intent.putExtra("channel", c);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "无效的频道",Toast.LENGTH_SHORT);
                }
            }
        });
        channelList.setAdapter(listAdapter);
        channelList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        DataLab lab = new DataLab(this);
        this.data = lab.getChannels("data.json");
    }

}
