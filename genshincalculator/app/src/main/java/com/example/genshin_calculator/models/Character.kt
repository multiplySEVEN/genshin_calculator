package com.example.genshin_calculator.models

// 增强版角色数据类
data class Character(
    val id: String,               // 角色ID
    val name: String,             // 角色名称
    val element: String,          // 元素类型
    val weaponType: String,       // 武器类型
    val baseHP: List<Double>,          // 基础生命值
    val baseATK: List<Double>,         // 基础攻击力
    val baseDEF: List<Double>,          // 基础防御力
    val ascensionStat: StatType,  // 突破属性类型
    val ascensionValues: List<Double>, // 突破属性数值(每级)
    val constellations: List<Constellation>, // 命之座效果
    val talents: Talents,         // 天赋数据
    val level: Int = 90,          // 当前等级(默认90)
    val constellation: Int = 0    // 命之座等级(默认0)
) {
    // 命之座效果
    data class Constellation(
        val level: Int,          // 命座等级(1-6)
        val effect: String,       // 效果描述
        val modifiers: Map<String, Double> // 属性修改器
    )

    // 天赋数据
    data class Talents(
        val normalAttack: List<Double>, // 普攻倍率(每级)
        val elementalSkill: List<Double>, // 元素战技倍率
        val elementalBurst: List<Double>, // 元素爆发倍率
        val levels: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13) // 天赋等级
    )
}

// 属性类型枚举
enum class StatType {
    HP, ATK, DEF, HP_PERCENT, ATK_PERCENT, DEF_PERCENT,
    ELEMENTAL_MASTERY, ENERGY_RECHARGE,
    CRIT_RATE, CRIT_DMG, HEALING_BONUS,
    PYRO_DMG, HYDRO_DMG, ELECTRO_DMG, CRYO_DMG,
    ANEMO_DMG, GEO_DMG, DENDRO_DMG, PHYSICAL_DMG,
    NORMAL_ATTACK_DMG,
    CHARGED_ATTACK_DMG,
    PLUNGING_ATTACK_DMG,
}
