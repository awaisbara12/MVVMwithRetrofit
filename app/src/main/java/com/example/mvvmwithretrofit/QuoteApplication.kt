package com.example.mvvmwithretrofit

import android.app.Application
import androidx.constraintlayout.widget.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mvvmwithretrofit.api.QuoteService
import com.example.mvvmwithretrofit.api.RetrofitHelper
import com.example.mvvmwithretrofit.db.QuoteDatabase
import com.example.mvvmwithretrofit.repository.QuoteRepository
import com.example.mvvmwithretrofit.worker.QuoteWorker
import java.util.concurrent.TimeUnit

class QuoteApplication: Application() {

    lateinit var quoteRepository: QuoteRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
        setupWorker()
    }

    private fun setupWorker() {
        //constraint To check network is connected
        val constraint = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        //Worker Request time to run worker (here is every 30 mins)
        val workerResult = PeriodicWorkRequest
            .Builder(QuoteWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraint).build()
        //WorkManager to run worker
        WorkManager.getInstance(this).enqueue(workerResult)


    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val database = QuoteDatabase.getDatabase(applicationContext)
        quoteRepository = QuoteRepository(quoteService, database, applicationContext)
    }
}