package com.canabrix.controllers;

import com.canabrix.flightlog.Flightlog;
import com.canabrix.flightlog.FlightlogElasticSearchService;
import com.canabrix.flightlog.FlightlogParseException;
import com.canabrix.flightlog.FlightlogS3ReaderService;
import com.canabrix.flightlog.reports.Report;
import com.canabrix.flightlog.reports.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/flightlog")
public class FlightlogController {

    @Autowired
    private FlightlogElasticSearchService flightlogElasticSearchService;

    @Autowired
    private FlightlogS3ReaderService flightlogS3ReaderService;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @GetMapping("/latestS3Key")
    public Map<String, String> getLatestS3Key() {
        Map<String, String> results = new HashMap<>();
        String latestKey = flightlogS3ReaderService.findLatestFlightlogKey();
        results.put("File name: ", latestKey);
        return results;
    }

    @PostMapping("/updateLatestFromS3")
    public Map<String, Object> updateLatestFromS3() throws FlightlogParseException {
        Map<String, Object> response = new HashMap<>();
        String latestKey = flightlogS3ReaderService.findLatestFlightlogKey();
        response.put("File name: ", latestKey);
        Flightlog fl = flightlogS3ReaderService.createFlightlogFromS3File(latestKey);
        response.put("number of entries read", fl.getNumEntries());
        int numIndexed = flightlogElasticSearchService.index(fl);
        response.put("number of entries indexed", numIndexed);
        return response;
    }

    @PostMapping("/updateFromS3")
    public Map<String, Object> processLogbook(
            @RequestParam(name = "key") String key
    ) throws FlightlogParseException {
        Map<String, Object> response = new HashMap<>();
        Flightlog fl = flightlogS3ReaderService.createFlightlogFromS3File(key);
        response.put("number of entries", fl.getNumEntries());
        return response;
    }

    @GetMapping
    public List<Report> getReports() throws IOException {

        Flightlog flightlog = flightlogElasticSearchService.queryFlightlog();
        return reportGenerationService.generateReports(flightlog);

    }

}
