package com.caijia.chat.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caijia.chat.R;
import com.caijia.chat.service.KeyboardUtils;
import com.caijia.chat.service.SharedPreferenceUtil;

/**
 * Created by cai.jia on 2016/3/28 0028.
 */
public class ChatToolbar extends LinearLayout implements View.OnClickListener, TextWatcher, AudioRecordButton.AudioRecordFinishListener, MeasureKeyboardLayout.OnSoftKeyboardStateChangeListener {

    public static final int OPEN_SOFT_KEYBOARD = 0x1;
    public static final int OPEN_EMOTICON = 0x2;
    public static final int OPEN_MORE_FUNCTION = 0x3;
    public static final int OPEN_VOICE = 0x4;
    public static final int CLOSE = 0x5;

    public static final String SOFT_KEYBROAD_HEIGHT = "soft_keybroad_height";

    private ImageView mVoice;

    private ImageView mAdd;

    private ImageView mEmoticon;

    private TextView mSend;

    private EmoticonEditText mMessage;

    private AudioRecordButton mRecordButton;

    private FrameLayout mBottomLayout;

    private RelativeLayout mMessageLayout;

    private int mSoftKeyboardHeight;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public ChatToolbar(Context context) {
        super(context);
        init(context);
    }

    public ChatToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ChatToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setMeasureKeyboardLayout(MeasureKeyboardLayout keyboardLayout) {
        keyboardLayout.setSoftKeyboardChangeListener(this);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_chat_tool_bar, this, true);

        mVoice = (ImageView) view.findViewById(R.id.voice);
        mAdd = (ImageView) view.findViewById(R.id.add);
        mEmoticon = (ImageView) view.findViewById(R.id.emoticon);
        mMessage = (EmoticonEditText) view.findViewById(R.id.message);
        mRecordButton = (AudioRecordButton) view.findViewById(R.id.audio_record);
        mBottomLayout = (FrameLayout) view.findViewById(R.id.bottom_layout);
        mMessageLayout = (RelativeLayout) findViewById(R.id.message_layout);
        mSend = (TextView) findViewById(R.id.send);

        mVoice.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mEmoticon.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mMessage.addTextChangedListener(this);
        mRecordButton.setAudioRecordFinishListener(this);

        //如果本地手机没有保存软键盘高度,第一次弹出软键盘测量
        int keyboardHeight = getPreferenceSoftKeyboardHeight();
        if (getPreferenceSoftKeyboardHeight() == 0) {
            mMessage.requestFocus();
        } else {
            mSoftKeyboardHeight = keyboardHeight;
        }
    }

    private int getPreferenceSoftKeyboardHeight() {
        return SharedPreferenceUtil.getSharedPreferences(getContext())
                .getInt(SOFT_KEYBROAD_HEIGHT, 0);
    }

    private void saveSoftKeyboardHeight(int softKeyboardHeight) {
        if (getPreferenceSoftKeyboardHeight() == 0) {
            SharedPreferenceUtil.getEditor(getContext())
                    .putInt(SOFT_KEYBROAD_HEIGHT, softKeyboardHeight)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        boolean popToolbar = isPopToolbar();

        if (v == mVoice) {
            boolean isVoiceType = mRecordButton.isShown();
            changeState(isVoiceType ? OPEN_SOFT_KEYBOARD : OPEN_VOICE);

        } else if (v == mAdd) {
            if (!popToolbar) {
                changeState(OPEN_MORE_FUNCTION);

            } else {
                changeState(mCurState == OPEN_MORE_FUNCTION ? OPEN_SOFT_KEYBOARD : OPEN_MORE_FUNCTION);
            }

        } else if (v == mEmoticon) {
            if (!popToolbar) {
                changeState(OPEN_EMOTICON);

            } else {
                changeState(mCurState == OPEN_EMOTICON ? OPEN_SOFT_KEYBOARD : OPEN_EMOTICON);
            }

        } else if (v == mSend) {
            if (actionListener != null) {
                actionListener.onSendTextMessage(mMessage.getText().toString());
                mMessage.setText("");
            }
        }
    }

    private int mCurState;

    private void changeState(int state) {
        this.mCurState = state;
        switch (state) {
            case OPEN_SOFT_KEYBOARD:
                mMessageLayout.setVisibility(VISIBLE);
                mRecordButton.setVisibility(GONE);
                mVoice.setSelected(false);
                mEmoticon.setSelected(false);
                toggleSendButton();
                mMessage.requestFocus();
                setBottomLayoutHeight(mSoftKeyboardHeight);
                KeyboardUtils.openSoftKeyboard(mMessage);
                break;

            case OPEN_EMOTICON:
                mEmoticon.setSelected(true);
                KeyboardUtils.closeSoftKeyboard(mMessage);
                setBottomLayoutHeight(mSoftKeyboardHeight);

                if (actionListener != null) {
                    actionListener.onSwitchToEmoticon();
                }
                break;

            case OPEN_MORE_FUNCTION:
                mEmoticon.setSelected(false);
                mMessageLayout.setVisibility(VISIBLE);
                mRecordButton.setVisibility(GONE);
                mVoice.setSelected(false);
                mEmoticon.setSelected(false);
                KeyboardUtils.closeSoftKeyboard(mMessage);
                setBottomLayoutHeight(mSoftKeyboardHeight);

                if (actionListener != null) {
                    actionListener.onSwitchToMoreFunction();
                }
                break;

            case OPEN_VOICE:
                mMessageLayout.setVisibility(GONE);
                mRecordButton.setVisibility(VISIBLE);
                mSend.setVisibility(INVISIBLE);
                mAdd.setVisibility(VISIBLE);
                mVoice.setSelected(true);
                KeyboardUtils.closeSoftKeyboard(mMessage);
                setBottomLayoutHeight(0);
                break;

            case CLOSE:
                mEmoticon.setSelected(false);
                KeyboardUtils.closeSoftKeyboard(mMessage);
                setBottomLayoutHeight(0);
                break;
        }
    }

    private void setBottomLayoutHeight(final int height) {
        boolean same = mBottomLayout.getLayoutParams().height == height;
        if (same) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mBottomLayout.getLayoutParams();
                layoutParams.height = height;
                mBottomLayout.setLayoutParams(layoutParams);
            }
        });
    }

    public boolean isClose() {
        return mBottomLayout.getLayoutParams().height <= 0;
    }

    private boolean isPopToolbar() {
        return mBottomLayout.getHeight() != 0;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        toggleSendButton();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void toggleSendButton() {
        String message = mMessage.getText().toString();
        boolean isSend = !TextUtils.isEmpty(message) && message.length() > 0;
        mSend.setVisibility(isSend ? VISIBLE : INVISIBLE);
        mAdd.setVisibility(isSend ? GONE : VISIBLE);
    }

    /**
     * 录音完成
     *
     * @param second   录音时间
     * @param filePath 录音文件路径
     */
    @Override
    public void onFinish(float second, String filePath) {
        if (actionListener != null) {
            actionListener.onSendVoiceMessage(filePath, (long) second);
        }
    }

    public void close() {
        changeState(CLOSE);
    }

    @Override
    public void onSoftKeyboardStateChange(boolean open, int softKeyboardHeight) {
        if (open) {
            System.out.println("height=" + softKeyboardHeight);
            mSoftKeyboardHeight = softKeyboardHeight;
            saveSoftKeyboardHeight(softKeyboardHeight);
            changeState(OPEN_SOFT_KEYBOARD);
        } else {
            if (mCurState == OPEN_SOFT_KEYBOARD) {
                changeState(CLOSE);
            }
        }
    }

    public interface OnToolbarActionListener {

        /**
         * 发送消息
         *
         * @param message 文本消息
         */
        void onSendTextMessage(String message);

        /**
         * 发送语言消息
         *
         * @param path   录音文件路径
         * @param length 录音文件长度
         */
        void onSendVoiceMessage(String path, long length);

        /**
         * 切换到emoticon页面
         */
        void onSwitchToEmoticon();

        /**
         * 切换到更多功能页面
         */
        void onSwitchToMoreFunction();
    }

    private OnToolbarActionListener actionListener;

    public void setOnToolbarActionListener(OnToolbarActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public EmoticonEditText getEmoticonEditText() {
        return mMessage;
    }
}
