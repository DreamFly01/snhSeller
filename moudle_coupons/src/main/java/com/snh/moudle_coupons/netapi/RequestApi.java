package com.snh.moudle_coupons.netapi;


import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.moudle_coupons.bean.CouponsBean;
import com.snh.moudle_coupons.bean.CouponsNumBean;
import com.snh.moudle_coupons.bean.CouponsProductIdBean;
import com.snh.moudle_coupons.bean.RetailProductBean;
import com.snh.moudle_coupons.bean.SupplierCouponBean;

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
    @POST("api/ApiSource")
    Observable<BaseResultBean> UpLoadFile(@Part List<MultipartBody.Part> parts);
    /**
     * 获取验证码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SupplierAuto/SmsSend")
    Observable<BaseResultBean> PostSms(@FieldMap Map<String, Object> params);

    /**
     * 获取我的优惠券
     * @param params
     * @return
     */
    @GET("Coupons/GetCouponsList")
    Observable<BaseResultBean<List<CouponsBean>>> GetCouponsList(@QueryMap Map<String, Object> params);

    /**
     * 获取我的供应商优惠劵
     * @param params
     * @return
     */
    @GET("Coupons/GetSupplierCouponsList")
    Observable<BaseResultBean<List<CouponsBean>>> GetSupplierCouponsList(@QueryMap Map<String, Object> params);

    /**
     * 获取优惠劵总数
     * @param params
     * @return
     */
    @GET("Coupons/GetCouponsCount")
    Observable<BaseResultBean<CouponsNumBean>> GetCouponsCount(@QueryMap Map<String, Object> params);

    /**
     * 设置供应商优惠劵是否显示
     * @param params
     * @return
     */
    @POST("Coupons/UseSupplierCoupons")
    Observable<BaseResultBean> SetCouponsIsShow(@Body Map<String, Object> params);

    /**
     * 添加优惠劵
     * @param params
     * @return
     */
    @POST("Coupons/AddCoupons")
    Observable<BaseResultBean> AddCoupons(@Body Map<String, Object> params);

    /**
     * 获取该商户商品按类型分组
     * @param params
     * @return
     */
    @GET("Coupons/GetGoodsBySupplierId")
    Observable<BaseResultBean<List<RetailProductBean>>> GetGoodsBySupplierId(@QueryMap Map<String, Object> params);

    /**
     * 获取优惠券指定商品
     * @param params
     * @return
     */
    @GET("Coupons/GetCouponsGoods")
    Observable<BaseResultBean<List<CouponsProductIdBean>>> GetCouponsGoods(@QueryMap Map<String, Object> params);

    /**
     * 编辑优惠劵
     * @param params
     * @return
     */
    @POST("Coupons/EditCoupons")
    Observable<BaseResultBean> EditCoupons(@Body Map<String, Object> params);

    /**
     * 商户找供应商批发商品可使用的优惠券
     * @param params
     * @return
     */
    @GET("Coupons/GetSupplierCoupon")
    Observable<BaseResultBean<List<SupplierCouponBean>>> GetSupplierCoupon(@QueryMap Map<String, Object> params);
}
