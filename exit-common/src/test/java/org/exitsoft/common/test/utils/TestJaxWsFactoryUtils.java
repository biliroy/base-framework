package org.exitsoft.common.test.utils;

import org.exitsoft.common.utils.JaxWsFactoryUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * JaxWsFactory工具类单元测试
 * 
 * @author vincent
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestJaxWsFactoryUtils {
	
	@Test
	public void testInvoke() throws Exception {
		Object[] result = null;
		
		//首次调用，CXF会创建动态客户端，效率相当低。
		result = JaxWsFactoryUtils.invoke("http://webservice.webxml.com.cn/webservices/mobilecodews.asmx?wsdl", 
										  "getMobileCodeInfo", 
										  "18776974353",
										  null);
		
		Assert.assertEquals(result[0],"18776974353：广西 南宁 广西移动全球通卡");
		
		//二次调用，不在创建动态客户端，速度比首次快很多。
		result = JaxWsFactoryUtils.invoke("http://webservice.webxml.com.cn/webservices/mobilecodews.asmx?wsdl", 
				  "getMobileCodeInfo", 
				  "15277108562",
				  null);
		
		Assert.assertEquals(result[0],"15277108562：广西 南宁 广西移动动感地带卡");
		
		//刷新所有动态客户端
		JaxWsFactoryUtils.refreshClientMap();
		//刷新单个动态客户端
		JaxWsFactoryUtils.refreshClient("http://webservice.webxml.com.cn/webservices/mobilecodews.asmx?wsdl");
		
	}
	
}
