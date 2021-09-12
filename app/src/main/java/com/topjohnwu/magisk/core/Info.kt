package com.topjohnwu.magisk.core

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.topjohnwu.magisk.StubApk
import com.topjohnwu.magisk.core.di.AppContext
import com.topjohnwu.magisk.core.model.UpdateInfo
import com.topjohnwu.magisk.core.repository.NetworkService
import com.topjohnwu.magisk.core.utils.net.NetworkObserver
import com.topjohnwu.magisk.ktx.getProperty
import com.topjohnwu.superuser.ShellUtils.fastCmd

val isRunningAsStub get() = Info.stub != null

object Info {

    var stub: StubApk.Data? = null

    val EMPTY_REMOTE = UpdateInfo()
    var remote = EMPTY_REMOTE
    suspend fun getRemote(svc: NetworkService): UpdateInfo? {
        return if (remote === EMPTY_REMOTE) {
            svc.fetchUpdate()?.apply { remote = this }
        } else remote
    }

    // Device state
    @JvmStatic val env by lazy { loadState() }
    @JvmField var isSAR = false
    var isAB = false
    @JvmField val isZygiskEnabled = System.getenv("ZYGISK_ENABLED") == "1"
    @JvmStatic val isFDE get() = crypto == "block"
    @JvmField var ramdisk = false
    @JvmField var vbmeta = false
    var crypto = ""
    var noDataExec = false
    var isRooted = false

    @JvmField var hasGMS = true
    val isSamsung = Build.MANUFACTURER.equals("samsung", ignoreCase = true)
    @JvmField val isEmulator =
        getProperty("ro.kernel.qemu", "0") == "1" ||
            getProperty("ro.boot.qemu", "0") == "1"

    val isConnected: LiveData<Boolean> by lazy {
        MutableLiveData(false).also { field ->
            NetworkObserver.observe(AppContext) {
                remote = EMPTY_REMOTE
                field.postValue(it)
            }
        }
    }

<<<<<<< HEAD
    private fun loadState(): Env {
        val v = fastCmd("magisk -v").split(":".toRegex())
        return Env(
            v[0], v.size >= 3 && v[2] == "D",
            runCatching { fastCmd("magisk -V").toInt() }.getOrDefault(-1)
        )
    }

    class Env(
        val versionString: String = "",
        val isDebug: Boolean = false,
        code: Int = -1
    ) {
        val versionCode = when {
=======
    private fun loadState() = Env(
        fastCmd("magisk -v").split(":".toRegex())[0],
        runCatching { fastCmd("magisk -V").toInt() }.getOrDefault(-1),
        Shell.su("magiskhide status").exec().isSuccess
    )

    class Env(
        val magiskVersionString: String = "",
        code: Int = -1,
        hide: Boolean = false
    ) {
        val magiskHide get() = Config.magiskHide
        val magiskVersionCode = when {
>>>>>>> parent of 65b0ea792 (MagiskHide is no more)
            code < Const.Version.MIN_VERCODE -> -1
            isRooted ->  code
            else -> -1
        }
        val isUnsupported = code > 0 && code < Const.Version.MIN_VERCODE
<<<<<<< HEAD
        val isActive = versionCode > 0
=======
        val isActive = magiskVersionCode >= 0

        init {
            Config.magiskHide = hide
        }
>>>>>>> parent of 65b0ea792 (MagiskHide is no more)
    }
}
