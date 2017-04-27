package com.caijia.chat.view;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.caijia.chat.service.DeviceUtil;
import com.caijia.chat.service.EmoticonService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cai.jia on 2015/12/2.
 */
public class EmoticonHandler implements TextWatcher {

    TextView mTextView;

    private List<ImageSpan> mRemoveSpanList;

    public EmoticonHandler(TextView textView) {
        mRemoveSpanList = new ArrayList<>();
        mTextView = textView;
        mTextView.addTextChangedListener(this);
    }

    /**
     * (删除) -> count
     *
     * @param s     表示之前的文本内容
     * @param start 添加时 ->  光标开始的位置,删除时为光标结束位置
     * @param count 删除时 ->  添加的字符个数,添加时为0
     * @param after 添加时 ->  添加的字符个数,删除时为0
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count > 0) {
            //表示删除,找到删除中是否包含imageSpan
            Editable message = mTextView.getEditableText();
            ImageSpan[] spans = message.getSpans(start, start + count, ImageSpan.class);
            if (spans != null && spans.length > 0) {
                Collections.addAll(mRemoveSpanList, spans);
            }
        }
    }

    /**
     * @param s 当前文本内容
     */
    @Override
    public void afterTextChanged(Editable s) {
        Editable message = mTextView.getEditableText();
        if (!mRemoveSpanList.isEmpty()) {
            for (ImageSpan span : mRemoveSpanList) {
                message.removeSpan(span);
            }
            mRemoveSpanList.clear();
        }
    }

    /**
     * (添加) -> count
     *
     * @param s      表示当前的文本内容
     * @param start  添加时 ->  光标开始的位置,删除时为光标结束位置
     * @param before 删除时 -> 删除的字符个数,添加时为0
     * @param count  添加时 -> 添加的字符个数,删除时为0
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            //添加的字符中是否包含表情编码
            CharSequence text = s.subSequence(start, start + count);
            Editable editable = mTextView.getEditableText();

            Pattern p = Pattern.compile(EmoticonService.getInstance(mTextView.getContext()).getPattern(),
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(text);

            while (matcher.find()) {
                String unicode = matcher.group();
                Drawable drawable = EmoticonService.getInstance(mTextView.getContext()).getEmoticon(unicode);
                if (drawable != null) {
                    Drawable newDrawable = drawable.getConstantState().newDrawable();
                    EmoticonSpan emoticonSpan = new EmoticonSpan(newDrawable, DeviceUtil.dp2px(mTextView.getContext(), 16));
                    int startPos = matcher.start() + start;
                    int endPos = matcher.end() + start;
                    editable.setSpan(emoticonSpan, startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}
