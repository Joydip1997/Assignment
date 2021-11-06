package com.example.assignment.Utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


inline fun <reified T> SharedFlow<T>.collectWhileStarted(
    owner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) {
    object : DefaultLifecycleObserver {
        private var job: Job? = null

        override fun onStart(owner: LifecycleOwner) {
            job = owner.lifecycleScope.launch {
                this@collectWhileStarted.collect {
                    collector(it)
                }
            }
        }

        override fun onStop(owner: LifecycleOwner) {
            job?.cancel()
            job = null
        }

        init {
            owner.lifecycle.addObserver(this)
        }
    }
}