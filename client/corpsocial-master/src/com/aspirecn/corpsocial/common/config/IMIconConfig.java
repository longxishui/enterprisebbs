package com.aspirecn.corpsocial.common.config;

import android.content.Context;

import com.aspiren.corpsocial.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenziqiang
 */
public class IMIconConfig {
    static int[] deptColor = new int[]{R.drawable.addrbook_dept_bg1,
            R.drawable.addrbook_dept_bg2, R.drawable.addrbook_dept_bg3,
            R.drawable.addrbook_dept_bg4};
    static int[] groupColor = new int[]{R.drawable.addrbook_group_bg1,
            R.drawable.addrbook_group_bg2};
    private static String DeptIconKey = "IM_ICON_DEPT_CONFIG";
    private static String GroupIconKey = "IM_ICON_GROUP_CONFIG";
    private static ImIconConfig deptConfig = loadConfig("dept");
    private static ImIconConfig groupConfig = loadConfig("group");

    /**
     * 部门群获取图标
     *
     * @param deptId 部门ID
     * @return 部门图标
     */
    public static int getDeptColor(String deptId) {
        return getColor("dept", deptId);
    }

    /**
     * 自建群获取图标
     *
     * @param groupId 自建群ID
     * @return 群图标
     */
    public static int getGroupColor(String groupId) {
        return getColor("group", groupId);
    }

    /**
     * @param type 群类型
     * @param id   群ID
     * @return 返回群图片
     */
    private static int getColor(String type, String id) {
        int index = -1;
        if (type.equals("dept")) {
            index = deptConfig.getIndex(id);
            if (index < 0) {
                deptConfig.current = 4;
                deptConfig.add(id);
                saveConfig("dept");
                index = getColor(type, id);
                return index;
            }
            return deptColor[index - 1];
        } else {
            index = groupConfig.getIndex(id);
            if (index < 0) {
                groupConfig.current = 2;
                groupConfig.add(id);
                saveConfig("group");
                index = getColor(type, id);
                return index;
            }
            return groupColor[index - 1];
        }

    }

    /**
     * 将map转换成字符串，并保存
     *
     * @param type
     */

    private static void saveConfig(String type) {
        if (type.equals("dept")) {
            Config.getInstance().setValue(DeptIconKey, deptConfig.toString());
        } else {
            Config.getInstance().setValue(GroupIconKey, groupConfig.toString());
        }
    }

    /**
     * 取出字符串，转换成MAP
     *
     * @param type 群类型
     * @return 群map
     */
    private static ImIconConfig loadConfig(String type) {
        if (type.equals("dept")) {
            String str = Config.getInstance().getStringValue(DeptIconKey, "-1");
            return ImIconConfig.fromString(str);
        } else {
            String str = Config.getInstance()
                    .getStringValue(GroupIconKey, "-1");
            return ImIconConfig.fromString(str);
        }
    }

    /**
     * 设置字体颜色
     *
     * @param groupId
     * @param context
     * @return
     */
    public static int getTextColor(String groupId, Context context) {
        int mColor = 0;
        int pointer = deptConfig.getIndex(groupId);
        switch (pointer) {
            case 1:
                mColor = context.getResources().getColor(
                        R.color.addrbook_dept_textcolor_1);
                break;
            case 2:
                mColor = context.getResources().getColor(
                        R.color.addrbook_dept_textcolor_2);
                break;
            case 3:
                mColor = context.getResources().getColor(
                        R.color.addrbook_dept_textcolor_3);
                break;
            default:
                mColor = context.getResources().getColor(
                        R.color.addrbook_dept_textcolor_4);
                break;
        }
        return mColor;
    }

    /**
     * 群图片的索引操作
     *
     * @author chenziqiang
     */
    private static class ImIconConfig {
        static Map<String, Integer> map = new HashMap<String, Integer>();
        static int index = 1;
        int current = 0;

        /**
         * String转换成map
         *
         * @param str 需要转换的String
         * @return map
         */
        @SuppressWarnings("static-access")
        public static ImIconConfig fromString(String str) {
            ImIconConfig result = new ImIconConfig();
            if (!str.equals("-1")) {
                String[] array = str.split("\\|");
                index = Integer.valueOf(array[0]);
                for (String item : array) {
                    if (!item.equals(array[0])) {
                        int line = item.indexOf("_");
                        String key = item.substring(0, line);
                        int contant = Integer.valueOf(item.substring(line + 1));
                        map.put(key, contant);
                    }
                }
                result.index = index;
                result.map = map;
            }
            return result;

        }

        /**
         * map转换成String
         */
        @Override
        public String toString() {
            StringBuilder strbuff = new StringBuilder(index + "|");
            for (String itemkey : map.keySet()) {
                strbuff.append(itemkey + "_" + map.get(itemkey) + "|");
            }
            return strbuff.toString();
        }

        /**
         * 获取索引
         *
         * @param id 群id
         * @return 群图片索引
         */
        public int getIndex(String id) {
            if (map.containsKey(id)) {
                return map.get(id);
            }
            return -1;
        }

        /**
         * 创建并保存索引
         *
         * @param id      群图标
         * @param current 索引最大值
         * @return 增长的index
         */

        public int add(String id) {

            if (index >= current) {
                index = 1;
            } else {
                index++;
            }
            map.put(id, index);
            return index;
        }
    }
}
