package com.template.sign_up_feature.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.template.resourses_module.compose_theme.ui_theme.TodoAppTheme
import com.template.sign_up_feature.R
import com.template.sign_up_feature.di.SignComponentViewModel
import com.template.sign_up_feature.di.modules.viewmodels.ViewModelFactory
import com.template.sign_up_feature.ui.compose_views.SignScreen
import javax.inject.Inject

class SignFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SignViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

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

}