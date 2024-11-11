package com.app.inventory.persistence.entity;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;

@MappedSuperclass

@EntityListeners(AuditingEntityListener.class)
public abstract class MasterEntity {

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private ZonedDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	@LastModifiedDate
	private ZonedDateTime updatedAt;

	@Column(name = "version")
	@Version
	private Long version;

	@Column(name = "deleted_at")
	private ZonedDateTime deletedAt;

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ZonedDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(ZonedDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public ZonedDateTime getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(ZonedDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}

	@PrePersist
	protected void onPrePersist() {

		ZonedDateTime now = ZonedDateTime.now();
		if (this.createdAt == null) {
			this.createdAt = now;
		}
		if (this.updatedAt == null) {
			this.updatedAt = now;
		}
	}

}
