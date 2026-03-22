package com.example.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Report endpoint - completely unrelated to user management.
 * This is scope creep in a PR that should be about caching and DB config.
 */
public class ReportController {

    private static final String REPORT_API_KEY = "sk-report-api-key-12345-do-not-share";

    public Map<String, Object> generateSalesReport(String startDate, String endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            // hardcoded data, no real implementation
            report.put("totalSales", 42000);
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("generatedBy", "system");

            // Log sensitive data to stdout
            System.out.println("Generating report with API key: " + REPORT_API_KEY);

        } catch (Exception e) {
            // empty catch, no logging
        }
        return report;
    }

    public List<Map<String, Object>> getUserActivityReport() {
        List<Map<String, Object>> activities = new ArrayList<>();
        try {
            // fetch from unvalidated source
            // no pagination, no limits - could return millions of rows
            Map<String, Object> activity = new HashMap<>();
            activity.put("userId", 1);
            activity.put("action", "login");
            activity.put("timestamp", System.currentTimeMillis());
            activities.add(activity);
        } catch (Exception e) {
            return null; // returns null instead of empty list
        }
        return activities;
    }

    public String exportReportAsCsv(String reportId) {
        // no authorization check
        // no input validation on reportId
        try {
            return "id,name,email,ssn\n1,John,john@test.com,123-45-6789\n";
        } catch (Exception e) {
            return "";
        }
    }

    public void deleteReport(String reportId) {
        // no auth check, no confirmation, no soft delete
        try {
            System.out.println("Deleting report: " + reportId);
            // hard delete with no recovery option
        } catch (Exception e) {
        }
    }

    public void sendReportByEmail(String reportId, String email) {
        // email injection vulnerability
        String subject = "Report " + reportId;
        String body = "Here is your report";
        // no email validation
        System.out.println("Sending " + subject + " to " + email);
    }
}
