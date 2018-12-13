/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package travel.office;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TripRepository tripRepository;

	@Before
	public void deleteAllBeforeTests() throws Exception {
		customerRepository.deleteAll();
		tripRepository.deleteAll();
	}

	@Test
	public void shouldReturnRepositoryIndexCustomer() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.customers").exists());
	}

	@Test
	public void shouldCreateEntityCustomer() throws Exception {

		mockMvc.perform(post("/customers").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("customers/")));
	}

	@Test
	public void shouldRetrieveEntityCustomer() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/customers").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Frodo")).andExpect(
						jsonPath("$.lastName").value("Baggins"));
	}

	@Test
	public void shouldQueryEntityCustomer() throws Exception {

		mockMvc.perform(post("/customers").content(
				"{ \"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated());

		mockMvc.perform(
				get("/customers/search/findByLastName?name={name}", "Baggins")).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.customers[0].firstName").value(
										"Frodo"));
	}

	@Test
	public void shouldUpdateEntityCustomer() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/customers").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bilbo")).andExpect(
						jsonPath("$.lastName").value("Baggins"));
	}

	@Test
	public void shouldPartiallyUpdateEntityCustomer() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/customers").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"firstName\": \"Bilbo Jr.\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bilbo Jr.")).andExpect(
						jsonPath("$.lastName").value("Baggins"));
	}

	@Test
	public void shouldDeleteEntityCustomer() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/customers").content(
				"{ \"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}

	@Test
	public void shouldReturnRepositoryIndexTrip() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.trips").exists());
	}

	@Test
	public void shouldCreateEntityTrip() throws Exception {

		mockMvc.perform(post("/trips").content(
				"{\"dest\": \"Mordor\", \"time\":\"ASAP\"}")).andExpect(
				status().isCreated()).andExpect(
				header().string("Location", containsString("trips/")));
	}

	@Test
	public void shouldRetrieveEntityTrip() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/trips").content(
				"{\"dest\": \"Mordor\", \"time\":\"ASAP\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.dest").value("Mordor")).andExpect(
				jsonPath("$.time").value("ASAP"));
	}

	@Test
	public void shouldQueryEntityTrip() throws Exception {

		mockMvc.perform(post("/trips").content(
				"{ \"dest\": \"Mordor\", \"time\":\"ASAP\"}")).andExpect(
				status().isCreated());

		mockMvc.perform(
				get("/trips/search/findByDest?dest={dest}", "Mordor")).andExpect(
				status().isOk()).andExpect(
				jsonPath("$._embedded.trips[0].time").value(
						"ASAP"));
	}

	@Test
	public void shouldUpdateEntityTrip() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/trips").content(
				"{\"dest\": \"Mordor\", \"time\":\"ASAP\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"dest\": \"Isengard\", \"time\":\"now\"}")).andExpect(
				status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.dest").value("Isengard")).andExpect(
				jsonPath("$.time").value("now"));
	}

	@Test
	public void shouldPartiallyUpdateEntityTrip() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/trips").content(
				"{\"dest\": \"Mordor\", \"time\":\"ASAP\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"dest\": \"Minas Tirith\"}")).andExpect(
				status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.dest").value("Minas Tirith")).andExpect(
				jsonPath("$.time").value("ASAP"));
	}

	@Test
	public void shouldDeleteEntityTrip() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/trips").content(
				"{ \"dest\": \"Isengard\", \"time\":\"now\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}
}