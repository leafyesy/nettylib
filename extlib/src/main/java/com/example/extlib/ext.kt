package com.example.extlib

import android.view.View

fun View.onClick(method: (() -> Unit)): View {
    setOnClickListener { method.invoke() }
    return this
}