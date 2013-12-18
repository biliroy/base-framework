package org.exitsoft.showcase.test.founction.foundation.variable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.exitsoft.showcase.test.founction.FunctionTestCaseSupport;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 测试数据字典管理功能
 * 
 * @author vincent
 *
 */
public class TestDataDictonaryManagerFunction extends FunctionTestCaseSupport{
	
	@Test
	public void doTest() {
		s.open("/");
		//通过点击，进入功能模块
		s.click(By.id("SJDK3849CKMS3849DJCK2039ZMSK0017"));
		s.click(By.id("SJDK3849CKMS3849DJCK2039ZMSK0018"));
		
		//获取table中的所有操作前的tr
		List<WebElement> beforeTrs = s.findElements(By.xpath("//table//tbody//tr"));
		//断言所有tr是否等于期望值
		assertEquals(beforeTrs.size(), 10);
		
		//打开添加页面
		s.click(By.xpath("//a[@href='/exitsoft-basic-curd/foundation/variable/data-dictionary/read']"));
		//填写表单
		s.type(By.xpath("//form[@id='save-data-dictionary-form']//input[@name='name']"), "test_data_dictionary");
		s.type(By.xpath("//form[@id='save-data-dictionary-form']//input[@name='value']"), "01");
		
		s.type(By.xpath("//form[@id='save-data-dictionary-form']//textarea[@name='remark']"),"这是一个测试保存的数据字典记录");
		//提交表单，页面验证不通过
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		//设置最后的值
		s.getSelect(By.xpath("//form[@id='save-data-dictionary-form']//select[@name='categoryId']")).selectByValue("402881e437d467d80137d46fc0e50001");
		s.getSelect(By.xpath("//form[@id='save-data-dictionary-form']//select[@name='type']")).selectByValue("S");
		//验证通过，提交表单
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		
		//返回成功信息
		String message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("保存成功"));
		
		//点击下一页
		s.click(By.xpath("//a[@href='/exitsoft-basic-curd/foundation/variable/data-dictionary/view?pageNo=2']"));
		//点击编辑功能
		s.findElement(By.xpath("//table//tbody//tr//*[text()='test_data_dictionary']//..//a")).click();
		//填写表单
		s.type(By.xpath("//form[@id='save-data-dictionary-form']//input[@name='name']"), "test_data_dictionary_modify");
		s.type(By.xpath("//form[@id='save-data-dictionary-form']//input[@name='value']"), "02");
		s.type(By.name("remark"),"");
		//提交表单
		s.click(By.xpath("//div[@class='panel-footer']//button[@type='submit']"));
		
		//返回成功信息
		message = s.findElement(By.className("alert")).getText();
		assertTrue(message.contains("保存成功"));
		
		//点击下一页
		s.click(By.xpath("//a[@href='/exitsoft-basic-curd/foundation/variable/data-dictionary/view?pageNo=2']"));
		//选中删除的记录
		s.check(By.xpath("//table//tbody//tr//*[text()='test_data_dictionary_modify']//..//input"));
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
		s.type(By.id("filter_LIKES_name"), "启用");
		s.type(By.id("filter_EQS_value"), "1");
		s.getSelect(By.name("filter_EQS_type")).selectByValue("I");
		s.getSelect(By.name("filter_EQS_category.id")).selectByValue("402881e437d467d80137d46fc0e50001");
		
		//查询
		s.click(By.xpath("//div[@class='modal-footer']//button[@type='submit']"));
		
		List<WebElement> aflterTrs = s.findElement(By.tagName("table")).findElements(By.xpath("//tbody//tr"));
		//断言查询后的记录数
		assertEquals(aflterTrs.size(), 1);
		
	}
	
}
