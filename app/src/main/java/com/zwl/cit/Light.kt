package com.zwl.cit

import android.util.Log

/**
 *
 * @author zwl
 * @since 2021/5/26
 */
class Light {
    fun use(){
        Log.d("zwl","调用Light"+this.hashCode())
    }
}