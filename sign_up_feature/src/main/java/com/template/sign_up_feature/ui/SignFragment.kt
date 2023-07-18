package com.template.sign_up_feature.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.template.resourses_module.compose_theme.ui_theme.TodoAppTheme
import com.template.sign_up_feature.R
import com.template.sign_up_feature.di.SignComponentViewModel
import com.template.sign_up_feature.di.YandexLoginIntent
import com.template.sign_up_feature.di.modules.viewmodels.ViewModelFactory
import com.template.sign_up_feature.ui.compose_views.SignScreen
import com.template.sign_up_feature.ui.navigation.SignNavigation
import com.template.sign_up_feature.ui.setup_screen.ErrorSignUp
import com.template.sign_up_feature.ui.setup_screen.SuccessSignUp
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var yandexAuthSdk: YandexAuthSdk

    @Inject
    @YandexLoginIntent
    lateinit var yandexLoginIntent: Intent

    private var navigation: SignNavigation? = null


    private val launcherYandexLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.resultCode != Activity.RESULT_CANCELED) {
                val yandexAuthToken = yandexAuthSdk.extractToken(it.resultCode, it.data)
                if (yandexAuthToken != null) {

                    viewModel.onAction(SuccessSignUp(yandexAuthToken))
                } else {
                    viewModel.onAction(ErrorSignUp)
                }
            } else {
                viewModel.onAction(ErrorSignUp)
            }
        }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SignViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SignNavigation) {
            navigation = context
        }

        ViewModelProvider(this).get<SignComponentViewModel>()
            .component.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_sign, container, false)
            .apply {
                findViewById<ComposeView>(R.id.compose_view_id).setContent {
                    TodoAppTheme {
                        SignScreen(viewModel::stateSignScreen.get(), viewModel::onAction)
                    }
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.goToYandexSignUpAct.collect {
                if (it) {
                    Log.d("nullTokenTest", "launcherYandexLogin.launch(yandexLoginIntent)")
                    launcherYandexLogin.launch(yandexLoginIntent)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSuccessRegistered.collect {
                if (it) {
                    navigation?.goToListFragment()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigation = null
    }
}