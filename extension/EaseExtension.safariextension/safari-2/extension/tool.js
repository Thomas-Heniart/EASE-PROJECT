var tool = {
	generateSingleId:function() {
		var date = new Date();
		var components = [
    		date.getYear(),
    		date.getMonth(),
    		date.getDate(),
    		date.getHours(),
    		date.getMinutes(),
    		date.getSeconds(),
    		date.getMilliseconds()
		];
		return components.join("");
	}
}