package com.snh.snhseller.requestApi;

import com.snh.snhseller.bean.AgreementBean;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.DataBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.bean.UserBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 *
 */
public interface RequestApi {
    /**
     * 上传文件
     * @param parts
     * @return
     */
    @Multipart
    @POST("webapi/fileUpload/postfile")
    Observable<BaseResultBean> UpLoadFile(@Part List<MultipartBody.Part> parts);
    /**
     * 商家入驻获取验证码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SupplierAuto/SmsSend")
    Observable<BaseResultBean> PostSms(@FieldMap Map<String,Object> params);

    /**
     * 商家入驻登录
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SupplierAuto/SupplierLogin")
    Observable<BaseResultBean<UserBean>> LoginPhone(@FieldMap Map<String,Object> params);

    /**
     * 验证手机号码
     * @param params
     * @return
     */
    @POST("SupplierAuto/VerifyPhoneNumber")
    Observable<BaseResultBean> VerifyPhone(@Body Map<String,Object> params);

    /**
     * 忘记密码修改密码
     * @param params
     * @return
     */
    @POST("SupplierAuto/ForgetPwd")
    Observable<BaseResultBean> ForgetPwd(@Body Map<String,Object> params);


    /**
     * 商家入驻登录
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SupplierEnter/PhoneLogin")
    Observable<BaseResultBean> MerchantLogin(@FieldMap Map<String,Object> params);

    /**
     * 当地入驻
     * @param params
     * @return
     */
    @POST("SupplierEnter/LocalEnter")
    Observable<BaseResultBean> MerchantLocalEnter(@Body Map<String,Object> params);

    /**
     * 个人入驻
     * @param params
     * @return
     */
    @POST("SupplierEnter/PersonageEnter")
    Observable<BaseResultBean> MerchantPersonEnter(@Body Map<String,Object> params);

    /**
     * 企业入驻
     * @param params
     * @return
     */
    @POST("SupplierEnter/EnterpriseEnter")
    Observable<BaseResultBean> MerchantCompanyEntry(@Body Map<String,Object> params);

    /**
     * 获取协议web
     * @return
     */
    @GET("webapi/SnhAgreement/GetAgreementList")
    Observable<BaseResultBean<List<AgreementBean>>> GetAgreementList();

    /**
     * 获取订单列表
     * @param params
     * @return
     */
    @GET("SupplierOrderManager/GetOrderList")
    Observable<BaseResultBean<List<OrderBean>>> getOrderList(@QueryMap Map<String,Object> params);


    /**
     * 修改订单商品价格
     * @param params
     * @return
     */
    @POST("SupplierOrderManager/OrderChangePrice")
    Observable<BaseResultBean> ChangePrice(@Body Map<String,Object> params);

    /**
     * 确认发货
     * @param params
     * @return
     */
    @POST("SupplierOrderManager/OrderShipments")
    Observable<BaseResultBean> ConfirmShipment(@Body Map<String,Object> params);

    /**
     * 获取店铺商品
     * @param params
     * @return
     */
    @GET("SupplierCommTenant/GetSaleOfGoods")
    Observable<BaseResultBean<List<ProductBean>>> GetSaleOfGoods(@QueryMap Map<String,Object> params);

    /**
     * 商品上下架
     * @param params
     * @return
     */
    @POST("SupplierCommTenant/IsPutaway")
    Observable<BaseResultBean> UpOrDownProduct(@Body Map<String,Object> params);

    /**
     * 删除商品
     * @param params
     * @return
     */
    @POST("SupplierCommTenant/DelCommTenantById")
    Observable<BaseResultBean> DelProduct(@Body Map<String,Object> params);

    /**
     * 新增商品
     * @param params
     * @return
     */
    @POST("SupplierCommTenant/AddCommTenant")
    Observable<BaseResultBean> AddProduct(@Body Map<String,Object> params);

    /**
     * 编辑商品
     * @param params
     * @return
     */
    @POST("SupplierCommTenant/EditCommTenant")
    Observable<BaseResultBean> EditProduct(@Body Map<String,Object> params);

    /**
     * 修改店铺logo
     * @param params
     * @return
     */
    @POST("SupplierUser/ModifSupplierIcon")
    Observable<BaseResultBean> ModifShopLogo(@Body Map<String,Object> params);

    /**
     * 修改店铺信息
     * @param params
     * @return
     */
    @POST("SupplierUser/SupplierBaseInfoModif")
    Observable<BaseResultBean> ModifInfo(@Body Map<String,Object> params);

    /**
     * 修改店铺描述
     * @param params
     * @return
     */
    @POST("SupplierUser/SupplierDescribeModif")
    Observable<BaseResultBean> ModifDesc(@Body Map<String,Object> params);

    /**
     * 通过原密码修改密码
     * @param params
     * @return
     */
    @POST("SupplierUser/OldPwdToNewPwd")
    Observable<BaseResultBean> OldPwdToNewPwd(@Body Map<String,Object> params);

    /**
     * 提交意见反馈
     * @param params
     * @return
     */
    @POST("SupplierUser/CommitFeedBack")
    Observable<BaseResultBean> CommitFeedBack(@Body Map<String,Object> params);

    /**
     * 获取数据统计
     * @param params
     * @return
     */
    @GET("SupplierDataStatistics/DataStatistics")
    Observable<BaseResultBean<DataBean>> DataStatistics(@QueryMap Map<String,Object> params);
}
