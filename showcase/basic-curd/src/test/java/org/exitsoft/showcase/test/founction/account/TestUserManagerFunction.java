package org.exitsoft.showcase.test.founction.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.exitsoft.showcase.test.founction.FunctionTestCaseSupport;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 测试用户管理功能
 * 
 * @author vincent
 *
 */
public class TestUserManagerFunction extends FunctionTestCaseSupport{
	
	@Test
	public void doTest() {
		s.open("/");
		//通过点击，进入功能模块
		s.click(By.id("SJDK3849CKMS3849DJCK2039ZMSK0003"));
		s.click(By.id("SJDK3849CKMS3849DJCK2039ZMSK0004"));
		
		//获取table中的所有操作前的tr
		List<WebElement> beforeTrs = s.findElements(By.xpath("//table//tbody//tr"));
		//断言所有tr是否等于期望值
		assertEquals(beforeTrs.size(), 5);
		
		//打开添加页面
		s.click(By.xpath("//a[@href='/exitsoft-basic-curd/account/user/read']"));
		//填写表单
		s.type(By.xpath("//form[@id='create-user-form']//input[@name='username']"), "admin");
		s.type(By.xpath("//form[@id='create-user-form']//input[@name='realname']"), "vincent.chen");
		s.type(By.xpath("//form[@id='create-user-form']//input[@name='password']"), "admin");
		s.type(By.xpath("//form[@id='create-user-form']//input[@name='confirmPassword']"), "admin");
		s.getSelect(By.xpath("//form[@id='create-user-form']//select[@name='state']")).selectByValue("1");
		s.type(By.xpath("//form[@id='create-user-form']//input[@name='email']"), "es.chenxiaobo@gmail.com");
		
		//选中所有复选框
		s.check(s.findElement(By.id("selectAll")));
		//提交表单，页面验证不通过
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		//设置最后的一个值
		s.type(By.xpath("//form[@id='create-user-form']//input[@name='username']"), "test_admin");
		//验证通过，提交表单
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		
		//返回成功信息
		String message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("新增成功"));
		
		//获取table中的所有操作前的tr
		List<WebElement> aflterTrs = s.findElements(By.xpath("//table//tbody//tr"));
		//添加成功后应该比开始的记录多一条
		assertEquals(aflterTrs.size(), beforeTrs.size() + 1);
		
		//点击编辑功能
		s.findElement(By.xpath("//table//tbody//tr//*[text()='test_admin']//..//a")).click();
		//填写表单
		s.type(By.xpath("//form[@id='update-user-form']//input[@name='realname']"), "test_realname");
		s.getSelect(By.xpath("//form[@id='update-user-form']//select[@name='state']")).selectByValue("2");
		//选中所有复选框
		for (WebElement element : s.findElements(By.name("groupId"))) {
			s.uncheck(element);
		}
		
		//提交表单
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		
		//返回成功信息
		message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("修改成功"));
		
		aflterTrs = s.findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
		//添加成功后应该比开始的记录多一条
		assertEquals(aflterTrs.size(), beforeTrs.size() + 1);
		
		//选中删除的记录
		s.check(By.xpath("//table//tbody//tr//*[text()='test_admin']//..//input"));
		//提交删除表单
		s.click(By.xpath("//div[@class='panel-footer']//*[@type='submit']"));
		Alert alert = s.getDriver().switchTo().alert();
		alert.accept();
		
		//返回成功信息
		message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("删除1条信息成功"));
		
		aflterTrs = s.findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
		//删除成功后应该刚刚开始的记录一样
		assertEquals(aflterTrs.size(), beforeTrs.size());
		
		//打开查询框
		s.click(By.xpath("//div[@class='panel-footer']//*[@data-toggle='modal']"));
		s.waitForVisible(By.id("search-modal"));
		
		//设置查询条件值
		s.type(By.id("filter_RLIKES_username"), "admin");
		s.type(By.id("filter_RLIKES_realname"), "vincent");
		s.getSelect(By.name("filter_EQI_state")).selectByValue("1");
		s.type(By.id("filter_RLIKES_email"), "es");
		
		//查询
		s.click(By.xpath("//div[@class='modal-footer']//button[@type='submit']"));
		
		aflterTrs = s.findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
		//断言查询后的记录数
		assertEquals(aflterTrs.size(), 1);
	}
	
}
