package com.example.genshin_calculator.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.genshin_calculator.models.Character

/**
 * 角色 Spinner 的适配器
 */
class CharacterAdapter(context: Context, data: List<Character>) :
    ArrayAdapter<Character>(context, android.R.layout.simple_spinner_item, data) {
    init { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tv = super.getView(position, convertView, parent) as TextView
        tv.text = getItem(position)?.name ?: ""
        return tv
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tv = super.getDropDownView(position, convertView, parent) as TextView
        tv.text = getItem(position)?.name ?: ""
        return tv
    }
}
