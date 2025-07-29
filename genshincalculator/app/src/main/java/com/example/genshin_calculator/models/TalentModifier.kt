package com.example.genshin_calculator.models

// 技能倍率数据类：用于天赋倍率伤害计算
data class TalentModifier(
    val talentName: String,      // 技能名称（如普攻、元素战技）
    val multiplier: Double       // 倍率百分比，例如150.0表示150%
)
