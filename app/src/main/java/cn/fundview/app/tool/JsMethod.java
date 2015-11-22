package cn.fundview.app.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.fundview.app.tool.json.JsonItem;

public class JsMethod {

    public static void main(String[] args) {

        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("a", "asdfa");
        jsonData.put("ab", "asdasdfafa");
        jsonData.put("c", "asdfaasdfaasdfa");
        String result = createJsWithJsonItems("javascript:addProj(${a}, ${ab}, ${c});", jsonData);

        System.out.println(result);
    }

    public static String createJs(String js, Object... objs) {

        Pattern p = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher m = p.matcher(js);

        StringBuffer sb = new StringBuffer();
        int i = 0;
        boolean result = m.find();
        while (result) {

            // 替换内容
            Object obj = objs[i];
            if (obj == null) {

                m.appendReplacement(sb, "null");
            } else {

                if (obj.getClass().getSimpleName().equals("String"))
                    m.appendReplacement(sb, "\'" + obj.toString().replaceAll("\n", "\\\\\\\\n").replaceAll("\r", "\\\\\\\\r").replaceAll("\"", "\\\\\\\\\'") + "\'");
                else
                    m.appendReplacement(sb, obj.toString());
            }

            // 查找下一个
            result = m.find();
            i++;
        }

        m.appendTail(sb);

        return sb.toString();

    }

    /**
     * 创建填充详细页面的js
     *
     * @param js       js模版
     * @param jsonData 从服务器/本地读取的json数据
     * @return 拼接完成的js字符串
     */
    public static String createJsWithJsonItems(String js, Map<String, String> jsonData) {

        Pattern p = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher m = p.matcher(js);

        StringBuffer sb = new StringBuffer();
        int i = 0;
        boolean result = m.find();
        if (jsonData != null && jsonData.size() > 0) {

            while (result) {

                // 替换内容
                String key = m.group();
                key = key.substring(2, key.length() - 1);
                System.out.println(key);
                String value = jsonData.get(key);
                if (value == null) {

                    m.appendReplacement(sb, "null");
                } else {

                    m.appendReplacement(sb, "\"" + value.replaceAll("\n", "\\\\\\\\n").replaceAll("\r", "\\\\\\\\r").replaceAll("\"", "\\\\\\\\\"") + "\"");

                }

                // 查找下一个
                result = m.find();
            }
        }
        m.appendTail(sb);

        return sb.toString();

    }

}
