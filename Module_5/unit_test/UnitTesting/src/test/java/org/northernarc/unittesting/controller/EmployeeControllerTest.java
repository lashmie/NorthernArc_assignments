package org.northernarc.unittesting.controller;

import org.junit.jupiter.api.Test;
import org.northernarc.unittesting.dto.EmployeeRequestDTO;
import org.northernarc.unittesting.dto.EmployeeResponseDTO;
import org.northernarc.unittesting.exception.EmployeeNotFoundException;
import org.northernarc.unittesting.exception.GlobalExceptionHandler;
import org.northernarc.unittesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    EmployeeService service;

    @Test
    @WithMockUser
    void testGetEmployeeSuccess() throws Exception {
        EmployeeResponseDTO emp = new EmployeeResponseDTO(1L, "yuva", 100000.0);

        when(service.getById(1L)).thenReturn(emp);

        mockMvc.perform(
                        get("/api/employees/1")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("yuva"))
                .andExpect(jsonPath("$.salary").value(100000));
    }

    @Test
    @WithMockUser
    void testGetEmployeeNotFoundException() throws Exception {
        when(service.getById(999L))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id: 999"));

        mockMvc.perform(
                        get("/api/employees/999")
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"));
    }


    @WithMockUser
    @Test
    void deleteById() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/employees/1").with(csrf()))
                .andExpect(status().isNoContent()); // 204 is typical for delete
    }


    @WithMockUser
    @Test
    void testAddEmployee() throws Exception {
        EmployeeResponseDTO empResponse = new EmployeeResponseDTO(1L, "Rahul", 12000.0);

        when(service.create(any(EmployeeRequestDTO.class))).thenReturn(empResponse);

        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Rahul\",\"salary\":12000}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.salary").value(12000.0));
    }

}
