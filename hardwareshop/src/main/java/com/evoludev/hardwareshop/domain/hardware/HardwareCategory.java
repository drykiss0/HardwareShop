package com.evoludev.hardwareshop.domain.hardware;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HardwareCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private int id;
	private String name;
	private Integer itemCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public HardwareCategory() {
	}
	
	public HardwareCategory(String name, Integer itemCount) {
		this.name = name;
		this.itemCount = itemCount;
	}

	@Override
	public String toString() {
		return "HardwareCategory [id=" + id + ", name=" + name + ", itemCount=" + itemCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemCount == null) ? 0 : itemCount.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HardwareCategory other = (HardwareCategory) obj;
		if (itemCount == null) {
			if (other.itemCount != null)
				return false;
		} else if (!itemCount.equals(other.itemCount))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}