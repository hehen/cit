package com.zwl.cit

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
@Singleton
class BigCar @Inject constructor() {
    fun use(){
        Log.d("zwl","调用BigCar"+this.hashCode())
    }
}