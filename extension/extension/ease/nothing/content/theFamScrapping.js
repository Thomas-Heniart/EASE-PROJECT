var Content = function() {
    console.log("theFamScrapping content");
    if (context.getType() === 'tab') {
        if (I.memory.urls == undefined) {
            I.memory.urls = [];
            function waitfor(target, callback) {
                var interval = setInterval(function() {
                    if ($(target).length > 0) {
                        clearInterval(interval);
                        callback();
                    }
                }, 300);
            }
            waitfor(".thumbnail a", function() {
                $(".thumbnail a").each(function(index, element) {
                    I.memory.urls.push($(element).attr("href"));
                });
                I.memory.people = [];
                I.memory.index = 0;
                I.pushMemory();
                window.location.href = I.memory.urls[I.memory.index];
            });
        } else {
            var personObject = new PersonObject($(".col-md-8 > h2"), $(".col-md-8 p:last-child"), $("a[href*='mailto']"), $("#info li"));
            I.memory.index++;
            I.memory.people.push(personObject);
            I.pushMemory();
            if (I.memory.index >= I.memory.urls.length) {
              console.log(I.memory.people);
              I.memory.people.forEach(function(personObject) {
                  console.log(getCsv(personObject));
              });
              /*if (window.location.href != "about:blank")
                window.location.href = "about:blank";
              else {
                I.memory.people.forEach(function(personObject) {
                    console.log(getCsv(personObject));
                    $("body").append(getCsv(personObject));
                });*/
                I.finish();
            } else
                window.location.href = I.memory.urls[I.memory.index];
        }
    }
}

var PersonObject = function(nameSelector, phoneNumberSelector, emailsSelector, companySelector) {
    var self = this;
    this.name = nameSelector.text();
    this.phoneNumber = "unknown phone";
    this.emails = [];
    this.company = "unknown company";
    if (phoneNumberSelector.text().length > 0)
        this.phoneNumber = phoneNumberSelector.text();
    emailsSelector.each(function(index, element) {
        self.emails.push($(element).text());
    });
    if (companySelector.text().length > 0) {
        this.company = companySelector.text()
        while (this.company.startsWith(" "))
            this.company = this.company.substring(1);
        while (this.company.endsWith(" "))
            this.company = this.company.substring(0, this.company.length - 1);
    }
}