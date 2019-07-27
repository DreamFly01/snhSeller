package com.snh.snhseller.ui.merchantEntry;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
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
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.AddImgAdapter;
import com.snh.snhseller.adapter.FootItemDelagateAdapter;
import com.snh.snhseller.adapter.MyMultiItemAdapter;
import com.snh.snhseller.bean.AreasBean;
import com.snh.snhseller.bean.ImgDelagateBean;
import com.snh.snhseller.bean.StoreClassficationBean;
import com.snh.snhseller.bean.beanDao.AearBean;
import com.snh.snhseller.greendao.AearBeanDao;
import com.snh.snhseller.greendao.DaoMaster;
import com.snh.snhseller.greendao.DaoSession;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.WaterImgUtils;

import java.io.File;
import java.io.Serializable;
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
 * <p>creatTime：2019/1/23<p>
 * <p>changeTime：2019/1/23<p>
 * <p>version：1<p>
 */
public class PerfectCompanyThreeActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {


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
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.tv_jd)
    TextView tvJd;
    @BindView(R.id.tv_wd)
    TextView tvWd;
    @BindView(R.id.et_18code)
    EditText et18code;
    @BindView(R.id.iv_chose1)
    ImageView ivChose1;
    @BindView(R.id.iv_chose2)
    ImageView ivChose2;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.tv_refresh_map)
    ImageView tvRefreshMap;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private LocationClient mLocationListener;
    private double Latitude;
    private double Longitude;
    private BaiduMap baiduMap;

    private LocationClient mLocationClient;

    private Bundle bundle;
    private Map<String, Object> dataMap;

    private DialogUtils dialogUtils;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CompressConfig compressConfig;
    private int type = 0;
    private String phone;
    private List<Map<Object, Object>> mapList = new ArrayList<>();
    private List<String> pathList = new ArrayList<>();
    private Map<Object, Object> pathMap1 = new TreeMap<>();
    private Map<Object, Object> pathMap2 = new TreeMap<>();
    private Map<Object, Object> pathMap3 = new TreeMap<>();
    private Map<Object, Object> allMap = new TreeMap<>();
    private String ShopCategoryType;
    private FootItemDelagateAdapter.OnMyAdapterClick onMyClick;
    private AddImgAdapter.DetelOnClick detelOnClick;

    private FootItemDelagateAdapter footAdapter;
    private AddImgAdapter addAdapter;
    private int flag;
    private String psw;
    private String shopType;
    private String money;
    private String name;
    private StoreClassficationBean typeBean;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfectcompany3_layout);
        bundle = getIntent().getExtras();
        dialogUtils = new DialogUtils(this);
        takePhoto = getTakePhoto();
        compressConfig = new CompressConfig.Builder().setMaxPixel(800).setMaxSize(2 * 1024*1024).create();
        if (null != bundle) {
            ShopCategoryType = bundle.getString("ShopCategoryType");
            flag = bundle.getInt("flag");
            dataMap = (Map<String, Object>) bundle.getSerializable("data");
            psw = bundle.getString("psw");
            phone = bundle.getString("phone");
            shopType = bundle.getString("shopType");
            money = bundle.getString("money");
            name = bundle.getString("name");
            typeBean = bundle.getParcelable("typeBean");
        }
    }

    @Override
    public void setUpViews() {
        if (shopType.equals("3")) {
            heardTitle.setText("入驻本地信息完善");
        }
        if (shopType.equals("2")) {
            heardTitle.setText("入驻企业信息完善");
        }
        if (shopType.equals("1")) {
            heardTitle.setText("入驻个人信息完善");
        }
        IsBang.setImmerHeard(this, rlHead);
        checkPermission();
        setMap();
        initReclyView();
        mapList1 = (List<Map<Object, Object>>) dataMap.get("ImgUrlList");
        dialogUtils.initJson();
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

    private void setMap() {

        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        //定位初始化
        mLocationClient = new LocationClient(this);

//通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//设置locationClientOption
        mLocationClient.setLocOption(option);

//注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图层
        mLocationClient.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            showLongToast("必须同意才能使用");
                            finish();
                            return;
                        }
                    }
                    setMap();
                } else {
                    showLongToast("发生未知错误");
                    finish();
                }
                break;
        }
    }


    @OnClick({R.id.heard_back, R.id.tv_address, R.id.iv_chose1, R.id.iv_chose2, R.id.tv_commit, R.id.tv_refresh_map})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_address:
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
                if (dataCopy.size() > 0) {
                    for (int i = 0; i < dataCopy.size(); i++) {
                        paths.add(dataCopy.get(i).url);
                    }
                    type = 3;
                    upLoadImg(paths);
                } else {
                    if (check()) {
                        if (shopType.equals("2")) {
                            dialogUtils.twoBtnDialog("是否是海淘店", new DialogUtils.ChoseClickLisener() {
                                @Override
                                public void onConfirmClick(View v) {
                                    bundle = new Bundle();
                                    IsOverseas = "1";
                                    bundle.putString("money", typeBean.EHaiTao);
                                    setData();
                                    dialogUtils.dismissDialog();
                                    checkJump();

                                }

                                @Override
                                public void onCancelClick(View v) {
                                    bundle = new Bundle();
                                    IsOverseas = "0";
                                    bundle.putString("money", typeBean.ENoHaiTao);
                                    setData();
                                    dialogUtils.dismissDialog();
                                    checkJump();
                                }
                            }, true);
                        } else {
                            setData();
                        }
                    }

                }

                break;
            case R.id.tv_refresh_map:
                setMap();
                break;
        }
    }

    private void checkJump() {
        bundle.putSerializable("data", (Serializable) dataMap);
        bundle.putInt("flag", 2);
        bundle.putString("psw", psw);
        bundle.putString("shopType", shopType);
        bundle.putString("name", name);
        if (flag == 3) {
            commitData();
        } else {
            JumpUtils.dataJump(this, PerfectPersonThreeActivity.class, bundle, false);
        }
    }

    private List<Map<Object, Object>> mapList1 = new ArrayList<>();
    private String IsOverseas;

    private void setData() {
        if (StrUtils.isEmpty(Province) || StrUtils.isEmpty(City) || StrUtils.isEmpty(Area)) {
            try {
                AearBean aearBean = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.AddressName.eq(province)).unique();
                Province = aearBean.id;
                AearBean aearBean1 = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.ParentID.eq(aearBean.id), AearBeanDao.Properties.AddressName.eq(city)).unique();
                City = aearBean1.id;
                AearBean aearBean2 = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.ParentID.eq(aearBean1.id), AearBeanDao.Properties.AddressName.eq(district)).unique();
                Area = aearBean2.id;
            } catch (Exception e) {
                dialogUtils.noBtnDialog("当前定位未在平台覆盖区域，请手动选择！");
                return;
            }

        }
        mapList.clear();
        paths.clear();
        dataMap.put("ShopCategoryType", ShopCategoryType);
        dataMap.put("CompanyName", etName.getText().toString().trim());
        dataMap.put("CompanyAddress", etAddress.getText().toString().trim());
        dataMap.put("CreditCode", et18code.getText().toString().trim());
        dataMap.put("Longitude", Longitude);
        dataMap.put("Latitude", Latitude);
        dataMap.put("Province", Province);
        dataMap.put("City", City);
        dataMap.put("Area", Area);
        for (int i = 0; i < allMap.size(); i++) {
            mapList.add((Map<Object, Object>) allMap.get(i + 1));
        }
        if (shopType.equals("2")) {
            dataMap.put("IsOverseas", IsOverseas);
        }
        mapList1.addAll(mapList);
        dataMap.put("ImgUrlList", mapList1);
        checkJump();
    }

    private boolean check() {

        if (StrUtils.isEmpty(etAddress.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入详细地址");
            return false;
        }
        if (StrUtils.isEmpty(etName.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入公司名称");
            return false;
        }
        if (!(et18code.getText().toString().trim().length() == 18||et18code.getText().toString().trim().length() == 15)) {
            dialogUtils.noBtnDialog("请输入正确的社会信用代码");
            return false;
        }

        if (StrUtils.isEmpty(et18code.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入社会信用代码");
            return false;
        }
        if (pathMap1.size() <= 0) {
            dialogUtils.noBtnDialog("请上传营业执照");
            return false;
        }
        return true;
    }

    private String province;
    private String city;
    private String district;
    private String street;

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            province = location.getAddress().province;
            city = location.getAddress().city;
            district = location.getAddress().district;
            street = location.getAddress().street;
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            tvJd.setText("纬度：" + Latitude);
            tvWd.setText("经度：" + Longitude);
            tvAddress.setText(province + " " + city + " " + district);
            etAddress.setText(street);
            etAddress.setSelection(street.length());
        }
    }


    private List<ImgDelagateBean> data = new ArrayList<>();
    private List<ImgDelagateBean> dataCopy = new ArrayList<>();
    private List<String> paths = new ArrayList<>();
    MyMultiItemAdapter adapter;

    private void initReclyView() {
        //初始化添加材料+按钮
        ImgDelagateBean imgDelagateBean = new ImgDelagateBean();
        imgDelagateBean.isItem = false;
        imgDelagateBean.isDelet = false;
        data.add(imgDelagateBean);
        //图片材料添加
        adapter = new MyMultiItemAdapter(this, data);
        onMyClick = new FootItemDelagateAdapter.OnMyAdapterClick() {
            @Override
            public void onPhotoClick(View v) {
                type = 3;
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
            public void onAlumbClick(View v) {
                type = 3;
                takePhoto.onEnableCompress(compressConfig, true);
                takePhoto.onPickFromGallery();
            }
        };
        detelOnClick = new AddImgAdapter.DetelOnClick() {
            @Override
            public void detelClick(View v, int position) {

                data.remove(0);
                dataCopy.remove(position);
                adapter.upData(data);
            }

        };

        footAdapter = new FootItemDelagateAdapter(this, this, onMyClick);
        addAdapter = new AddImgAdapter(this, this, detelOnClick);
        addAdapter.setData(data, null);
        adapter.addItemViewDelegate(footAdapter);
        adapter.addItemViewDelegate(addAdapter);
        adapter.notifyDataSetChanged();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void takeSuccess(TResult result) {
        if (type == 3) {
            if (paths.size() < 5) {
                ImgDelagateBean imgDelagateBean = new ImgDelagateBean();
                imgDelagateBean.isItem = true;
                imgDelagateBean.isDelet = false;

                File file = new File(result.getImage().getOriginalPath());

                String path;
                if (file.length() > 2 * 1024 * 1024) {
                    path = result.getImage().getCompressPath();
                } else {
                    path = result.getImage().getOriginalPath();
                }
                String path1 = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(path, this),path);
                imgDelagateBean.url = path1;
                data.add(0, imgDelagateBean);
                dataCopy.add(imgDelagateBean);
                addAdapter.setData(data, dataCopy);
                adapter.notifyDataSetChanged();
            } else {
                dialogUtils.noBtnDialog("最多添加五张");
            }
        } else {

            pathList.clear();
            File file = new File(result.getImage().getOriginalPath());
            if (file.length() > 2 * 1024 * 1024) {
                pathList.add(result.getImage().getCompressPath());
            } else {
                pathList.add(result.getImage().getOriginalPath());
            }
            String path;
            switch (type) {
                case 1:
                    Glide.with(this).load(WaterImgUtils.createWaterMaskCenter(pathList.get(0), this)).into(ivChose1);
                    path = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(pathList.get(0), this),pathList.get(0));
                    pathList.clear();
                    pathList.add(path);
//                    allMap.put(1, pathMap1);
                    break;
                case 2:
                    Glide.with(this).load(WaterImgUtils.createWaterMaskCenter(pathList.get(0), this)).into(ivChose2);
                    path = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(pathList.get(0), this),pathList.get(0));
                    pathList.clear();
                    pathList.add(path);
//                    allMap.put(2, pathMap2);
                    break;
            }
            upLoadImg(pathList);
        }

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

    private void showPhotoPick() {
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
                        pathMap1.put("ImgType", 3);
                        pathMap1.put("ImgSaveUrl", model.filepath);
                        allMap.put(1, pathMap1);
                        break;
                    case 2:
                        pathMap2.put("ImgType", 4);
                        pathMap2.put("ImgSaveUrl", model.filepath);
                        allMap.put(2, pathMap2);
                        break;
                    case 3:
                        pathMap3.put("ImgType", 5);
                        pathMap3.put("ImgSaveUrl", model.filepath);
                        allMap.put(3, pathMap3);
                        setData();
                        if (check()) {
                            bundle = new Bundle();
                            bundle.putSerializable("data", (Serializable) dataMap);
                            bundle.putInt("flag", 2);
                            bundle.putString("psw", psw);
                            if (flag == 3) {
                                commitData();
                            } else {
                                JumpUtils.dataJump(PerfectCompanyThreeActivity.this, PerfectPersonThreeActivity.class, bundle, false);
                            }
                        }
                        break;
                }
            }
        }));

    }

    private String Province;
    private String City;
    private String Area;
    DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(this).getWritableDatabase());
    DaoSession daoSession = daoMaster.newSession();
    AearBeanDao aearBeanDao = daoSession.getAearBeanDao();

    private void showPickerView() {//条件选择器初始化


        dialogUtils.Address1Dialog(new DialogUtils.Address1Chose() {
            @Override
            public void onAddressChose(AreasBean bean) {
                AearBean aearBean = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.Id.eq(bean.ParentID)).unique();
                City = aearBean.id;
                AearBean aearBean1 = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.Id.eq(aearBean.ParentID)).unique();
                Province = aearBean1.id;
                Area = bean.id;
                tvAddress.setText(aearBean1.AddressName + " " + aearBean.AddressName + " " + bean.AddressName);
            }
        });
    }

    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;

    private void checkPermission() {
        List<String> permissionlist = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(PerfectCompanyThreeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionlist.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(PerfectCompanyThreeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionlist.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(PerfectCompanyThreeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionlist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionlist.isEmpty()) {
            String[] permissions = permissionlist.toArray(new String[permissionlist.size()]);
            ActivityCompat.requestPermissions(PerfectCompanyThreeActivity.this, permissions, 1);
        } else {
//            getLoction();
            setMap();
        }

    }

//    private void getLoction() {
//        //设置位置客户端选项
//        LocationClientOption option = new LocationClientOption();
//        //设置位置取得模式  （这里是指定为设备传感器也就是 GPS 定位）
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
//        //设置 间隔扫描的时间  也就是 位置时隔多长时间更新
////        option.setScanSpan(5000);
//        //设置 是否需要地址 （需要联网取得 百度提供的位置信息）
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setIsNeedAddress(true);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        // 实例化 LocationClient  传入的context 应该是全局的
//        mLocationListener = new LocationClient(getApplicationContext());
//        //将选项设置进去
//        mLocationListener.setLocOption(option);
//        //设置监听器 （这个方法有两个其中一个过时了，要使用 new BDAbstractLocationListener 的这个监听器）
//        mLocationListener.registerLocationListener(new BDAbstractLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                StringBuilder currentPosition = new StringBuilder();
//                //获取经纬度
//                currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
//                currentPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
//                Latitude = bdLocation.getLatitude();
//                Longitude = bdLocation.getLongitude();
//                tvJd.setText("纬度：" + Latitude);
//                tvWd.setText("经度：" + Longitude);
//
//                //获取定位方式
//
//                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
//                    currentPosition.append("GPS 定位");
//                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
//                    currentPosition.append("网络 定位");
//                }
//            }
//        });
//        //显示到Activity
//        mLocationListener.start();
//        setMap();
//    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();

    }

    private void commitData() {
        addSubscription(RequestClient.MerchantLocalEnter(dataMap, this, new NetSubscriber<BaseResultBean>(this, true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                bundle = new Bundle();
                bundle.putString("phone", (String) dataMap.get("PhoneNumber"));
                bundle.putString("psw", psw);
                bundle.putString("pcUrl", (String) model.data);
                JumpUtils.dataJump(PerfectCompanyThreeActivity.this, CompleteActivity.class, bundle, false);
            }
        }));
    }
}
