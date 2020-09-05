package person.person.spring5webfluxrest.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import person.person.spring5webfluxrest.domain.Category;
import person.person.spring5webfluxrest.domain.Vendor;
import person.person.spring5webfluxrest.repositories.CategoryRepository;
import person.person.spring5webfluxrest.repositories.VendorRepository;

import java.util.UUID;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        categoryRepository.deleteAll().block();
        vendorRepository.deleteAll().block();

        loadCategories();
        loadVendors();
    }

    private void loadCategories() {
        categoryRepository.save(Category.builder().description("Fruits").build()).block();
        categoryRepository.save(Category.builder().description("Nuts").build()).block();
        categoryRepository.save(Category.builder().description("Beans").build()).block();
        categoryRepository.save(Category.builder().description("Meats").build()).block();
        categoryRepository.save(Category.builder().description("Eggs").build()).block();

        System.out.println("Categories loaded: " + categoryRepository.count().block());
    }

    private void loadVendors() {
        Vendor cars = new Vendor();
        cars.setFirstName("Mike");
        cars.setLastName("Shelby");

        Vendor planes = new Vendor();
        planes.setFirstName("Ihor");
        planes.setLastName("Sikorsky");

        vendorRepository.save(cars).block();
        vendorRepository.save(planes).block();

        System.out.println("Vendors loaded: " + vendorRepository.count().block());
    }
}
