package com.app.inventory.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.inventory.persistence.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	Optional<Order> findTopByOrderByOrderNoDesc();
}
