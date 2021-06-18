package com.zwl.cit.download

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class DownloadService : Service(), CoroutineScope by MainScope() {
    private var count =0
    private var c =0
    /**
     * 声明下载通道
     */
    private val downLoadChannel = Channel<String>()

    /**
     * 添加下载任务
     */
    private suspend fun addDownloadTask(down: String) {
        count++
        Log.d(TAG, "增加:$down，当前任务数:$count")
        downLoadChannel.send(down)
    }
    @ExperimentalCoroutinesApi
    suspend fun processingDownloadTasks() {
        while (!downLoadChannel.isEmpty) {
            val down = downLoadChannel.receive()
            Log.d(TAG, "正在下载：$down")
            // 模拟下载
            delay(1000)
            count--
        }
        waitingToStop()
    }

    @ExperimentalCoroutinesApi
    suspend fun waitingToStop() {
        Log.d(TAG, "等待...${downLoadChannel.isEmpty}")
        delay(11000)
        Log.d(TAG, "每11s检查是否有下载任务,当前任务：${downLoadChannel.isEmpty}")
        if (downLoadChannel.isEmpty) {
            Log.d(TAG, "没有下载任务，最后5s到底有没有,当前任务：${downLoadChannel.isEmpty}")
            delay(5000)
            Log.d(TAG, "延迟，当前任务：${downLoadChannel.isEmpty}")
            if (downLoadChannel.isEmpty) {
                Log.d(TAG, "真的没有下载任务，我要关闭了")
                downLoadChannel.cancel()
                // 停止
                stopSelf()
                return
            }
        }
        processingDownloadTasks()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

//    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "启动服务")

        launch(Dispatchers.IO) {
            processingDownloadTasks()
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //每次调用startService()都会调用一次
        launch {
            c++
            Log.d(TAG, "准备增加")
            addDownloadTask("下载任务$c")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val TAG = "DownloadService"
    }
}