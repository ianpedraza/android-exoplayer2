package com.ianpedraza.exoplayerio18.ui.audio

import android.app.Notification
import android.app.Service
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class NotificationListener(private val service: Service) :
    PlayerNotificationManager.NotificationListener {

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        service.startForeground(notificationId, notification)
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        service.stopSelf()
    }
}
