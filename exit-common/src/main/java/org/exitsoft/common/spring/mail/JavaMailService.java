/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exitsoft.common.spring.mail;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 借助spring {@link JavaMailSender} 来做邮件发送器的Java邮件服务类
 * 
 * @author vincent
 *
 */
public class JavaMailService {
	
	private static Logger logger = LoggerFactory.getLogger(JavaMailService.class);
	
	//spring邮件发送器
	private JavaMailSender mailSender;
	//freemarker配置，帮助通过模板，方便的发送html
	private Configuration freemarkerConfiguration;
	//MimeMessageHelper和freemarker的编码
	private String encoding;
	//线程执行器，如果注入会通过执行器来runnable 发送邮件
	private Executor executor;
	
	/**
	 * 设置spring邮件发送器
	 * 
	 * @param mailSender spring邮件发送器
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * 设置freemarker配置
	 * 
	 * @param freemarkerConfiguration freemarker配置
	 */
	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}
	
	/**
	 * 设置MimeMessageHelper和freemarker的字符编码
	 * 
	 * @param encoding 编码
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 设置线程执行器，通过执行器来runnable发送邮件
	 * 
	 * @param executor 线程执行器
	 */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	/**
	 * 通过freemarker模板来发送邮件
	 * 
	 * @param sendTo 要接收的邮件地址
	 * @param sendFrom 接收到邮件后的昵称
	 * @param subject 邮件主题
	 * @param templateName freemarker模板文件名
	 * @param attachment 附件，该附件为一个map，将key来做文件名，value来做文件的形式发送出去
	 * @param model freemarker模板里要取值的el名称
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 * 
	 */
	public void sendByTemplate(String sendTo,String sendFrom,String subject,
			String templateName,Map<String, File> attachment,Map<String, ?> model) throws IOException, TemplateException {
		send(sendTo, sendFrom, subject, getTemplateString(templateName,model), attachment);
	}
	
	/**
	 * 发送邮件
	 * 
	 * @param sendTo 要接收的邮件地址
	 * @param sendFrom 接收到邮件后的昵称
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param attachment 附件，该附件为一个map，将key来做文件名，value来做文件的形式发送出去
	 */
	public void send(final String sendTo,final String sendFrom,final String subject,final String content,final Map<String, File> attachment) {
		if (this.executor != null) {
			this.executor.execute(new Runnable() {
				@Override
				public void run() {
					doSend(sendTo, sendFrom, subject, content, attachment);
				}
			});
		} else {
			doSend(sendTo, sendFrom, subject, content, attachment);
		}
		
	}
	
	/**
	 * 执行发送邮件
	 * 
	 * @param sendTo 要接收的邮件地址
	 * @param sendFrom 接收到邮件后的昵称
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param attachment 附件，该附件为一个map，将key来做文件名，value来做文件的形式发送出去
	 */
	private void doSend(String sendTo,String sendFrom,String subject,String content,Map<String, File> attachment) {
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, encoding);

			helper.setTo(sendTo);
			helper.setFrom(sendFrom);
			helper.setSubject(subject);
			helper.setText(content, true);

			if (!MapUtils.isEmpty(attachment)) {
				for (Entry<String, File> entry : attachment.entrySet()) {
					helper.addAttachment(entry.getKey(), entry.getValue());
				}
			}

			mailSender.send(msg);
			logger.info("邮件发送成功");
		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}
	
	/**
	 * 获取freemarker模板的内容
	 * 
	 * @param templateName 模板名称
	 * @param model freemarker模板里要取值的el名称
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	private String getTemplateString(String templateName,Map<String, ?> model) throws IOException, TemplateException {
		Template template = freemarkerConfiguration.getTemplate(templateName, encoding);
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	}
}
