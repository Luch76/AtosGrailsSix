<ul data-role="listview" data-split-icon="plus">	
	<g:each var="module" status="myIndex" in="${modules}">
		<li>
			<g:link controller="${module.controller}" action="${module.action}" >
				<asset:image height="80" width="80" src="${module.image}"/>
				<h3>${module.title}</h3>
				<g:if test="${module.description != null && module.description.trim().length() > 0}">
					<p>${module.description}</p>
				</g:if>
				<g:if test="${module.plusController != null && module.plusController.trim().length() > 0}">
					<g:link controller="${module.plusController}" action="${module.plusAction}" ></g:link>
				</g:if>
			</g:link>				
		</li>
	</g:each>				
</ul>
<br/>
