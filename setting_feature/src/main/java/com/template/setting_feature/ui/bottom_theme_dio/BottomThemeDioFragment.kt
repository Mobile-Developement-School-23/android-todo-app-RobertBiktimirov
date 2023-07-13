package com.template.setting_feature.ui.bottom_theme_dio

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.template.setting_feature.ui.models.ThemeModel

class BottomThemeDioFragment : BottomSheetDialogFragment() {


    interface ThemeDioResult {
        fun onResult(themeModel: ThemeModel)
    }







}