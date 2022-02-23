package com.example.testapplication.cards

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class DisabledScrollingLayoutManager(context: Context): LinearLayoutManager(context) {

    override fun canScrollVertically(): Boolean {
        return false
    }

}