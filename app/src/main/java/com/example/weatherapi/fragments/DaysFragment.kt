package com.example.weatherapi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.MainViewModel
import com.example.weatherapi.data.Forecastday
import com.example.weatherapi.adapters.RVAdapter
import com.example.weatherapi.databinding.FragmentDaysBinding

class DaysFragment : Fragment(), RVAdapter.Listener {

    private lateinit var binding: FragmentDaysBinding
    private lateinit var adapter: RVAdapter
    private val viewmodel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()

        viewmodel.weatherCurrentData.observe(viewLifecycleOwner){
            val forecastdayList = it.forecast?.forecastday
            if (forecastdayList != null) {
                adapter.forecastdayList = forecastdayList
            }
        }
    }

    private fun initRcView() = with(binding) {

        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        rcItems.layoutManager = layoutManager
        adapter = RVAdapter(requireContext(), this@DaysFragment.tag.toString(), this@DaysFragment)
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
                            viewmodel.isSetUserInputEnabled.value=true
                        } else {
                            viewmodel.isSetUserInputEnabled.value=false
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        lastX = 0
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
            DaysFragment()
    }

    override fun onForecastdayClick(forecastday: Forecastday) {
        viewmodel.forecastdayData.value = forecastday
    }
}
