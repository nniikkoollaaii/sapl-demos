package org.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.demo.domain.Patient;
import org.demo.domain.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.sapl.pep.BlockingSAPLAuthorizer;
import io.sapl.pep.SAPLAuthorizer;

@Controller
public class UIController {

	private BlockingSAPLAuthorizer sapl;
	private PatientRepository patientenRepo;

	@Autowired
	public UIController(SAPLAuthorizer sapl, PatientRepository patientenRepo) {
		this.sapl = new BlockingSAPLAuthorizer(sapl);
		this.patientenRepo = patientenRepo;
	}

	@GetMapping("/profiles")
	public String profileList(Model model, Authentication authentication, HttpServletRequest request) {
		model.addAttribute("profiles", patientenRepo.findAll());
		return "profiles";
	}

	@GetMapping("/patient")
	public String loadProfile(@RequestParam("id") int id, Model model, Authentication authentication) {
		Patient patient = patientenRepo.findById(id).orElse(null);
		if (patient == null) {
			throw new IllegalArgumentException();
		}

		model.addAttribute("patient", patient);
		model.addAttribute("viewRoomNumberPermission", sapl.authorize(authentication, "viewRoomNumber", patient));
		return "patient";
	}

}