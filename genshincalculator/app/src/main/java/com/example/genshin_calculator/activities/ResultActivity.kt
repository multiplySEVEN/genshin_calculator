package com.example.genshin_calculator.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.genshin_calculator.R
import com.example.genshin_calculator.adapters.DamageResultAdapter
import com.example.genshin_calculator.models.*
import com.example.genshin_calculator.modifiers.*
import com.example.genshin_calculator.utils.Calculator
import com.example.genshin_calculator.utils.DataLoader
import com.example.genshin_calculator.utils.AttackType

/**
 * 结果页：显示伤害计算结果
 */
class ResultActivity : AppCompatActivity() {

    private lateinit var rvResults: RecyclerView
    private val resultList = mutableListOf<DamageResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        rvResults = findViewById(R.id.rv_results)
        rvResults.layoutManager = LinearLayoutManager(this)

        // 获取传递的参数
        val charIdx = intent.getIntExtra("char_index", -1)
        val weapIdx = intent.getIntExtra("weap_index", -1)
        val charLevel = intent.getIntExtra("char_level", 90)
        val weapLevel = intent.getIntExtra("weap_level", 90)
        val constellation = intent.getIntExtra("constellation", 0)
        val refinement = intent.getIntExtra("refinement", 1)
        val normalAttack = intent.getIntExtra("normal_attack", 10)
        val skill = intent.getIntExtra("skill", 10)
        val burst = intent.getIntExtra("burst", 10)
        val attackType = intent.getIntExtra("attack_type", 0)
        val reaction = intent.getIntExtra("reaction", 0)

        // 加载数据
        val chars = DataLoader.loadJsonFromRaw<Character>(this, R.raw.characters)
        val weaps = DataLoader.loadJsonFromRaw<Weapon>(this, R.raw.weapons)
        val arts = DataLoader.loadJsonFromRaw<Artifact>(this, R.raw.artifacts)

        if (charIdx >= 0 && weapIdx >= 0) {
            // 创建角色和武器对象(带等级和精炼)
            val char = chars[charIdx].copy(
                level = charLevel,
                constellation = constellation
            )

            val weap = weaps[weapIdx].copy(
                level = weapLevel,
                refinement = refinement
            )

            // 确定攻击类型和天赋等级
            val (talentLevel, attackMultiplier) = when (attackType) {
                0 -> Pair(normalAttack, 1.0) // 普攻
                1 -> Pair(normalAttack, 1.5)  // 重击(示例倍率)
                2 -> Pair(normalAttack, 2.0) // 下落攻击
                3 -> Pair(skill, 1.0)        // 元素战技
                4 -> Pair(burst, 1.0)        // 元素爆发
                else -> Pair(10, 1.0)
            }

            // 转换反应类型
            val reactionType = when (reaction) {
                0 -> Reaction.NONE
                1 -> Reaction.VAPORIZE
                2 -> Reaction.VAPORIZE
                3 -> Reaction.MELT
                4 -> Reaction.MELT
                5 -> Reaction.OVERLOADED
                6 -> Reaction.SUPERCONDUCT
                7 -> Reaction.ELECTROCHARGED
                8 -> Reaction.BLOOM
                9 -> Reaction.HYPERBLOOM
                10 -> Reaction.BURGEON
                else -> Reaction.NONE
            }

            // 计算伤害(示例: 使用前5个圣遗物组合)
            arts.take(5).forEach { art ->
                // 修改计算调用部分
                val result = Calculator.calculateDamage(
                    character = char,
                    weapon = weap,
                    artifacts = listOf(art),
                    talentLevel = talentLevel,
                    attackType = when (attackType) {
                        0 -> AttackType.NORMAL
                        1 -> AttackType.CHARGED
                        2 -> AttackType.PLUNGING
                        3 -> AttackType.ELEMENTAL_SKILL
                        4 -> AttackType.ELEMENTAL_BURST
                        else -> AttackType.NORMAL
                    },
                    attackMultiplier = attackMultiplier,
                    enemy = Enemy(level = 90, resistance = 0.1),
                    reaction = reactionType
                )
                resultList.add(result)
            }
        }

        // 显示结果
        rvResults.adapter = DamageResultAdapter(resultList)
    }
}