package com.example.genshin_calculator.models

// 圣遗物数据类：五件部位合并为一个对象
data class Artifact(
    val name: String,
    val mainStat: String,    // 主词条描述
    val attackPercent: Double,     // 攻击力百分比加成
    val flatAttack: Int,           // 固定攻击力加成
    val critRate: Double,          // 暴击率加成
    val critDamage: Double,        // 暴击伤害加成
    val elementalBonus: Double     // 元素伤加成
)
