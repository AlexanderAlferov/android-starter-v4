package eu.darken.androidstarter.common.viewmodel

import androidx.lifecycle.asLiveData
import eu.darken.androidstarter.common.coroutine.DefaultDispatcherProvider
import eu.darken.androidstarter.common.coroutine.DispatcherProvider
import kotlinx.coroutines.flow.Flow


abstract class SmartVM(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : VM() {

    fun <T> Flow<T>.asLiveData2() = this.asLiveData(context = dispatcherProvider.Default)

}