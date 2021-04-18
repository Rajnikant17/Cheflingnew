package com.example.chefling

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.chefling.repository.BusinessLogic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WorkerClass
@WorkerInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val businessLogic: BusinessLogic
) : Worker(appContext, workerParams) {
    override  fun doWork(): Result {

        return try {
            GlobalScope.launch {
                businessLogic.calledApiFromWorkerClass()
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}