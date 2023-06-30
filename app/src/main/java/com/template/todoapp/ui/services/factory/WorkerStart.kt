package com.template.todoapp.ui.services.factory

import android.content.Context
import androidx.work.*
import com.template.todoapp.ui.services.load_data_from_bd.LoadDataFromBdWorker
import com.template.todoapp.ui.services.update_data.UpdateDataWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerStart @Inject constructor(
    updateDataWorkerFactory: CreateWorkerFactory,
    private val context: Context
) {

    private val wifiConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    private val allNetworkConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val myUploadWork = PeriodicWorkRequestBuilder<UpdateDataWorker>(
        8, TimeUnit.HOURS,
        7, TimeUnit.HOURS
    )
        .setConstraints(wifiConstraints)
        .build()

    private val onEachUpdateWork = OneTimeWorkRequestBuilder<UpdateDataWorker>()
        .setConstraints(allNetworkConstraints)
        .build()

    private val onEachLoadNewDataWork = OneTimeWorkRequestBuilder<LoadDataFromBdWorker>()
        .setConstraints(allNetworkConstraints)
        .setInitialDelay(3L, TimeUnit.MINUTES)
        .build()


    private val workManagerConfig = Configuration.Builder()
        .setWorkerFactory(updateDataWorkerFactory)
        .build()


    init {
        WorkManager.initialize(context, workManagerConfig)
    }


    fun startUpdateDataWorker() {
        WorkManager.getInstance(context).enqueue(listOf(onEachUpdateWork, myUploadWork))
    }

    fun startLoadNewDataFromDb(){
        WorkManager.getInstance(context).enqueue(onEachLoadNewDataWork)
    }
}