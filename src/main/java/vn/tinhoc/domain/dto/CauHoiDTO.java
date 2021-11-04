package vn.tinhoc.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.DapAn;

public class CauHoiDTO {
	private CauHoi cauHoi;
	
	private List<DapAn> dapAns;

	public CauHoiDTO(CauHoi cauHoi, List<DapAn> dapAns) {
		this.cauHoi = cauHoi;
		this.dapAns = dapAns;
	}

	@JsonProperty("CauHoi")
	public CauHoi getCauHoi() {
		return cauHoi;
	}

	public void setCauHoi(CauHoi cauHoi) {
		this.cauHoi = cauHoi;
	}

	@JsonProperty("DanhSachDapAn")
	public List<DapAn> getDapAns() {
		return dapAns;
	}

	public void setDapAns(List<DapAn> dapAns) {
		this.dapAns = dapAns;
	}
}