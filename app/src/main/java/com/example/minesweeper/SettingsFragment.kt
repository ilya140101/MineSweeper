package com.example.minesweeper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.minesweeper.databinding.SettingsFragmentBinding


class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.settings_fragment, container, false
        )

        setClickListener()





        return binding.root
    }

    private fun setClickListener() {
        binding.apply {
            startButton.setOnClickListener {
                val height: Int = editHeight.text.toString().toIntOrNull()?: 0
                val width: Int = editWidth.text?.toString()?.toIntOrNull() ?: 0
                val countMine: Int = editCountMine.text?.toString()?.toIntOrNull() ?: 0

                val max = height * width - 9
                if (height == 0 || width == 0) {
                    showAlert(resources.getString(R.string.zero_text))
                    return@setOnClickListener
                }
                if (countMine > max) {
                    showAlert(resources.getString(R.string.alert_text) + max)
                    return@setOnClickListener
                }
                Navigation.findNavController(it).navigate(
                    SettingsFragmentDirections.actionSettingsFragmentToGameFragment(
                        height,
                        width,
                        countMine
                    )
                )


            }
        }
    }

    private fun showAlert(text: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("")
            .setMessage(text)
            .setPositiveButton(android.R.string.ok) { _, _ ->
            }
            .show()
    }


}