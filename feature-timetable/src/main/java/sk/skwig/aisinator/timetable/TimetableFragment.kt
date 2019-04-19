package sk.skwig.aisinator.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.timetable.databinding.FragmentTimetableBinding

class TimetableFragment : BaseFragment<TimetableViewModel, FragmentTimetableBinding>() {

    private lateinit var timetableAdapter: TimetableAdapter

    override fun createViewModel() = ViewModelProviders.of(this, viewModelFactory).get(TimetableViewModel::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTimetableBinding.inflate(layoutInflater, container, false)

        timetableAdapter = TimetableAdapter()

        return binding.root
    }
}
