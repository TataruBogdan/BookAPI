package be.intecbrussel.bookapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user_tb")
public class AuthUser {
    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean admin;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_borrowed_books")
    @JsonIgnore
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

    public AuthUser(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected AuthUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowedBook> userBorrowedBooks) {
        this.borrowedBooks = userBorrowedBooks;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}
