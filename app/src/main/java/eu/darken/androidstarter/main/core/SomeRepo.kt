package eu.darken.androidstarter.main.core

import eu.darken.androidstarter.common.coroutine.AppScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SomeRepo @Inject constructor(
    @AppScope private val appCoroutineScope: CoroutineScope,
) {

    val countsWhileSubscribed: Flow<Long> = flow {
        var sub = 0L
        while (currentCoroutineContext().isActive) {
            val toEmit = sub++
            Timber.v("Emitting (sub) $toEmit")
            emit(toEmit)
            delay(1_000)
        }
    }

    val countsAlways: Flow<Long> = flow {
        var counter = 0L
        while (currentCoroutineContext().isActive) {
            val toEmit = counter++
            Timber.v("Emitting (perm) $toEmit")
            emit(toEmit)
            delay(1_000)
        }
    }.shareIn(
        scope = appCoroutineScope,
        started = SharingStarted.Lazily,
        replay = 1
    )

    val emojis: Flow<String> = flow {
        val emoji = EMOJIS[(Math.random() * EMOJIS.size).toInt()]
        Timber.v("Emitting $emoji")
        emit(emoji)
    }

    companion object {
        internal val EMOJIS = listOf("\uD83D\uDE00", "\uD83D\uDE02", "\uD83E\uDD17", "\uD83D\uDE32")
    }
}
