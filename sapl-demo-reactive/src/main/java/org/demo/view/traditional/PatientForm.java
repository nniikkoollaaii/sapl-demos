package org.demo.view.traditional;

import java.io.IOException;

import org.demo.service.UIController;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PatientForm extends AbstractPatientForm {

	private static final long serialVersionUID = 1L;

	PatientForm(UIController controller, RefreshCallback refreshCallback) {
		super(controller, refreshCallback);
	}

	protected void updateFieldVisibility() {
		medicalRecordNumber.setVisible(isPermitted("read", medicalRecordNumber.getData()));
		name.setVisible(isPermitted("read", name.getData()));
		diagnosisText.setVisible(isPermitted("read", diagnosisText.getData()));
		icd11Code.setVisible(isPermitted("read", icd11Code.getData()));
		attendingDoctor.setVisible(isPermitted("read", attendingDoctor.getData()));
		attendingNurse.setVisible(isPermitted("read", attendingNurse.getData()));
		phoneNumber.setVisible(isPermitted("read", phoneNumber.getData()));

		try {
			final JsonNode resource = objectMapper.readTree("{ \"id\": " + patient.getId()
					+ ", \"uiElement\": \"" + roomNumber.getData() + "\"}");
			roomNumber.setVisible(isPermitted("read", resource));
		}
		catch (IOException e) {
			LOGGER.error("Error while creating a JsonNode from a JSON string.", e);
			roomNumber.setVisible(false);
		}
	}

	protected void updateFieldEnabling() {
		boolean isNewPatient = patient.getId() == null;
		medicalRecordNumber.setEnabled(isNewPatient && isPermitted("edit", medicalRecordNumber.getData()));
		name.setEnabled(isPermitted("edit", name.getData()));
		icd11Code.setEnabled(isPermitted("edit", icd11Code.getData()));
		diagnosisText.setEnabled(isPermitted("edit", diagnosisText.getData()));
		attendingDoctor.setEnabled(isPermitted("edit", attendingDoctor.getData()));
		attendingNurse.setEnabled(isPermitted("edit", attendingNurse.getData()));
		phoneNumber.setEnabled(isPermitted("edit", phoneNumber.getData()));
		roomNumber.setEnabled(isPermitted("edit", roomNumber.getData()));
	}

	protected void updateButtonVisibility() {
		boolean isNewPatient = patient.getId() == null;
		saveBtn.setVisible(isPermitted(isNewPatient ? "useForCreate" : "useForUpdate", saveBtn.getData()));

		try {
			final JsonNode resource = objectMapper.readTree("{ \"id\": " + patient.getId()
					+ ", \"uiElement\": \"" + deleteBtn.getData() + "\"}");
			deleteBtn.setVisible(!isNewPatient && isPermitted("use", resource));
		}
		catch (IOException e) {
			LOGGER.error("Error while creating a JsonNode from a JSON string.", e);
			deleteBtn.setVisible(false);
		}
	}

	private boolean isPermitted(Object action, Object resource) {
		final SingleRequestStreamManager streamManager = getSession().getAttribute(SingleRequestStreamManager.class);
		return streamManager.isAccessPermitted(action, resource);
	}

}