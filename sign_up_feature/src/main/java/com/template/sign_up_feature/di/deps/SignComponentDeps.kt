package com.template.sign_up_feature.di.deps

import androidx.annotation.RestrictTo
import kotlin.properties.Delegates

interface SignDeps {
}


interface SignDepsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val deps: SignDeps

    companion object : SignDepsProvider by SignDepsStore

}

object SignDepsStore : SignDepsProvider {
    override var deps: SignDeps by Delegates.notNull()
}