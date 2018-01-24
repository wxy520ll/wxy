package cn.net.xinyi.xmjt.activity.ZHFK.PlcBx.person;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.net.xinyi.xmjt.R;

/**
 * Created by gjz on 9/4/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<UserInfoMapper> userInfoMapper;
    private int layoutId;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ContactsAdapter(List<UserInfoMapper> userInfoMapper, int layoutId) {
        this.userInfoMapper = userInfoMapper;
        this.layoutId = layoutId;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, final int position) {
        UserInfoMapper contact = userInfoMapper.get(position);
        if (position == 0 || !userInfoMapper.get(position - 1).getFirstNamePinYin().equals(contact.getFirstNamePinYin())) {
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(contact.getFirstNamePinYin());
        } else {
            holder.tvIndex.setVisibility(View.GONE);
        }
        holder.tvName.setText(contact.getName() + "  " + contact.getCellphone());
        holder.ll_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userInfoMapper.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIndex;
        public TextView tvName;
        public LinearLayout ll_person;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            tvIndex = (TextView) itemView.findViewById(R.id.tv_index);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ll_person = (LinearLayout) itemView.findViewById(R.id.ll_person);
        }
    }
}