package com.loanlenders.controller;

import com.loanlenders.domain.Loan;
import com.loanlenders.domain.LoanLender;
import com.loanlenders.domain.LoanResponse;
import com.loanlenders.exception.NoLenderException;
import com.loanlenders.exception.ProvidedFileHasErrorException;
import com.loanlenders.exception.UnsufficientAvailableLoans;
import com.loanlenders.reader.LoanLendersCsvReader;
import com.loanlenders.service.LoanLendersService;
import com.loanlenders.service.LoanLendersServiceImpl;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.join;

public class LoanLendersController {

    private static final int AMOUNT_STEPS = 100;
    private static final int AMOUNT_MIN = 1000;
    private static final int AMOUNT_MAX = 15000;

    private LoanLendersCsvReader loanLendersCsvReader;

    private LoanLendersService loanLendersService;

    public LoanLendersController() {
        loanLendersCsvReader = new LoanLendersCsvReader();
        loanLendersService = new LoanLendersServiceImpl();
    }

    public LoanResponse getCheapestLoanLender(String loanLenderFilePath, int requestedAmount) {

        LoanResponse errorResponse = validateParameters(loanLenderFilePath, requestedAmount);
        if (errorResponse != null) {
            return errorResponse;
        }

        try {
            final List<LoanLender> lenders = loanLendersCsvReader.readLoanLendersFromCsv(loanLenderFilePath);
            Loan cheapestLoan = loanLendersService.getCheapestLoanLender(lenders, requestedAmount);

            return new LoanResponse(cheapestLoan);

        } catch (ProvidedFileHasErrorException | NoLenderException | UnsufficientAvailableLoans ex) {
            return new LoanResponse(ex.getMessage());
        }
    }

    private LoanResponse validateParameters(String loanLenderFilePath, int requestedAmount) {

        if (isBlank(loanLenderFilePath)) {
            return new LoanResponse("Please provide a valid path to a lenders csv file.");
        }

        boolean isAmountDivBy10 = requestedAmount % AMOUNT_STEPS == 0;
        if (!isAmountDivBy10) {
            return new LoanResponse("Please provide an amount in 100£ steps.");
        }

        boolean isAmountBetweenMinMax = requestedAmount >= AMOUNT_MIN && requestedAmount <= AMOUNT_MAX;
        if (!isAmountBetweenMinMax) {
            return new LoanResponse(join("Please provide an amount between ", AMOUNT_MIN, " and ", AMOUNT_MAX));
        }

        return null;
    }
}
