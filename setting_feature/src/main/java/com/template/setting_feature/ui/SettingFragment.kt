package com.template.setting_feature.ui

import android.app.ProgressDialog.show
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.template.setting_feature.R
import com.template.setting_feature.databinding.FragmentSettingBinding
import com.template.setting_feature.di.SettingComponentViewModel
import com.template.setting_feature.domain.entity.YandexAccountEntity
import com.template.setting_feature.ui.models.ThemeModel
import com.template.setting_feature.ui.utils.toAvatarUrl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("binding not must be null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<SettingComponentViewModel>()
            .component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accountData.collectLatest { accountData: YandexAccountEntity? ->
                    accountData?.let {
                        changeUi(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.theme.collectLatest {
                when (it) {
                    ThemeModel.DARK -> {
                        binding.darkTheme.isChecked = true
                    }

                    ThemeModel.DAY -> {
                        binding.lightTheme.isChecked = true
                    }

                    ThemeModel.SYSTEM -> {
                        binding.systemTheme.isChecked = true
                    }
                }
            }
        }
    }

    private fun changeUi(accountData: YandexAccountEntity) {

        with(binding) {
            loadingView.isVisible = false
            Glide.with(this@SettingFragment)
                .load(accountData.avatarId.toAvatarUrl())
                .circleCrop()
                .placeholder(com.template.resourses_module.R.drawable.baseline_person_24)
                .into(userAvatar)

            userEmail.text = accountData.email
            userLogin.text = accountData.login
            userName.text = accountData.name
            userPhone.text = accountData.phone



            signOutButton.setOnClickListener {
                Toast.makeText(requireContext(), "Пока не доделано :)", Toast.LENGTH_SHORT).show()
            }

            themeApplicationRadioGroup.setOnCheckedChangeListener { _, i ->
                when (i) {
                    R.id.dark_theme -> {
                        viewModel.saveTheme(ThemeModel.DARK)
                    }

                    R.id.light_theme -> {
                        viewModel.saveTheme(ThemeModel.DAY)
                    }

                    R.id.system_theme -> {
                        viewModel.saveTheme(ThemeModel.SYSTEM)
                    }
                }
            }

            isBackButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}