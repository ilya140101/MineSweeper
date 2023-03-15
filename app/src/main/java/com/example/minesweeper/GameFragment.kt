package com.example.minesweeper


import android.R.attr.defaultValue
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minesweeper.databinding.GameFragmentBinding


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var viewModelFactory: GameViewModelFactory

    private lateinit var binding: GameFragmentBinding

    private lateinit var mapping: MutableMap<Button, Int>
    private val format: String = "%03d"



        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val args = GameFragmentArgs.fromBundle(requireArguments())

        binding = DataBindingUtil.inflate(
            inflater, R.layout.game_fragment, container, false
        )

        viewModelFactory = GameViewModelFactory(args.height, args.width, args.countMine)
        viewModel = ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]


        mapping = mutableMapOf()

        generateFields()

        binding.resetButton.setOnClickListener { viewModel.clear() }

        viewModel.countRemainingMines.observe(viewLifecycleOwner) {
            binding.countMine.text = format.format(it)
        }

        viewModel.seconds.observe(viewLifecycleOwner) {
            binding.chronometer.text = format.format(it)
        }

        viewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                GameState.LOSE -> binding.resetButton.setBackgroundResource(R.drawable.sad_smile)
                GameState.WIN -> binding.resetButton.setBackgroundResource(R.drawable.finish)
                else -> binding.resetButton.setBackgroundResource(R.drawable.smile)
            }
        }

        return binding.root
    }

    private fun generateFields() {
        binding.apply {
            mainGrid.columnCount = viewModel.width
            mainGrid.rowCount = viewModel.height
            for (i in 0 until viewModel.countCell) {
                val button = Button(context)
                //button.layoutParams = ViewGroup.LayoutParams(100, 100)
                button.id = View.generateViewId()
                button.setTextAppearance(R.style.button_style)
                mapping[button] = i
                //button.textAlignment = View.TEXT_ALIGNMENT_CENTER
                viewModel.gameField[i].condition.observe(
                    viewLifecycleOwner
                ) {
                    when (it) {
                        Condition.FLAG -> {
                            button.setBackgroundResource(R.drawable.flag_button)
                            button.setOnClickListener(null)
                        }
                        Condition.HIDDEN -> {
                            button.setBackgroundResource(R.drawable.hidden_button)
                            button.text = ""
                            button.setOnLongClickListener { setFlag(button) }
                            button.setOnClickListener { click(button) }
                        }
                        else -> open(button)

                    }
                }
                viewModel.gameField[i].isFlagError.observe(viewLifecycleOwner) {
                    if (it == true) button.setBackgroundResource(R.drawable.error_flag)
                }
                mainGrid.addView(button)

            }
        }
    }

    private fun click(button: Button) {
        if (viewModel.gameState.value == GameState.WIN || viewModel.gameState.value == GameState.LOSE) return
        mapping[button]?.let { viewModel.open(it) }
    }


    private fun open(button: Button) {
        val cell = viewModel.gameField[mapping[button]!!]
        val value = cell.value
        button.setOnClickListener(null)
        button.setOnLongClickListener(null)
        if (cell.type == Type.MINE) {
            if (cell.condition.value != Condition.FLAG) {
                if (!cell.isOpenMine) button.setBackgroundResource(R.drawable.black_mine)
                else button.setBackgroundResource(R.drawable.red_mine)
            }
            return
        }

        button.setBackgroundResource(R.drawable.empty_button)
        button.text = value.toString()
        button.setTextColor(
            ContextCompat.getColor(
                requireContext(), when (value) {
                    1 -> R.color.blue
                    2 -> R.color.green
                    3 -> R.color.red
                    4 -> R.color.dark_blue
                    5 -> R.color.brown
                    6 -> R.color.turquoise
                    7 -> R.color.pink
                    8 -> R.color.black
                    else -> R.color.gray
                }
            )
        )
    }

    private fun setFlag(button: Button): Boolean {
        if (viewModel.gameState.value == GameState.WIN || viewModel.gameState.value == GameState.LOSE) return false
        viewModel.changeFlag(mapping[button]!!)
        return true

    }
}