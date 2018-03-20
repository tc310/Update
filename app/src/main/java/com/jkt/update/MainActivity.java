package com.jkt.update;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jkt.update.services.UpdateIntentService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bnClick(View view) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("版本更新");
        dialog.setMessage("更新至新的版本");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beforeUpdateWork();
                    }
                });
        dialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        dialog.show();
    }

    private void beforeUpdateWork() {
        if (!isEnableNotification()) {
            showNotificationAsk();
            return;
        }
        toIntentServiceUpdate();

    }

    private void showNotificationAsk() {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("通知权限");
        dialog.setMessage("打开通知权限");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toSetting();
                    }
                });
        dialog.setNeutralButton("跳过", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toIntentServiceUpdate();
            }
        });
        dialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        dialog.show();
    }

    private boolean isEnableNotification() {
        boolean ret = true;
        try {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            ret = manager.areNotificationsEnabled();
        } catch (Exception e) {
            return true;
        }
        return ret;
    }

    private void toSetting() {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivity(localIntent);
        } catch (Exception e) {

        }
    }

    private void toIntentServiceUpdate() {
        Intent updateIntent = new Intent(this, UpdateIntentService.class);
        updateIntent.setAction(UpdateIntentService.ACTION_UPDATE);
        updateIntent.putExtra("appName", "update-1.0.1");
        //随便一个apk的url进行模拟
        updateIntent.putExtra("downUrl", "http://gdown.baidu.com/data/wisegame/38cbb321c273886e/YY_30086.apk");
        startService(updateIntent);
    }
}
