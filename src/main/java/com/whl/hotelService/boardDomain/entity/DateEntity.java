package com.whl.hotelService.boardDomain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 상속받아서 사용해야함
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class DateEntity {
    @CreationTimestamp //생성됬을 때 시간 작성Date.Now()와 같음
    @Column(updatable = false) // 수정됬을 때는 관여 안함
    private LocalDateTime createdTime;
    @UpdateTimestamp // 수정했을 때 시간 작성
    @Column(insertable = false) // 작성했을 때는 관여 안함
    private LocalDateTime updatedTime;
}
