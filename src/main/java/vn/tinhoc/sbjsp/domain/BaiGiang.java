package vn.tinhoc.sbjsp.domain;

import vn.lanhoang.ontology.annotation.Name;
import vn.lanhoang.ontology.annotation.OntologyObject;

@OntologyObject(uri = "BaiGiang")
public class BaiGiang {
	@Name
	private String id;
	
	private Integer ChuongTrinh ;
	
	private Integer HocKy ;
	
	private Integer NamHoc ;
	
	private GiaoVien DuocSoanBoi;

	public GiaoVien getDuocSoanBoi() {
		return DuocSoanBoi;
	}

	public void setDuocSoanBoi(GiaoVien duocSoanBoi) {
		DuocSoanBoi = duocSoanBoi;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getChuongTrinh() {
		return ChuongTrinh;
	}

	public void setChuongTrinh(Integer chuongTrinh) {
		ChuongTrinh = chuongTrinh;
	}

	public Integer getHocKy() {
		return HocKy;
	}

	public void setHocKy(Integer hocKy) {
		HocKy = hocKy;
	}

	public Integer getNamHoc() {
		return NamHoc;
	}

	public void setNamHoc(Integer namHoc) {
		NamHoc = namHoc;
	}

}
