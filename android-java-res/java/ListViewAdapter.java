package com.example.user.complaintanalyser;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 18-04-2021.
 */

public class ListViewAdapter extends ArrayAdapter{

    Activity context;
    List<ComplaintDetails> list;
    ComplaintDetails details;

    public ListViewAdapter(@NonNull Activity context, @NonNull List<ComplaintDetails> list) {
        super(context, R.layout.listview_layout, list);
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        details = list.get(position);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.listview_layout,null,false);
        TextView t1 = (TextView) view.findViewById(R.id.textView);
        TextView t2 = (TextView) view.findViewById(R.id.textView2);
        t1.setText(details.getTitle());
        t2.setText(details.getBody());
        if(details.getIsResolved()) {
            t1.setBackgroundResource(R.drawable.listview_text_top_resolved);
            t2.setBackgroundResource(R.drawable.listview_text_bottom_resolved);
        }
        return view;
    }
}
