package sk.skwig.aisinator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import sk.skwig.aisinator.common.BaseActivity
import timber.log.Timber

class MainActivity : BaseActivity<MainViewModel, sk.skwig.aisinator.databinding.ActivityMainBinding>() {

    // TODO: skusit cez findFragment
    override val navController: NavController
        get() = supportFragmentManager
            .fragments
            .first()
            .findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java).also {
            disposable += it.showLoginScreen
                .subscribeBy(
                    onNext = { navController.navigate(R.id.action_global_loginFragment) },
                    onError = Timber::e
                )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
