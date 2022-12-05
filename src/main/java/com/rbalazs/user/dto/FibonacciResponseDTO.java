package com.rbalazs.user.dto;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FibonacciResponseDTO {
    int serialNumber;
    BigInteger fibonacciNumber;
}
