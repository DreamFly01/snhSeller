package com.snh.snhseller.ui.home.account;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.beanDao.UserEntity;
import com.snh.snhseller.greendao.DaoSession;
import com.snh.snhseller.greendao.UserEntityDao;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/23<p>
 * <p>changeTime：2019/2/23<p>
 * <p>version：1<p>
 */
public class ShopInfoActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_shopName)
    TextView tvShopName;
    @BindView(R.id.tv_leimu)
    TextView tvLeimu;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    @BindView(R.id.tv_07)
    TextView tv07;
    @BindView(R.id.ll_05)
    LinearLayout ll05;
    private UserEntity useInfo;

    private Bundle bundle;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CompressConfig compressConfig;
    private DialogUtils dialogUtils;
    private String path = "";
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopinfo_layout);
        takePhoto = getTakePhoto();
        compressConfig = new CompressConfig.Builder().setMaxPixel(800).setMaxSize(2 * 1024).create();
        dialogUtils = new DialogUtils(this, this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("店铺信息");
        IsBang.setImmerHeard(this,rlHead);
        useInfo = DBManager.getInstance(this).getUserInfo();
        ImageUtils.loadUrlImage(this,useInfo.Logo,ivLogo);
        tvShopName.setText(useInfo.ShopName);
        tvLeimu.setText(useInfo.BusinessActivities);
        tvAddress.setText(useInfo.Address);
        tvUserName.setText(useInfo.Contacts);
        tvPhone.setText(useInfo.ContactsTel);
        tvEmail.setText(useInfo.ContactsQQ);
        tv07.setText(useInfo.Introduction);
    }

    @Override
    public void setUpLisener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02, R.id.ll_03, R.id.ll_04, R.id.ll_05})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                dialogUtils.headImgDialog(new DialogUtils.HeadImgChoseLisener() {
                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                    }

                    @Override
                    public void onPhotoClick(View v) {

                        String name = "takePhoto" + System.currentTimeMillis();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.Images.Media.TITLE, name);
                        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
                        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        takePhoto.onEnableCompress(compressConfig, true);
                        takePhoto.onPickFromCapture(uri);

                    }

                    @Override
                    public void onAlumClick(View v) {
                        takePhoto.onEnableCompress(compressConfig, true);
                        takePhoto.onPickFromGallery();

                    }
                }, true);
                break;
            case R.id.ll_02:
                bundle = new Bundle();
                bundle.putInt("type",1);
                bundle.putString("name",tvUserName.getText().toString().trim());
                JumpUtils.dataJump(this,ModifInfoActivity.class,bundle,false);
                break;
            case R.id.ll_03:
                bundle = new Bundle();
                bundle.putInt("type",2);
                bundle.putString("phone",tvPhone.getText().toString().trim());
                JumpUtils.dataJump(this,ModifInfoActivity.class,bundle,false);
                break;
            case R.id.ll_04:
                bundle = new Bundle();
                bundle.putInt("type",3);
                bundle.putString("email",useInfo.ContactsQQ);
                JumpUtils.dataJump(this,ModifInfoActivity.class,bundle,false);
                break;
            case R.id.ll_05:
                bundle = new Bundle();
                bundle.putInt("type",4);
                bundle.putString("desc",tv07.getText().toString().trim());
                JumpUtils.dataJump(this,ModifInfoActivity.class,bundle,false);
                break;
        }
    }

    List<String> pathList = new ArrayList<>();

    @Override
    public void takeSuccess(TResult result) {
        ImageUtils.loadUrlImage(this, result.getImage().getOriginalPath(), ivLogo);
        dialogUtils.dismissDialog();
        File file = new File(result.getImage().getOriginalPath());
//        if (file.length() > 2 * 1024 * 1024) {
//            File file1 = new File(result.getImage().getCompressPath());
//            pathList.add(result.getImage().getCompressPath());
//        } else {
            pathList.add(result.getImage().getOriginalPath());
//        }
        upLoadImg(pathList);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    private void upLoadImg(List<String> datas) {

        addSubscription(RequestClient.UpLoadFile(datas, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                StringBuffer sb = new StringBuffer(model.filepath);
                path = sb.replace(0,1,"h").toString();
                RequestClient.ModifShopLogo(path, ShopInfoActivity.this, new NetSubscriber<BaseResultBean>(ShopInfoActivity.this,true) {
                    @Override
                    public void onResultNext(BaseResultBean model) {
                        UserEntityDao userEntityDao = DBManager.getInstance(ShopInfoActivity.this).getDaoSession().getUserEntityDao();
                        UserEntity userEntity = userEntityDao.queryBuilder().build().list().get(0);
                        userEntityDao.deleteAll();
                        userEntity.Logo = path;
                        userEntityDao.insert(userEntity);
                        showShortToast("修改成功");
                    }
                });
            }
        }));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        useInfo = DBManager.getInstance(this).getUserInfo();
        ImageUtils.loadUrlImage(this,useInfo.Logo,ivLogo);
        tvShopName.setText(useInfo.ShopName);
        tvLeimu.setText(useInfo.BusinessActivities);
        tvAddress.setText(useInfo.Address);
        tvUserName.setText(useInfo.Contacts);
        tvPhone.setText(useInfo.ContactsTel);
        tvEmail.setText(useInfo.ContactsQQ);
        tv07.setText(useInfo.Introduction);
    }
}
