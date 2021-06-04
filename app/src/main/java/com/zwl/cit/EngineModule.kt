package com.zwl.cit

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class EngineModule {
    @Binds
    abstract fun bindEngine(
        engine: Engine
    ): IEngine

}