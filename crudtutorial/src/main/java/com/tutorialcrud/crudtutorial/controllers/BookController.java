package com.tutorialcrud.crudtutorial.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.tutorialcrud.crudtutorial.model.Book;
import com.tutorialcrud.crudtutorial.repository.BookRepo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
public class BookController {

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/getAllBooks")
    public ResponseEntity<?> getAllBooks() {
        try {
            List<Book> bookList = bookRepo.findAll();

            if (bookList.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No books found in the database");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            return new ResponseEntity<>(bookList, HttpStatus.OK);

        } catch (Exception ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal server error while retrieving books");
            response.put("details", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookById/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Invalid book ID. ID must be a positive number");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Optional<Book> bookData = bookRepo.findById(id);

            if (bookData.isPresent()) {
                return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
            }

            Map<String, String> response = new HashMap<>();
            response.put("error", "Book not found");
            response.put("message", "Book with ID " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal server error while retrieving book");
            response.put("details", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            // Validate input
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Book title is required");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Book author is required");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Trim whitespace
            book.setTitle(book.getTitle().trim());
            book.setAuthor(book.getAuthor().trim());

            Book savedBook = bookRepo.save(book);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book added successfully");
            response.put("book", savedBook);
            response.put("bookId", savedBook.getId());

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to add book");
            response.put("details", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateBookById/{id}")
    public ResponseEntity<?> updateBookById(@PathVariable Long id, @RequestBody Book newBookData) {
        try {
            // Validate ID
            if (id == null || id <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Invalid book ID. ID must be a positive number");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Validate input
            if (newBookData.getTitle() != null && newBookData.getTitle().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Book title cannot be empty");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (newBookData.getAuthor() != null && newBookData.getAuthor().trim().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Book author cannot be empty");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Optional<Book> oldBookData = bookRepo.findById(id);

            if (oldBookData.isPresent()) {
                Book updatedBookData = oldBookData.get();

                // Update only non-null fields
                if (newBookData.getTitle() != null) {
                    updatedBookData.setTitle(newBookData.getTitle().trim());
                }
                if (newBookData.getAuthor() != null) {
                    updatedBookData.setAuthor(newBookData.getAuthor().trim());
                }

                Book savedBook = bookRepo.save(updatedBookData);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Book updated successfully");
                response.put("book", savedBook);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            Map<String, String> response = new HashMap<>();
            response.put("error", "Book not found");
            response.put("message", "Cannot update. Book with ID " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to update book");
            response.put("details", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        try {
            // Validate ID
            if (id == null || id <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Invalid book ID. ID must be a positive number");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if (!bookRepo.existsById(id)) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Book not found");
                response.put("message", "Cannot delete. Book with ID " + id + " does not exist");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            bookRepo.deleteById(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Book deleted successfully");
            response.put("deletedBookId", id.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to delete book");
            response.put("details", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}