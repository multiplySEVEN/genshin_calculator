package com.example.genshin_calculator.modifiers

// 队友提供的增益，如夜兰、班尼特等
data class TeammateBuff(
    val source: String,             // 来源角色
    val description: String,        // 描述
    val attackBonus: Double = 0.0,  // 攻击力百分比提升
    val critRateBonus: Double = 0.0,
    val critDamageBonus: Double = 0.0,
    val elementBonus: Double = 0.0
)
