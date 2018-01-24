package cn.net.xinyi.xmjt.rxbus.event;

import cn.net.xinyi.xmjt.model.HisTraceQueryModel;

/**
 * Created by Fracesuit on 2017/4/7.
 */

public class QueryHisTraceEvent {
    public static final int  EVENT_TRACEING=0;
    public static final int  EVENT_TRACE_STOP=1;
    private int type;
    private HisTraceQueryModel hisTraceQueryModel;
    public QueryHisTraceEvent(int type)
    {
        this.type=type;
    }
    public int getType() {
        return type;
    }



    public HisTraceQueryModel getHisTraceQueryModel() {
        return hisTraceQueryModel;
    }

    public void setHisTraceQueryModel(HisTraceQueryModel hisTraceQueryModel) {
        this.hisTraceQueryModel = hisTraceQueryModel;
    }
}
