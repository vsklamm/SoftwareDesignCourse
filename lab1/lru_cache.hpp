/**
 * @file lru_cache.h
 * @author Sergey Vasilchenko (klammvs@gmail.com)
 * @brief LRU Cache implementation with asserts (and C++20 concepts) 
 * @version 1.0
 * @date 2021-09-10
 * 
 * @copyright Copyright (c) 2021
 */

#pragma once

#include <cassert>       // std::assert
#include <concepts>      // concept
#include <cstddef>       // std::size
#include <iterator>      // std::list::iterator
#include <list>          // std::list
#include <stdexcept>     // std::out_of_range
#include <unordered_map> // std::unordered_map

namespace vsklamm {

template <typename T>
concept Hashable = requires(T a)
{
    {
        std::hash<T>{}(a)
    }
    ->std::convertible_to<std::size_t>;
};

template <Hashable key_t, typename value_t>
class lru_cache
{
public:
    lru_cache(std::size_t max_size = 32)
        : m_max_size(max_size)
    {
        assert(m_max_size > 0);
        m_keys_map.reserve(m_max_size);
    }

    void put(const key_t & key, const value_t & value)
    {
        assert(m_elements_order.size() <= m_max_size &&
               m_keys_map.size() == m_elements_order.size());

        auto key_it = m_keys_map.find(key);
        if (key_it != m_keys_map.end()) {
            move_to_front(key_it);
            m_elements_order.front().second = value;
        }
        else {
            if (m_elements_order.size() == m_max_size) {
                remove_oldest();
            }
            m_elements_order.emplace_front(key, value);
            m_keys_map[key] = m_elements_order.begin();
        }

        assert(m_elements_order.size() <= m_max_size &&
               m_elements_order.size() == m_keys_map.size());
        assert(m_elements_order.front().first == key);
        assert(m_elements_order.front().second == value);
        assert(m_keys_map.at(key)->second == value);
    }

    value_t get(const key_t & key)
    {
        assert(m_elements_order.size() <= m_max_size &&
               m_keys_map.size() == m_elements_order.size());
        const auto old_size = m_elements_order.size();

        auto key_it = m_keys_map.find(key);
        if (key_it == m_keys_map.end()) {
            throw std::out_of_range("No element with such key");
        }
        move_to_front(key_it);

        assert(m_elements_order.size() >= 1 &&
               m_elements_order.size() == old_size &&
               m_keys_map.size() == old_size);
        assert(key_it->second->first == key);
        return key_it->second->second;
    }

    bool contains(const key_t & key) const
    {
        const auto key_it = m_keys_map.find(key);
        return key_it != m_keys_map.end();
    }

    std::size_t size() const noexcept { return m_elements_order.size(); }

    [[nodiscard]] bool empty() const noexcept { return m_elements_order.empty(); }

    void clear()
    {
        assert(m_elements_order.size() <= m_max_size &&
               m_keys_map.size() == m_elements_order.size());

        m_elements_order.clear();
        m_keys_map.clear();

        assert(m_elements_order.size() == 0 && m_keys_map.size() == 0);
    }

private:
    inline void move_to_front(auto m_keys_map_iterator)
    {
        assert(m_elements_order.size() > 0 &&
               m_elements_order.size() <= m_max_size &&
               m_elements_order.size() == m_keys_map.size());

        auto element_it = m_keys_map_iterator->second;
        const auto [old_key, old_value] = *element_it;

        m_elements_order.splice(m_elements_order.begin(), m_elements_order, element_it);
        m_keys_map_iterator->second = m_elements_order.begin();

        assert(m_elements_order.size() > 0 &&
               m_elements_order.size() <= m_max_size &&
               m_elements_order.size() == m_keys_map.size());
        assert(m_elements_order.front().first == old_key);
        assert(m_elements_order.front().second == old_value);
        assert(m_keys_map.at(old_key)->second == old_value);
    }

    inline void remove_oldest()
    {
        assert(m_elements_order.size() == m_max_size);

        const auto & lru_key = m_elements_order.back().first;
        auto lru_key_it = m_keys_map.find(lru_key);
        m_keys_map.erase(lru_key_it);
        m_elements_order.pop_back();

        assert(m_elements_order.size() == m_max_size - 1);
    }

private:
    using KeyElement = std::pair<key_t, value_t>;
    using ListIterator = typename std::list<KeyElement>::iterator;

    const std::size_t m_max_size;
    std::list<KeyElement> m_elements_order;
    std::unordered_map<key_t, ListIterator> m_keys_map;
};
} // namespace vsklamm