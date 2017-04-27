package com.caijia.chat.service;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.caijia.chat.R;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cai.jia on 2015/12/2.
 */
public class EmoticonService {

    String[] emoticon3 = new String[]{
            ":blush:",
            ":open_mouth:",
            ":rage:",
            ":disappointed:",
            ":innocent:",
            ":scream:",
            ":confused:",
            ":smiley:",
            ":yum:",
            ":confounded:",
            ":sob:",
            ":grimacing:",
            ":mask:",
            ":wink:",
            ":sunglasses:",
            ":flushed:",
            ":laughing:",
            ":sleeping:",
            ":hushed:",
            ":sparkling_heart:",
            ":heart_eyes:",
            ":rose:",
            ":smirk:",
            ":broken_heart:",
            ":sun_with_face:",
            ":kissing_face:",
            ":moon:",
            ":+1:",
            ":star:",
            ":fallen_leaf:",
            ":kiss:",
            ":rainbow:",
            ":santa:",
            ":expressionless:",
            ":neutral_face:"
    };

    int[] sIconIds_3 = {R.drawable.emoji_1, R.drawable.emoji_2, R.drawable.emoji_3, R.drawable.emoji_4, R.drawable.emoji_5,R.drawable.emoji_6, R.drawable.emoji_7, R.drawable.emoji_8, R.drawable.emoji_9, R.drawable.emoji_10
            ,R.drawable.emoji_11, R.drawable.emoji_12, R.drawable.emoji_13, R.drawable.emoji_14, R.drawable.emoji_15,R.drawable.emoji_16, R.drawable.emoji_17, R.drawable.emoji_18, R.drawable.emoji_19, R.drawable.emoji_20
            ,R.drawable.emoji_21, R.drawable.emoji_22, R.drawable.emoji_23, R.drawable.emoji_24, R.drawable.emoji_25,R.drawable.emoji_26, R.drawable.emoji_27, R.drawable.emoji_28,R.drawable.emoji_29,R.drawable.emoji_30
            ,R.drawable.emoji_31, R.drawable.emoji_32, R.drawable.emoji_33, R.drawable.emoji_34, R.drawable.emoji_35
    };


    /**
     * key : emoticon的unicode
     * value : emoticon对应的icon
     */
    private Map<String, Drawable> mShowEmoticonMap;

    //所有支持的表情
    private Map<String, Drawable> mSupportEmoticonMap;

    /**
     * key的正则匹配pattern
     */
    private String mPattern;

    private static volatile EmoticonService manager = null;

    private EmoticonService(Context context) {
        mShowEmoticonMap = new LinkedHashMap<>();
        mSupportEmoticonMap = new LinkedHashMap<>();
    }

    public static EmoticonService getInstance(Context context) {
        if (manager == null) {
            synchronized (EmoticonService.class) {
                if (manager == null) {
                    manager = new EmoticonService(context.getApplicationContext());
                }
            }
        }
        return manager;
    }

    /**
     * 得到emoticon对应的路径
     *
     * @param unicode
     * @return
     */
    public Drawable getEmoticon(String unicode) {
        return mSupportEmoticonMap.get(unicode);
    }

    /**
     * 得到系统emoticon正则匹配的pattern -> [\uD83D\uDE04]
     *
     * @return
     */
    public String getPattern() {
        if (!TextUtils.isEmpty(mPattern)) {
            return mPattern;
        }
        StringBuilder sb = new StringBuilder();
        for (String key : mSupportEmoticonMap.keySet()) {
            String newKey = filter(key);
            if (!TextUtils.isEmpty(newKey)) {
                sb.append(newKey).append("|");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        mPattern = sb.toString();
        return mPattern;
    }

    /**
     * 过滤正则表达式中需要转义的符号
     *
     * @param key
     * @return
     */
    protected String filter(String key) {
        if (!TextUtils.isEmpty(key)) {
            return key.replace("[", "\\[").replace("]", "\\]")
                    .replace("$", "\\$").replace("(", "\\(").replace(")", "\\)")
                    .replace("*", "\\*").replace("+", "\\+").replace(".", "\\.")
                    .replace("?", "\\?").replace("\\", "\\").replace("^", "\\^")
                    .replace("{", "\\{").replace("}", "\\}").replace("|", "\\|");
        }
        return null;
    }

    /**
     * 显示在聊天页面的表情
     *
     * @return
     */
    public Map<String, Drawable> getShowEmoticonMap() {
        return mShowEmoticonMap;
    }

    /**
     * 所有支持的表情
     *
     * @return
     */
    public Map<String, Drawable> getSupportEmoticonMap() {
        return mSupportEmoticonMap;
    }


    /**
     * 加载emoticon对应到map中
     * 1. data/data/包名/cache/emoticon/emoticon.zip + support_emoticon.zip 解压
     * 2. data/data/包名/cache/emoticon/xxx表情.png + support_emoticon/xxx表情.png
     * 3. data/data/包名/cache/emoticon/xxx表情.png
     * 4. 将表情文件的文件名(emoticon的unicode编码)和Drawable放到map中
     */
    public void load(Context context) {
        mPattern = "";
        mShowEmoticonMap.clear();
        mSupportEmoticonMap.clear();
        long start = System.currentTimeMillis();
        //将assets 下的emoticon文件夹复制到sd卡中
        String emoticon = "emoticon";
        String supportEmoticon = "support_emoticon";
        String showEmoticon = "show_emoticon";
        String cachePath = context.getCacheDir().getAbsolutePath();
        FileUtil.copyAssetFile(context, emoticon, cachePath);

        //将sd卡中emoticon下zip文件解压
        File emoticonDir = new File(cachePath, emoticon);
        File[] zipFiles = emoticonDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String path = pathname.getAbsolutePath();
                return path.contains(".") && "zip".equals(path.substring(path.lastIndexOf(".") + 1));
            }
        });

        if (zipFiles != null && zipFiles.length > 0) {
            for (File file : zipFiles) {
                FileUtil.unZipFile(file.getAbsolutePath(), file.getParent());
                FileUtil.delete(file.getAbsolutePath());
            }
        }

        //支持的表情目录
        File supportEmoticonDir = new File(emoticonDir, supportEmoticon);

        //显示在聊天表情页面
        File showEmoticonDir = new File(emoticonDir, showEmoticon);

        //加表情文件转化为Drawable
        loadEmoticon(showEmoticonDir, mSupportEmoticonMap, mShowEmoticonMap);
        loadEmoticon(supportEmoticonDir, mSupportEmoticonMap);
        Resources res = context.getResources();
        if (emoticon3.length == sIconIds_3.length) {
            for (int i = 0; i < emoticon3.length; i++) {
                mSupportEmoticonMap.put(emoticon3[i], res.getDrawable(sIconIds_3[i]));
            }
        }
        System.out.println("copy emoticon time=" + (System.currentTimeMillis() - start));
        System.out.println("pattern=" + getPattern());
    }

    private void loadEmoticon(File emoticonDir, Map<String, Drawable>... maps) {
        File[] emoticonFiles = emoticonDir.listFiles();
        for (File file : emoticonFiles) {
            //表情文件
            if (file.exists() && file.isFile()) {
                try {
                    String name = file.getName();
                    String unicode = name.substring(0, name.lastIndexOf("."));
                    String key = String.valueOf(Character.toChars(Integer.parseInt(unicode, 16)));
                    Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
                    setEmoticonToMap(key, drawable, maps);
                } catch (Exception e) {
                    try {
                        String name = file.getName();
                        String key = name.substring(0, name.lastIndexOf("."));
                        Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
                        setEmoticonToMap(key, drawable, maps);
                    } catch (Exception e1) {

                    }
                }
            }

        }
    }

    private void setEmoticonToMap(String key, Drawable d, Map<String, Drawable>... maps) {
        if (maps != null && maps.length > 0) {
            for (Map<String, Drawable> map : maps) {
                map.put(key, d);
            }
        }
    }
}
