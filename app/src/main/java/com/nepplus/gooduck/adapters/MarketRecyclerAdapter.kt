package com.nepplus.gooduck.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ItemHeaderBinding
import com.nepplus.gooduck.models.Category
import com.nepplus.gooduck.models.SmallCategory

class MarketRecyclerAdapter(
    val mContext: Context,
    val mList: List<Category>
) : RecyclerView.Adapter<MarketRecyclerAdapter.ItemViewHolder>() {

    lateinit var binding: ItemHeaderBinding

    lateinit var mSmallAdapter : MarketChildRecyclerAdapter
    var mSmallList = ArrayList<SmallCategory>()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Category) {
            binding.title.text = item.name

            mSmallList.clear()
//            setPicture(item)
            mSmallList.addAll(item.smallCategories)

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_header,
            parent,
            false
        )
        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mList[position])
        mSmallAdapter = MarketChildRecyclerAdapter(mContext, mSmallList)
        binding.smallRecyclerView.adapter = mSmallAdapter
        binding.smallRecyclerView.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

//    fun setPicture(item: Category){
//        when( item.id){
//            1 -> {
//                item.smallCategories[0].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[1].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[2].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[3].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[4].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//            }
//            2 -> {
//                item.smallCategories[0].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[1].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[2].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[3].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[4].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//            }
//            3 -> {
//                item.smallCategories[0].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[1].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[2].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[3].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//                item.smallCategories[4].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
//            }
//        }
//    }



}