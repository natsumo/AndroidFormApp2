package com.nifty.cloud.mb.androidformapp2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nifty.cloud.mb.androidformapp2.mbaas.Mbaas;
import com.nifty.cloud.mb.androidformapp2.utils.ProgressBarFragment;
import com.nifty.cloud.mb.androidformapp2.utils.Utils;
import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;

import java.util.ArrayList;
import java.util.List;

public class Demo1Fragment extends Fragment {
    protected View mView;
    private ProgressBarFragment mProgressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_demo1, container, false);

            final Spinner spnAge = (Spinner) mView.findViewById(R.id.spinner_age);
            List<String> ages  = new ArrayList<String>();
            ages.add("- age -");
            for (int i = 0; i<= 120; i++) {
                ages.add(String.valueOf(i));
            }
            ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ages);
            spnAge.setAdapter(ageAdapter);

            final Spinner spnPrefecture = (Spinner) mView.findViewById(R.id.spinner_prefecture);
            ArrayAdapter<String> prefectureAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.prefecture_data));
            spnPrefecture.setAdapter(prefectureAdapter);

            final EditText inputName = (EditText) mView.findViewById(R.id.inputName);
            final EditText inputMail = (EditText) mView.findViewById(R.id.inputMailAddress);
            final EditText inputTitle = (EditText) mView.findViewById(R.id.inputTitle);
            final EditText inputContents = (EditText) mView.findViewById(R.id.inputContents);
            Button btnSubmit = (Button) mView.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("".equals(inputName.getText().toString())) {
                        Utils.showDialog(getContext(), getString(R.string.name_is_not_entered));
                    } else if ("".equals(inputMail.getText().toString())) {
                        Utils.showDialog(getContext(), getString(R.string.email_is_not_entered));
                    } else if (spnAge.getSelectedItemPosition() == 0) {
                        Utils.showDialog(getContext(), getString(R.string.age_has_not_been_entered));
                    } else if (spnPrefecture.getSelectedItemPosition() == 0) {
                        Utils.showDialog(getContext(), getString(R.string.province_is_not_entered));
                    } else if ("".equals(inputTitle.getText().toString())) {
                        Utils.showDialog(getContext(), getString(R.string.inquiry_title_has_not_been_entered));
                    } else if ("".equals(inputContents.getText().toString())) {
                        Utils.showDialog(getContext(), getString(R.string.inquiry_content_is_not_entered));
                    } else {
                        // show progress
                        mProgressBar = new ProgressBarFragment();
                        getFragmentManager().beginTransaction().add(R.id.progress, mProgressBar).commitAllowingStateLoss();

                        String name = inputName.getText().toString();
                        String email = inputMail.getText().toString();
                        Integer age = spnAge.getSelectedItemPosition() - 1;
                        String prefecture = spnPrefecture.getSelectedItem().toString();
                        String title = inputTitle.getText().toString();
                        String contents = inputContents.getText().toString();
                        Mbaas.saveData(name, email, age, prefecture, title, contents, new DoneCallback() {
                            @Override
                            public void done(NCMBException e) {
                                // remove progress
                                if (mProgressBar != null) {
                                    getFragmentManager().beginTransaction().remove(mProgressBar).commitAllowingStateLoss();
                                }
                                if (e != null) {
                                    //保存失敗
                                    Utils.showDialog(getContext(), getString(R.string.failed_to_accept_inquiries));
                                } else {
                                    // 保存成功
                                    Utils.showDialog(getContext(), getString(R.string.inquiries_accepted));
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
