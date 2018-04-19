package io.sapl.demo.shared.marshalling;

import io.sapl.demo.domain.Patient;
import io.sapl.demo.domain.resource.PatientResource;
import io.sapl.spring.marshall.mapper.SaplClassMapper;
import io.sapl.spring.marshall.mapper.SaplRequestElement;

public class PatientMapper implements SaplClassMapper {

	@Override
	public Object map(Object objectToMap, SaplRequestElement element) {
		Patient patient = (Patient) objectToMap;
		return new PatientResource(patient);
	}

	@Override
	public Class<?> getMappedClass() {
		return Patient.class;
	}

}
