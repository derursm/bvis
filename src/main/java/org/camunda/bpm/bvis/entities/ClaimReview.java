package org.camunda.bpm.bvis.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class ClaimReview {
	@NotNull
	protected int claimStatus;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	protected long claimReviewID;
	protected String claimDescription;
	
	@ManyToOne
	protected Claim claim;
	
	public String getProcessIDCapitol() {
		return processIDCapitol;
	}
	public void setProcessIDCapitol(String processIDCapitol) {
		this.processIDCapitol = processIDCapitol;
	}
	public long getClaimReviewID() {
		return claimReviewID;
	}
	public void setClaimReviewID(long claimReviewID) {
		this.claimReviewID = claimReviewID;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
	}
	private String processIDCapitol;
	public Claim getClaim() {
		return claim;
	}
	public void setClaimID(Claim claim) {
		this.claim = claim;
	}
	public int getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(int claimStatus) {
		this.claimStatus = claimStatus;
	}
	public String getClaimDescription() {
		return claimDescription;
	}
	public void setClaimDescription(String claimDescription) {
		this.claimDescription = claimDescription;
	}
	
}
