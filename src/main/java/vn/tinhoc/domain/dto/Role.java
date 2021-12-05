package vn.tinhoc.domain.dto;

import vn.lanhoang.ontology.annotation.Default;
import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "Quyen")
public class Role {
	@Name
	private String id;

	@Default
	private String TenQuyen;
	
	public Role() {
		
	}

	public Role(String id, String tenQuyen) {
		this.id = id;
		TenQuyen = tenQuyen;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenQuyen() {
		return TenQuyen;
	}

	public void setTenQuyen(String tenQuyen) {
		TenQuyen = tenQuyen;
	}
}
