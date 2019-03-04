package com.snh.snhseller.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/25<p>
 * <p>changeTime：2019/2/25<p>
 * <p>version：1<p>
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private Gson gson;
    private DialogUtils dialogUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        dialogUtils = new DialogUtils(this);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, "", true);
        //将应用的appid注册到微信
        api.registerApp("");
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                Log.d("wxLog", "参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("wxLog:", "baseReq:" + gson.toJson(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("wxLog:", "baseResp:" + gson.toJson(baseResp));
        Log.e("wxLog:", "baseResp:" + baseResp.errStr + "," + baseResp.openId + "," + baseResp.transaction + "," + baseResp.errCode);
        String result = "";
        System.out.println("errcode:" + baseResp.errCode + "   " + baseResp.errStr);
        System.out.println("baseResp:" + gson.toJson(baseResp) + "   " + baseResp.openId);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
//                showMsg(1,result);
//                WxDataBean wxDataBean = gson.fromJson(gson.toJson(baseResp), WxDataBean.class);
//                Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
//                login(null, null, wxb);
//                RequestClient.GetWxData("https://api.weixin.qq.com/", wxDataBean.code, this, new WxNetSubscriber<WxBean>(this) {
//                    @Override
//                    public void onResultNext(WxBean wxBean) {
//                        if (!StrUtils.isEmpty(wxBean.openid)) {
//                            if (!StrUtils.isEmpty(SPUtils.getInstance(WXEntryActivity.this, UserEnum.USERINFO.getName()).getString(Contans.PHONENUMBER))) {
//                                bindWx(wxBean.openid);
//                            } else {
//                                login(null, null, wxBean.openid);
//                            }
//
//                        }
//                    }
//
//                });
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
//                showMsg(2,result);
//                Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
//                showMsg(1,result);
//                Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "发送返回";
//                showMsg(0,result);
//                Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

//    private void login(String name, String psw, final String code) {
//
//        RequestClient.Loging(name, psw, code, this, new NetSubscriber<String>(this, true) {
//            @Override
//            public void onResultNext(String model) {
//                if (!model.equals("")) {
//
//                    JSONObject jsonObject = JSON.parseObject(model);
//                    SPUtils.getInstance(WXEntryActivity.this, String.valueOf(UserEnum.USERINFO))
//                            .saveUserData(jsonObject.getString("TokenObject"), jsonObject.getString("DoctorID"), jsonObject.getBoolean("Audit"), jsonObject.getString("DivisionName"));
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, 2);
//                    Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                    JumpUtils.dataJump(WXEntryActivity.this, MainActivity.class, bundle, true);
//                } else {
//                    Bundle bundle1 = new Bundle();
//                    bundle1.putInt("swich",0);
//                    bundle1.putInt("flag",3);
//                    bundle1.putString("opendId",code);
//                    JumpUtils.dataJump(WXEntryActivity.this, AccountBindingInfoActivity.class,bundle1,true);
//                }
//
//            }
//
//            @Override
//            public void onResultErro(APIException erro) {
//                super.onResultErro(erro);
//                final DialogUtils dialogUtils = new DialogUtils(WXEntryActivity.this);
//                dialogUtils.simpleDialog(erro.msg, new DialogUtils.ConfirmClickLisener() {
//                    @Override
//                    public void onConfirmClick(View v) {
//                        dialogUtils.dismissDialog();
//                        WXEntryActivity.this.finish();
//                    }
//                }, false);
//            }
//        });
//        this.finish();
//    }
//
//    private void bindWx(final String code) {
//        RequestClient.BindWx(code, this, new NetSubscriber<String>(this, false) {
//            @Override
//            public void onResultNext(String model) {
//                dialogUtils.simpleDialog("绑定成功", new DialogUtils.ConfirmClickLisener() {
//                    @Override
//                    public void onConfirmClick(View v) {
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(MainActivity.SHOW_FRAGMENT_INDEX, 2);
//                        Toast.makeText(WXEntryActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
//                        JumpUtils.dataJump(WXEntryActivity.this, MainActivity.class, bundle, true);
//                    }
//                }, false);
//            }
//
//            @Override
//            public void onResultErro(APIException erro) {
//                super.onResultErro(erro);
//                Toast.makeText(WXEntryActivity.this, erro.msg, Toast.LENGTH_SHORT).show();
//                WXEntryActivity.this.finish();
//            }
//        });
//
//
//    }
}
