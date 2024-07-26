package com.bookstore.task.specifications;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.bookstore.task.model.BookStorage;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BookSpecification
{
    public Specification<BookStorage> by(String text, String field) {
        return (root, query, criteriaBuilder) ->
            StringUtils.isEmpty(text) ? null : criteriaBuilder.like(root.get(field), "%" + text + "%");
    }
}
