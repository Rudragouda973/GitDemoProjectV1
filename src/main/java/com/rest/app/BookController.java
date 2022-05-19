package com.rest.app;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/book")
public class BookController {

	@Autowired
	private BookRepository bookRepository;
	
	@GetMapping("/findAllBooks")
	public List<Book> getAllBookRecords(){
		return bookRepository.findAll();
	}
	
	@GetMapping("/findBookById/{bookId}")
	public Book getBookById(@PathVariable("bookId") Long bookId) {
		return bookRepository.findById(bookId).get();
	}
	
	@PostMapping("/saveBook")
	public Book createBookRecord(@Validated @RequestBody Book bookRecord) {
		 return bookRepository.save(bookRecord);
	}
	
	@PutMapping("/updateBook")
	public Book updateBookRecord(@Validated @RequestBody Book bookRecord) throws NotFoundException {
		  
		if(bookRecord == null || bookRecord.getBookId() == null) {
			throw new NotFoundException("BookRecord or ID must not be null");
		}
		Optional<Book> optionalBook = bookRepository.findById(bookRecord.getBookId());
		if(!optionalBook.isPresent()) {
			throw new NotFoundException("Book with Id: "+bookRecord.getBookId()+ " does not exists..");
		}
		
		Book existingBookRecord = optionalBook.get();
		existingBookRecord.setName(bookRecord.getName());
		existingBookRecord.setSummary(bookRecord.getSummary());
		existingBookRecord.setRating(bookRecord.getRating());
		return bookRepository.save(existingBookRecord);
	}
	
	@DeleteMapping("/deleteBook/{bookId}")
	public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException {
		if(!bookRepository.findById(bookId).isPresent()) {
			throw new NotFoundException("BookId: "+bookId+ " not present");
		}
		bookRepository.deleteById(bookId);
	}
}

