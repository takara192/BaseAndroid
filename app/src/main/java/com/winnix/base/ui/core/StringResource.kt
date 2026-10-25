package com.winnix.base.ui.core

import android.content.Context
import androidx.annotation.StringRes

sealed class StringResource {
    data class Resource(@param:StringRes val id: Int) : StringResource()
    data class Text(val value: String) : StringResource()

    fun resolve(context: Context): String = when (this) {
        is Resource -> context.getString(id)
        is Text -> value
    }
}
