<h3>操作记录清单</h3>
	
<table>
	<thead>
        <tr>
            <th>操作用户</th>
			<th>ip地址</th>
			<th>操作目标</th>
			<th>执行方法</th>
			<th>模块名称</th>
			<th>功能名称</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>执行状态</th>
        </tr>
    </thead>
    <tbody>
    	<tr>
			<td>${entity.username!""}</td>
			<td>${entity.ip!""}</td>
			<td>${entity.operatingTarget!""}</td>
			<td>${entity.method!""}</td>
			<td>${entity.module!""}</td>
			<td>${entity.function!""}</td>
			<td>${entity.startDate!""}</td>
			<td>${entity.endDate!""}</td>
			<td>${entity.stateName}</td>
		</tr>
   </tbody>
</table>
    	
<h3>描述</h3>

${entity.remark!''}