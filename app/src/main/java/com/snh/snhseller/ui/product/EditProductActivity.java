package com.snh.snhseller.ui.product;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/22<p>
 * <p>changeTime：2019/2/22<p>
 * <p>version：1<p>
 */
public class EditProductActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
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
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.et_01)
    EditText et01;
    @BindView(R.id.et_02)
    EditText et02;
    @BindView(R.id.et_03)
    EditText et03;
    @BindView(R.id.et_04)
    EditText et04;
    @BindView(R.id.et_05)
    EditText et05;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private int type;
    private Bundle bundle;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CompressConfig compressConfig;
    private DialogUtils dialogUtils;
    private String path = "";
    private Map<String, Object> map = new TreeMap<>();
    private ProductBean bean;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_editproduct_layout);
        bundle = getIntent().getExtras();
        if(null != bundle){
            type = bundle.getInt("type");
            bean = bundle.getParcelable("data");
        }
        takePhoto = getTakePhoto();
        compressConfig = new CompressConfig.Builder().setMaxPixel(800).setMaxSize(2 * 1024).create();
        dialogUtils = new DialogUtils(this, this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead);
        btnCommit.setText("立即发布");
        if(type == 1){
            heardTitle.setText("添加商品");
        }else {
            heardTitle.setText("编辑商品");
        }
        if(null!=bean){
            path = bean.CommTenantIcon;
            ImageUtils.loadUrlImage(this,bean.CommTenantIcon,ivLogo);
            et01.setText(bean.CommTenantName);
            et02.setText(bean.CategoryName);
            et03.setText(bean.Price+"");
            et04.setText(bean.UnitsTitle);
            et05.setText(bean.Inventory+"");
        }
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

    @OnClick({R.id.heard_back, R.id.ll_01,R.id.btn_commit})
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
            case R.id.btn_commit:
                if(check()){
                    setData();
                    if(type == 1){
                        addProduct();
                    }else {
                        editProduct();
                    }
                }
                break;
        }
    }

    List<String> pathList = new ArrayList<>();

    @Override
    public void takeSuccess(TResult result) {
        ImageUtils.loadUrlImage(this, result.getImage().getOriginalPath(), ivLogo);
        dialogUtils.dismissDialog();
        File file = new File(result.getImage().getOriginalPath());
        if (file.length() > 2 * 1024 * 1024) {
            File file1 = new File(result.getImage().getCompressPath());
            pathList.add(result.getImage().getCompressPath());
        } else {
            pathList.add(result.getImage().getOriginalPath());
        }
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
                    path = model.filepath;
            }
        }));
    }

    private boolean check(){
        if(StrUtils.isEmpty(path)){
            dialogUtils.noBtnDialog("请添加图片");
            return false;
        }
        if(StrUtils.isEmpty(et01.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入商品标题");
            return false;
        }
        if(StrUtils.isEmpty(et02.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入所属类目");
            return false;
        }
        if(StrUtils.isEmpty(et03.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入零售价格");
            return false;
        }
        if(StrUtils.isEmpty(et04.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入商品单位");
            return false;
        }
        if(StrUtils.isEmpty(et05.getText().toString().trim())){
            dialogUtils.noBtnDialog("请输入详细库存");
            return false;
        }


        return true;
    }
    private void setData(){
        map.put("SupplierId",DBManager.getInstance(this).getUseId());
        map.put("CommTenantIcon",path);
        map.put("CommTenantTitle",et01.getText().toString().trim());
        map.put("Category",et02.getText().toString().trim());
        map.put("Price",et03.getText().toString().trim());
        map.put("UnitsTitle",et04.getText().toString().trim());
        map.put("Inventory",et05.getText().toString().trim());
        if(null != bean){
            map.put("GoodId",bean.CategoryId);
        }
    }
    private void addProduct(){
        addSubscription(RequestClient.AddProduct(map, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                    dialogUtils.simpleDialog("新增商品成功", new DialogUtils.ConfirmClickLisener() {
                        @Override
                        public void onConfirmClick(View v) {
                            EditProductActivity.this.finish();
                        }
                    },false);
            }
        }));
    }
    private void editProduct(){
        addSubscription(RequestClient.EditProduct(map, this, new NetSubscriber<BaseResultBean>(this,true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("编辑商品成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        EditProductActivity.this.finish();
                    }
                },false);
            }
        }));
    }
}
