package com.lwj.wants.util

import android.app.Activity
import android.content.DialogInterface
import com.lwj.wants.view.ProgressDialog

class ProgressDialogUtil {
    companion object {
        private var pd: ProgressDialog? = null
        fun showProgressDialog(activity: Activity) {
            showProgressDialog(activity, "加载中", true, null)
        }

        fun showProgressDialog(activity: Activity, listener: DialogInterface.OnCancelListener) {
            showProgressDialog(activity, "加载中", true, listener)
        }

        fun showProgressDialog(activity: Activity, msg: String) {
            showProgressDialog(activity, msg, true, null)
        }
        fun showProgressDialog(
            activity: Activity,
            msg: String,
            listener: DialogInterface.OnCancelListener
        ) {
            showProgressDialog(activity, msg, true, listener)
        }

        fun showProgressDialog(activity: Activity, msg: String, cancelable: Boolean) {
            showProgressDialog(activity, msg, cancelable, null)
        }

        fun showProgressDialog(
            activity: Activity?,
            msg: String,
            cancelable: Boolean,
            listener: DialogInterface.OnCancelListener?
        ) {
            if (activity == null || activity.isFinishing) {
                return
            }
            if (pd == null) {
                pd = ProgressDialog(activity).setMessage(msg).cancelable(cancelable)
                pd?.setOwnerActivity(activity)
                pd?.setOnCancelListener(listener)
            } else {
                if (activity == pd!!.ownerActivity) {
                    pd!!.setMessage(msg)
                    pd!!.setCancelable(cancelable)
                    pd!!.setOnCancelListener(listener)
                } else {
                    cancelProgressDialog()
                    pd = ProgressDialog(activity).setMessage(msg).cancelable(cancelable)
                    pd?.setOwnerActivity(activity)
                    pd?.setOnCancelListener(listener)
                }
            }
            if (!pd!!.isShowing) {
                pd!!.show()
            }
        }

        fun cancelProgressDialog(activity: Activity) {
            if (pd != null && pd!!.isShowing) {
                if (pd!!.ownerActivity === activity) {
                    pd!!.cancel()
                    pd = null
                }
            }
        }

        fun cancelProgressDialog() {
            if (pd != null && pd!!.isShowing) {
                pd!!.cancel()
                pd = null
            }
        }
    }
}
