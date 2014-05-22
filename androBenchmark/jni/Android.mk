LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := app_androbenchmark_Matrix
LOCAL_SRC_FILES := app_androbenchmark_Matrix.c

include $(BUILD_SHARED_LIBRARY)
