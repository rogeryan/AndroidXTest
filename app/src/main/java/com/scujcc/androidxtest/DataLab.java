package com.scujcc.androidxtest;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataLab {
    private  Context context;

    public DataLab(Context ctx) {
        context = ctx;
    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e("FFPLAY", ex.getMessage());
            ex.printStackTrace();
        }
        return json;
    }

    public List<Channel> getChannels(String filename) {
        List<Channel> result = new ArrayList<>();
        String json = loadJSONFromAsset(filename);
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray data = obj.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Channel c = new Channel();
                JSONObject item = data.getJSONObject(i);
                c.setTitle(item.getString("title"));
                c.setQuality(item.getString("quality"));
                c.setUrl(item.getString("url"));
                result.add(c);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
