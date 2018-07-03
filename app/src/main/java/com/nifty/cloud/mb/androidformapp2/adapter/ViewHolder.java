package com.nifty.cloud.mb.androidformapp2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nifty.cloud.mb.androidformapp2.R;

class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvCreateDate;
    TextView tvContents;

    public ViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.title);
        tvCreateDate = (TextView) itemView.findViewById(R.id.create_date);
        tvContents = (TextView) itemView.findViewById(R.id.contents);
    }
}
