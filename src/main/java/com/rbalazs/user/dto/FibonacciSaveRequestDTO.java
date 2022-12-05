package com.rbalazs.user.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FibonacciSaveRequestDTO implements Serializable {
    private int serialNumber;
    private byte[] fibonacciNumber;
    private int userId;
}
