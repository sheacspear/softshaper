package ru.zorb.services.report;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import ru.zorb.services.meta.ContentDataSource;
import ru.zorb.services.report.impl.ReportImpl;
import ru.zorb.services.report.impl.UniversalDynamicContentListReport;

@Component
public class ReportService implements IReportService {

  //@Autowired
  private ContentDataSource<Record> dataSource;

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
