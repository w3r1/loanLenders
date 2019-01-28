package com.loanlenders.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoanLender implements Comparable<LoanLender> {

    private String lender;

    private double rate;

    private int availableAmount;

    @Override
    public int compareTo(LoanLender o) {

        if (this.rate > o.rate) {
            return 1;
        } else if (this.rate < o.rate) {
            return -1;
        } else {
            return 0;
        }
    }
}
