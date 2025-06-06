package com.example.tender;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenders")
public class TenderController {

    @Autowired
    private TenderScraperService scraperService;

    @GetMapping("/scrape")
    public List<Tender> scrapeTenders() {
        return scraperService.scrapeTenders();
    }
}
