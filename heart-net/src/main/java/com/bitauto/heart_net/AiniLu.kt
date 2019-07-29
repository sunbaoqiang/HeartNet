package com.bitauto.heart_net

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Printer
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.bitauto.heart_net.view.FunctionActivity

/**
 * @author : sunbq
 * e-mail : subbq@yiche.com
 * date   : 2019-07-2910:19
 * desc   : 海贼王 空岛 GOD艾尼路 心网使用者
 */

@SuppressLint("StaticFieldLeak")
object AiniLu : View.OnTouchListener {

    private lateinit var wm: WindowManager
    private lateinit var view: ImageView
    private lateinit var params: WindowManager.LayoutParams
    // 触屏监听
    var lastX: Float = 0.toFloat()
    var lastY: Float = 0.toFloat()
    var oldOffsetX: Int = 0
    var oldOffsetY: Int = 0
    var tag = 0// 悬浮球 所需成员变量
    //启动时间
    var launchTime = 0L
    var launchedTime = 0L

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.let {
            val action = it.action
            val x = it.x
            val y = it.y
            if (tag == 0) {
                oldOffsetX = params.x // 偏移量
                oldOffsetY = params.y // 偏移量
            }
            if (action == MotionEvent.ACTION_DOWN) {
                lastX = x
                lastY = y
            } else if (action == MotionEvent.ACTION_MOVE) {
                params.x += (x - lastX).toInt() / 3 // 减小偏移量,防止过度抖动
                params.y += (y - lastY).toInt() / 3 // 减小偏移量,防止过度抖动
                tag = 1
                wm.updateViewLayout(view, params)
            } else if (action == MotionEvent.ACTION_UP) {
                val newOffsetX = params.x
                val newOffsetY = params.y
                // 只要按钮一动位置不是很大,就认为是点击事件
                if (Math.abs(oldOffsetX - newOffsetX) <= 20 && Math.abs(oldOffsetY - newOffsetY) <= 20) {
                    FunctionActivity.start(v?.context)
                } else {
                    tag = 0
                }
            }
        }
        return true
    }

    fun init(context: Application) {
        launchTime = System.currentTimeMillis()
        wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        view = ImageView(context)
        view.setBackgroundResource(R.drawable.ic_heartnet_zzx)
        params = WindowManager.LayoutParams()
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        // 所有程序窗口的“基地”窗口，其他应用程序窗口都显示在它上面。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//6.0
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT
        view.setOnTouchListener(this)
        wm.addView(view, params)
        //冷启动
        context.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (launchedTime == 0L) {
                    launchedTime = System.currentTimeMillis()
                    Toast.makeText(activity, "cost=" + (launchedTime - launchTime) + "ms", Toast.LENGTH_LONG).show()
                }
            }
        })

        //监听 主线程 方法
        Looper.getMainLooper().setMessageLogging(object : Printer {
            override fun println(x: String) {
                if (x.startsWith(START)) {
                    //开始计时
                    LogMonitor.instance.startMonitor()
                }
                if (x.startsWith(END)) {
                    //结束计时，并计算出方法执行时间
                    LogMonitor.instance.removeMonitor()
                }
            }

            val START = ">>>>> Dispatching"
            val END = "<<<<< Finished"
        })

    }
}
