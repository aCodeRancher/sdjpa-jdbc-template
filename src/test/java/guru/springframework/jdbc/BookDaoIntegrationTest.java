package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.dao.BookDaoImpl;
import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(BookDaoImpl.class)
public class BookDaoIntegrationTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testDeleteBook() {
        Book book = new Book();
        book.setTitle("Hibernate and Spring Data JPA");
        book.setIsbn("5710");
        book.setAuthorId(1L);
        book.setPublisher("Spring Framework Guru");

        Book saved =  bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookDao.getById(saved.getId());
        });
    }

    @Test
    void testUpdateAuthor() {
        Book book = new Book();
        book.setPublisher("Spring Framework Guru");
        book.setIsbn("5710");
        book.setAuthorId(1L);
        book.setTitle("Hibernate and Spring Data JPA");

        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("Hibernate and Spring Data JPA Beginner to Guru");
        Book updated = bookDao.updateBook(saved);

        assertThat(updated.getTitle()).isEqualTo("Hibernate and Spring Data JPA Beginner to Guru");
    }

    @Test
    void testInsertBook() {
       Book book = new Book();
       book.setTitle("Hibernate and Spring Data JPA");
       book.setPublisher("Spring Framework Guru");
       book.setIsbn("5710");
       book.setAuthorId(1L);
       Book saved = bookDao.saveNewBook(book);
       System.out.println("New Id is: " + saved.getId());

        assertThat(saved).isNotNull();
    }

    @Test
    void testGetBookByTitle() {
        Book book = bookDao.findBookByTitle("Spring in Action, 5th Edition");

        assertThat(book).isNotNull();
    }

    @Test
    void testGetBook() {

        Book book = bookDao.getById(1l);

        assertThat(book.getId()).isNotNull();
    }
}
