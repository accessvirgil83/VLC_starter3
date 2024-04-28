package com.devdroid.myapplication

import android.app.Service
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.ServerSocket
import java.net.Socket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, RTSPService::class.java))
    }
}

class RTSPService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        GlobalScope.launch {
            // Запуск сокет сервера
            val serverSocket = ServerSocket(65432)
            while (true) {
                val clientSocket: Socket = serverSocket.accept()
                val inputStream = clientSocket.getInputStream()
                val buffer = ByteArray(1024)
                val read = inputStream.read(buffer)
                val message = String(buffer, 0, read)
                if (message.trim() == "flag") {
                    launchRTSPStream()
                    stopSelf()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun launchRTSPStream() {
        val rtspUrl = "" // ваш RTSP поток
        // Запуск VLC плеера с RTSP потоком
        val VPath = "com.devdroid.videoexample2"
        val packageVLC = "org.videolan.vlc"
        val intentVLC = Intent(Intent.ACTION_VIEW, Uri.parse(VPath))
        intentVLC.setClassName(VPath, "$VPath.MainActivity")
        intentVLC.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentVLC)
    }
}
