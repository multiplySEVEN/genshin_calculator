package com.example.genshin_calculator.models

// 最终伤害计算结果
/*
data class DamageResult(
    val normalDamage: Double,     // 非暴击伤害
    val critDamage: Double,       // 暴击伤害
    val averageDamage: Double,    // 平均期望伤害（带暴击期望）
    val reactionType: String,     // 元素反应类型，例如“蒸发”
    val reactionMultiplier: Double // 反应倍率（如1.5蒸发、2.0融化）
)
*/
/**
 * 伤害结果数据类，包含各个计算阶段的数据
 */
data class DamageResult(
    val baseAttack: Double,
    val totalAttack: Double,
    val critRate: Double,
    val critDamage: Double,
    val averageDamage: Double,    // 平均期望伤害
    val damageBonus: Double,
    val finalDamage: Double,
    val reactionMultiplier: Double
)
