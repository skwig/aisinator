package sk.skwig.aisinator.feature.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.skwig.aisinator.databinding.FragmentTimetableBinding
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.BaseFragment

class TimetableFragment : BaseFragment<TimetableViewModel, FragmentTimetableBinding>() {

    private lateinit var timetableAdapter: TimetableAdapter

    override fun createViewModel() = Injector.injectTimetableViewModel(this)

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentTimetableBinding {
        return FragmentTimetableBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            timetableAdapter = TimetableAdapter()
        }
    }
}
