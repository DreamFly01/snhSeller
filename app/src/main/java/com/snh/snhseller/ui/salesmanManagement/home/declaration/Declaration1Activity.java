package com.snh.snhseller.ui.salesmanManagement.home.declaration;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.salebean.TypeBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.salesmanManagement.BaseActivity;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.TimeUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：费用申请<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/5<p>
 * <p>changeTime：2019/3/5<p>
 * <p>version：1<p>
 */
public class Declaration1Activity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
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
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.ll_06)
    LinearLayout ll06;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.iv_03)
    ImageView iv03;
    @BindView(R.id.iv_del1)
    ImageView ivDel1;
    @BindView(R.id.iv_del2)
    ImageView ivDel2;
    @BindView(R.id.iv_del3)
    ImageView ivDel3;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private int type = 0;//照片选择第几张
    private int type1;//类型选择 1：申请类型  2：日期选择
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CompressConfig compressConfig;
    private DialogUtils dialogUtils;
    private String path;
    private Map<Integer, String> pathMap = new TreeMap<>();

    private List<String> options1Items = new ArrayList<>();

    private Map<String, Object> dataMap = new TreeMap<>();
    private int ApplyType = 0;
    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_declaration1_layout);
        takePhoto = getTakePhoto();
        compressConfig = new CompressConfig.Builder().setMaxPixel(800).setMaxSize(2 * 1024 * 1024).create();
        dialogUtils = new DialogUtils(this, this);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead, "#2E8AFF");
        heardTitle.setText("费用申请");
        btnCommit.setText("提交");
        getType();
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

    @OnClick({R.id.heard_back, R.id.tv_type, R.id.tv_time, R.id.iv_01, R.id.iv_02, R.id.iv_03, R.id.iv_del1, R.id.iv_del2, R.id.iv_del3, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_type:
                type1 = 1;
                showPickeView();
                break;
            case R.id.tv_time:
                type1 = 2;
                showPickeView();
                break;
            case R.id.iv_01:
                type = 1;
                showDialog();
                break;
            case R.id.iv_02:
                type = 2;
                showDialog();
                break;
            case R.id.iv_03:
                type = 3;
                showDialog();
                break;
            case R.id.iv_del1:
                pathMap.remove(1);
                Glide.with(this).load(R.drawable.up_img_bg).into(iv01);
                break;
            case R.id.iv_del2:
                pathMap.remove(2);
                Glide.with(this).load(R.drawable.up_img_bg).into(iv02);
                break;
            case R.id.iv_del3:
                pathMap.remove(3);
                Glide.with(this).load(R.drawable.up_img_bg).into(iv03);
                break;
            case R.id.btn_commit:
                if (check()) {
                    setData();
                    postData();
                }
                break;
        }
    }

    List<String> pathList = new ArrayList<>();

    @Override
    public void takeSuccess(TResult result) {
        pathList.clear();
        path = result.getImage().getCompressPath();
        switch (type) {
            case 1:
                ImageUtils.loadUrlImage(this, result.getImage().getOriginalPath(), iv01);
                break;
            case 2:
                ImageUtils.loadUrlImage(this, result.getImage().getOriginalPath(), iv02);
                break;
            case 3:
                ImageUtils.loadUrlImage(this, result.getImage().getOriginalPath(), iv03);
                break;
        }
//        ImageUtils.loadUrlCircleImage(this, result.getImage().getOriginalPath(), ivHeard);
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
        try {
            getTakePhoto().onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            Toast.makeText(this, "请重新选择照片", Toast.LENGTH_SHORT).show();
        }

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
                pathMap.put(type, model.filepath);
            }
        }));

    }

    private void showDialog() {
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


    private boolean check() {
        if (StrUtils.isEmpty(etName.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入项目名称");
            return false;
        }
        if (StrUtils.equals("请选择", tvType.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请选择申请类型");
            return false;
        }
        if (StrUtils.isEmpty(etMoney.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入预算");
            return false;
        }
        if (StrUtils.equals("请选择", tvTime.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请选择日期");
            return false;
        }
        return true;
    }

    private void setData() {
        dataMap.put("SalesmanId", DBManager.getInstance(this).getSaleInfo().SalesmanId);
        dataMap.put("ProjectName", etName.getText().toString().trim());
        dataMap.put("ApplyType", ApplyType);
        dataMap.put("Budget", etMoney.getText().toString().trim());
        dataMap.put("OccurDate", tvTime.getText().toString().trim());
        dataMap.put("ApplyTypeName", tvType.getText().toString().trim());
        if (!StrUtils.isEmpty(etDesc.getText().toString().trim())) {
            dataMap.put("Remark", etDesc.getText().toString().trim());
        }
        if (pathMap.size() > 0) {
            Collection<String> valueCollection = pathMap.values();
            List<String> list = new ArrayList<String>(valueCollection);
            dataMap.put("ExpenseVoucher", StringUtils.join(list, ",", 0, list.size()));
        }
    }

    private void postData() {
        addSubscription(RequestClient.PostApply(dataMap, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.simpleDialog("申请成功", new DialogUtils.ConfirmClickLisener() {
                    @Override
                    public void onConfirmClick(View v) {
                        dialogUtils.dismissDialog();
                        Declaration1Activity.this.finish();
                    }
                }, false);
            }
        }));
    }

    List<TypeBean> typeBeans = new ArrayList<>();

    private void getType() {
        addSubscription(RequestClient.GetTypeList(this, new NetSubscriber<BaseResultBean<List<TypeBean>>>(this) {
            @Override
            public void onResultNext(BaseResultBean<List<TypeBean>> model) {
                typeBeans = model.data;
                for (TypeBean bean : typeBeans) {
                    options1Items.add(bean.Name);
                }
            }
        }));
    }

    private void showPickeView() {
        if (type1 == 1) {
            OptionsPickerView pvOptions = new OptionsPickerBuilder(Declaration1Activity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String tx = options1Items.get(options1);
                    tvType.setText(tx);
                    for (TypeBean bean : typeBeans) {
                        if (StrUtils.equals(bean.Name, tx)) {
                            ApplyType = bean.Id;
                        }
                    }
                }
            }).setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).build();
            pvOptions.setPicker(options1Items);
            pvOptions.show();
        }
        if (type1 == 2) {
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
            currentDate.set(mYear, mMonth, mDay);
            //正确设置方式 原因：注意事项有说明
            startDate.set(mYear - 1, 0, 1);
            endDate.set(mYear + 1, 11, 1);
            TimePickerView pvTime = new TimePickerBuilder(Declaration1Activity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    tvTime.setText(TimeUtils.getDataString(date));
                }
            })
                    .setRangDate(startDate, endDate)
                    .setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).build();
            pvTime.setDate(currentDate);
            pvTime.show();
        }

    }
}
