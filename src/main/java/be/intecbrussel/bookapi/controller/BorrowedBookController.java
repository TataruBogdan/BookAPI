package be.intecbrussel.bookapi.controller;

import be.intecbrussel.bookapi.model.BorrowedBook;
import be.intecbrussel.bookapi.service.BorrowedBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user/borrowed")
public class BorrowedBookController {
    private final BorrowedBookService borrowedBookService;

    public BorrowedBookController(BorrowedBookService borrowedBookService) {
        this.borrowedBookService = borrowedBookService;
    }

    @GetMapping("/getAll")
    public ResponseEntity getBorrowedBooks(@RequestParam String email) {

        Optional<List<BorrowedBook>> optionalBB = borrowedBookService.findBorrowedBooks(email);

        if (optionalBB.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<BorrowedBook> borrowedBooks = optionalBB.get();

        return ResponseEntity.ok(borrowedBooks);
    }




}
