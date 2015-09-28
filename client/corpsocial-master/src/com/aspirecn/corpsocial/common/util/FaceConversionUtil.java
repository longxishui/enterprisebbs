package com.aspirecn.corpsocial.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.aspirecn.corpsocial.bundle.im.domain.ChatEmoji;
import com.aspirecn.corpsocial.common.config.FacePackageColumns;
import com.aspiren.corpsocial.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FaceConversionUtil {


    private static FaceConversionUtil mFaceConversionUtil;
    /**
     * 表情分页的结果集合
     */
    //public List<List<ChatEmoji>> emojiLists = new ArrayList<List<ChatEmoji>>();
    public List<List<ChatEmoji>> emojiLists;
    /**
     * 每一页表情的个数
     */
    private int pageSize = 20;
    /**
     * 保存于内存中的表情HashMap
     */
    //private HashMap<String, String> emojiMap = new HashMap<String, String>();
    private HashMap<String, String> emojiMap;
    /**
     * 保存于内存中的表情集合
     */
    //private List<ChatEmoji> emojis = new ArrayList<ChatEmoji>();
    private List<ChatEmoji> emojis;
    private Map<String, Bitmap> imageCache = new HashMap<String, Bitmap>();

    private FaceConversionUtil() {
    }

    public static FaceConversionUtil getInstace() {
        if (mFaceConversionUtil == null) {
            mFaceConversionUtil = new FaceConversionUtil();
        }
        return mFaceConversionUtil;
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public SpannableString getExpressionString(Context context, String str) {
        getFileText(context);
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
        String zhengze = "\\[[^\\]]+\\]";
        // 通过传入的正则表达式来生成一个pattern
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            //Log.e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 添加表情
     *
     * @param context
     * @param imgId
     * @param spannableString
     * @return
     */
    public SpannableString addFace(Context context, String path,
                                   String spannableString) {
        if (TextUtils.isEmpty(spannableString)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //设置输入框的表情的大小
        int dp = DensityUtil.dip2px(context, 25);
        bitmap = Bitmap.createScaledBitmap(bitmap, dp, dp, true);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannable = new SpannableString(spannableString);
        spannable.setSpan(imageSpan, 0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            String path = FacePackageColumns.FACE_PACKAGE_UN_FACE_PATH + "/" + value;
            if (!imageCache.containsKey(path)) {
                if (new File(path).exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    int dp = DensityUtil.dip2px(context, 40);
                    bitmap = Bitmap.createScaledBitmap(bitmap, dp, dp, true);
                    // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                    imageCache.put(path, bitmap);
                }
            }
            spannableString.setSpan(new ImageSpan(imageCache.get(path)), matcher.start(), matcher.end(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 读取配置文件中的表情
     *
     * @param context
     */
    public void getFileText(Context context) {
        if (emojiMap == null) {
            ParseData(FileUtils.getEmojiFile(context), context);
        }
    }

    /**
     * 解析字符,获取表情资源
     *
     * @param data
     */
    private void ParseData(List<String> data, Context context) {
        emojiMap = new HashMap<String, String>();
        emojis = new ArrayList<ChatEmoji>();
        emojiLists = new ArrayList<List<ChatEmoji>>();
        if (data == null) {
            return;
        }
        ChatEmoji emojEentry;
        try {
            for (String str : data) {
                if (TextUtils.isEmpty(str.trim())) {
                    continue;
                }
                String[] text = str.split(",");

                if (text.length != 2) {
                    continue;
                }
                //System.out.println("fileName"+text[0]);
                String fileName = text[0];
                //.substring(0, text[0].lastIndexOf("."));
                emojiMap.put(text[1], fileName);

                //System.out.println(text[1]+":"+fileName);
                /*int resID = context.getResources().getIdentifier(fileName,
                        "drawable", context.getPackageName());*/
                //System.out.println(new File(path).exists());
                //String path = FacePackageColumns.FACE_PACKAGE_UN_FACE_PATH+"/"+fileName+".png";
                String path = FacePackageColumns.FACE_PACKAGE_UN_FACE_PATH + "/" + fileName;
                //System.out.println(path);
                if (new File(path).exists()) {
                    emojEentry = new ChatEmoji();
                    emojEentry.setId(1);
                    emojEentry.setPath(path);
                    emojEentry.setCharacter(text[1]);
                    emojEentry.setFaceName(fileName);
                    emojis.add(emojEentry);
                    //System.out.println("emojis.size():"+emojis.size());
                }
            }

            //System.out.println("emojis.size():"+emojis.size());
            int pageCount = (int) Math.ceil(emojis.size() / 20 + 0.1);

            for (int i = 0; i < pageCount; i++) {
                emojiLists.add(getData(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分页数据
     *
     * @param page
     * @return
     */
    private List<ChatEmoji> getData(int page) {
        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        if (endIndex > emojis.size()) {
            endIndex = emojis.size();
        }
        // 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
        List<ChatEmoji> list = new ArrayList<ChatEmoji>();
        list.addAll(emojis.subList(startIndex, endIndex));
        if (list.size() < pageSize) {
            for (int i = list.size(); i < pageSize; i++) {
                ChatEmoji object = new ChatEmoji();
                list.add(object);
            }
        }
        if (list.size() == pageSize) {
            ChatEmoji object = new ChatEmoji();
            object.setId(R.drawable.im_chat_face_del_icon_selector);
            list.add(object);
        }
        return list;
    }

}

