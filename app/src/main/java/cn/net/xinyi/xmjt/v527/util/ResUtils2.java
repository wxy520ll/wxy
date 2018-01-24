package cn.net.xinyi.xmjt.v527.util;

import java.util.Random;

/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ResUtils2 {

    private final static int[] DEF_COLOR_ID = new int[]{
            com.xinyi_tech.comm.R.color.comm_red,
            com.xinyi_tech.comm.R.color.comm_green,
            com.xinyi_tech.comm.R.color.comm_amber,
    };

    private static Random sRandom = new Random();

    public static int getRandomColor() {
        int index = sRandom.nextInt(DEF_COLOR_ID.length);
        return DEF_COLOR_ID[index];
    }


}
