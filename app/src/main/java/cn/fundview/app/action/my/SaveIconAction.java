package cn.fundview.app.action.my;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.fundview.app.action.ABaseAction;
import cn.fundview.app.domain.dao.DaoFactory;
import cn.fundview.app.domain.webservice.RService;
import cn.fundview.app.tool.Constants;
import cn.fundview.app.tool.file.PreferencesUtils;
import cn.fundview.app.view.ABaseWebView;
import cn.fundview.app.view.my.PhotoPreviewWebView;

public class SaveIconAction extends ABaseAction {

    /**
     * 参数
     **/
    private String fileSaveDir;
    private String fileName;
    private String filePath;
    private String width;
    private String height;
    private int uid;

    public SaveIconAction(Context context, ABaseWebView webView) {
        super(context, webView);
        uid = PreferencesUtils.getInt(context, Constants.ACCOUNT_ID);
    }

    public void execute(String fileSaveDir, String fileName, String filePath,
                        String width, String height) {

        this.fileSaveDir = fileSaveDir;
        this.fileName = fileName;
        this.filePath = filePath;
        this.width = width;
        this.height = height;

        this.showWaitDialog();
        handle(false);
    }

    /**
     * 异步处理
     **/
    @Override
    protected void doHandle() {

        Bitmap bitmap = bitmapCompressSize(filePath, Integer.valueOf(width),
                Integer.valueOf(height));
        int imgAngle = readPictureDegree(filePath);
        Matrix m = new Matrix();
        m.setRotate(imgAngle); // 旋转imgAngle度
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), m, true);
        try {
            File f = new File(fileSaveDir, fileName);
            // 保存图片
            saveToLocal(f, bitmap);
            if (bitmap != null && bitmap.isRecycled()) {
                bitmap.recycle();
            }
            // 保存到数据库中
            DaoFactory.getInstance(context).getUserInforDao().updateUserInforHeadIcon(fileSaveDir + fileName, uid);

            // 上传到网络
            RService.updateProfileIcon(uid, fileSaveDir + fileName, webView);

        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理结果
     **/
    @Override
    protected void doHandleResult() {

//		this.closeWaitDialog();


    }

    /**
     * 按尺寸压缩图片
     **/
    private Bitmap bitmapCompressSize(String filePath, int width, int height) {

        // 文件过大时，说就是硬件加速的时候，对图片的大小有限制。
        // 一个解决的方法是禁止硬件加速，简单粗暴：有关
        // <application android:hardwareAccelerated="false" ...>
        BitmapFactory.Options op = new BitmapFactory.Options();
        // 如果该属性设置为true，解析器将不会返回Bitmap，而是null。但是options.outWidth
        // ，options.outHeight
        // 的字段仍然被设置，方便调用者查询Bitmap的宽高。不需要为Bitmap的实际像素划分内存。
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, op);

        // 拿到Bitmap的宽高后，设置为自己想要的值后，重新构造自己的Bitmap：
        // options.outWidth = 400;
        // options.outHeight = 400;
        // 获取尺寸信息
        // 获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / width);
        int hRatio = (int) Math.ceil(op.outHeight / height);
        // 如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filePath, op);
        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    // 按比例保存文件图片
    private boolean saveToLocal(File saveFile, Bitmap bm) {
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bm.compress(CompressFormat.JPEG, 75, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
