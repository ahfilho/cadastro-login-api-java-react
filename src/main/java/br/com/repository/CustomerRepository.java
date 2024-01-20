package br.com.repository;

import br.com.entity.CustomerSale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerSale, Long> {
}
