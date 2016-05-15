package cn.fundview.app.tool;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * 振动器
 *
 * @author ouda
 */
public class Shaker implements SensorEventListener {

    private static Shaker instance = new Shaker();

    public static Shaker getInstance() {
        return instance;
    }

    public void enable() {
        if (sensorManager != null && sensor != null)
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    public void unenable() {
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 3000;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 70;
    // 传感器管理器
    private SensorManager sensorManager = null;
    // 传感器
    private Sensor sensor = null;
    // 重力感应监听器
    private ShakerListener shakerListener;
    private Context context;

    // 手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;

    // 上次检测时间
    private long lastUpdateTime;

    // 开始
    public void start(Context context, ShakerListener listener) {

        this.context = context;
        this.shakerListener = listener;

        // 获得传感器管理器
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            // 获得重力传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // 注册
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void restart() {

        start(this.context, this.shakerListener);
    }

    // 停止检测
    public void stop() {

        if (null != sensorManager)
            sensorManager.unregisterListener(this);

        sensor = null;
        sensorManager = null;
    }

    // 重力感应器感应获得变化数据
    @Override
    public void onSensorChanged(SensorEvent event) {

        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;

        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;

        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
                * deltaZ)
                / timeInterval * 10000;
        // 达到速度阀值，发出提示
        if (speed >= SPEED_SHRESHOLD) {

            Log.w("fundview", "SensorChanged");

            shakerListener.onShake();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 振动器监听者
     *
     * @author ouda
     */
    public interface ShakerListener {

        void onShake();
    }
}