package com.example.testbottomview

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*
@Navigator.Name("fixFragment")
class FixFragmentNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    companion object {
        private const val TAG = "FixFragmentNavigator"
    }

    @ExperimentalStdlibApi
    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            Log.i(
                Companion.TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
        /*val frag = instantiateFragment(
            context, manager,
            className, args
        )
        frag.arguments = args*/

        val ft = manager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

//        ft.replace(containerId, frag)
        /**
         * 1 、先查询当前显示的 fragment 不为空则将其 hide
         * 2 、根据 tag 查询当前添加的 fragment 是否不为 null，不为 null 则将其直接 show
         * 3 、为 null 则通过 instantiateFragment 方法创建 fragment 实例
         * 4 、将创建的实例添加在事务中
         */
        val primaryNavigationFragment = manager.primaryNavigationFragment
        if (primaryNavigationFragment != null){
            Log.i(TAG, "navigate: hideFragment")
            ft.hide(primaryNavigationFragment)
        }
        val tag = destination.id.toString()
        var findFragment: Fragment? = manager.findFragmentByTag(tag)
        if (findFragment != null) {
            ft.show(findFragment)
        } else {
            findFragment = instantiateFragment(context,manager, className,args)
            findFragment.arguments = args
            ft.add(containerId,findFragment,tag)
        }
        ft.setPrimaryNavigationFragment(findFragment)
        @IdRes val destId = destination.id
        var mBackStack: ArrayDeque<Int>? = null
        try {
            val mBackStackField = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            mBackStackField.isAccessible = true
            mBackStack = mBackStackField.get(this) as ArrayDeque<Int>
        } catch (e: Exception){
            e.printStackTrace()
        }

        val initialNavigation = mBackStack?.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation!!
                && navOptions.shouldLaunchSingleTop()
                && mBackStack?.peekLast()?.toInt() == destId)

        val isAdded: Boolean
        isAdded = if (initialNavigation!!) {
            true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack!!.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                manager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack!!.size + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack!!.add(destId)
            destination
        } else {
            null
        }
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String? {
        return "$backStackIndex-$destId"
    }
}