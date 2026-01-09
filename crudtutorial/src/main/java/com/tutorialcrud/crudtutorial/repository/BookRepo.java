package com.tutorialcrud.crudtutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tutorialcrud.crudtutorial.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

}
