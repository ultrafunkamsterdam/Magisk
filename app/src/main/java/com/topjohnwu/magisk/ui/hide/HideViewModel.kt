package com.topjohnwu.magisk.ui.hide

import android.annotation.SuppressLint
import android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES
import androidx.databinding.Bindable
import com.topjohnwu.magisk.BR
import com.topjohnwu.magisk.arch.AsyncLoadViewModel
import com.topjohnwu.magisk.core.di.AppContext
import com.topjohnwu.magisk.databinding.bindExtra
import com.topjohnwu.magisk.databinding.filterableListOf
import com.topjohnwu.magisk.databinding.set
import com.topjohnwu.magisk.ktx.concurrentMap
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.withContext

<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListViewModel.kt
class DenyListViewModel : AsyncLoadViewModel() {
=======
class HideViewModel : BaseViewModel(), Queryable {
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideViewModel.kt

    var isShowSystem = false
        set(value) {
            field = value
            query()
        }

    var isShowOS = false
        set(value) {
            field = value
            query()
        }

    var query = ""
        set(value) {
            field = value
            query()
        }

<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListViewModel.kt
    val items = filterableListOf<DenyListRvItem>()
    val extraBindings = bindExtra {
        it.put(BR.viewModel, this)
=======
    val items = filterableListOf<HideRvItem>()
    val itemBinding = itemBindingOf<HideRvItem> {
        it.bindExtra(BR.viewModel, this)
    }
    val itemInternalBinding = itemBindingOf<HideProcessRvItem> {
        it.bindExtra(BR.viewModel, this)
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideViewModel.kt
    }

    @get:Bindable
    var loading = true
        private set(value) = set(value, field, { field = it }, BR.loading)

    @SuppressLint("InlinedApi")
    override suspend fun doLoadWork() {
        loading = true
        val (apps, diff) = withContext(Dispatchers.Default) {
            val pm = AppContext.packageManager
<<<<<<< HEAD:app/src/main/java/com/topjohnwu/magisk/ui/deny/DenyListViewModel.kt
            val denyList = Shell.cmd("magisk --denylist ls").exec().out
                .map { CmdlineListItem(it) }
            val apps = pm.getInstalledApplications(MATCH_UNINSTALLED_PACKAGES).run {
                asFlow()
                    .filter { AppContext.packageName != it.packageName }
                    .concurrentMap { AppProcessInfo(it, pm, denyList) }
                    .filter { it.processes.isNotEmpty() }
                    .concurrentMap { DenyListRvItem(it) }
                    .toCollection(ArrayList(size))
            }
            apps.sort()
=======
            val hideList = Shell.su("magiskhide ls").exec().out.map { CmdlineHiddenItem(it) }
            val apps = pm.getInstalledApplications(MATCH_UNINSTALLED_PACKAGES)
                .asSequence()
                .filterNot { blacklist.contains(it.packageName) }
                .map { HideAppInfo(it, pm, hideList) }
                .filter { it.processes.isNotEmpty() }
                .filter { info -> info.enabled || info.processes.any { it.isHidden } }
                .map { HideRvItem(it) }
                .toList()
                .sorted()
>>>>>>> parent of 65b0ea792 (MagiskHide is no more):app/src/main/java/com/topjohnwu/magisk/ui/hide/HideViewModel.kt
            apps to items.calculateDiff(apps)
        }
        items.update(apps, diff)
        query()
    }

    fun query() {
        items.filter {
            fun filterSystem() = isShowSystem || !it.info.isSystemApp()

            fun filterOS() = (isShowSystem && isShowOS) || it.info.isApp()

            fun filterQuery(): Boolean {
                fun inName() = it.info.label.contains(query, true)
                fun inPackage() = it.info.packageName.contains(query, true)
                fun inProcesses() = it.processes.any { p -> p.process.name.contains(query, true) }
                return inName() || inPackage() || inProcesses()
            }

            (it.isChecked || (filterSystem() && filterOS())) && filterQuery()
        }
        loading = false
    }
}
