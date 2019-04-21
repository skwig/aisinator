package sk.skwig.aisinator.feature.chat.viewmodel

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import sk.skwig.aisinator.common.util.listing.ListingViewModel
import sk.skwig.aisinator.feature.chat.Chatroom
import sk.skwig.aisinator.feature.course.CourseRepository

class ChatroomListViewModel(courseRepository: CourseRepository) : ListingViewModel<Chatroom>() {

    private val openChatroomRelay = PublishRelay.create<Chatroom>()

    val openChatroom: Observable<Chatroom>
        get() = openChatroomRelay

    init {
        disposable += courseRepository.getActiveCourses()
            .map { it.map { Chatroom(it.id, it) } }
            .toViewState()
            .subscribe(stateRelay)
    }

    fun onChatroomSelected(chatroom: Chatroom) {
        openChatroomRelay.accept(chatroom)
    }

}