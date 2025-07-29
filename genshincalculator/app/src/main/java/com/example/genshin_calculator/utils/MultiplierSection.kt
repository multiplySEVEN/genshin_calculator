package com.example.genshin_calculator.utils

// 表示一个乘区（如暴击区、攻击区等），内部可包含多个乘项
data class MultiplierSection(
    val sectionType: SectionType,
    val multipliers: List<Multiplier>
) {
    // 计算该乘区的整体乘积值
    fun calculateSectionMultiplier(): Double {
        return multipliers.fold(1.0) { acc, multiplier ->
            acc * (1 + multiplier.value)
        }
    }
}
