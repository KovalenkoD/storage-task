package com.bookstore.task.repository;

import com.bookstore.task.model.BookStorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStorageRepository extends JpaRepository<BookStorage, Long>, JpaSpecificationExecutor<BookStorage> {}