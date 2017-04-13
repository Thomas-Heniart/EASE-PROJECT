var Content = function() {
	console.log("addWebsiteAction");
	
	if (context.getType() === 'tab') {

		I.getXPath = function(node) {
			var path = " ";

			if(node.previousSibling) {
				var count = 1;
				var sibling = node.previousSibling
				do {
					if(sibling.nodeType == 1 && sibling.nodeName == node.nodeName) {count++;}
					sibling = sibling.previousSibling;
				} while(sibling);
				if(count == 1) {count = null;}
			} else if(node.nextSibling) {
				var sibling = node.nextSibling;
				do {
					if(sibling.nodeType == 1 && sibling.nodeName == node.nodeName) {
						var count = 1;
						sibling = null;
					} else {
						var count = null;
						sibling = sibling.previousSibling;
					}
				} while(sibling);
			}

			if(node.nodeType == 1) {
				path += node.nodeName.toLowerCase() + (node.id ? "[id='"+node.id+"']" : count > 0 ? ":eq("+(count - 1)+")" : '');
			}
			if(node.parentNode && (node.nodeName.toLowerCase() === "li" || node.nodeName.toLowerCase() === "ul" || $(path).length > 1)) {
				path = I.getXPath(node.parentNode) + path;
			}
			return path;
		};

		I.listenAction = function() {
			$(document).ready(function() {
				listenAction = true;
				document.body.addEventListener('click', function(event) {
					var target = I.getXPath(event.target);
					if (target !== " button[id='done']") {
						I.memory.actions.push({"action":"click", "target":target});
						console.log(I.memory.actions[I.memory.actions.length - 1]);
						I.pushMemory();
					}
				}, true);
				document.body.addEventListener('keyup', function(event) {
					var action = {"action":"val", "target":I.getXPath(event.target)};
					console.log("action: " + I.memory.actions[I.memory.actions.length - 1].action + "|" + action.action);
					console.log("target: " + I.memory.actions[I.memory.actions.length - 1].target + '|' + action.target);
					if (action.target !== " button[id='done']" && !(I.memory.actions[I.memory.actions.length - 1].action === action.action && I.memory.actions[I.memory.actions.length - 1].target === action.target)) {
						I.memory.actions.push(action);
						console.log(I.memory.actions[I.memory.actions.length - 1]);
						I.pushMemory();
					}
				}, true);
			});
		}

		I.cleanDom = function(dom) {
			var scripts = dom.getElementsByTagName('script');
			console.log(scripts.length + " scripts");
			for (var i = (scripts.length - 1); i >= 0; i--) {
				scripts[i].parentNode.removeChild(scripts[i]);
			};

			var styles = dom.getElementsByTagName('style');
			console.log(styles.length + " styles");
			for (var i = (styles.length - 1); i >= 0; i--) {
				styles[i].parentNode.removeChild(styles[i]);
			};
		}

		var firstDom = document.getElementsByTagName('body')[0].cloneNode(true);
		I.cleanDom(firstDom);
		var domUrl = window.location.href;
		var domDomain = window.location.host;
		console.log(domUrl);
		var observer = new MutationObserver(function(mutations) {
 			console.log("changements");
		});
		var config = { attributes: true, subtree: true, characterData: true };
		observer.observe(document.querySelector('body'), config);

		if (I.memory.state == undefined) {
			I.memory.state = "GoToHomePage";
			I.pushMemory();
		}
		if (I.memory.actions == undefined) {
			I.memory.actions = [];
			I.pushMemory();
		}
		I.import("GoToHomePagePopupJS", function() {
			var AddWebsitePopup;
			var text;
			var onDone = {};
			var listenAction = false;
			onDone.home = function() {
				I.sendMessage("unconnectedDom", {'url':domUrl, 'domain':domDomain, 'dom':firstDom.innerHTML, 'actions':I.memory.actions});
				AddWebsitePopup.getLoggedPage();
				I.memory.state = "Loggin";
				I.memory.actions = [];
				I.pushMemory();
				AddWebsitePopup.onDone(onDone.loggin);
				if (listenAction == false) {
					I.listenAction();
				}
			}
			onDone.loggin = function() {
				I.sendMessage("connectedDom", {'url':domUrl, 'domain':domDomain, 'dom':firstDom.innerHTML, 'actions':I.memory.actions});
				AddWebsitePopup.getLoggedOutPage();	
				I.memory.state = "Loggout";
				I.memory.actions = [];
				I.pushMemory();
				AddWebsitePopup.onDone(onDone.loggout);
				if (listenAction == false) {
					I.listenAction();
				}
			}
			onDone.loggout = function() {
				I.sendMessage("loggedOutDom", {'url':domUrl, 'domain':domDomain, 'dom':firstDom.innerHTML, 'actions':I.memory.actions});
				AddWebsitePopup.remove();
				I.finish();
			}

			if (I.memory.state === "GoToHomePage") {
				text = "Go to the website's home page.";
				onDone.actualy = onDone.home;
			} else if (I.memory.state === "Loggin") {
				text = "Please connect you.";
				onDone.actualy = onDone.loggin;
				I.listenAction();
				
			} else if (I.memory.state === "Loggout") {
				text = "Please loggout you.";
				onDone.actualy = onDone.loggout;
				I.listenAction();
			}

			AddWebsitePopup = new I.AddWebsitePopup(text);
			AddWebsitePopup.onDone(onDone.actualy);

		});
	}
}