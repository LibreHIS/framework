package ims.framework;

/**
 * @author mmihalec
 */
public interface IReportDataProvider 
{
	boolean canProvideData(IReportSeed[] reportSeeds);
	boolean hasData(IReportSeed[] reportSeeds);
	IReportField[] getData(IReportSeed[] reportSeeds);	
	IReportField[] getData(IReportSeed[] reportSeeds, boolean excludeNulls);
}
