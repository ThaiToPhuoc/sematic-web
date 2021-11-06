package vn.tinhoc.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.CauHoi;
import vn.tinhoc.domain.DapAn;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.repository.DapAnRepository;

@Service
public class PublicService {
	
	@Autowired
	CauHoiRepository cauHoiRepository;
	
	@Autowired
	DapAnRepository dapAnRepository;
	
	@Autowired
	OntologyVariables vars;
	
	public CauHoi update(CauHoi cauHoi) {
		
		return null;
	}
	
	public CauHoiDTO create(CauHoiDTO cauHoiDTO) {
		CauHoi cauHoi = cauHoiDTO.getCauHoi();
		List<DapAn> dapAns = cauHoiDTO.getDapAns();
		
		cauHoi.setId(vars.getBaseUri() + "Cau2_KTC2_Lop6_1aaaa");
		cauHoiRepository.save(cauHoi);
		
		for (int i = 1; i <= dapAns.size(); i++) {
			DapAn dapAn = dapAns.get(i - 1);
			dapAn.setId("DapAn" + i);
			dapAn.setThuocCauHoi(cauHoi);
			dapAnRepository.save(dapAn);
		}
		
		return cauHoiDTO;
	}
	
	public List<CauHoiDTO> listCauHoi() {
		List<CauHoi> cauHois = cauHoiRepository.find();
		List<DapAn> dapAns = dapAnRepository.find();
		return cauHois.stream().map(ch -> {
			List<DapAn> das = dapAns.stream()
			.filter(da -> da.getThuocCauHoi() != null)
			.filter(da -> {
				return da.getThuocCauHoi().getId().equals(ch.getId());
			})
			.collect(Collectors.toList());
			
			das.forEach(da -> da.setThuocCauHoi(null));
			return new CauHoiDTO(ch, das);
		})
		.collect(Collectors.toList());
	}
}
