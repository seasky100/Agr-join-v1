package cn.fundview.app.activity.my;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.fundview.R;
import cn.fundview.app.activity.ABaseActivity;
import cn.fundview.app.tool.DeviceConfig;
import cn.fundview.app.view.OptionMenuListener;
import cn.fundview.app.view.SearchTitleBar;
import cn.fundview.app.view.TitleBarListener;
import cn.fundview.app.view.my.PhotoPreviewWebView;

public class TakeHeadIconActivity extends ABaseActivity implements TitleBarListener, OptionMenuListener {

    /**
     * 从本地相册中选择图片
     **/
    private static final int FLAG_TAKE_LOCAL = 6;

    /**
     * 从照相机中拍照
     **/
    private static final int FLAG_TAKE_PIC = 8;

    /**
     * 调用系统修剪图片后返回Code
     **/
    private static final int FLAG_SAVE_PIC = 10;

    // 临时保存的文件名
    private static String localTempImageFileName = "temp.jpg";

    // 临时保存文件的路径
    private String localTempImageFileDir;

    private SearchTitleBar titleBarView;

    private PhotoPreviewWebView webView;

    // 文件临时保存的路径
    private String tempSavePath;
    private boolean onReslut = false;

    // 来源 1为相机 2为本地相册
    private int source = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_take_head_icon);

        this.setCommonTitleBar("我的头像", R.id.photoPreviewWebView, null, false);
        init();
    }

    private void init() {

        source = getIntent().getIntExtra("source", 0); // 1 相机  2 相册
        String fileSavePath = getIntent().getStringExtra("fileSavePath");
        String fileName = getIntent().getStringExtra("fileName");

        System.out.println("个人头像的保存路径 : " + fileSavePath);

        String dir = fileSavePath.substring(0, fileSavePath.lastIndexOf("/") + 1);

        System.out.println(dir);

        File fileSaveDir = new File(dir);

        if (!fileSaveDir.exists()) {

            fileSaveDir.mkdirs();
        }

        String drivePath = DeviceConfig.getSysPath(TakeHeadIconActivity.this);
        localTempImageFileDir = drivePath + "/fundView/images/temp/";

        File temp = new File(localTempImageFileDir);
        if (!temp.exists()) {

            temp.mkdirs();
        }

        webView = (PhotoPreviewWebView) this.findViewById(R.id.photoPreviewWebView);
        webView.setPath(fileSavePath, fileName);

        titleBarView = (SearchTitleBar) this.findViewById(R.id.titleBarView);
        String operate = "";
        if (source == 1)
            operate = "重拍";
        else
            operate = "重选";

        titleBarView.setCommonTitlebarRightBtn("我的头像", operate, false);
        titleBarView.registerListener(this);

        execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.w("Findview", "requestCode:" + requestCode + " resultCode:" + resultCode);

        // 不是OK的就关掉
        if (resultCode != RESULT_OK) {
            TakeHeadIconActivity.this.finish();
        } else {

            switch (requestCode) {
                case FLAG_TAKE_LOCAL:
                    Uri fielUri = data.getData();

                    if (null != fielUri) {
                        doCropPhoto(fielUri);
                    }
                    break;
                case FLAG_TAKE_PIC:

                    doCropPhoto(Uri.fromFile(new File(localTempImageFileDir + "/" + localTempImageFileName)));
                    break;

                case FLAG_SAVE_PIC:
                    onReslut = true;
                    tempSavePath = localTempImageFileDir + "/" + localTempImageFileName;
                    // 得到修剪后的图片的bitmap
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    File f = new File(tempSavePath);
                    saveToLocal(f, bitmap);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("onResume");
        // 如果是有返回值的
        if (onReslut) {
            onReslut = false;
            showPreview(tempSavePath);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void execute() {

        switch (source) {
            case 1:
                getPicFromCapture();
                break;
            case 2:
                getPicFromContent();
                break;
        }
    }

    /**
     * 拍照
     */
    private void getPicFromCapture() {

        File f = new File(localTempImageFileDir, localTempImageFileName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, FLAG_TAKE_PIC);
    }

    /**
     * 相册
     */
    private void getPicFromContent() {

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, FLAG_TAKE_LOCAL);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, FLAG_TAKE_LOCAL);
        }

    }

    // 编辑拍出来的照片
    private void doCropPhoto(Uri uri) {

        Intent intent;

        try {
            intent = getCropImageIntent(uri);
            startActivityForResult(intent, FLAG_SAVE_PIC);
        } catch (Exception e) {
            Toast.makeText(this, "剪裁失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序 剪裁后的图片跳转到新的界面
     */
    private Intent getCropImageIntent(Uri photoUri) {

        Intent intent = new Intent("com.android.camera.action.CROP");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            String url = getPath(this, photoUri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(photoUri, "image/*");
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高(这里是头像的大小)
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 240);
        intent.putExtra("return-data", true);
        return intent;

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

    /**
     * 显示预览图片
     **/
    private void showPreview(String path) {

        // 打开页面
        titleBarView.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        webView.showPic(path);
    }

    @Override
    public void onClick(int menuId) {
        // TODO Auto-generated method stub
        if (menuId == R.id.quit) {

            //在我的页面
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("backPage", "my");// 表示在登录页面返回的话直接返回到我的页面
            startActivity(intent);
            this.finish();
        }
    }


    @Override
    public void onClickLeftBtn() {
        this.finish();
    }

    @Override
    public void onClickMiddle() {

    }

    @Override
    public void onClickRight() {
        execute();
    }


    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
