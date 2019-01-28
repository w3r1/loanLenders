package com.loanlenders;

import com.loanlenders.controller.LoanLendersController;

public class LoanLendersMain {

    public static final void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: cmd> [application] [market_file:Text] [loan_amount:Number]");
            return;
        }

        String filePath = args[0];
        int requestedAmount = 0;
        try {
            requestedAmount = Integer.valueOf(args[1]);
        } catch (NumberFormatException ex) {
            System.out.println("Usage: cmd> [application] [market_file:Text] [loan_amount:Number]");
            System.out.println("Provide a number please on the second parameter.");
        }

        System.out.println(
                new LoanLendersController().getCheapestLoanLender(filePath, requestedAmount).toString()
        );
    }
}
