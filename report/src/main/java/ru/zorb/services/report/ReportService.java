package ru.zorb.services.report;

import org.springframework.stereotype.Component;
import ru.zorb.services.report.impl.ReportImpl;

@Component
public class ReportService implements IReportService {


    //@Autowired
    //private ViewMapperFactory viewMapperFactory;

    /*
     * (non-Javadoc)
     *
     * @see ru.zorb.services.report.IReportService#getReport(java.lang.String)
     */
    @Override
    public IReport getReport(String report) {
        return new ReportImpl();
    }
}
