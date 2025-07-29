package com.example.genshin_calculator.utils

import com.google.gson.Gson

// JSON 工具类
object JsonUtil {
    inline fun <reified T> toJson(data: T): String {
        return Gson().toJson(data)
    }

    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }
}
