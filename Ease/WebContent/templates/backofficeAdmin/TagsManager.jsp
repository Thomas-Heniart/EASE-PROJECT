<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.Ease.Context.Catalog.Tag" %>
<div class="RightSideViewTab" id="TagsManagerTab">
	<button id="quit">
		<i class="fa fa-times"></i>
	</button>
	<div class="presentation">
		<h1>Tags manager</h1>
	</div>
	<div class="flex-row">
		<div class="centered-row">
			<h3 class="module-presentation">Tags list</h3>
			<div id="tags"></div>
		</div>
	</div>
	<div class="flex-row">
		<div class="tag-websites">
			<div id="tag-websiteList" class="hidden">
				<div class="website">
					<div class="logo">
						<img id="addTagWebsite" src="resources/icons/plus-black-symbol.png" />					
					</div>
					<p>Add website</p>
				</div>
			</div>
			<div id="catalogWebsites" class="hidden"></div>
		</div>
		<div id="tag-settings">
			<button id="AddTagMode" class="settings-button">Add tag</button>
			<input type="text" placeholder="Edit name" id="tag-settings-editName"/>
			<% 
				for(int i=0; i < Tag.colors.length; i++) {
					if(i==0) {
			%>
			<div class="tag-settings-color-selected" style="background-color: <%= Tag.colors[i] %>" index="<%= i %>"><%=Tag.colors[i]%></div>
			<div class="tag-settings-colors">
			<%			
					}
			%>
				<div class="tag-settings-color" style="background-color: <%= Tag.colors[i] %>" index="<%= i %>"><%=Tag.colors[i]%></div>
			<%
				}
			%>
			</div>
			<button id="tag-settings-submit" class="settings-button">Submit</button>
			<button id="tag-delete" class="hide settings-button">Delete</button>
		</div>
	</div>
</div>