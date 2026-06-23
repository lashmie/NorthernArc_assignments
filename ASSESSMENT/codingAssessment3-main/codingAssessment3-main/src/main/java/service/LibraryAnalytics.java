package service;

import entity.Book;

import java.util.*;
import java.util.stream.Collectors;

public class LibraryAnalytics {
    private Map<String, Book> books = new HashMap<>();
    public void loadBooks(List<String> records) {
//While loading(adding records to map) records:
//BOOK_ID must be unique.
//If duplicate BOOK_ID found:
//Keep only the record having:
        if (records == null) {
            return;
        }for (String record : records) {
            if (record == null || record.trim().isEmpty()) {
                continue;
            }
            String[] data = record.trim().split("\\|");
            if (data.length != 6) {
                continue;
            }
            String bookId = data[0].trim();
            String title = data[1].trim();
            String author = data[2].trim();
            String category = data[3].trim();
            int borrowCount;
            double rating;
            try {
                borrowCount = Integer.parseInt(data[4].trim());
                rating = Double.parseDouble(data[5].trim());
            } catch (Exception e) {
                continue;
            }

            //codition ==Rule 2:
//            Ignore records if:
//            Rating < 0
//            Rating > 5
//            or
//            BorrowCount < 0
//            or
//            Any field empty

            if (bookId.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty() || borrowCount < 0 || rating < 0 || rating > 5) {
                continue;
            }

            Book current = new Book(
                    bookId, title, author, category, borrowCount, rating
            );

            Book existing = books.get(bookId);
//Higher Rating
//
//If ratings equal:
//Higher Borrow Count
//
//If both equal:
//Lexicographically smaller Title

            if (existing == null) {
                books.put(bookId, current);
            } else {

                boolean replace = false;
                if (current.getRating() > existing.getRating()) {
                    replace = true;
                }
                else if (current.getRating() == existing.getRating()) {

                    if (current.getBorrowCount() > existing.getBorrowCount()) {
                        replace = true;
                    }
                    else if (current.getBorrowCount() == existing.getBorrowCount()) {
                        if (current.getTitle()
                                .compareTo(existing.getTitle()) < 0) {//- values
                            replace = true;
                        }
                    }
                }
                if (replace) {
                    books.put(bookId, current);
                }}
        }
    }

    public List<Book> topRatedBooks(int n) {
//        topRatedBooks(n)
//        Sort by:
//        Rating DESC
//        If rating is same then
//        Borrow Count DESC
//        If borrow count same then
//        Title ASC
//        Return first n books.
     return books.values().stream()
             .sorted(Comparator.comparingDouble(Book::getRating).reversed().thenComparingInt(Book::getBorrowCount).reversed().thenComparing(Book::getTitle))
             .limit(n)
             .toList();
    }
    public Map<String, Double> averageRatingByCategory() {
//        averageRatingByCategory()
//        Return:
//        TreeMap
//        sorted alphabetically by category.
//        Average rounded to:
//        2 decimal places
            return books.values().stream()
                    .collect(Collectors.groupingBy(
                            Book::getCategory,
                            TreeMap::new,
                            Collectors.collectingAndThen(
                                    Collectors.averagingDouble(Book::getRating),
                                    avg -> Math.round(avg * 100.0) / 100.0
                            )
                    ));
        }

    public Optional<Book> mostBorrowedBook() {
//        mostBorrowedBook()
//        Return book with:
//        Highest borrow count
//
//        If tie:
//        Highest rating
//
//        If tie:
//        Smallest bookId
        return books.values().stream()
                .max(
                        Comparator.comparingInt(Book::getBorrowCount)
                                .thenComparingDouble(Book::getRating)
                                .thenComparing(Book::getBookId,Comparator.reverseOrder())
                );

    }

    public Set<String> authorsWithMultipleCategories() {
//        authorsWithMultipleCategories()
//        Return authors who have books in more than one category.
//        Output must be:
//        TreeSet
        return books.values().stream().collect(Collectors.groupingBy(Book::getAuthor, Collectors.mapping(Book::getCategory, Collectors.toSet())))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
        //return books.values().stream().collect(Collectors.groupingBy(Book::getAuthor, Collectors.toList())).
        public Map<String, List<Book>> groupBooksByAuthor() {
//            groupBooksByAuthor()
//            Return:
//            LinkedHashMap
//            ordered by:
//            Author Name ASC
//            Within each list:
//            Rating DESC
//            Borrow Count DESC
            LinkedHashMap<String, List<Book>> result = new LinkedHashMap<>();
            books.values().stream().collect(Collectors
                            .groupingBy(Book::getAuthor)).entrySet()
                    .stream().sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        List<Book> list = entry.getValue()
                                .stream().sorted(
                                        Comparator.comparing(Book::getRating).reversed()
                                                .thenComparing(Book::getBorrowCount, Comparator.reverseOrder() // Borrow Count DESC
                                                ))
                                .toList();
                        result.put(entry.getKey(), list);
                    });

            return result;
        }


    public List<String> suspiciousBooks() {

        return null;
    }

    public Map<String, Map<String, Book>> categoryWiseTopRatedBookByEachAuthor() {
    return books.values().stream()
                .collect(Collectors.groupingBy(
                        Book::getCategory,
                        Collectors.groupingBy(Book::getAuthor, Collectors.collectingAndThen(Collectors.maxBy(
                                                Comparator.comparing(Book::getRating)
                                        ), Optional::get))));
    }
}