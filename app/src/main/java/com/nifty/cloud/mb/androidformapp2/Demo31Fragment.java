package com.nifty.cloud.mb.androidformapp2;

import android.app.Activity;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Demo31Fragment extends Fragment {
    protected View mView;
    private List<Inquiry> inquiryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private InquiryListAdapter mAdapter;
    private ProgressBarFragment mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_demo31, container, false);

            recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);
            mAdapter = new InquiryListAdapter(inquiryList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            final TextView tvResultCount = (TextView) mView.findViewById(R.id.tvResultCount);
            final Spinner spnPrefecture = (Spinner) mView.findViewById(R.id.spinner_prefecture);
            ArrayAdapter<String> prefectureAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.prefecture_data));
            spnPrefecture.setAdapter(prefectureAdapter);
            final EditText inputMail = (EditText) mView.findViewById(R.id.inputMailAddress);
            Button btnSearchByEmail = (Button) mView.findViewById(R.id.btnSearchMail);
            Button btnSearchByPref = (Button) mView.findViewById(R.id.btnSearchPrefecture);

            btnSearchByEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvResultCount.setVisibility(View.INVISIBLE);
                    inquiryList = new ArrayList<>();
                    mAdapter.setInquiryList(inquiryList);
                    mAdapter.notifyDataSetChanged();
                    dismissKeyboard(getActivity());

                    String email = inputMail.getText().toString();
                    if ("".equals(email)) {
                        Utils.showDialog(getContext(), getString(R.string.please_fill_in_the_value));
                    } else {
                        // show progress
                    mProgressBar = new ProgressBarFragment();
                    getFragmentManager().beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss();

                        Mbaas.getSearchData(Mbaas.EMAIL, email, new FindCallback<NCMBObject>() {
                            @Override
                            public void done(List<NCMBObject> list, NCMBException e) {
                                // remove progress
                                if (mProgressBar != null && mProgressBar.isAdded()) {
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
                                    mAdapter.notifyDataSetChanged();
                                    tvResultCount.setText(String.format(getString(R.string.condition_search_result), list.size()));
                                    tvResultCount.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
            });

            btnSearchByPref.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvResultCount.setVisibility(View.INVISIBLE);
                    inquiryList = new ArrayList<>();
                    mAdapter.setInquiryList(inquiryList);
                    mAdapter.notifyDataSetChanged();
                    dismissKeyboard(getActivity());

                    if (spnPrefecture.getSelectedItemPosition() == 0) {
                        Utils.showDialog(getContext(), getString(R.string.please_fill_in_the_value));
                    } else {
                        // show progress
                        mProgressBar = new ProgressBarFragment();
                        getFragmentManager().beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss();

                        Mbaas.getSearchData(Mbaas.PREFECTURE, spnPrefecture.getSelectedItem().toString(), new FindCallback<NCMBObject>() {
                            @Override
                            public void done(List<NCMBObject> list, NCMBException e) {
                                // remove progress
                                if (mProgressBar != null && mProgressBar.isAdded()) {
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
                                    mAdapter.notifyDataSetChanged();
                                    tvResultCount.setText(String.format(getString(R.string.condition_search_result), list.size()));
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

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}
