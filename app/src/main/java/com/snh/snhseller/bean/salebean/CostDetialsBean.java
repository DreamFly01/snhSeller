package com.snh.snhseller.bean.salebean;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/6<p>
 * <p>changeTime：2019/3/6<p>
 * <p>version：1<p>
 */
public class CostDetialsBean {
    public int CostId;
    public int SalesmanId;
    public String SalesmanName;
    public String ApprovalNo;
    public int CostStates;//1.审批中 2.已同意 3.已驳回
    public int CostType; //1.差旅费 2.招待费 3.市场营销 4.其他
    public String CostName;
    public double Budget;
    public String OccurDate;
    public String Remark;
    public String ExpenseVoucher;
    public int SupplierId;
    public String SupplierIconUrl;
    public String OperationTime;
    public String SupplierName;
}
