package com.app.inventory.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.inventory.error.ErrorMessageConstant;
import com.app.inventory.helper.ResponseHelper;
import com.app.inventory.model.request.CreateOrderRequest;
import com.app.inventory.model.request.UpdateOrderRequest;
import com.app.inventory.model.response.OrderResponse;
import com.app.inventory.model.response.WebResponse;
import com.app.inventory.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/api" })
public class OrderController {

	private final OrderService service;
	private static final String PROP_OREDR = "order ";

	public OrderController(OrderService service) {
		super();
		this.service = service;
	}

	@PostMapping(value = "/order")
	public ResponseEntity<WebResponse<String>> add(@Valid @RequestBody CreateOrderRequest request) {
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_OREDR + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@PutMapping(value = "/order")
	public ResponseEntity<WebResponse<String>> edit(@RequestBody UpdateOrderRequest request) {
		service.edit(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_OREDR + ErrorMessageConstant.HAS_BEEN_EDITED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/order/{id}")
	public ResponseEntity<WebResponse<String>> delete(@PathVariable String id) {
		service.delete(id);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_OREDR + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/order/{id}")
	public ResponseEntity<WebResponse<OrderResponse>> getById(@PathVariable String id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/order")
	public ResponseEntity<WebResponse<Page<OrderResponse>>> getAllItems(Pageable pageable) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAllItems(pageable)));
	}

}
