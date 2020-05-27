package ge.bog.practice.sql;

import java.sql.*;


public class DBInsert {

    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/books_db";
    private static Connection conn = null;



    public static void addAuthor(String author) {
        String sql = "insert into books_db.authors (authorname) values (?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, author);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            sfClose(preparedStatement);
        }
    }

    public static void addAuthorAndBook(String authorName, String bookName) {
        String sql = "insert into books_db.author_to_books (bookname, authorname) values (?, ?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, authorName);
            preparedStatement.setString(2, bookName);
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
            addAuthor("shota");
            addAuthorAndBook("shota", "book");
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
