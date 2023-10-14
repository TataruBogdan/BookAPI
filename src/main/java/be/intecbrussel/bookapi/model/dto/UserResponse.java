package be.intecbrussel.bookapi.model.dto;

import be.intecbrussel.bookapi.model.BorrowedBook;

import java.util.ArrayList;
import java.util.List;

public class UserResponse {
    private String firstName;
    private String lastName;

    private List<BorrowedBook> borrowedBooks;

    public UserResponse(String firstName, String lastName, List<BorrowedBook> borrowedBooks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.borrowedBooks = borrowedBooks;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
