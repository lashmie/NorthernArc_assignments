package org.northernarc.unittesting;

import org.junit.jupiter.api.Test;
import org.northernarc.unittesting.controller.EmployeeController;
import org.northernarc.unittesting.dto.EmployeeResponseDTO;
import org.northernarc.unittesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void getEmployee() throws Exception {
        EmployeeResponseDTO saved = EmployeeResponseDTO.builder()
                .id(1L)
                .name("Laaaa")
                .salary(78000.0)
                .build();
        when(employeeService.getById(1L)).thenReturn(saved);
        mockMvc.perform(get("/api/employees/1", saved.getId()))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Laaaa"))
                .andExpect(jsonPath("$.salary").value(78000.0));
    }



}

