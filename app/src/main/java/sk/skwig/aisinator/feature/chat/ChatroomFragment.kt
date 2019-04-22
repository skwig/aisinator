package sk.skwig.aisinator.feature.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.util.MarginItemDecoration
import sk.skwig.aisinator.common.util.showChild
import sk.skwig.aisinator.databinding.FragmentChatroomBinding
import sk.skwig.aisinator.di.Injector
import sk.skwig.aisinator.feature.BaseFragment
import sk.skwig.aisinator.feature.chat.paging.ChatPagingState
import sk.skwig.aisinator.feature.chat.viewmodel.ChatroomViewModel
import timber.log.Timber

class ChatroomFragment : BaseFragment<ChatroomViewModel, FragmentChatroomBinding>() {

    protected lateinit var adapter: ChatMessageAdapter

    override fun createViewModel(): ChatroomViewModel {
        return ChatroomFragmentArgs.fromBundle(arguments!!).let {
            Injector.injectChatroomViewModel(this, it.chatroomId)
        }
    }

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?): FragmentChatroomBinding {
        return FragmentChatroomBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return super.onCreateView(inflater, container, savedInstanceState).also {
            adapter = ChatMessageAdapter(viewModel::onRetry)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val onNextPage = viewModel::onNextPage
            val layoutManager = LinearLayoutManager(context)
                .apply { reverseLayout = true }

            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(MarginItemDecoration(8f, 8f))
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val currentLastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                    if (dy < 0 && currentLastVisiblePosition >= adapter.itemCount - 3) {
                        onNextPage()
                    }
                }
            })

            disposable += viewModel.uiState
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        when (it) {
                            ChatPagingState.InitialState,
                            is ChatPagingState.NoItemState.LoadingPage -> viewAnimator.showChild(progressBar)
                            is ChatPagingState.NoItemState.Error -> viewAnimator.showChild(error)
                            is ChatPagingState.HasItemsState -> {
                                viewAnimator.showChild(recyclerView)
                                adapter.submitState(it)

                                // automatically get next page if cant scroll (screen isnt filled)
                                if (it is ChatPagingState.HasItemsState.Normal) {
                                    if (!recyclerView.canScrollVertically(-1)) {
                                        onNextPage()
                                    }
                                }
                            }
                        }
                    },
                    onError = Timber::e
                )
        }
    }
}