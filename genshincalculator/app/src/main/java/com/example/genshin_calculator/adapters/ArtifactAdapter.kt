package com.example.genshin_calculator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.genshin_calculator.R
import com.example.genshin_calculator.models.Artifact

/**
 * RecyclerView 适配器：显示圣遗物列表
 * @param artifacts 圣遗物数据列表
 * @param onItemClick 点击事件回调
 */
class ArtifactAdapter(
    private val artifacts: List<Artifact>,
    private val onItemClick: (Artifact) -> Unit
) : RecyclerView.Adapter<ArtifactAdapter.ArtifactViewHolder>() {

    // ViewHolder 用来缓存控件引用
    class ArtifactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textArtifactName: TextView = itemView.findViewById(R.id.textArtifactName)
        val textMainStat: TextView = itemView.findViewById(R.id.textMainStat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtifactViewHolder {
        // 加载item_artifact.xml布局
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artifact, parent, false)
        return ArtifactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtifactViewHolder, position: Int) {
        val artifact = artifacts[position]
        holder.textArtifactName.text = artifact.name
        holder.textMainStat.text = "主词条: ${artifact.mainStat}"

        // 点击事件
        holder.itemView.setOnClickListener {
            onItemClick(artifact)
        }
    }

    override fun getItemCount(): Int = artifacts.size
}
