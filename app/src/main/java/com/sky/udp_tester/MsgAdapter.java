package com.sky.udp_tester;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MsgAdapter extends BaseAdapter {
    private ArrayList<MsgEntity> msgList;
    private LayoutInflater inflater;
    public MsgAdapter(Context context, ArrayList<MsgEntity> msgList){
        inflater = LayoutInflater.from(context);
        this.msgList = msgList;
    }


    @Override
    public int getCount() {
        return msgList == null ? 0 : msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        MsgEntity entity = msgList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();

            if (MsgEntity.TYPE_SEND.equals(entity.getType())) {
                // send
                convertView = inflater.inflate(R.layout.lyt_send, null);
                holder.lytId = R.layout.lyt_send;
            } else {
                // receive
                convertView = inflater.inflate(R.layout.lyt_receive, null);
                holder.lytId = R.layout.lyt_receive;
            }
            holder.msgTv = convertView.findViewById(R.id.lyt_msg_tv);
            holder.dateTv = convertView.findViewById(R.id.lyt_date_tv);
            holder.numTv = convertView.findViewById(R.id.lyt_num_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

            if (holder.lytId == R.layout.lyt_send) {
                if (MsgEntity.TYPE_RECEIVE.equals(entity.getType())) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.lyt_receive, null);
                    holder.lytId = R.layout.lyt_receive;
                    holder.msgTv = convertView.findViewById(R.id.lyt_msg_tv);
                    holder.dateTv = convertView.findViewById(R.id.lyt_date_tv);
                    holder.numTv = convertView.findViewById(R.id.lyt_num_tv);
                    convertView.setTag(holder);
                }
            } else {
                if (MsgEntity.TYPE_SEND.equals(entity.getType())) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.lyt_send, null);
                    holder.lytId = R.layout.lyt_send;
                    holder.msgTv = convertView.findViewById(R.id.lyt_msg_tv);
                    holder.dateTv = convertView.findViewById(R.id.lyt_date_tv);
                    holder.numTv = convertView.findViewById(R.id.lyt_num_tv);
                    convertView.setTag(holder);
                }
            }
        }
        holder.dateTv.setText(DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date(entity.getDate())));
        holder.numTv.setText(" Num : ["+ entity.getMsg().length() +"]");
        holder.msgTv.setText("Data:" + entity.getMsg());

        return convertView;
    }

    private class ViewHolder {
        int lytId;
        TextView dateTv;
        TextView numTv;
        TextView msgTv;
    }
}
