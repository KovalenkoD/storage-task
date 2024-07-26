package com.bookstore.task.mapper;
import com.bookstore.task.controller.dto.BookStorageDto;
import com.bookstore.task.model.BookStorage;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookStorageMapper
{
    BookStorageDto toDto(BookStorage bookStorage);
    BookStorage toEntity(BookStorageDto bookStorageDto);
}
