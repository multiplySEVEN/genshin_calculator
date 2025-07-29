package com.example.genshin_calculator.utils

import com.example.genshin_calculator.models.DamageResult
import com.example.genshin_calculator.models.Character
import com.example.genshin_calculator.models.Weapon
import com.example.genshin_calculator.models.Artifact
import com.example.genshin_calculator.modifiers.*
import kotlin.math.max

// 伤害计算器：整合所有计算逻辑
object Calculator {
    fun calculateDamage(
        character: Character,
        weapon: Weapon,
        artifact: Artifact,
        buffs: List<Buff>,
        teammateBuffs: List<TeammateBuff>,
        enemy: Enemy,
        reaction: Reaction
    ): DamageResult {

        // 基础攻击 = 角色基础 + 武器攻击
        val baseAtk = character.baseAttack + weapon.attack

        // 总攻击力加成（来自圣遗物、队友、增益）
        val totalAtkPercent = artifact.attackPercent +
                buffs.sumOf { it.attackPercent } +
                teammateBuffs.sumOf { it.attackBonus }

        val totalFlatAtk = artifact.flatAttack + buffs.sumOf { it.attackBonus }

        val totalAtk = baseAtk * (1 + totalAtkPercent) + totalFlatAtk

        // 暴击率与暴击伤害
        val critRate = (character.critRate + weapon.critRate + artifact.critRate + buffs.sumOf { it.critRate }).coerceAtMost(1.0)
        val critDmg = character.critDamage + weapon.critDamage + artifact.critDamage + buffs.sumOf { it.critDamage }

        // 元素加成
        val elementBonus = artifact.elementalBonus + buffs.sumOf { it.elementalBonus }

        // 反应倍率
        val reactionMultiplier = ReactionMultiplier.getMultiplier(reaction)

        // 计算最终伤害（调用核心公式）
        val finalDamage = DamageFormula.calculateFinalDamage(
            atk = totalAtk,
            critRate = critRate,
            critDamage = critDmg,
            bonus = elementBonus,
            enemy = enemy,
            reactionMultiplier = reactionMultiplier
        )

        return DamageResult(
            baseAttack = baseAtk,
            totalAttack = totalAtk,
            critRate = critRate,
            critDamage = critDmg,
            bonus = elementBonus,
            reactionMultiplier = reactionMultiplier,
            finalDamage = finalDamage,
            averageDamage = finalDamage
        )
    }
}
