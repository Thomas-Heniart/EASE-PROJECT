var I = {};

I.Identity = function () {
	
	var magic = "4577LaMenuiserieMec!";
	this.getMagic = function() {
		return magic;
	}
}

var IDENTITY = new I.Identity();

var global = {};