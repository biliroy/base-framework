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
 * 测试资源管理功能
 * 
 * @author vincent
 *
 */
public class TestResourceManagerFunction extends FunctionTestCaseSupport{
	
	@Test
	public void doTest() {
		s.open("/");
		//通过点击，进入功能模块
		s.click(By.id("SJDK3849CKMS3849DJCK2039ZMSK0003"));
		s.click(By.id("SJDK3849CKMS3849DJCK2039ZMSK0010"));
		
		//获取table中的所有操作前的tr
		List<WebElement> beforeTrs = s.findElements(By.xpath("//table//tbody//tr"));
		//断言所有tr是否等于期望值
		assertEquals(beforeTrs.size(), 10);
		
		//打开添加页面
		s.click(By.xpath("//a[@href='/exitsoft-basic-curd/account/resource/read']"));
		//填写表单
		s.getSelect(By.xpath("//form[@id='save-resource-form']//select[@name='type']")).selectByValue("01");
		s.type(By.xpath("//form[@id='save-resource-form']//input[@name='value']"), "/test/**");
		s.type(By.xpath("//form[@id='save-resource-form']//input[@name='permission']"), "perms[test:test]");
		s.type(By.xpath("//form[@id='save-resource-form']//textarea[@name='remark']"), "这是一个测试的资源");
		
		//提交表单，页面验证不通过
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		//设置最后的一个值
		s.type(By.xpath("//form[@id='save-resource-form']//input[@name='name']"), "test_menu");
		//验证通过，提交表单
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		
		//返回成功信息
		String message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("保存成功"));
		
		//点击编辑功能
		s.findElement(By.xpath("//*//td[contains(text(),'test_menu')]//..//a")).click();
		//填写表单
		s.type(By.xpath("//form[@id='save-resource-form']//input[@name='name']"), "test_menu_modify");
		s.getSelect(By.xpath("//form[@id='save-resource-form']//select[@name='type']")).selectByValue("02");
		
		//提交表单
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		
		//返回成功信息
		message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("保存成功"));
		
		//选中删除的记录
		s.check(By.xpath("//*//td[contains(text(),'test_menu_modify')]//..//input"));
		//提交删除表单
		s.click(By.xpath("//div[@class='panel-footer']//*[@type='submit']"));
		Alert alert = s.getDriver().switchTo().alert();
		alert.accept();
		
		//返回成功信息
		message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("删除1条信息成功"));
		
		//打开查询框
		s.click(By.xpath("//div[@class='panel-footer']//*[@data-toggle='modal']"));
		s.waitForVisible(By.id("search-modal"));
		
		//设置查询条件值
		s.type(By.id("filter_LIKES_name"), "查看");
		s.getSelect(By.name("filter_EQS_type")).selectByValue("02");
		s.type(By.id("filter_LIKES_value"), "/foundation/");
		s.type(By.id("filter_LIKES_permission"), "operating-record");
		s.getSelect(By.name("filter_EQS_parent.id")).selectByValue("SJDK3849CKMS3849DJCK2039ZMSK0026");
		
		//查询
		s.click(By.xpath("//div[@class='modal-footer']//button[@type='submit']"));
		
		List<WebElement> aflterTrs = s.findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
		//断言查询后的记录数
		assertEquals(aflterTrs.size(), 1);
	}
	
}
