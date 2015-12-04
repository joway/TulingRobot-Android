package com.wong.joway.mywechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<Msg> {

    private int resourceId;

    public MsgAdapter(Context context, int textViewResourceId, List<Msg> objects) {
        super(context, textViewResourceId, objects);//将Activity,界面View,数据绑定起来
        resourceId = textViewResourceId;
    }

    //这个方法在每个子项被滚动到屏幕内的时候会被调用
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = getItem(position);//获取当前项实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {//初始状态，没有缓存
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);//加载传入的布局
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
            view.setTag(viewHolder);//将viewHolder存储在view中
        }
        else {
            view = convertView;//缓存之前已经加载好了的View
            viewHolder = (ViewHolder) view.getTag();//重新获取viewHolder
        }
        //viewHolder的使用时为了不用每次都调用findViewById，提高性能！

        if (msg.getType() == Msg.TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);//设置可见性
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
        }
        else if(msg.getType() == Msg.TYPE_SENT) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.getContent());
        }
        return view;
    }

    class ViewHolder {

        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;

    }

}
