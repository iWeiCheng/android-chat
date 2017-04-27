package com.caijia.chat.emoticon.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caijia.chat.R;
import com.caijia.chat.emoticon.entities.Emoticon;
import com.caijia.chat.service.EmoticonService;
import com.caijia.chat.service.ViewHolder;

import java.io.File;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by cai.jia on 2015/8/27.
 */
public class EmoticonItemAdapter extends ArrayAdapter<Emoticon> {

    public EmoticonItemAdapter(Context context, List<Emoticon> emoticonList) {
        super(context, 0, emoticonList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_emoticon_item, parent, false);
        }
        ImageView image = ViewHolder.get(convertView, R.id.iv_emoticon_item);
        Emoticon emoticon = getItem(position);
        if (emoticon != null) {
            boolean last = position == getCount() - 1;
            if (last) {
                //删除按钮
                image.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                image.setImageResource(R.drawable.icon_del);
            } else {
                image.setImageDrawable(EmoticonService.getInstance(getContext()).getEmoticon(emoticon.getUnicode()));
            }
        }
        return convertView;
    }
}
