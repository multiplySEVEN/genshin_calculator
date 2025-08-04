package com.example.genshin_calculator.utils

import com.example.genshin_calculator.modifiers.Enemy
import kotlin.math.max

// 增强版伤害公式
object DamageFormula {
    /**
     * 计算最终伤害
     * @param atk 总攻击力
     * @param talentMultiplier 天赋倍率
     * @param critRate 暴击率(0-1)
     * @param critDamage 暴击伤害(如1.5表示150%)
     * @param damageBonus 伤害加成(如0.466表示46.6%)
     * @param enemy 敌人数据
     * @param reactionMultiplier 反应倍率
     * @return 最终伤害
     */
    fun calculateFinalDamage(
        atk: Double,
        talentMultiplier: Double,
        critRate: Double,
        critDamage: Double,
        damageBonus: Double,
        enemy: Enemy,
        reactionMultiplier: Double
    ): Double {
        // 1. 基础伤害 = 攻击力 × 天赋倍率
        val baseDamage = atk * talentMultiplier

        // 2. 暴击期望 = 基础伤害 × (1 + 暴击率 × 暴击伤害)
        val critExpected = baseDamage * (1 + critRate * critDamage)

        // 3. 伤害加成 = 基础伤害 × (1 + 伤害加成)
        val damageWithBonus = critExpected * (1 + damageBonus)

        // 4. 抗性修正
        val resistance = enemy.resistance - enemy.resistanceReduction
        val resistanceMultiplier = calculateResistanceMultiplier(resistance)

        // 5. 防御修正
        val defenseMultiplier = calculateDefenseMultiplier(
            attackerLevel = 90, // 假设角色90级
            enemyLevel = enemy.level,
            defenseReduction = enemy.defenseReduction
        )

        // 6. 完整公式: 基础 × 暴击 × 加成 × 抗性 × 防御 × 反应
        return damageWithBonus * resistanceMultiplier * defenseMultiplier * reactionMultiplier
    }

    // 计算抗性乘区
    private fun calculateResistanceMultiplier(resistance: Double): Double {
        return when {
            resistance < 0 -> 1 - resistance / 2  // 负抗性收益减半
            resistance < 0.75 -> 1 - resistance    // 正常抗性
            else -> 1.0 / (4 * resistance + 1)     // 高抗性衰减
        }
    }

    // 计算防御乘区
    private fun calculateDefenseMultiplier(
        attackerLevel: Int,
        enemyLevel: Int,
        defenseReduction: Double
    ): Double {
        return (attackerLevel + 100.0) /
                ((attackerLevel + 100.0) + (enemyLevel + 100.0) * (1 - defenseReduction))
    }
}