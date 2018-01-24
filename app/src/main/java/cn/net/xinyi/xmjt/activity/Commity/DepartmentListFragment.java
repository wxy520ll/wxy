package cn.net.xinyi.xmjt.activity.Commity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import cn.net.xinyi.xmjt.config.BaseListAdp;

/**
 * Created by studyjun on 2016/6/27.
 */
public class DepartmentListFragment extends BaseListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View  view=super.onCreateView(inflater, container, savedInstanceState);

        setAdapter(new DepartmentAdp(mListView,new ArrayList<String>(),android.R.layout.simple_list_item_1,getActivity()));
        mAdapter.getData().addAll(valueList);
        mAdapter.setState(BaseListAdp.STATE_LESS_ONE_PAGE);
        mAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    protected void requestData() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, Object t) {
        super.onItemClick(parent, view, position, id, t);
        Intent intent = new Intent(getActivity(),TopicFeedListAty.class);
        intent.putExtra("data",t.toString());
        startActivity(intent);
    }
}
