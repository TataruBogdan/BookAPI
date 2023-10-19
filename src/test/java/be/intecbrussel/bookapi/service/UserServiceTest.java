package be.intecbrussel.bookapi.service;

import be.intecbrussel.bookapi.model.AuthUser;
import be.intecbrussel.bookapi.model.Book;
import be.intecbrussel.bookapi.model.BorrowedBook;
import be.intecbrussel.bookapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {


    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookService bookService;

    @MockBean
    private BorrowedBookService borrowedBookService;

    @Autowired
    ApplicationContext context;

    @Test
    void findUserShouldReturnTrueIfExist() {
        // Arrange
        String userEmail = "gigi@email.com";
        AuthUser expectedUser = new AuthUser(userEmail, "gigi", "Gigi", "Igig");

        OngoingStubbing<Optional<AuthUser>> optionalOngoingStubbing = when(userRepository.findById(userEmail)).thenReturn(Optional.of(expectedUser));

        //Act
        Optional<AuthUser> result = userService.findUser(userEmail);

        // Assert
        assertEquals(Optional.of(expectedUser), result);
        assertTrue(result.isPresent());
        assertEquals(optionalOngoingStubbing, expectedUser);
        verify(userRepository).findById(userEmail);

    }

    @Test
    void updateUser() {

        //Arrange
        AuthUser userToUpdate = new AuthUser("gig@email.com", "gigi", "Gigi", "Igig");

        // Act
        userService.updateUser(userToUpdate);

        //Assert
        verify(userRepository).saveAndFlush(userToUpdate);
    }

    @Test
    void borrowBook() throws Exception{

        // Arrange
        String userEmail = "gigi@email.com";
        AuthUser mockUser = new AuthUser(userEmail,"gigi", "Gigi", "Igig");
        Book mockBookToBeBorrowed = new Book
                ("123.15213.123.123", "/some URL", "Title", "Me", "XXX", "about xxx", "2023/10/16");

        BorrowedBook mockBorrowedBook = new BorrowedBook(mockUser, mockBookToBeBorrowed, LocalDate.now(), LocalDate.now().plusDays(1));

        //Act
        Optional<BorrowedBook> result = userService.borrowBook(userEmail, mockBookToBeBorrowed.getId());

        //Assert
        assertTrue(result.isPresent());
        assertEquals(mockBorrowedBook, result.get());

        //Verify interactions
        verify(bookService).findBookById(mockBorrowedBook.getId());
        verify(userRepository).findById(mockUser.getEmail());
        verify(borrowedBookService).borrowBook(mockUser, mockBookToBeBorrowed);
        verify(userRepository).saveAndFlush(mockUser);


    }

    @Test
    void returnBorrowedBook() throws Exception {

        //Arrange
        String userEmail = "gigi@email.com";
        AuthUser mockUser = new AuthUser(userEmail,"gigi", "Gigi", "Igig");
        Book mockBookToBeBorrowed = new Book
                ("123.15213.123.123", "/some URL", "Title", "Me", "XXX", "about xxx", "2023/10/16");

        BorrowedBook mockBorrowedBook = new BorrowedBook(mockUser, mockBookToBeBorrowed, LocalDate.now(), LocalDate.now().plusDays(1));

        //Act
        boolean returnBorrowedBook = userService.returnBorrowedBook(userEmail, 1);

        assertTrue(returnBorrowedBook);

        //Verify interactions
        verify(bookService).findBookById(mockBorrowedBook.getId());
        verify(userRepository).findById(mockUser.getEmail());
        verify(borrowedBookService).borrowBook(mockUser, mockBookToBeBorrowed);


    }

    @Test
    void renewBorrowedBook() throws Exception {

        //Arrange
        String userEmail = "gigi@email.com";
        AuthUser mockUser = new AuthUser(userEmail,"gigi", "Gigi", "Igig");
        Book mockBookToBeBorrowed = new Book
                ("123.15213.123.123", "/some URL", "Title", "Me", "XXX", "about xxx", "2023/10/16");
        long borrowedBookId = 1L;

        BorrowedBook mockBorrowedBook = new BorrowedBook(mockUser, mockBookToBeBorrowed, LocalDate.now(), LocalDate.now().plusDays(1));

        when(userRepository.findById(userEmail)).thenReturn(Optional.of(mockUser));
        when(borrowedBookService.renewBorrowedBook(mockUser, borrowedBookId)).thenReturn(true);

        //Act
        userService.renewBorrowedBook(userEmail, borrowedBookId);

        //Verify
        verify(userRepository).findById(userEmail);
        verify(borrowedBookService).renewBorrowedBook(mockUser, borrowedBookId);
        verify(userRepository).saveAndFlush(mockUser);


    }
}