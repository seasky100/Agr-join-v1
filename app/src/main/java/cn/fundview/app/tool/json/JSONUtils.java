package cn.fundview.app.tool.json;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import cn.fundview.app.tool.StringUtils;

/**
 * Json Utils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class JSONUtils {

    public static boolean isPrintException = true;

    private JSONUtils() {

    }

    /**
     * get Long from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getLong(JSONObject, String, JSONObject)}</li>
     * </ul>
     */
    public static long getLongValue(String jsonData, String key, long defaultValue) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getLongValue(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     * @see JSONUtils#getLong(JSONObject, String, Long)
     */
    public static Long getLongValue(String jsonData, String key, Long defaultValue) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getLong(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Int from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getInt(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getInt(String)}</li>
     * </ul>
     */
    public static Integer getIntegerValue(String jsonData, String key, Integer defaultValue) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getInteger(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Int from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getInt(JSONObject, String, JSONObject)}</li>
     * </ul>
     */
    public static int getIntValue(String jsonData, String key, int defaultValue) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getIntValue(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Double from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getDouble(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getDouble(String)}</li>
     * </ul>
     */
    public static Double getDouble(String jsonData, String key, Double defaultValue) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getDouble(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Double from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getDouble(JSONObject, String, JSONObject)}</li>
     * </ul>
     */
    public static double getDoubleValue(String jsonData, String key, double defaultValue) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {
            return defaultValue;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getDoubleValue(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getString(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getString(String)}</li>
     * </ul>
     */
    public static String getString(String jsonData, String key, String defaultValue) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getString(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get String array from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getJSONArray(String)} exception, return
     * defaultValue</li>
     * <li>if {@link JSONArray#getString(int)} exception, return
     * defaultValue</li>
     * <li>return string array</li>
     * </ul>
     */
    public static String[] getStringArray(String jsonData, String key) {

        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {

            return null;
        }

        try {

            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                String[] value = new String[statusArray.size()];
                for (int i = 0; i < statusArray.size(); i++) {
                    value[i] = statusArray.getString(i);
                }
                return value;
            }
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    /**
     * get String list from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getJSONArray(String)} exception, return
     * defaultValue</li>
     * <li>if {@link JSONArray#getString(int)} exception, return
     * defaultValue</li>
     * <li>return string array</li>
     * </ul>
     */
    public static List<String> getStringList(String jsonData, String key) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {

            return null;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            JSONArray statusArray = jsonObject.getJSONArray(key);
            if (statusArray != null) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < statusArray.size(); i++) {
                    list.add(statusArray.getString(i));
                }
                return list;
            }
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }


    /**
     * get JSONObject from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonData is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getJSONObject(JSONObject, String, JSONObject)}</li>
     * </ul>
     */
    public static JSONObject getJSONObject(String jsonData, String key, JSONObject defaultValue) {

        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {

            return null;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get JSONArray from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>if {@link JSONObject#getJSONArray(String)} exception, return
     * defaultValue</li>
     * <li>return {@link JSONObject#getJSONArray(String)}</li>
     * </ul>
     */
    public static JSONArray getJSONArray(String jsonData, String key, JSONArray defaultValue) {

        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {

            return null;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get Boolean from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if key is null or empty, return defaultValue</li>
     * <li>return {@link JSONObject#getBoolean(String)}</li>
     * </ul>
     */
    public static Boolean getBoolean(String jsonData, String key, Boolean defaultValue) {

        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {

            return defaultValue;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getBoolean(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    /**
     * get boolean from jsonData
     *
     * @param jsonData
     * @param key
     * @param defaultValue
     * @return <ul>
     * <li>if jsonObject is null, return defaultValue</li>
     * <li>if jsonData {@link JSONObject#JSONObject(String)} exception,
     * return defaultValue</li>
     * <li>return
     * {@link JSONUtils#getBoolean(JSONObject, String, Boolean)}</li>
     * </ul>
     */
    public static boolean getBooleanValue(String jsonData, String key, boolean defaultValue) {
        if (StringUtils.isEmpty(jsonData) || StringUtils.isEmpty(key)) {

            return defaultValue;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonData);
            return jsonObject.getBooleanValue(key);
        } catch (JSONException e) {
            if (isPrintException) {
                e.printStackTrace();
            }
            return defaultValue;
        }
    }

    public static <T> T getBean(String jsonData, Class<T> clazz) {

        return JSONObject.parseObject(jsonData, clazz);
    }

}
