package com.yosemiteyss.greentransit.app.stop

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yosemiteyss.greentransit.R
import com.yosemiteyss.greentransit.app.utils.viewBinding
import com.yosemiteyss.greentransit.databinding.FragmentStopRoutesBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by kevin on 17/5/2021
 */

@AndroidEntryPoint
class StopRoutesFragment : Fragment(R.layout.fragment_stop_routes) {

    private val binding: FragmentStopRoutesBinding by viewBinding(FragmentStopRoutesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}