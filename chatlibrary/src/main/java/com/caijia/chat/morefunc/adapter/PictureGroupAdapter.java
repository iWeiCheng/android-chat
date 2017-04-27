package com.caijia.chat.morefunc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caijia.chat.R;
import com.caijia.chat.event.OnItemClickListener;
import com.caijia.chat.morefunc.entities.PictureGroup;
import com.caijia.chat.service.DeviceUtil;
import com.caijia.chat.service.ImageLoaderUtil;

import java.util.List;

/**
 * Created by cai.jia on 2016/4/7 0007.
 */
public class PictureGroupAdapter extends RecyclerView.Adapter<PictureGroupAdapter.PictureGroupVH> {

    private Context context;

    private List<PictureGroup> list;

    private static final String FILE_PREFIX = "file://";

    private int imageSize;

    public PictureGroupAdapter(Context context, List<PictureGroup> list) {
        this.context = context;
        this.list = list;
        imageSize = DeviceUtil.dp2px(context, 72);
    }

    public PictureGroup getItem(int position) {
        return list.get(position);
    }

    @Override
    public PictureGroupVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.adapter_picture_group, parent, false);
        return new PictureGroupVH(view);
    }

    @Override
    public void onBindViewHolder(PictureGroupVH holder, final int position) {
        PictureGroup item = getItem(position);
        if (holder != null && item != null) {
            holder.name.setText(item.getGroupName());
            List<String> paths = item.getPicturePaths();
            if (paths != null && !paths.isEmpty()) {
                String firstPath = paths.get(0);
                holder.num.setText(String.format("%d张", paths.size()));
                ImageLoaderUtil.loadImage(holder.image, FILE_PREFIX + firstPath, R.drawable.shape_transparent, imageSize);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(PictureGroupAdapter.this, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PictureGroupVH extends RecyclerView.ViewHolder {

        public ImageView image;

        public TextView name;

        public TextView num;

        public PictureGroupVH(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            num = (TextView) itemView.findViewById(R.id.picture_num);
        }
    }

    private OnItemClickListener<PictureGroupVH> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<PictureGroupVH> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
