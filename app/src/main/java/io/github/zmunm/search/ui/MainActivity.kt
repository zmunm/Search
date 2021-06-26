package io.github.zmunm.search.ui

import dagger.hilt.android.AndroidEntryPoint
import io.github.zmunm.search.R
import io.github.zmunm.search.databinding.ActivityMainBinding
import io.github.zmunm.search.ui.base.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main
}
