package person.person.spring5webfluxrest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import person.person.spring5webfluxrest.domain.Category;
import person.person.spring5webfluxrest.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

public class CategoryControllerTest {

    public static final String DESCRIPTION = "Cat1";

    WebTestClient webTestClient;
    private CategoryRepository categoryRepository;
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() throws Exception {
        categoryRepository = mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);

        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(categoryRepository.findAll())
            .willReturn(Flux.just(Category.builder().description(DESCRIPTION).build(),
                Category.builder().description("Cat2").build()));

        webTestClient.get()
            .uri(CategoryController.BASE_URL)
            .exchange()
            .expectBodyList(Category.class)
            .hasSize(2);
    }

    @Test
    public void getById() {
        BDDMockito.given(categoryRepository.findById(anyString()))
            .willReturn(Mono.just(Category.builder().description(DESCRIPTION).build()));

        webTestClient.get()
            .uri(CategoryController.BASE_URL + "/asd")
            .exchange()
            .expectBody(Category.class)
            .value(Category::getDescription, equalTo(DESCRIPTION));
    }
}
