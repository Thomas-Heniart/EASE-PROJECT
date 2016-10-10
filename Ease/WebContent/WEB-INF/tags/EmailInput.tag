<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name = "id"%>
<div class="EmailInput">
<input type="text" />
<p style="display:inline-block">X</p>
</div>
<div class="bitch">
<input type="text" />
<p style="display:inline-block">X</p>
</div>
<div class="NoEmailInput" id="${id}">
<input type="text" />
<p style="display:inline-block">X</p>
</div>
<div class="NoEmailInput">
<input type="text" />
<p style="display:inline-block">X</p>
</div>
<div class="NoEmailInput">
<input type="text" />
<p style="display:inline-block">X</p>
</div>

<script type="text/javascript">
var EmailInput = function (rootEl) {
	var self = this;
	this.qRoot = rootEl;
	this.qInput = this.qRoot.find("input");
	this.qIndic = this.qRoot.find("p");
	
	this.qInput.on("keyup", function () {
		tmp = self.validate();
		if (tmp != self.isValid) {
			self.isValid = tmp;
			for (var i = 0; i < self.target.length; ++i) {
				self.target[i].trigger("Changed");
			}
			tmp && self.qInput.addClass("valid") || self.qInput.removeClass("valid");
			self.qIndic.text((tmp) ? "V" : "X");
		}
	});
	
	this.target = [],
	this.isValid = false,
	this.isValidate = function () {
		return self.isValid;
	},
	this.validate = function () {
		return self.qInput.val().length > 0;
	},
	this.listenBy = function (qBy) {
		self.target.push(qBy);
	}
	
};

var NoEmailInput = function(rootEl){
	EmailInput.apply(this,arguments);
	var self = this;
	this.validate = function(){
		return self.qInput.val().length == 2;
	}
};


$(document).ready(function () {
	var emailInput = new EmailInput($(".EmailInput"));
	$(".NoEmailInput").each(function(){
		new NoEmailInput($(this));
	});

});
	

</script>