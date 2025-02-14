package com.snh.snhseller.wediget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.haibin.calendarview.MultiMonthView;
import com.haibin.calendarview.WeekView;

import java.util.List;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/26<p>
 * <p>changeTime：2019/2/26<p>
 * <p>version：1<p>
 */
public class MyWeekView extends WeekView {

    private int mRadius;
    private Paint mRingPaint = new Paint();
    private int mRingRadius;

    /**
     * 不可用画笔
     */
    private Paint mDisablePaint = new Paint();

    private int mH;

    public MyWeekView(Context context) {
        super(context);
        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));
        mSelectedPaint.setColor(Color.RED);
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemePaint);
        mSchemePaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mSchemePaint.getColor());
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(dipToPx(context, 1));
        setLayerType(View.LAYER_TYPE_SOFTWARE, mRingPaint);
        mRingPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.SOLID));

        mDisablePaint.setColor(0xFF9f9f9f);
        mDisablePaint.setAntiAlias(true);
        mDisablePaint.setStrokeWidth(0);
        mDisablePaint.setFakeBoldText(true);
        mDisablePaint.setStrikeThruText(false);
        mDisablePaint.clearShadowLayer();

        mH = dipToPx(context, 18);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 6 * 2;
        mRingRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSelectTextPaint.setTextSize(dipToPx(getContext(),17));

        mSchemePaint.setColor(Color.WHITE);
        mSchemeTextPaint.setColor(Color.WHITE);
        mOtherMonthTextPaint.setColor(Color.WHITE);
    }

    /**
     * 如果需要点击Scheme没有效果，则return true
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return false 则不绘制onDrawScheme，因为这里背景色是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        canvas.drawCircle(cx, cy, mRingRadius, mRingPaint);
        return true;
    }


    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
//        int cx = x + mItemWidth / 2;
//        int cy = mItemHeight / 2;
        //canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine - dipToPx(getContext(), 1);
        int cx = x + mItemWidth / 2;
        if (isSelected) {
            canvas.drawText(calendar.isCurrentDay() ? "今" : "选",
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(calendar.isCurrentDay() ? "今" : String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSelectTextPaint : mSelectTextPaint);

        } else {
            canvas.drawText(calendar.isCurrentDay() ? "今" : String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSelectTextPaint : mSelectTextPaint);
        }

        //日期是否可用？拦截
        if (onCalendarIntercept(calendar)) {
//            canvas.drawLine(x + mH, mH, x + mItemWidth - mH, mItemHeight - mH, mOtherMonthTextPaint);
//            canvas.drawText(String.valueOf(calendar.getDay()), cx,
//                    baselineY,mDisablePaint);
        }
    }


    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    @SuppressWarnings("all")
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}