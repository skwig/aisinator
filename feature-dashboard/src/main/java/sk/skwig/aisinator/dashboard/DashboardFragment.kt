package sk.skwig.aisinator.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.BaseFragment
import sk.skwig.aisinator.common.course.CourseAdapter
import sk.skwig.aisinator.common.course.viewmodel.ActiveCoursesViewModel
import sk.skwig.aisinator.common.deadline.DeadlineAdapter
import sk.skwig.aisinator.common.deadline.viewmodel.DeadlinesViewModel
import sk.skwig.aisinator.common.lesson.UpcomingLessonsAdapter
import sk.skwig.aisinator.common.lesson.viewmodel.UpcomingLessonsViewModel
import sk.skwig.aisinator.dashboard.databinding.FragmentDashboardBinding
import sk.skwig.aisinator.dashboard.viewmodel.DashboardViewModel
import timber.log.Timber

class DashboardFragment : BaseFragment<DashboardViewModel, FragmentDashboardBinding>() {

    lateinit var activeCoursesViewModel: ActiveCoursesViewModel
    lateinit var deadlinesViewModel: DeadlinesViewModel
    lateinit var upcomingLessonsViewModel: UpcomingLessonsViewModel

    private lateinit var activeCoursesAdapter: CourseAdapter
    private lateinit var deadlinesAdapter: DeadlineAdapter
    private lateinit var upcomingLessonsAdapter: UpcomingLessonsAdapter

    override fun createViewModel() = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activeCoursesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ActiveCoursesViewModel::class.java)
        deadlinesViewModel = ViewModelProviders.of(this, viewModelFactory).get(DeadlinesViewModel::class.java)
        upcomingLessonsViewModel = ViewModelProviders.of(this, viewModelFactory).get(UpcomingLessonsViewModel::class.java)

        activeCoursesAdapter = CourseAdapter()
        deadlinesAdapter = DeadlineAdapter(deadlinesViewModel::onDismiss)
        upcomingLessonsAdapter = UpcomingLessonsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: rozbit do fragmentov + template method pre toto vsetko vvv
        activeCoursesViewModel.also {
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

        deadlinesViewModel.also {
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

        upcomingLessonsViewModel.also {
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
