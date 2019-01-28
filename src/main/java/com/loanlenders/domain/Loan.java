package com.loanlenders.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Loan {

    private int requestedAmount;

    private double rate;

    private double monthlyRepayment;

    private double totalRepayment;
}
