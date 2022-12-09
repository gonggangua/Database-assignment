package com.dao;

import com.pojo.Category;
import com.pojo.CategoryVisible;
import com.pojo.Group;

import java.util.List;
import java.util.Map;

public interface CategoryVisibleMapper {
    void insert(CategoryVisible visible);

    void delete(CategoryVisible visible);

    List<Category> getVisibleCategories(Map<String, Object> map);
}
