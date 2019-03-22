package com.example.forceupdate;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class ForceUpdate {

    static final  String KEY_SAVE = "force_update";

    private static ForceUpdate instance;

    private String url;

    private boolean useCustom;

    private LoadJson loadJson;

    private OnListen onListen;

    public static ForceUpdate get() {
        if(instance==null)
            instance = new ForceUpdate();
        return instance;
    }

    public ForceUpdate urlLoad(String s){
        this.url = s;
        return this;
    }
    public ForceUpdate useCustomDialog(Boolean bl){
        this.useCustom = bl;
        return this;
    }
    public ForceUpdate listen(OnListen onListen){
        this.onListen = onListen;
        return this;
    }

    public void check(final Activity activity){

        if(loadJson==null||loadJson.getStatus()!= AsyncTask.Status.RUNNING){
            loadJson = new LoadJson(PreferenceManager.getDefaultSharedPreferences(activity));
            loadJson.execute(url);
        }

        Force force = getForce(activity);
        if(force==null){
            force = new Force();
            force.setAppCode(0);
            force.setFlag(Force.Flag.NOT_SHOW);
            if(onListen!=null)onListen.onSuccess(force);
            return;
        }

        if(BuildConfig.VERSION_CODE>=force.getAppCode()){
            if(onListen!=null)onListen.onSuccess(force);
            return;
        }


        if(useCustom){
            if(onListen!=null)onListen.onSuccess(force);
        } else if(force.getFlag()== Force.Flag.NOT_SHOW){
            if(onListen!=null)onListen.onSuccess(force);
        }else {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_update2);
            dialog.setCancelable(force.getFlag()== Force.Flag.SHOW_CANCELABLE);

            TextView tvTitle = dialog.findViewById(R.id.tvTitle);
            TextView tvMessage = dialog.findViewById(R.id.tvMessage);
            Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
            TextView btnLater = dialog.findViewById(R.id.btnLater);
            View vLater = dialog.findViewById(R.id.viewLater);

            if(force.getFlag()== Force.Flag.SHOW_NOT_CANCELABLE){
                btnLater.setVisibility(View.GONE);
                vLater.setVisibility(View.GONE);
            }

            tvTitle.setText(force.getTitle());
            tvMessage.setText(force.getMessage());
            final Force finalForce1 = force;
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToStore(activity, finalForce1.getUpdateLink());
                }
            });
            btnLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final Force finalForce = force;
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(onListen!=null)onListen.onSuccess(finalForce);
                }
            });

            dialog.show();
        }

    }

    private Force getForce(Activity activity){
        Force force = new Force();
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(activity);
        String json = preferenceManager.getString(KEY_SAVE,"");
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


    public interface OnListen{
        void onSuccess(Force force);
    }

    private void goToStore(Context context, String url) {
        if(url.contains("https://play.google.com/")){
            String MARKET_DETAILS_ID = "market://details?id=";
            String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
            String link = url.replace(PLAY_STORE_LINK,"");
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID +link)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK +link)));
            }
        }else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }

    }

}
