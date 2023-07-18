package com.template.todoapp.data.worker.load_data_from_bd

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.template.common.utli.runCatchingNonCancellation
import com.template.todoapp.data.MainRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class LoadDataFromBdWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val updateDataRepository: MainRepositoryImpl
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        return runCatchingNonCancellation {
            Log.d("ConnectionObserver.StatusTest", "LoadDataFromBdWorker do work")
            updateDataRepository.loadNewDataFromDb()
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(context: Context, workerParameters: WorkerParameters): LoadDataFromBdWorker
    }
}

