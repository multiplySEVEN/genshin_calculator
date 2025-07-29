package com.example.genshin_calculator.modifiers

// 元素反应类型：用于选择反应倍率
enum class Reaction {
    NONE,       // 无反应
    VAPORIZE,   // 蒸发（火+水 或 水+火）
    MELT,       // 融化（火+冰 或 冰+火）
    OVERLOADED, // 超载
    SUPERCONDUCT, // 超导
    ELECTROCHARGED, // 感电
    BLOOM,      // 绽放
    HYPERBLOOM, // 超绽放
    BURGEON     // 烈绽放
}
