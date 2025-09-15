import java.util.*;

// =================== BOOK ===================
class Book {
    String title, author, ISBN, genre;
    String status; // available / borrowed

    Book(String t, String a, String i, String g) {
        title = t;
        author = a;
        ISBN = i;
        genre = g;
        status = "available";
    }

    void display() {
        System.out.println("[" + title + "] by " + author +
                " | ISBN: " + ISBN +
                " | Genre: " + genre +
                " | Status: " + status);
    }

    void updateBook(String t, String a, String i, String g) {
        title = t;
        author = a;
        ISBN = i;
        genre = g;
    }
}

// =================== USER ===================
class User {
    int userId;
    String userName;
    String contact;
    List<Book> borrowedBooks = new ArrayList<>();

    User(int id, String name, String contact) {
        this.userId = id;
        this.userName = name;
        this.contact = contact;
    }

    void borrowBook(Book book) {
        if (book.status.equals("available")) {
            borrowedBooks.add(book);
            book.status = "borrowed";
            System.out.println(userName + " borrowed " + book.title);
        } else {
            System.out.println("Book not available!");
        }
    }

    void returnBook(Book book) {
        if (borrowedBooks.remove(book)) {
            book.status = "available";
            System.out.println(userName + " returned " + book.title);
        } else {
            System.out.println("This user did not borrow " + book.title);
        }
    }

    void displayBorrowedBooks() {
        System.out.println(userName + " borrowed books:");
        for (Book b : borrowedBooks) {
            System.out.println(" - " + b.title);
        }
    }
}

// =================== LIBRARIAN ===================
class Librarian extends User {
    Librarian(int id, String name, String contact) {
        super(id, name, contact);
    }

    void addBook(Book b, Library lib) {
        lib.books.add(b);
        System.out.println("Book added: " + b.title);
    }

    void removeBook(String ISBN, Library lib) {
        Iterator<Book> it = lib.books.iterator();
        while (it.hasNext()) {
            Book b = it.next();
            if (b.ISBN.equals(ISBN)) {
                it.remove();
                System.out.println("Book removed: " + b.title);
                return;
            }
        }
        System.out.println("Book not found!");
    }

    void registerUser(User u, Library lib) {
        lib.users.add(u);
        System.out.println("User registered: " + u.userName);
    }
}

// =================== LIBRARY ===================
class Library {
    List<Book> books = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<BorrowTransaction> transactions = new ArrayList<>();

    // --- Book Search ---
    Book searchBookByTitle(String title) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    Book searchBookByISBN(String isbn) {
        for (Book b : books) {
            if (b.ISBN.equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        return null;
    }

    List<Book> searchBooksByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.author.equalsIgnoreCase(author)) {
                results.add(b);
            }
        }
        return results;
    }

    List<Book> searchBooksByGenre(String genre) {
        List<Book> results = new ArrayList<>();
        for (Book b : books) {
            if (b.genre.equalsIgnoreCase(genre)) {
                results.add(b);
            }
        }
        return results;
    }

    // --- User Search ---
    User searchUserById(int id) {
        for (User u : users) {
            if (u.userId == id) {
                return u;
            }
        }
        return null;
    }

    User searchUserByName(String name) {
        for (User u : users) {
            if (u.userName.equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }
}

// =================== TRANSACTION ===================
class BorrowTransaction {
    static int counter = 1;
    int transactionId;
    Book book;
    User user;
    Date borrowDate;
    Date returnDate;

    BorrowTransaction(Book b, User u) {
        this.transactionId = counter++;
        this.book = b;
        this.user = u;
        this.borrowDate = new Date();
        this.returnDate = null;
    }

    void returnBook() {
        this.returnDate = new Date();
    }

    void displayTransaction() {
        System.out.println("Transaction #" + transactionId + ": " +
                user.userName + " borrowed " + book.title +
                " on " + borrowDate +
                (returnDate != null ? " | Returned on " + returnDate : ""));
    }
}

// =================== MAIN ===================
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();


        Librarian librarian = new Librarian(1, "Admin", "9999999999");




        boolean running = true;
        while (running) {
            System.out.println("\n==== Library System ====");
            System.out.println("1. Book Management");
            System.out.println("2. User Management");
            System.out.println("3. Borrow / Return");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int mainChoice = sc.nextInt();
            sc.nextLine();

            switch (mainChoice) {
                case 1: // Book Management
                    System.out.println("\n--- Book Management ---");
                    System.out.println("1. Add Book");
                    System.out.println("2. Remove Book");
                    System.out.println("3. List Books");
                    System.out.println("4. Search Book");
                    System.out.print("Enter choice: ");
                    int bChoice = sc.nextInt();
                    sc.nextLine();

                    switch (bChoice) {
                        case 1:
                            System.out.print("Enter Title: ");
                            String t = sc.nextLine();
                            System.out.print("Enter Author: ");
                            String a = sc.nextLine();
                            System.out.print("Enter ISBN: ");
                            String i = sc.nextLine();
                            System.out.print("Enter Genre: ");
                            String g = sc.nextLine();
                            librarian.addBook(new Book(t, a, i, g), library);
                            break;

                        case 2:
                            System.out.print("Enter ISBN to remove: ");
                            String isbn = sc.nextLine();
                            librarian.removeBook(isbn, library);
                            break;

                        case 3:
                            for (Book b : library.books) {
                                b.display();
                            }
                            break;

                        case 4:
                            System.out.println("Search by: 1) Title 2) Author 3) ISBN 4) Genre");
                            int opt = sc.nextInt();
                            sc.nextLine();
                            Book book;
                            switch (opt) {
                                case 1:
                                    System.out.print("Enter Title: ");
                                    String title = sc.nextLine();
                                    book = library.searchBookByTitle(title);
                                    if (book != null) book.display(); else System.out.println("Not found!");
                                    break;
                                case 2:
                                    System.out.print("Enter Author: ");
                                    String author = sc.nextLine();
                                    List<Book> byAuthor = library.searchBooksByAuthor(author);
                                    if (byAuthor.isEmpty()) System.out.println("Not found!");
                                    else byAuthor.forEach(Book::display);
                                    break;
                                case 3:
                                    System.out.print("Enter ISBN: ");
                                    isbn = sc.nextLine();
                                    book = library.searchBookByISBN(isbn);
                                    if (book != null) book.display(); else System.out.println("Not found!");
                                    break;
                                case 4:
                                    System.out.print("Enter Genre: ");
                                    String genre = sc.nextLine();
                                    List<Book> byGenre = library.searchBooksByGenre(genre);
                                    if (byGenre.isEmpty()) System.out.println("Not found!");
                                    else byGenre.forEach(Book::display);
                                    break;
                            }
                            break;
                    }
                    break;

                case 2: // User Management
                    System.out.println("\n--- User Management ---");
                    System.out.println("1. Register User");
                    System.out.println("2. Search User");
                    System.out.println("3. List Users");
                    System.out.print("Enter choice: ");
                    int uChoice = sc.nextInt();
                    sc.nextLine();

                    switch (uChoice) {
                        case 1:
                            System.out.print("Enter User ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Enter Name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter Contact: ");
                            String contact = sc.nextLine();
                            librarian.registerUser(new User(id, name, contact), library);
                            break;

                        case 2:
                            System.out.println("Search User by: 1) ID 2) Name");
                            int opt = sc.nextInt();
                            sc.nextLine();
                            User foundUser = null;
                            if (opt == 1) {
                                System.out.print("Enter User ID: ");
                                id = sc.nextInt();
                                sc.nextLine();
                                foundUser = library.searchUserById(id);
                            } else if (opt == 2) {
                                System.out.print("Enter User Name: ");
                                String uname = sc.nextLine();
                                foundUser = library.searchUserByName(uname);
                            }
                            if (foundUser != null) {
                                System.out.println("User Found: " + foundUser.userName + " (ID: " + foundUser.userId + ")");
                                foundUser.displayBorrowedBooks();
                            } else {
                                System.out.println("User not found!");
                            }
                            break;

                        case 3:
                            for (User u : library.users) {
                                System.out.println("ID: " + u.userId + " | Name: " + u.userName + " | Contact: " + u.contact);
                            }
                            break;
                    }
                    break;

                case 3: // Borrowing and Returning
                    System.out.println("\n--- Borrow / Return ---");
                    System.out.println("1. Borrow Book");
                    System.out.println("2. Return Book");
                    System.out.println("3. View Transactions");
                    System.out.print("Enter choice: ");
                    int brChoice = sc.nextInt();
                    sc.nextLine();

                    switch (brChoice) {
                        case 1:
                            System.out.print("Enter User ID: ");
                            int uid = sc.nextInt();
                            sc.nextLine();
                            User borrower = library.searchUserById(uid);
                            if (borrower == null) {
                                System.out.println("User not found!");
                                break;
                            }
                            System.out.print("Enter Book Title: ");
                            String title = sc.nextLine();
                            Book book = library.searchBookByTitle(title);
                            if (book != null && book.status.equals("available")) {
                                borrower.borrowBook(book);
                                BorrowTransaction bt = new BorrowTransaction(book, borrower);
                                library.transactions.add(bt);
                            } else {
                                System.out.println("Book not available or not found!");
                            }
                            break;

                        case 2:
                            System.out.print("Enter User ID: ");
                            uid = sc.nextInt();
                            sc.nextLine();
                            borrower = library.searchUserById(uid);
                            if (borrower == null) {
                                System.out.println("User not found!");
                                break;
                            }
                            System.out.print("Enter Book Title to Return: ");
                            title = sc.nextLine();
                            book = library.searchBookByTitle(title);
                            if (book != null) {
                                borrower.returnBook(book);
                                for (BorrowTransaction t : library.transactions) {
                                    if (t.book == book && t.user == borrower && t.returnDate == null) {
                                        t.returnBook();
                                        break;
                                    }
                                }
                            } else {
                                System.out.println("Book not found!");
                            }
                            break;

                        case 3:
                            for (BorrowTransaction t : library.transactions) {
                                t.displayTransaction();
                            }
                            break;
                    }
                    break;

                case 4:
                    running = false;
                    System.out.println("Exiting Library System...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        }
        sc.close();
    }
}
