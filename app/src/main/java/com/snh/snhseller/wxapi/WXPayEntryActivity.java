package com.snh.snhseller.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.snh.snhseller.R;
import com.snh.snhseller.ui.home.supplier.PayActivity;
import com.snh.snhseller.utils.JumpUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/8<p>
 * <p>changeTime：2019/3/8<p>
 * <p>version：1<p>
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_pay_result)
    TextView tvPayResult;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, "wx223ecb204beddad4");//这里填入自己的微信APPID
        api.registerApp("wx223ecb204beddad4");
        api.handleIntent(getIntent(), this);
        ImmersionBar.with(this).titleBar(rlHead).statusBarColor(R.color.app_red).init();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        heardTitle.setText("支付结果");
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCord = baseResp.errCode;
            if (errCord == 0) {
                tvPayResult.setText("支付成功");
                Bundle bundle = new Bundle();
                bundle.putInt("cord",errCord);
                JumpUtils.dataJump(this, PayActivity.class,bundle,true);
            } else if (errCord == -2) {
                tvPayResult.setText("支付失败");
            } else {
                tvPayResult.setText("支付失败");
            }

            //这里接收到了返回的状态码可以进行相应的操作，如果不想在这个页面操作可以把状态码存在本地然后finish掉这个页面，这样就回到了你调起支付的那个页面
            //获取到你刚刚存到本地的状态码进行相应的操作就可以了
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }
}
