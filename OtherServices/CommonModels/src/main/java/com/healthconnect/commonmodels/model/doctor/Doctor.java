package com.healthconnect.commonmodels.model.doctor;

import com.healthconnect.commonmodels.model.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String specialty;

    private Integer yearsOfExperience;
    private String education;
    private Double consultationFee;
    private Double ratings;

    @Column(length = 2000)
    private String bio;

    private Long userId;
    private Boolean isActive;

    @ElementCollection
    @CollectionTable(name = "doctor_hospitals", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "hospital_id")
    private List<Long> hospitalIds;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doctor_id")
    private List<DoctorAvailability> availabilities;

    @PrePersist
    protected void onCreate() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }
}
