package org.camunda.bpm.bvis.bvis;

public class RestExample2AdderRequest {

	private int first;
	private int second;

	/**
	 * Convenience constructor
	 */
	public RestExample2AdderRequest(int first, int second) {
		this.first = first;
		this.second = second;
	}

	// default constructor
	RestExample2AdderRequest() { }

	public int getFirst() {
		return first;
	}

	public int getSecond() {
		return second;
	}

}
