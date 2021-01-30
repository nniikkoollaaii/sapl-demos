package io.sapl.playground.models;

public abstract class Example {

	private String policy;
	
	private String authSub;
	
	public String getPolicy() {
		return this.policy;
	}
	
	public String getAuthSub() {
		return this.authSub;
	}
	
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	
	public void setAuthSub(String authSub) {
		this.authSub = authSub;
	}
}
