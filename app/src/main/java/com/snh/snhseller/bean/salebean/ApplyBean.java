package com.snh.snhseller.bean.salebean;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/5<p>
 * <p>changeTime：2019/3/5<p>
 * <p>version：1<p>
 */
public class ApplyBean {
    public int CostId;
    public int CostType;//1.1.差旅费 2.招待费 3.市场营销 4.其他
    public int CostStates;//1.审批中 2.已通过 3.已驳回 4.撤销
    public String CostName;
    public double Budget;
    public String CreateTime;
    public String SalesmanName;
}
