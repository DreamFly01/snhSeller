package com.snh.snhseller.ui.product;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.snh.library_base.db.DBManager;
import com.snh.library_base.utils.Contans;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.ShopGoodsTypeBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

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
    TextView et02;
    @BindView(R.id.et_03)
    EditText et03;
    @BindView(R.id.et_04)
    TextView et04;
    @BindView(R.id.et_05)
    EditText et05;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.et_06)
    EditText et06;

    private int type;
    private int isDel;
    private Bundle bundle;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CompressConfig compressConfig;
    private DialogUtils dialogUtils;
    private String path = "";
    private Map<String, Object> map = new TreeMap<>();

    private String CommTenantIcon, CommTenantName, CategoryName, UnitsTitle;
    private int Inventory, CommTenantId;
    private double Price, MarketPrice;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_editproduct_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            type = bundle.getInt("type");
//            bean = bundle.getParcelable("data");
            CommTenantIcon = bundle.getString("CommTenantIcon");
            CommTenantName = bundle.getString("CommTenantName");
            CategoryName = bundle.getString("CategoryName");
            UnitsTitle = bundle.getString("UnitsTitle");
            Inventory = bundle.getInt("Inventory");
            Price = bundle.getDouble("Price");
            MarketPrice = bundle.getDouble("MarketPrice");
            CommTenantId = bundle.getInt("CommTenantId");
            isDel = bundle.getInt("isDel");
        }
        takePhoto = getTakePhoto();
        compressConfig = new CompressConfig.Builder().setMaxPixel(800).setMaxSize(2 * 1024*1024).create();
        dialogUtils = new DialogUtils(this, this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        btnCommit.setText("立即发布");
        getTypeData();
        if (type == 1) {
            heardTitle.setText("添加商品");
        } else {
            heardTitle.setText("编辑商品");
        }
        if (isDel == 2) {
            heardTvMenu.setText("删除");
        }
        if (!StrUtils.isEmpty(CommTenantIcon)) {
            path = CommTenantIcon;
            ImageUtils.loadUrlImage(this, CommTenantIcon, ivLogo);
            et01.setText(CommTenantName);
            et01.setSelection(CommTenantName.length());
//            et01.setFocusable(false);
//            et01.setFocusableInTouchMode(false);
            et02.setText(CategoryName);
//            et02.setEnabled(false);
            et03.setText(Price + "");
            et03.setSelection((Price + "").length());
            et04.setText(UnitsTitle);
            et05.setText(Inventory + "");
            et05.setSelection((Inventory + "").length());
            et06.setText(MarketPrice + "");
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

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.btn_commit, R.id.et_02, R.id.et_04, R.id.heard_tv_menu})
    public void onClick(View view) {
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                requestPermision();

                break;
            case R.id.btn_commit:
                if (check()) {
                    setData();
                    if (type == 1) {
                        addProduct();
                    } else {
                        editProduct();
                    }
                }
                break;
            case R.id.et_02:
                showPickView(1);
                break;
            case R.id.et_04:
                showPickView(2);
                break;
            case R.id.heard_tv_menu:
                dialogUtils.twoBtnDialog("是否确定删除商品", new DialogUtils.ChoseClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        delProduct(CommTenantId, "删除商品成功");
                    }

                    @Override
                    public void onCancelClick(View v) {
                        dialogUtils.dismissDialog();
                    }
                }, false);
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

    private boolean check() {
        if (StrUtils.isEmpty(path)) {
            dialogUtils.noBtnDialog("请添加图片");
            return false;
        }
        if (StrUtils.isEmpty(et01.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入商品标题");
            return false;
        }
        if (StrUtils.isEmpty(et02.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请选择所属类目");
            return false;
        }
//        if (StrUtils.isEmpty(et03.getText().toString().trim())) {
//            dialogUtils.noBtnDialog("请输入会员价格");
//            return false;
//        }
        if (!StrUtils.isEmpty(et03.getText().toString().trim())) {
            if (Double.parseDouble(et03.getText().toString().trim()) <= 0) {
                dialogUtils.noBtnDialog("会员价格必须大于零");
                return false;
            }
        }
//        if (StrUtils.isEmpty(et06.getText().toString().trim())) {
//            dialogUtils.noBtnDialog("请输入市场价格");
//            return false;
//        }
        if (StrUtils.isEmpty(et03.getText().toString().trim()) && StrUtils.isEmpty(et06.getText().toString().trim())) {
            dialogUtils.noBtnDialog("会员价与市场价必须输入一个");
            return false;
        }
        if (!StrUtils.isEmpty(et06.getText().toString().trim())) {
            if (Double.parseDouble(et06.getText().toString().trim()) <= 0) {
                dialogUtils.noBtnDialog("市场价格必须大于零");
                return false;
            }
        }
        if (!StrUtils.isEmpty(et03.getText().toString().trim()) && !StrUtils.isEmpty(et06.getText().toString().trim())) {
            if (Double.parseDouble(et03.getText().toString().trim()) > Double.parseDouble(et06.getText().toString().trim())) {
                dialogUtils.noBtnDialog("会员价不能大于市场价");
                return false;
            }
        }
        if (StrUtils.isEmpty(et04.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请选择商品单位");
            return false;
        }
        if (StrUtils.isEmpty(et05.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入详细库存");
            return false;
        }


        return true;
    }

    private String typeId;

    private void setData() {
        map.put("SupplierId", DBManager.getInstance(this).getUseId());
        map.put("CommTenantIcon", path);
        map.put("CommTenantTitle", et01.getText().toString().trim());
        map.put("Category", typeId);
        if (StrUtils.isEmpty(et03.getText().toString().trim())) {
            map.put("Price", et06.getText().toString().trim());
        } else {
            map.put("Price", et03.getText().toString().trim());
        }
        map.put("UnitsTitle", et04.getText().toString().trim());
        map.put("Inventory", et05.getText().toString().trim());
        if (StrUtils.isEmpty(et06.getText().toString().trim())) {
            map.put("MarketPrice", et03.getText().toString().trim());
        } else {
            map.put("MarketPrice", et06.getText().toString().trim());
        }
        if (CommTenantId != 0) {
            map.put("GoodId", CommTenantId);
        }
    }

    private void addProduct() {
        addSubscription(RequestClient.AddProduct(map, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("新增商品成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        SPUtils.getInstance(EditProductActivity.this).savaBoolean(Contans.FRESH, true).commit();
                        EditProductActivity.this.finish();
                    }
                }, false);
            }
        }));
    }

    private void editProduct() {
        addSubscription(RequestClient.EditProduct(map, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("编辑商品成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        SPUtils.getInstance(EditProductActivity.this).savaBoolean(Contans.FRESH, true).commit();
                        EditProductActivity.this.finish();
                    }
                }, false);
            }
        }));
    }

    private List<String> options1Items = new ArrayList<>();

    private void showPickView(final int type) {
        options1Items.clear();
        if (type == 2) {
            options1Items.add("包");
            options1Items.add("袋");
            options1Items.add("瓶");
            options1Items.add("件");
            options1Items.add("支(只)");
            options1Items.add("Kg");
            options1Items.add("g");
            options1Items.add("L");
            options1Items.add("ml");
            options1Items.add("个");
            options1Items.add("桶");
            options1Items.add("提");
            options1Items.add("盒");
            options1Items.add("本");
            options1Items.add("套");
            options1Items.add("双");
            options1Items.add("条");
        } else {
            for (ShopGoodsTypeBean bean : typeBeans) {
                options1Items.add(bean.Name);
            }
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(EditProductActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                if (type == 1) {
                    typeId = typeBeans.get(options1).Id;
                    et02.setText(tx);
                } else {
                    et04.setText(tx);
                }
            }
        }).setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();
    }

    private List<ShopGoodsTypeBean> typeBeans;

    private void getTypeData() {
        addSubscription(RequestClient.GetShopGoodsType(DBManager.getInstance(this).getUserInfo().suppType, this, new NetSubscriber<BaseResultBean<List<ShopGoodsTypeBean>>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<List<ShopGoodsTypeBean>> model) {
                typeBeans = model.data;
            }
        }));
    }

    private void delProduct(int commtenanId, final String content) {
        addSubscription(RequestClient.DelProduct(commtenanId, this, new NetSubscriber<BaseResultBean>(this) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.dismissDialog();
                Toast.makeText(EditProductActivity.this, content, Toast.LENGTH_SHORT).show();
                EditProductActivity.this.finish();
            }
        }));
    }

    private void requestPermision() {

        String[] params = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, params)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, params, 10010);
            }

        } else {
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
        }
    }
}
