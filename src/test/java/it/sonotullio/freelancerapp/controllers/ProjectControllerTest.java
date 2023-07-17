package it.sonotullio.freelancerapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sonotullio.freelancerapp.common.Status;
import it.sonotullio.freelancerapp.model.Project;
import it.sonotullio.freelancerapp.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.stream.Stream;

import static it.sonotullio.freelancerapp.controllers.CustomerControllerTest.getCustomer;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectRepository projectService;

    private static Project getProject() {
        return Project.builder()
                .id("1")
                .name("My project")
                .description("My project description")
                .customer(getCustomer())
                .status(Status.DONE)
                .workingHours(10)
                .build();
    }

    private static Stream<Project> getInvalidProjects() {
        Project project = getProject();

        return Stream.of(
                project.toBuilder().name(null).build(),
                project.toBuilder().name("").build(),
                project.toBuilder().description(null).build(),
                project.toBuilder().description("").build(),
                project.toBuilder().customer(null).build(),
                project.toBuilder().status(null).build(),
                project.toBuilder().workingHours(null).build()
        );
    }

    @Test
    void create() throws Exception {
        // given
        Project projectToSave = getProject();
        projectToSave.setId(null);

        Project returnedProject = getProject();

        // when
        when(projectService.save(projectToSave)).thenReturn(returnedProject);

        mvc.perform(
                post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectToSave))
        )
                // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(returnedProject.getId())))
        .andExpect(jsonPath("$.name", is(returnedProject.getName())))
        .andExpect(jsonPath("$.description", is(returnedProject.getDescription())))
        .andExpect(jsonPath("$.customer.id", is(returnedProject.getCustomer().getId())))
        .andExpect(jsonPath("$.status", is(returnedProject.getStatus())))
        .andExpect(jsonPath("$.workingHours", is(returnedProject.getWorkingHours())));

    }

    @Test
    void createKo() throws Exception {
        // given
        Stream<Project> invalidProjects = getInvalidProjects();

        // when
        mvc.perform(
                        post("/api/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(invalidProjects))
                )
                // then
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById() throws Exception {
        // given
        Project project = getProject();

        // when
        when(projectService.findById(project.getId())).thenReturn(java.util.Optional.of(project));
        mvc.perform(
                get("/api/projects/{id}", project.getId())
        )
                // then
                .andExpect(status().isOk());
    }

    @Test
    void findByIdNotFound() throws Exception {
        String id = "1";

        when(projectService.findById(id)).thenReturn(Optional.empty());

        mvc.perform(get("/api/projects/" + id))
                .andExpect(status().isNotFound());
    }
}