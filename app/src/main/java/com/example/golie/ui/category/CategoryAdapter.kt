package com.example.golie.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.golie.R
import com.example.golie.data.dataClasses.Goal

class CategoryAdapter(context: Context, layoutResource: Int, textViewResourceId: Int, allGoals: MutableList<Goal>): ArrayAdapter<Goal>(context, layoutResource, textViewResourceId, allGoals) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val goal = getItem(position)!!
        val view: View
        val title: TextView
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.goal_item, parent, false)
            title = view.findViewById<TextView>(R.id.goal_item_title)
            title.text = goal.title
            if(goal.colorId != -1){
                view.setBackgroundColor(context.getColor(goal.colorId))
            }
            return view
        }
        else{
            title = convertView.findViewById<TextView>(R.id.goal_item_title)
            title.text = goal.title
            if(goal.colorId != -1){
                title.setBackgroundColor(context.getColor(goal.colorId))
            }

            return convertView
        }
    }

    override fun remove(`object`: Goal?) {
        super.remove(`object`)
    }
}