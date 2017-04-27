package com.caijia.chat.event;

import android.support.v7.widget.RecyclerView;

/**
 * Created by cai.jia on 2016/4/7 0007.
 */
public interface OnItemClickListener<VH extends RecyclerView.ViewHolder> {

    void onItemClick(RecyclerView.Adapter<VH> adapter, int position);
}
