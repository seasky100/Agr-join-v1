package cn.fundview.app;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import cn.fundview.R;
import cn.fundview.app.activity.expert.DetailActivity;
import cn.fundview.app.activity.qrcode.CompanyDetailActivity;
import cn.fundview.app.activity.qrcode.ExpertDetailActivity;
import cn.fundview.app.activity.qrcode.KyorgDetailActivity;
import cn.fundview.app.camera.CameraManager;
import cn.fundview.app.decoding.CaptureActivityHandler;
import cn.fundview.app.decoding.InactivityTimer;
import cn.fundview.app.domain.model.UserInfor;
import cn.fundview.app.domain.webservice.util.Constants;
import cn.fundview.app.tool.ToastUtils;
import cn.fundview.app.view.ViewfinderView;

@SuppressLint("NewApi")
public class CaptureActivity extends Activity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private SurfaceView surfaceView;
    private OrientationEventListener mOrEventListener; // 设备方向监听器
    private boolean mCurrentOrientation; // 当前设备方向 横屏false,竖屏true

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        // 初始化 CameraManager
        CameraManager.init(this);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        startOrientationChangeListener(); // 启动设备方向监听器

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
            decodeFormats = null;
            characterSet = null;

            playBeep = true;
            AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                playBeep = false;
            }
            initBeepSound();
            vibrate = true;
        } catch (Exception e) {

            e.printStackTrace();
            inactivityTimer.shutdown();
            this.finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.getInstance().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.getInstance().openDriver(surfaceHolder);
        } catch (Exception ioe) {

            ToastUtils.show(this, "打开相机失败,请授予权限后再重新打开");
            finish();

        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        System.out.println("WIDTH = " + width + " height = " + height);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            if (!hasSurface) {
                hasSurface = true;
                initCamera(holder);
            }
        } catch (Exception e) {

            e.printStackTrace();
            inactivityTimer.shutdown();

            CaptureActivity.this.finish();
        }


    }

    public boolean isMCurrentOrientation() {
        return mCurrentOrientation;
    }

    public void setMCurrentOrientation(boolean mCurrentOrientation) {
        this.mCurrentOrientation = mCurrentOrientation;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // super.onDestroy();
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(final Result obj, final Bitmap barcode) {
        inactivityTimer.onActivity();
        Matrix matrix = new Matrix();
        playBeepSoundAndVibrate();
        System.out.println(obj);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if (barcode == null) {
            dialog.setIcon(null);
        } else {

            matrix.postScale(0.5f, 0.5f); // 长和宽放大缩小的比例
            Bitmap resizeBmp = Bitmap.createBitmap(barcode, 0, 0, barcode.getWidth(), barcode.getHeight(), matrix, true);
            Drawable drawable = new BitmapDrawable(getResources(), resizeBmp);
            dialog.setIcon(drawable);
        }

//        if (handler != null)     //实现连续扫描
//            handler.restartPreviewAndDecode();

        Uri contentUrl = Uri.parse(obj.getText());

        //该二维码是平台生成的二维码
        if (Constants.M_DOMAIN.equals(contentUrl.getHost()) && contentUrl.getBooleanQueryParameter("qrType", false) && contentUrl.getBooleanQueryParameter("accountId", false)) {

            //跳转到详细页
            int qrType = Integer.parseInt(contentUrl.getQueryParameter("qrType"));
            String accountId = contentUrl.getQueryParameter("accountId");
            switch (qrType) {

                case UserInfor.COMPANY_TYPE:

                    Intent compIntent = new Intent(CaptureActivity.this, CompanyDetailActivity.class);
                    compIntent.putExtra("compId", Integer.parseInt(accountId));
                    startActivity(compIntent);
                    break;
                case UserInfor.EXPERT_TYPE:

                    Intent expertIntent = new Intent(CaptureActivity.this, ExpertDetailActivity.class);
                    expertIntent.putExtra("expertId", Integer.parseInt(accountId));
                    startActivity(expertIntent);
                    break;
                case UserInfor.KYORG_TYPE:

                    Intent orgIntent = new Intent(CaptureActivity.this, KyorgDetailActivity.class);
                    orgIntent.putExtra("orgId", Integer.parseInt(accountId));
                    startActivity(orgIntent);
                    break;

            }

            this.finish();

        } else {

            //跳转
            Intent intent = null;
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(contentUrl);

            startActivity(intent);

            this.finish();
        }

//        contentUrl.getPath();
//        contentUrl.getQueryParameter("qrid");
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private final void startOrientationChangeListener() {
        mOrEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315)
                        || ((rotation >= 135) && (rotation <= 225))) {// portrait
                    mCurrentOrientation = true;
//                    System.out.println("竖屏");
                } else if (((rotation > 45) && (rotation < 135))
                        || ((rotation > 225) && (rotation < 315))) {// landscape
                    mCurrentOrientation = false;
//                    System.out.println("横屏");
                }
            }
        };
        mOrEventListener.enable();
    }


}