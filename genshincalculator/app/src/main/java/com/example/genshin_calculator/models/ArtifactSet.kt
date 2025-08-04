package com.example.genshin_calculator.models

// 圣遗物套装加成，例如：角斗士2件套 +18%攻击，乐团4件套 +35%元素伤害等
data class ArtifactSet(
    val name: String,             // 套装名称
    val twoPieceBonus: String,   // 2件套效果描述
    val fourPieceBonus: String   // 4件套效果描述
)
/*
// 圣遗物套装效果
data class ArtifactSet(
    val name: String,             // 套装名称
    val twoPiece: SetEffect,      // 2件套效果
    val fourPiece: SetEffect      // 4件套效果
) {
    data class SetEffect(
        val description: String,  // 效果描述
        val modifiers: Map<String, Double> // 属性修改器
    )
}
*/