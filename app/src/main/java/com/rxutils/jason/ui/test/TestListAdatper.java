package com.rxutils.jason.ui.test;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rxutils.jason.MainActivity;
import com.rxutils.jason.R;

import java.util.List;

/**
 * @author by jason-何伟杰，2020/5/12
 * des:测试用例列表
 */
public class TestListAdatper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TestBean> mDataList;
    private MainActivity mActivity;

    public TestListAdatper(Activity activity, List<TestBean> list) {
        this.mDataList = list;
        this.mActivity = (MainActivity) activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TestBean.LATYOUT_BUTTON) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_button, parent, false);
            return new ButtonHolder(view);
        } else if (viewType == TestBean.LATYOUT_TXT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_txt, parent, false);
            return new TxtHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ButtonHolder) {
            ButtonHolder holder1 = (ButtonHolder) holder;
            holder1.button.setText(mDataList.get(position).title);
            holder1.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.emitEvent(position);
                }
            });
        } else if (holder instanceof TxtHolder) {
            TxtHolder holder1 = (TxtHolder) holder;
            holder1.textView.setText(mDataList.get(position).title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getLayout_type();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ButtonHolder extends RecyclerView.ViewHolder {
        Button button;

        public ButtonHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.btn_test);
        }
    }

    public static class TxtHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TxtHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_test);
        }
    }
}
