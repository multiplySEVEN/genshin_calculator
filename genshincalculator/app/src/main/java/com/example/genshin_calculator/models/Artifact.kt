package com.example.genshin_calculator.models

// 增强版圣遗物数据类
data class Artifact(
    val setName: String,          // 套装名称
    val slot: SlotType,           // 部位类型
    val mainStat: MainStat,       // 主词条
    val subStats: List<SubStat>,  // 副词条(最多4条)
    val level: Int = 20           // 强化等级(默认20)
) {
    // 部位类型枚举
    enum class SlotType { FLOWER, PLUME, SANDS, GOBLET, CIRCLET }

    // 主词条
    data class MainStat(
        val type: StatType,       // 属性类型
        val values: List<Double>  // 属性数值(每级)
    )

    // 副词条
    data class SubStat(
        val type: StatType,       // 属性类型
        val value: Double,       // 属性数值
        val rolls: Int = 1       // 强化次数(默认1)
    )
}