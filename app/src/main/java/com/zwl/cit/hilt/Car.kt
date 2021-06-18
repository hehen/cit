package com.zwl.cit.hilt

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
@ActivityScoped
class Car @Inject constructor(@ActivityContext private val context: Context, val engine: Engine, val wheel: Wheel, @HeadLight val headLight: Light, @FootLight val footLight: Light) {
    fun use(){
        Log.d("zwl","调用Car"+this.hashCode())
        engine.use()
        wheel.use()
        headLight.use()
        footLight.use()
    }
}