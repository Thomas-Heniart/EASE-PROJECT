/**
 * Created by nicetwice on 17/05/17.
 */
(function(Handlebars) {
Handlebars.registerHelper('fullName', function(fname, lname, options) {
    var attributes = [];

    for (var attributeName in options.hash) {
        attributes.push(attributeName + '="' + options.hash[attributeName] + '"');
    }
    console.log(attributes.join(' '));
    return fname + " bot" + lname;
});

Handlebars.registerHelper('bold', function(options) {
    return new Handlebars.SafeString(
        '<strong>'
        + options.fn(this)
        + '</strong>');
});

Handlebars.registerPartial("person", Handlebars.templates["second_template"]);
}(window.Handlebars));