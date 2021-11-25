package com.snowdango.musiclogger.viewmodel

import androidx.lifecycle.ViewModel
import com.snowdango.musiclogger.model.ui.MainModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(),KoinComponent {

    private val model by inject<MainModel>()





}