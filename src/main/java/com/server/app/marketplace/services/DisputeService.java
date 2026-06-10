package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CreateDisputeRequest;
import com.server.app.marketplace.domain.dto.request.ResolveDisputeRequest;
import com.server.app.marketplace.domain.dto.request.RespondDisputeRequest;
import com.server.app.marketplace.domain.dto.response.dispute.DisputeResponse;

import java.util.List;

public interface DisputeService {

    DisputeResponse createDispute(CreateDisputeRequest request);

    DisputeResponse getDisputeById(Long id);

    List<DisputeResponse> getDisputesByBuyer(Long buyerId);

    List<DisputeResponse> getDisputesBySeller(Long sellerUserId);

    List<DisputeResponse> getPendingDisputesForAdmin(Long adminUserId);

    DisputeResponse respondToDispute(Long disputeId, RespondDisputeRequest request);

    DisputeResponse resolveDispute(Long disputeId, ResolveDisputeRequest request);
}
