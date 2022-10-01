package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class BookDaoImpl implements BookDao{

    private JdbcTemplate jdbcTemplate;

    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
         this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
      return jdbcTemplate.queryForObject("SELECT * FROM book where id = ?",
              getRowMapper(),id);
    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE title= ?",
                getRowMapper(), title );
    }

    @Override
    public Book saveNewBook(Book book){
      jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUE (?, ? , ? , ?)",
             book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthorId());
      Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
      return this.getById(createdId);
    }

    @Override
    public Book updateBook(Book book){
     jdbcTemplate.update("UPDATE book SET title=? WHERE id=?",
             book.getTitle(),book.getId());
     return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id){
      jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    private RowMapper<Book> getRowMapper(){
        return new BookMapper();
    }
}
