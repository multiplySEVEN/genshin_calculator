package com.example.genshin_calculator.models

// 武器数据类，包含名称、攻击力、副词条（例如暴击率）等
data class Weapon(
    val name: String,               // 武器名称
    val attack: Double,                // 武器提供的攻击力
    val subStatType: String,        // 副词条类型：暴击率、暴击伤害等
    val subStatValue: Double        // 副词条数值，例如0.20代表+20%

    ,val critRate: Double,
    val critDamage: Double
)
