package com.nepplus.gooduck.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.nepplus.gooduck.BaseActivity
import com.nepplus.gooduck.R
import com.nepplus.gooduck.R.*
import com.nepplus.gooduck.adapters.MainViewPagerAdapter
import com.nepplus.gooduck.adapters.SideNaviListAdapter
import com.nepplus.gooduck.databinding.ActivityMainBinding
import com.nepplus.gooduck.dialog.CustomAlertDialog
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Category
import com.nepplus.gooduck.ui.market.MarketDetailActivity
import com.nepplus.gooduck.ui.setting.MyReviewListActivity
import com.nepplus.gooduck.ui.setting.PaymentListActivity
import com.nepplus.gooduck.ui.setting.PointListActivity
import com.nepplus.gooduck.ui.signup.LoginActivity
import com.nepplus.gooduck.utils.ContextUtil
import com.nepplus.gooduck.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity(){

    lateinit var binding : ActivityMainBinding

    lateinit var mPagerAdapter : MainViewPagerAdapter

    var mCategoryList = ArrayList<Category>()

    var titleList = ArrayList<String>()
    val dataList = LinkedHashMap<String, List<String>>()


    var lastClickedPosition = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout.activity_main)
        getCategoryList()
        setupEvents()
        setValues()

    }

    override fun setupEvents() {
//        네비 헤더 메뉴 클릭 리스너
        binding.sideNaviHeader.Txt.text = "안녕하세요. ${GlobalData.loginUser!!.nickname}님, 환영합니다!"

        binding.sideNaviHeader.logoutBtn.setOnClickListener {
            //        로그아웃
            val alert = CustomAlertDialog(mContext)
            alert.myDialog(object : CustomAlertDialog.ButtonClickListener{
                override fun positiveBtnClicked() {
                    alert.dialog.dismiss()
                    ContextUtil.clear(mContext)
                    GlobalData.loginUser = null
                    val myIntent = Intent(mContext, LoginActivity::class.java)
                    myIntent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(myIntent)
                }
                override fun negativeBtnClicked() {
                }
            })

            alert.binding.titleTxt.text = "로그 아웃"
            alert.binding.bodyTxt.text = "정말 로그아웃 하시겠습니까 ?"
            alert.binding.contentEdt1.visibility = View.GONE
            alert.binding.positiveBtn.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            alert.binding.positiveBtn.setBackgroundResource(R.drawable.r5_red_stroke_1dp)
        }

        binding.sideNaviHeader.myPageBtn.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            binding.mainViewPager.currentItem = 2
        }

        binding.sideNaviHeader.manageReviewLayout.setOnClickListener{
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val myIntent = Intent(mContext, MyReviewListActivity::class.java)
            startActivity(myIntent)
        }

        binding.sideNaviHeader.manageBuyLayout.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val myIntent = Intent(mContext, PaymentListActivity::class.java)
            startActivity(myIntent)
        }

        binding.sideNaviHeader.managePointLayout.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val myIntent = Intent(mContext, PointListActivity::class.java)
            startActivity(myIntent)
        }


    }

    override fun setValues() {

        mPagerAdapter = MainViewPagerAdapter(this)
        binding.mainViewPager.adapter = mPagerAdapter

        binding.mainViewPager.currentItem = 1
        binding.mainViewPager.isUserInputEnabled = false
        binding.bottomNav.selectedItemId = id.market



        binding.mainViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNav.menu.getItem(position).isChecked = true
                }
            })

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                id.review -> binding.mainViewPager.currentItem = 0
                id.market -> binding.mainViewPager.currentItem = 1
                id.settings -> binding.mainViewPager.currentItem = 2
            }
            return@setOnItemSelectedListener true
        }

//      사이드 네비게이션 바를 위한 Toolbar 추가
        setSupportActionBar(binding.mainToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(drawable.three_line)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.mainToolbar, string.navigation_drawer_open, string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var myIntent = Intent()
        when(item.itemId){
            id.topNavCart -> {
                myIntent = Intent(mContext, CartActivity::class.java)
                startActivity(myIntent)
            }
            id.searchBtn -> {
                myIntent = Intent(mContext, SearchActivity::class.java)
                startActivity(myIntent)
            }
            else ->{
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }

    fun getCategoryList(){
        apiList.getRequestAllCategory().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mCategoryList.addAll(br.data.categories)
                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })

        apiList.getRequestAllCategory().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mCategoryList.addAll(br.data.categories)
                    val name = br.data.categories.map { it.name }
                    titleList.addAll(name)
                    for( i in 0 until titleList.size){
                        val child = ArrayList<String>()
                        child.addAll(mCategoryList[i].smallCategories.map { it.name })
                        dataList[name[i]] = child
                    }

//                    Log.d("titleList", titleList.toString())
//
//                    Log.d("dataList", dataList.toString())

                    titleList = ArrayList(dataList.keys)
                    val expandAdapter = SideNaviListAdapter(mContext, titleList, dataList)
                    binding.expandListView.setAdapter(expandAdapter)

                    binding.expandListView.setOnChildClickListener { parent, view, groupPosition, childPosition, id ->
//                        Toast.makeText(mContext, groupPosition.toString(), Toast.LENGTH_SHORT).show()
                        val myIntent = Intent(mContext, MarketDetailActivity::class.java)
                        myIntent.putExtra("sCategories", mCategoryList[groupPosition].smallCategories[childPosition])
                        mContext.startActivity(myIntent)
                        false
                    }


                    binding.expandListView.setOnGroupClickListener { parent, view, groupPosition, id ->
                        setListViewHeight(parent, groupPosition)
                        // 선택 한 groupPosition의 펼침/닫힘 상태 체크
                        val isExpand: Boolean = !parent.isGroupExpanded(groupPosition)

                        // 이 전에 열려있던 group 닫기
                        parent.collapseGroup(lastClickedPosition)

                        if (isExpand) {
                            parent.expandGroup(groupPosition)
                        }
                        lastClickedPosition = groupPosition

                        true
                    }

                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }
        })
    }

    private fun setListViewHeight(
        listView: ExpandableListView,
        group: Int
    ) {
        val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if (listView.isGroupExpanded(i) && i != group
                || !listView.isGroupExpanded(i) && i == group
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params = listView.layoutParams
        var height = (totalHeight
                + listView.dividerHeight * (listAdapter.groupCount - 1))
        if (height < 10) height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

}


