package uk.co.conjure.vmscopedemo.ui.main.screens.multifragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import uk.co.conjure.viewmodelscope.addViewModelKey
import uk.co.conjure.viewmodelscope.createViewModelScope
import uk.co.conjure.vmscopedemo.databinding.FragmentMultiFragmentParentBinding
import uk.co.conjure.vmscopedemo.ui.main.NavigationViewModel

private const val CHILD_1_TAG = "CHILD_1_TAG"

class MultiFragmentFragment : Fragment() {

    private val child1ViewModelKey = "child1ViewModelKey"
    private val child2ViewModelKey = "child2ViewModelKey"

    private var _binding: FragmentMultiFragmentParentBinding? = null
    private val binding: FragmentMultiFragmentParentBinding get() = _binding!!

    private val navigationViewModel: NavigationViewModel by activityViewModels()

    init {
        createViewModelScope<ChildViewModelImpl>(child1ViewModelKey)
        createViewModelScope<ChildViewModelImpl>(child2ViewModelKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMultiFragmentParentBinding.inflate(inflater, container, false)
            .also { fragmentMainBinding -> this._binding = fragmentMainBinding }
            .root
    }

    override fun onStart() {
        super.onStart()

        binding.btnGoTo.setOnClickListener {
            navigationViewModel.showFirstScreen()
        }

        //inflate two child fragments into the root linear layout in binding
        if (childFragmentManager.findFragmentByTag(CHILD_1_TAG) == null) {
            childFragmentManager.beginTransaction()
                .add(
                    binding.fragmentContainer.id,
                    MultiChildFragment::class.java,
                    Bundle().addViewModelKey(child1ViewModelKey),
                    CHILD_1_TAG
                )
                .add(
                    binding.fragmentContainer.id,
                    MultiChildFragment::class.java,
                    Bundle().addViewModelKey(child2ViewModelKey)
                )
                .commit()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}