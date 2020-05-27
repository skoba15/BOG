package ge.bog.practice.sql;

import java.sql.*;


public class DBInsert {

    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/books_db";
    private static Connection conn = null;



    public static void addAuthor(Author author) {
        String sql = "insert into books_db.authors (firstname, lastname) values (?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            sfClose(preparedStatement);
        }
    }


    private static int getAuthorId(Author author) {
        String sql = "select * from books_db.authors where firstname = ? and lastname = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt(1);
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        } finally {
            sfClose(preparedStatement);
        }
    }



    public static void addBook(Book book, Author author) {
        String sql = "insert into books_db.books (bookname, pagesnumber, idauthor) values (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        int authorId = getAuthorId(author);
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setInt(2, book.getPagesNumber());
            preparedStatement.setInt(3, authorId);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            sfClose(preparedStatement);
        }
    }



    private static void sfClose(AutoCloseable ac) {
        try {
            if (ac != null) {
                ac.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            Author author = new Author("Mark", "Twain");
            Book book = new Book("Tom Sawyer", 200, author);
            addAuthor(author);
            addBook(book, author);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            sfClose(conn);
        }

    }



}
