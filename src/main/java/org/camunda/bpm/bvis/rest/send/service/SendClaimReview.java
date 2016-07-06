package org.camunda.bpm.bvis.rest.send.service;

import javax.faces.bean.ManagedBean;
import javax.inject.Named;

import org.camunda.bpm.bvis.Entities.ClaimReview;
import org.camunda.bpm.bvis.rest.send.dto.ClaimReviewDTO;
import org.camunda.bpm.bvis.rest.send.dto.ClaimReviewDecision;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

@ManagedBean
//@ConversationScoped
@Named
public class SendClaimReview {

	private static final String BASE_URI = "http://camunda-capitol.uni-muenster.de/partner-interface/";
	
	public String sendClaimReview(ClaimReview entityReview,
			String processInstanceID) {
		ClaimReviewDTO review = new ClaimReviewDTO();
		review.setDecision(this.parseClaimReviewDecision(entityReview));
		review.setProcessinstance_id_bvis(processInstanceID);
		review.setProcessinstance_id_capitol(entityReview.getProcessIDCapitol());
		ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI);
		SendClaimReviewClient senderClient = target.proxy(SendClaimReviewClient.class);
		String result = senderClient.sendClaimReview(review);
		return result;
	}
	
	private ClaimReviewDecision parseClaimReviewDecision(ClaimReview entityReview) {
		ClaimReviewDecision review = new ClaimReviewDecision();
		review.setClaim_id(Integer.parseInt(entityReview.getClaim().getClaimID().toString()));
		review.setClaim_status(entityReview.getClaimStatus());
		review.setDescription(entityReview.getClaimDescription());
		return review;
	}
}
