package com.manuchar.themoviedb.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    protected lateinit var views: B
        private set

    abstract val bindingInflater: (LayoutInflater) -> B

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = getColor(androidx.appcompat.R.color.primary_dark_material_dark)
        super.onCreate(savedInstanceState)
        views = bindingInflater.invoke(layoutInflater).apply {
            setContentView(root)
        }
    }


}