/*var CatalogApp = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.url = this.qRoot.attr('url');
	this.id = this.qRoot.attr('idx');
	this.name = this.qRoot.attr('name');
}*/

var currentDraggedApp;
var catalogApp = function (name, id, imgSrc, canLoginWith, ssoId, url, inputs, isNew, count) {
    var self = this;

    this.qRoot = $(
        '<div class="catalogApp">'
        + '<div class="catalogAppLogo">'
        + '<img src="/resources/websites/HostelWorld/logo.png">'
        + '<a href="http://www.hostelworld.com/" target="_blank" class="siteUrl">'
        + '<span class="fa-stack fa-lg">'
        + '<i class="fa fa-circle fa-stack-2x"></i>'
        + '<i class="fa fa-stack-1x fa-link fa-rotate-90"></i>'
        + '</span>'
        + '</a>'
        + '<span class="newCatalogApp">New!</span>'
        + '</div>'
        + '<div class="catalogAppName">'
        + '<p>HostelWorld</p>'
        + '</div>'
        + '</div>'
    );
    this.name = name;
    this.id = id;
    this.imgSrc = imgSrc;
    this.canLoginWith = canLoginWith;
    this.ssoId = ssoId;
    this.url = url;
    this.inputs = inputs;
    this.isNew = isNew;
    this.count = count;
    this.nameHandler = this.qRoot.find('.catalogAppName p');
    this.imageHandler = this.qRoot.find('.catalogAppLogo img');
    this.urlHandler = this.qRoot.find('.siteUrl');

    this.nameHandler.text(self.name);
    this.imageHandler.attr('src', self.imgSrc);
    this.urlHandler.attr('href', self.url);

    if (!this.isNew) {
        self.qRoot.find('.newCatalogApp').remove();
    }
    this.qRoot.attr('idx', self.id);
    this.qRoot.attr('data-login', self.canLoginWith);
    this.qRoot.attr('data-sso', self.ssoId);
    this.qRoot.attr('name', self.name);
    this.qRoot.attr('url', self.url);
    this.qRoot.attr('newapp', self.isNew);

    this.qRoot.draggable({
        cursor: 'move',
        cursorAt: {
            left: 25,
            top: 25
        },
        appendTo: "body",
        helper: function (e, ui) {
            var ret;
            ret = $('<div class="dragHelperLogo" style="position: fixed; z-index: 3;pointer-events:none;"/>');
            ret.attr("type", "catalog");
            ret.append($('<img />'));
            for (var key in self.qRoot.attr()) {
                if (!self.qRoot.attr().hasOwnProperty(key))
                    continue;
                switch (key) {
                    case "class":
                        break;
                    case "style":
                        break;
                    default:
                        ret.attr(key, self.qRoot.attr()[key]);
                        break;
                }
            }
            ret.find('img').attr("src", self.imgSrc);
            currentDraggedApp = self;
            return ret;
        }
    });

    this.increaseCount = function () {
        self.count = self.count + 1;
        $(".apps-integrated .count", self.qRoot).text(self.count);
        $(".apps-integrated", self.qRoot).addClass("show");
    }
    this.decreaseCount = function () {
        self.count = self.count - 1;
        $(".apps-integrated .count", self.qRoot).text(self.count);
        if (self.count <= 0)
            $(".apps-integrated", self.qRoot).removeClass("show");
    }
};