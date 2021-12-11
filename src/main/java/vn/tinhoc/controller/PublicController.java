package vn.tinhoc.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.lanhoang.ontology.configuration.OntologyVariables;
import vn.tinhoc.domain.BaiGiang;
import vn.tinhoc.domain.Chuong;
import vn.tinhoc.domain.KiemTra;
import vn.tinhoc.domain.Tiet;
import vn.tinhoc.domain.dto.BaiGiangDTO;
import vn.tinhoc.domain.dto.CauHoiDTO;
import vn.tinhoc.domain.dto.NopBaiDTO;
import vn.tinhoc.repository.CauHoiRepository;
import vn.tinhoc.service.PublicService;
import vn.tinhoc.tokenizer.Dictionary;
import vn.tinhoc.utils.DataUtils;
import vn.tinhoc.utils.model.LContainer;

import static vn.tinhoc.utils.DataUtils.distinctByKey;
import static vn.tinhoc.utils.DataUtils.escapeMetaCharacters;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/public")
public class PublicController {
	@Autowired
	PublicService publicService;
	
	@Autowired
	CauHoiRepository cauHoiRepository;

	@Autowired
	OntologyVariables vars;

	@Autowired
	Dictionary dict;
	
	@GetMapping("/cau-hoi/{id}")
	public ResponseEntity<?> listCauHoi(@PathVariable String id) {
		
		List<CauHoiDTO> cauhoi = publicService.listCauHoi(id);
		return cauhoi != null
				? new ResponseEntity<>(cauhoi, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/cau-hoi")
	public ResponseEntity<?> save(@RequestBody CauHoiDTO cauHoiDTO) {
		
		return new ResponseEntity<>(publicService.create(cauHoiDTO), HttpStatus.OK);
	}
	
	@GetMapping("/chuong/{id}")
	public ResponseEntity<?> findChuongById(@PathVariable String id) {
		Chuong chuong = publicService.findChuongById(id);
		return chuong != null
				? new ResponseEntity<>(chuong, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/tiet/{id}")
	public ResponseEntity<?> findTietById(@PathVariable String id) {
		Tiet tiet = publicService.findTietById(id);
		return tiet != null
				? new ResponseEntity<>(tiet, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/bai-giang/{id}")
	public ResponseEntity<?> findBaiGiangById(@PathVariable String id) {
		BaiGiangDTO baiGiangdto = publicService.findBaiGiangById(id);
		return baiGiangdto != null
				? new ResponseEntity<>(baiGiangdto, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/bai-giang")
	public ResponseEntity<?> listBaiGiang() {
		
		return new ResponseEntity<>(publicService.listBaiGiang(), HttpStatus.OK);
	}

	@PostMapping("/nop-bai")
	public ResponseEntity<?> nopBai(@RequestBody List<NopBaiDTO> nopBaiDTOs,
									@RequestParam String user,
									@RequestParam String ktra){
		return ResponseEntity.ok(publicService.nopBai(nopBaiDTOs, user, ktra));
	}

	@GetMapping("/ket-qua/{username}")
	public ResponseEntity<?> xemKetQua(@PathVariable String username) {
		return ResponseEntity.ok(publicService.findKQByUsername(username));
	}

	@PostMapping("/search/basic")
	public ResponseEntity<?> searchBasic(@RequestBody Map<String, String> map) {
		return ResponseEntity.ok(publicService.basicSearch(
				escapeMetaCharacters(map.get("text")))
		);
	}

	@GetMapping("/search/labels")
	public ResponseEntity<?> labels() {
		Property property = ModelFactory.createDefaultModel().createProperty(vars.getBaseUri() + "dinhDanh");
		List<LContainer> labels = DataUtils.getComments(property, BaiGiang.class, vars);
		labels.addAll(DataUtils.getComments(property, KiemTra.class, vars));

		return ResponseEntity.ok(labels.stream().filter(distinctByKey(LContainer::getType)).collect(Collectors.toList()));
	}

	@PostMapping("/search/advance/{type}")
	public ResponseEntity<?> searchAdvance(@PathVariable String type,
										   @RequestBody List<Map<String, Object>> lQueries) {
		StringBuilder str = new StringBuilder("SELECT ?s { ");
		str.append("\n\t");
		str.append("?s rdf:type ");
		str.append(vars.getPreffix());
		str.append(type);
		str.append(" .\n");
		String qqq = lQueries
				.stream()
				.map(lQuery -> {
					String s = "\n\t{\n\t\t";
					s += advSea(lQuery, true);
					s += "\n\t}\n";

					return s;
				}).collect(Collectors.joining("\t."));

		str.append(qqq);
		str.append("\n }");
		System.out.println(str);

		return ResponseEntity.ok(publicService.advanceSearch(str.toString(), type));
	}

	private String advSea(Map<String, Object> lQuery, boolean isHead) {
		if (lQuery.get("value") instanceof String) {
			String _ps = "?o" + lQuery.get("id");
			String _s = _ps + "_v";
			StringBuilder head = new StringBuilder();
			if (!isHead) {
				head.append(" ");
				head.append(_ps);
				head.append(" . ");
				head.append("?o");
				head.append(lQuery.get("id"));
			} else {
				head.append("?s");
				head.append(" ");
			}
			head.append(" ");
			head.append(vars.getPreffix());
			head.append(lQuery.get("name"));
			head.append(" ");
			head.append(_s);
			return new StringBuilder()
					.append(head)
					.append(" . ")
					.append("FILTER( ")
					.append(
							StringUtils.isNotBlank((CharSequence) lQuery.get("value")) ?
							dict.extract((String) lQuery.get("value"))
									.stream()
									.map(s -> String.format("regex(%s, \"%s\", \"i\")", _s, s))
									.collect(Collectors.joining(" || ")) :
							"regex(" + _s + ", \"\", \"i\")"
					)
					.append(" )")
					.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			if (!isHead) {
				sb.append("?o")
					.append((String) lQuery.get("id"))
					.append(" . ");
				sb.append("?o")
					.append((String) lQuery.get("id"))
					.append(" ");
			} else {
				sb.append("?s ");
			}

			return sb.append(vars.getPreffix())
					.append(lQuery.get("name"))
					.append(" ")
					.append(
						advSea(
							(Map<String, Object>) lQuery.get("value"),
							false
						)
					)
					.toString();
		}
	}
}
