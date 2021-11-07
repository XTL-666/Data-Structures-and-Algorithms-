package demo.liang.service;

import demo.liang.dao.BookDao;
import demo.liang.pojo.Books;

import java.util.List;

public class BookServiceImpl implements BookService{
    private BookDao bookMapper;

    public void setBookMapper(BookDao bookMapper) {
        this.bookMapper = bookMapper;
    }

    public int addBook(Books book) {
        return bookMapper.addBook(book);
    }

    public int deleteBookById(int id) {
        return bookMapper.deleteBookById(id);
    }

    public int updateBook(Books books) {
        return bookMapper.updateBook(books);
    }

    public Books queryBookById(int id) {
        return bookMapper.queryBookById(id);
    }

    public List<Books> queryAllBook() {
        return bookMapper.queryAllBook();
    }
}
