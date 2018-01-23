(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['first_template'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper;

  return ((stack1 = ((helper = (helper = helpers.body || (depth0 != null ? depth0.body : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"body","hash":{},"data":data}) : helper))) != null ? stack1 : "");
},"3":function(container,depth0,helpers,partials,data) {
    var stack1;

  return container.escapeExpression((helpers.fullName || (depth0 && depth0.fullName) || helpers.helperMissing).call(depth0 != null ? depth0 : (container.nullContext || {}),((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.fname : stack1),((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.lname : stack1),{"name":"fullName","hash":{"visible":true,"class":"lala"},"data":data}));
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, options, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3=container.escapeExpression, alias4="function", alias5=helpers.blockHelperMissing, buffer = 
  "<div class=\"entry\">\n    <h2>"
    + alias3((helpers.fullName || (depth0 && depth0.fullName) || alias2).call(alias1,((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.fname : stack1),((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.lname : stack1),{"name":"fullName","hash":{"visible":true,"class":"lala"},"data":data}))
    + "</h2>\n    <h1>"
    + alias3(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias4 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "</h1>\n    <div class=\"body\">\n        ";
  stack1 = ((helper = (helper = helpers.bold || (depth0 != null ? depth0.bold : depth0)) != null ? helper : alias2),(options={"name":"bold","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data}),(typeof helper === alias4 ? helper.call(alias1,options) : helper));
  if (!helpers.bold) { stack1 = alias5.call(depth0,stack1,options)}
  if (stack1 != null) { buffer += stack1; }
  buffer += "\n        ";
  stack1 = ((helper = (helper = helpers.bold || (depth0 != null ? depth0.bold : depth0)) != null ? helper : alias2),(options={"name":"bold","hash":{},"fn":container.program(3, data, 0),"inverse":container.noop,"data":data}),(typeof helper === alias4 ? helper.call(alias1,options) : helper));
  if (!helpers.bold) { stack1 = alias5.call(depth0,stack1,options)}
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n    </div>\n"
    + ((stack1 = container.invokePartial(partials.person,depth0,{"name":"person","data":data,"indent":"    ","helpers":helpers,"partials":partials,"decorators":container.decorators})) != null ? stack1 : "")
    + "</div>\n";
},"usePartial":true,"useData":true});
templates['second_template'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper;

  return ((stack1 = ((helper = (helper = helpers.body || (depth0 != null ? depth0.body : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"body","hash":{},"data":data}) : helper))) != null ? stack1 : "");
},"3":function(container,depth0,helpers,partials,data) {
    var stack1;

  return container.escapeExpression((helpers.fullName || (depth0 && depth0.fullName) || helpers.helperMissing).call(depth0 != null ? depth0 : (container.nullContext || {}),((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.fname : stack1),((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.lname : stack1),{"name":"fullName","hash":{"visible":true,"class":"lala"},"data":data}));
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, options, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3=container.escapeExpression, alias4="function", alias5=helpers.blockHelperMissing, buffer = 
  "<div class=\"entry\">\n    <h2>"
    + alias3((helpers.fullName || (depth0 && depth0.fullName) || alias2).call(alias1,((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.fname : stack1),((stack1 = (depth0 != null ? depth0.author : depth0)) != null ? stack1.lname : stack1),{"name":"fullName","hash":{"visible":true,"class":"lala"},"data":data}))
    + "</h2>\n    <h1>"
    + alias3(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias4 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "</h1>\n    <div class=\"body\">\n        ";
  stack1 = ((helper = (helper = helpers.bold || (depth0 != null ? depth0.bold : depth0)) != null ? helper : alias2),(options={"name":"bold","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data}),(typeof helper === alias4 ? helper.call(alias1,options) : helper));
  if (!helpers.bold) { stack1 = alias5.call(depth0,stack1,options)}
  if (stack1 != null) { buffer += stack1; }
  buffer += "\n        ";
  stack1 = ((helper = (helper = helpers.bold || (depth0 != null ? depth0.bold : depth0)) != null ? helper : alias2),(options={"name":"bold","hash":{},"fn":container.program(3, data, 0),"inverse":container.noop,"data":data}),(typeof helper === alias4 ? helper.call(alias1,options) : helper));
  if (!helpers.bold) { stack1 = alias5.call(depth0,stack1,options)}
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n    </div>\n</div>\n";
},"useData":true});
})();