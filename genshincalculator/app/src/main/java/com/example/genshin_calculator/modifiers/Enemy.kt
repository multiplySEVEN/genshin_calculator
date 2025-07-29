package com.example.genshin_calculator.modifiers

// 敌人信息：等级、防御、抗性等
data class Enemy(
    val level: Int,                 // 敌人等级
    val resistance: Double,         // 敌人抗性（0.1 = 10%抗）
    val resistanceReduction: Double = 0.0, // 减抗（我方作用于敌人）
    val defenseReduction: Double = 0.0     // 减防（我方作用于敌人）
)
