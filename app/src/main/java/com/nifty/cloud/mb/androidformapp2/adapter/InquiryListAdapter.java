package com.nifty.cloud.mb.androidformapp2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nifty.cloud.mb.androidformapp2.R;
import com.nifty.cloud.mb.androidformapp2.model.Inquiry;
import com.nifty.cloud.mb.androidformapp2.utils.Utils;

import java.util.List;

public class InquiryListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Inquiry> inquiryList;

    public InquiryListAdapter(List<Inquiry> listItem) {
        inquiryList = listItem;
    }

    public void setInquiryList(List<Inquiry> listItem) {
        inquiryList = listItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.item, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Utils.showDialog(parent.getContext(), inquiryList.get(position).contents);
                }

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Inquiry inquiry = inquiryList.get(position);
        holder.tvTitle.setText(inquiry.title);
        holder.tvCreateDate.setText(inquiry.createDate);
        holder.tvContents.setText(new StringBuilder(inquiry.name).append("( ").append(inquiry.prefecture)
                .append(" ) -").append(inquiry.age).append("- ").append(inquiry.emailAddress).toString());
    }

    @Override
    public int getItemCount() {
        if (inquiryList != null) {
            return inquiryList.size();
        }
        return 0;
    }
}