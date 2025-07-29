package com.example.genshin_calculator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.genshin_calculator.R
import com.example.genshin_calculator.models.DamageResult

/**
 * RecyclerView 适配器，用于展示伤害计算结果
 */
class DamageResultAdapter(private val list: List<DamageResult>) :
    RecyclerView.Adapter<DamageResultAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvNorm: TextView = view.findViewById(R.id.tv_normal)
        val tvCrit: TextView = view.findViewById(R.id.tv_crit)
        val tvAvg: TextView = view.findViewById(R.id.tv_avg)
        val tvDetail: TextView = view.findViewById(R.id.tv_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_damage_result, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = list[position]
        holder.tvNorm.text = "非暴击: %.1f".format(r.baseAttack)
        holder.tvCrit.text = "暴击伤害: %.1f".format(r.critDamage)
        holder.tvAvg.text = "期望: %.1f".format(r.averageDamage)
        //holder.tvDetail.text = "乘区详情: ${r.detailBreakdown}"
    }
}
