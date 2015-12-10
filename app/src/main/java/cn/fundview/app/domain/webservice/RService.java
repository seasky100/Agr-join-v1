package cn.fundview.app.domain.webservice;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.domain.model.Achv;
import cn.fundview.app.domain.model.AttentUser;
import cn.fundview.app.domain.model.Company;
import cn.fundview.app.domain.model.Expert;
import cn.fundview.app.domain.model.FundviewInfor;
import cn.fundview.app.domain.model.Org;
import cn.fundview.app.domain.model.Requ;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.FileTools;
import cn.fundview.app.tool.StringUtils;
import cn.fundview.app.tool.json.JSONTools;
import cn.fundview.app.view.UploadListener;

/**
 * 单例
 */
public class RService {

    public static final String TAG = "fundview";

    private RService() {}

    private static List<Achv> achvs;
    private static List<Expert> experts;
    private static List<Company> comps;
    private static List<Requ> requs;
    private static String result;

    /**
     * 获取专家成果列表 首页
     **/
    public static <T> List<T> getHome(int size, Class<T> clazz, String url) {

        try {
            // 解析json数据
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            FileTools tools = new FileTools();
            Map<String, String> textParams = new HashMap<String, String>();
            textParams.put("size", String.valueOf(size));
            textParams.put("page", "0");
            tools.setTextParams(textParams);
            InputStream inputStream = tools.doGet(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println("reader = " + reader);
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                inputStream.close();
                reader.close();

                List<T> list = JSONTools.parseListJson(stringBuffer.toString(), clazz);
                if (null != list && list.size() > 0) {

                    return list;
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
//            throw new Exception(e1);
        }
        return null;
    }

    /**
     * 成果搜索 根据关键字
     **/
    public static List<Achv> searchAchv(Map<String, String> param, int page, int pageSize) {
        try {
            // 解析json数据
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;

            FileTools tools = new FileTools();
            Map<String, String> textParams = new HashMap<String, String>();
            if (param != null) {

                Set<String> keys = param.keySet();
                if (keys != null && keys.size() > 0) {

                    for (String key : keys) {

                        textParams.put(key, param.get(key));
                    }
                }
            }
            textParams.put("page", page + "");
            textParams.put("size", pageSize + "");
            tools.setTextParams(textParams);
            InputStream inputStream = tools.doGet(Constants.GET_ACHV_LIST_URL);
            while ((len = inputStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            inputStream.close();
            // "{'companys': [{'areaNames': '山东,济南市','attention': 0,'id': 565, 'logo': 'http://findview.cn/static/thumb/proj_logo/20131008/2013100809532555681_186_136.jpg','name': '阳光集团','time': 1413105518000,'tradeNames': '林特产类'}, { 'areaNames': '湖南,娄底市','attention': 0, 'id': 556,'logo': 'http://findview.cn/static/thumb/proj_logo/20131118/2013111817442317250_186_136.png','name': 'sdfs','time': 1411720290000,'tradeNames': '粮食类'}]}";
            List<Achv> list = JSONTools.parseListJson(new String(outStream.toByteArray()), Achv.class);
            if (null == list || list.size() == 0)
                return null;
            return list;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * 专家搜索 根据关键字
     **/
    public static List<Expert> searchExpert(Map<String, String> param, int page, int pageSize) {
        try {
            // 解析json数据
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            String tradeName = param.get("tradeName");
            String areaName = param.get("areaName");
            String orderName = param.get("orderName");
            Map<String, String> map = new HashMap<String, String>();
            map.put("key", param.get("key"));
            if (!tradeName.equals("")) {

                // 有行业条件
                map.put("tradeName", param.get("tradeName"));
            }
            if (!areaName.equals("")) {

                // 有行业条件
                map.put("areaName", param.get("areaName"));
            }
            if (!orderName.equals("")) {

                map.put("orderName", param.get("orderName"));
            }

            FileTools tools = new FileTools();
            tools.setTextParams(map);
            InputStream inputStream = tools.doGet(Constants.GET_EXPERT_LIST_URL);
            while ((len = inputStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            inputStream.close();
            // String json =
            // "{'experts':[{'areaNames': '广东,中山市','attention': 0,'id': 555,'logo': 'http://192.168.1.133/static/thumb/expert/logo/20140928/2014092817221893840_80_60.jpg','name': 'zhangjia111','time': 1411725795000,'tradeNames': '油料类,食用油植物油'}]}";
            List<Expert> list = JSONTools.parseListJson(new String(outStream.toByteArray()), Expert.class);
            if (null != list && list.size() > 0) {
                return list;
            }
        } catch (Exception e1) {

            e1.printStackTrace();

        }
        return null;
    }

    /**
     * 需求搜索 根据关键字
     **/
    public static List<Requ> searchRequ(Map<String, String> param, int page, int size) {
        try {
            // 解析json数据
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;

            FileTools tools = new FileTools();
            Map<String, String> textParams = new HashMap<String, String>();
            textParams.put("key", param.get("key"));
            textParams.put("page", page + "");
            textParams.put("size", size + "");
            tools.setTextParams(textParams);
            InputStream inputStream = tools.doGet(Constants.GET_REQU_LIST_URL);
            while ((len = inputStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            inputStream.close();
            // "{'companys': [{'areaNames': '山东,济南市','attention': 0,'id': 565, 'logo': 'http://findview.cn/static/thumb/proj_logo/20131008/2013100809532555681_186_136.jpg','name': '阳光集团','time': 1413105518000,'tradeNames': '林特产类'}, { 'areaNames': '湖南,娄底市','attention': 0, 'id': 556,'logo': 'http://findview.cn/static/thumb/proj_logo/20131118/2013111817442317250_186_136.png','name': 'sdfs','time': 1411720290000,'tradeNames': '粮食类'}]}";
            List<Requ> list = JSONTools.parseListJson(new String(outStream.toByteArray()), Requ.class);
            if (null == list || list.size() == 0)
                return null;
            return list;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * 参展机构搜索 根据关键字
     **/
    public static List<Org> searchOrg(Map<String, String> param, int page, int size) {
        try {
            // 解析json数据
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;

            FileTools tools = new FileTools();
            Map<String, String> textParams = new HashMap<String, String>();
            textParams.put("key", param.get("key"));
            textParams.put("page", page + "");
            textParams.put("size", size + "");
            tools.setTextParams(textParams);
            InputStream inputStream = tools.doGet(Constants.GET_REQU_LIST_URL);
            while ((len = inputStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            inputStream.close();
            // "{'companys': [{'areaNames': '山东,济南市','attention': 0,'id': 565, 'logo': 'http://findview.cn/static/thumb/proj_logo/20131008/2013100809532555681_186_136.jpg','name': '阳光集团','time': 1413105518000,'tradeNames': '林特产类'}, { 'areaNames': '湖南,娄底市','attention': 0, 'id': 556,'logo': 'http://findview.cn/static/thumb/proj_logo/20131118/2013111817442317250_186_136.png','name': 'sdfs','time': 1411720290000,'tradeNames': '粮食类'}]}";
            List<Org> list = JSONTools.parseListJson(new String(outStream.toByteArray()), Org.class);
            if (null == list || list.size() == 0)
                return null;
            return list;
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * 上传用户的头像信息
     **/
    public static void updateProfileIcon1(int uid, String filePath) {
        try {
            File f = new File(filePath);

            URL url = new URL(Constants.PROFILE_ICON_SAVE_SERVER_URL);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            // 上传图片的一些参数设置
            uc.setRequestProperty("Accept", "image/gif,   image/x-xbitmap,   image/jpeg,   image/pjpeg,   application/vnd.ms-excel,   application/vnd.ms-powerpoint,   application/msword,   application/x-shockwave-flash,   application/x-quickviewplus,   */*");
            uc.setRequestProperty("Accept-Language", "zh-cn");
            uc.setRequestProperty("Content-type", "multipart/form-data;   boundary=---------------------------7d318fd100112");
            uc.setRequestProperty("Accept-Encoding", "gzip,   deflate");
            uc.setRequestProperty("User-Agent", "Mozilla/4.0   (compatible;   MSIE   6.0;   Windows   NT   5.1)");
            uc.setRequestProperty("Connection", "Keep-Alive");
            uc.setDoOutput(true);
            uc.setUseCaches(true);

            // 读取文件流
            int size = (int) f.length();
            byte[] data = new byte[size];
            FileInputStream fis = new FileInputStream(f);
            OutputStream out = uc.getOutputStream();
            fis.read(data, 0, size);
            String param = uid + "," + ".jpg";
            // 写入文件名
            out.write(param.getBytes());
            // 写入分隔符
            out.write('|');
            // 写入图片流
            out.write(data);
            out.flush();
            out.close();
            fis.close();

            // 读取响应数据
            int code = uc.getResponseCode();
            String sCurrentLine = "";
            // 存放响应结果
            String sTotalString = "";
            if (code == 200) {
                InputStream is = uc.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((sCurrentLine = reader.readLine()) != null) {
                    if (sCurrentLine.length() > 0) {
                        sTotalString = sTotalString + sCurrentLine.trim();
                    }
                }
            } else {
                sTotalString = "远程服务器连接失败,错误代码:" + code;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateProfileIcon(int accountId, String filePath, final UploadListener listener) {

        // 设置请求参数的编码
        RequestParams params = new RequestParams(); // 默认编码UTF-8
        File f = new File(filePath);
        // 添加文件
        params.addBodyParameter("img", f);

        // 用于非multipart表单的单文件上传
        //params.setBodyEntity(new FileUploadEntity(new File("/sdcard/test.zip"), "binary/octet-stream"));

        // 用于非multipart表单的流上传
        //params.setBodyEntity(new InputStreamUploadEntity(stream ,length));
        HttpUtils http = new HttpUtils();
        // 设置返回文本的编码， 默认编码UTF-8
        //http.configResponseTextCharset("GBK");

        // 自动管理 cookie
//      http.configCookieStore(preferencesCookieStore);
        http.send(HttpRequest.HttpMethod.POST,
                Constants.PROFILE_ICON_SAVE_SERVER_URL + "?accountId=" + accountId,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {

                        listener.onStart();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {

                        listener.onLoading(total, current, isUploading);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        resultText.setText("reply: " + responseInfo.result);
                        listener.onSuccess(responseInfo);
                    }


                    @Override
                    public void onFailure(HttpException error, String msg) {

                        //resultText.setText(msg);
                        listener.onFailure(error, msg);
                    }
                });
    }

    /**
     * 同步请求 必须在异步块儿中执行
     * 用户登录使用
     * 修改用户的基本信息字段的时候使用
     *
     * @param param 传递的参数
     * @param url   服务端url
     * @return 返回的json
     */
    public static String doPostSync(Map<String, String> param, String url) {
        RequestParams params = new RequestParams();
        if (param != null && param.size() > 0) {

            Set<String> keys = param.keySet();
            for (String key : keys) {

                /*try {*/
                params.addBodyParameter(key, /*URLEncoder.encode(param.get(key), "utf-8")*/param.get(key));
                /*} catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
            }
        }

        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1000 * 10);

        try {
            ResponseStream responseStream = http.sendSync(HttpRequest.HttpMethod.POST, url, params);
            int statusCode = responseStream.getStatusCode();
            //Header[] headers = responseStream.getBaseResponse().getAllHeaders();
            if (statusCode == 200)
                return responseStream.readString();
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 发送异步异步请求
     * @param url
     */
    public static void doAsync(String url) {

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST,url,
        new RequestCallBack(){
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
//                testTextView.setText(current + / + total);
            }

            @Override
            public void onSuccess(ResponseInfo responseInfo) {
//                textView.setText(responseInfo.result);
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailure(HttpException error, String msg) {
            }
        });

    }
}
