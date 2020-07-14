package com.hactyki

import android.app.Application
import com.hactyki.data.firebase.FirebaseSource
import com.hactyki.data.repositories.TestRepository
import com.hactyki.data.repositories.UserRepository
import com.hactyki.ui.auth.AuthViewModelFactory
import com.hactyki.ui.companyuser.CompanyViewModelFactory
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

class KodeinClass: Application(), DIAware {


    override val di = DI.lazy {
        import (androidXModule(this@KodeinClass))
        bind( ) from singleton { FirebaseSource()}
        bind( ) from singleton { UserRepository(instance()) }
        bind() from singleton { TestRepository(instance()) }

        bind() from provider { AuthViewModelFactory (instance())}
        bind() from provider { CompanyViewModelFactory (instance())}

    }

}