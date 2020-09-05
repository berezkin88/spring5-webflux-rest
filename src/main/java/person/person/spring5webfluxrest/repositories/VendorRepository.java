package person.person.spring5webfluxrest.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import person.person.spring5webfluxrest.domain.Vendor;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
