package com.example.minesweeper

import android.content.res.Resources
import android.content.res.XmlResourceParser
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.example.minesweeper.databinding.MainFragmentBinding
import java.util.Objects


class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.main_fragment, container, false
        )

        binding.apply {


            setTextAndListener(superEasyDifficulty, resources.getIntArray(R.array.super_easy))
            setTextAndListener(easyDifficulty, resources.getIntArray(R.array.easy))
            setTextAndListener(normalDifficulty, resources.getIntArray(R.array.normal))

            customDifficulty.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())

            }
        }



        return binding.root
    }


    private fun setTextAndListener(button: Button, sizes : IntArray) {
        button.text = "" + sizes[0] + "X" + sizes[1] + " (" + sizes[2] + " " + resources.getString(R.string.mine) + ")"
        button.setOnClickListener { navigate(it, sizes[0], sizes[1], sizes[2]) }
    }

    private fun navigate(view: View, height: Int, width: Int, countMine: Int) {

        Navigation.findNavController(view).navigate(
            MainFragmentDirections.actionMainFragmentToGameFragment(
                height,
                width,
                countMine
            )
        )
    }

}
