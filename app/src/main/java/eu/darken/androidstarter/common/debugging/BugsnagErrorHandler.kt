package eu.darken.androidstarter.common.debugging

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import com.bugsnag.android.Event
import com.bugsnag.android.OnErrorCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.androidstarter.BuildConfig
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BugsnagErrorHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bugsnagTree: BugsnagTree,
) : OnErrorCallback {

    override fun onError(event: Event): Boolean {
        bugsnagTree.injectLog(event)

        TAB_APP.also { tab ->
            event.addMetadata(tab, "gitSha", BuildConfig.GITSHA)
            event.addMetadata(tab, "buildTime", BuildConfig.BUILDTIME)

            context.tryFormattedSignature()?.let { event.addMetadata(tab, "signatures", it) }
        }

        return !BuildConfig.DEBUG
    }

    companion object {
        private const val TAB_APP = "app"

        @Suppress("DEPRECATION")
        @SuppressLint("PackageManagerGetSignatures")
        fun Context.tryFormattedSignature(): String? = try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures?.let { sigs ->
                val sb = StringBuilder("[")
                for (i in sigs.indices) {
                    sb.append(sigs[i].hashCode())
                    if (i + 1 != sigs.size) sb.append(", ")
                }
                sb.append("]")
                sb.toString()
            }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

}