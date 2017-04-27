package com.caijia.chat.service;

import android.content.Context;
import android.media.MediaRecorder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioManager {

    private MediaRecorder mediaRecorder;
    private String dir;
    private String currentFilePath;
    private Context context;

    private static volatile AudioManager audioInstance; // 单例

    public boolean isPrepared = false;

    private AudioManager(String dir,Context context) {
        this.dir = dir;
        this.context = context;
    }

    public interface AudioStateChangeListener {
        void wellPrepared();
    }

    public AudioStateChangeListener audioStateChangeListener;

    public void setOnAudioStateChangeListener(AudioStateChangeListener listener) {
        audioStateChangeListener = listener;
    }

    public static AudioManager getInstance(String dir,Context context) {
        if (audioInstance == null) {
            synchronized (AudioManager.class) {
                if (audioInstance == null) {
                    audioInstance = new AudioManager(dir,context.getApplicationContext());
                }
            }
        }
        return audioInstance;
    }

    public void prepareAudio() {
        try {
            isPrepared = false;
            File fileDir = new File(dir);
            if (!fileDir.exists())
                fileDir.mkdirs();
            String fileName = generateFileName();
            File file = new File(fileDir, fileName);

            currentFilePath = file.getAbsolutePath();
            mediaRecorder = new MediaRecorder();
            // 设置输出文件
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            // 设置音频源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            // 设置音频编码
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();
            mediaRecorder.start();
            // 准备结束
            isPrepared = true;
            //
            if (audioStateChangeListener != null) {
                audioStateChangeListener.wellPrepared();
            }
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成文件名称
     *
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared) {
            try {
                // 振幅范围mediaRecorder.getMaxAmplitude():1-32767
                return maxLevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
            }
        }
        return 1;
    }

    public void release() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        } catch (Exception e) {
            Toast.makeText(context, "请开启录音权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel() {
        release();
        if (currentFilePath != null) {
            File file = new File(currentFilePath);
            file.delete();
            currentFilePath = null;
        }
    }

    public String getCurrentPath() {
        return currentFilePath;
    }
}
