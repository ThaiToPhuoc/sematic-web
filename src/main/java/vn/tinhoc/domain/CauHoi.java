package vn.tinhoc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "CauHoi")
public class CauHoi {
	
	@Name
	private String id;

	private String NoiDungCauHoi;
	
	private Integer STTCauHoi;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("NoiDungCauHoi")
	public String getNoiDungCauHoi() {
		return NoiDungCauHoi;
	}

	public void setNoiDungCauHoi(String noiDungCauHoi) {
		NoiDungCauHoi = noiDungCauHoi;
	}

	@JsonProperty("STTCauHoi")
	public Integer getSTTCauHoi() {
		return STTCauHoi;
	}

	public void setSTTCauHoi(Integer sTTCauHoi) {
		STTCauHoi = sTTCauHoi;
	}
}
