package vn.tinhoc.domain;

import java.util.List;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "KiemTra")
public class KiemTra {
	@Name
	private String id;
	
	private Chuong ThuocChuong;
	
	private List<CauHoi> GomCauHoi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Chuong getThuocChuong() {
		return ThuocChuong;
	}

	public void setThuocChuong(Chuong thuocChuong) {
		ThuocChuong = thuocChuong;
	}

	public List<CauHoi> getGomCauHoi() {
		return GomCauHoi;
	}

	public void setGomCauHoi(List<CauHoi> gomCauHoi) {
		GomCauHoi = gomCauHoi;
	}
	
	
}
