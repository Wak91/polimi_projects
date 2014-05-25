LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := app_androbenchmark_GrayScaling
LOCAL_SRC_FILES := app_androbenchmark_GrayScaling.cpp
LOCAL_LDLIBS    := -lm -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)
