package com.snh.moudle_coupons.netapi;

import android.content.Context;

import com.snh.library_base.db.DBManager;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.module_netapi.requestApi.NetworkUtils;
import com.snh.module_netapi.requestApi.RetrofitProxy;
import com.snh.moudle_coupons.bean.CouponsBean;
import com.snh.moudle_coupons.bean.CouponsNumBean;
import com.snh.moudle_coupons.bean.CouponsProductIdBean;
import com.snh.moudle_coupons.bean.RetailProductBean;
import com.snh.moudle_coupons.bean.SupplierCouponBean;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.BuildConfig;
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
     * 获取我的优惠券
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetCouponsList(int pageIndex, Context context, NetSubscriber<BaseResultBean<List<CouponsBean>>> observer) {
        Map<String,Object> map = new TreeMap<>();
        map.put("supplierId",DBManager.getInstance(context).getUseId());
        map.put("pageIndex",pageIndex);
        map.put("pageSize",20);
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).GetCouponsList(map), context, observer);
    }

    /**
     * 获取我的供应商优惠劵
     * @param pageIndex
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSupplierCouponsList(int pageIndex, Context context, NetSubscriber<BaseResultBean<List<CouponsBean>>> observer) {
        Map<String,Object> map = new TreeMap<>();
        map.put("supplierId",DBManager.getInstance(context).getUseId());
        map.put("pageIndex",pageIndex);
        map.put("pageSize",20);
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).GetSupplierCouponsList(map), context, observer);
    }

    /**
     * 获取优惠劵总数
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetCouponsCount( Context context, NetSubscriber<BaseResultBean<CouponsNumBean>> observer) {
        Map<String,Object> map = new TreeMap<>();
        map.put("supplierId",DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).GetCouponsCount(map), context, observer);
    }

    /**
     * 设置供应商优惠劵是否显示
     * @param couponsId
     * @param type
     * @param context
     * @param observer
     * @return
     */
    public static Subscription SetCouponsIsShow(int couponsId,int type, Context context, NetSubscriber<BaseResultBean> observer) {
        Map<String,Object> map = new TreeMap<>();
        map.put("couponsId",couponsId);
        map.put("type",type);
        map.put("supplierId",DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).SetCouponsIsShow(map), context, observer);
    }

    /**
     * 添加优惠劵
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription AddCoupons( Map<String,Object> map, Context context, NetSubscriber<BaseResultBean> observer) {
        map.put("SendPart",DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).AddCoupons(map), context, observer);
    }

    /**
     * 获取该商户商品按类型分组
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetGoodsBySupplierId(Context context, NetSubscriber<BaseResultBean<List<RetailProductBean>>> observer) {
        Map<String,Object> map = new TreeMap<>();
        map.put("supplierId",DBManager.getInstance(context).getUseId());
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).GetGoodsBySupplierId(map), context, observer);
    }

    /**
     * 获取优惠券指定商品
     * @param couponsId
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetCouponsGoods(int couponsId,Context context, NetSubscriber<BaseResultBean<List<CouponsProductIdBean>>> observer) {
        Map<String,Object> map = new TreeMap<>();
        map.put("couponsId",couponsId);
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).GetCouponsGoods(map), context, observer);
    }

    /**
     * 编辑优惠劵
     * @param map
     * @param context
     * @param observer
     * @return
     */
    public static Subscription EditCoupons( Map<String,Object> map,Context context, NetSubscriber<BaseResultBean> observer) {

        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).EditCoupons(map), context, observer);
    }

    /**
     * 商户找供应商批发商品可使用的优惠券
     * @param merchantsId
     * @param goodsName
     * @param context
     * @param observer
     * @return
     */
    public static Subscription GetSupplierCoupon( int merchantsId,String goodsName,Context context, NetSubscriber<BaseResultBean<List<SupplierCouponBean>>> observer) {
        Map<String,Object> map = new TreeMap<>();
        map.put("merchantsId",DBManager.getInstance(context).getUseId());
        map.put("supplierId",merchantsId);
        map.put("goodsName",goodsName);
        return doRequest(RetrofitProxy.init(context).create(RequestApi.class).GetSupplierCoupon(map), context, observer);
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
