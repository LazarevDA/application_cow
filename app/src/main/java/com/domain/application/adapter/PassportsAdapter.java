package com.domain.application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.domain.application.R;
import com.domain.application.activity.PassportActivity;
import com.domain.application.model.PassportInfo;

import java.util.List;

public class PassportsAdapter extends BaseAdapter {

    private List<PassportInfo> listPassportInfo;
    private LayoutInflater inflater;
    private Context mContext;


    public PassportsAdapter(Context context, List<PassportInfo> listPassportInfo) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.listPassportInfo = listPassportInfo;
    }

    @Override
    public int getCount() {
        return listPassportInfo.size();
    }

    @Override
    public PassportInfo getItem(int i) {
        return listPassportInfo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listPassportInfo.get(i).getId();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_passport,
                    null);
            holder = new ViewHolder();
            holder.number = view.findViewById(R.id.number);
            holder.age = view.findViewById(R.id.age);
            holder.breed = view.findViewById(R.id.breed);
            holder.suit = view.findViewById(R.id.suit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final PassportInfo item = getItem(position);
        if (item != null) {

            holder.number.setText(item.getNumber());
            holder.age.setText(String.valueOf(item.getAge()));
            holder.breed.setText(item.getBreed());
            holder.suit.setText(item.getSuits());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PassportActivity.class);
                intent.putExtra("IsNewRecord", false);
                intent.putExtra("id", getItemId(position));
                mContext.startActivity(intent);
            }
        });

        return view;
    }


    class ViewHolder {
        TextView number;
        TextView age;
        TextView breed;
        TextView suit;
    }
}
