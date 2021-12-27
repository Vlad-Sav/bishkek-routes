package com.android.bishkekroutes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderView


class InfoFragment : Fragment() {
    lateinit var sliderView: SliderView
    lateinit var textView: TextView
    private val args by navArgs<InfoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_info, container, false)
        sliderView = view.findViewById(R.id.imageSlider)
        var sliderAdapter = SliderAdapter(args.info.link)
        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.setScrollTimeInSec(2);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);

        textView = view.findViewById(R.id.descriptionTextView)
        textView.setText(args.info.description)
        return view

    }
}