package com.example.genshin_calculator.models

// 角色数据类，包含名称、基础攻击力、元素类型、突破加成等
data class Character(
    val name: String,               // 角色名称
    val baseAttack: Int,           // 角色基础攻击力
    val weaponType: String,        // 武器类型
    val element: String,           // 元素类型，例如：火、水、雷等
    val level: Int,                // 当前等级
    val critRate: Double,          // 暴击率（0.05~1.0）
    val critDamage: Double,        // 暴击伤害（1.5 = +50%）
    val elementalBonus: Double,    // 元素伤加成（例如火元素伤害加成）
    val defenseIgnore: Double = 0.0,   // 无视防御比例（部分角色技能具有）
    val resistanceReduction: Double = 0.0 // 敌人减抗
)
