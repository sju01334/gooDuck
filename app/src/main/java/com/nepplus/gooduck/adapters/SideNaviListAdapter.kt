package com.nepplus.gooduck.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.nepplus.gooduck.R
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Category
import com.nepplus.gooduck.models.SmallCategory
import retrofit2.Callback
import kotlin.collections.ArrayList

class SideNaviListAdapter(
    val mContext: Context,
    val titleList: List<String>,
    val dataList: LinkedHashMap<String, List<String>>
) : BaseExpandableListAdapter(){

    override fun getGroupCount(): Int {
        return titleList.size
    }

    override fun getChildrenCount(position: Int): Int {
        return dataList[titleList[position]]?.size ?: 0
    }

    override fun getGroup(position: Int): Any {
        return titleList[position]
    }

    override fun getChild(parentPosition: Int, childPosition: Int): Any {
        return dataList[titleList[parentPosition]]!![childPosition]
    }

    override fun getGroupId(position: Int): Long {
        return position.toLong()
    }

    override fun getChildId(parentPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(position: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var tempRow = convertView
        if(tempRow == null){
            tempRow = LayoutInflater.from(mContext).inflate(R.layout.item_side_navi_parent, null)
        }
        var row = tempRow!!

        val bigTxt = tempRow.findViewById<TextView>(R.id.bigTxt)
        val plusminusBtn = tempRow.findViewById<ImageView>(R.id.plusminusBtn)

        if (isExpanded)
            plusminusBtn.setImageResource(R.drawable.plus);
        else
            plusminusBtn.setImageResource(R.drawable.minus);

        bigTxt.text = titleList[position]

        return row
    }

    override fun getChildView(parentPosition: Int, childPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var tempRow = convertView
        if(tempRow == null){
            tempRow = LayoutInflater.from(mContext).inflate(R.layout.item_side_navi_child, null)
        }
        var row = tempRow!!

        val smallTxt = tempRow.findViewById<TextView>(R.id.smallTxt)

        smallTxt.text = getChild(parentPosition, childPosition).toString()

        return row
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

}
