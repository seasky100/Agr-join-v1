package cn.fundview.app.tool.json;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.model.ResultBean;
import cn.fundview.app.model.ResultListBean;
import cn.fundview.app.tool.FileTools;

public class JSONTools {

    public static Map<String, String> parseJsonFile(File jsonFile) {

        Map<String, String> result = new HashMap<>();
        String jsonStr = FileTools.readFileContent(jsonFile);

        if (null != jsonStr && !"".equals(jsonStr)) {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(jsonStr);

            if (jsonObject != null && jsonObject.size() > 0) {

                Set<String> keys = jsonObject.keySet();
                for (String key : keys) {

                    result.put(key, jsonObject.getString(key));
                }
            }

            return result;
        }

        return null;
    }

    // 解析json 列表
    public static <T> List<T> parseListJson(String jsonData, Class<T> clazz) throws JSONException {
        System.out.println(jsonData);
        List<T> result = null;
        if (null != jsonData && !"".equals(jsonData)) {
            result = new ArrayList<T>();
            result = com.alibaba.fastjson.JSON.parseArray(jsonData, clazz);
        }
        return result;
    }

    // 解析json 列表
    public static <T> ResultListBean<T> parseList(String jsonData, Class<T> clazz) throws JSONException {
        System.out.println(jsonData);
        ResultListBean<T> result = null;
        List<T> resultList = null;
        if (null != jsonData && !"".equals(jsonData)) {

            result = JSON.parseObject(jsonData, ResultListBean.class);
            resultList = JSON.parseArray(result.getResultList(), clazz);
            result.setList(resultList);
        }
        return result;
    }

    /**
     * 解析{"message":"登录成功!","result":{"emailChecked":false,"id":4,"isAuth":false,"type":2,"username":"keji001","valid":false},"status":2}
     */
    public static ResultBean parseResult(String jsonData) throws Exception {

        if (null != jsonData && !"".equals(jsonData)) {
            JSONObject json = new JSONObject(jsonData);
            ResultBean resultBean = new ResultBean();

            resultBean.setStatus(json.getInt("status"));
            resultBean.setMessage(json.getString("message"));
            if (json.getInt("status") == Constants.REQUEST_SUCCESS) {

                try {

                    String result = json.getString("result");
                    resultBean.setResult(result);
                } catch (JSONException e) {

                    e.printStackTrace();
                    resultBean.setResult("");
                    return resultBean;
                }
            }
            return resultBean;
        }
        return null;
    }


}
