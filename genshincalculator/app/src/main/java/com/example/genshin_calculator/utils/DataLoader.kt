package com.example.genshin_calculator.utils

import android.content.Context
import com.example.genshin_calculator.models.Character
import com.example.genshin_calculator.models.Weapon
import com.example.genshin_calculator.models.Artifact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// 加载JSON数据（角色、武器、圣遗物）到对象
object DataLoader {
    inline fun <reified T> loadJsonData(context: Context, fileName: String): List<T> {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<T>>() {}.type
        return Gson().fromJson(json, type)
    }
}
