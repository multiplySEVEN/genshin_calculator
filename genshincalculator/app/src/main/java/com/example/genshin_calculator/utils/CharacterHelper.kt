package com.example.genshin_calculator.utils

import com.example.genshin_calculator.models.Character

// 提供角色辅助工具，例如处理特殊逻辑
object CharacterHelper {
    fun isSpecial(character: Character): Boolean {
        return character.name.contains("雷电将军") || character.name.contains("芙宁娜")
    }
}
