package com.minimo.backend.challenge.domain;

import com.minimo.backend.certification.domain.Certification;
import com.minimo.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@Table(name = "challenges")
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 128)
    private String title;

    private String content;

    private String challengeIcon;

    private int durationDays;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private ChallengeStatus status;

    @OneToMany(mappedBy = "challenge",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<Certification> certifications = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if (this.startDate == null) {
            this.startDate = LocalDate.now();
        }
        if (this.status == null) {
            this.status = ChallengeStatus.ACTIVE;
        }
    }

    public void complete() {
        this.status = ChallengeStatus.COMPLETED;
    }

    public void addCertification(Certification certification) {
        this.certifications.add(certification);
        certification.setChallenge(this);
    }
}
