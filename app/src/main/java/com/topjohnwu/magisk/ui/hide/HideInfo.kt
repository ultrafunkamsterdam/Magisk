package com.topjohnwu.magisk.ui.hide

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.content.pm.ServiceInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.os.ProcessCompat
import com.topjohnwu.magisk.core.utils.currentLocale
import com.topjohnwu.magisk.ktx.getLabel
import java.util.*

class CmdlineHiddenItem(line: String) {
    val packageName: String
    val process: String

    init {
        val split = line.split(Regex("\\|"), 2)
        packageName = split[0]
        process = split.getOrElse(1) { packageName }
    }
}

const val ISOLATED_MAGIC = "isolated"

@SuppressLint("InlinedApi")
<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/AppProcessInfo.kt
class AppProcessInfo(
    private val info: ApplicationInfo,
    pm: PackageManager,
    denyList: List<CmdlineListItem>
) : Comparable<AppProcessInfo> {

    private val denyList = denyList.filter {
        it.packageName == info.packageName || it.packageName == ISOLATED_MAGIC
    }

    val label = info.getLabel(pm)
    val iconImage: Drawable = runCatching { info.loadIcon(pm) }.getOrDefault(pm.defaultActivityIcon)
    val packageName: String get() = info.packageName
    val processes = fetchProcesses(pm)
=======
class HideAppInfo(info: ApplicationInfo, pm: PackageManager, hideList: List<CmdlineHiddenItem>)
    : ApplicationInfo(info), Comparable<HideAppInfo> {

    val label = info.getLabel(pm)
    val iconImage: Drawable = info.loadIcon(pm)
    val processes = fetchProcesses(pm, hideList)
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideInfo.kt

    override fun compareTo(other: HideAppInfo) = comparator.compare(this, other)

<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/AppProcessInfo.kt
    fun isSystemApp() = info.flags and ApplicationInfo.FLAG_SYSTEM != 0

    fun isApp() = ProcessCompat.isApplicationUid(info.uid)
=======
    private fun fetchProcesses(
        pm: PackageManager,
        hideList: List<CmdlineHiddenItem>
    ): List<HideProcessInfo> {
        // Fetch full PackageInfo
        val baseFlag = MATCH_DISABLED_COMPONENTS or MATCH_UNINSTALLED_PACKAGES
        val packageInfo = try {
            val request = GET_ACTIVITIES or GET_SERVICES or GET_RECEIVERS or GET_PROVIDERS
            pm.getPackageInfo(packageName, baseFlag or request)
        } catch (e: NameNotFoundException) {
            // EdXposed hooked, issue#3276
            return emptyList()
        } catch (e: Exception) {
            // Exceed binder data transfer limit, fetch each component type separately
            pm.getPackageInfo(packageName, baseFlag).apply {
                runCatching { activities = pm.getPackageInfo(packageName, baseFlag or GET_ACTIVITIES).activities }
                runCatching { services = pm.getPackageInfo(packageName, baseFlag or GET_SERVICES).services }
                runCatching { receivers = pm.getPackageInfo(packageName, baseFlag or GET_RECEIVERS).receivers }
                runCatching { providers = pm.getPackageInfo(packageName, baseFlag or GET_PROVIDERS).providers }
            }
        }

        val hidden = hideList.filter { it.packageName == packageName || it.packageName == ISOLATED_MAGIC }
        fun createProcess(name: String, pkg: String = packageName): HideProcessInfo {
            return HideProcessInfo(name, pkg, hidden.any { it.process == name && it.packageName == pkg })
        }
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideInfo.kt

    private fun createProcess(name: String, pkg: String = info.packageName) =
        ProcessInfo(name, pkg, denyList.any { it.process == name && it.packageName == pkg })

    private fun ComponentInfo.getProcName(): String = processName
        ?: applicationInfo.processName
        ?: applicationInfo.packageName

    private val ServiceInfo.isIsolated get() = (flags and ServiceInfo.FLAG_ISOLATED_PROCESS) != 0
    private val ServiceInfo.useAppZygote get() = (flags and ServiceInfo.FLAG_USE_APP_ZYGOTE) != 0

    private fun Array<out ComponentInfo>?.toProcessList() =
        orEmpty().map { createProcess(it.getProcName()) }

    private fun Array<ServiceInfo>?.toProcessList() = orEmpty().map {
        if (it.isIsolated) {
            if (it.useAppZygote) {
                val proc = info.processName ?: info.packageName
                createProcess("${proc}_zygote")
            } else {
                val proc = if (SDK_INT >= Build.VERSION_CODES.Q)
                    "${it.getProcName()}:${it.name}" else it.getProcName()
                createProcess(proc, ISOLATED_MAGIC)
            }
        } else {
            createProcess(it.getProcName())
        }
    }

    private fun fetchProcesses(pm: PackageManager): Collection<ProcessInfo> {
        val flag = MATCH_DISABLED_COMPONENTS or MATCH_UNINSTALLED_PACKAGES or
            GET_ACTIVITIES or GET_SERVICES or GET_RECEIVERS or GET_PROVIDERS
        val packageInfo = try {
            pm.getPackageInfo(info.packageName, flag)
        } catch (e: Exception) {
            // Exceed binder data transfer limit, parse the package locally
            pm.getPackageArchiveInfo(info.sourceDir, flag) ?: return emptyList()
        }

        val processSet = TreeSet<ProcessInfo>(compareBy({ it.name }, { it.isIsolated }))
        processSet += packageInfo.activities.toProcessList()
        processSet += packageInfo.services.toProcessList()
        processSet += packageInfo.receivers.toProcessList()
        processSet += packageInfo.providers.toProcessList()
        return processSet
    }

    companion object {
<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/AppProcessInfo.kt
        private val comparator = compareBy<AppProcessInfo>(
            { it.label.lowercase(currentLocale) },
            { it.info.packageName }
=======
        private val comparator = compareBy<HideAppInfo>(
            { it.label.toLowerCase(currentLocale) },
            { it.packageName }
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideInfo.kt
        )
    }
}

data class HideProcessInfo(
    val name: String,
    val packageName: String,
    var isHidden: Boolean
) {
    val isIsolated = packageName == ISOLATED_MAGIC
    val isAppZygote = name.endsWith("_zygote")
}
