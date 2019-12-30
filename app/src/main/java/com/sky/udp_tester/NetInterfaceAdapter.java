package com.sky.udp_tester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NetInterfaceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<InterfaceEntity> interfaceList;
    public NetInterfaceAdapter(Context context, ArrayList<InterfaceEntity> interfaceList){
        inflater = LayoutInflater.from(context);
        this.interfaceList = interfaceList;
    }

    @Override
    public int getCount() {
        return interfaceList == null ? 0 : interfaceList.size();
    }

    @Override
    public Object getItem(int position) {
        return interfaceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.lyt_interface_info, null);
            holder.nameTv = convertView.findViewById(R.id.lyt_interface_name_tv);
            holder.ipAddrTv = convertView.findViewById(R.id.lyt_interface_ip_address_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InterfaceEntity entity = interfaceList.get(position);
        holder.nameTv.setText(entity.getName());
        holder.ipAddrTv.setText(entity.toString());

        return convertView;
    }

    private class ViewHolder {
        TextView nameTv;
        TextView ipAddrTv;
    }
}
