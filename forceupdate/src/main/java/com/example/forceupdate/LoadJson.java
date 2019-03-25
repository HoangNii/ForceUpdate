package com.example.forceupdate;

import android.os.AsyncTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadJson extends AsyncTask<String,Void,String> {

    private onListen onListen;

    public LoadJson(LoadJson.onListen onListen) {
        this.onListen = onListen;
    }

    @Override
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(strings[0]);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onListen.onLoad(s);
    }


    public interface onListen{
        void onLoad(String s);
    }

}
