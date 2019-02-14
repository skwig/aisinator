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
import sk.skwig.aisinator.dashboard.viewmodel.ActiveCoursesViewModel
import sk.skwig.aisinator.dashboard.viewmodel.DeadlinesViewModel
import sk.skwig.aisinator.dashboard.databinding.FragmentDashboardBinding
import sk.skwig.aisinator.dashboard.viewmodel.DashboardViewModel
import sk.skwig.aisinator.dashboard.viewmodel.UpcomingLessonsViewModel
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding>() {


    lateinit var activeCoursesViewModel: ActiveCoursesViewModel
    lateinit var deadlinesViewModel: DeadlinesViewModel
    lateinit var upcomingLessonsViewModel: UpcomingLessonsViewModel

    private lateinit var activeCoursesAdapter: CourseAdapter
    private lateinit var deadlinesAdapter: DeadlineAdapter
    private lateinit var upcomingLessonsAdapter: UpcomingLessonsAdapter

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

        deadlinesViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(DeadlinesViewModel::class.java)
                    .also {
                        disposable += it.state
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                onNext = {
                                    when (it) {
                                        is DeadlinesViewModel.ViewState.Normal -> deadlinesAdapter.submitList(
                                            it.deadlines
                                        )
                                    }
                                },
                                onError = Timber::e
                            )
                    }

        upcomingLessonsViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(UpcomingLessonsViewModel::class.java)
                    .also {
                        disposable += it.state
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                onNext = {
                                    when (it) {
                                        is UpcomingLessonsViewModel.ViewState.Normal -> upcomingLessonsAdapter.submitList(
                                            it.upcomingLessons
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
        deadlinesAdapter = DeadlineAdapter(deadlinesViewModel::onDismiss)
        upcomingLessonsAdapter = UpcomingLessonsAdapter()

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

        binding.layoutUpcomingLessons.apply {
            upcomingLessonsRecycler.adapter = upcomingLessonsAdapter
            upcomingLessonsRecycler.layoutManager = LinearLayoutManager(context)
        }

        binding.fab.setOnClickListener {
            //            viewModel.doFoo()
        }
    }
}
