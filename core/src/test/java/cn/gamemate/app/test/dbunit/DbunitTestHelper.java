package cn.gamemate.app.test.dbunit;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

@Configurable
public class DbunitTestHelper {
	

	@Autowired
	DataSource dataSource;
	
	
	@Autowired
	ApplicationContext ctx;
	
	List<IDataSet> dataSets =  Lists.newArrayList();
	IDataSet OneDataSet;
	

	private DataSourceDatabaseTester dbunitTester;
	
	public DbunitTestHelper load(String dataSetFileName){
		IDataSet dataSet;
		try {
			dataSet = new FlatXmlDataSetBuilder()
			.build(ctx.getResource("user10.xml").getInputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		dataSets.add(dataSet);
		return this;
	}
	
	public DbunitTestHelper setup(){
		if (dataSets.size() == 0){
			return this;
		}else if (dataSets.size() == 1){
			OneDataSet = dataSets.get(0);
		}else{
			try {
				OneDataSet = new CompositeDataSet((IDataSet[])dataSets.toArray());
			} catch (DataSetException e) {
				throw new RuntimeException(e);
			}
		}
		dbunitTester = new DataSourceDatabaseTester(
				dataSource);
		dbunitTester.setDataSet(OneDataSet);
		try {
			dbunitTester.onSetup();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

}
