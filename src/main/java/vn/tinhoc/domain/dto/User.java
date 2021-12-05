package vn.tinhoc.domain.dto;

import java.util.List;

import vn.lanhoang.ontology.annotation.Default;
import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "ConNguoi")
public class User {
	@Name
	private String id;

	@Default
	private String TenTaiKhoan;
	
	private String MatKhau;
	
	private List<Role> GomQuyen;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenTaiKhoan() {
		return TenTaiKhoan;
	}

	public void setTenTaiKhoan(String tenTaiKhoan) {
		TenTaiKhoan = tenTaiKhoan;
	}

	public String getMatKhau() {
		return MatKhau;
	}

	public void setMatKhau(String matKhau) {
		MatKhau = matKhau;
	}

	public List<Role> getGomQuyen() {
		return GomQuyen;
	}

	public void setGomQuyen(List<Role> gomQuyen) {
		GomQuyen = gomQuyen;
	}
}
