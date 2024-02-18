package com.example.weatherapi.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.weatherapi.DialogManager
import com.example.weatherapi.MainViewModel
import com.example.weatherapi.R
import com.example.weatherapi.adapters.VPAdapter
import com.example.weatherapi.databinding.FragmentMainBinding
import com.example.weatherapi.utils.tempToInt
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var fLocationClient: FusedLocationProviderClient
    private lateinit var plauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val viewmodel: MainViewModel by activityViewModels()

    private val fList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val tList = listOf(
        "HOURS",
        "DAYS"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()
        updateCurrentCard()
        initMenu()
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun initMenu() {
        binding.toolbar.apply {

            inflateMenu(R.menu.bottom_nav_menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search_menu -> {
                        DialogManager.searchByName(
                            requireContext(),
                            object : DialogManager.Listener {
                                override fun onClick(name: String?) {
                                    name?.let { viewmodel.loadWeatherData(it) }
                                }
                            })
                    }

                    R.id.update_menu -> {
                        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
                        checkLocation()
                    }
                }
                true
            }
        }
    }

    private fun init() {

        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val vpAdapter = VPAdapter(activity as FragmentActivity, fList)
        binding.vp.adapter = vpAdapter
        TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
            tab.text = tList[position]
        }.attach()

        viewmodel.isSetUserInputEnabled.observe(viewLifecycleOwner) {
            binding.vp.setUserInputEnabled(it)
        }

        viewmodel.isLoading.observe(viewLifecycleOwner) {
            if (it) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        }

        viewmodel.forecastdayData.observe(viewLifecycleOwner) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
        }
    }

    private fun getLocation() {
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                viewmodel.loadWeatherData("${it.result.latitude},${it.result.longitude}")
            }
    }

    private fun checkLocation() {
        if (isLocatedEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(requireContext(), object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun isLocatedEnabled(): Boolean {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun updateCurrentCard() = with(binding) {
        viewmodel.weatherCurrentData.observe(viewLifecycleOwner) {

            viewmodel.forecastdayData.value = it.forecast?.forecastday?.get(0)

            tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            tvCity.text = it.location?.name
            toolbar.title = "last updated: ${it.current?.last_updated}"
            tvCurrentTemp.text = String.format(
                getString(R.string.temp_template),
                it.current?.let { it1 ->
                    tempToInt(it1.temp_c)
                }
            )
            tvCondition.text = it.current?.condition?.text
            val firstDay = it.forecast?.forecastday?.get(0)
            val maxTemp = tempToInt(firstDay?.day?.maxtemp_c)
            val minTemp = tempToInt(firstDay?.day?.mintemp_c)
            tvMaxMinTemp.text = String.format(
                getString(R.string.max_min_template),
                maxTemp,
                minTemp
            )

            tvTimeSunrise.text = firstDay?.astro?.sunrise
            tvTimeSunset.text = firstDay?.astro?.sunset
            tvTimeMoonrise.text = firstDay?.astro?.moonrise
            tvTimeMoonset.text = firstDay?.astro?.moonset

            tvHumidity.text = String.format(
                getString(R.string.humidity_template),
                it.current?.humidity)
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            plauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun permissionListener() {
        plauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }


}