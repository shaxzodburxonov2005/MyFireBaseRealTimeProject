package com.example.myfirebaserealtimeproject

import android.content.Context
import android.content.SharedPreferences

object MySharedPreference {
    private const val NAME="nameSplash"
    private const val MODE= Context.MODE_PRIVATE
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context){
        sharedPreferences=context.getSharedPreferences(NAME, MODE)
    }
    private inline fun SharedPreferences.edit(operation:(SharedPreferences.Editor)->Unit){
        val editor: SharedPreferences.Editor=edit()
        operation(editor)
        editor.apply()
    }
    var editSplash:Boolean?
        get() = sharedPreferences.getBoolean("name",false)
        set(value) = sharedPreferences.edit {
            if (value!=null){
                it.putBoolean("name",value)
            }
        }
}