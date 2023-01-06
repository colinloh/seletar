package com.canabrix.flightlog;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.canabrix.elasticsearch.CanabrixElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FlightlogElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(FlightlogElasticSearchService.class);
    public static final String INDEX_NAME = "logbooks";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static final Date DEFAULT_START_DATE;

    static {
        try {
            // Default start date: Wright Brothers' first flight on Dec 17 1903.
            DEFAULT_START_DATE = sdf.parse("1903-12-17");
        } catch (ParseException e) {
            throw new RuntimeException("Failed to define default start date", e);
        }
    }

    public FlightlogElasticSearchService() {
        super();
    }

    public Flightlog queryFlightlog() throws IOException {
        return queryFlightLog(DEFAULT_START_DATE, new Date());
    }

    public static final int SEARCH_RESULT_SIZE = 9999;

    public Flightlog queryFlightLog(Date startDate, Date endDate) throws IOException {
        JsonData startStr = JsonData.of(sdf.format(startDate));
        JsonData endStr = JsonData.of(sdf.format(endDate));
        if (logger.isDebugEnabled()) {
            logger.debug("Searching flight logs from: "
                    + startStr
                    + " to: "
                    + endStr
            );
        }

        SortOptions sortOptions = new SortOptions.Builder()
                .field(
                        f -> f.field("entryDate")
                                .order(SortOrder.Desc))
                .build();

        SearchResponse<FlightlogEntry> response = CanabrixElasticSearchService.getClient().search(
                s -> s
                        .index(INDEX_NAME)
                        .query(q -> q
                                .bool(b -> b
                                        .filter(f -> f
                                                .range(r -> r
                                                        .field("entryDate")
                                                        .gte(startStr)
                                                        .lte(endStr)
                                                )
                                        )
                                )
                        )
                        .size(SEARCH_RESULT_SIZE)
                        .sort(sortOptions)
                        ,
                FlightlogEntry.class
        );

        Flightlog flightlog = new Flightlog();
        response.hits().hits().forEach(
                h -> flightlog.addEntry(h.source())
        );
        return flightlog;
    }

    /**
     * @param flightlog The flight log we wish to insert into Elastic Search
     * @return The number of flights which has successfully been inserted
     */
    public int index(Flightlog flightlog) {
        int attempts = 0;
        int inserted = 0;
        for (FlightlogEntry fle : flightlog.getEntries()) {
            try {
                attempts++;
                index(fle);
                inserted++;
            } catch (IOException e) {
                logger.info("Failed to insert into Elastic Search Flightlog Event #: " + attempts);
            }
        }
        logger.info("Insertion of Flightlog Events into Elastic Search. Attempts: " + attempts + " Inserted: " + inserted);

        return inserted;
    }

    private void index(FlightlogEntry entry) throws IOException {
        esService.insertDocument(
                INDEX_NAME,
                ElasticSearchDocumentIdUtil.deriveDocId(entry),
                entry
        );
    }

    @Autowired
    private CanabrixElasticSearchService esService;

}
