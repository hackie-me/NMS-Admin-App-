package com.example.nmsadminapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.nmsadminapp.R
import com.example.nmsadminapp.models.FaqModel

class FaqListAdapter(
    context: Context,
    private val list: Array<FaqModel>
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int = list.size

    override fun getChildrenCount(groupPosition: Int): Int = 1

    override fun getGroup(groupPosition: Int): Any {
        return list[groupPosition].question
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return list[groupPosition].answer
    }

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        if (view == null) {
            val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.rc_list_item_faq_question, null)
        }
        val questionTextView = view?.findViewById<TextView>(R.id.tv_question)
        questionTextView?.text = list[groupPosition].question
        return view!!
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        if (view == null) {
            val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.rc_list_item_faq_answer, null)
        }
        val answerTextView = view?.findViewById<TextView>(R.id.tv_answer)
        answerTextView?.text = list[groupPosition].answer
        return view!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = false

}