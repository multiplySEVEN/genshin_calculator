package com.example.genshin_calculator.utils

import android.content.Context
import com.example.genshin_calculator.models.Character
import com.example.genshin_calculator.models.Weapon
import com.example.genshin_calculator.models.Artifact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

// 加载JSON数据（角色、武器、圣遗物）到对象
object DataLoader {
    /**
     * 从 res/raw 读取 JSON 数据并解析成 List<T>
     * @param context Context
     * @param resourceId R.raw.xxx（例如 R.raw.characters）
     * @return List<T> 或空列表（如果读取失败）
     */
    inline fun <reified T> loadJsonFromRaw(context: Context, resourceId: Int): List<T> {
        return try {
            val json = context.resources.openRawResource(resourceId)
                .bufferedReader()
                .use { it.readText() }
            val type = object : TypeToken<List<T>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList() // 返回空列表，避免崩溃
        }
    }

    /**
     * 从 assets 读取 JSON 数据（保留原方法，兼容旧代码）
     */
    inline fun <reified T> loadJsonData(context: Context, fileName: String): List<T> {
        return try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<T>>() {}.type
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}
