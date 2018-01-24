package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.person;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.net.xinyi.xmjt.R;
import cn.net.xinyi.xmjt.api.ApiResource;
import cn.net.xinyi.xmjt.config.BaseActivity2;
import cn.net.xinyi.xmjt.utils.GeneralUtils;
import cn.net.xinyi.xmjt.weiget.WaveSideBar;


public class PersonActivity extends BaseActivity2 {

    private RecyclerView rvContacts;
    private RecyclerView rvPcs;
    private WaveSideBar sideBar;

    private ArrayList<UserInfoMapper> contacts = new ArrayList<>();
    private ArrayList<String> pcss = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private PcsAdapter pcsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initView();
    }

    private void initView() {
        final Map<String, String> pcsMaps = zdUtils.getZdlbAndFzdbmToZdz(GeneralUtils.ZZJG_PCS, userInfo.getFjbm());
        pcss.clear();
        pcss.addAll(pcsMaps.values());
        rvPcs = (RecyclerView) findViewById(R.id.rv_pcs);
        rvPcs.setLayoutManager(new LinearLayoutManager(this));
        pcsAdapter = new PcsAdapter(pcss);
        rvPcs.setAdapter(pcsAdapter);

        rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new ContactsAdapter(contacts, R.layout.item_contacts);
        rvContacts.setAdapter(contactsAdapter);

        sideBar = (WaveSideBar) findViewById(R.id.side_bar);
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getFirstNamePinYin().equals(index)) {
                        ((LinearLayoutManager) rvContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = getIntent();
                intent.putExtra("phone", contacts.get(position).getCellphone());
                setResult(100, intent);
                finish();
            }
        });

        pcsAdapter.setOnItemClickListener(new PcsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                pcsAdapter.selectIndex = position;
                pcsAdapter.notifyDataSetChanged();
                String pcs = pcsMaps.keySet().toArray(new String[pcsMaps.keySet().size()])[position];
                initData(pcs);
            }
        });

        //开始的数据
        String pcs = pcsMaps.keySet().toArray(new String[pcsMaps.keySet().size()])[0];
        initData(pcs);
        pcsAdapter.selectIndex = 0;
        pcsAdapter.notifyDataSetChanged();


    }

    private void initData(String pcs) {
        showLoadding();
        ApiResource.getUserInfosByPcs(getRequestUserinfos(pcs), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String result = new String(bytes);
                try {
                    NetUserInfosByPcsEntity parse = JSON.parseObject(result, NetUserInfosByPcsEntity.class);
                    List<UserInfoMapper> transform = parse.transform(parse.getResult());
                    Collections.sort(transform);
                    contacts.clear();
                    contacts.addAll(transform);
                    contactsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    toast("获取数据失败");
                }
                stopLoading();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                stopLoading();
                toast("获取数据失败");

            }
        });
    }

    private String getRequestUserinfos(String pcs) {
        JSONObject requestJson = new JSONObject();
        requestJson.put("ORGANCODE", pcs);
        return requestJson.toJSONString();
    }

    @Override
    public boolean enableBackUpBtn() {
        return true;
    }

    @Override
    public String getAtyTitle() {
        return "人员信息";
    }
}
