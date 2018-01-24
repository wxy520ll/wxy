package cn.net.xinyi.xmjt.v527.presentation.home.cj.zacs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.config.BaseListAdp;
import cn.net.xinyi.xmjt.config.BaseListAty;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.utils.ListUtils;
import cn.net.xinyi.xmjt.utils.StringUtils;

/**
 * Created by hao.zhou on 2016/2/22.
 */
public class ZacsMainAty extends BaseListAty  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Map<String, String> mapAayys = zdUtils.getZdlbToZdz(GeneralUtils.XXCJ_ZAJCFL_JT_NEW);
        List<String> list=ListUtils.mapTransitionList(mapAayys);
        ZacsMainAdp mAdapter=new ZacsMainAdp(mListView, list,R.layout.aty_zacsmain_item,ZacsMainAty.this);
        setAdapter(mAdapter);
        mAdapter.setState(BaseListAdp.STATE_NO_MORE);
    }

    @Override
    protected void requestData() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, Object t) {
        super.onItemClick(parent, view, position, id, t);
        if (t instanceof String){
            String zdnum= zdUtils.getZdlbAndZdzToZdbm(GeneralUtils.XXCJ_ZAJCFL_JT_NEW,t.toString());
          if (!StringUtils.isEmpty(zdnum)&&Integer.parseInt(zdnum)>136){
              Intent intent = new Intent(ZacsMainAty.this, ZacsAty2.class);
              intent.putExtra("zdnum",zdnum);
              intent.putExtra("zdz",t.toString());
              startActivity(intent);
          }else {
              Intent intent = new Intent(ZacsMainAty.this, ZacsAty.class);
              intent.putExtra("zdnum",zdnum);
              intent.putExtra("zdz",t.toString());
              startActivity(intent);
          }
        }
    }
    @Override
    public String getAtyTitle() {
        return "场所类别";
    }


    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

}