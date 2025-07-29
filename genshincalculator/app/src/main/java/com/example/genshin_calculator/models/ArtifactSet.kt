package com.example.genshin_calculator.models

// 圣遗物套装加成，例如：角斗士2件套 +18%攻击，乐团4件套 +35%元素伤害等
data class ArtifactSet(
    val name: String,             // 套装名称
    val twoPieceBonus: String,   // 2件套效果描述
    val fourPieceBonus: String   // 4件套效果描述
)
