package com.example.genshin_calculator.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.genshin_calculator.R
import com.example.genshin_calculator.adapters.DamageResultAdapter
import com.example.genshin_calculator.models.*
import com.example.genshin_calculator.utils.Calculator
import com.example.genshin_calculator.utils.DataLoader
import com.example.genshin_calculator.modifiers.*

/**
 * 结果页：根据选择，在同一视图显示伤害结果列表
 */
class ResultActivity : AppCompatActivity() {

    private lateinit var rvResults: RecyclerView
    private val resultList = mutableListOf<DamageResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        rvResults = findViewById(R.id.rv_results)
        rvResults.layoutManager = LinearLayoutManager(this)

        // 获取传递的索引
        val chIdx = intent.getIntExtra("char_index", -1)
        val wpIdx = intent.getIntExtra("weap_index", -1)

        val chars = DataLoader.loadJsonData<Character>(this, "characters.json")
        val weaps = DataLoader.loadJsonData<Weapon>(this, "weapons.json")
        val arts = DataLoader.loadJsonData<Artifact>(this, "artifacts.json")

        if (chIdx >= 0 && wpIdx >= 0) {
            val char = chars[chIdx]
            val weap = weaps[wpIdx]
            // 模拟循环不同圣遗物组合
            arts.take(5).forEach { art ->
                val res = Calculator.calculateDamage(
                    character = char,
                    weapon = weap,
                    artifact = art,
                    buffs = emptyList(),
                    teammateBuffs = emptyList(),
                    enemy = Enemy(level = 90, resistance = 0.1),
                    reaction = Reaction.VAPORIZE
                )
                resultList.add(res)
            }
        }

        rvResults.adapter = DamageResultAdapter(resultList)
    }
}
