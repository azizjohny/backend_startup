package uz.xnarx.xnarx.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.xnarx.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
