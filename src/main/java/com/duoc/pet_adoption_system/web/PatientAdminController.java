package com.duoc.pet_adoption_system.web;

import com.duoc.pet_adoption_system.patients.application.CreatePatientUseCase;
import com.duoc.pet_adoption_system.patients.application.GetPatientByIdUseCase;
import com.duoc.pet_adoption_system.patients.application.ListPatientsUseCase;
import com.duoc.pet_adoption_system.web.dto.PatientView;
import com.duoc.pet_adoption_system.web.forms.PatientForm;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/patients")
@Validated
public class PatientAdminController {

	private final ListPatientsUseCase listPatientsUseCase;
	private final GetPatientByIdUseCase getPatientByIdUseCase;
	private final CreatePatientUseCase createPatientUseCase;

	public PatientAdminController(
			ListPatientsUseCase listPatientsUseCase,
			GetPatientByIdUseCase getPatientByIdUseCase,
			CreatePatientUseCase createPatientUseCase) {
		this.listPatientsUseCase = listPatientsUseCase;
		this.getPatientByIdUseCase = getPatientByIdUseCase;
		this.createPatientUseCase = createPatientUseCase;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("patients", listPatientsUseCase.execute().stream().map(PatientView::from).toList());
		return "app/patients/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("patientForm", new PatientForm());
		return "app/patients/form";
	}

	@PostMapping("/new")
	public String create(@ModelAttribute PatientForm patientForm, RedirectAttributes redirectAttributes) {
		if (!isPatientFormValid(patientForm)) {
			redirectAttributes.addFlashAttribute("errorMessage", "Complete todos los campos obligatorios.");
			return "redirect:/app/patients/new";
		}
		var patient = createPatientUseCase.execute(
				patientForm.getName(),
				patientForm.getSpecies(),
				patientForm.getIntakeDate(),
				patientForm.getTreatmentNotes(),
				patientForm.getCareStatus());
		redirectAttributes.addFlashAttribute("successMessage", "Paciente registrado.");
		return "redirect:/app/patients/" + patient.id().value();
	}

	@GetMapping("/{id}")
	public String detail(@PathVariable @Size(max = 36) String id, Model model) {
		model.addAttribute("patient", PatientView.from(getPatientByIdUseCase.execute(id)));
		return "app/patients/detail";
	}

	private static boolean isPatientFormValid(PatientForm f) {
		return f.getName() != null && !f.getName().isBlank()
				&& f.getSpecies() != null && !f.getSpecies().isBlank()
				&& f.getIntakeDate() != null
				&& f.getCareStatus() != null;
	}
}
