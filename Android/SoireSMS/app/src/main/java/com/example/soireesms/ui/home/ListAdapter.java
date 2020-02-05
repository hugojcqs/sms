package com.example.soireesms.ui.home;


import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.soireesms.R;

import java.util.List;


public class ListAdapter extends BaseAdapter {

    Context context;
    private List<Sms> sms;


    public ListAdapter(Context context, List<Sms> sms){
        this.context = context;
        this.sms = sms;
    }

    @Override
    public int getCount() {
        return sms.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_list_item, parent, false);
            viewHolder.message = convertView.findViewById(R.id.messageID);
            viewHolder.telephoneNumber = convertView.findViewById(R.id.telephoneNumberID);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.message.setText(sms.get(position).getMsg());
        viewHolder.telephoneNumber.setText(sms.get(position).getAddress());

        return result;
    }

    private static class ViewHolder {
        TextView message;
        TextView telephoneNumber;
    }

}