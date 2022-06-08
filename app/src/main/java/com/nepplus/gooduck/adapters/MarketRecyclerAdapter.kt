package com.nepplus.gooduck.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.nepplus.gooduck.R
import com.nepplus.gooduck.databinding.ItemGroupBinding
import com.nepplus.gooduck.databinding.ItemHeaderBinding
import com.nepplus.gooduck.databinding.ItemSliderBinding
import com.nepplus.gooduck.models.Banner
import com.nepplus.gooduck.models.Category
import com.nepplus.gooduck.models.SmallCategory

class MarketRecyclerAdapter(
    val mContext: Context,
    val mBannerList : List<Banner>,
    val mCategoryList : List<Category>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_HEADER = 0
    val TYPE_ITEM = 1

    lateinit var frag : Fragment

    lateinit var mSliderAdapter : ImageSliderAdapter


    lateinit var mSmallAdapter : MarketChildRecyclerAdapter
    var mSmallList = ArrayList<SmallCategory>()

    inner class ItemViewHolder(val binding : ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.title.text = item.name

            mSmallList.clear()
            setPicture(item)
//            Log.d("이미지 있니?", item.smallCategories[0].imageUrl)
            mSmallList.addAll(item.smallCategories)

            mSmallAdapter = MarketChildRecyclerAdapter(mContext, mSmallList)
            binding.smallRecyclerView.adapter = mSmallAdapter
            binding.smallRecyclerView.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)

        }

    }

    inner class HeaderViewHolder(val binding : ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(){

            mSliderAdapter = ImageSliderAdapter(mContext, mBannerList)
            binding.bannerVeiwPager.adapter = mSliderAdapter
            binding.bannerVeiwPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            binding.dotsIndicator.setViewPager2(binding.bannerVeiwPager)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderViewHolder(ItemHeaderBinding.inflate(LayoutInflater.from(mContext), parent, false))
            }
            else -> {
                ItemViewHolder(ItemGroupBinding.inflate(LayoutInflater.from(mContext), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind()
            }
            is ItemViewHolder -> {
                holder.bind(mCategoryList[position-1])
            }
        }
    }

    override fun getItemCount(): Int {
        return mCategoryList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 ->  TYPE_HEADER
            else -> TYPE_ITEM
        }
    }


    fun setPicture(item: Category){
        when( item.id){
            1 -> {
                item.smallCategories[0].imageUrl ="http://res.heraldm.com/phpwas/restmb_idxmake.php?idx=507&simg=/content/image/2017/04/24/20170424000318_0.jpg"
                item.smallCategories[1].imageUrl ="https://src.hidoc.co.kr/image/lib/2021/3/19/1616132123214_0.jpg"
                item.smallCategories[2].imageUrl ="https://health.chosun.com/site/data/img_dir/2022/03/16/2022031602184_0.jpg"
                item.smallCategories[3].imageUrl ="https://t1.daumcdn.net/cfile/blog/251AE644580D9E1B29"
                item.smallCategories[4].imageUrl ="https://post-phinf.pstatic.net/MjAxOTA0MjlfMTUz/MDAxNTU2NTAzNzY5MjA3.RihgWEzT78pN22Qyq1UxJQD3HWQxtn7YoJS-xF7h0jUg.TszYdTGEFW5RjNbtEowo0XkXcd02G5SqA8opRPjl6W4g.JPEG/%EC%9D%B4%EB%AF%B8%EC%A7%80_2.jpg?type=w1200"
            }
            2 -> {
                item.smallCategories[0].imageUrl ="https://imgc.1300k.com/aaaaaib/goods/215024/89/215024893926.jpg?3"
                item.smallCategories[1].imageUrl ="http://media.istockphoto.com/vectors/underwear-silhouette-isolated-vector-set-vector-id543657416?k=6&m=543657416&s=612x612&w=0&h=OP7JXU0wTxWelfQb6nJsaWQIoMWxUnSywQeJ2RI-ttc="
                item.smallCategories[2].imageUrl ="https://img.ssfshop.com/display/html/DSP_CTGRY/20190524/HOMME_190513_Summer01.jpg"
                item.smallCategories[3].imageUrl ="https://thumbnews.nateimg.co.kr/view610///news.nateimg.co.kr/orgImg/mh/2021/10/29/2021102901031430119001_b.jpg"
                item.smallCategories[4].imageUrl ="https://cdn.econovill.com/news/orgPhoto/202105/534311_443950_03.png"
            }
            3 -> {
                item.smallCategories[0].imageUrl ="https://image.edaily.co.kr/images/Photo/files/NP/S/2020/01/PS20011900305.jpg"
                item.smallCategories[1].imageUrl ="https://img-cf.kurly.com/shop/data/goodsview/20191120/gv10000068844_1.jpg"
                item.smallCategories[2].imageUrl ="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZ0GM0uMvJVOfQCFIa_BzEQkP-9jEV8udXyg&usqp=CAU"
                item.smallCategories[3].imageUrl ="http://img.segye.com/content/image/2021/07/06/20210706506994.jpg"
                item.smallCategories[4].imageUrl ="http://image.babosarang.co.kr/product/detail/TIL/1905021412160108/_600.jpg"
            }
        }
    }




}