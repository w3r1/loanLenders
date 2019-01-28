package com.loanlenders.reader;

import com.loanlenders.domain.LoanLender;
import com.loanlenders.exception.ProvidedFileHasErrorException;
import io.vavr.control.Try;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;

public class LoanLendersCsvReader {

    private static final Logger LOGGER = Logger.getLogger(LoanLendersCsvReader.class.getName());

    public List<LoanLender> readLoanLendersFromCsv(String loanLenderFilePath) {

        Reader loanLenderFileReader = Try.of(() -> Files.newBufferedReader(Paths.get(loanLenderFilePath)))
                .onFailure(ex -> {
                    LOGGER.log(SEVERE, "File on provided path cannot be read.", ex);
                    throw new ProvidedFileHasErrorException("File on provided path cannot be read.");
                })
                .get();
        CSVParser loanLenderFileParser = Try.of(() ->
                new CSVParser(loanLenderFileReader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim()))
                .onFailure(ex -> {
                    LOGGER.log(SEVERE, "File on provided path cannot be read.", ex);
                    throw new ProvidedFileHasErrorException("File on provided path cannot be read.");
                })
                .get();

        List<LoanLender> loanLenders = new ArrayList<>();
        for (CSVRecord lenderRecord : loanLenderFileParser) {

            Optional<Double> rate = Try.of(() -> Double.valueOf(getCSVRecord(lenderRecord, "Rate")))
                    .onFailure(ex -> LOGGER.warning(join("Value for Rate is NaN at ", lenderRecord.getRecordNumber(), ".")))
                    .toJavaOptional();
            Optional<Integer> availableAmount = Try.of(() -> Integer.valueOf(getCSVRecord(lenderRecord, "Available")))
                    .onFailure(ex -> LOGGER.warning(join("Value for Available is NaN at ", lenderRecord.getRecordNumber(), ".")))
                    .toJavaOptional();
            if (!rate.isPresent() || !availableAmount.isPresent()) {
                continue;
            }

            loanLenders.add(new LoanLender(getCSVRecord(lenderRecord, "Lender"), rate.get(), availableAmount.get()));
        }

        return loanLenders;
    }

    private String getCSVRecord(CSVRecord lenderRecord, String header) {

        return Try.of(() -> lenderRecord.get(header))
                .onFailure(ex -> LOGGER.log(SEVERE,
                        join("No data found for header ", header, " at ", lenderRecord.getRecordNumber(), "."),
                        ex))
                .toJavaOptional()
                .filter(s -> isNotBlank(s))
                .orElse(null);
    }
}
