package sk.skwig.aisinator.feature.authentication

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import sk.skwig.aisinator.common.AisApi

class AuthenticationManager(private val aisApi: AisApi) {

    private val disposable = CompositeDisposable()

    val foo: Single<ResponseBody>

    init {
        foo = aisApi
            .login("", "")
            .subscribeOn(Schedulers.io())
    }

}