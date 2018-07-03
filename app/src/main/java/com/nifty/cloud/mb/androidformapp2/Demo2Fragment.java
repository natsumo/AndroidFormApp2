package com.nifty.cloud.mb.androidformapp2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nifty.cloud.mb.androidformapp2.adapter.InquiryListAdapter;
import com.nifty.cloud.mb.androidformapp2.mbaas.Mbaas;
import com.nifty.cloud.mb.androidformapp2.model.Inquiry;
import com.nifty.cloud.mb.androidformapp2.utils.ProgressBarFragment;
import com.nifty.cloud.mb.androidformapp2.utils.Utils;
import com.nifty.cloud.mb.core.FindCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;

import java.util.ArrayList;
import java.util.List;

public class Demo2Fragment extends Fragment {
    protected View mView;
    private List<Inquiry> inquiryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private InquiryListAdapter mAdapter;
    private ProgressBarFragment mProgressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_demo2, container, false);
            mProgressBar = new ProgressBarFragment();
            getFragmentManager().beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss();

            final TextView tvResultCount = (TextView) mView.findViewById(R.id.tvResultCount);
            recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
            mAdapter = new InquiryListAdapter(inquiryList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            Mbaas.getAllData(new FindCallback<NCMBObject>() {
                @Override
                public void done(List<NCMBObject> list, NCMBException e) {
                    if (mProgressBar != null) {
                        getFragmentManager().beginTransaction().remove(mProgressBar).commitAllowingStateLoss();
                    }

                    if (e != null) {
                        //検索失敗時の処理
                        Utils.showDialog(getContext(), getString(R.string.data_acquisition_failed));
                    } else {
                        //検索成功時の処理
                        for (NCMBObject obj: list) {
                            Inquiry inquiry = new Inquiry();
                            inquiry.name = obj.getString("name");
                            inquiry.title = obj.getString("title");
                            inquiry.contents = obj.getString("contents");
                            inquiry.age = obj.getInt("age");
                            inquiry.prefecture = obj.getString("prefecture");
                            inquiry.emailAddress = obj.getString("emailAddress");
                            inquiry.createDate = Utils.formatTime(obj.getString("createDate"));
                            inquiryList.add(inquiry);
                        }
                        tvResultCount.setText(String.format(getString(R.string.all_search_results), list.size()));
                        tvResultCount.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        return mView;
    }
}
