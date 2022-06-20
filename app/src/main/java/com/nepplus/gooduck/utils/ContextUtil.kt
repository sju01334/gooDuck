package com.nepplus.gooduck.utils

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException


class ContextUtil {

    companion object{
        private val prefName = "GooDuck"
        private val LOGIN_TOKEN = "LOGIN_TOKEN"
        private val AUTO_LOGIN = "AUTO_LOGIN"
        private val RECENT_SEARCH = "RECENT_SEARCH"

        fun setLoginToken (context : Context, token : String){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(LOGIN_TOKEN, token).apply()
        }

        fun getLoginToken (context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(LOGIN_TOKEN, "")!!
        }

        fun setAutoLogin(context: Context, autoLogin : Boolean){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putBoolean(AUTO_LOGIN, autoLogin).apply()
        }

        fun getAutoLogin(context: Context) : Boolean{
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(AUTO_LOGIN, false)
        }


        fun setRecentSearch(context: Context, values: ArrayList<String>) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = pref.edit()
            val a = JSONArray()
            for (i in 0 until values.size) {
                a.put(values[i])
            }
            if (!values.isEmpty()) {
                editor.putString(RECENT_SEARCH, a.toString())
            } else {
                editor.putString(RECENT_SEARCH, null)
            }
            editor.apply()
        }

        fun getRecentSearch(context: Context): ArrayList<String>? {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val json = pref.getString(RECENT_SEARCH, null)
            val list = ArrayList<String>()
            if (json != null) {
                try {
                    val a = JSONArray(json)
                    for (i in 0 until a.length()) {
                        val url = a.optString(i)
                        list.add(url)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return list
        }


        fun clear(context: Context){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun clearTag(context: Context, tag : String){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.remove(tag)
            editor.apply()
        }


    }
}