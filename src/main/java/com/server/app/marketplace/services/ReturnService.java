package com.server.app.marketplace.services;

import com.server.app.marketplace.domain.dto.request.CreateReturnRequest;
import com.server.app.marketplace.domain.dto.request.ProcessReturnRefundRequest;
import com.server.app.marketplace.domain.dto.request.RejectReturnRequest;
import com.server.app.marketplace.domain.dto.response.return_.ReturnResponse;

import java.util.List;

public interface ReturnService {

    ReturnResponse createReturn(CreateReturnRequest request);

    ReturnResponse getReturnById(Long id);

    List<ReturnResponse> getReturnsByBuyer(Long buyerId);

    List<ReturnResponse> getReturnsBySeller(Long sellerUserId);

    ReturnResponse approveReturn(Long returnId, Long sellerUserId);

    ReturnResponse rejectReturn(Long returnId, RejectReturnRequest request);

    ReturnResponse processRefund(Long returnId, ProcessReturnRefundRequest request);

    ReturnResponse cancelReturn(Long returnId, Long buyerId);
}
