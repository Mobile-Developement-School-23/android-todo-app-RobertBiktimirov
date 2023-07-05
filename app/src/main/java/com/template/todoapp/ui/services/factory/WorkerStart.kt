package com.template.todoapp.ui.services.factory

import android.content.Context
import android.util.Log
import androidx.work.*
import com.template.todoapp.data.worker.load_data_from_bd.LoadDataFromBdWorker
import com.template.todoapp.data.worker.update_data.UpdateDataWorker
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

class WorkerStart @Inject constructor(
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


    init {
        workManager = WorkManager.getInstance(context)
    }


    fun startUpdateDataWorker() {
        Log.d("testWorkManager", "startUpdateDataWorker")
        workManager.enqueue(onEachUpdateWork)
    }

    fun startLoadNewDataFromDb() {

        if (workManager.getWorkInfoById(onEachLoadNewDataUUID).isDone) {
            workManager.enqueue(onEachLoadNewDataWork)
        }
    }

    fun startPeriodicUpdateData() {
        workManager.enqueue(myUploadWork)
    }


    companion object {
        private var onEachLoadNewDataUUID: UUID = UUID.randomUUID()
    }
}