package org.exitsoft.showcase.web.foundation.variable;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.exitsoft.orm.core.PropertyFilters;
import org.exitsoft.orm.core.Page;
import org.exitsoft.orm.core.PageRequest;
import org.exitsoft.orm.core.PageRequest.Sort;
import org.exitsoft.orm.core.PropertyFilter;
import org.exitsoft.showcase.common.SystemVariableUtils;
import org.exitsoft.showcase.common.annotation.OperatingAudit;
import org.exitsoft.showcase.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.entity.foundation.variable.DataDictionary;
import org.exitsoft.showcase.service.foundation.SystemVariableManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 数据字典管理Controller
 * 
 * @author vincent
 *
 */
@Controller
@OperatingAudit("数据字典管理")
@RequestMapping("/foundation/variable/data-dictionary")
public class DataDictionaryController {
	
	@Autowired
	private SystemVariableManager systemDictionaryManager;
	
	/**
	 * 获取数据字典列表
	 * 
	 * @param pageRequest 分页实体信息
	 * @param request HttpServlet请求
	 * 
	 * @return {@link Page}
	 */
	@RequestMapping("view")
	public Page<DataDictionary> view(PageRequest pageRequest,HttpServletRequest request) {
		
		List<PropertyFilter> filters = PropertyFilters.build(request,true);
		
		if (!pageRequest.isOrderBySetted()) {
			pageRequest.setOrderBy("id");
			pageRequest.setOrderDir(Sort.DESC);
		}
		
		request.setAttribute("valueTypes", SystemVariableUtils.getVariables(SystemDictionaryCode.ValueType));
		request.setAttribute("categoriesList", systemDictionaryManager.getDictionaryCategories());
		
		return systemDictionaryManager.searchDataDictionaryPage(pageRequest, filters);
	}
	
	/**
	 * 
	 * 保存或更新数据字典,保存成功后重定向到:foundation/variable/data-dictionary/view
	 * 
	 * @param entity 实体信息
	 * @param categoryId 所对应的字典类别id
	 * @param redirectAttributes spring mvc 重定向属性
	 * 
	 * @return String
	 * 
	 */
	@RequestMapping("save")
	@OperatingAudit(function="保存或更新数据字典")
	public String save(@ModelAttribute("entity") DataDictionary entity,String categoryId,RedirectAttributes redirectAttributes) {
		
		if (StringUtils.isEmpty(categoryId)) {
			entity.setCategory(null);
		} else {
			entity.setCategory(systemDictionaryManager.getDictionaryCategory(categoryId));
		}
		
		systemDictionaryManager.saveDataDictionary(entity);
		redirectAttributes.addFlashAttribute("success", "保存成功");
		
		return "redirect:/foundation/variable/data-dictionary/view";
	}
	
	/**
	 * 
	 * 读取数据字典,返回foundation/variable/data-dictionary/read.html页面
	 * 
	 * @param model Spring mvc的Model接口，主要是将model的属性返回到页面中
	 * 
	 * @return String
	 * 
	 */
	@RequestMapping("read")
	public String read(Model model) {
		
		model.addAttribute("valueTypes", SystemVariableUtils.getVariables(SystemDictionaryCode.ValueType));
		model.addAttribute("categoriesList", systemDictionaryManager.getDictionaryCategories());
		
		return "/foundation/variable/data-dictionary/read";
		
	}
	
	/**
	 * 通过主键id集合删除数据字典,删除成功后重定向到:foundation/variable/data-dictionary/view
	 * 
	 * @param ids 主键id集合
	 * @param redirectAttributes spring mvc 重定向属性
	 * 
	 * @return String
	 */
	@RequestMapping("delete")
	@OperatingAudit(function="删除数据字典")
	public String delete(@RequestParam("ids")List<String> ids,RedirectAttributes redirectAttributes) {
		systemDictionaryManager.deleteDataDictionary(ids);
		redirectAttributes.addFlashAttribute("success", "删除" + ids.size() + "条信息成功");
		return "redirect:/foundation/variable/data-dictionary/view";
	}
	
	/**
	 * 绑定实体数据，如果存在id时获取后从数据库获取记录，进入到相对的C后在将数据库获取的记录填充到相应的参数中
	 * 
	 * @param id 主键ID
	 * 
	 */
	@ModelAttribute("entity")
	public DataDictionary bindingModel(@RequestParam(value = "id", required = false)String id) {
		
		DataDictionary dataDictionary = new DataDictionary();
		
		if (StringUtils.isNotEmpty(id)) {
			dataDictionary = systemDictionaryManager.getDataDictionary(id);
		}
		
		return dataDictionary;
	}
	
}
