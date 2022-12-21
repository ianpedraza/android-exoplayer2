package com.ianpedraza.exoplayerio18.ui.audio

import android.app.Notification
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.ianpedraza.exoplayerio18.R
import com.ianpedraza.exoplayerio18.utils.DownloadUtil

class AudioDownloadService :
    DownloadService(
        DOWNLOAD_NOTIFICATION_ID,
        DOWNLOAD_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        DOWNLOAD_CHANNEL_ID,
        R.string.channel_download,
        R.string.channel_download_description
    ) {

    override fun getDownloadManager(): DownloadManager {
        return DownloadUtil.getDownloadManager(this)
    }

    override fun getScheduler(): Scheduler? {
        // Provides job scheduler in order to resume downloads if process isn't running
        return null
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        return DownloadNotificationHelper(this, DOWNLOAD_CHANNEL_ID)
            .buildProgressNotification(
                this,
                R.drawable.ic_queue_music,
                null,
                null,
                downloads,
                notMetRequirements
            )
    }

    companion object {
        private const val DOWNLOAD_NOTIFICATION_ID = 953
        private const val DOWNLOAD_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL = 1000L
        private const val DOWNLOAD_CHANNEL_ID = "DownloadChannel"
    }
}
