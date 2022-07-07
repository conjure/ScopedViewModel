package uk.co.conjure.vmscopedemo.ui.main.screens.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uk.co.conjure.viewmodelscope.scopedInterface
import uk.co.conjure.vmscopedemo.databinding.FragmentChildBinding

class ChildFragment : Fragment() {

    private val childViewModel: ChildViewModel by scopedInterface()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChildBinding.inflate(inflater, container, false).apply {
            btnPlus10.setOnClickListener {
                childViewModel.increaseTimer(10)
            }
        }.root
    }
}