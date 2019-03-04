package com.snh.snhseller.ui.home.accoutData;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.DataBean;
import com.snh.snhseller.bean.DataListBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.IsBang;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/25<p>
 * <p>changeTime：2019/2/25<p>
 * <p>version：1<p>
 */
public class DataStatisticsActivity extends BaseActivity {
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
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_num1)
    TextView tvNum1;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.line_chart)
    LineChart lineChart;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_datastatis_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead);
        heardTitle.setText("数据统计");
        getData();
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

    @OnClick({R.id.heard_back, R.id.tv_num1, R.id.tv_price})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.tv_num1:
                tvNum.setVisibility(View.GONE);
                tvNum1.setTextColor(Color.WHITE);
                tvNum1.setBackgroundResource(R.drawable.shape_soild_blue_left_bg);
                tvPrice.setTextColor(Color.parseColor("#34a9f9"));
                tvPrice.setBackgroundResource(R.drawable.shape_rangle_blue_right_bg);
                initLineChart(data1);
                break;
            case R.id.tv_price:
                tvPrice.setTextColor(Color.WHITE);
                tvPrice.setBackgroundResource(R.drawable.shape_soild_blue_right_bg);
                tvNum1.setTextColor(Color.parseColor("#34a9f9"));
                tvNum1.setBackgroundResource(R.drawable.shape_rangle_blue_left_bg);
                initLineChart(data2);
                break;
        }
    }
    private void initLineChart(final List<Float> datas){
        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++)
        {
            entries.add(new Entry(i, datas.get(i)));
        }
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        //线颜色
        lineDataSet.setColor(Color.parseColor("#34a9f9"));
        //线宽度
        lineDataSet.setLineWidth(1.6f);
        //显示圆点
        lineDataSet.setDrawCircles(true);
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        LineData data = new LineData(lineDataSet);
        //无数据时显示的文字
        lineChart.setNoDataText("暂无数据");
        //折线图显示数值
        data.setDrawValues(true);
        //得到X轴
        XAxis xAxis = lineChart.getXAxis();
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
//        xAxis.setLabelCount(list.size() / 6, false);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMinimum(0f);
//        xAxis.setAxisMaximum((float) list.size());
        xAxis.enableGridDashedLine(10f,10f,0f);
        //是否显示网格线
        xAxis.setDrawGridLines(true);

        // 标签倾斜
        xAxis.setLabelRotationAngle(45);
        //设置X轴值为字符串
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                int IValue = (int) value;
//                CharSequence format = DateFormat.format("MM/dd",
//                        System.currentTimeMillis()-(long)(datas.size()-IValue)*24*60*60*1000);
                StringBuffer sb = new StringBuffer(beans.get((int) value).Date);
                String data = sb.replace(0,5,"").toString();
                return data;
            }
        });
        //得到Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        //设置Y轴是否显示
        rightYAxis.setEnabled(true); //右侧Y轴不显示
        rightYAxis.enableGridDashedLine(10f,10f,0f);
        //设置y轴坐标之间的最小间隔
        //不显示网格线
        yAxis.setDrawGridLines(false);
        //设置Y轴坐标之间的最小间隔
        yAxis.setGranularity(1);
        //设置y轴的刻度数量
        //+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
//        yAxis.setLabelCount((int) (Collections.max(datas) + 2), false);
        //设置从Y轴值
        yAxis.setAxisMinimum(0f);
        //+1:y轴多一个单位长度，为了好看
//        yAxis.setAxisMaximum(Collections.max(list) + 1);

        //y轴
        yAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                int IValue = (int) value;
                return String.valueOf(IValue);
            }
        });
        //图例：得到Lengend
        Legend legend = lineChart.getLegend();
        //隐藏Lengend
        legend.setEnabled(false);
        //隐藏描述
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
        lineChart.animateXY(1000,0);
        //折线图点的标记
//        MyMarkerView mv = new MyMarkerView(this);
//        lineChart.setMarker(mv);
        //设置数据
        lineChart.setData(data);
        //图标刷新
        lineChart.invalidate();
    }



    private DataBean bean;
    private List<DataListBean> beans;
    private List<Float> data1 = new ArrayList<>();
    private List<Float> data2 = new ArrayList<>();
    private void getData(){
        addSubscription(RequestClient.DataStatistics(this, new NetSubscriber<BaseResultBean<DataBean>>(this) {
            @Override
            public void onResultNext(BaseResultBean<DataBean> model) {
                bean = model.data;
                beans = model.data.DataList;
                tvMoney.setText(bean.VolumeTransaction+"元");
                tvNum.setText(bean.OrderNumber+"笔");
                for (int i = 0; i < beans.size(); i++) {
                    data1.add((float) beans.get(i).OrderNum);
                    data2.add((float) beans.get(i).Volume);
                }
                initLineChart(data1);
            }
        }));
    }
}
