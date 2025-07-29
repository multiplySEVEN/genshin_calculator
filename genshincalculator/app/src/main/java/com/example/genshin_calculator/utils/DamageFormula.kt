package com.example.genshin_calculator.utils

import com.example.genshin_calculator.modifiers.Enemy
import kotlin.math.max

// 核心伤害公式模块
object DamageFormula {
    fun calculateFinalDamage(
        atk: Double,
        critRate: Double,
        critDamage: Double,
        bonus: Double,
        enemy: Enemy,
        reactionMultiplier: Double
    ): Double {

        // 抗性修正
        val resistance = enemy.resistance - enemy.resistanceReduction
        val resistanceMultiplier = when {
            resistance < 0 -> 1 - resistance / 2
            resistance < 0.75 -> 1 - resistance
            else -> 1.0 / (4 * resistance + 1)
        }

        // 防御修正
        val level = 90
        val defenseMultiplier = (level + 100.0) / ((level + 100.0) + (enemy.level + 100.0) * (1 - enemy.defenseReduction))

        // 暴击期望伤害
        val critMultiplier = 1 + critRate * critDamage

        // 完整公式乘区结构
        return atk * critMultiplier * (1 + bonus) * resistanceMultiplier * defenseMultiplier * reactionMultiplier
    }
}
