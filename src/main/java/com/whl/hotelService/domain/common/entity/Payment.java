package com.whl.hotelService.domain.common.entity;

import com.whl.hotelService.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="payment")
public class Payment {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name="reservation_id", foreignKey = @ForeignKey(name="fk_Payment_reservation_id", foreignKeyDefinition = "FOREIGN KEY(reservation_id) REFERENCES reservation(id) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)

    private Reservation reservation_id; // 예약 정보

    @ManyToOne
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name="fk_Payment_user_id", foreignKeyDefinition = "FOREIGN KEY(user_id) REFERENCES user(user_id) ON DELETE CASCADE ON UPDATE CASCADE"), nullable = false)
    private User user_id; // 사용자 정보

    @Column(nullable = false)
    private String imp_uid;
    @Column(nullable = false)
    private String merchant_uid;
    @Column(nullable = false)
    private String pay_method;
    @Column(nullable = false)
    private String paid_amount;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false ,columnDefinition = "DATETIME(6)")
    private Date payDate;
}