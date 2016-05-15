package cn.fundview.app.tool.file;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.StringUtils;

/**
 * POST和GET提交工具类
 *
 * @author ouda
 */
public class FileTools {

    public static final String TAG = "fundview";
    public static final int BUFFSIZE = 1024 * 200;

    private URL url;

    private HttpURLConnection conn;

    private String boundary = "--------www.fundview.cn";
    private Map<String, String> textParams = new HashMap<String, String>();

    private Map<String, File> fileparams = new HashMap<String, File>();

    private Map<String, String> headParams = new HashMap<String, String>();

    private DataOutputStream ds = null;

    public FileTools(String url) throws Exception {
        this.url = new URL(url);
    }

    public FileTools() {
    }

    public void setUrl(String url) throws Exception {
        this.url = new URL(url);
    }

    /**
     * 设置文本字段集合
     *
     * @param textParams 字段集合
     */
    public void setTextParams(Map<String, String> textParams) {
        this.textParams = textParams;
    }

    /**
     * 设置文件参数集合
     *
     * @param fileparams 文件集合
     */
    public void setFileparams(Map<String, File> fileparams) {
        this.fileparams = fileparams;
    }

    /**
     * @param name
     * @param value
     */
    public void addTextParameter(String name, String value) {
        textParams.put(name, value);
    }

    public void addFileParameter(String name, File value) {
        fileparams.put(name, value);
    }

    public void addHeadParameters(String key, String value) {
        headParams.put(key, value);
    }

    public void clearAllParameters() {
        textParams.clear();
        fileparams.clear();
        headParams.clear();
    }

    /**
     * 发送post 请求
     **/
    public InputStream doPost() throws Exception {
        initConnection();
        try {
            conn.connect();
        } catch (SocketTimeoutException e) {

            throw new RuntimeException();
        }
        try {
            ds = new DataOutputStream(conn.getOutputStream());
            writeFileParams();
            writeStringParams();
            paramsEnd();
        } finally {
            if (ds != null) {
                ds.flush();
                ds.close();
            }
        }
        return conn.getInputStream();
    }

    public InputStream doPostJson(String json) throws Exception {

        initConnection();
        try {

            conn.connect();
            ds = new DataOutputStream(conn.getOutputStream());

            ds.write(json.getBytes());

            int code = conn.getResponseCode();
            // 存放响应结果
            if (code == 200) {

                return conn.getInputStream();
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        } finally {
            if (ds != null) {
                ds.flush();
                ds.close();
            }
        }
        return null;
    }

    public InputStream doGet(String url) throws Exception {

        if (url == null || url.trim().equals(""))
            return null;

        StringBuffer sb = new StringBuffer();
        sb.append("?");
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = this.encode(textParams.get(name), "UTF-8");
            sb.append(name).append("=").append(value).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        this.setUrl(url + sb.toString());
        conn = (HttpURLConnection) this.url.openConnection();
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(2000);
        conn.setRequestMethod("GET");
        conn.setDoOutput(false);
        conn.setDoInput(true);
        this.writeHeads();

        try {
            conn.connect();
            if (conn.getResponseCode() == 200)
                return conn.getInputStream();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        return null;

    }

    /**
     * 设置请求头
     */
    private void writeHeads() {
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0;CIBA)");

        Set<String> keySet = headParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String key = it.next();
            String value = headParams.get(key);
            conn.setRequestProperty(key, value);
        }
    }

    /**
     * 初始连接参数
     *
     * @throws Exception
     */
    private void initConnection() throws Exception {
        conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    private void writeStringParams() throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = textParams.get(name);
            ds.writeBytes("--" + boundary + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
            ds.writeBytes("\r\n");
            ds.write(value.getBytes("UTF-8"));
            ds.writeBytes("\r\n");
        }
    }

    private void writeFileParams() throws Exception {
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            File value = fileparams.get(name);
            ds.writeBytes("--" + boundary + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + encode(value.getName(), "UTF-8") + "\"\r\n");
            ds.writeBytes("Content-Type: " + getContentType(value) + "\r\n");
            ds.writeBytes("\r\n");
            ds.write(getBytes(value));
            ds.writeBytes("\r\n");
        }
    }

    private String getContentType(File f) throws Exception {

        return "";

    }

    private byte[] getBytes(File f) throws Exception {
        FileInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(f);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            return out.toByteArray();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    private void paramsEnd() throws Exception {
        ds.writeBytes("--" + boundary + "--" + "\r\n");
        ds.writeBytes("\r\n");
    }

    private String encode(String value, String encoding) throws Exception {
        return URLEncoder.encode(value, encoding);
    }

    /**
     * 下载文件 使用原始文件名,存放在指定的位置
     * 默认用三个线程下载
     *
     * @param urlStr 下载链接
     * @param path   存放目录
     * @return void
     */
    public static void downFile(final String urlStr, final String path, final DownLoadListener downLoadListener) {


        if (!StringUtils.isBlank(urlStr)) {
            HttpUtils http = new HttpUtils();
            http.configRequestThreadPoolSize(3);
            RequestCallBack<File> requestCallBack = new RequestCallBack<File>() {

                @Override
                public void onStart() {

                    downLoadListener.onStart();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {

                    downLoadListener.onLoading(total, current, isUploading);
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {

                    downLoadListener.onSuccess(responseInfo);
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    downLoadListener.onFailure(error, msg);
                }
            };

            http.download(urlStr, path, true, true, requestCallBack);
        }

    }

    /**
     * 将一个InputStream里面的数据写入到传入的路进
     */
    public static File write2FileFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        creatDir(path);
        try {
            if (input != null) {
                file = creatFile(path + fileName);
                output = new FileOutputStream(path + fileName);
                byte buffer[] = new byte[4 * 1024];
                int i = 0;
                while ((i = input.read(buffer)) > 0) {
                    output.write(buffer, 0, i);
                }
                output.flush();
            }
        } catch (Exception e) {
            Log.i("file", "保存错误");
            e.printStackTrace();
            return null;
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 保存下载的图片
     **/
    public static boolean saveDownFile(String path, String fileName, InputStream input) throws Exception {

        OutputStream output = null;
        try {
            if (input == null)
                return false;
            creatDir(path);
            File file = creatFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int i = 0;
            while ((i = input.read(buffer)) > 0) {
                output.write(buffer, 0, i);
            }
            output.flush();
        } catch (Exception e) {
            Log.w(Constants.TAG, "保存文件错误");
            delFile(path + fileName);
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (output != null)
                    output.close();

                if (input != null) {

                    input.close();
                }
            } catch (Exception e) {
                Log.w(Constants.TAG, "保存文件关闭流错误");
                delFile(path + fileName);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 创建目录
     *
     * @param dirName
     */
    public static File creatDir(String dirName) {
        File dir = new File(dirName);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;

    }

    /**
     * 创建文件
     *
     * @throws IOException
     */
    public static File creatFile(String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 判断文件是否存在，存在返回TRUE
     *
     * @param file
     * @return
     */
    public static boolean isFileExist(String file) {
        File temp = new File(file);
        boolean result = false;
        try {
            result = temp.exists();
        } catch (Exception e1) {
            Log.w(TAG, "文件不存在");
        }
        return result;

    }

    /**
     * 删除目录及其下面所有文件
     *
     * @param filepath
     */
    public static void delFile(String filepath) {
        try {
            if (filepath == null)
                return;
            File f = new File(filepath);// 定义文件路径
            if (f.exists()) {// 判断是文件还是目录
                if (f.isDirectory()) {
                    if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                        f.delete();
                    } else {// 若有则把文件放进数组，并判断是否有下级目录
                        File delFile[] = f.listFiles();
                        int i = f.listFiles().length;
                        for (int j = 0; j < i; j++) {
                            if (delFile[j].isDirectory()) {
                                delFile(delFile[j].getAbsolutePath());// 递归调用delFile方法并取得子目录路径
                            }
                            delFile[j].delete();// 删除文件
                        }
                    }
                } else {
                    f.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件
     *
     * @param resPath
     * @param savePath
     * @param fileName
     */
    public static void copyFile(String resPath, String savePath, String fileName) {

        InputStream in = null;
        FileOutputStream raf = null;

        creatDir(savePath);

        try {
            File saveFile = creatFile(savePath + fileName);
            byte[] tempbytes = new byte[BUFFSIZE];

            int byteread = 0;

            in = new FileInputStream(resPath);
            raf = new FileOutputStream(saveFile);
            while ((byteread = in.read(tempbytes)) != -1) {
                raf.write(tempbytes, 0, byteread);
                raf.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                    raf = null;
                }
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static String readFileContent(File file) {

        FileInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }

            return new String(out.toByteArray());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static boolean isHavingFile(String filePath) {

        File temp = new File(filePath);
        if (temp.exists()) {

            Log.d(Constants.TAG, "文件存在");
            return true;
        } else {

            Log.d(Constants.TAG, "文件不存在");
            return false;
        }
    }
}
