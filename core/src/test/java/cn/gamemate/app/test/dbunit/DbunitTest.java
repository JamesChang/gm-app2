package cn.gamemate.app.test.dbunit;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.gamemate.app.domain.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-rdbms.xml" })
public class DbunitTest {

	@Autowired
	DataSource dataSource;
	
	
	@Autowired
	ApplicationContext ctx;
	
	private DataSourceDatabaseTester dbunitTester;

	@Test
	public void testSingleLoad() throws Exception{
		//setup
		dbunitTester = new DataSourceDatabaseTester(
				dataSource);
		IDataSet dataSet = new FlatXmlDataSetBuilder()
				.build(ctx.getResource("user10.xml").getInputStream());
		dbunitTester.setDataSet(dataSet);
		dbunitTester.onSetup();
		
		//my work
		Assert.assertTrue(true);
		User user = User.findUser(64);
		Assert.assertNotNull(user);
		
		// will call default tearDownOperation
		dbunitTester.onTearDown();
	}

}
