package cn.net.xinyi.xmjt.model;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * Created by studyjun on 2016/4/23.
 */
public class XinyiLatLng implements Serializable{
    private static final String a = LatLng.class.getSimpleName();
    public final double latitude;
    public final double longitude;

    public XinyiLatLng(double var1, double var3) {
        this.latitude =var1;
        this.longitude = var3;
    }

    public String toString() {
        String var1 = new String("latitude: ");
        var1 = var1 + this.latitude;
        var1 = var1 + ", longitude: ";
        var1 = var1 + this.longitude;
        return var1;
    }
}