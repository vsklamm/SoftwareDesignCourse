#include "lru_cache.hpp"

#include "gtest/gtest.h"

#include <random>
#include <string>
#include <vector>

using namespace vsklamm;

TEST(Correctness, SimplePut)
{
    lru_cache<int, int> lru(1);
    lru.put(1, 42);
    EXPECT_EQ(lru.size(), 1);
    EXPECT_TRUE(lru.contains(1));
    EXPECT_EQ(lru.get(1), 42);
}

TEST(Correctness, PutExisted)
{
    lru_cache<int, int> lru;
    lru.put(1, 42);
    EXPECT_EQ(lru.get(1), 42);
    lru.put(1, 420);
    EXPECT_EQ(lru.size(), 1);
    EXPECT_TRUE(lru.contains(1));
    EXPECT_EQ(lru.get(1), 420);
}

TEST(Correctness, PutMaxSize)
{
    lru_cache<int, int> lru(3);
    lru.put(1, 42);
    lru.put(2, 100);
    lru.put(3, 8989);
    lru.put(4, 1);
    EXPECT_EQ(lru.size(), 3);
    EXPECT_FALSE(lru.contains(1));
    EXPECT_EQ(lru.get(2), 100);
    EXPECT_EQ(lru.get(3), 8989);
    EXPECT_EQ(lru.get(4), 1);
}

TEST(Correctness, PutExistedMaxSize)
{
    lru_cache<int, int> lru(3);
    lru.put(1, 42);
    lru.put(2, 100);
    lru.put(3, 8989);
    lru.put(2, 1);
    EXPECT_EQ(lru.size(), 3);
    EXPECT_EQ(lru.get(1), 42);
    EXPECT_EQ(lru.get(2), 1);
    EXPECT_EQ(lru.get(3), 8989);
}

TEST(Correctness, UpdateOrderInGet)
{
    lru_cache<int, int> lru(3);
    lru.put(1, 42);
    lru.put(2, 100);
    lru.put(3, 8989);
    lru.get(1);
    lru.put(4, 9000);
    lru.put(5, 9999);
    EXPECT_EQ(lru.size(), 3);
    EXPECT_EQ(lru.get(1), 42);
    EXPECT_FALSE(lru.contains(2));
    EXPECT_FALSE(lru.contains(3));
}

TEST(Correctness, DifferentTypes)
{
    lru_cache<std::string, float> lru(1);
    lru.put("42", 42);
    EXPECT_EQ(lru.size(), 1);
    EXPECT_TRUE(lru.contains("42"));
    EXPECT_EQ(lru.get("42"), 42);
}

TEST(Correctness, Clear)
{
    const int size = 100;
    lru_cache<int, int> lru(size);
    for (int i = 0; i < size * 100; ++i) {
        lru.put(i, 42 + i);
    }
    EXPECT_EQ(lru.size(), size);
    lru.clear();
    EXPECT_TRUE(lru.empty());
}

TEST(Correctness, MissingValue)
{
    lru_cache<int, int> lru(1);
    EXPECT_THROW(lru.get(42), std::out_of_range);
}
