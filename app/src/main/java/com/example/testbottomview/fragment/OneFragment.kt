package com.example.testbottomview.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testbottomview.R
import kotlinx.android.synthetic.main.one_fragment.*

const val REQUEST_EXTERNAL_STORAGE: Int = 1

class OneFragment : Fragment() {

    companion object {
        fun newInstance() = OneFragment()
    }

    private lateinit var viewModel: OneViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.one_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OneViewModel::class.java)
        mImgSave.run {
            setOnClickListener {
                if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_EXTERNAL_STORAGE)
                }else{
                    viewModel.saveToPhoto(imageView.drawable.toBitmap())
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    viewModel.saveToPhoto(imageView.drawable.toBitmap())
                }else{
                    Toast.makeText(requireContext(), "保存失败,请赋予应用写入存储权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}