package com.qa.musichwa.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.musichwa.domain.Music;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:testschema.sql", "classpath:testdata.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class MusicControllerIntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Test
	public void createTest() throws Exception {
		Music entry = new Music("Single", "Little More Love", "AJ Tracey", 2021);
		Music result = new Music(2L, "Single", "Little More Love", "AJ Tracey", 2021);
		String entryAsJSON = this.mapper.writeValueAsString(entry);
		String resultAsJSON = this.mapper.writeValueAsString(result);
		
		mvc.perform(post("/api/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(entryAsJSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(resultAsJSON));	
	}
	
	@Test
	public void getAllTest() throws Exception {
		Music entry = new Music(1L, "Album", "Flu Game", "AJ Tracey", 2021);
		List<Music> output = new ArrayList<>();
		output.add(entry);
		String outputAsJSON = this.mapper.writeValueAsString(output);
		
		mvc.perform(get("/api/getAll")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(outputAsJSON));
	}
	
	@Test
	public void getOneTest() throws Exception {
		Music entry = new Music(1L, "Album", "Flu Game", "AJ Tracey", 2021);
		String entryAsJSON = this.mapper.writeValueAsString(entry);
		
		mvc.perform(get("/api/getOne/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(entryAsJSON));
	}
	
	@Test
	public void deleteSuccessTest() throws Exception {
		mvc.perform(delete("/api/remove/1")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void updateTest() throws Exception {
		Music entry = new Music("Single", "Little More Love", "AJ Tracey", 2021);
		String entryAsJSON = this.mapper.writeValueAsString(entry);
		Music result = new Music(1L, "Single", "Little More Love", "AJ Tracey", 2021);
		String resultAsJSON = this.mapper.writeValueAsString(result);
		
		mvc.perform(put("/api/update/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(entryAsJSON))
			.andExpect(status().isAccepted())
			.andExpect(content().json(resultAsJSON));
	}
}
