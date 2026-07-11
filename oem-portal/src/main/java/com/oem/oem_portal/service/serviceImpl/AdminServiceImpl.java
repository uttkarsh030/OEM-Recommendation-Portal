package com.oem.oem_portal.service.serviceImpl;

import com.oem.oem_portal.repo.RecommendationRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oem.oem_portal.dtos.request.AssignBankerRequest;
import com.oem.oem_portal.dtos.request.AssignDepartmentHeadRequest;
import com.oem.oem_portal.dtos.request.AssignRecommendationRequest;
import com.oem.oem_portal.dtos.request.DepartmentRequest;
import com.oem.oem_portal.dtos.response.AdminDashboardResponse;
import com.oem.oem_portal.dtos.response.BankerResponse;
import com.oem.oem_portal.dtos.response.DepartmentResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.dtos.response.VendorResponse;
import com.oem.oem_portal.enums.BankerStatus;
import com.oem.oem_portal.enums.RecommendationStatus;
import com.oem.oem_portal.enums.Role;
import com.oem.oem_portal.exception.DuplicateResourceException;
import com.oem.oem_portal.exception.ResourceNotFoundException;
import com.oem.oem_portal.model.Banker;
import com.oem.oem_portal.model.Department;
import com.oem.oem_portal.model.DepartmentHead;
import com.oem.oem_portal.model.Recommendation;
import com.oem.oem_portal.model.Vendor;
import com.oem.oem_portal.repo.BankerRepository;
import com.oem.oem_portal.repo.DepartmentHeadRepository;
import com.oem.oem_portal.repo.DepartmentRepository;
import com.oem.oem_portal.repo.VendorRepository;
import com.oem.oem_portal.service.AdminService;
import com.oem.oem_portal.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final RecommendationRepository recommendationRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentHeadRepository departmentHeadRepository;
    private final BankerRepository bankerRepository;
    private final VendorRepository vendorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;


    @Override
    public AdminDashboardResponse getDashboard() {

    List<Recommendation> allRecommendations = recommendationRepository.findAll();

    return AdminDashboardResponse.builder()
            .totalDepartments(departmentRepository.count())
            .totalDepartmentHeads(departmentHeadRepository.count())
            .totalBankers(bankerRepository.count())
            .pendingApprovals(
                bankerRepository.findByStatus(BankerStatus.PENDING).size()
            )
            .totalVendors(vendorRepository.count())
            
            .totalRecommendations(allRecommendations.size())
            .uploadedRecommendations(
                allRecommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.UPLOADED)
                    .count()
            )
            .assignedRecommendations(
                allRecommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.DEPARTMENT_ASSIGNED
                            || r.getStatus() == RecommendationStatus.ASSIGNED)
                    .count()
            )
            .reviewedRecommendations(
                allRecommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.REVIEWED)
                    .count()
            )
            .verifiedRecommendations(
                allRecommendations.stream()
                    .filter(r -> r.getStatus() == RecommendationStatus.VERIFIED)
                    .count()
            )
        .build();
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if(departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Department already exists");
        }

        Department department = Department.builder()
            .name(request.getName())
            .description(request.getDescription())
            .active(true)
            .build();
        departmentRepository.save(department);

        return mapToDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {

        Department department = departmentRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Department not found with id: " + id)
            );
        department.setName(request.getName());
        department.setDescription(request.getDescription());

        departmentRepository.save(department);

        return mapToDepartmentResponse(department);

    }

@Override
public DepartmentResponse toggleDepartmentStatus(Long id) {

    Department department = departmentRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Department not found with id: " + id)
            );

    department.setActive(!department.isActive());
    departmentRepository.save(department);

    return mapToDepartmentResponse(department);
}

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
            .stream()
            .map(this::mapToDepartmentResponse)
            .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Department not found with id: " + id)
            );
        return mapToDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse assignDepartmentHead(AssignDepartmentHeadRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Department not found")
            );
        if(departmentHeadRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        if(department.getDepartmentHead() != null) {
            throw new DuplicateResourceException(
                "Department already has a head assigned"
            );
        }

        DepartmentHead dh = DepartmentHead.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.DEPARTMENT_HEAD)
            .build();

        departmentHeadRepository.save(dh);

        department.setDepartmentHead(dh);
        departmentRepository.save(department);
        
        return mapToDepartmentResponse(department);
    }

    @Override
    public List<BankerResponse> getPendingBankers() {
        return bankerRepository.findByStatus(BankerStatus.PENDING)
            .stream()
            .map(this::mapToBankerResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<BankerResponse> getAllBankers() {
        return bankerRepository.findAll()
            .stream()
            .map(this::mapToBankerResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BankerResponse approveBanker(Long bankerId) {
        Banker banker = bankerRepository.findById(bankerId)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Banker with this is not found: " + bankerId)
            );
        banker.setStatus(BankerStatus.ACTIVE);
        bankerRepository.save(banker);

        auditLogService.log(
            "BANKER_APPROVED",
            "admin",
            "ADMIN",
            null,
            "PENDING",
            "ACTIVE",
            "Admin approved banker: " + banker.getUsername()
        );

        return mapToBankerResponse(banker);
    }

    @Override
    public BankerResponse rejectBanker(Long bankerId) {
        Banker banker = bankerRepository.findById(bankerId)
            .orElseThrow(() ->
                new ResourceNotFoundException("Banker not found with id: " + bankerId)
            );
        banker.setStatus(BankerStatus.INACTIVE);
        bankerRepository.save(banker);

        auditLogService.log(
            "BANKER_REJECTED",
            "admin",
            "ADMIN",
            null,
            "PENDING",
            "INACTIVE",
            "Admin rejected banker: " + banker.getUsername()
        );

        return mapToBankerResponse(banker);
    }

    @Override
    public BankerResponse assignBankerToDepartment(AssignBankerRequest request) {
        Banker banker = bankerRepository.findById(request.getBankerId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Banker not found")
            );
        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> 
                new ResourceNotFoundException("Department not found")
            );
        
        if(banker.getStatus() != BankerStatus.ACTIVE) {
            throw new IllegalStateException(
                "Banker must be approved beofore assigning to department"
            );
        }
        
        banker.setDepartment(department);
        bankerRepository.save(banker);

        return mapToBankerResponse(banker);
    }

    @Override
    public List<VendorResponse> getAllVendors() {
        return vendorRepository.findAll()
            .stream()
            .map(this::mapToVendorResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<RecommendationResponse> getAllRecommendations() {
        return recommendationRepository.findAll()
            .stream()
            .map(this::mapToRecommendationResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<RecommendationResponse> getUploadedRecommendations() {
        return recommendationRepository
            .findByStatus(RecommendationStatus.UPLOADED)
            .stream()
            .map(this::mapToRecommendationResponse)
            .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse assignRecommendationToDepartment(AssignRecommendationRequest request) {
        Recommendation recommendation = recommendationRepository.findById(request.getRecommendationId())
            .orElseThrow(() -> 
                new ResourceNotFoundException("Recommendation not found")
            );
        
        if(recommendation.getStatus() != RecommendationStatus.UPLOADED) {
                throw new IllegalStateException(
                    "Recommendation has already been assigned"
                );
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> 
                new ResourceNotFoundException("Department not found")
            );
        if(!department.isActive()) {
            throw new IllegalStateException("Cannot assign to disable department");
        }

        if(department.getDepartmentHead() == null) {
            throw new IllegalStateException(
                "Department must have a head before receiving recommendations"
            );
        }

        recommendation.setDepartment(department);
        recommendation.setStatus(RecommendationStatus.DEPARTMENT_ASSIGNED);
        recommendationRepository.save(recommendation);

        auditLogService.log(
            "RECOMMENDATION_ASSIGNED_TO_DEPARTMENT",
            "admin",
            "ADMIN",
            recommendation.getId(),
            "UPLOADED",
            "DEPARTMENT_ASSIGNED",
            "Admin assigned recommendation to department: " + department.getName()
        );

        return mapToRecommendationResponse(recommendation);

    }
    
    @Override
    public List<RecommendationResponse> getReviewedRecommendations() {
        return recommendationRepository
            .findByStatus(RecommendationStatus.REVIEWED)
            .stream()
            .map(this::mapToRecommendationResponse)
            .collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse verifyRecommendation(Long recommendationId) {
        Recommendation recommendation = recommendationRepository
            .findById(recommendationId)
            .orElseThrow(() -> 
                new ResourceNotFoundException("Recommendation not found")
            );
        
        if(recommendation.getStatus() != RecommendationStatus.REVIEWED) {
            throw new IllegalStateException(
                "Recommendation must be reviewed before verification"
            );
        }

        recommendation.setStatus(RecommendationStatus.VERIFIED);
        recommendationRepository.save(recommendation);

        auditLogService.log(
            "RECOMMENDATION_VERIFIED",
            "admin",
            "ADMIN",
            recommendation.getId(),
            "REVIEWED",
            "VERIFIED",
            "Admin verified recommendation"
        );

        return mapToRecommendationResponse(recommendation);
    }




    


    private DepartmentResponse mapToDepartmentResponse(Department department) {
        List<BankerResponse> bankerResponses = Collections.emptyList();

        if(department.getBankers() != null) {
            bankerResponses = department.getBankers()
                .stream()
                .map(this::mapToBankerResponse)
                .collect(Collectors.toList());

        }

        return DepartmentResponse.builder()
            .id(department.getId())
            .name(department.getName())
            .description(department.getDescription())
            .active(department.isActive())
            .departmentHeadName(
                department.getDepartmentHead() != null ? department.getDepartmentHead().getUsername() : null
            )
            .deaprtmentHeadEmail(
                department.getDepartmentHead() != null ? department.getDepartmentHead().getEmail() : null
            )
            .bankers(bankerResponses)
            .build();
    }

    private BankerResponse mapToBankerResponse(Banker banker) {
        return BankerResponse.builder()
            .id(banker.getId())
            .username(banker.getUsername())
            .email(banker.getEmail())
            .status(banker.getStatus())
            .departmentName(
                banker.getDepartment() != null ? banker.getDepartment().getName() : null
            )
            .build();
    }

    private VendorResponse mapToVendorResponse(Vendor vendor) {
        return VendorResponse.builder()
            .id(vendor.getId())
            .name(vendor.getName())
            .email(vendor.getEmail())
            .phone(vendor.getPhone())
            .totalRecommendations(
                recommendationRepository.findByVendorId(vendor.getId()).size()
            )
            .build();
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
