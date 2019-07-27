package com.snh.snhseller.requestApi;

import android.content.Context;
import android.widget.Toast;

import com.snh.library_base.utils.Contans;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.module_netapi.requestApi.RetrofitProxy;
import com.snh.module_netapi.requestApi.RetrofitProxy1;
import com.snh.snhseller.BuildConfig;
import com.snh.snhseller.bean.AgreementBean;
import com.snh.snhseller.bean.BanksBean;
import com.snh.snhseller.bean.BusinessBean;
import com.snh.snhseller.bean.CostApplyBean;
import com.snh.snhseller.bean.DataBean;
import com.snh.snhseller.bean.InSwitchBean;
import com.snh.snhseller.bean.MoneyBean;
import com.snh.snhseller.bean.MyBankBean;
import com.snh.snhseller.bean.MyMsgBean;
import com.snh.snhseller.bean.NormsBean;
import com.snh.snhseller.bean.NoticeBean;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.bean.OrderDetailBean;
import com.snh.snhseller.bean.OrderDetailsBean;
import com.snh.snhseller.bean.PayWxBean;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.bean.RetailProductBean;
import com.snh.snhseller.bean.ShopGoodsTypeBean;
import com.snh.snhseller.bean.StoreClassficationBean;
import com.snh.snhseller.bean.UserBean;
import com.snh.snhseller.bean.WithdrawBean;
import com.snh.snhseller.bean.WithdrawDetailsBean;
import com.snh.snhseller.bean.salebean.ApplyBean;
import com.snh.snhseller.bean.salebean.CostDetialsBean;
import com.snh.snhseller.bean.salebean.OperationBean;
import com.snh.snhseller.bean.salebean.RecordBean;
import com.snh.snhseller.bean.salebean.SaleUserBean;
import com.snh.snhseller.bean.salebean.TypeBean;
import com.snh.snhseller.bean.salesBean.NoManagerBean;
import com.snh.snhseller.bean.salesBean.SalesCountBean;
import com.snh.snhseller.bean.salesBean.SalesRecodeBean;
import com.snh.snhseller.bean.salesBean.SalesmanBean;
import com.snh.snhseller.bean.supplierbean.AllSupplierBean;
import com.snh.snhseller.bean.supplierbean.GoodsBean;
import com.snh.snhseller.bean.supplierbean.SupplierBean;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.utils.NetworkUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;

import java.io.File;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class RequestClient {

    /**
     * 上传附件
     *
     * @param path
     * @param context
     * @param observer
     * @return
     */
    public static Subscription UpLoadFile(List<String> path, Context context, NetSubscriber<BaseResultBean> observer) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        try {
            for (int i = 0; i < path.size(); i++) {
//                File file = new File(UriUtils.getRealPathFromURI(context,path.get(i)));
                File file = new File(path.get(i));
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("MultipartFile", file.getName(), fileBody);
                parts.add(part);
            }
        } catch (Exception e) {
            Toast.makeText(context, "图片上传失败", Toast.LENGTH_SHORT).show();
        }

        return doRequest(RetrofitProxy1.init(context).create(RequestApi.class).UpLoadFile(parts), context, observer);
    }

    /**
     * 商家入驻获取短信验证码
     *
     * @param PhoneNumber
     * @param type        1 短信登录，
     *                    2 注册 ，
     *                    3 绑定，
     *                    4修改手机号码，
     *                    5 忘记密码
     * @param context
     * @param observer
     * @return
     */
    public static Subscription PostSms(String PhoneNumber, int type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumber", PhoneNumber);
        map.put("Type", type);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        PostSms(map),
                context, observer);
    }

    /**
     * @param PhoneNumber
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SmsCode(String PhoneNumber, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumber", PhoneNumber);
        map.put("Type", 2);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        SmsCode(map),
                context, observer);
    }

    /**
     * 商家入驻登录
     *
     * @param PhoneNumber
     * @param Code
     * @param context
     * @param observer
     * @return
     */
    public static Subscription LoginPhone(String PhoneNumber, String Code, String psw, Context context, NetSubscriber<BaseResultBean<UserBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumber", PhoneNumber);
        if (StrUtils.isEmpty(Code)) {
            map.put("Code", "");
        } else {
            map.put("Code", Code);
        }
        if (StrUtils.isEmpty(psw)) {
            map.put("Pwd", "");
        } else {
            map.put("Pwd", psw);
        }
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        LoginPhone(map),
                context, observer);
    }


    /**
     * 验证手机号码
     *
     * @param PhoneNumber
     * @param code
     * @param context
     * @param observer
     * @return
     */
    public static Subscription VerifyPhone(String PhoneNumber, String code, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumber", PhoneNumber);
        map.put("Code", code);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        VerifyPhone(map),
                context, observer);
    }

    /**
     * 手机号码修改密码
     *
     * @param PhoneNumber
     * @param NewPwd
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ForgetPwd(String PhoneNumber, String NewPwd, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumber", PhoneNumber);
        map.put("NewPwd", NewPwd);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        ForgetPwd(map),
                context, observer);
    }


    /**
     * 获取主营类目
     *
     * @param type
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetShopType(String type, Context context, NetSubscriber<BaseResultBean<List<StoreClassficationBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("type", type);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetShopType(map),
                context, observer);
    }

    /**
     * 获取入驻开关 第一个值是本地 第二个值是个人 第三个值是企业 ：1为启用 0为关闭
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetInSwitch(Context context, NetSubscriber<BaseResultBean<InSwitchBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetInSwitch(map),
                context, observer);
    }

    /**
     * 获取商品类型
     *
     * @return
     */
    public static Subscription GetShopGoodsType(String shopTypeId, Context context, NetSubscriber<BaseResultBean<List<ShopGoodsTypeBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("shopTypeId", shopTypeId);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetShopGoodsType(map),
                context, observer);
    }

    /**
     * 商家入驻登录
     *
     * @param PhoneNumber
     * @param Code
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MerchantLogin(String PhoneNumber, String Code, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumber", PhoneNumber);
        map.put("Code", Code);
        map.put("Type", 2);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MerchantLogin(map),
                context, observer);
    }

    /**
     * 当地入驻
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MerchantLocalEnter(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        map.put("InvitingCode", SPUtils.getInstance(context).getString(Contans.REFEREE));
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MerchantLocalEnter(map),
                context, observer);
    }

    /**
     * 个人入驻
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MerchantPersonEnter(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        map.put("InvitingCode", SPUtils.getInstance(context).getString(Contans.REFEREE));
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MerchantPersonEnter(map),
                context, observer);
    }

    /**
     * 企业入驻
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MerchantCompanyEntry(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        map.put("InvitingCode", SPUtils.getInstance(context).getString(Contans.REFEREE));
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MerchantCompanyEntry(map),
                context, observer);
    }

    /**
     * 获取协议
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetAgreementList(Context context, NetSubscriber<BaseResultBean<List<AgreementBean>>> observer) {
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetAgreementList(),
                context, observer);
    }


    /**
     * 获取商品列表
     *
     * @param orderStates
     * @param payStates
     * @param index
     * @param context
     * @param observer
     * @return
     */
    public static Subscription getOrderList(String orderNo,String orderStates, String payStates, int index, Context context, NetSubscriber<BaseResultBean<List<OrderBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        if (!StrUtils.isEmpty(orderStates)) {
            map.put("orderStates", orderStates);
        }
        if (!StrUtils.isEmpty(payStates)) {
            map.put("payStates", payStates);
        }
        if(!StrUtils.isEmpty(orderNo)){
            map.put("orderNo", orderNo);
        }
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageSize", 10);
        map.put("pageIndex", index);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        getOrderList(map),
                context, observer);
    }

    /**
     * 获取出库订单列表
     *
     * @param type
     * @param index
     * @param condition
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MyShipmentOrderList(int type, int index, String condition, Context context, NetSubscriber<BaseResultBean<List<OrderBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("type", type);
        if (!StrUtils.isEmpty(condition)) {
            map.put("condition", condition);
        }
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageSize", 10);
        map.put("pageIndex", index);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MyShipmentOrderList(map),
                context, observer);
    }

    /**
     * 获取进货订单
     *
     * @param type
     * @param index
     * @param condition
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MyStockOrderList(int type, int index, String condition, Context context, NetSubscriber<BaseResultBean<List<OrderBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("type", type);
        if (!StrUtils.isEmpty(condition)) {
            map.put("condition", condition);
        }
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageSize", 10);
        map.put("pageIndex", index);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MyStockOrderList(map),
                context, observer);
    }

    /**
     * 修改订单商品价格
     *
     * @param OrderId
     * @param Price
     * @param Freight
     * @param context
     * @param observer
     * @return
     */

    public static Subscription ChangePrice(int OrderId, String Price, String Freight, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("OrderId", OrderId);
        map.put("Price", Price);
        map.put("Freight", Freight);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        ChangePrice(map),
                context, observer);
    }


    /**
     * 提交发货
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ConfirmShipment(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        ConfirmShipment(map),
                context, observer);
    }

    /**
     * 获取店铺商品
     *
     * @param commType
     * @param pageIndex
     * @param commtenantName
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSaleOfGoods(int commType, int pageIndex, String commtenantName, Context context, NetSubscriber<BaseResultBean<List<ProductBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageSize", 10);
        if (StrUtils.isEmpty(commtenantName)) {
            map.put("commtenantName", "");
        } else {
            map.put("commtenantName", commtenantName);
        }
        if (commType == -2) {

        } else {
            map.put("commType", commType);
        }
        map.put("pageIndex", pageIndex);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetSaleOfGoods(map),
                context, observer);
    }

    /**
     * 新版获取店铺零售商品
     * @param commType
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSaleOfGoodsTwo(int commType,  Context context, NetSubscriber<BaseResultBean<List<RetailProductBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
//        map.put("pageSize", 10);
//        if (StrUtils.isEmpty(commtenantName)) {
//            map.put("commtenantName", "");
//        } else {
//            map.put("commtenantName", commtenantName);
//        }
            map.put("commType", commType);
//        map.put("pageIndex", pageIndex);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetSaleOfGoodsTwo(map),
                context, observer);
    }

    /**
     * 商品上下架
     *
     * @param CommtenantId
     * @param Type         1,上架   2，下架
     * @param context
     * @param observer
     * @return
     */
    public static Subscription UpOrDownProduct(int CommtenantId, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("CommtenantId", CommtenantId);
        map.put("Type", Type);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        UpOrDownProduct(map),
                context, observer);
    }

    /**
     * 删除商品
     *
     * @param CommtenantId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription DelProduct(int CommtenantId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("CommtenantId", CommtenantId);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        DelProduct(map),
                context, observer);
    }

    /**
     * 新增商品
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AddProduct(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        AddProduct(map),
                context, observer);
    }

    /**
     * 编辑商品
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription EditProduct(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        EditProduct(map),
                context, observer);
    }

    /**
     * 修改用户头像
     *
     * @param Icon
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ModifShopLogo(String Icon, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("Icon", Icon);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        ModifShopLogo(map),
                context, observer);
    }

    /**
     * 修改店铺信息
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ModifInfo(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        ModifInfo(map),
                context, observer);
    }

    /**
     * 修改店铺描述
     *
     * @param Describe
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ModifDesc(String Describe, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("Describe", Describe);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        ModifDesc(map),
                context, observer);
    }


    /**
     * 通过原密码修改新密码
     *
     * @param OldPwd
     * @param NewPwd
     * @param context
     * @param observer
     * @return
     */
    public static Subscription OldPwdToNewPwd(String OldPwd, String NewPwd, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("OldPwd", OldPwd);
        map.put("NewPwd", NewPwd);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        OldPwdToNewPwd(map),
                context, observer);
    }

    /**
     * 提交意见反馈
     *
     * @param Starlevel
     * @param ImgUrlList
     * @param Content
     * @param context
     * @param observer
     * @return
     */

    public static Subscription CommitFeedBack(String Starlevel, String ImgUrlList, String Content, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("Starlevel", Starlevel);
        map.put("ImgUrlList", ImgUrlList);
        map.put("Content", Content);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        CommitFeedBack(map),
                context, observer);
    }


    /**
     * 获取统计数据
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription DataStatistics(Context context, NetSubscriber<BaseResultBean<DataBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        DataStatistics(map),
                context, observer);
    }
    /**-------------------------------------------------------分割线-----------------------------------------------——————————————————————————————————————**/

    /**
     * 业务员账号密码登录
     *
     * @param username
     * @param psw
     * @param context
     * @param observer
     * @return
     */
    public static Subscription LoginSale(String username, String psw, Context context, NetSubscriber<BaseResultBean<SaleUserBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("UserName", username);
        map.put("PassWord", psw);

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        LoginSale(map),
                context, observer);
    }

    /**
     * 发送短信验证码
     *
     * @param PhoneNumer
     * @param Type       1.短信登录   2.注册   3.修改支付密码    4.修改手机号码
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SaleSendSms(String PhoneNumer, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumer", PhoneNumer);
        map.put("Type", Type);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        SaleSendSms(map),
                context, observer);
    }


    /**
     * 手机验证码登录
     *
     * @param PhoneNumer
     * @param Code
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SalePhoneLogin(String PhoneNumer, String Code, Context context, NetSubscriber<BaseResultBean<SaleUserBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("PhoneNumer", PhoneNumer);
        map.put("Code", Code);

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        SalePhoneLogin(map),
                context, observer);
    }

    /**
     * 修改密码
     *
     * @param OldPwd
     * @param NewPwd
     * @param context
     * @param observer
     * @return
     */

    public static Subscription ChangePsw(String OldPwd, String NewPwd, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("OldPwd", OldPwd);
        map.put("NewPwd", NewPwd);

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        OldPwdAlterNewPwd(map),
                context, observer);
    }


    /**
     * 获取我的供应商商户列表
     *
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription CommTenantList(int pageIndex, String condition, Context context, NetSubscriber<BaseResultBean<OperationBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        if (!StrUtils.isEmpty(condition)) {
            map.put("condition", condition);
        }
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        CommTenantList(map),
                context, observer);
    }

    /**
     * 获取商户列表
     *
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MyCommTenantList(int pageIndex, Context context, NetSubscriber<BaseResultBean<OperationBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);


        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MyManagerCommtenantList(map),
                context, observer);
    }

    /**
     * 添加成为我的商户
     *
     * @param CommtenantId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AddToMyCommtenant(int CommtenantId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("CommtenantId", CommtenantId);

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        AddToMyCommtenant(map),
                context, observer);
    }

    /**
     * 删除我的商户
     *
     * @param CommtenantId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription DelMyCommtenant(int CommtenantId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("CommtenantId", CommtenantId);

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        DelMyCommtenant(map),
                context, observer);
    }

    /**
     * 业务员打卡
     *
     * @param CommtenantId
     * @param Content
     * @param Type
     * @param context
     * @param observer
     * @return
     */
    public static Subscription RecordClockIn(int CommtenantId, String Content, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("CommtenantId", CommtenantId);
        if (StrUtils.isEmpty(Content)) {
            map.put("Content", "");
        } else {
            map.put("Content", Content);
        }
        map.put("Type", Type);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        RecordClockIn(map),
                context, observer);
    }

    /**
     * 获取打卡记录
     *
     * @param index
     * @param time
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetClockInRecord(int index, String time, Context context, NetSubscriber<BaseResultBean<List<RecordBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("pageIndex", index);
        map.put("pageSize", 20);
        map.put("time", time);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetClockInRecord(map),
                context, observer);
    }

    /**
     * 费用申请
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription PostApply(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        PostApply(map),
                context, observer);
    }

    /**
     * 获取类型列表
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetTypeList(Context context, NetSubscriber<BaseResultBean<List<TypeBean>>> observer) {
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetTypeList(),
                context, observer);
    }

    /**
     * 费用申请列表
     *
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetApplyList(int pageIndex, Context context, NetSubscriber<BaseResultBean<List<ApplyBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("salesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        map.put("pageSize", 10);
        map.put("pageIndex", pageIndex);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetApplyList(map),
                context, observer);
    }

    /**
     * 获取费用详情
     *
     * @param costId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetCostDetail(int costId, Context context, NetSubscriber<BaseResultBean<CostDetialsBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("costId", costId);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetCostDetail(map),
                context, observer);
    }

    /**
     * 重新提交
     *
     * @param costId
     * @param remark
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AgainCommit(int costId, String remark, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("costId", costId);
        map.put("remark", remark);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        AgainCommit(map),
                context, observer);
    }

    /**
     * 撤销申请
     *
     * @param costId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription RevocationApply(int costId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("costId", costId);
        map.put("salesmanId", DBManager.getInstance(context).getSaleInfo().SalesmanId);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        RevocationApply(map),
                context, observer);
    }

    /**---------------------------------------------------分销系统-----------------------------------------------------------*/


    /**
     * 获取我的商户/供应商列表
     *
     * @param pageIndex
     * @param type      1.为供应商 2.为商户  默认1
     * @param condition
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MySupplier(int pageIndex, int type, String condition, Context context, NetSubscriber<BaseResultBean<List<SupplierBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        if (!StrUtils.isEmpty(condition)) {
            map.put("condition", condition);
        }
        map.put("type", type);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        MySupplier(map),
                context, observer);
    }

    /**
     * 获取商品列表
     *
     * @param pageIndex
     * @param type      0.为不存在关系 1.为存在关系 默认为0
     * @param condition
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetGoodsList(int id, int pageIndex, int type, String condition, Context context, NetSubscriber<BaseResultBean<List<GoodsBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("mysupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("supplierId", id);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        if (!StrUtils.isEmpty(condition)) {
            map.put("condition", condition);
        }
        map.put("type", type);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        CommTenantDetail(map),
                context, observer);
    }


    /**
     * 获取商品详情
     *
     * @param id
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetGoodsDetail(int id, int supplierid, Context context, NetSubscriber<BaseResultBean<com.snh.snhseller.bean.supplierbean.ProductBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", supplierid);
        map.put("shopgoodsId", id);
        map.put("mysupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        ShopGoodsDetail(map),
                context, observer);
    }


    /**
     * 提交订单
     *
     * @param TargetSupplierId
     * @param ShopGoodsId
     * @param LeaveWord
     * @param PayMethod        货到付款传6
     * @param NormsList
     * @param context
     * @param observer
     * @return
     */
    public static Subscription PostOrder(int TargetSupplierId, int ShopGoodsId, double SumPrice, String LeaveWord, String PayMethod, List<NormsBean> NormsList,String CouponCodeIds, Context context, NetSubscriber<BaseResultBean<String>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("TargetSupplierId", TargetSupplierId);
        map.put("ShopGoodsId", ShopGoodsId);
        if (!StrUtils.isEmpty(LeaveWord)) {
            map.put("LeaveWord", LeaveWord);
        }
        if (!StrUtils.isEmpty(PayMethod)) {
            map.put("PayMethod", PayMethod);
        }
        if(!StrUtils.isEmpty(CouponCodeIds)){
            map.put("CouponCodeIds",CouponCodeIds);
        }
        map.put("SumPrice", SumPrice);
        map.put("NormsList", NormsList);
        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        PostOrder(map),
                context, observer);
    }

    /**
     * 获取账户余额
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetMoney(Context context, NetSubscriber<BaseResultBean<String>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        GetSupplierBalance(map),
                context, observer);
    }


    /**
     * 支付
     *
     * @param OrderNo
     * @param PayMethod 1.余额支付 2.微信+余额 3.支付宝+余额 4.微信 5.支付  6.其他
     * @param context
     * @param observer
     * @return
     */

    //含有微信支付的支付方式
    public static Subscription Pay(String OrderNo, int PayMethod, Context context, NetSubscriber<BaseResultBean<PayWxBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("OrderNo", OrderNo);
        map.put("PayMethod", PayMethod + "");

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        OnlinePay(map),
                context, observer);
    }

    //余额支付
    public static Subscription Pay1(String OrderNo, int PayMethod, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("OrderNo", OrderNo);
        map.put("PayMethod", PayMethod + "");

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        OnlinePay1(map),
                context, observer);
    }

    //含有支付宝的支付方式
    public static Subscription Pay2(String OrderNo, int PayMethod, Context context, NetSubscriber<BaseResultBean<String>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("OrderNo", OrderNo);
        map.put("PayMethod", PayMethod + "");

        return doRequest(RetrofitProxy.
                        init(context).create(RequestApi.class).
                        OnlinePay2(map),
                context, observer);
    }

    /**
     * 获取所有店铺列表
     *
     * @param pageIndex
     * @param condition
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SeekCommTenant(int pageIndex, String condition, int channel, Context context, NetSubscriber<BaseResultBean<List<AllSupplierBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 10);
        map.put("channel", channel);
        if (!StrUtils.isEmpty(condition)) {
            map.put("condition", condition);
        }
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .SeekCommTenant(map),
                context, observer);
    }


    /**
     * 申请成为我的供应商/商户
     *
     * @param TargetSupplierId
     * @param Type
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ApplyFor(int TargetSupplierId, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("TargetSupplierId", TargetSupplierId);
        map.put("Type", Type);


        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .ApplyFor(map),
                context, observer);
    }

    /**
     * @param TargetSupplierId
     * @param Type             1.删除我的供应商  2.删除我的商户
     * @param context
     * @param observer
     * @return
     */
    public static Subscription DelMySupplier(int TargetSupplierId, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("TargetSupplierId", TargetSupplierId);
        map.put("Type", Type);


        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .DelMySupplier(map),
                context, observer);
    }


    /**
     * 取消订单
     *
     * @param OrderId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription CancelOrder(int OrderId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("OrderId", OrderId);

        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .CancelOrder(map),
                context, observer);
    }

    /**
     * 确认收/发货
     *
     * @param OrderId
     * @param Type     1.确认发货2.确认收货
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AffirmSSStates(int OrderId, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("OrderId", OrderId);
        map.put("Type", Type);

        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .AffirmSSStates(map),
                context, observer);
    }

    /**
     * 提醒发货
     *
     * @param OrderId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription Remind(int OrderId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("OrderId", OrderId);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .Remind(map),
                context, observer);
    }

    /**
     * 获取订单详情
     *
     * @param OrderId
     * @param type
     * @param context
     * @param observer
     * @return
     */

    public static Subscription GetOrderDetail(int OrderId, int type, Context context, NetSubscriber<BaseResultBean<OrderDetailBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("orderId", OrderId);
        map.put("type", type);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetOrderDetail(map),
                context, observer);
    }

    /**
     * 获取用户订单详情
     *
     * @param orderid
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetMyOrderDetail(String orderid, Context context, NetSubscriber<BaseResultBean<OrderDetailsBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("orderid", orderid);
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).GetMyOrderDetail(map), context, observer);
    }

    /**
     * @param index
     * @param condition
     * @param filtrate  0.全部 1.已设置 2.未设置
     * @param context
     * @param observer
     * @return
     */

    public static Subscription MyCommodityList(int index, String condition, int filtrate, Context context, NetSubscriber<BaseResultBean<List<BusinessBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageIndex", index);
        map.put("pageSize", 20);
        if (!StrUtils.isEmpty(condition)) {
            map.put("condition", condition);
        }
        map.put("filtrate", filtrate);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .MyCommodityList(map),
                context, observer);
    }

    /**
     * 获取上商品规格列表
     *
     * @param shopgoodsId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetNormList(int shopgoodsId, Context context, NetSubscriber<BaseResultBean<List<com.snh.snhseller.bean.SkuBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("shopgoodsId", shopgoodsId);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetNormList(map),
                context, observer);
    }


    /**
     * 删除某个规格
     *
     * @param normId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription DelNorm(int normId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("normId", normId);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .DelNorm(map),
                context, observer);
    }

    /**
     * 编辑商品
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription EditeNorm(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .EditeNorm(map),
                context, observer);
    }

    /**
     * 新增规格
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AddNorm(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .AddNorm(map),
                context, observer);
    }

    /**
     * 获取申请消息列表
     *
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetApplyForList(int pageIndex, Context context, NetSubscriber<BaseResultBean<List<com.snh.snhseller.bean.ApplyBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageSize", 20);
        map.put("pageIndex", pageIndex);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetApplyForList(map),
                context, observer);
    }

    /**
     * 同意/拒绝申请
     *
     * @param ApplyId
     * @param Type     1.同意申请 2.拒绝申请
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ConsentApplyFor(int ApplyId, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("ApplyId", ApplyId);
        map.put("Type", Type);
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .ConsentApplyFor(map),
                context, observer);
    }

    /**
     * 获取我的信息
     *
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription MyMsg(int pageIndex, Context context, NetSubscriber<BaseResultBean<List<MyMsgBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .MyMsg(map),
                context, observer);
    }

/**-------------------------------------------商家业务员管理-------------------------------**/

    /**
     * 我的业务员列表
     *
     * @param pageIndex
     * @param condition
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SalesmanList(int pageIndex, String condition, Context context, NetSubscriber<BaseResultBean<List<SalesmanBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        map.put("condition", condition);
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .SalesmanList(map),
                context, observer);
    }

    /**
     * 删除我的业务员
     *
     * @param SalesmanId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription DelSalesman(int SalesmanId, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", SalesmanId);
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .DelSalesman(map),
                context, observer);
    }


    /**
     * 冻结/解冻业务员
     *
     * @param SalesmanId
     * @param Type
     * @param context
     * @param observer
     * @return
     */
    public static Subscription LockSalesman(int SalesmanId, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SalesmanId", SalesmanId);
        map.put("Type", Type);
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .LockSalesman(map),
                context, observer);
    }

    /**
     * 新增业务员
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AddSalesman(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .AddSalesman(map),
                context, observer);
    }

    /**
     * 编辑业务员
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription EditSalesman(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .EditSalesman(map),
                context, observer);
    }

    /**
     * 获取店铺管理状态
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription NoManagerCommtenantLis(Context context, NetSubscriber<BaseResultBean<List<NoManagerBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .NoManagerCommtenantLis(map),
                context, observer);
    }

    /**
     * 获取业务员打卡记录列表
     *
     * @param salesmanId
     * @param time
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ClockInRecord(int salesmanId, String time, int pageIndex, Context context, NetSubscriber<BaseResultBean<List<SalesRecodeBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("salesmanId", salesmanId);
        map.put("time", time);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .ClockInRecord(map),
                context, observer);
    }

    /**
     * 获取业务员统计列表
     *
     * @param time
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SalesClockInRecord(String time, int pageIndex, Context context, NetSubscriber<BaseResultBean<List<SalesCountBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("time", time);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .SalesClockInRecord(map),
                context, observer);
    }

    /**
     * @param type      1.待审批 2.已通过 3.已驳回
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSalesmanCostList(int type, int pageIndex, Context context, NetSubscriber<BaseResultBean<List<CostApplyBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("type", type);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetSalesmanCostList(map),
                context, observer);
    }


    /**
     * 驳回、同意费用审批
     *
     * @param CostId
     * @param SalesmanId
     * @param Type       1.同意申请 2.驳回申请
     * @param context
     * @param observer
     * @return
     */
    public static Subscription ConsentCostApply(int CostId, int SalesmanId, int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("CostId", CostId);
        map.put("SalesmanId", SalesmanId);
        map.put("Type", Type);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .ConsentCostApply(map),
                context, observer);
    }

    /**
     * 费用申请 添加备注
     *
     * @param CostId
     * @param SalesmanId
     * @param ReMark
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AddReMark(int CostId, int SalesmanId, String ReMark, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("CostId", CostId);
        map.put("SalesmanId", SalesmanId);
        map.put("ReMark", ReMark);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .AddReMark(map),
                context, observer);
    }

    /**
     * 设置是否支持货到付款
     *
     * @param Type
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SetPayMethod(int Type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("Type", Type);

        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .SetPayMethod(map),
                context, observer);
    }

    /**------------------------------账号资金----------------------------------------------**/

    /**
     * 获取资金
     *
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetAccountMoney(int pageIndex, Context context, NetSubscriber<BaseResultBean<MoneyBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetAccountMoney(map),
                context, observer);
    }

    /**
     * 获取银行卡信息
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSupplierBankCards(Context context, NetSubscriber<BaseResultBean<List<MyBankBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetSupplierBankCards(map),
                context, observer);
    }

    /**
     * 删除银行卡
     *
     * @param BankCardIds
     * @param context
     * @param observer
     * @return
     */
    public static Subscription DeleteSupplierBankCard(int BankCardIds, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String, Object> map = new TreeMap<>();
//        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("BankCardIds", BankCardIds);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .DeleteSupplierBankCard(map),
                context, observer);
    }

    /**
     * 获取银行接口
     *
     * @param BankCardIds
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetBanks(String BankCardIds, Context context, NetSubscriber<BaseResultBean<List<BanksBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
//        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        if (!StrUtils.isEmpty(BankCardIds)) {
            map.put("id", BankCardIds);
        }
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetBanks(map),
                context, observer);
    }

    /**
     * 商户添加银行卡
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AddSupplierBankCard(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .AddSupplierBankCard(map),
                context, observer);
    }

    /**
     * 提现
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription Withdrawal(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        map.put("SupplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .Withdrawal(map),
                context, observer);
    }

    /**
     * 获取资金明细
     *
     * @param data
     * @param type     默认0 0全部 1收入 2支出 3提现
     * @param index
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSupplierMoneyLog(String data, int type, int index,
                                                   Context context, NetSubscriber<BaseResultBean<WithdrawBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("date", data);
        map.put("type", type);
        map.put("pageIndex", index);
        map.put("pageSize", 20);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetSupplierMoneyLog(map),
                context, observer);
    }

    /**
     * 获取资金明细
     *
     * @param logId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSupplierMoneyDetails(String logId, Context context, NetSubscriber<BaseResultBean<WithdrawDetailsBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("logId", logId);

        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetSupplierMoneyDetails(map),
                context, observer);
    }

    /**
     * 登录
     *
     * @param UserName
     * @param PassWord
     * @param Code
     * @param context
     * @param observer
     * @return
     */
    public static Subscription Login(String UserName, String PassWord, String Code, Context context, NetSubscriber<BaseResultBean<com.snh.library_base.db.AllUserBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("UserName", UserName);
        if (StrUtils.isEmpty(PassWord)) {
            map.put("PassWord", "");
        } else {
            map.put("PassWord", PassWord);
        }
        if (StrUtils.isEmpty(Code)) {
            map.put("Code", "");
        } else {
            map.put("Code", Integer.parseInt(Code));
        }

        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .Login(map),
                context, observer);
    }

    /**
     * 获取消息通知
     *
     * @param type（1用户订单通知、2供销通知商户订单通知、3供销通知申请通知、4资金通知、5系统通知）
     * @param pageIndex
     * @param orderNo                                         订单编号用于查询
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSupplierNotice(int type, int pageIndex, String orderNo, Context context, NetSubscriber<BaseResultBean<List<NoticeBean>>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        map.put("type", type);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", 20);
        if (!StrUtils.isEmpty(orderNo)) {
            map.put("orderNo", orderNo);
        }
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetSupplierNotice(map),
                context, observer);
    }

    /**
     * 获取消息条数
     *
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSupplierNoticeUnreadCount(Context context, NetSubscriber<BaseResultBean<NoticeNumBean>> observer) {
        Map<String, Object> map = new TreeMap<>();
        map.put("supplierId", com.snh.library_base.db.DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .GetSupplierNoticeUnreadCount(map),
                context, observer);
    }

    /**
     * 完善资料
     *
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription UpdateSupplier(Map<String, Object> map, Context context, NetSubscriber<BaseResultBean> observer) {

        map.put("PhoneNumber", com.snh.library_base.db.DBManager.getInstance(context).getUserInfo().ContactsTel);
        return doRequest(RetrofitProxy
                        .init(context).create(RequestApi.class)
                        .UpdateSupplier(map),
                context, observer);
    }


    /***
     * 包装请求后再发起请求
     *
     * @param observable 请求
     * @param context    上下文
     * @param observer   观察者回调
     * @param <T>
     * @return
     */

    private static <T> Subscription doRequest(Observable observable, Context context, NetSubscriber<T> observer) {
        return observable
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new DealErroHttpFunc1(context, null))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     * 用来统一处理测试数据和错误处理
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private static class DealErroHttpFunc<T extends String> implements Func1<Throwable, T> {

        private T testResult;
        private Context context;

        public DealErroHttpFunc(Context context, T t) {
            testResult = t;
            this.context = context;
        }

        @Override
        public T call(Throwable throwable) {

            if (BuildConfig.DEBUG) {
//                Log.e("httpErro:", throwable.getMessage());
                //在开发模式下，测试数据默认无视网络情况
                return testResult == null ? createException(throwable) : testResult;
            } else {
                //Release版本
                return createException(throwable);
            }
        }

        private T createException(Throwable throwable) {

            if (!NetworkUtils.hasNetWork(context)) {
                if (throwable instanceof SocketTimeoutException) {
                    return (T) "您的网络不给力！请重试！";
                }
                if (throwable instanceof UnknownHostException || throwable instanceof HttpException) {
                    return (T) "在与服务器通讯过程中发生未知异常！";
                }
                return (T) throwable.getMessage();
            } else {
                return (T) "当前无法访问网络！";
            }

        }
    }

    private static class DealErroHttpFunc1<T extends BaseResultBean> implements Func1<Throwable, T> {

        private T testResult;
        private Context context;

        public DealErroHttpFunc1(Context context, T t) {
            testResult = t;
            this.context = context;
        }

        @Override
        public T call(Throwable throwable) {

            if (BuildConfig.DEBUG) {
//                Log.e("httpErro:", throwable.getMessage());
                //在开发模式下，测试数据默认无视网络情况
                return testResult == null ? createException(throwable) : testResult;
            } else {
                //Release版本
                return createException(throwable);
            }
        }

        private T createException(Throwable throwable) {

            if (!NetworkUtils.hasNetWork(context)) {
                if (throwable instanceof SocketTimeoutException) {
                    return (T) new BaseResultBean("您的网络不给力！请重试！", -99 + "", "");
                }
                if (throwable instanceof UnknownHostException || throwable instanceof HttpException) {
                    return (T) new BaseResultBean("在与服务器通讯过程中发生未知异常！", -99 + "", "");
                }
                return (T) new BaseResultBean(throwable.getMessage() + "", -99 + "", "");

            } else {
                return (T) new BaseResultBean("当前无法访问网络！", -99 + "", "");

            }

        }
    }
}
