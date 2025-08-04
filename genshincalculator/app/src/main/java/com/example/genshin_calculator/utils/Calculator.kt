package com.example.genshin_calculator.utils

import com.example.genshin_calculator.models.*
import com.example.genshin_calculator.modifiers.*
import kotlin.math.max

// 增强版伤害计算器
object Calculator {
    /**
     * 计算最终伤害
     * @param character 角色数据
     * @param weapon 武器数据
     * @param artifacts 圣遗物列表(5件)
     * @param talentLevel 天赋等级(普攻/战技/爆发)
     * @param attackType 攻击类型(普攻/重击/下落攻击/战技/爆发)
     * @param attackMultiplier 攻击倍率(来自JSON)
     * @param buffs 自身增益
     * @param teammateBuffs 队友增益
     * @param enemy 敌人数据
     * @param reaction 元素反应类型
     * @param reactionMultiplier 反应倍率(可选覆盖默认)
     * @return 伤害结果
     */
    fun calculateDamage(
        character: Character,
        weapon: Weapon,
        artifacts: List<Artifact>,
        talentLevel: Int = 10,
        attackType: AttackType = AttackType.NORMAL,
        attackMultiplier: Double = 1.0,
        buffs: List<Buff> = emptyList(),
        teammateBuffs: List<TeammateBuff> = emptyList(),
        enemy: Enemy = Enemy(level = 90, resistance = 0.1),
        reaction: Reaction = Reaction.NONE,
        reactionMultiplier: Double? = null
    ): DamageResult {
        // 1. 计算基础属性
        val charLevelIndex = character.level / 10  // 简化处理，实际应根据成长曲线计算
        val baseHP = character.baseHP.getOrNull(charLevelIndex) ?: 0.0
        val baseATK = (character.baseATK.getOrNull(charLevelIndex) ?: 0.0) +
                (weapon.baseATK.getOrNull(weapon.level / 10) ?: 0.0)
        val baseDEF = character.baseDEF.getOrNull(charLevelIndex) ?: 0.0

        // 2. 计算武器效果
        val weaponSubStat = weapon.subStat.values[weapon.level / 10]
        val weaponPassive = weapon.passive.refinements[weapon.refinement - 1]

        // 3. 计算圣遗物属性
        val artifactStats = calculateArtifactStats(artifacts)

        // 4. 计算天赋倍率
        val talentMultiplier = when (attackType) {
            AttackType.NORMAL -> character.talents.normalAttack[talentLevel - 1]
            AttackType.ELEMENTAL_SKILL -> character.talents.elementalSkill[talentLevel - 1]
            AttackType.ELEMENTAL_BURST -> character.talents.elementalBurst[talentLevel - 1]
            else -> 1.0
        } * attackMultiplier

        // 5. 计算总攻击力
        val totalATK = calculateTotalATK(
            baseATK = baseATK,
            baseHP = baseHP,
            character = character,
            weapon = weapon,
            artifacts = artifactStats,
            buffs = buffs,
            teammateBuffs = teammateBuffs
        )

        // 6. 计算暴击属性
        val (critRate, critDMG) = calculateCritStats(
            character = character,
            weapon = weapon,
            artifacts = artifactStats,
            buffs = buffs,
            teammateBuffs = teammateBuffs
        )

        // 7. 计算伤害加成
        val damageBonus = calculateDamageBonus(
            character = character,
            artifacts = artifactStats,
            buffs = buffs,
            teammateBuffs = teammateBuffs,
            attackType = attackType,
            element = character.element
        )

        // 8. 计算元素反应
        val reactionMult = reactionMultiplier ?: ReactionMultiplier.getMultiplier(reaction)

        // 9. 计算最终伤害
        val finalDamage = DamageFormula.calculateFinalDamage(
            atk = totalATK,
            talentMultiplier = talentMultiplier,
            critRate = critRate,
            critDamage = critDMG,
            damageBonus = damageBonus,
            enemy = enemy,
            reactionMultiplier = reactionMult
        )

        return DamageResult(
            baseAttack = baseATK,
            totalAttack = totalATK,
            critRate = critRate,
            critDamage = critDMG,
            damageBonus = damageBonus,
            reactionMultiplier = reactionMult,
            finalDamage = finalDamage,
            averageDamage = finalDamage * (1 + critRate * critDMG) // 期望伤害
        )
    }

    // 计算圣遗物总属性
    private fun calculateArtifactStats(artifacts: List<Artifact>): Map<StatType, Double> {
        val stats = mutableMapOf<StatType, Double>()

        artifacts.forEach { artifact ->
            // 主词条
            stats.merge(artifact.mainStat.type, artifact.mainStat.values[artifact.level / 4]) { a, b -> a + b }

            // 副词条
            artifact.subStats.forEach { subStat ->
                stats.merge(subStat.type, subStat.value * subStat.rolls) { a, b -> a + b }
            }
        }

        return stats
    }

    // 计算总攻击力
    private fun calculateTotalATK(
        baseATK: Double,
        baseHP: Double,
        character: Character,
        weapon: Weapon,
        artifacts: Map<StatType, Double>,
        buffs: List<Buff>,
        teammateBuffs: List<TeammateBuff>
    ): Double {
        // 基础攻击力
        var totalATK = baseATK

        // 攻击力百分比加成
        val atkPercent = (artifacts[StatType.ATK_PERCENT] ?: 0.0) +
                buffs.sumOf { it.attackPercent } +
                teammateBuffs.sumOf { it.attackBonus }

        // 固定攻击力加成
        val flatATK = (artifacts[StatType.ATK] ?: 0.0) +
                buffs.sumOf { it.attackBonus }

        // 生命值转攻击力(如胡桃、护摩之杖)
        val hpToATK = weapon.passive.refinements.getOrNull(weapon.refinement - 1)?.get("HP_TO_ATK_RATE") ?: 0.0
        val hpBasedATK = if (hpToATK > 0) baseHP * hpToATK else 0.0

        // 计算总攻击力
        return totalATK * (1 + atkPercent) + flatATK + hpBasedATK
    }

    // 计算暴击属性
    private fun calculateCritStats(
        character: Character,
        weapon: Weapon,
        artifacts: Map<StatType, Double>,
        buffs: List<Buff>,
        teammateBuffs: List<TeammateBuff>
    ): Pair<Double, Double> {
        // 基础暴击率(5%)和暴击伤害(50%)
        var critRate = 0.05
        var critDMG = 0.50

        // 角色突破属性
        if (character.ascensionStat == StatType.CRIT_RATE) {
            critRate += character.ascensionValues[character.level / 20]
        } else if (character.ascensionStat == StatType.CRIT_DMG) {
            critDMG += character.ascensionValues[character.level / 20]
        }

        // 武器副属性
        if (weapon.subStat.type == StatType.CRIT_RATE) {
            critRate += weapon.subStat.values[weapon.level / 10] / 100
        } else if (weapon.subStat.type == StatType.CRIT_DMG) {
            critDMG += weapon.subStat.values[weapon.level / 10] / 100
        }

        // 圣遗物属性
        critRate += (artifacts[StatType.CRIT_RATE] ?: 0.0) / 100
        critDMG += (artifacts[StatType.CRIT_DMG] ?: 0.0) / 100

        // 增益效果
        critRate += buffs.sumOf { it.critRate }
        critDMG += buffs.sumOf { it.critDamage }

        // 队友增益
        critRate += teammateBuffs.sumOf { it.critRateBonus }
        critDMG += teammateBuffs.sumOf { it.critDamageBonus }

        // 暴击率限制在5%-100%
        return critRate.coerceIn(0.05, 1.0) to critDMG
    }

    // 计算伤害加成
    private fun calculateDamageBonus(
        character: Character,
        artifacts: Map<StatType, Double>,
        buffs: List<Buff>,
        teammateBuffs: List<TeammateBuff>,
        attackType: AttackType,
        element: String
    ): Double {
        var bonus = 0.0

        // 元素伤害加成
        val elementBonusType = when (element) {
            "火" -> StatType.PYRO_DMG
            "水" -> StatType.HYDRO_DMG
            "雷" -> StatType.ELECTRO_DMG
            "冰" -> StatType.CRYO_DMG
            "风" -> StatType.ANEMO_DMG
            "岩" -> StatType.GEO_DMG
            "草" -> StatType.DENDRO_DMG
            else -> StatType.PHYSICAL_DMG
        }

        bonus += (artifacts[elementBonusType] ?: 0.0) / 100

        // 攻击类型加成(如普攻加成、重击加成)
        when (attackType) {
            AttackType.NORMAL -> bonus += (artifacts[StatType.NORMAL_ATTACK_DMG] ?: 0.0) / 100
            AttackType.CHARGED -> bonus += (artifacts[StatType.CHARGED_ATTACK_DMG] ?: 0.0) / 100
            AttackType.PLUNGING -> bonus += (artifacts[StatType.PLUNGING_ATTACK_DMG] ?: 0.0) / 100
            else -> {}
        }

        // 增益效果
        bonus += buffs.sumOf { it.elementalBonus }

        // 队友增益
        bonus += teammateBuffs.sumOf { it.elementBonus }

        return bonus
    }
}
/*
// 攻击类型枚举
enum class AttackType {
    NORMAL, CHARGED, PLUNGING, ELEMENTAL_SKILL, ELEMENTAL_BURST
}
*/