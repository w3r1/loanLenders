package com.loanlenders.service;

import com.loanlenders.domain.Loan;
import com.loanlenders.domain.LoanLender;
import com.loanlenders.exception.UnsufficientAvailableLoans;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.apache.commons.lang3.StringUtils.join;

public class LoanLendersServiceImpl implements LoanLendersService {

    private static final Logger LOGGER = Logger.getLogger(LoanLendersServiceImpl.class.getName());

    private static final double LOAN_MONTHS = 36d;

    @Override
    public Loan getCheapestLoanLender(final List<LoanLender> loanLenders, final double requestedAmount) {

        Collections.sort(loanLenders);

        double rateAverage = 0d;
        double reservedAmount = 0d;
        for (LoanLender loanLender : loanLenders) {

            double stillNeededAmount = requestedAmount - reservedAmount;

            double lendersAvailableAmount = (double) loanLender.getAvailableAmount();
            double lendersRate = loanLender.getRate();

            boolean isAvailableSufficient = stillNeededAmount <= lendersAvailableAmount;
            if (isAvailableSufficient) {

                rateAverage += stillNeededAmount * lendersRate;
                reservedAmount += stillNeededAmount;
                break;
            } else {

                rateAverage += lendersAvailableAmount * lendersRate;
                reservedAmount += lendersAvailableAmount;
            }
        }

        boolean areLoansSufficient = requestedAmount == reservedAmount;
        if (!areLoansSufficient) {

            LOGGER.warning(join("Unsufficient available loans to fullfil ", requestedAmount));
            throw new UnsufficientAvailableLoans("It is not possible to provide a quote at this time.");
        }

        rateAverage = rateAverage / requestedAmount;
        double monthlyRepayment = getMonthlyPayment(rateAverage, requestedAmount);

        return new Loan((int) requestedAmount, rateAverage, monthlyRepayment,  monthlyRepayment * LOAN_MONTHS);
    }

    private double getMonthlyPayment(double rate, double requestedAmount) {
        double effectiveRate = Math.pow(1d + rate, 1d / 12d) - 1d;
        return (effectiveRate * requestedAmount) / (1d - Math.pow(1d + effectiveRate, - LOAN_MONTHS));
    }
}