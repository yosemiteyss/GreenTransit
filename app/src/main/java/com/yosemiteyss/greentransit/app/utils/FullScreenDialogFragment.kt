//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.app.utils

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.yosemiteyss.greentransit.R

open class FullScreenDialogFragment : DialogFragment {

    open var onBackPressed: (() -> Unit)? = null

    private var disableTransition: Boolean = false

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            setStyle(STYLE_NORMAL, R.style.ThemeOverlay_GreenTransit_Dialog_Fullscreen_DayNight)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                val handler = onBackPressed
                if (handler != null) {
                    handler()
                } else {
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onStart() {
        if (disableTransition) {
            dialog?.window?.setWindowAnimations(
                R.style.Animation_GreenTransit_FullScreenDialogFragment_Restore
            )
        } else {
            dialog?.window?.setWindowAnimations(
                R.style.Animation_GreenTransit_FullScreenDialogFragment
            )
        }

        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        disableTransition = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressed = null
    }
}