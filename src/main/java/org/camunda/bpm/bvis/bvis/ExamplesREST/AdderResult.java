package org.camunda.bpm.bvis.bvis.ExamplesREST;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement // needed to produce xml responses, not for JSON
public class AdderResult {
	private int first;
	private int second;

	/**
	 * Convenience constructor
	 */
	public AdderResult(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * @param adderRequest
	 *            extracts values from {@link RestExample2AdderRequest} to set itself up
	 */
	public AdderResult(AdderRequest adderRequest) {
		this(adderRequest.getFirst(), adderRequest.getSecond());
	}
	
	// default constructor
	AdderResult() { }

	@XmlElement // needed to produce xml responses, not for JSON
	public int getFirst() {
		return first;
	}

	@XmlElement // needed to produce xml responses, not for JSON
	public int getSecond() {
		return second;
	}

	@XmlElement // needed to produce xml responses, not for JSON
	public int getResult() {
		return first + second;
	}
}