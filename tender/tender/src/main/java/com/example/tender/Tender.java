package com.example.tender;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tender {
    private String refNo;
    private String title;
    private String tenderValue;
    private String bidSubmissionEndDate;
    private String emd;
    private String bidOpenDate;
}
