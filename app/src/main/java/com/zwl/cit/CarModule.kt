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
class CarModule {

    @Provides
    fun provideWheel(
        // Potential dependencies of this type
    ): Wheel {
        return Wheel()
    }

    @HeadLight
    @Provides
    fun provideHeadLight(): Light {
        return Light()
    }

    @FootLight
    @Provides
    fun provideFootLight(): Light {
        return Light()
    }
}