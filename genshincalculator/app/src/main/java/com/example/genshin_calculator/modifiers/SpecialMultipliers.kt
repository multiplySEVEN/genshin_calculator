package com.example.genshin_calculator.modifiers

// 一些特殊角色或命座的特殊增伤逻辑集中在此
object SpecialMultipliers {

    // 如雷电将军E技能期间提升伤害倍率
    fun getRaidenBurstBonus(energyCost: Int): Double {
        // 比如：每点元素爆发能量增加0.3%加成
        return 1.0 + energyCost * 0.003
    }

    // 芙宁娜生命值转化为增伤（例子）
    fun getFurinaBonus(maxHp: Int): Double {
        // 假设每1000点生命值提供0.5%增伤
        return 1.0 + (maxHp / 1000) * 0.005
    }

    // 八重神子命座6提升技能伤害
    fun getYaeC6Bonus(): Double {
        return 1.2 // 增加20%
    }
}
