package com.example.genshin_calculator.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.genshin_calculator.R
import com.example.genshin_calculator.adapters.CharacterAdapter
import com.example.genshin_calculator.adapters.WeaponAdapter
import com.example.genshin_calculator.models.Character
import com.example.genshin_calculator.models.Weapon
import com.example.genshin_calculator.utils.DataLoader

/**
 * 主界面：角色 & 武器 选择，以及跳转至结果页
 */
class MainActivity : AppCompatActivity() {

    private lateinit var spinnerChar: Spinner
    private lateinit var spinnerWeap: Spinner
    private lateinit var btnNext: Button

    private lateinit var characters: List<Character>
    private lateinit var weapons: List<Weapon>
    private var selectedChar: Character? = null
    private var selectedWeap: Weapon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerChar = findViewById(R.id.spinner_character)
        spinnerWeap = findViewById(R.id.spinner_weapon)
        btnNext = findViewById(R.id.btn_next)

        // 加载本地 JSON 数据
        characters = DataLoader.loadJsonData(this, "characters.json")
        weapons = DataLoader.loadJsonData(this, "weapons.json")

        // 初始化角色下拉菜单
        val charAdapter = CharacterAdapter(this, characters)
        spinnerChar.adapter = charAdapter
        spinnerChar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p: AdapterView<*>, view: android.view.View, pos: Int, id: Long
            ) {
                selectedChar = characters[pos]
                // 筛选武器列表与角色武器类型匹配
                val filtered = weapons.filter { it.subStatType.contains(selectedChar!!.element) || true }
                val wAdapter = WeaponAdapter(this@MainActivity, filtered)
                spinnerWeap.adapter = wAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 点击按钮跳转
        btnNext.setOnClickListener {
            if (selectedChar == null || selectedWeap == null) {
                Toast.makeText(this, "请先选择角色和武器", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val i = Intent(this, ResultActivity::class.java)
            i.putExtra("char_index", characters.indexOf(selectedChar!!))
            i.putExtra("weap_index", weapons.indexOf(selectedWeap!!))
            startActivity(i)
        }
    }
}
