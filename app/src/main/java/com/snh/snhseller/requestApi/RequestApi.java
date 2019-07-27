package com.snh.snhseller.requestApi;

import com.snh.library_base.db.AllUserBean;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.snhseller.bean.AgreementBean;
import com.snh.snhseller.bean.BanksBean;
import com.snh.snhseller.bean.BusinessBean;
import com.snh.snhseller.bean.CostApplyBean;
import com.snh.snhseller.bean.DataBean;
import com.snh.snhseller.bean.InSwitchBean;
import com.snh.snhseller.bean.MoneyBean;
import com.snh.snhseller.bean.MyBankBean;
import com.snh.snhseller.bean.MyMsgBean;
import com.snh.snhseller.bean.NoticeBean;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.bean.OrderDetailBean;
import com.snh.snhseller.bean.OrderDetailsBean;
import com.snh.snhseller.bean.PayWxBean;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.bean.RetailProductBean;
import com.snh.snhseller.bean.ShopGoodsTypeBean;
import com.snh.snhseller.bean.SkuBean;
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
    Observable<BaseResultBean> PostSms(@FieldMap Map<String,Object> params);


    /**
     * 商家入驻登录
     * @param params
     * @return
     */

    @POST("SupplierAuto/SupplierLogin")
    Observable<BaseResultBean<UserBean>> LoginPhone(@Body Map<String,Object> params);

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
     * 商家入驻获取短信验证码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SupplierEnter/SmsCode")
    Observable<BaseResultBean> SmsCode(@FieldMap Map<String,Object> params);

    /**
     * 获取商家入驻开关值
     * @param params
     * @return
     */
    @GET("webapi/SupplierEnter/GetInSwitch")
    Observable<BaseResultBean<InSwitchBean>> GetInSwitch(@QueryMap Map<String,Object> params);
    /**
     * 获取主营类目
     * @param params
     * @return
     */
    @GET("Shop/GetShopType")
    Observable<BaseResultBean<List<StoreClassficationBean>>> GetShopType(@QueryMap Map<String,Object> params);

    /**
     * 获取商品类型
     * @param params
     * @return
     */
    @GET("Shop/GetShopGoodsType")
    Observable<BaseResultBean<List<ShopGoodsTypeBean>>> GetShopGoodsType(@QueryMap Map<String,Object> params);
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
     * 获取我的订单列表
     * @param params
     * @return
     */
    @GET("SupplierOrderManager/GetOrderList")
    Observable<BaseResultBean<List<OrderBean>>> getOrderList(@QueryMap Map<String,Object> params);


    /**
     * 获取出库订单列表
     * @param params
     * @return
     */
    @GET("Distribution/MyShipmentOrderList")
    Observable<BaseResultBean<List<OrderBean>>> MyShipmentOrderList(@QueryMap Map<String,Object> params);

    /**
     * 获取进货订单
     * @param params
     * @return
     */
    @GET("Distribution/MyStockOrderList")
    Observable<BaseResultBean<List<OrderBean>>> MyStockOrderList(@QueryMap Map<String,Object> params);
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
     * 新版获取店铺零售商品
     * @param params
     * @return
     */
    @GET("SupplierCommTenant/GetSaleOfGoodsTwo")
    Observable<BaseResultBean<List<RetailProductBean>>> GetSaleOfGoodsTwo(@QueryMap Map<String,Object> params);
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

    /**---------------------------华丽的分割线--------------------------------**/
    /**---------------------------业务员接口---------------------------------**/

    /**
     * 业务员管理登录
     * @param params
     * @return
     */
    @POST("SalesmanUser/Login")
    Observable<BaseResultBean<SaleUserBean>> LoginSale(@Body Map<String,Object> params);

    /**
     * 获取验证码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SalesmanUser/sendphonecode")
    Observable<BaseResultBean> SaleSendSms(@FieldMap Map<String,Object> params);

    /**
     * 手机号码登录
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SalesmanUser/PhoneLogin")
    Observable<BaseResultBean<SaleUserBean>> SalePhoneLogin(@FieldMap Map<String,Object> params);

    /**
     * 修改密码
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("SalesmanUser/OldPwdAlterNewPwd")
    Observable<BaseResultBean<SaleUserBean>> OldPwdAlterNewPwd(@FieldMap Map<String,Object> params);

    /**
     * 供应商商户列表
     * @param params
     * @return
     */
    @GET("CommTenantManager/CommTenantList")
    Observable<BaseResultBean<OperationBean>> CommTenantList(@QueryMap Map<String,Object> params);


    /**
     * 添加成为我的管理商户
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("CommTenantManager/AddToMyCommtenant")
    Observable<BaseResultBean> AddToMyCommtenant(@FieldMap Map<String,Object> params);

    /**
     * 我的管理商户列表
     */
    @GET("CommTenantManager/MyManagerCommtenantList")
    Observable<BaseResultBean<OperationBean>> MyManagerCommtenantList(@QueryMap Map<String,Object> params);

    /**
     * 移除管理商户
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("CommTenantManager/DelMyCommtenant")
    Observable<BaseResultBean<SaleUserBean>> DelMyCommtenant(@FieldMap Map<String,Object> params);

    /**
     * 业务员打卡
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("CommTenantManager/RecordClockIn")
    Observable<BaseResultBean> RecordClockIn(@FieldMap Map<String,Object> params);

    /**
     * 业务员行程记录
     * @param params
     * @return
     */
    @POST("CommTenantManager/RecordRoute")
    Observable<BaseResultBean<SaleUserBean>> RecordRoute(@QueryMap Map<String,Object> params);


    /**
     * 我的行程记录
     * @param params
     * @return
     */
    @POST("Record/GetRouteRecordList")
    Observable<BaseResultBean<SaleUserBean>> GetRouteRecordList(@QueryMap Map<String,Object> params);

    /**
     * 打卡记录
     * @param params
     * @return
     */
    @GET("Record/GetClockInRecord")
    Observable<BaseResultBean<List<RecordBean>>> GetClockInRecord(@QueryMap Map<String,Object> params);

    /**
     * 费用申请
     * @param params
     * @return
     */

    @POST("SalesmanCost/CostApply")
    Observable<BaseResultBean> PostApply(@Body Map<String,Object> params);

    /**
     * 获取类型列表
     * @return
     */
    @GET("SalesmanCost/GetApplyTypeList")
    Observable<BaseResultBean<List<TypeBean>>> GetTypeList();

    /**
     * 获取费用列表
     * @param params
     * @return
     */
    @GET("SalesmanCost/CostApplyList")
    Observable<BaseResultBean<List<ApplyBean>>> GetApplyList(@QueryMap Map<String,Object> params);


    /**
     * 获取费用详情
     * @param params
     * @return
     */
    @GET("SalesmanCost/CostDetail")
    Observable<BaseResultBean<CostDetialsBean>> GetCostDetail(@QueryMap Map<String,Object> params);

    /**
     * 重新提交申请
     * @param params
     * @return
     */
    @POST("SalesmanCost/AnewCommit")
    Observable<BaseResultBean> AgainCommit(@Body Map<String,Object> params);

    /**
     * 撤销申请
     * @param params
     * @return
     */
    @POST("SalesmanCost/RevocationApply")
    Observable<BaseResultBean> RevocationApply(@Body Map<String,Object> params);

    /**---------------------------------------分销系统-------------------------------------**/

    /**
     * 获取我的供销商/商户
     * @param params
     * @return
     */
    @GET("webapi/Distribution/MySupplier")
    Observable<BaseResultBean<List<SupplierBean>>> MySupplier(@QueryMap Map<String,Object> params);

    /**
     * 获取商店商品列表
     * @param params
     * @return
     */
    @GET("webapi/Distribution/CommTenantDetail")
    Observable<BaseResultBean<List<GoodsBean>>> CommTenantDetail(@QueryMap Map<String,Object> params);

    /**
     * 获取商品详情
     * @param params
     * @return
     */
    @GET("webapi/Distribution/ShopGoodsDetail")
    Observable<BaseResultBean<com.snh.snhseller.bean.supplierbean.ProductBean>> ShopGoodsDetail(@QueryMap Map<String,Object> params);


    /**
     * 提交订单
     * @param params
     * @return
     */
    @POST("webapi/Distribution/PlaceOrder")
    Observable<BaseResultBean<String>> PostOrder(@Body Map<String,Object> params);

    /**
     * 获取账户余额
     * @param params
     * @return
     */
    @GET("webapi/Distribution/GetSupplierBalance")
    Observable<BaseResultBean<String>> GetSupplierBalance(@QueryMap Map<String,Object> params);

    /**
     * 订单支付
     * @param params
     * @return
     */
    @POST("paynew/supperpay")
    Observable<BaseResultBean<PayWxBean>> OnlinePay(@Body Map<String,Object> params);

    /**
     * 在线支付
     * @param params
     * @return
     */
    @POST("paynew/supperpay")
    Observable<BaseResultBean> OnlinePay1(@Body Map<String,Object> params);
    @POST("paynew/supperpay")
    Observable<BaseResultBean<String>> OnlinePay2(@Body Map<String,Object> params);

    /**
     * 获取所有商户列表
     * @param params
     * @return
     */
    @GET("webapi/Distribution/SeekCommTenant")
    Observable<BaseResultBean<List<AllSupplierBean>>> SeekCommTenant(@QueryMap Map<String,Object> params);

    /**
     * 申请成为我的供应商/商户
     * @param params
     * @return
     */
    @POST("webapi/Distribution/ApplyFor")
    Observable<BaseResultBean> ApplyFor(@Body Map<String,Object> params);

    /**
     * 删除我的供应商/商户
     * @param params
     * @return
     */
    @POST("webapi/Distribution/DelMySupplier")
    Observable<BaseResultBean> DelMySupplier(@Body Map<String,Object> params);

    /**
     * 取消订单
     * @param params
     * @return
     */
    @POST("webapi/Distribution/CancelOrder")
    Observable<BaseResultBean> CancelOrder(@Body Map<String,Object> params);

    /**
     * 确认收/发货
     * @param params
     * @return
     */
    @POST("webapi/Distribution/AffirmSSStates")
    Observable<BaseResultBean> AffirmSSStates(@Body Map<String,Object> params);

    /**
     * 提醒发货
     */
    @POST("webapi/Distribution/Remind")
    Observable<BaseResultBean> Remind(@Body Map<String,Object> params);

    /**
     * 订单详情
     * @param params
     * @return
     */
    @GET("webapi/Distribution/OrderDetail")
    Observable<BaseResultBean<OrderDetailBean>> GetOrderDetail(@QueryMap Map<String,Object> params);


    /**
     * 获取用户订单详情
     * @param params
     * @return
     */
    @GET("webapi/ShopOrder/GetOrderDetail")
    Observable<BaseResultBean<OrderDetailsBean>> GetMyOrderDetail(@QueryMap Map<String,Object> params);

    /**
     * 获取商家商品列表
     * @param params
     * @return
     */
    @GET("webapi/Distribution/MyCommodityList")
    Observable<BaseResultBean<List<BusinessBean>>> MyCommodityList(@QueryMap Map<String,Object> params);

    /**
     * 获取规格列表
     * @param params
     * @return
     */
    @GET("webapi/Distribution/NormList")
    Observable<BaseResultBean<List<SkuBean>>> GetNormList(@QueryMap Map<String,Object> params);

    /**
     * 删除某条规格
     * @param params
     * @return
     */
    @POST("Distribution/DelNorm")
    Observable<BaseResultBean> DelNorm(@Body Map<String,Object> params);

    /**
     * 编辑规格
     * @param params
     * @return
     */
    @POST("webapi/Distribution/EditNorm")
    Observable<BaseResultBean> EditeNorm(@Body Map<String,Object> params);

    /**
     * 新增规格
     * @param params
     * @return
     */
    @POST("webapi/Distribution/AddNorm")
    Observable<BaseResultBean> AddNorm(@Body Map<String,Object> params);

    /**
     * 获取申请通知列表
     * @param params
     * @return
     */
    @GET("webapi/Distribution/GetApplyForList")
    Observable<BaseResultBean<List<com.snh.snhseller.bean.ApplyBean>>> GetApplyForList(@QueryMap Map<String,Object> params);

    /**
     * 同意/拒绝申请
     * @param params
     * @return
     */
    @POST("webapi/Distribution/ConsentApplyFor")
    Observable<BaseResultBean> ConsentApplyFor(@Body Map<String,Object> params);

    /**
     * 获取我的消息列表
     * @param params
     * @return
     */
    @GET("webapi/Distribution/MyMsg")
    Observable<BaseResultBean<List<MyMsgBean>>> MyMsg(@QueryMap Map<String,Object> params);

    /**-----------------------------------------------业务员管理系统------------------------------------------------------------------**/
    /**
     * 我的业务员列表
     * @param params
     * @return
     */
    @GET("SupplierSalesman/SalesmanList")
    Observable<BaseResultBean<List<SalesmanBean>>> SalesmanList(@QueryMap Map<String,Object> params);

    /**
     * 删除我的业务员
     * @param params
     * @return
     */
    @POST("SupplierSalesman/DelSalesman")
    Observable<BaseResultBean> DelSalesman(@Body Map<String,Object> params);

    /**
     * 冻结/解冻业务员
     * @param params
     * @return
     */
    @POST("SupplierSalesman/LockSalesman")
    Observable<BaseResultBean> LockSalesman(@Body Map<String,Object> params);

    /**
     * 新增我的业务员
     * @param params
     * @return
     */
    @POST("SupplierSalesman/AddSalesman")
    Observable<BaseResultBean> AddSalesman(@Body Map<String,Object> params);

    /**
     * 编辑业务员
     * @param params
     * @return
     */
    @POST("SupplierSalesman/EditSalesman")
    Observable<BaseResultBean> EditSalesman(@Body Map<String,Object> params);

    /**
     * 商铺管理状态
     * @param params
     * @return
     */
    @GET("SupplierSalesman/NoManagerCommtenantList")
    Observable<BaseResultBean<List<NoManagerBean>>> NoManagerCommtenantLis(@QueryMap Map<String,Object> params);

    /**
     * 业务员打卡记录
     * @param params
     * @return
     */
    @GET("SupplierSalesman/ClockInRecord")
    Observable<BaseResultBean<List<SalesRecodeBean>>> ClockInRecord(@QueryMap Map<String,Object> params);

    /**
     * 业务员打卡统计
     * @param params
     * @return
     */
    @GET("SupplierSalesman/StatisticsSalesmanClockIn")
    Observable<BaseResultBean<List<SalesCountBean>>> SalesClockInRecord(@QueryMap Map<String,Object> params);

    /**---------------------------------------费用审批--------------------------------------**/

    /**
     * 获取费用 列表
     * @param params
     * @return
     */
    @GET("SupplierSalesman/GetSalesmanCostList")
    Observable<BaseResultBean<List<CostApplyBean>>> GetSalesmanCostList(@QueryMap Map<String,Object> params);

    /**
     *
     * @param params
     * @return
     */
    @POST("SupplierSalesman/ConsentCostApply")
    Observable<BaseResultBean> ConsentCostApply(@Body Map<String,Object> params);

    /**
     * 费用申请添加备注
     * @param params
     * @return
     */
    @POST("SupplierSalesman/AddReMark")
    Observable<BaseResultBean> AddReMark(@Body Map<String,Object> params);

    /**
     * 设置是否支持货到付款
     * @param params
     * @return
     */
    @POST("SupplierUser/SetPayMethod")
    Observable<BaseResultBean> SetPayMethod(@Body Map<String,Object> params);
    /**------------------------------账号资金----------------------------------------------**/

    /**
     * 获取账号资金信息
     * @param params
     * @return
     */
    @GET("SupplierMoney/GetAccountMoney")
    Observable<BaseResultBean<MoneyBean>> GetAccountMoney(@QueryMap Map<String,Object> params);

    /**
     * 获取银行卡信息
     * @param params
     * @return
     */
    @GET("SupplierMoney/GetSupplierBankCards")
    Observable<BaseResultBean<List<MyBankBean>>> GetSupplierBankCards(@QueryMap Map<String,Object> params);

    /**
     * 删除银行卡
     * @param params
     * @return
     */
    @POST("SupplierMoney/DeleteSupplierBankCard")
    Observable<BaseResultBean> DeleteSupplierBankCard(@Body Map<String,Object> params);

    /**
     * 获取银行接口
     * @param params
     * @return
     */
    @GET("SupplierMoney/GetBanks")
    Observable<BaseResultBean<List<BanksBean>>> GetBanks(@QueryMap Map<String,Object> params);

    /**
     * 商家新增银行卡
     * @param params
     * @return
     */
    @POST("SupplierMoney/AddSupplierBankCard")
    Observable<BaseResultBean> AddSupplierBankCard(@Body Map<String,Object> params);

    /**
     * 提现
     * @param params
     * @return
     */
    @POST("SupplierMoney/Withdrawal")
    Observable<BaseResultBean> Withdrawal(@Body Map<String,Object> params);

    /**
     * 获取资金明细
     * @param params
     * @return
     */
    @GET("SupplierMoney/GetSupplierMoneyLog")
    Observable<BaseResultBean<WithdrawBean>> GetSupplierMoneyLog(@QueryMap Map<String,Object> params);

    /**
     * 获取资金明细
     * @param params
     * @return
     */
    @GET("SupplierMoney/GetSupplierMoneyLogDetail")
    Observable<BaseResultBean<WithdrawDetailsBean>> GetSupplierMoneyDetails(@QueryMap Map<String,Object> params);

    /**
     * 登陆
     * @param params
     * @return
     */
    @POST("SupplierAuto/SupplierSalesmanLogin")
    Observable<BaseResultBean<AllUserBean>> Login(@Body Map<String,Object> params);

    /**
     * 获取通知
     * @param params
     * @return
     */
    @GET("webapi/Message/GetSupplierNotice")
    Observable<BaseResultBean<List<NoticeBean>>> GetSupplierNotice(@QueryMap Map<String ,Object> params);

    /**
     * 获取未读信息条数
     * @param params
     * @return
     */
    @GET("webapi/Message/GetSupplierNoticeUnreadCount")
        Observable<BaseResultBean<NoticeNumBean>> GetSupplierNoticeUnreadCount(@QueryMap Map<String,Object> params);

    /**
     * 完善资料
     * @param params
     * @return
     */
    @POST("webapi/SupplierEnter/UpdateSupplier")
    Observable<BaseResultBean> UpdateSupplier(@Body Map<String,Object> params);


}
