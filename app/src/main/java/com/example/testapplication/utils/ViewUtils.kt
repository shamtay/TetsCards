package com.example.testapplication.utils

import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException

fun RecyclerView.requireAdapter(): RecyclerView.Adapter<*> {
    return adapter ?: throw IllegalStateException("Adapter is not set")
}