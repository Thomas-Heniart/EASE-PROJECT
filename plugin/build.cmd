xcopy /i src out\chrome /s
xcopy /i src out\firefox /s
xcopy /i src out\safari /s

xcopy /i nav\chrome\chrome.js out\chrome\extension.js
xcopy /i nav\chrome\manifest.json out\chrome\manifest.json

xcopy /i nav\safari\safari.js out\safari\extension.js

xcopy /i nav\firefox\firefox.js out\firefox\extension.js
xcopy /i nav\firefox\manifest.json out\firefox\manifest.json