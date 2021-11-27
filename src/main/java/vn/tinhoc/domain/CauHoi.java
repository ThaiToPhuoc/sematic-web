package vn.tinhoc.domain;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "CauHoi")
public class CauHoi {
	
	@Name
	private String id;

	private String NoiDungCauHoi;
	
	private Integer STTCauHoi;

	private String TuKhoa;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoiDungCauHoi() {
		return NoiDungCauHoi;
	}

	public void setNoiDungCauHoi(String NoiDungCauHoi) {
		this.NoiDungCauHoi = NoiDungCauHoi;
	}

	public Integer getSTTCauHoi() {
		return STTCauHoi;
	}

	public void setSTTCauHoi(Integer STTCauHoi) {
		this.STTCauHoi = STTCauHoi;
	}

	public String getTuKhoa() {
		return TuKhoa;
	}

	public void setTuKhoa(String tuKhoa) {
		TuKhoa = tuKhoa;
	}
}