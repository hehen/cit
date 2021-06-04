LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

#OPENCV_INSTALL_MODULES:=off
#OPENCV_LIB_TYPE:=SHARED

include D:\wan\OpenCV-android-sdk\sdk\native\jni\OpenCV.mk

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_MODULE    := mixed_sample
LOCAL_SRC_FILES := jni_part.cpp
LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
