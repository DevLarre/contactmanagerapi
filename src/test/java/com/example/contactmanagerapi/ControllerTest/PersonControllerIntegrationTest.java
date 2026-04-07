package com.example.contactmanagerapi.ControllerTest;

import com.example.contactmanagerapi.dto.*;
import com.example.contactmanagerapi.enums.PhoneType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PersonControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private PersonCreateDto buildCreateDto(String name, String email) {
        return new PersonCreateDto(
                name, email,
                List.of(new PhoneCreateDto("(11) 91234-5678", PhoneType.CELULAR)),
                List.of(new AddressCreateDto("Rua A", "10", "Centro",
                        "São Paulo", "SP", "01001-000"))
        );
    }

    private String cadastrar(PersonCreateDto dto) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asText();
    }

    @Test
    @DisplayName("POST /api/persons — deve cadastrar contato e retornar 201")
    void create_retorna201() throws Exception {
        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                buildCreateDto("João Silva", "joao@email.com"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.status").value("ATIVO"))
                .andExpect(jsonPath("$.phones").isArray())
                .andExpect(jsonPath("$.addresses").isArray());
    }

    @Test
    @DisplayName("POST /api/persons — deve retornar 409 para email duplicado")
    void create_emailDuplicado_retorna409() throws Exception {
        cadastrar(buildCreateDto("João Silva", "joao@email.com"));

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                buildCreateDto("Outro Nome", "joao@email.com"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email já cadastrado"));
    }

    @Test
    @DisplayName("POST /api/persons — deve retornar 400 para dados inválidos")
    void create_dadosInvalidos_retorna400() throws Exception {
        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/persons/{id} — deve retornar contato cadastrado")
    void findById_retorna200() throws Exception {
        String id = cadastrar(buildCreateDto("Maria Souza", "maria@email.com"));

        mockMvc.perform(get("/api/persons/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Maria Souza"));
    }

    @Test
    @DisplayName("GET /api/persons/{id} — deve retornar 404 para ID inexistente")
    void findById_notFound_retorna404() throws Exception {
        mockMvc.perform(get("/api/persons/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Contato não encontrado"));
    }

    @Test
    @DisplayName("PATCH /api/persons/{id}/status — deve alternar status")
    void toggleStatus_alternaDeAtivoParaInativo() throws Exception {
        String id = cadastrar(buildCreateDto("Carlos Lima", "carlos@email.com"));

        mockMvc.perform(patch("/api/persons/" + id + "/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INATIVO"));

        mockMvc.perform(patch("/api/persons/" + id + "/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATIVO"));
    }

    @Test
    @DisplayName("GET /api/persons/search/name — deve filtrar por nome")
    void findByName_retornaFiltrado() throws Exception {
        cadastrar(buildCreateDto("Ana Paula", "ana@email.com"));
        cadastrar(buildCreateDto("Bruno Costa", "bruno@email.com"));

        mockMvc.perform(get("/api/persons/search/name").param("name", "ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Ana Paula"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("DELETE /api/persons/{id} — deve deletar e retornar 204")
    void delete_retorna204() throws Exception {
        String id = cadastrar(buildCreateDto("Pedro Alves", "pedro@email.com"));

        mockMvc.perform(delete("/api/persons/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/persons/" + id))
                .andExpect(status().isNotFound());
    }
}