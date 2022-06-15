package com.nepplus.gooduck.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nepplus.gooduck.R
import com.nepplus.gooduck.adapters.ReviewRecyclerAdapter
import com.nepplus.gooduck.databinding.ActivityCardAddBinding.inflate
import com.nepplus.gooduck.databinding.FragmentReviewBinding
import com.nepplus.gooduck.models.BasicResponse
import com.nepplus.gooduck.models.Review
import com.nepplus.gooduck.ui.review.ReviewAddActivity
import com.nepplus.gooduck.ui.review.ReviewDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewFragment  : BaseFragment(){

    lateinit var binding : FragmentReviewBinding

    lateinit var mReviewAdapter : ReviewRecyclerAdapter
    var mReviewList = ArrayList<Review>()

    var reviewSize = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllReviewList()

        setupEvents()

        setValues()
    }

    override fun onResume() {
        super.onResume()
//        getAllReviewList()

    }

    override fun setupEvents() {

        binding.faBtn.setOnClickListener {
            val myIntent = Intent(mContext , ReviewAddActivity::class.java)
            startActivity(myIntent)
        }

        binding.addReviewBtn.setOnClickListener {
            val myIntent = Intent(mContext , ReviewAddActivity::class.java)
            startActivity(myIntent)
        }


        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_dialog, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView)


        binding.sortBtn.setOnClickListener {
            bottomSheetDialog.show()

        }

        val newestSort = bottomSheetView.findViewById<LinearLayout>(R.id.newestSort)
        val rankSort = bottomSheetView.findViewById<LinearLayout>(R.id.rankSort)
        val ratingTopSort = bottomSheetView.findViewById<LinearLayout>(R.id.ratingTopSort)
        val ratingBottomSort = bottomSheetView.findViewById<LinearLayout>(R.id.ratingBottomSort)
        val isNewest = bottomSheetView.findViewById<ImageView>(R.id.isNewest)
        val isRank = bottomSheetView.findViewById<ImageView>(R.id.isRank)
        val isRatingTop = bottomSheetView.findViewById<ImageView>(R.id.isRatingTop)
        val isRatingBottom = bottomSheetView.findViewById<ImageView>(R.id.isRatingBottom)


        newestSort.setOnClickListener {
            getAllReviewList()
            isNewest.visibility = View.VISIBLE
            isRank.visibility = View.GONE
            isRatingTop.visibility = View.GONE
            isRatingBottom.visibility = View.GONE
            binding.sortBtn.text = "등록순"
            bottomSheetDialog.dismiss()

        }
        rankSort.setOnClickListener {
            getSortReviewRank()
            isNewest.visibility = View.GONE
            isRank.visibility = View.VISIBLE
            isRatingTop.visibility = View.GONE
            isRatingBottom.visibility = View.GONE
            binding.sortBtn.text = "랭킹순"
            bottomSheetDialog.dismiss()
        }

        ratingTopSort.setOnClickListener {
            mReviewList.sortByDescending { list -> list.score}
            mReviewAdapter.notifyDataSetChanged()
            isNewest.visibility = View.GONE
            isRank.visibility = View.GONE
            isRatingTop.visibility = View.VISIBLE
            isRatingBottom.visibility = View.GONE
            binding.sortBtn.text = "별점 높은순"
            bottomSheetDialog.dismiss()
        }

        ratingBottomSort.setOnClickListener {
            mReviewList.sortBy { list -> list.score}
            mReviewAdapter.notifyDataSetChanged()
            isNewest.visibility = View.GONE
            isRank.visibility = View.GONE
            isRatingTop.visibility = View.GONE
            isRatingBottom.visibility = View.VISIBLE
            binding.sortBtn.text = "별점 낮은순"
            bottomSheetDialog.dismiss()
        }

    }

    override fun setValues() {
        initAdapters()
    }

    fun getAllReviewList(){
        apiList.getRequestAllReview().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    reviewSize = br.data.reviews.size
//                    Log.d("리뷰개수", reviewSize.toString())

                    binding.reviewCnt.text = "${reviewSize}개의 리뷰"
                    if(reviewSize > 0 ){
                        binding.emptyLayout.visibility = View.GONE
                        binding.reviewRecyclerView.visibility = View.VISIBLE
                        mReviewList.clear()
                        mReviewList.addAll(br.data.reviews)
                        initAdapters()
                    }
                    else {
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.reviewRecyclerView.visibility = View.GONE
                    }

                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {Log.d("호출 실패", t.toString())}
        })
    }
    fun getSortReviewRank(){
        apiList.getRequestSortRank().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    reviewSize = br.data.reviews.size
//                    Log.d("리뷰개수", reviewSize.toString())

                    binding.reviewCnt.text = "${reviewSize}개의 리뷰"
                    if(reviewSize > 0 ){
                        binding.emptyLayout.visibility = View.GONE
                        binding.reviewRecyclerView.visibility = View.VISIBLE
                        mReviewList.clear()
                        mReviewList.addAll(br.data.reviews)
                        mReviewAdapter.notifyDataSetChanged()
//                        initAdapters()
                    }
                    else {
                        binding.emptyLayout.visibility = View.VISIBLE
                        binding.reviewRecyclerView.visibility = View.GONE
                    }

                }
            }
            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {Log.d("호출 실패", t.toString())}
        })
    }

    fun initAdapters(){
        mReviewAdapter = ReviewRecyclerAdapter(mContext, mReviewList)
        binding.reviewRecyclerView.adapter = mReviewAdapter
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(mContext)

        mReviewAdapter.notifyDataSetChanged()

        mReviewAdapter.setItemClickListener(object : ReviewRecyclerAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val myIntent = Intent(mContext, ReviewDetailActivity::class.java)
                myIntent.putExtra("review", mReviewList[position])
                startActivity(myIntent)
//                Toast.makeText(mContext, "${position}번쨰 선택", Toast.LENGTH_SHORT).show()
            }
        })
    }
}