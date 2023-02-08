package uk.co.conjure.vmscopedemo.ui.main.screens.multifragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uk.co.conjure.viewmodelscope.scopedInterface
import uk.co.conjure.viewmodelscope.scopedViewModel
import uk.co.conjure.vmscopedemo.databinding.FragmentMultiChildBinding

class MultiChildFragment : Fragment() {
    private val viewModel: ChildViewModel by scopedInterface()

    private var _binding: FragmentMultiChildBinding? = null
    private val binding: FragmentMultiChildBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMultiChildBinding.inflate(inflater, container, false)
            .also { fragmentMainBinding -> this._binding = fragmentMainBinding }
            .root
    }

    override fun onStart() {
        super.onStart()
        binding.tvNumber.text = viewModel.number.toString()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}