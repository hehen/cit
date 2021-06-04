package com.zwl.cit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
@Module
@InstallIn(SingletonComponent::class)
class BigCarModule {

    @Singleton
    @Provides
    fun provideBigCar(
        // Potential dependencies of this type
    ): BigCar {
        return BigCar()
    }

}