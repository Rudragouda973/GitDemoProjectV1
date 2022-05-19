package com.rest.app;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
	
	private MockMvc mockMvc;
	
	ObjectMapper objectMapper =  new ObjectMapper();
	ObjectWriter objectWritter = objectMapper.writer();
	
	@Mock
	private BookRepository bookRepository;
	
	@InjectMocks
	private BookController bookController;
	
	Book RECORD_1 = new Book(1l,"Atomic Habits","how to build better habits",5);
	Book RECORD_2 = new Book(2l, "Thinking Fast and slow", "How to create good  mental models about thinking",4);
	Book RECORD_3 = new Book(3l, "Groking Algorithem", "Learn Algorithems the fun way",5);
	
	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
	}
	
	@Test 
	public void getAllRecord_success() throws Exception {
	
		List<Book> records =  new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
		
		Mockito.when(bookRepository.findAll()).thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
					    .get("/book/findAllBooks")
					    .contentType(MediaType.APPLICATION_JSON))
					    .andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$[0].bookId").value("1"))
						.andExpect(jsonPath("$[1].bookId").value("2"))
						.andExpect(jsonPath("$[2].bookId").value("3"))
						.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
						.andExpect(jsonPath("$[2].name",is("Groking Algorithem")))
						.andExpect(jsonPath("$[1].name",is("Thinking Fast and slow")));
	}
	
	@Test
	public void getBookById_success() throws Exception{
			
		Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.of(RECORD_1));
			
		mockMvc.perform(MockMvcRequestBuilders
					    .get("/book/findBookById/1")
					    .contentType(MediaType.APPLICATION_JSON))
					    .andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.bookId").value("1"))
						.andExpect(jsonPath("$", notNullValue()))
						/* .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1))) */
						.andExpect(jsonPath("$.name",is("Atomic Habits")));
	}
	
	@Test 
	public void createRecord_success() throws Exception {
				Book record = Book.builder()
							  .bookId(4l)
							  .name("intruduction to java")
							  .summary("The name but longer")
							  .rating(5)
							  .build();

				Mockito.when(bookRepository.save(record)).thenReturn(record);
				
				String content = objectWritter.writeValueAsString(record);
				
				MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book/saveBook")
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON)
								.content(content);
						
				mockMvc.perform(mockRequest)
								.andExpect(status().isOk())
								.andExpect(jsonPath("$", notNullValue()))
								.andExpect(jsonPath("$.name",is("intruduction to java")));
	}
	
	@Test
	public void updateBookRecord_success() throws Exception {
		
		Book updateRecord = Book.builder()
								.bookId(1l)
								.name("Update Book Name")
								.summary("Update Summary")
								.rating(4)
								.build();
		
		Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(java.util.Optional.ofNullable(RECORD_1));
		Mockito.when(bookRepository.save(updateRecord)).thenReturn(updateRecord);
		
		
		String updatedContent = objectWritter.writeValueAsString(updateRecord);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book/updateBook")
							.contentType(MediaType.APPLICATION_JSON)
							.accept(MediaType.APPLICATION_JSON)
							.content(updatedContent);
		
		mockMvc.perform(mockRequest)
							.andExpect(status().isOk())
							.andExpect(jsonPath("$", notNullValue()))
							.andExpect(jsonPath("$.name",is("Update Book Name")));
	}
	
	@Test
	public void deleteBookById_success() throws Exception {
		
		Mockito.when(bookRepository.findById(RECORD_2.getBookId())).thenReturn(Optional.of(RECORD_2));
		
		mockMvc.perform(MockMvcRequestBuilders
						.delete("/book/deleteBook/2")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	}
	
}
