package com.loanlenders.domain;

import lombok.Getter;

import static java.lang.String.format;

@Getter
public class LoanResponse {

    private static final String LINE_END = System. getProperty("line.separator");

    private boolean successful;

    private String errorMessage;

    private Loan cheapestLoan;

    public LoanResponse(Loan cheapestLoan) {
        this.cheapestLoan = cheapestLoan;
        this.successful = true;
    }

    public LoanResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.successful = false;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        if (successful) {
            buffer.append("Requested amount: £").append(cheapestLoan.getRequestedAmount()).append(LINE_END);
            buffer.append("Rate: ").append(format("%.1f", cheapestLoan.getRate() * 100f)).append("%").append(LINE_END);
            buffer.append("Monthly repayment: £").append(format("%.2f", cheapestLoan.getMonthlyRepayment())).append(LINE_END);
            buffer.append("Total repayment: £").append(format("%.2f", cheapestLoan.getTotalRepayment())).append(LINE_END);
        } else {
            buffer.append(errorMessage).append(LINE_END);
        }

        return buffer.toString();
    }
}
