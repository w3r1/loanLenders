package com.loanlenders.service;

import com.loanlenders.domain.Loan;
import com.loanlenders.domain.LoanLender;

import java.util.List;

public interface LoanLendersService {

    Loan getCheapestLoanLender(List<LoanLender> loanLenders, double requestedAmount);
}
