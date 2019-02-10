package sk.skwig.aisinator.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.course.CourseAdapter
import sk.skwig.aisinator.course.viewmodel.ActiveCoursesViewModel
import sk.skwig.aisinator.course.viewmodel.CourseworkDeadlinesViewModel
import sk.skwig.aisinator.dashboard.databinding.FragmentDashboardBinding
import sk.skwig.aisinator.dashboard.viewmodel.DashboardViewModel
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding>() {


    lateinit var activeCoursesViewModel: ActiveCoursesViewModel
    lateinit var courseworkDeadlinesViewModel: CourseworkDeadlinesViewModel

    private lateinit var activeCoursesAdapter: CourseAdapter
    private lateinit var deadlinesAdapter: DeadlineAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: subscriby do onViewCreated
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel::class.java).also {}
        activeCoursesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ActiveCoursesViewModel::class.java)
            .also {
                disposable += it.state
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onNext = {
                            when (it) {
                                is ActiveCoursesViewModel.ViewState.Normal -> activeCoursesAdapter.submitList(it.activeCourses)
                            }
                        },
                        onError = Timber::e
                    )
            }

        courseworkDeadlinesViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(CourseworkDeadlinesViewModel::class.java)
                    .also {
                        disposable += it.state
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                onNext = {
                                    when (it) {
                                        is CourseworkDeadlinesViewModel.ViewState.Normal -> deadlinesAdapter.submitList(
                                            it.deadlines
                                        )
                                    }
                                },
                                onError = Timber::e
                            )
                    }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)

        activeCoursesAdapter = CourseAdapter()
        deadlinesAdapter = DeadlineAdapter(courseworkDeadlinesViewModel::onDismiss)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutActiveCourses.apply {
            courseRecycler.adapter = activeCoursesAdapter
            courseRecycler.layoutManager = LinearLayoutManager(context)
        }

        binding.layoutDeadlines.apply {
            deadlineRecycler.adapter = deadlinesAdapter
            deadlineRecycler.layoutManager = LinearLayoutManager(context)
        }

        binding.fab.setOnClickListener {
            //            viewModel.doFoo()
        }
    }
}