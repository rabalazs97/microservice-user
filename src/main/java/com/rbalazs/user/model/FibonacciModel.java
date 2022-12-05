package com.rbalazs.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="fibonacci")
public class FibonacciModel {
    @Id
    @SequenceGenerator(
            name = "fibonacci_id_sequence",
            sequenceName = "fibonacci_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fibonacci_id_sequence"
    )
    @Column
    private int id;

    @Column
    private int serialNumber;

    @Lob
    @Column
    private byte[] fibonacciNumber;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private UserModel userId;
}
