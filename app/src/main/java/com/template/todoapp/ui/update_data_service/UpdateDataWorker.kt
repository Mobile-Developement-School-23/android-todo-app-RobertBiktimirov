package com.template.todoapp.ui.update_data_service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.template.api.services.TodoService
import com.template.common.utli.runCatchingNonCancellation
import com.template.database.dao.TodoDao
import com.template.todoapp.data.UpdateDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class UpdateDataWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val updateDataRepository: UpdateDataRepository
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {

        runCatchingNonCancellation {
            updateDataRepository.loadDataInDb()
            return Result.success()
        }.getOrElse {
            return Result.retry()
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(context: Context, workerParameters: WorkerParameters): UpdateDataWorker
    }
}

class UpdateDataWorkerFactory @Inject constructor(
    private val updateDataWorker: UpdateDataWorker.Factory
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
            else -> null
        }
    }
}