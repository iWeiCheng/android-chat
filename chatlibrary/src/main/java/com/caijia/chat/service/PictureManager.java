package com.caijia.chat.service;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.caijia.chat.morefunc.entities.PictureGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 得到手机里面的图片
 * Created by cai.jia on 2016/4/7 0007.
 */
public class PictureManager {

    private Context context;

    private PictureManager(Context context) {
        this.context = context;
    }

    private static volatile PictureManager instance = null;

    public static PictureManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PictureManager.class) {
                if (instance == null) {
                    instance = new PictureManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void getLocalPicture(final OnGetLocalPictureListener listener) {
        new AsyncTask<Void, Void, List<String>>() {

            @Override
            protected void onPostExecute(List<String> list) {
                if (listener != null) {
                    listener.onGetPictureFinish(list);
                }
            }

            @Override
            protected List<String> doInBackground(Void... params) {
                List<String> pathList = new ArrayList<>();
                //查询手机里面的所有图片
                Cursor cursor = context.getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? and "
                                + MediaStore.Images.Media.SIZE + ">?",
                        new String[]{"image/jpeg", "0"}, MediaStore.Images.Media.DATE_MODIFIED + " desc");
                while (cursor != null && cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(path);
                    if (file.exists()) {
                        pathList.add(path);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                return pathList;
            }
        }.execute();
    }

    public List<PictureGroup> toPictureGroup(List<String> pathList) {
        List<PictureGroup> groupList = new ArrayList<>();
        if (pathList == null || pathList.isEmpty()) {
            return groupList;
        }

        Map<String, List<String>> map = new HashMap<>();
        for (String path : pathList) {
            String parentPath = new File(path).getParentFile().getAbsolutePath();
            if (map.containsKey(parentPath)) {
                List<String> list = map.get(parentPath);
                list.add(path);
            } else {
                List<String> list = new ArrayList<>();
                list.add(path);
                map.put(parentPath, list);
            }
        }

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            PictureGroup group = new PictureGroup();
            String parentPath = entry.getKey();
            group.setGroupName(parentPath.substring(parentPath.lastIndexOf(File.separator) + 1));
            group.setPicturePaths(entry.getValue());
            groupList.add(group);
        }
        return groupList;
    }

    public interface OnGetLocalPictureListener {
        void onGetPictureFinish(List<String> list);
    }
}
