chrome.privacy.services.autofillEnabled.set({value: false});
chrome.privacy.services.passwordSavingEnabled.set({value: false});

function allowPopup(url) {
    chrome.contentSettings.popups.set({
        primaryPattern: url,
        setting: chrome.contentSettings.PopupsContentSetting.ALLOW
    });
}