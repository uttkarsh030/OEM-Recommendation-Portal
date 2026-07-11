package com.oem.oem_portal.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oem.oem_portal.dtos.request.AssignRecommendationToBankerRequest;
import com.oem.oem_portal.dtos.response.BankerResponse;
import com.oem.oem_portal.dtos.response.DepartmentHeadDashboardResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.enums.BankerStatus;
import com.oem.oem_portal.enums.RecommendationStatus;
import com.oem.oem_portal.exception.ResourceNotFoundException;
import com.oem.oem_portal.model.Banker;
import com.oem.oem_portal.model.Department;
import com.oem.oem_portal.model.DepartmentHead;
import com.oem.oem_portal.model.Recommendation;
import com.oem.oem_portal.repo.BankerRepository;
import com.oem.oem_portal.repo.DepartmentHeadRepository;
import com.oem.oem_portal.repo.DepartmentRepository;
import com.oem.oem_portal.repo.RecommendationRepository;
import com.oem.oem_portal.service.AuditLogService;
import com.oem.oem_portal.service.DepartmentHeadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentHeadServiceImpl implements DepartmentHeadService {
    
    private final DepartmentHeadRepository departmentHeadRepository;
    private final DepartmentRepository departmentRepository;
    private final RecommendationRepository recommendationRepository;
    private final BankerRepository bankerRepository;
    private final AuditLogService auditLogService;

    @Override
    public DepartmentHeadDashboardResponse getDashboard(String email) {

        Department department = findDepartmentByHeadEmail(email);
        List<Recommendation> recommendations = recommendationRepository
            .findByDepartmentId(department.getId());
        
        return DepartmentHeadDashboardResponse.builder()
            .deaprtmentName(department.getName())
            .assignedRecommendations(
                recommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.DEPARTMENT_ASSIGNED)
                    .count()
            )
                .underReviewRecommendations(
                    recommendations.stream()
                        .filter(r -> r.getStatus() == RecommendationStatus.UNDER_REVIEW)
                        .count()
                )
                .assignedToBankers(
                    recommendations.stream()
                        .filter(r -> r.getStatus() == RecommendationStatus.ASSIGNED)
                        .count()
                )
                .implementedRecommendations(
                    recommendations.stream()
                        .filter(r -> r.getStatus() == RecommendationStatus.IMPLEMENTED)
                        .count()
                )
                .reviewedRecommendations(
                    recommendations.stream()
                        .filter(r -> r.getStatus() == RecommendationStatus.REVIEWED)
                        .count()
                )
                .build();
    }

    @Override
    public List<RecommendationResponse> getAssignedRecommendations(String email) {
        Department department = findDepartmentByHeadEmail(email);

        return recommendationRepository.findByDepartmentId(department.getId())
            .stream()
            .filter(r -> r.getStatus() == RecommendationStatus.DEPARTMENT_ASSIGNED)
            .map(this::mapToRecommendationResponse)
            .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse startReview(Long recommendationId, String email) {

        Department department = findDepartmentByHeadEmail(email);

        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Recommendation not found")
                );

        if (!recommendation.getDepartment().getId().equals(department.getId())) {
            throw new IllegalStateException("This recommendation is not assigned to your department");
        }

        if (recommendation.getStatus() != RecommendationStatus.DEPARTMENT_ASSIGNED) {
            throw new IllegalStateException("Recommendation is not in assignable state");
        }

        recommendation.setStatus(RecommendationStatus.UNDER_REVIEW);
        recommendationRepository.save(recommendation);

        auditLogService.log(
            "RECOMMENDATION_UNDER_REVIEW",
            email,
            "DEPARTMENT_HEAD",
            recommendation.getId(),
            "DEPARTMENT_ASSIGNED",
            "UNDER_REVIEW",
            "Department Head started review"
        );

        return mapToRecommendationResponse(recommendation);
    }

        @Override
    public List<BankerResponse> getMyBankers(String email) {

        Department department = findDepartmentByHeadEmail(email);

        if (department.getBankers() == null) {
            return List.of();
        }

        return department.getBankers()
                .stream()
                .filter(b -> b.getStatus() == BankerStatus.ACTIVE)
                .map(this::mapToBankerResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse assignToBanker(
            AssignRecommendationToBankerRequest request,
            String email
    ) {

        Department department = findDepartmentByHeadEmail(email);

        Recommendation recommendation = recommendationRepository
                .findById(request.getRecommendationId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Recommendation not found")
                );

        if (!recommendation.getDepartment().getId().equals(department.getId())) {
            throw new IllegalStateException("This recommendation is not assigned to your department");
        }

        if (recommendation.getStatus() != RecommendationStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Recommendation must be under review before assigning to banker");
        }

        Banker banker = bankerRepository.findById(request.getBankerId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Banker not found")
                );

        if (banker.getStatus() != BankerStatus.ACTIVE) {
            throw new IllegalStateException("Banker is not active");
        }

        if (banker.getDepartment() == null ||
            !banker.getDepartment().getId().equals(department.getId())) {
            throw new IllegalStateException("Banker does not belong to your department");
        }

        recommendation.setBanker(banker);
        recommendation.setStatus(RecommendationStatus.ASSIGNED);
        recommendationRepository.save(recommendation);

        auditLogService.log(
            "RECOMMENDATION_ASSIGNED_TO_BANKER",
            email,
            "DEPARTMENT_HEAD",
            recommendation.getId(),
            "UNDER_REVIEW",
            "ASSIGNED",
            "Department Head assigned to banker: " + banker.getUsername()
        );

        return mapToRecommendationResponse(recommendation);
    }

    @Override
    public List<RecommendationResponse> getImplementedRecommendations(String email) {

        Department department = findDepartmentByHeadEmail(email);

        return recommendationRepository.findByDepartmentId(department.getId())
                .stream()
                .filter(r -> r.getStatus() == RecommendationStatus.IMPLEMENTED)
                .map(this::mapToRecommendationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse reviewImplementation(Long recommendationId, String email) {

        Department department = findDepartmentByHeadEmail(email);

        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("Recommendation not found")
                );

        if (!recommendation.getDepartment().getId().equals(department.getId())) {
            throw new IllegalStateException("This recommendation is not assigned to your department");
        }

        if (recommendation.getStatus() != RecommendationStatus.IMPLEMENTED) {
            throw new IllegalStateException("Recommendation must be implemented before review");
        }

        recommendation.setStatus(RecommendationStatus.REVIEWED);
        recommendationRepository.save(recommendation);

        auditLogService.log(
            "IMPLEMENTATION_REVIEWED",
            email,
            "DEPARTMENT_HEAD",
            recommendation.getId(),
            "IMPLEMENTED",
            "REVIEWED",
            "Department Head reviewed implementation"
        );

        return mapToRecommendationResponse(recommendation);
    }




    //private healper functions
    private Department findDepartmentByHeadEmail(String email) {
        DepartmentHead dh = departmentHeadRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResourceNotFoundException("Department Head not found")
            );
        
        return departmentRepository.findAll()
            .stream()
            .filter(d -> d.getDepartmentHead() != null &&
                    d.getDepartmentHead().getId().equals(dh.getId()))
            .findFirst()
            .orElseThrow(() ->
                new ResourceNotFoundException("Department not found for this head")
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

    private BankerResponse mapToBankerResponse(Banker banker) {
        return BankerResponse.builder()
            .id(banker.getId())
            .username(banker.getUsername())
                .email(banker.getEmail())
                .status(banker.getStatus())
                .departmentName(
                    banker.getDepartment() != null
                        ? banker.getDepartment().getName() : null
                )
                .build();
    }
}
