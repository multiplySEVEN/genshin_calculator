package com.example.genshin_calculator.modifiers

// 提供不同反应类型对应的倍率
object ReactionMultiplier {
    fun getMultiplier(reaction: Reaction): Double {
        return when (reaction) {
            Reaction.VAPORIZE -> 1.5    // 蒸发
            Reaction.MELT -> 2.0        // 融化
            Reaction.OVERLOADED -> 1.0  // 非增伤类，伤害固定
            Reaction.SUPERCONDUCT -> 0.0 // 仅减防，不增伤
            Reaction.ELECTROCHARGED -> 1.0
            Reaction.BLOOM -> 1.0
            Reaction.HYPERBLOOM -> 1.0
            Reaction.BURGEON -> 1.0
            else -> 1.0
        }
    }
}
