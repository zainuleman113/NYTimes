package com.example.nytimesmostpopular.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nytimesmostpopular.R;
import com.example.nytimesmostpopular.business.objects.Result;

import java.util.List;


public class MostPopularAdapter extends ArrayAdapter<Result> {
    private Context context;
    private int layout;
    private List<Result> list;


    public MostPopularAdapter(Context context, int layout, List<Result> list) {
        super(context, layout);
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvTimeDate;
        TextView tvAmount;
        TextView tvBalance;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflator = ((Activity) context).getLayoutInflater();
        if (convertView == null) {
            convertView = inflator.inflate(layout, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvTimeDate = (TextView) convertView.findViewById(R.id.tvTimeDate);
//            holder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
//            holder.tvBalance = (TextView) convertView.findViewById(R.id.tvBalance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(list.get(position).getTitle());
        holder.tvTimeDate.setText(list.get(position).getPublishedDate());


        return convertView;
    }
}



