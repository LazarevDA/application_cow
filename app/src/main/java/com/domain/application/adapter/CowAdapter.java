package com.domain.application.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.domain.application.R;
import com.domain.application.model.Cow;

import java.util.List;

public class CowAdapter extends ArrayAdapter {

    private final LayoutInflater inflater;
    private List<Cow> cows;

    @SuppressWarnings("unchecked")
    public CowAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
        inflater = LayoutInflater.from(context);
        cows = objects;
    }

    @SuppressWarnings("unchecked")
    public CowAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        cows = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner, parent, false);
        }
        Cow rowItem = (Cow) getItem(position);
        TextView name = convertView.findViewById(R.id.name);
        assert rowItem != null;
        name.setText(rowItem.getNumber());
        return convertView;
    }

    public int getIndexByID(long id) {
        int index = 0;
        for (Cow item : cows) {
            if (item.getId() == id) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner, parent, false);
        }
        Cow rowItem = (Cow) getItem(position);
        TextView name = convertView.findViewById(R.id.name);
        assert rowItem != null;
        name.setText(rowItem.getNumber());
        return convertView;
    }


}
