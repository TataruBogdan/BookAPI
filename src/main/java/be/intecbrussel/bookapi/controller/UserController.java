package be.intecbrussel.bookapi.controller;

import be.intecbrussel.bookapi.model.AuthUser;
import be.intecbrussel.bookapi.model.BorrowedBook;
import be.intecbrussel.bookapi.model.dto.UserResponse;
import be.intecbrussel.bookapi.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ResponseEntity getAllUser() {
        Optional<List<AuthUser>> allUsers = userService.findAllUsers();

        if(allUsers.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<AuthUser> users = allUsers.get();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get")
    public ResponseEntity getUser(@RequestParam String email, @RequestHeader HttpHeaders headers) {
        Optional<AuthUser> optionalUser = userService.findUser(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AuthUser user = optionalUser.get();

        UserResponse response = new UserResponse(user.getFirstName(), user.getLastName(), user.getBorrowedBooks());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/borrow")
    public ResponseEntity borrowBook(@RequestParam String email, @RequestParam Long bookId) {
        Optional<BorrowedBook> borrowedBook = userService.borrowBook(email, bookId);

        if (borrowedBook.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/return")
    public ResponseEntity returnBook(@RequestParam String email, @RequestParam Long BBId) {
        try {
            boolean success = userService.returnBorrowedBook(email, BBId);

            if (!success) {
                return ResponseEntity.unprocessableEntity().build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/renew")
    public ResponseEntity renewDueDate(@RequestParam String email, @RequestParam Long BBId) {
        try {
            userService.renewBorrowedBook(email, BBId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
