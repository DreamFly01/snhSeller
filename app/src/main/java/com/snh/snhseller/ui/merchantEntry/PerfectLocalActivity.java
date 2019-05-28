package com.snh.snhseller.ui.merchantEntry;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

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
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.AddImgAdapter;
import com.snh.snhseller.adapter.FootItemDelagateAdapter;
import com.snh.snhseller.adapter.MyMultiItemAdapter;
import com.snh.snhseller.bean.AreasBean;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.ImgDelagateBean;
import com.snh.snhseller.bean.StoreClassficationBean;
import com.snh.snhseller.bean.beanDao.AearBean;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.greendao.AearBeanDao;
import com.snh.snhseller.greendao.DaoMaster;
import com.snh.snhseller.greendao.DaoSession;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.Md5Utils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.WaterImgUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
public class PerfectLocalActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
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
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.tv_refresh_map)
    ImageView tvRefreshMap;
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
    @BindView(R.id.tv_commit)
    TextView tvCommit;


    private Bundle bundle;
    private Map<String, Object> personMap;
    private DialogUtils dialogUtils;
    private String ShopCategoryType;
    private String phone;
    private String shopType;
    private int flag;
    private int flag1;
    private LocationManager locationManager;
    private double Latitude;
    private double Longitude;
    private BaiduMap baiduMap;

    private LocationClient mLocationClient;

    private Map<String, Object> dataMap = new TreeMap<>();
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private CompressConfig compressConfig;
    private int type = 0;
    private List<Map<Object, Object>> mapList = new ArrayList<>();
    private List<String> pathList = new ArrayList<>();
    private Map<Object, Object> pathMap1 = new TreeMap<>();
    private Map<Object, Object> pathMap2 = new TreeMap<>();
    private Map<Object, Object> pathMap3 = new TreeMap<>();
    private Map<Object, Object> allMap = new TreeMap<>();
    private FootItemDelagateAdapter.OnMyAdapterClick onMyClick;
    private AddImgAdapter.DetelOnClick detelOnClick;

    private FootItemDelagateAdapter footAdapter;
    private AddImgAdapter addAdapter;
    private String money;
    private String name;
    private StoreClassficationBean typeBean;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfectlocal_layout);
        takePhoto = getTakePhoto();
        compressConfig = new CompressConfig.Builder().setMaxPixel(800).setMaxSize(2 * 1024 * 1024).create();
        bundle = getIntent().getExtras();
        if (null != bundle) {
            flag = bundle.getInt("flag", -1);
            flag1 = bundle.getInt("flag1", -1);
            personMap = (Map<String, Object>) bundle.getSerializable("data");
            phone = bundle.getString("phone");
            shopType = bundle.getString("shopType");
        }
        dialogUtils = new DialogUtils(this);
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("填写入驻信息");
        IsBang.setImmerHeard(this, rlHead);
//        tvPhone.setText((String) personMap.get("PhoneNumber"));
        getShopType();
        setMap();
        initReclyView();
        dialogUtils.initJson();
    }

    @Override
    public void setUpLisener() {

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


    private List<String> options1Items = new ArrayList<>();
    private String ShopTypeId;
    private String IsOverseas;
    private int position;

    private void showPickView() {
        options1Items.clear();
        if (null != typeData && typeData.size() > 0) {

            for (StoreClassficationBean bean : typeData) {
                options1Items.add(bean.Name);
            }

            OptionsPickerView pvOptions = new OptionsPickerBuilder(PerfectLocalActivity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String tx = options1Items.get(options1);
                    ShopTypeId = typeData.get(options1).Id + "";
                    position = options1;
                    tvType.setText(tx);
                }
            }).build();
            pvOptions.setPicker(options1Items);
            pvOptions.show();
        } else {
            showLongToast("加载类目失败，请稍后再试");
        }

    }

    private boolean check() {
        if (StrUtils.isEmpty(etName.getText().toString().trim())) {
            dialogUtils.noBtnDialog("店铺名不能为空");
            return false;
        }
        if (tvType.getText().toString().trim().equals("请选择类目")) {
            dialogUtils.noBtnDialog("请选择店铺类型");
            return false;
        }
        if (StrUtils.isEmpty(etAddress.getText().toString().trim())) {
            dialogUtils.noBtnDialog("请输入详细地址");
            return false;
        }
//        if (StrUtils.isEmpty(et18code.getText().toString().trim())) {
//            dialogUtils.noBtnDialog("请输入社会信用代码");
//            return false;
//        }
//        if (!(et18code.getText().toString().trim().length() == 18 || et18code.getText().toString().trim().length() == 15)) {
//            dialogUtils.noBtnDialog("请输入正确的社会信用代码");
//            return false;
//        }


        if (pathMap1.size() <= 0) {
            dialogUtils.noBtnDialog("请上传营业执照");
            return false;
        }
        return true;
    }

    String pswstr;

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
        dataMap.put("ShopName", etName.getText().toString().trim());
        dataMap.put("CompanyAddress", etAddress.getText().toString().trim());
//        dataMap.put("CreditCode", et18code.getText().toString().trim());
        dataMap.put("Longitude", Longitude);
        dataMap.put("Latitude", Latitude);
        dataMap.put("Province", Province);
        dataMap.put("City", City);
        dataMap.put("Area", Area);
        for (Object key:allMap.keySet()){
            mapList.add((Map<Object, Object>) allMap.get(key));
        }
        mapList1.addAll(mapList);
        dataMap.put("ImgUrlList", mapList1);
        dataMap.put("PhoneNumber", phone);
        dataMap.put("ShopType", ShopTypeId);
        pswstr = "snh" + phone.substring(phone.length() - 4, phone.length());
        System.out.println("psw" + pswstr);
        dataMap.put("Pwd", Md5Utils.md5(pswstr));
        dataMap.put("IsOverseas", IsOverseas);

        commitData();
    }

    List<StoreClassficationBean> typeData;


    private void getShopType() {

        addSubscription(RequestClient.GetShopType("1", this, new NetSubscriber<BaseResultBean<List<StoreClassficationBean>>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<List<StoreClassficationBean>> model) {
                typeData = model.data;
            }
        }));
    }


    private List<Map<Object, Object>> mapList1 = new ArrayList<>();


    private String province;
    private String city;
    private String district;
    private String street;
    private double latitude;
    private double longitude;
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
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
            district = getMyDistrict(latitude, longitude);
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

    //百度地图 获取的区县位置没有更新，所以采用Android 系统自带
    private String getMyDistrict(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> mAddresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!mAddresses.isEmpty()) {
                Address address = mAddresses.get(0);
//                SgLog.d("手机定位：" + address.getAdminArea() + "  " + address.getLocality() + "  " + address.getCountryName() + "  " + address.getSubLocality());
                return address.getSubLocality();
            }
        } catch (IOException e) {
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
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
//        result.getImage().get
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
                String path1 = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(path, this), path);
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
                    path = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(pathList.get(0), this), pathList.get(0));
                    pathList.clear();
                    pathList.add(path);
//                    allMap.put(1, pathMap1);
                    break;
                case 2:
                    Glide.with(this).load(WaterImgUtils.createWaterMaskCenter(pathList.get(0), this)).into(ivChose2);
                    path = WaterImgUtils.saveBitmap(this, WaterImgUtils.createWaterMaskCenter(pathList.get(0), this), pathList.get(0));
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


    private void upLoadImg(final List<String> datas) {
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
                        List<String> pathList = Arrays.asList(model.filepath.split(","));
                        for (int i = 0; i < datas.size(); i++) {
                            pathMap3 = new TreeMap<>();
                            pathMap3.put("ImgType", 5);
                            pathMap3.put("ImgSaveUrl", pathList.get(i));
                            allMap.put(3 + i, pathMap3);
                        }
                        setData();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

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
                bundle.putString("psw", pswstr);
                bundle.putString("pcUrl", (String) model.data);
                JumpUtils.dataJump(PerfectLocalActivity.this, CompleteActivity.class, bundle, false);
            }
        }));
    }

    @OnClick({R.id.heard_back, R.id.tv_address, R.id.iv_chose1, R.id.iv_chose2, R.id.tv_commit, R.id.tv_refresh_map, R.id.tv_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_address:
                showPickerView();
                break;
            case R.id.tv_type:
                showPickView();
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
                        setData();
                    }

                }

                break;
            case R.id.tv_refresh_map:
                setMap();
                break;
        }
    }

    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/"+ index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

}
