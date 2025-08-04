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
 * 主界面：角色、武器、等级、天赋等选择
 */
class MainActivity : AppCompatActivity() {

    private lateinit var spinnerChar: Spinner
    private lateinit var spinnerWeap: Spinner
    private lateinit var btnCalculate: Button
    private lateinit var btnArtifacts: Button
    private lateinit var btnTeammates: Button

    private lateinit var seekBarCharLevel: SeekBar
    private lateinit var tvCharLevel: TextView
    private lateinit var seekBarWeapLevel: SeekBar
    private lateinit var tvWeapLevel: TextView

    private lateinit var spinnerConstellation: Spinner
    private lateinit var spinnerRefinement: Spinner
    private lateinit var spinnerNormalAttack: Spinner
    private lateinit var spinnerSkill: Spinner
    private lateinit var spinnerBurst: Spinner
    private lateinit var spinnerAttackType: Spinner
    private lateinit var spinnerReaction: Spinner

    private lateinit var characters: List<Character>
    private lateinit var weapons: List<Weapon>
    private var selectedChar: Character? = null
    private var selectedWeap: Weapon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化视图
        initViews()

        // 加载数据
        loadData()

        // 设置监听器
        setupListeners()
    }

    private fun initViews() {
        spinnerChar = findViewById(R.id.spinner_character)
        spinnerWeap = findViewById(R.id.spinner_weapon)
        btnCalculate = findViewById(R.id.btn_calculate)
        btnArtifacts = findViewById(R.id.btn_artifacts)
        btnTeammates = findViewById(R.id.btn_teammates)

        seekBarCharLevel = findViewById(R.id.seekBar_char_level)
        tvCharLevel = findViewById(R.id.tv_char_level)
        seekBarWeapLevel = findViewById(R.id.seekBar_weap_level)
        tvWeapLevel = findViewById(R.id.tv_weap_level)

        spinnerConstellation = findViewById(R.id.spinner_constellation)
        spinnerRefinement = findViewById(R.id.spinner_refinement)
        spinnerNormalAttack = findViewById(R.id.spinner_normal_attack)
        spinnerSkill = findViewById(R.id.spinner_skill)
        spinnerBurst = findViewById(R.id.spinner_burst)
        spinnerAttackType = findViewById(R.id.spinner_attack_type)
        spinnerReaction = findViewById(R.id.spinner_reaction)

        // 设置默认值
        spinnerNormalAttack.setSelection(9)  // 默认10级
        spinnerSkill.setSelection(9)
        spinnerBurst.setSelection(9)
    }

    private fun loadData() {
        // 加载角色和武器数据
        characters = DataLoader.loadJsonFromRaw(this, R.raw.characters)
        weapons = DataLoader.loadJsonFromRaw(this, R.raw.weapons)

        // 初始化角色下拉菜单
        val charAdapter = CharacterAdapter(this, characters)
        spinnerChar.adapter = charAdapter
    }

    private fun setupListeners() {
        // 角色选择监听
        spinnerChar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, pos: Int, id: Long) {
                selectedChar = characters[pos]
                updateWeaponList()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                spinnerWeap.adapter = WeaponAdapter(this@MainActivity, emptyList())
            }
        }

        // 武器选择监听
        spinnerWeap.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, pos: Int, id: Long) {
                selectedWeap = (spinnerWeap.adapter as WeaponAdapter).getItem(pos)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 角色等级滑动条
        seekBarCharLevel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val level = progress  // 1-14对应1-90级
                tvCharLevel.text = (calculateCharLevel(level)).toString() // 近似转换
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // 武器等级滑动条
        seekBarWeapLevel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val level = progress  // 1-14对应1-90级
                tvWeapLevel.text = (calculateWeapLevel(level)).toString() // 近似转换
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // 计算按钮点击
        btnCalculate.setOnClickListener {
            if (selectedChar == null || selectedWeap == null) {
                Toast.makeText(this, "请先选择角色和武器", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 准备数据并跳转到结果页
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("char_index", characters.indexOf(selectedChar!!))
                putExtra("weap_index", weapons.indexOf(selectedWeap!!))
                putExtra("char_level", calculateCharLevel(seekBarCharLevel.progress))
                putExtra("weap_level", calculateWeapLevel(seekBarWeapLevel.progress))
                putExtra("constellation", spinnerConstellation.selectedItemPosition)
                putExtra("refinement", spinnerRefinement.selectedItemPosition + 1) // 精炼1-5
                putExtra("normal_attack", spinnerNormalAttack.selectedItemPosition + 1)
                putExtra("skill", spinnerSkill.selectedItemPosition + 1)
                putExtra("burst", spinnerBurst.selectedItemPosition + 1)
                putExtra("attack_type", spinnerAttackType.selectedItemPosition)
                putExtra("reaction", spinnerReaction.selectedItemPosition)
            }
            startActivity(intent)
        }

        // 圣遗物设置按钮
        btnArtifacts.setOnClickListener {
            // 跳转到圣遗物设置页
            ArtifactActivity.launch(this)
        }

        // 队友增益按钮
        btnTeammates.setOnClickListener {
            // 跳转到队友增益设置页
            // TeammateActivity.launch(this)
            Toast.makeText(this, "队友增益功能开发中", Toast.LENGTH_SHORT).show()
        }
    }

    // 更新武器列表(根据角色武器类型筛选)
    private fun updateWeaponList() {
        selectedChar?.let { char ->
            val filteredWeapons = weapons.filter { it.weaponType == char.weaponType }
            val wAdapter = WeaponAdapter(this, filteredWeapons)
            spinnerWeap.adapter = wAdapter

            // 默认选中第一个武器
            if (filteredWeapons.isNotEmpty()) {
                spinnerWeap.setSelection(0)
            }
        }
    }

    // 计算角色等级(1-14对应1-90级)
    private fun calculateCharLevel(progress: Int): Int {
        return when (progress + 1) {
            1 -> 1
            2 -> 20
            3 -> 40
            4 -> 50
            5 -> 60
            6 -> 70
            7 -> 80
            8 -> 90
            else -> 90 // 简化处理
        }
    }

    // 计算武器等级(1-14对应1-90级)
    private fun calculateWeapLevel(progress: Int): Int {
        return when (progress + 1) {
            1 -> 1
            2 -> 20
            3 -> 40
            4 -> 50
            5 -> 60
            6 -> 70
            7 -> 80
            8 -> 90
            else -> 90 // 简化处理
        }
    }
}