package ru.softshaper.services.report;

import org.springframework.stereotype.Component;
import ru.softshaper.services.report.impl.ReportImpl;

@Component
public class ReportService implements IReportService {


    //@Autowired
    //private ViewMapperFactory viewMapperFactory;

    /*
     * (non-Javadoc)
     *
     * @see ru.softshaper.services.report.IReportService#getReport(java.lang.String)
     */
    @Override
    public IReport getReport(String report) {
        return new ReportImpl();
    }
}
