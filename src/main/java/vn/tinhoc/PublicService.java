package vn.tinhoc;

import java.util.List;
import java.util.Objects;
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
	
	public CauHoi create(CauHoi cauHoi) {
		
		return null;
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
