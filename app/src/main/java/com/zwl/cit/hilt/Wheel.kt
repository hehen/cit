package com.zwl.cit.hilt

import android.util.Log

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
class Wheel {
    fun use(){
        Log.d("zwl","调用wheel"+this.hashCode())
    }
}