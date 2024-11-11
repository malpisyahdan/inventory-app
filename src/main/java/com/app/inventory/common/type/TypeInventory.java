package com.app.inventory.common.type;

public enum TypeInventory {

	T("TOP UP"), W("WITHDRAWAL");

	private final String typeString;

	TypeInventory(String typeString) {
		this.typeString = typeString;
	}

	public String getTypeString() {
		return typeString;
	}

	public static TypeInventory fromTypeString(String typeString) {
		for (TypeInventory status : TypeInventory.values()) {
			if (status.getTypeString().equalsIgnoreCase(typeString)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Unknown type string: " + typeString);
	}

}
