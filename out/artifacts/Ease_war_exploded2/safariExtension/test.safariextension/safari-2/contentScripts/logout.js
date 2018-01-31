if (window.top === window) {

extension.runtime.onMessage("logout", function(msg, sendResponse) {
	doThings(msg, sendResponse);
    logoutOverlay(msg);
});
    
}