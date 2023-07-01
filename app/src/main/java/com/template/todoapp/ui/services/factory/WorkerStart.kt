package com.template.todoapp.ui.services.factory

import android.content.Context
import androidx.work.*
import com.template.todoapp.ui.services.load_data_from_bd.LoadDataFromBdWorker
import com.template.todoapp.ui.services.update_data.UpdateDataWorker
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

class WorkerStart @Inject constructor(
    updateDataWorkerFactory: CreateWorkerFactory,
    private val context: Context
) {

    private var workManager: WorkManager by Delegates.notNull()

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
        .setId(onEachLoadNewDataUUID)
        .build()


    private val workManagerConfig = Configuration.Builder()
        .setWorkerFactory(updateDataWorkerFactory)
        .build()


    init {
        WorkManager.initialize(context, workManagerConfig)
        workManager = WorkManager.getInstance(context)
    }


    fun startUpdateDataWorker() {
        workManager
            .beginWith(onEachLoadNewDataWork)
            .then(onEachUpdateWork)
            .enqueue()
    }

    fun startLoadNewDataFromDb() {

        if (workManager.getWorkInfoById(onEachLoadNewDataUUID).isCancelled) {
            workManager.enqueue(onEachLoadNewDataWork)
        }
    }

    fun startPeriodicUpdateData(){
        workManager.enqueue(myUploadWork)
    }


    companion object {
        private var onEachLoadNewDataUUID: UUID = UUID.randomUUID()
    }
}