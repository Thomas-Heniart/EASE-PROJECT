function getUserNavigator() {
	var ua = navigator.userAgent;
	var x = ua.indexOf("MSIE");
	var y = "MSIE";
	if (x == -1) {
		x = ua.indexOf("Firefox");
		y = "Firefox";
		if (x == -1) {
			if (x == -1) {
				x = ua.indexOf("Chrome");
				y = "Chrome";
				if (x == -1) {
					x = ua.indexOf("Opera");
					y = "Opera";
					if (x == -1) {
						x = ua.indexOf("Safari");
						if (x != -1) {
							x = ua.indexOf("Version");
							y = "Safari";
						}
					}
				}
			}
		}
	}
	return (y);
}