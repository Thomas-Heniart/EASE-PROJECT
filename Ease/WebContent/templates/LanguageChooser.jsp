<%@ page pageEncoding="UTF-8" %>
<div class="custom-select-language">
	<form id="languageSelectFrom" style="display:none;">
		<select id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="fr_FR" ${language == 'fr_FR' ? 'selected' : ''}>Français</option>
		</select>
	</form>
	<div class="custom-sel">
		<a class="selected" data="${language}">
			${language == 'en' ? 'english' : 'français'}
			<i class="fa fa-caret-down lightblue" aria-hidden="true"></i>
		</a>
		<a class="selection hidden ${language == 'en' ? 'hide' : ''}" data="en">
			<p>english</p>
		</a>
		<a class="selection hidden ${language == 'fr_FR' ? 'hide' : ''}" data="fr_FR">
			<p>français</p>
		</a>
	</div>
</div>