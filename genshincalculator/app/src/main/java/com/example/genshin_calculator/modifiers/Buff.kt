package com.example.genshin_calculator.modifiers

// 通用增益类，例如班尼特大招、攻击力提升等
data class Buff(
    val name: String,           // 增益名称
    val attackBonus: Int = 0,   // 固定攻击力加成
    val attackPercent: Double = 0.0, // 攻击力百分比加成
    val critRate: Double = 0.0,      // 增加暴击率
    val critDamage: Double = 0.0,    // 增加暴击伤害
    val elementalBonus: Double = 0.0 // 元素伤加成
)
