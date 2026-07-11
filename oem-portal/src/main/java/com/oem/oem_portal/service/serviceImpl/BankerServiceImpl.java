package com.oem.oem_portal.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oem.oem_portal.dtos.request.UpdateStatusRequest;
import com.oem.oem_portal.dtos.response.BankerDashboardResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.enums.RecommendationStatus;
import com.oem.oem_portal.exception.ResourceNotFoundException;
import com.oem.oem_portal.model.Banker;
import com.oem.oem_portal.model.Recommendation;
import com.oem.oem_portal.repo.BankerRepository;
import com.oem.oem_portal.repo.RecommendationRepository;
import com.oem.oem_portal.service.AuditLogService;
import com.oem.oem_portal.service.BankerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankerServiceImpl implements BankerService{
    private final BankerRepository bankerRepository;
    private final RecommendationRepository recommendationRepository;
    private final AuditLogService auditLogService;

    @Override
    public BankerDashboardResponse getDashboard(String email) {
        Banker banker = findBankerByEmail(email);
        List<Recommendation> recommendations = recommendationRepository
            .findByBankerId(banker.getId());

        String departmentName = null;
        String deapartmentHeadName = null;

        if(banker.getDepartment() != null) {
            departmentName = banker.getDepartment().getName();
            if(banker.getDepartment().getDepartmentHead() != null) {
                deapartmentHeadName = banker.getDepartment().getDepartmentHead().getUsername();
            }
        }

        return BankerDashboardResponse.builder()
                .totalAssigned(recommendations.size())
                .notImplemented(
                    recommendations.stream()
                        .filter(r -> r.getStatus() == RecommendationStatus.ASSIGNED)
                        .count()
                )
                .inProgress(
                    recommendations.stream()
                        .filter(r -> r.getStatus() == RecommendationStatus.IN_PROGRESS)
                        .count()
                )
                .implemented(
                    recommendations.stream()
                        .filter(r -> r.getStatus() == RecommendationStatus.IMPLEMENTED)
                        .count()
                )
                .deaprtmentName(departmentName)
                .deapartmentHeadName(deapartmentHeadName)
                .build();
    }

    @Override
    public List<RecommendationResponse> getAssignedRecommendations(String email) {
        Banker banker = findBankerByEmail(email);

        return recommendationRepository.findByBankerId(banker.getId())
            .stream()
            .map(this::mapToRecommendationResponse)
            .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse getRecommendationById(Long id, String email) {

        Banker banker = findBankerByEmail(email);

        Recommendation recommendation = recommendationRepository.findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Recommendation not found")
            );

        if(recommendation.getBanker() == null || !recommendation.getBanker().getId().equals(banker.getId())) {
            throw new IllegalStateException("This recommendation is not assigned to you");
        }

        return mapToRecommendationResponse(recommendation);

    }

    public RecommendationResponse updateStatus(Long recommendationId, UpdateStatusRequest request, String email) {

        Banker banker = findBankerByEmail(email);

        Recommendation recommendation = recommendationRepository.findById(recommendationId)
            .orElseThrow(() ->
                 new ResourceNotFoundException("Recommendation not found")
            );
        
        if(recommendation.getBanker() == null || !recommendation.getBanker().getId().equals(banker.getId())) {
            throw new IllegalStateException("This recommendation is not assigned to you");
        }

        RecommendationStatus currentStatus = recommendation.getStatus();
        RecommendationStatus newStatus = request.getStatus();

        if(currentStatus == RecommendationStatus.ASSIGNED && 
            newStatus == RecommendationStatus.IN_PROGRESS) {
                recommendation.setStatus(RecommendationStatus.IN_PROGRESS);
        }
        else if(currentStatus == RecommendationStatus.IN_PROGRESS &&
            newStatus == RecommendationStatus.IMPLEMENTED) {
                recommendation.setStatus(RecommendationStatus.IMPLEMENTED);
        }
        else {
            throw new IllegalStateException(
                "Invalid state transition. " +
                "Current:" + currentStatus + ", Requested: " + newStatus
            );
        }

        recommendationRepository.save(recommendation);

        auditLogService.log(
            "STATUS_UPDATED",
            email,
            "BANKER",
            recommendation.getId(),
            currentStatus.name(),
            newStatus.name(),
            "Banker updated status from " + currentStatus + " to " + newStatus
        );

        return mapToRecommendationResponse(recommendation);

    }



    //private helper functions
    private Banker findBankerByEmail(String email) {
        return bankerRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResourceNotFoundException("Banker not found with email: " + email)
            );
    }

    private RecommendationResponse mapToRecommendationResponse(Recommendation r) {
        return RecommendationResponse.builder()
                .id(r.getId())
                .title(r.getTitle())
                .description(r.getDescription())
                .oemName(r.getOem() != null ? r.getOem().getName() : null)
                .productName(r.getProduct() != null ? r.getProduct().getName() : null)
                .applicationName(r.getApplication() != null ? r.getApplication().getName() : null)
                .version(r.getVersion())
                .releaseDate(r.getReleaseDate())
                .documentPath(r.getDocumentPath())
                .uploadDate(r.getUploadDate())
                .status(r.getStatus())
                .vendorName(r.getVendor() != null ? r.getVendor().getName() : null)
                .deapartmentName(r.getDepartment() != null ? r.getDepartment().getName() : null)
                .bankerName(r.getBanker() != null ? r.getBanker().getUsername() : null)
                .build();
    }
}
