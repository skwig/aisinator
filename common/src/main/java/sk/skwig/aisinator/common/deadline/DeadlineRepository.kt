package sk.skwig.aisinator.common.deadline

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import sk.skwig.aisinator.common.auth.AuthManager
import sk.skwig.aisinator.common.data.Deadline
import sk.skwig.aisinator.common.deadline.api.DeadlineApi
import sk.skwig.aisinator.common.deadline.db.DeadlineDao

interface DeadlineRepository {
    fun getActiveDeadlines(): Observable<List<Deadline>>
    fun dismissDeadline(deadline: Deadline): Completable
}

internal class DeadlineRepositoryImpl(
    private val authManager: AuthManager,
    private val deadlineApi: DeadlineApi,
    private val deadlineDao: DeadlineDao
) : DeadlineRepository {
    override fun getActiveDeadlines(): Observable<List<Deadline>> =
        authManager.authentication
            .doOnNext { Log.d("matej", "DeadlineRepositoryImpl.getDeadlines") }
            .switchMap {
                Observable.just(it)
                    .subscribeOn(Schedulers.io())
                    .flatMapSingle { deadlineApi.getDeadlines(it) }
                    .concatMapCompletable { deadlineDao.insertDeadlines(it) }
                    .andThen(deadlineDao.loadAllCourses())
            }

    override fun dismissDeadline(deadline: Deadline): Completable =
        deadlineDao.updateDeadline(deadline.copy(isDismissed = true))
            .subscribeOn(Schedulers.io())
            .doOnComplete { Log.d("matej", "DeadlineRepositoryImpl.dismissDeadline") }
}