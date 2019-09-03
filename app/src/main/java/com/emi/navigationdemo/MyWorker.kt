package com.emi.navigationdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker (context: Context, workerParams : WorkerParameters) : Worker(context, workerParams){

    override fun doWork(): Result{
        Log.d(MyWorker::class.java.simpleName, "Performing long running task in scheduled job")
        return Result.success()
    }

}