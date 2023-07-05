package com.template.todoapp.ui.services.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.template.todoapp.data.worker.load_data_from_bd.LoadDataFromBdWorker
import com.template.todoapp.data.worker.update_data.UpdateDataWorker
import javax.inject.Inject

class CreateWorkerFactory @Inject constructor(
    private val updateDataWorker: UpdateDataWorker.Factory,
    private val loadDataFromBdWorker: LoadDataFromBdWorker.Factory
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UpdateDataWorker::class.java.name -> {
                updateDataWorker.create(appContext, workerParameters)
            }
            LoadDataFromBdWorker::class.java.name -> {
                loadDataFromBdWorker.create(appContext, workerParameters)
            }
            else -> null
        }
    }
}