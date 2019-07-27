package com.snh.snhseller.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.alibaba.fastjson.JSONException;
import com.snh.library_base.db.DBManager;
import com.snh.library_base.utils.Contans;
import com.snh.snhseller.MainActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.ui.home.money.CapitalActivity;
import com.snh.snhseller.ui.loging.LogingActivity;
import com.snh.snhseller.ui.msg.SupplyNoticeActivity;
import com.snh.snhseller.ui.msg.SystemNoticeActivity;
import com.snh.snhseller.utils.BadgeUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;

import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/2<p>
 * <p>changeTime：2019/4/2<p>
 * <p>version：1<p>
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private int count ;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                count = Integer.parseInt(SPUtils.getInstance(context).getString(Contans.RED_COUNT));
                count+=1;
                SPUtils.getInstance(context).saveData(Contans.RED_COUNT,count+"");
                BadgeUtils.setBadgeCount(context,count, R.mipmap.logo);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                //打开自定义的Activity
//                Logger.d(TAG,printBundle(bundle));
                count = 0;
                SPUtils.getInstance(context).saveData(Contans.RED_COUNT,count+"");
                BadgeUtils.setBadgeCount(context,count, R.mipmap.logo);
                if (DBManager.getInstance(context).getUseId() != 0&&bundle.getString(JPushInterface.EXTRA_EXTRA).contains("Categories")) {
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                int Categories = json.getInt("Categories");
                Intent i;
                if (Categories == 101 || Categories == 102 || Categories == 201 || Categories == 202 || Categories == 203 || Categories == 204) {
                        bundle = new Bundle();
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, 1);
                        bundle.putInt("categories", Categories);
                    if (MainActivity.isForeground) {
                        i = new Intent(context, MainActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else {
                        MainActivity.setCategories(Categories);
                        MainActivity.setTabSelection(1);
                    }
                } else if (Categories == 301 || Categories == 302) {
                    if (SupplyNoticeActivity.isForeground) {
                        bundle = new Bundle();
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, 1);
                        i = new Intent(context, SupplyNoticeActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                } else if (Categories == 401 || Categories == 402 || Categories == 403) {
                    if (CapitalActivity.isForeground) {
                        bundle = new Bundle();
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, 1);
                        i = new Intent(context, CapitalActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }

                } else if (Categories == 505 || Categories == 506 || Categories == 507||Categories == 508) {
                    if (SystemNoticeActivity.isForeground) {
                        bundle = new Bundle();
                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, 1);
                        i = new Intent(context, SystemNoticeActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                } else {
                    i = new Intent(context, MainActivity.class);
                    i.putExtras(bundle);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }}else {
                    if (DBManager.getInstance(context).getUseId() != 0) {
                        bundle = new Bundle();
                        bundle.putInt(MainActivity.KEY_TITLE, 1);
                        Intent i = new Intent(context, MainActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else {
                        Intent i = new Intent(context, LogingActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                }


            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (StrUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }

            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }
}
