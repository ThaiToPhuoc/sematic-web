package vn.tinhoc.sbjsp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.tinhoc.sbjsp.domain.GiaoVien;
import vn.tinhoc.sbjsp.repository.GiaoVienRepository;
import vn.tinhoc.sbjsp.utils.DataUtils;

@Controller
@RequestMapping("/giao-vien")
public class GiaoVienController {
	
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private GiaoVienRepository giaoVienRepository;
	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
	public String giaovien(Model model) {
		log.info("Giao vien {}", giaoVienRepository.find());
		model.addAttribute("dsGiaoVien", giaoVienRepository.find());
		
		
		return "giaovien/index";
	}
	
	@RequestMapping(value = {"/add"}, method = RequestMethod.GET)
	public String showAddPage(Model model) {
		GiaoVien giaoVien = new GiaoVien();
		model.addAttribute("giaoVien", giaoVien);
		
		
		return "giaovien/add";
	}
	
	@RequestMapping(value = {"/add"}, method = RequestMethod.POST)
	public String add(Model model, @ModelAttribute("giaoVien") GiaoVien giaoVien) {
		String id = giaoVien.getHo() + " " + giaoVien.getTen();
		giaoVien.setId(DataUtils.normalized(id));
		
		model.addAttribute("giaoVien", giaoVienRepository.save(giaoVien));
		
		return "giaovien/add";
	}

}
