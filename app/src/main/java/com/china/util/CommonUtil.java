package com.china.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.androidquery.util.AQUtility;

import java.lang.reflect.Method;

/**
 * User: Gebing
 * Date: 13-7-23 上午9:39
 */
public class CommonUtil {

    /**
     * 利用Java反射机制调用close()。主要是简化了所有需要捕捉Exception的代码
     *
     * @param object 所有支持close()的对象
     */
    public static void close(Object object) {
        try {
            if (object == null) return;
            Method method = object.getClass().getMethod("close");
            if (method != null) method.invoke(object);
        } catch (Exception e) {
        }
    }

    /**
     * 当前线程sleep指定时间。主要是简化了所有需要捕捉Exception的代码
     *
     * @param time
     * @return false - 被中断； true - 正常结束
     */
    public static boolean sleep(long time) {
        return sleep(Thread.currentThread(), time);
    }

    /**
     * 指定线程sleep指定时间。主要是简化了所有需要捕捉Exception的代码
     *
     * @param thread
     * @param time
     * @return false - 被中断； true - 正常结束
     */
    public static boolean sleep(Thread thread, long time) {
        try {
            thread.sleep(time);
            return true;
        } catch (InterruptedException ie) {
            return false;
        }
    }

    /**
     * 在指定时间内等待当前线程结束。主要是简化了所有需要捕捉Exception的代码
     *
     * @param time （可选，缺省为-1）< 0,一直等待；>=0, 等待时间
     * @return false - 被中断； true - 正常结束
     */
    public static boolean join(long... time) {
        return join(Thread.currentThread(), time);
    }

    /**
     * 在指定时间内等待指定线程结束。主要是简化了所有需要捕捉Exception的代码
     *
     * @param thread
     * @param time   （可选，缺省为-1）< 0,一直等待；>=0, 等待时间
     * @return false - 被中断； true - 正常结束
     */
    public static boolean join(Thread thread, long... time) {
        try {
            if (time.length == 0 || time[0] < 0) thread.join();
            else thread.join(time[0]);
            return true;
        } catch (InterruptedException ie) {
            return false;
        }
    }

    /**
     * 在指定时间内等待当前线程被Notify。主要是简化了所有需要捕捉Exception的代码
     *
     * @param time （可选，缺省为-1）< 0,一直等待；>=0, 等待时间
     * @return false - 被中断； true - 正常结束
     */
    public static boolean wait(long... time) {
        return wait(Thread.currentThread(), time);
    }

    /**
     * 在指定时间内等待指定对象被Notify。主要是简化了所有需要捕捉Exception的代码
     *
     * @param object
     * @param time   （可选，缺省为-1）< 0,一直等待；>=0, 等待时间
     * @return false - 被中断； true - 正常结束
     */
    public static boolean wait(Object object, long... time) {
        if (object == null) return false;
        try {
            synchronized (object) {
                if (time.length == 0 || time[0] < 0) object.wait();
                else object.wait(time[0]);
            }
            return true;
        } catch (InterruptedException ie) {
            return false;
        }
    }

    /**
     * 通知并唤醒一个执行wait()命令的对象
     *
     * @param object
     */
    public static void notify(Object object) {
        if (object == null) return;
        synchronized (object) {
            object.notify();
        }
    }

    /**
     * 通知并唤醒所有执行wait()命令的对象
     *
     * @param object
     */
    public static void notifyAll(Object object) {
        if (object == null) return;
        synchronized (object) {
            object.notifyAll();
        }
    }

    public static void toast(String message){
        toast(Toast.LENGTH_SHORT, message);
    }

    public static void toast(int resId) {
        toast(Toast.LENGTH_SHORT, resId);
    }

    /**
     * 显示Toast信息
     *
     * @param duration Toast.LENGTH_SHORT/Toast.LENGTH_LONG/自定义时间
     * @param message  显示的信息
     */
    public static void toast(int duration, String message) {
        Toast.makeText(AQUtility.getContext(), message, duration).show();
    }

    /**
     * 显示Toast信息
     *
     * @param duration Toast.LENGTH_SHORT/Toast.LENGTH_LONG/自定义时间
     * @param resId    显示信息的资源Id
     */
    public static void toast(int duration, int resId) {
        Toast.makeText(AQUtility.getContext(), resId, duration).show();
    }

    /**
     * 显示Toast信息
     *
     * @param duration Toast.LENGTH_SHORT/Toast.LENGTH_LONG/自定义时间
     * @param resId    显示信息的资源Id
     * @formatArgs 资源格式化参数
     */
    public static void toast(int duration, int resId, Object... formatArgs) {
        String message = AQUtility.getContext().getString(resId, formatArgs);
        toast(duration, message);
    }

    /**
     * 显示等待对话框
     *
     * @param context
     * @param message 提示信息的内容
     */
    public static Dialog progressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.5f;            // 透明度
        lp.dimAmount = 0.5f;        // 黑暗度
        window.setAttributes(lp);
        dialog.setMessage(message);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    /**
     * 显示等待对话框
     *
     * @param context
     * @param resId   提示信息内容的资源Id
     */
    public static Dialog progressDialog(Context context, int resId, Object... formatArgs) {
        return progressDialog(context, context.getString(resId, formatArgs));
    }
}
