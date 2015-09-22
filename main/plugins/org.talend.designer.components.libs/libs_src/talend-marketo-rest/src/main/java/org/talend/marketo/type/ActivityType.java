package org.talend.marketo.type;

import java.util.List;
import java.util.Map;

public class ActivityType {
	private int id;

	private String name;

	private String description;

	private Map<String, String> primaryAttribute;

	private List<Map<String, String>> attributes;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setPrimaryAttribute(Map<String, String> primaryAttribute) {
		this.primaryAttribute = primaryAttribute;
	}

	public Map<String, String> getPrimaryAttribute() {
		return this.primaryAttribute;
	}

	public void setAttributes(List<Map<String, String>> attributes) {
		this.attributes = attributes;
	}

	public List<Map<String, String>> getAttributes() {
		return this.attributes;
	}
}
