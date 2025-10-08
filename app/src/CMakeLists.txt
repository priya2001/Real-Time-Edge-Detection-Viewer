cmake_minimum_required(VERSION 3.6)
project("edge detection")

set(CMAKE_CXX_STANDARD 14)
set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/opencv/sdk/native/jni")

find_package(OpenCV REQUIRED core improper)

add_library(native-lib SHARED src/main/cpp/native-lib.cpp)
target_include_directories(native-lib PRIVATE ${OpenCV_INCLUDE_DIRS})
target_link_libraries(native-lib ${OpenCV_LIBS})

