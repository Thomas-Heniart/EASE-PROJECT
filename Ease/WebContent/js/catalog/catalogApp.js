

var CatalogApp = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.url = this.qRoot.attr('url');
	this.id = this.qRoot.attr('idx');
	this.name = this.qRoot.attr('name');
}