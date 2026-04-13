package com.cts.rivio.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BulkUploadResponse {
    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private List<String> errors; // Holds specific row error messages
}