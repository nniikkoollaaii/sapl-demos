package io.sapl.playground.models;

public class BasicExample extends Example {

	public BasicExample() {
		String policy = "policy \"policy 1\"\r\n"
				+ "permit\r\n"
				+ "    action == \"read\"\r\n"
				+ "where\r\n"
				+ "    subject == \"WILLI\";\r\n"
				+ "    time.dayOfWeekFrom(\"UTC\".<clock.now>) =~ \"MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY\";";
		this.setPolicy(policy);
		String authSub = "{\\r\\n"
    			+ " \"subject\": \"WILLI\",\r\n"
    			+ " \"action\"      : \"read\",\r\n"
    			+ " \"resource\"    : \"something\"\r\n"
    			+ "}";
		this.setAuthSub(authSub);
	}
}
