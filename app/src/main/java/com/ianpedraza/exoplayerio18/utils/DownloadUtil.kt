package com.ianpedraza.exoplayerio18.utils

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File
import java.lang.Exception

object DownloadUtil {

    @Volatile
    private var cache: Cache? = null

    @Volatile
    private var downloadManager: DownloadManager? = null

    @Synchronized
    fun getCache(context: Context): Cache {
        if (cache == null) {
            val cacheDirectory = File(context.getExternalFilesDir(null), "downloads")
            val database = StandaloneDatabaseProvider(context)
            cache = SimpleCache(cacheDirectory, NoOpCacheEvictor(), database)
        }

        return cache!!
    }

    @Synchronized
    fun getDownloadManager(context: Context): DownloadManager {
        if (downloadManager == null) {
            // val actionFile = File(context.externalCacheDir, "actions")
            val dataSourceFactory = DefaultHttpDataSource.Factory()
            val database = StandaloneDatabaseProvider(context)

            downloadManager = DownloadManager(
                context,
                database,
                getCache(context),
                dataSourceFactory
            ) {
            }

            downloadManager!!.addListener(downloadListener)
        }

        return downloadManager!!
    }

    private val downloadListener = object : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            super.onDownloadChanged(downloadManager, download, finalException)
            Log.d("DownloadUtil", "downloadListener:onDownloadChanged:${download.bytesDownloaded}b")
        }
    }
}
