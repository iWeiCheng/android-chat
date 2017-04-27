package com.caijia.chat.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import com.caijia.chat.service.DeviceUtil;
import com.caijia.chat.service.EmoticonService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 可以添加和删除emoticon的TextView
 * Created by cai.jia on 2015/11/27.
 */
public class EmoticonTextView extends AppCompatTextView {

    public EmoticonTextView(Context context) {
        super(context);
    }

    public EmoticonTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmoticonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Pattern p = Pattern.compile(EmoticonService.getInstance(getContext()).getPattern(),
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);

        while (matcher.find()) {
            String unicode = matcher.group();
            Drawable drawable = EmoticonService.getInstance(getContext()).getEmoticon(unicode);
            if (drawable != null) {
                Drawable newDrawable = drawable.getConstantState().newDrawable();
                EmoticonSpan span = new EmoticonSpan(newDrawable, DeviceUtil.dp2px(getContext(), 16));
                int startPos = matcher.start();
                int endPos = matcher.end();
                builder.setSpan(span, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(builder, type);
    }
}
