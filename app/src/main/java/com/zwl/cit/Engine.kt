package com.zwl.cit

import android.util.Log
import javax.inject.Inject

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
class Engine @Inject constructor():IEngine {
    fun use(){
        Log.d("zwl","调用Engine"+this.hashCode())
    }
}