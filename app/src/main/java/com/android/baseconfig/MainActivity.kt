package com.android.baseconfig

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

val permissions = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

const val TAG: String = "MainActivity"


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvApply.setOnClickListener {

            if (EasyPermissions.hasPermissions(this, *permissions)) {
                openCamera()
            } else {
                EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(this, 0x222, *permissions)
                        .setRationale("申请相机权限")
                        .setNegativeButtonText("取消")
                        .setPositiveButtonText("确定")
                        .build()
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    //多个请求权限时，只要有未通过的权限，就会调用这个方法
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        Log.i(TAG, "onPermissionsDenied")

        //验证被拒绝的权限中是否有点了不在询问的权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("应用权限设置")
                .setRationale("未设置的权限可能导致APP部分功能不可以用。前往设置界面进行设置。")
                .setNegativeButton("取消")
                .setPositiveButton("确定")
                .build()
                .show()

        }

    }

    //多个请求审核时，只要有通过的权限就会调用此方法
    //多个权限下，即有通过也有未通过时，两个回调都会被调用
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i(TAG, "onPermissionsGranted")

        if (!EasyPermissions.hasPermissions(this,*permissions)) {
            //有未通过的权限，这里不做处理，在onDenied中处理
            Log.i(TAG, "onPermissionsGranted 有未通过的权限")
            return
        }

        openCamera()
    }

    private fun openCamera() {
        Toast.makeText(this, "成功打开相机", Toast.LENGTH_SHORT).show()
    }
}
