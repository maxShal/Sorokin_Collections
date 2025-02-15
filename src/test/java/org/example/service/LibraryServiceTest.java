package org.example.service;

import org.example.model.Book;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {

    private LibraryService libraryService;
    private User user1;
    private User user2;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        user1 = new User( "Alice", 20, 1L);
        user2 = new User( "Bob", 80,2L);

        book1 = new Book("Book 1", "Author 1",2000,1L);
        book2 = new Book("Book 2", "Author 2",1900,2L);
        book3 = new Book( "Book 3", "Author 3", 1600, 3L);

        List<User> users = Arrays.asList(user1, user2);
        List<Book> books = Arrays.asList(book1, book2, book3);

        libraryService = new LibraryService(users, books);
    }

    @Test
    public void testGetAllBooks() {
        List<Book> allBooks = libraryService.getAllBooks();
        assertEquals(3, allBooks.size());
        assertTrue(allBooks.contains(book1));
        assertTrue(allBooks.contains(book2));
        assertTrue(allBooks.contains(book3));
    }

    @Test
    public void testGetAllAvailableBooks() {
        List<Book> availableBooks = libraryService.getAllAvailableBooks();
        assertEquals(3, availableBooks.size());
        assertTrue(availableBooks.contains(book1));
        assertTrue(availableBooks.contains(book2));
        assertTrue(availableBooks.contains(book3));
    }

    @Test
    public void testTakeBook() {
        assertTrue(libraryService.takeBook(1L, 1L));
        List<Book> userBooks = libraryService.getUserBooks(1L);
        assertEquals(1, userBooks.size());
        assertTrue(userBooks.contains(book1));

        List<Book> availableBooks = libraryService.getAllAvailableBooks();
        assertEquals(2, availableBooks.size());
        assertFalse(availableBooks.contains(book1));
    }

    @Test
    public void testReturnBook() {
        libraryService.takeBook(1L, 1L);
        assertTrue(libraryService.returnBook(1L, 1L));

        List<Book> userBooks = libraryService.getUserBooks(1L);
        assertEquals(0, userBooks.size());

        List<Book> availableBooks = libraryService.getAllAvailableBooks();
        assertEquals(3, availableBooks.size());
        assertTrue(availableBooks.contains(book1));
    }

    @Test
    public void testTakeBookAlreadyTaken() {
        libraryService.takeBook(1L, 1L);
        assertFalse(libraryService.takeBook(2L, 1L));
    }

    @Test
    public void testReturnBookNotBorrowed() {
        assertFalse(libraryService.returnBook(1L, 1L));
    }
}