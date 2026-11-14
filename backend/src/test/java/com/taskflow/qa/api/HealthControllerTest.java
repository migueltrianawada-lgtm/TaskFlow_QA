package com.taskflow.qa.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.taskflow.qa.TaskFlowApplication;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = TaskFlowApplication.class)
@AutoConfigureMockMvc
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthEndpointReportsServiceAsAvailable() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void deniesBusinessRoutesWithoutBearerToken() throws Exception {
        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void demoAdminCanAuthenticateAndUseProtectedApi() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"admin@taskflow.local","senha":"TaskFlow-Demo-2026!"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        String token = com.jayway.jsonpath.JsonPath.read(response, "$.accessToken");

        mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@taskflow.local"));
        mockMvc.perform(get("/api/recursos").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"admin@taskflow.local","senha":"wrong-password"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createsReservationAndRejectsAnOverlappingRequest() throws Exception {
        String token = demoAdminToken();
        String resourceResponse = mockMvc.perform(post("/api/recursos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Sala concorrência","tipo":"SALA","localizacao":"Bloco A",
                                 "capacidade":20,"restrito":false}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int resourceId = com.jayway.jsonpath.JsonPath.read(resourceResponse, "$.id");
        String body = """
                {"finalidade":"Aula de teste","inicio":"2027-01-20T13:00:00Z",
                 "fim":"2027-01-20T14:00:00Z","recursoIds":[%d],"quantidades":{}}
                """.formatted(resourceId);

        mockMvc.perform(post("/api/reservas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APROVADA"));

        mockMvc.perform(post("/api/reservas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("ocupado")));
    }

    @Test
    void adminCanCreateUsersButRequesterCannotManageResources() throws Exception {
        String adminToken = demoAdminToken();
        mockMvc.perform(post("/api/usuarios")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Solicitante de teste","email":"solicitante@test.local",
                                 "senha":"Senha-de-teste-2026!","perfil":"SOLICITANTE"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.perfis[0]").value("SOLICITANTE"));
        String login = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"solicitante@test.local","senha":"Senha-de-teste-2026!"}
                                """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String requesterToken = com.jayway.jsonpath.JsonPath.read(login, "$.accessToken");

        mockMvc.perform(post("/api/recursos")
                        .header("Authorization", "Bearer " + requesterToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Recurso não autorizado","tipo":"SALA","capacidade":12}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void simultaneousRequestsForOneResourceConfirmAtMostOneReservation() throws Exception {
        String token = demoAdminToken();
        String resourceResponse = mockMvc.perform(post("/api/recursos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Sala simultânea","tipo":"SALA","capacidade":20,"restrito":false}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int resourceId = com.jayway.jsonpath.JsonPath.read(resourceResponse, "$.id");
        String body = """
                {"finalidade":"Reserva concorrente","inicio":"2027-01-21T13:00:00Z",
                 "fim":"2027-01-21T14:00:00Z","recursoIds":[%d],"quantidades":{}}
                """.formatted(resourceId);
        var executor = Executors.newFixedThreadPool(2);
        var ready = new CountDownLatch(2);
        var start = new CountDownLatch(1);
        try {
            var first = executor.submit(() -> reservarConcorrentemente(token, body, ready, start));
            var second = executor.submit(() -> reservarConcorrentemente(token, body, ready, start));
            assertTrue(ready.await(5, TimeUnit.SECONDS));
            start.countDown();
            int firstStatus = first.get(20, TimeUnit.SECONDS);
            int secondStatus = second.get(20, TimeUnit.SECONDS);
            assertTrue((firstStatus == 201 && secondStatus == 409)
                    || (firstStatus == 409 && secondStatus == 201),
                    "Uma solicitação deve ser confirmada e a outra rejeitada como conflito.");
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void utilizationReportFiltersAndAggregatesReservationsInRequestedPeriod() throws Exception {
        String token = demoAdminToken();
        String resourceResponse = mockMvc.perform(post("/api/recursos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"Sala relatório de utilização","tipo":"SALA",
                                 "localizacao":"Bloco B","capacidade":30,"restrito":false}
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int resourceId = com.jayway.jsonpath.JsonPath.read(resourceResponse, "$.id");
        mockMvc.perform(post("/api/reservas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"finalidade":"Reserva para relatório","inicio":"2040-06-15T10:00:00Z",
                                 "fim":"2040-06-15T12:00:00Z","recursoIds":[%d],"quantidades":{}}
                                """.formatted(resourceId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/relatorios/utilizacao")
                        .header("Authorization", "Bearer " + token)
                        .param("inicio", "2040-06-15T09:00:00Z")
                        .param("fim", "2040-06-15T13:00:00Z"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservas").value(1))
                .andExpect(jsonPath("$.horasAlocadas").value(2.0))
                .andExpect(jsonPath("$.recursosUtilizados").value(1))
                .andExpect(jsonPath("$.utilizacaoPorRecurso[0].recursoId").value(resourceId))
                .andExpect(jsonPath("$.utilizacaoPorRecurso[0].reservas").value(1))
                .andExpect(jsonPath("$.utilizacaoPorRecurso[0].horas").value(2.0));
    }

    private int reservarConcorrentemente(String token, String body,
                                         CountDownLatch ready, CountDownLatch start) throws Exception {
        ready.countDown();
        assertTrue(start.await(5, TimeUnit.SECONDS));
        return mockMvc.perform(post("/api/reservas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn().getResponse().getStatus();
    }

    private String demoAdminToken() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"admin@taskflow.local","senha":"TaskFlow-Demo-2026!"}
                                """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return com.jayway.jsonpath.JsonPath.read(response, "$.accessToken");
    }
}
