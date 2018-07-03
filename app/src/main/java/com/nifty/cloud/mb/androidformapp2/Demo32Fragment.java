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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class Demo32Fragment extends Fragment {
    protected View mView;
    private List<Inquiry> inquiryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private InquiryListAdapter mAdapter;
    private ProgressBarFragment mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_demo32, container, false);
            recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
            mAdapter = new InquiryListAdapter(inquiryList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            final TextView tvResultCount = (TextView) mView.findViewById(R.id.tvResultCount);
            final Spinner spinnerAgeGreaterThan = (Spinner) mView.findViewById(R.id.spinnerAgeGreaterThan);
            final Spinner spinnerAgeLessThan = (Spinner) mView.findViewById(R.id.spinnerAgeLessThan);
            Button btnSearch = (Button) mView.findViewById(R.id.btnSearch);

            List<String> ages  = new ArrayList<String>();
            ages.add("- age -");
            for (int i = 0; i<= 120; i++) {
                ages.add(String.valueOf(i));
            }
            ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ages);

            spinnerAgeGreaterThan.setAdapter(ageAdapter);
            spinnerAgeLessThan.setAdapter(ageAdapter);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inquiryList = new ArrayList<>();
                    mAdapter.setInquiryList(inquiryList);
                    mAdapter.notifyDataSetChanged();
                    tvResultCount.setVisibility(View.INVISIBLE);

                    Integer ageGreaterThan = spinnerAgeGreaterThan.getSelectedItemPosition();
                    Integer ageLessThan = spinnerAgeLessThan.getSelectedItemPosition();

                    if (ageGreaterThan == 0 || ageGreaterThan == 0) {
                        Utils.showDialog(getContext(), getString(R.string.please_fill_in_the_value));
                    } else if (ageGreaterThan >= ageLessThan) {
                        Utils.showDialog(getContext(), getString(R.string.value_is_invalid));
                    } else {
                        // show progress
                    mProgressBar = new ProgressBarFragment();
                    getFragmentManager().beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss();

                        ageGreaterThan = ageGreaterThan - 1;
                        ageLessThan = ageLessThan - 1;
                        Mbaas.getRangeSearchData(ageGreaterThan, ageLessThan, new FindCallback<NCMBObject>() {
                            @Override
                            public void done(List<NCMBObject> list, NCMBException e) {
                                // remove progress
                                if (mProgressBar != null) {
                                    getFragmentManager().beginTransaction().remove(mProgressBar).commitAllowingStateLoss();
                                }

                                if (e != null) {
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
                                    mAdapter.setInquiryList(inquiryList);
                                    mAdapter.notifyDataSetChanged();
                                    tvResultCount.setText(String.format(getString(R.string.conditional_search_range_result), list.size()));
                                    tvResultCount.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            });
        }
        return mView;
    }
}
