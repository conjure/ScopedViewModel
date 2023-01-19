package uk.co.conjure.vmscopedemo.ui.main.screens.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import uk.co.conjure.viewmodelscope.createViewModelScope
import uk.co.conjure.vmscopedemo.R
import uk.co.conjure.vmscopedemo.databinding.FragmentMainBinding
import uk.co.conjure.vmscopedemo.ui.main.NavigationViewModel

class ParentFragment : Fragment() {

    private val parentViewModel: ParentViewModel by createViewModelScope()


    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val viewModel: ParentViewModel by createViewModelScope()
    private val navigationViewModel: NavigationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parentViewModel
        return FragmentMainBinding.inflate(inflater, container, false)
            .also { fragmentMainBinding -> this._binding = fragmentMainBinding }
            .root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        observeButtonClicks()
    }

    /**
     * Show the timer
     */
    private fun observeViewModel() {
        viewModel.timer.observe(viewLifecycleOwner) { timerValue ->
            binding.tvCounter.text = timerValue.toString()
        }
    }


    private fun observeButtonClicks() {
        binding.ivBtnCloseChild.setOnClickListener {
            binding.cvChildContainer.visibility = View.GONE
            childFragmentManager.popBackStack()
        }

        binding.btnOpenChild.setOnClickListener {
            binding.cvChildContainer.visibility = View.VISIBLE
            attachChildFragment()
        }

        binding.btnOpenOtherScreen.setOnClickListener {
            navigationViewModel.showSecondScreen()
        }

        binding.btnOpenMultifragmentScreen.setOnClickListener {
            navigationViewModel.showMultifragmentScreen()
        }
    }

    private fun attachChildFragment() {
        childFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.fl_child_fragment_container, ChildFragment())
            .commit()
    }
}