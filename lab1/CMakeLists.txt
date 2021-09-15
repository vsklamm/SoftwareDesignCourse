cmake_minimum_required(VERSION 3.5.2)

project(lru_cache)

set(CMAKE_C_COMPILER /usr/bin/gcc-11)
set(CMAKE_CXX_COMPILER /usr/bin/g++-11)

find_package(GTest REQUIRED)
if(NOT GTEST_LIBRARY)
  message("gtest library not found")
endif()

add_executable(
    lru_cache_test 
    lru_cache_test.cpp
    lru_cache.hpp
)

if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)                    
  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++20 -O0 -Wall -Wextra -Werror -pedantic")                                                
endif() 

target_link_libraries(lru_cache_test gtest gtest_main)
