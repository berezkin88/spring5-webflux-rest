package person.person.spring5webfluxrest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import person.person.spring5webfluxrest.domain.Category;
import person.person.spring5webfluxrest.domain.Vendor;
import person.person.spring5webfluxrest.repositories.CategoryRepository;
import person.person.spring5webfluxrest.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class VendorControllerTest {

    public static final String FIRST_NAME = "Name";
    public static final String LAST_NAME = "surname";
    WebTestClient webTestClient;

    @Mock
    private VendorRepository vendorRepository;
    @InjectMocks
    private VendorController vendorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        BDDMockito.given(vendorRepository.findAll())
            .willReturn(Flux.just(Vendor.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build(),
                Vendor.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build()));

        webTestClient.get()
            .uri(VendorController.BASE_URL)
            .exchange()
            .expectBodyList(Vendor.class)
            .hasSize(2);
    }

    @Test
    void getById() {
        BDDMockito.given(vendorRepository.findById(anyString()))
            .willReturn(Mono.just(Vendor.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build()));

        webTestClient.get()
            .uri(VendorController.BASE_URL + "/asd")
            .exchange()
            .expectBody(Vendor.class)
            .value(Vendor::getFirstName, equalTo(FIRST_NAME))
            .value(Vendor::getLastName, equalTo(LAST_NAME));
    }

    @Test
    void create() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
            .willReturn(Flux.just(Vendor.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build()));

        webTestClient.post()
            .uri(VendorController.BASE_URL)
            .body(Mono.just(Vendor.builder().build()), Vendor.class)
            .exchange()
            .expectStatus()
            .isCreated();
    }

    @Test
    void update() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
            .willReturn(Mono.just(Vendor.builder().build()));

        webTestClient.put()
            .uri(VendorController.BASE_URL + "/asd")
            .body(Mono.just(Category.builder().build()), Vendor.class)
            .exchange()
            .expectStatus()
            .isOk();
    }

    @Test
    void patch() {
        given(vendorRepository.findById(anyString()))
            .willReturn(Mono.just(Vendor.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder()
                                                            .firstName("Some first name")
                                                            .lastName("Some last name")
                                                            .build());

        webTestClient.patch()
            .uri(VendorController.BASE_URL + "/asd")
            .body(vendorToSaveMono, Vendor.class)
            .exchange()
            .expectStatus()
            .isOk();

        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    void patchNoChange() {
        given(vendorRepository.findById(anyString()))
            .willReturn(Mono.just(Vendor.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build()));

        Mono<Vendor> vendorToSaveMono
            = Mono.just(Vendor.builder().firstName(FIRST_NAME).lastName(LAST_NAME).build());

        webTestClient.patch()
            .uri(VendorController.BASE_URL + "/asd")
            .body(vendorToSaveMono, Vendor.class)
            .exchange()
            .expectStatus()
            .isOk();

        verify(vendorRepository, never()).save(any(Vendor.class));
    }
}
