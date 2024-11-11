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
import com.app.inventory.model.request.CreateInventoryRequest;
import com.app.inventory.model.request.UpdateInventoryRequest;
import com.app.inventory.model.response.InventoryResponse;
import com.app.inventory.model.response.WebResponse;
import com.app.inventory.service.InventoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/api" })
public class InventoryController {
	private final InventoryService service;
	private static final String PROP_INVENTORY = "inventory ";

	public InventoryController(InventoryService service) {
		super();
		this.service = service;
	}

	@PostMapping(value = "/inventory")
	public ResponseEntity<WebResponse<String>> add(@Valid @RequestBody CreateInventoryRequest request) {
		service.add(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_INVENTORY + ErrorMessageConstant.HAS_BEEN_ADDED_SUCCESSFULLY));
	}

	@PutMapping(value = "/inventory")
	public ResponseEntity<WebResponse<String>> edit(@RequestBody UpdateInventoryRequest request) {
		service.edit(request);
		return ResponseEntity.ok(ResponseHelper.ok(PROP_INVENTORY + ErrorMessageConstant.HAS_BEEN_EDITED_SUCCESSFULLY));
	}

	@DeleteMapping(value = "/inventory/{id}")
	public ResponseEntity<WebResponse<String>> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity
				.ok(ResponseHelper.ok(PROP_INVENTORY + ErrorMessageConstant.HAS_BEEN_DELETED_SUCCESSFULLY));
	}

	@GetMapping(value = "/inventory/{id}")
	public ResponseEntity<WebResponse<InventoryResponse>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getById(id)));
	}

	@GetMapping(value = "/inventory")
	public ResponseEntity<WebResponse<Page<InventoryResponse>>> getAllItems(Pageable pageable) {
		return ResponseEntity.ok(ResponseHelper.ok(service.getAllItems(pageable)));
	}

}
