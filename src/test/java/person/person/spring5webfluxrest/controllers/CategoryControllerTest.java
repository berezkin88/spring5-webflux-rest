package person.person.spring5webfluxrest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import person.person.spring5webfluxrest.domain.Category;
import person.person.spring5webfluxrest.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
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
    void list() {
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
    void getById() {
        BDDMockito.given(categoryRepository.findById(anyString()))
            .willReturn(Mono.just(Category.builder().description(DESCRIPTION).build()));

        webTestClient.get()
            .uri(CategoryController.BASE_URL + "/asd")
            .exchange()
            .expectBody(Category.class)
            .value(Category::getDescription, equalTo(DESCRIPTION));
    }

    @Test
    void create() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
            .willReturn(Flux.just(Category.builder().description(DESCRIPTION).build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().build());

        webTestClient.post()
            .uri(CategoryController.BASE_URL)
            .body(categoryToSaveMono, Category.class)
            .exchange()
            .expectStatus()
            .isCreated();
    }

    @Test
    void update() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
            .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description("Some cat").build());

        webTestClient.put()
            .uri(CategoryController.BASE_URL + "/asd")
            .body(categoryToSaveMono, Category.class)
            .exchange()
            .expectStatus()
            .isOk();
    }
}
