package com.example.genshin_calculator.models

// 增强版武器数据类
data class Weapon(
    val name: String,             // 武器名称
    val rarity: Int,              // 星级(3-5)
    val weaponType: String,       // 武器类型
    val baseATK: List<Double>,    // 基础攻击力(每级)
    val subStat: SubStat,         // 副属性
    val passive: Passive,         // 被动效果
    val level: Int = 90,          // 武器等级(默认90)
    val refinement: Int = 1       // 精炼等级(1-5)
) {
    // 副属性
    data class SubStat(
        val type: StatType,       // 属性类型
        val values: List<Double>  // 属性数值(每级)
    )

    // 被动效果
    data class Passive(
        val name: String,         // 效果名称
        val description: String,  // 效果描述
        val refinements: List<Map<String, Double>> // 精炼等级效果(1-5)
    )
}