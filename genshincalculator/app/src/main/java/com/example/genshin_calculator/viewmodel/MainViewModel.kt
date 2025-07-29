package com.example.genshin_calculator.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.genshin_calculator.models.*
import com.example.genshin_calculator.modifiers.Enemy
import com.example.genshin_calculator.modifiers.Reaction
import com.example.genshin_calculator.utils.Calculator
import com.example.genshin_calculator.utils.DataLoader

/**
 * ViewModel 层：负责加载数据并提供伤害计算调用接口
 */
class MainViewModel : ViewModel() {

    // 用于观察的角色、武器、圣遗物、结果数据
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    private val _weapons = MutableLiveData<List<Weapon>>()
    val weapons: LiveData<List<Weapon>> = _weapons

    private val _artifacts = MutableLiveData<List<Artifact>>()
    val artifacts: LiveData<List<Artifact>> = _artifacts

    private val _results = MutableLiveData<List<DamageResult>>()
    val results: LiveData<List<DamageResult>> = _results

    /**
     * 从 assets 中加载所有静态 JSON 数据
     */
    fun loadData(context: Context) {
        _characters.value = DataLoader.loadJsonData(context, "characters.json")
        _weapons.value = DataLoader.loadJsonData(context, "weapons.json")
        _artifacts.value = DataLoader.loadJsonData(context, "artifacts.json")
    }

    /**
     * 根据选定的索引和参数生成伤害结果列表
     */
    fun calculate(
        charIndex: Int,
        weapIndex: Int,
        reactionType: Reaction = Reaction.VAPORIZE
    ) {
        val chars = _characters.value ?: return
        val weaps = _weapons.value ?: return
        val arts = _artifacts.value ?: return

        val char = chars.getOrNull(charIndex) ?: return
        val weap = weaps.getOrNull(weapIndex) ?: return

        // 使用前五个圣遗物组合示例来演示结果
        val list = arts.take(5).map { art ->
            Calculator.calculateDamage(
                character = char,
                weapon = weap,
                artifact = art,
                buffs = emptyList(),          // 后续可改为动态输入
                teammateBuffs = emptyList(),
                enemy = Enemy(level = 90, resistance = 0.1),
                reaction = reactionType
            )
        }
        _results.value = list
    }
}
