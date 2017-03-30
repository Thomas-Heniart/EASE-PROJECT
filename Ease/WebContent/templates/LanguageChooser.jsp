<div class="custom-select-language">
	<form id="languageSelectFrom" style="display:none;">
		<select id="language" name="language" onchange="submit()">
			<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
			<option value="fr_FR" ${language == 'fr_FR' ? 'selected' : ''}>Français</option>
		</select>
	</form>
	<div class="custom-sel">
		<a class="selected" data="${language}">
			<div class="img">
			</div>
			<i class="fa fa-caret-down lightblue" aria-hidden="true"></i>
		</a>
		<a class="selection hidden ${language == 'en' ? 'hide' : ''}" data="en">
			<img src="resources/flags/english.jpg"/>
		</a>
		<a class="selection hidden ${language == 'fr_FR' ? 'hide' : ''}" data="fr_FR">
			<img src="resources/flags/french.png"/>
		</a>
	</div>
</div>