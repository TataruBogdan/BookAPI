package be.intecbrussel.bookapi.service;

import be.intecbrussel.bookapi.model.Book;
import be.intecbrussel.bookapi.model.BorrowedBook;
import be.intecbrussel.bookapi.model.AuthUser;
import be.intecbrussel.bookapi.repository.BorrowedBookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowedBookService {
    private final BorrowedBookRepository borrowedBookRepository;
    private final BookService bookService;

    public BorrowedBookService(BorrowedBookRepository borrowedBookRepository, BookService bookService) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.bookService = bookService;
    }

    public Optional<BorrowedBook> getBorrowedBook(Long borrowedBookId) {
        return borrowedBookRepository.findById(borrowedBookId);
    }

    public Optional<List<BorrowedBook>> findBorrowedBooks(String email) {
        List<BorrowedBook> borrowedBooksByUser = borrowedBookRepository.findBorrowedBookByUser_Email(email);

        if (borrowedBooksByUser.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(borrowedBooksByUser);
    }

    public Optional<BorrowedBook> borrowBook(AuthUser user, Book book) {

        BorrowedBook borrowedBook = new BorrowedBook(user, book, LocalDate.now(), LocalDate.now().plusMonths(1));
        borrowedBookRepository.save(borrowedBook);
        book.setAvailable(false);
        bookService.updateBook(book);

        return borrowedBookRepository.findBorrowedBookByBook_Id(book.getId());
    }

    public void returnBorrowedBook(Long borrowedBookId) {
        borrowedBookRepository.deleteById(borrowedBookId);
    }

    public boolean renewBorrowedBook(AuthUser user, long borrowedBookId) throws Exception {
        for (BorrowedBook book : user.getBorrowedBooks()) {
            if (book.getId() == borrowedBookId) {
                if (LocalDate.now().isBefore(book.getDueDate())) {
                    book.setDueDate(book.getDueDate().plusWeeks(2));

                    return true;
                } else {
                    throw new Exception("Can't be renewed, due date expired!");
                }
            } else {
                throw new Exception("Borrowed Book Not Found!");
            }
        }

        return false;
    }
}
