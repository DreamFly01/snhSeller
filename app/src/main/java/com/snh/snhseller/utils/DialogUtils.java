package com.snh.snhseller.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.AearsAdapter;
import com.snh.snhseller.adapter.ChoseAdapter;
import com.snh.snhseller.adapter.ChoseBankAdapter;
import com.snh.snhseller.adapter.CustomAdapter;
import com.snh.snhseller.adapter.MyChoseBankAdapter;
import com.snh.snhseller.bean.AreasBean;
import com.snh.snhseller.bean.BanksBean;
import com.snh.snhseller.bean.ChoseBean;
import com.snh.snhseller.bean.MyBankBean;
import com.snh.snhseller.bean.beanDao.AearBean;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.greendao.AearBeanDao;
import com.snh.snhseller.greendao.DaoMaster;
import com.snh.snhseller.greendao.DaoSession;
import com.snh.snhseller.wediget.RecycleViewDivider;

import org.greenrobot.greendao.async.AsyncSession;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class DialogUtils {
    private Activity mActivity;
    private Context mContext;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View v;
    private Dialog dialog;
    private boolean flag;
    private ConfirmClickLisener confirmClickLisener;
    private ChoseClickLisener choseClickLisener;
    private HeadImgChoseLisener headImgChoseLisener;
    private OnItemClick onItemClick;
    private EditClickLisener editClickLisener;
    private ConfirmBankLisener bankLisener;
    public static DialogUtils instance;
    private Address1Chose onAddress1hose;
    private ConfirmMyBankLisener myBankLisener;

    public interface Address1Chose {
        void onAddressChose(AreasBean bean);
    }

    public interface ConfirmClickLisener {
        void onConfirmClick(View v);
    }

    public interface ConfirmBankLisener {
        void onConfirmClick(BanksBean bean, int position);
    }

    public interface ConfirmMyBankLisener {
        void onConfirmClick(MyBankBean bean, int position);
    }

    public interface ChoseClickLisener {
        void onConfirmClick(View v);

        void onCancelClick(View v);
    }

    public interface EditClickLisener {
        void onCancelClick(View v);

        void onConfirmClick(View v, String content);
    }

    public interface HeadImgChoseLisener {
        void onCancelClick(View v);

        void onPhotoClick(View v);

        void onAlumClick(View v);
    }

    public interface OnItemClick {
        void onItemClick(View v, int position);
    }

    public static DialogUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (DialogUtils.class) {
                if (instance == null) {
                    instance = new DialogUtils(context);
                }
            }
        }
        return instance;
    }

    public DialogUtils(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    public DialogUtils(Context context) {
        this.mContext = context;
        this.mActivity = (Activity) context;
    }


    public DialogUtils noBtnDialog(String content) {

        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        if (null == DBManager.getInstance(mContext).getSaleInfo()) {
            v = inflater.inflate(R.layout.dialog_simple_layout, null);
        } else {
            v = inflater.inflate(R.layout.dialog_simple1_layout, null);
        }
        Button comfirm = (Button) v.findViewById(R.id.btn_comfirm);
        TextView txcontent = (TextView) v.findViewById(R.id.iv_dialog_content);
        txcontent.setText(content);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);

        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        return this;
    }

    /**
     * 简单弹窗
     *
     * @param content
     * @param flag
     */
    public DialogUtils simpleDialog(String content, final ConfirmClickLisener confirmClickLisener, boolean flag) {
        this.confirmClickLisener = confirmClickLisener;
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        if (null == DBManager.getInstance(mContext).getSaleInfo()) {
            v = inflater.inflate(R.layout.dialog_simple_layout, null);
        } else {
            v = inflater.inflate(R.layout.dialog_simple1_layout, null);
        }
        Button comfirm = (Button) v.findViewById(R.id.btn_comfirm);


        TextView txcontent = (TextView) v.findViewById(R.id.iv_dialog_content);
        txcontent.setText(content);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmClickLisener.onConfirmClick(view);
            }
        });


        return this;
    }

    /**
     * 有确定取消按钮弹窗
     *
     * @param content
     * @param choseClickLisener
     * @param flag
     * @return
     */
    public DialogUtils twoBtnDialog(String content, final ChoseClickLisener choseClickLisener, boolean flag) {
        this.choseClickLisener = choseClickLisener;
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        if (null == DBManager.getInstance(mContext).getSaleInfo()) {
            v = inflater.inflate(R.layout.dialog_two_btn_layout, null);
        } else {
            v = inflater.inflate(R.layout.dialog_two_btn1_layout, null);
        }

        Button comfirm = (Button) v.findViewById(R.id.btn_comfirm);
        Button cancle = (Button) v.findViewById(R.id.btn_cancel);

        if (content.equals("是否是海淘店")) {
            comfirm.setText("是");
            cancle.setText("否");
        }
        TextView txcontent = (TextView) v.findViewById(R.id.iv_dialog_content);
        txcontent.setText(content);


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseClickLisener.onConfirmClick(view);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseClickLisener.onCancelClick(view);
            }
        });

        return this;
    }

    /**
     * 有输入框的弹窗
     *
     * @param flag
     * @return
     */
    public DialogUtils editeDialog(String titlestr, final EditClickLisener editClickLisener, boolean flag) {
        this.editClickLisener = editClickLisener;
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        if (null == DBManager.getInstance(mContext).getSaleInfo()) {
            v = inflater.inflate(R.layout.dialog_edit_layout, null);
        } else {
            v = inflater.inflate(R.layout.dialog_edit1_layout, null);
        }
        Button comfirm = (Button) v.findViewById(R.id.btn_comfirm);
        Button cancle = (Button) v.findViewById(R.id.btn_cancel);
        final EditText editText = v.findViewById(R.id.et_content);
        TextView title = v.findViewById(R.id.tv_title);
        title.setText(titlestr);
//        TextView txcontent = (TextView) v.findViewById(R.id.iv_dialog_content);
//        txcontent.setText(content);


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClickLisener.onConfirmClick(view, editText.getText().toString().trim());
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClickLisener.onCancelClick(view);
            }
        });

        return this;
    }

    /**
     * 头像选择弹窗
     *
     * @param
     * @param headImgChoseLisener
     * @param flag
     */

    public DialogUtils headImgDialog(final HeadImgChoseLisener headImgChoseLisener, boolean flag) {
        this.headImgChoseLisener = headImgChoseLisener;
        //自定义样式使得弹窗铺满全屏
        builder = new AlertDialog.Builder(mContext, R.style.Dialog_FS);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_headimg_layout, null);
        TextView cancel = (TextView) v.findViewById(R.id.btn_cancel);
        TextView photo = (TextView) v.findViewById(R.id.btn_photograph);
        TextView album = (TextView) v.findViewById(R.id.btn_album);


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) display.getWidth();
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setContentView(v);
        dialog.getWindow().setWindowAnimations(R.style.MyDialogAlpha);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headImgChoseLisener.onPhotoClick(view);
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headImgChoseLisener.onAlumClick(view);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headImgChoseLisener.onCancelClick(view);
            }
        });
        return this;
    }

    /**
     * 自定义弹窗
     *
     * @param onItemClick
     * @param mTop
     * @param mRight
     * @param width
     * @param datas
     * @param imgList
     * @return
     */
    public DialogUtils customDialog(final OnItemClick onItemClick, int mTop, int mRight, int width, List<String> datas, List<Integer> imgList) {
        this.onItemClick = onItemClick;
        builder = new AlertDialog.Builder(mContext, R.style.dialog);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_custom_layout, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_dialog_custom);
        CustomAdapter adapter = new CustomAdapter(R.layout.item_dialog_custom, datas);
        adapter.setImgList(imgList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClick.onItemClick(view, position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        dialog = builder.create();
        dialog.show();
        dialog.setContentView(v);


        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = mRight - 40;//设置x坐标
        params.y = mTop;//设置y坐标
        params.width = 350;
        params.height = WRAP_CONTENT;
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        window.setAttributes(params);
        dialog.getWindow().setContentView(v, params);
        return this;
    }

    ChoseAdapter adapter;

    public DialogUtils choseDialog(final OnItemClick onItemClick, int mTop, List<ChoseBean> datas) {
        this.onItemClick = onItemClick;
        builder = new AlertDialog.Builder(mContext, R.style.dialog);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_chose_layout, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_dialog_custom);
        adapter = new ChoseAdapter(R.layout.item_dialog_chose_layout, datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClick.onItemClick(view, position);
            }
        });

        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayout.VERTICAL, R.drawable.line_2_gray));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        dialog = builder.create();
        dialog.show();
        dialog.setContentView(v);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = mTop;//设置y坐标
        params.width = MATCH_PARENT;
        params.height = WRAP_CONTENT;
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        window.setAttributes(params);
        dialog.getWindow().setContentView(v, params);
        return this;
    }

    public DialogUtils choseDataDialog(final OnItemClick onItemClick, int mTop, List<ChoseBean> datas) {
        this.onItemClick = onItemClick;
        builder = new AlertDialog.Builder(mContext, R.style.dialog);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_chose_layout, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_dialog_custom);
        adapter = new ChoseAdapter(R.layout.item_dialog_chose_layout, datas);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClick.onItemClick(view, position);
            }
        });

        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayout.VERTICAL, R.drawable.line_2_gray));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        dialog = builder.create();
        dialog.show();
        dialog.setContentView(v);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = mTop;//设置y坐标
        params.width = MATCH_PARENT;
        params.height = WRAP_CONTENT;
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        window.setAttributes(params);
        dialog.getWindow().setContentView(v, params);
        return this;
    }

    public void setChoseDatas(List<ChoseBean> datas) {
        adapter.setNewData(datas);
    }

    /**
     * 分享弹窗
     *
     * @return
     */
    public DialogUtils ShareDialog(final String title, final String name, final Bitmap bitmap) {
        this.confirmClickLisener = confirmClickLisener;
        //自定义样式使得弹窗铺满全屏
        builder = new AlertDialog.Builder(mContext, R.style.Dialog_FS);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_buttom_layout, null);
        TextView cancel = (TextView) v.findViewById(R.id.btn_cancel);
        ImageView friend = (ImageView) v.findViewById(R.id.share_friend);
        ImageView commends = (ImageView) v.findViewById(R.id.share_commends);

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) display.getWidth();
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setContentView(v);

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.ShareWechat(title, name, bitmap);
                dialog.dismiss();
            }
        });

        commends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.ShareWechatMom(title, name, bitmap);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return this;
    }

    private List<AreasBean> areasBeans = new ArrayList<>();
    List<AearBean> aear1 = new ArrayList<>();
    List<AearBean> aear2 = new ArrayList<>();
    List<AearBean> aear3 = new ArrayList<>();
    List<AreasBean> data1 = new ArrayList<>();
    List<AreasBean> data2 = new ArrayList<>();
    List<AreasBean> data3 = new ArrayList<>();
    AreasBean dataBean;

    LinearLayout ll01;
    LinearLayout ll02;
    LinearLayout ll03;
    RelativeLayout llDis;
    TextView tv01;
    TextView tv02;
    TextView tv03;
    ImageView iv01;
    ImageView iv02;
    ImageView iv03;
    TextView address1;
    TextView address2;

    public DialogUtils Address1Dialog(Address1Chose addressChose) {
        onAddress1hose = addressChose;
        DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(mContext).getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        final AearBeanDao aearBeanDao = daoSession.getAearBeanDao();
//        !SPUtils.getInstance(mContext).getBoolean(Contans.ADDRESS)
        if (!SPUtils.getInstance(mContext).getBoolean(Contans.ADDRESS)) {
            initJson();
        }
        data1.clear();

        aear1 = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.Level.eq("1")).list();
        for (int i = 0; i < aear1.size(); i++) {
            dataBean = new AreasBean();
            dataBean.AddressName = aear1.get(i).AddressName;
            dataBean.id = aear1.get(i).id;
            dataBean.Level = aear1.get(i).Level;
            dataBean.ParentID = aear1.get(i).ParentID;
            data1.add(dataBean);
        }


        builder = new AlertDialog.Builder(mContext, R.style.Dialog_FS);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_areas1_layout, null);
        ll01 = v.findViewById(R.id.ll_01);
        ll02 = v.findViewById(R.id.ll_02);
        ll03 = v.findViewById(R.id.ll_03);
        tv01 = v.findViewById(R.id.tv_01);
        tv02 = v.findViewById(R.id.tv_02);
        tv03 = v.findViewById(R.id.tv_03);
        iv01 = v.findViewById(R.id.iv_01);
        iv02 = v.findViewById(R.id.iv_02);
        iv03 = v.findViewById(R.id.iv_03);

        ll01.setEnabled(false);
        ll02.setEnabled(false);
        ll03.setEnabled(false);

        final AearsAdapter adapter1 = new AearsAdapter(R.layout.item_dialog_address_layout, null);
        final AearsAdapter adapter2 = new AearsAdapter(R.layout.item_dialog_address_layout, null);
        final AearsAdapter adapter3 = new AearsAdapter(R.layout.item_dialog_address_layout, null);
        final RecyclerView recyclerView1 = v.findViewById(R.id.recyclerView1);
        final RecyclerView recyclerView2 = v.findViewById(R.id.recyclerView2);
        final RecyclerView recyclerView3 = v.findViewById(R.id.recyclerView3);


        recyclerView1.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView1.setAdapter(adapter1);
        adapter1.setNewData(data1);
        recyclerView2.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView3.setAdapter(adapter3);

        dialog = builder.create();
        dialog.show();


        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) display.getWidth();
        layoutParams.height = (int) (display.getHeight() * 0.5);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setContentView(v);
        dialog.getWindow().setWindowAnimations(R.style.MyDialogAlpha);
//        ImmersionBar.with(mActivity, dialog).init();
        adapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                data2.clear();

                aear2 = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.ParentID.eq(aear1.get(position).id)).list();
                for (int i = 0; i < aear2.size(); i++) {
                    dataBean = new AreasBean();
                    dataBean.AddressName = aear2.get(i).AddressName;
                    dataBean.id = aear2.get(i).id;
                    dataBean.Level = aear2.get(i).Level;
                    dataBean.ParentID = aear2.get(i).ParentID;
                    data2.add(dataBean);
                    recyclerView1.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    recyclerView3.setVisibility(View.GONE);
                }
                adapter1.setIsChose(position);
                adapter2.setIsChose(-1);
                adapter2.setNewData(data2);
                setViewVisible(tv02, iv02);
                ll01.setEnabled(true);
                tv01.setText(data1.get(position).AddressName);
                tv02.setText("请选择市");
                tv03.setText("请选择区/县");
            }
        });
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                aear3 = aearBeanDao.queryBuilder().where(AearBeanDao.Properties.ParentID.eq(aear2.get(position).id)).list();
                data3.clear();

                for (int i = 0; i < aear3.size(); i++) {
                    dataBean = new AreasBean();
                    dataBean.AddressName = aear3.get(i).AddressName;
                    dataBean.id = aear3.get(i).id;
                    dataBean.Level = aear3.get(i).Level;
                    dataBean.ParentID = aear3.get(i).ParentID;
                    data3.add(dataBean);
                }
                adapter3.setNewData(data3);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.VISIBLE);
                setViewVisible(tv03, iv03);
                ll02.setEnabled(true);
                ll03.setEnabled(true);
                adapter2.setIsChose(position);

//                tv01.setText(data1.get(position).AddressName);
                tv02.setText(data2.get(position).AddressName);
                tv03.setText("请选择市");
            }


        });
        adapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onAddress1hose.onAddressChose(data3.get(position));
                dialog.dismiss();
            }
        });

        ll01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.GONE);
                setViewVisible(tv01, iv01);
            }
        });
        ll02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                recyclerView3.setVisibility(View.GONE);
                setViewVisible(tv02, iv02);
            }
        });
        ll03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.GONE);
                recyclerView3.setVisibility(View.VISIBLE);
                setViewVisible(tv03, iv03);
            }
        });
        return this;
    }

    private void setViewVisible(TextView tv, ImageView iv) {
        tv01.setTextColor(Color.parseColor("#1e1e1e"));
        tv02.setTextColor(Color.parseColor("#1e1e1e"));
        tv03.setTextColor(Color.parseColor("#1e1e1e"));
        iv01.setVisibility(View.INVISIBLE);
        iv02.setVisibility(View.INVISIBLE);
        iv03.setVisibility(View.INVISIBLE);

        tv.setTextColor(Color.parseColor("#fc1a4e"));
        iv.setVisibility(View.VISIBLE);

    }

    public void initJson() {
        try {
            List<AearBean> aearBeans = new ArrayList<>();
            DaoMaster daoMaster = new DaoMaster(DBManager.getInstance(mContext).getWritableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            final AearBeanDao aearBeanDao = daoSession.getAearBeanDao();
            String JsonData = new GetJsonDataUtil().getJson(mContext, "aears.json");//获取assets目录下的json文件数据
            aearBeans = JSON.parseArray(JsonData, AearBean.class);
            aearBeanDao.deleteAll();
            AsyncSession asyncSession = new AsyncSession(daoSession);
            final List<AearBean> finalAearBeans = aearBeans;
            asyncSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    aearBeanDao.insertOrReplaceInTx(finalAearBeans);
                }
            });

            SPUtils.getInstance(mContext).savaBoolean(Contans.ADDRESS, true).commit();
        } catch (Exception e) {
            Toast.makeText(mContext, "更新地址失败", Toast.LENGTH_SHORT).show();
        }

    }


    public DialogUtils bankDiaolog(final ConfirmBankLisener confirmClickLisener, final List<BanksBean> data, boolean flag) {
        bankLisener = confirmClickLisener;
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_bank_layout, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_bank);
        ChoseBankAdapter adapter = new ChoseBankAdapter(R.layout.item_chose_bank_layout, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        layoutParams.height = (int) (display.getHeight() * 0.5);

        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                bankLisener.onConfirmClick(data.get(position), position);
            }
        });
        return this;
    }

    public DialogUtils myBankDiaolog(final ConfirmMyBankLisener confirmClickLisener, final List<MyBankBean> data, boolean flag) {
        myBankLisener = confirmClickLisener;
        builder = new AlertDialog.Builder(mContext);
        inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.dialog_bank_layout, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_bank);
        MyChoseBankAdapter adapter = new MyChoseBankAdapter(R.layout.item_chose_bank_layout, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);


        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (display.getWidth() * 0.8);
        layoutParams.height = (int) (display.getHeight() * 0.5);

        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setContentView(v);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                myBankLisener.onConfirmClick(data.get(position), position);
            }
        });
        return this;
    }

    /**
     * 弹窗消失
     *
     * @return
     */
    public DialogUtils dismissDialog() {
        if (null != dialog) {

            dialog.dismiss();
        }

        return this;
    }

    public DialogUtils outSideIsDismiss(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
        dialog.setCancelable(flag);
        return this;
    }
}
