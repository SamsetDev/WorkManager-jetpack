package com.samset.workmanager

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.work.*
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),View.OnClickListener {


    private var TAG: String = MainActivity::class.java.simpleName

    private lateinit var workManager: WorkManager
    private lateinit var timeWorkRequest: OneTimeWorkRequest
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private  var PERIODIC_REQUEST_TAG:String="periodic_request"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workManager = WorkManager.getInstance()!!
        btnstart.setOnClickListener(this)
        btnstop.setOnClickListener(this)


    }

    private fun setupOneTimeWorker() {
        timeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)?.build()
        workManager.enqueue(timeWorkRequest)
    }

    private fun setupPeriodicWorker() {

        val works = WorkManager.getInstance().getStatusesByTag(PERIODIC_REQUEST_TAG)

        if (works.value != null && works.value?.isNotEmpty()!!) {
            return
        }

        val work = PeriodicWorkRequest.Builder(MyWorker::class.java, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MINUTES, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MINUTES)
        periodicWorkRequest = work.build()

         workManager.enqueue(periodicWorkRequest)

    }

    private fun requestPerodicWorkerSecondMethod(){
        val statusesByTag = workManager.getStatusesByTag(PERIODIC_REQUEST_TAG)
        if (statusesByTag == null || statusesByTag.value?.size == 0) {

        } else {
            for (i in 0 until statusesByTag.value?.size!!) {
                Log.e("TAG", " perodic request id " + statusesByTag.value?.get(i)?.id + " Status " + statusesByTag.value?.get(i)?.state)
            }
        }
    }


    override fun onClick(view: View?) {
        if (view == btnstart) {
            setupOneTimeWorker()
        } else if (view == btnstop) {
            setupPeriodicWorker()
            // or
           // requestPerodicWorkerSecondMethod()
        }

    }
}
