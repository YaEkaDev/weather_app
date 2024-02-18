package com.example.weatherapi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.MainViewModel
import com.example.weatherapi.data.Hour
import com.example.weatherapi.adapters.RVAdapter
import com.example.weatherapi.databinding.FragmentHoursBinding


class HoursFragment : Fragment() {

    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: RVAdapter
    private val viewmodel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()

        viewmodel.forecastdayData.observe(viewLifecycleOwner){

            val allHours = it.hour
            val hours = mutableListOf<Hour>()
            if (allHours != null) {
                for ((index,  i) in allHours.withIndex()){
                    if (index%2==0) hours.add(i)
                }
            }
                adapter.hoursList = hours

        }
    }


    private fun initRcView() = with(binding) {
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        rcItems.layoutManager = layoutManager
        adapter = RVAdapter(requireContext(), this@HoursFragment.tag.toString(), null)
        rcItems.adapter = adapter

        rcItems.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            var lastX = 0
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> lastX = e.x.toInt()
                    MotionEvent.ACTION_MOVE -> {
                        val isScrollingRight: Boolean = e.x < lastX
                        if (isScrollingRight && (rcItems.getLayoutManager() as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == (rcItems.getAdapter()
                                ?.getItemCount() ?: lastX) - 1 || !isScrollingRight && (rcItems.getLayoutManager() as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
                        ) {
                            //viewPager.setUserInputEnabled(true)
                            viewmodel.isSetUserInputEnabled.value=true
                        } else {
                            //viewPager.setUserInputEnabled(false)
                            viewmodel.isSetUserInputEnabled.value=false
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        lastX = 0
                        //viewPager.setUserInputEnabled(true)
                        viewmodel.isSetUserInputEnabled.value=true
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HoursFragment()
    }
}
