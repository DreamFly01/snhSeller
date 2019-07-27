package com.snh.snhseller.ui.merchantEntry;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
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
import com.snh.library_base.db.UserEntity;
import com.snh.library_base.utils.Contans;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.base.greendao.UserEntityDao;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.WaterImgUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/23<p>
 * <p>changeTime：2019/4/23<p>
 * <p>version：1<p>
 */
public class PerfectMyLocalActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_sfz_num)
    EditText etSfzNum;
    @BindView(R.id.tv_limit)
    TextView tvLimit;
    @BindView(R.id.iv_chose1)
    ImageView ivChose1;
    @BindView(R.id.iv_chose2)
    ImageView ivChose2;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private DialogUtils dialogUtils;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CompressConfig compressConfig;
    private int type = 0;
    private Bundle bundle;
    private Map<String, Object> dataMap = new HashMap<>();
    private List<Map<Object, Object>> mapList = new ArrayList<>();
    private List<String> pathList = new ArrayList<>();
    private Map<Object, Object> pathMap1 = new TreeMap<>();
    private Map<Object, Object> pathMap2 = new TreeMap<>();
    private Map<Object, Object> allMap = new TreeMap<>();

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfectmylocal_layout);
        dialogUtils = new DialogUtils(this);
        takePhoto = getTakePhoto();
        compressConfig = new CompressConfig.Builder().setMaxPixel(800).setMaxSize(2 * 1024 * 1024).create();
        bundle = getIntent().getExtras();
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("完善店铺信息");
        IsBang.setImmerHeard(this, rlHead);
    }

    @Override
    public void setUpLisener() {

    }

    private boolean check() {
        if (StrUtils.isEmpty(etName.getText().toString())) {
            dialogUtils.noBtnDialog("请填写真实姓名");
            return false;
        }
        if (StrUtils.isEmpty(etSfzNum.getText().toString())) {
            dialogUtils.noBtnDialog("请输入身份证号");
            return false;
        }
        if (tvLimit.getText().toString().equals("请选择身份证国徽面的期限")) {
            dialogUtils.noBtnDialog("请选择有效期");
            return false;
        }
        if (!StrUtils.isSfz(etSfzNum.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入正确的身份证号码");
            return false;
        }
        if (mapList.size() != 2) {
            dialogUtils.noBtnDialog("请完善身份证照片信息");
            return false;
        }
        return true;
    }

    @Override
    public void takeSuccess(TResult result) {
        pathList.clear();
        File file = new File(result.getImage().getOriginalPath());
        System.out.println("file.length:" + file.length());
        if (file.length() > 2 * 1024 * 1024) {
            pathList.add(result.getImage().getCompressPath());
        } else {
            pathList.add(result.getImage().getOriginalPath());
        }
        String path;
        switch (type) {
            case 1:
                Glide.with(this).load(WaterImgUtils.createWaterMaskCenter(pathList.get(0), this)).into(ivChose1);
                path = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(pathList.get(0), this), pathList.get(0));
                pathList.clear();
                pathList.add(path);
                break;
            case 2:
                Glide.with(this).load(WaterImgUtils.createWaterMaskCenter(pathList.get(0), this)).into(ivChose2);
                path = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(pathList.get(0), this), pathList.get(0));
                pathList.clear();
                pathList.add(path);
                break;
        }

        upLoadImg(pathList);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        showShortToast("选取失败");
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
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

    int mYear;
    int mMonth;
    int mDay;
    private void showPhotoPick() {

        long currenTime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();//
        c.setTimeInMillis(currenTime);

        mYear = c.get(Calendar.YEAR); // 获取当前年份
        mMonth = c.get(Calendar.MONTH);// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);
        Calendar currentDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(mYear - 1, mMonth, mMonth);
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


    private void upLoadImg(List<String> datas) {
        dialogUtils.dismissDialog();

        addSubscription(RequestClient.UpLoadFile(datas, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                StringBuffer str = new StringBuffer(model.filepath);

                String url = str.replace(0, 1, "h").toString().trim();
                switch (type) {
                    case 1:
                        pathMap1.put("ImgType", 1);
                        pathMap1.put("ImgSaveUrl", model.filepath);
                        allMap.put(1, pathMap1);
                        break;
                    case 2:
                        pathMap2.put("ImgType", 2);
                        pathMap2.put("ImgSaveUrl", model.filepath);
                        allMap.put(2, pathMap2);
                        break;
                }
            }
        }));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.heard_back, R.id.tv_limit, R.id.iv_chose1, R.id.iv_chose2, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_limit:
                showPickerView();
                break;
            case R.id.iv_chose1:
                type = 1;
                showPhotoPick();
                break;
            case R.id.iv_chose2:
                type = 2;
                showPhotoPick();
                break;
            case R.id.tv_commit:
                setDatas();
                if (check()) {
                    commit();
                }
                break;
        }
    }

    private void showPickerView() {//条件选择器初始化

        TimePickerView pvTime = new TimePickerBuilder(PerfectMyLocalActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tvLimit.setText(sdf.format(date));
            }
        })

                .build();
        pvTime.show();
    }

    private void setDatas() {
        mapList.clear();
        dataMap.put("RealName", etName.getText().toString().trim());
        dataMap.put("CardNo", etSfzNum.getText().toString().trim());
        dataMap.put("CardEndTime", tvLimit.getText().toString().trim());
        for (int i = 0; i < allMap.size(); i++) {
            mapList.add((Map<Object, Object>) allMap.get(i + 1));
        }
        dataMap.put("ImgUrlList", mapList);
    }

    private void commit() {
        addSubscription(RequestClient.UpdateSupplier(dataMap, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                UserEntityDao userEntityDao = DBManager.getDaoSession().getUserEntityDao();
                List<UserEntity> list = userEntityDao.queryBuilder().list();
                UserEntity userEntity = list.get(0);
                userEntityDao.deleteAll();
                userEntity.setContacts(etName.getText().toString().trim());
                userEntityDao.insert(userEntity);
                SPUtils.getInstance(PerfectMyLocalActivity.this).saveData(Contans.IS_FULL, "1");
                dialogUtils.simpleDialog("提交成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        PerfectMyLocalActivity.this.finish();
                    }
                }, false);
            }
        }));
    }
}
