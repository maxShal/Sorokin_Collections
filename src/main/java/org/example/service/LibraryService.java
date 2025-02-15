package org.example.service;

import org.example.model.Book;
import org.example.model.User;

import java.util.*;

public class LibraryService {

    private Map<Long, User> users;

    private Map<Long,Book> books;

    private Map<Long, List<Long>> borrowedBooks;

    private Map<Long, List<Long>> unborrowedBooks;

    public LibraryService(List<User> users, List<Book> books) {
        this.users = new HashMap<>();
        this.books = new HashMap<>();
        this.borrowedBooks = new HashMap<>();
        this.unborrowedBooks = new HashMap<>();


        for (User user : users) {
            this.users.put(user.getUserId(), user);
        }

        for (Book book : books) {
            this.books.put(book.getBookId(), book);
            this.unborrowedBooks.put( book.getBookId(), new ArrayList<>());
        }
    }

    public List<Book> getAllBooks()
    {
        return new ArrayList<>(books.values());
    }

    public List<Book> getAllAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Long bookId : unborrowedBooks.keySet()) {
            if (books.containsKey(bookId)) {
                result.add(books.get(bookId));
            }
        }
        return result;
    }

    public List<Book> getUserBooks(Long userId) {
        List<Book> result = new ArrayList<>();
        List<Long> borrowedBooksId = borrowedBooks.getOrDefault(userId, Collections.emptyList());

        for (Long bookId : borrowedBooksId) {
            if (books.containsKey(bookId)) {
                result.add(books.get(bookId));
            }
        }
        return result;
    }


    public boolean takeBook(Long userId, Long bookId) {

        if (unborrowedBooks.containsKey(bookId)) {
            unborrowedBooks.remove(bookId);

            borrowedBooks.putIfAbsent(userId, new ArrayList<>());
            borrowedBooks.get(userId).add(bookId);

            return true;
        }

        return false;
    }

    public boolean returnBook(Long userId, Long bookId) {

        if (borrowedBooks.containsKey(userId) && borrowedBooks.get(userId).contains(bookId)) {
            borrowedBooks.get(userId).remove(bookId);

            if (borrowedBooks.get(userId).isEmpty()) {
                borrowedBooks.remove(userId);
            }

            unborrowedBooks.putIfAbsent(bookId, new ArrayList<>());
            return true;
        }
        return false;
    }

}
