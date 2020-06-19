package com.example.base.utlis

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 *
 *
 *
 * App工具类
 */
object ApplicationUtils {
    /**
     * 备份App 首先无需提升权限就就可以复制APK，查看权限你就会知道，在data/app下的APK权限如下：-rw-r--r-- system
     *
     * @param packageName
     * @param mActivity
     * @throws IOException
     */
    @Throws(IOException::class)
    fun backupApp(packageName: String, mActivity: Activity) {
        // 存放位置
        val newFile = Environment.getExternalStorageDirectory()
            .absolutePath + File.separator
        var oldFile: String? = null
        try {
            // 原始位置
            oldFile = mActivity.packageManager.getApplicationInfo(packageName, 0).sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        println(newFile)
        println(oldFile)
        val `in` = File(oldFile)
        val out = File("$newFile$packageName.apk")
        if (!out.exists()) {
            out.createNewFile()
            Toast.makeText(mActivity, "文件备份成功！" + "存放于" + newFile + "目录下", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(mActivity, "文件已经存在！" + "查看" + newFile + "目录下", Toast.LENGTH_SHORT).show()
        }
        val fis = FileInputStream(`in`)
        val fos = FileOutputStream(out)
        var count: Int
        // 文件太大的话，我觉得需要修改
        val buffer = ByteArray(256 * 1024)
        while (fis.read(buffer).also { count = it } > 0) {
            fos.write(buffer, 0, count)
        }
        fis.close()
        fos.flush()
        fos.close()
    }

    /**
     * 获取当前Apk版本号 android:versionCode
     *
     * @param context
     * @return
     */
    fun getVerCode(context: Context): Int {
        var verCode = -1
        try {
            verCode =
                context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return verCode
    }

    fun getVerName(context: Context): String {
        try {
            return context.packageManager
                .getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return ""
    }

    /**
     * 返回当前的应用是否处于前台显示状态 不需要android.permission.GET_TASKS权限
     *
     * @param packageName
     * @return
     */
    fun isTopActivity(
        context: Context,
        packageName: String
    ): Boolean {
        // _context是一个保存的上下文
        val am = context.applicationContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.runningAppProcesses
        if (list.size == 0) return false
        for (process in list) {
            if (process.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.processName == packageName) {
                return true
            }
        }
        return false
    }

    /**
     * 检测APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    fun checkAppExist(
        context: Context,
        packageName: String
    ): Boolean {
        try {
            val info =
                context.packageManager.getApplicationInfo(packageName, 0)
            return info != null && info.packageName == packageName
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: Exception) {
        }
        return false
    }

    /**
     * 判断是否是DEBUG模式
     * @param context
     * @return
     */
    fun isApkDebugable(context: Context): Boolean {
        try {
            val info = context.applicationInfo
            return info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
        }
        return false
    }

    /**
     * 获取app名字
     * @param context
     * @return
     */
    fun getAppName(context: Context): String {
        val pm = context.packageManager
        return context.applicationInfo.loadLabel(pm).toString()
    }

    /**
     * 获取包名
     * @param context
     * @return
     */
    fun getPackgeName(context: Context): String {
        return context.packageName
    }

    /**
     * 调用系统浏览器下载
     * @param context
     * @param url
     */
    fun download(context: Context, url: String?) {
        if (url != null) {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse(url)
            intent.data = content_url
            context.startActivity(intent)
        }
    }

    /**
     * 跳转到权限设置界面
     */
    fun getAppDetailSettingIntent(context: Context) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            intent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(intent)
    }

    /**
     * 安装软件
     *
     * @param file
     */
    fun installApk(activity: Activity, file: File) {
        val install = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.action = Intent.ACTION_INSTALL_PACKAGE
            val contentUri: Uri =
                FileProvider.getUriForFile(activity, "com.qibeigo.ssm.fileprovider", file)
            install.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            install.action = Intent.ACTION_VIEW
            val uri = Uri.fromFile(file)
            install.setDataAndType(uri, "application/vnd.android.package-archive")
        }
        // 执行意图进行安装
        activity.startActivity(install)
    }
}