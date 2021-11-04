package vn.tinhoc.domain;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "DapAn")
public class DapAn {
	@Name
	private String id;
	
	private CauHoi ThuocCauHoi;
	
	private String NoiDungDapAn;
	
	private Integer KetQua;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CauHoi getThuocCauHoi() {
		return ThuocCauHoi;
	}

	public void setThuocCauHoi(CauHoi thuocCauHoi) {
		ThuocCauHoi = thuocCauHoi;
	}

	public String getNoiDungDapAn() {
		return NoiDungDapAn;
	}

	public void setNoiDungDapAn(String noiDungDapAn) {
		NoiDungDapAn = noiDungDapAn;
	}

	public Integer getKetQua() {
		return KetQua;
	}

	public void setKetQua(Integer ketQua) {
		KetQua = ketQua;
	}
}