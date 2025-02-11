package org.example.service;

import org.example.model.Book;
import org.example.model.User;

import java.util.*;

public class LibraryService {

    private Map<Long, User> users;

    private Map<Long,Book> books;

    private Map<Integer, List<Integer>> borrowedBooks;

    private Map<Integer, List<Integer>> unborrowedBooks;

    public LibraryService(List<User> users, List<Book> books) {
        this.users = new HashMap<>();
        this.books = new HashMap<>();
        this.borrowedBooks = new HashMap<>();
        this.unborrowedBooks = new HashMap<>();

        users.forEach(user -> this.users.put(user.getUserId(), user));
        books.forEach(book -> {
            this.books.put(book.getBookId(), book);
            this.unborrowedBooks.put(book.getBookId().intValue(), Collections.emptyList());
        });
    }
    public List<Book> getAllBooks()
    {
        return new ArrayList<>(books.values());
    }

    public List<Book> getAllAvailableBooks() {
        List<Book> result = new ArrayList<>();
        for (Integer bookId : unborrowedBooks.keySet()) {
            if (books.containsKey(bookId)) {
                result.add(books.get(bookId));
            }
        }
        return result;
    }

    public List<Book> getUserBooks(Long userId) {
        List<Book> result = new ArrayList<>();
        List<Integer> borrowedBooksId = borrowedBooks.getOrDefault(userId.intValue(), Collections.emptyList());

        for (Integer bookId : borrowedBooksId) {
            if (books.containsKey(bookId)) {
                result.add(books.get(bookId));
            }
        }
        return result;
    }


    public boolean takeBook(Long userId, Long bookId) {
        Integer bookKey = bookId.intValue();
        Integer userKey = userId.intValue();

        if (unborrowedBooks.containsKey(bookKey)) {
            unborrowedBooks.remove(bookKey);

            borrowedBooks.putIfAbsent(userKey, new ArrayList<>());
            borrowedBooks.get(userKey).add(bookKey);

            return true;
        }

        return false;
    }

    public boolean returnBook(Long userId, Long bookId) {
        Integer bookKey = bookId.intValue();
        Integer userKey = userId.intValue();

        if (borrowedBooks.containsKey(userKey) && borrowedBooks.get(userKey).contains(bookKey)) {
            borrowedBooks.get(userKey).remove(bookKey);

            if (borrowedBooks.get(userKey).isEmpty()) {
                borrowedBooks.remove(userKey);
            }

            unborrowedBooks.putIfAbsent(bookKey, new ArrayList<>());
            return true;
        }
        return false;
    }

}
