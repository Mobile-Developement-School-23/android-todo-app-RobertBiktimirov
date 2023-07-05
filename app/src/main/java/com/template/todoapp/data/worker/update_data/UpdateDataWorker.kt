package com.template.todoapp.data.worker.update_data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.template.common.utli.runCatchingNonCancellation
import com.template.todoapp.data.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class UpdateDataWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val updateDataRepository: MainRepository
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