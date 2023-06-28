package com.template.todoapp.ui.update_data_service

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateDataWorkerStart @Inject constructor(
    updateDataWorkerFactory: UpdateDataWorkerFactory,
    private val context: Context
){

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    private val myUploadWork = PeriodicWorkRequestBuilder<UpdateDataWorker>(
        8, TimeUnit.HOURS,
        7, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()


    private val workManagerConfig = Configuration.Builder()
        .setWorkerFactory(updateDataWorkerFactory)
        .build()


    init {
        WorkManager.initialize(context, workManagerConfig)
    }


    fun startUpdateDataWorker(){
        WorkManager.getInstance(context).enqueue(myUploadWork)
    }

}