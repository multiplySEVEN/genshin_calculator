package com.example.genshin_calculator.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.genshin_calculator.R
import com.example.genshin_calculator.adapters.ArtifactAdapter
import com.example.genshin_calculator.models.Artifact
import com.example.genshin_calculator.utils.DataLoader

/**
 * 展示圣遗物列表，选中返回结果
 */
class ArtifactActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var artifactList: List<Artifact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artifact)

        // 注意：这里ID要和布局文件中的RecyclerView控件ID一致
        recyclerView = findViewById(R.id.recyclerViewArtifacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 从 assets/artifacts.json 加载数据，返回List<Artifact>
        //artifactList = DataLoader.loadArtifacts(this, "artifacts.json")

        // 适配器，点击事件传回所选Artifact对象
        val adapter = ArtifactAdapter(artifactList) { selectedArtifact ->
            val resultIntent = Intent().apply {
                // 这里可以传整个对象，需Artifact实现Serializable或Parcelable
                putExtra("selectedArtifactName", selectedArtifact.setName)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        recyclerView.adapter = adapter
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ArtifactActivity::class.java)
            context.startActivity(intent)
        }
    }
}
