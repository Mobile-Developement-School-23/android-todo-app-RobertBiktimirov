package com.template.todoapp.ui.services.load_data_from_bd

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.template.common.utli.runCatchingNonCancellation
import com.template.todoapp.data.MainRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class LoadDataFromBdWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val updateDataRepository: MainRepository
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        return runCatchingNonCancellation {
            updateDataRepository.loadNewDataFromDb()
            Log.d("connection test", "do work")
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

