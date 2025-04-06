package com.example.its.service;

import com.example.its.domain.issue.IssueEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class CsvExportService {

    public void exportIssueToCsv(IssueEntity issue, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"issue_" + issue.getId() + ".csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID,Summary,Description,Liked");
            writer.println(issue.getId() + "," + issue.getSummary() + "," + issue.getDescription() + "," + issue.isLiked());
        }
    }
}