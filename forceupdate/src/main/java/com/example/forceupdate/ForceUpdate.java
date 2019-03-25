package com.example.forceupdate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public enum  ForceUpdate {
    get;

    private String url;

    private Dialog dialog;

    private WeakReference<Activity> activity;

    public ForceUpdate setUrl(String s){
        this.url = s;
        return this;
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public boolean isShow(){
        return dialog!=null&&dialog.isShowing();
    }
    public void getDialogDismiss(DialogInterface.OnDismissListener onDismissListener){
        dialog.setOnDismissListener(onDismissListener);
    }

    public void syn(){
        LoadJson loadJson = new LoadJson(new LoadJson.onListen() {
            @Override
            public void onLoad(String s) {
                final Force force = getForce(s);

                if (force != null) {

                    if (BuildConfig.VERSION_CODE >= force.getAppCode()) return;

                    if (force.getFlag() == Force.Flag.NOT_SHOW) return;

                    if (activity.get() == null) return;

                    dialog = new Dialog(activity.get());
                    dialog.setContentView(R.layout.dialog_update2);
                    dialog.setCancelable(force.getFlag() == Force.Flag.SHOW_CANCELABLE);

                    TextView tvTitle = dialog.findViewById(R.id.tvTitle);
                    TextView tvMessage = dialog.findViewById(R.id.tvMessage);
                    final Button btnLater = dialog.findViewById(R.id.btnLater);
                    Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

                    if (force.getFlag() == Force.Flag.SHOW_NOT_CANCELABLE) {
                        btnLater.setVisibility(View.GONE);
                    }
                    tvTitle.setText(force.getTitle());
                    tvMessage.setText(force.getMessage());
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToStore(activity.get(), force.getUpdateLink());
                        }
                    });

                    btnLater.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            }
        });
        loadJson.execute(url);
    }


    private Force getForce(String json){
        Force force = new Force();
        if(json!=null&&json.length()>0){
            try {
                JSONObject object = new JSONObject(json);
                String appId = object.getString("app_id");
                int appCode = Integer.parseInt(object.getString("app_code"));
                String title = object.getString("title");
                String message = object.getString("message");
                String link = object.getString("update_link");
                String f = object.getString("force_update");
                Force.Flag flag;
                switch (f) {
                    case "0":
                        flag = Force.Flag.NOT_SHOW;
                        break;
                    case "1":
                        flag = Force.Flag.SHOW_CANCELABLE;
                        break;
                    default:
                        flag = Force.Flag.SHOW_NOT_CANCELABLE;
                        break;
                }

                force.setAppId(appId);
                force.setAppCode(appCode);
                force.setFlag(flag);
                force.setTitle(title);
                force.setMessage(message);
                force.setUpdateLink(link);

                return force;


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    private void goToStore(Context context, String url) {
        if(url.contains("https://play.google.com/")){
            String MARKET_DETAILS_ID = "market://details?id=";
            String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
            String link = url.replace(PLAY_STORE_LINK,"");
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID +link))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK +link))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }

    }

}
