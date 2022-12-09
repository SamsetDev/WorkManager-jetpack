package com.samset.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.samset.workmanager.work.MyWorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener  {

    private var TAG: String = MainActivity::class.java.simpleName

    private lateinit var workManager: WorkManager
    private lateinit var timeWorkRequest: OneTimeWorkRequest
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private var PERIODIC_REQUEST_TAG: String = "periodic_request"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        workManager = WorkManager.getInstance()!!
        btnstart.setOnClickListener(this)
        btnstop.setOnClickListener(this)


    }

    private fun setupOneTimeWorker() {
        timeWorkRequest = OneTimeWorkRequest.Builder(MyWorkManager::class.java).build()
        workManager.enqueue(timeWorkRequest)
    }

    private fun setupPeriodicWorker() {

        val work = PeriodicWorkRequest.Builder(MyWorkManager::class.java, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MINUTES, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES)
        periodicWorkRequest = work.build()

        workManager.enqueue(periodicWorkRequest)

    }


    private fun isScheduleWork(tag: String): Boolean {
        val statuses = workManager.getWorkInfosByTag(tag)
        if (statuses.get().isEmpty()) return false
        var running = false

        for (state in statuses.get()) {

            running = state.state == WorkInfo.State.RUNNING || state.state == WorkInfo.State.ENQUEUED
        }
        return running

    }


    override fun onClick(view: View?) {
        if (view == btnstart) {
            setupOneTimeWorker()
        } else if (view == btnstop) {
            !isScheduleWork(PERIODIC_REQUEST_TAG)
            setupPeriodicWorker()

        }

    }
}