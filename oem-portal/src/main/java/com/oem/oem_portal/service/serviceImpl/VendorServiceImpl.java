package com.oem.oem_portal.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oem.oem_portal.dtos.request.RecommendationRequest;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.dtos.response.VendorDashboardResponse;
import com.oem.oem_portal.enums.RecommendationStatus;
import com.oem.oem_portal.exception.ResourceNotFoundException;
import com.oem.oem_portal.model.Application;
import com.oem.oem_portal.model.OEM;
import com.oem.oem_portal.model.Product;
import com.oem.oem_portal.model.Recommendation;
import com.oem.oem_portal.model.Vendor;
import com.oem.oem_portal.repo.ApplicationRepository;
import com.oem.oem_portal.repo.OEMRepository;
import com.oem.oem_portal.repo.ProductRepository;
import com.oem.oem_portal.repo.RecommendationRepository;
import com.oem.oem_portal.repo.VendorRepository;
import com.oem.oem_portal.service.AuditLogService;
import com.oem.oem_portal.service.VendorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final RecommendationRepository recommendationRepository;
    private final OEMRepository oemRepository;
    private final ProductRepository productRepository;
    private final ApplicationRepository applicationRepository;
    private final AuditLogService auditLogService;


    @Override
    public VendorDashboardResponse getDashboard(String email) {
        Vendor vendor = findVendorByEmail(email);
        List<Recommendation> recommendations = recommendationRepository.findByVendorId(vendor.getId());

        return VendorDashboardResponse.builder()
            .totalUploads(recommendations.size())
            .assignedRecommendations(
                recommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.ASSIGNED
                            || r.getStatus() == RecommendationStatus.DEPARTMENT_ASSIGNED)
                    .count()   
            )
            .implementedRecommendations(
                recommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.IMPLEMENTED)
                    .count()
            )
            .verifiedRecommendations(
                recommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.VERIFIED)
                    .count()
            )
            .build();
    }

    @Override
    public RecommendationResponse addRecommendation(String email, RecommendationRequest request) {
        Vendor vendor = findVendorByEmail(email);

        OEM oem = findOrCreateOEM(request.getOemName());

        Product product = findOrCreateProduct(request.getProductName(), oem);

        Application application = findOrCreateApplication(request.getApplicationName(), product);

        Recommendation recommendation = Recommendation.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .version(request.getVersion())
                .releaseDate(request.getReleaseDate())
                .uploadDate(LocalDateTime.now())
                .status(RecommendationStatus.UPLOADED)
                .vendor(vendor)
                .oem(oem)
                .product(product)
                .application(application)
                .build();
        
        recommendationRepository.save(recommendation);

        auditLogService.log(
            "RECOMMENDATION_UPLOADED",
            email,
            "VENDOR",
            recommendation.getId(),
            null,
            "UPLOADED",
            "Vendor uploaded recommendation: " + recommendation.getTitle()
        );

        return mapToRecommendationResponse(recommendation);
    }

    @Override
    public RecommendationResponse updateRecommendation(Long id, String email, RecommendationRequest request) {
        Vendor vendor = findVendorByEmail(email);

        Recommendation recommendation = recommendationRepository.findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Recommendation not found with id: " + id)
            );

        if(!recommendation.getVendor().getId().equals(vendor.getId())) {
            throw new IllegalStateException("You can only edit your own recommendations");
        }

        if(recommendation.getStatus() != RecommendationStatus.UPLOADED) {
            throw new IllegalStateException(
                "Cannot edit recommendation. It has already been processed."
            );
        }

        OEM oem = findOrCreateOEM(request.getOemName());
        Product product = findOrCreateProduct(request.getProductName(), oem);
        Application application = findOrCreateApplication(request.getApplicationName(), product);

        recommendation.setTitle(request.getTitle());
        recommendation.setDescription(request.getDescription());
        recommendation.setVersion(request.getVersion());
        recommendation.setReleaseDate(request.getReleaseDate());
        recommendation.setOem(oem);
        recommendation.setProduct(product);
        recommendation.setApplication(application);

        recommendationRepository.save(recommendation);

        return mapToRecommendationResponse(recommendation);
    }

    @Override
    public void deleteRecommendation(Long id, String email) {

        Vendor vendor = findVendorByEmail(email);

        Recommendation recommendation = recommendationRepository.findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Recommendation not found with id: " + id)
            );

        if(!recommendation.getVendor().getId().equals(vendor.getId())) {
            throw new IllegalStateException("You can only delete your own recommendations");
        }

        if(recommendation.getStatus() != RecommendationStatus.UPLOADED) {
            throw new IllegalStateException("Cannot delete recommendation. It has already been processe");
        }

        auditLogService.log(
            "RECOMMENDATION_DELETED",
            email,
            "VENDOR",
            recommendation.getId(),
            recommendation.getStatus().name(),
            null,
            "Vendor deleted recommendation: " + recommendation.getTitle());

        recommendationRepository.delete(recommendation);

    }

    @Override
    public List<RecommendationResponse> getMyRecommendations(String email) {
        Vendor vendor = findVendorByEmail(email);

        return recommendationRepository.findByVendorId(vendor.getId())
            .stream()
            .map(this::mapToRecommendationResponse)
            .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse getRecommendationById(Long id, String email) {
        Vendor vendor = findVendorByEmail(email);

        Recommendation recommendation = recommendationRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Recommendation not found with id: " + id)
            );
        
        if(!recommendation.getVendor().getId().equals(vendor.getId())) {
            throw new IllegalStateException("You can only view you own receommendations");
        }

        return mapToRecommendationResponse(recommendation);
        
    }




    //private helper function
    private Vendor findVendorByEmail(String email) {
        return vendorRepository.findByEmail(email)
            .orElseThrow(() ->
                new ResourceNotFoundException("Vendor not found with email: " + email)
            );
    }

    private OEM findOrCreateOEM(String name) {
        return oemRepository.findByName(name)
            .orElseGet(() -> {
                OEM newOem = OEM.builder()
                    .name(name)
                    .build();
                return oemRepository.save(newOem);
            });
    }

    private Product findOrCreateProduct(String name, OEM oem) {
        return productRepository.findByOemId(oem.getId())
            .stream()
            .filter(p -> p.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseGet(() -> {
                Product newProduct = Product.builder()
                    .name(name)
                    .oem(oem)
                    .build();
                return productRepository.save(newProduct);
            });
    }

    private Application findOrCreateApplication(String name, Product product) {
        return applicationRepository.findByProductId(product.getId())
            .stream()
            .filter(a -> a.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseGet(() -> {
                Application newApp = Application.builder()
                    .name(name)
                    .product(product)
                    .build();
                return applicationRepository.save(newApp);
            });
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
