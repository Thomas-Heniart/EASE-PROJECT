let anchor;
let segment;
let tabBody;
const dataTarget = "#onboardingCustomers";

$(document).ready(() => {
  anchor = $("a.item[data-target='" + dataTarget + "']");
  segment = $(dataTarget);
  tabBody = $("#onboardingCustomersTableBody");
  anchor.click(() => {
    segment.removeClass("loading");
    tabBody.addClass("loading");
    $("td", tabBody).remove();
    ajaxHandler.get("/api/v1/admin/onboarding/onboarding-customer-information", null, () => {
      tabBody.removeClass("loading");
    }, (data) => {
      data.forEach(processElem);
    })
  })
});

const processElem = (elem, index) => {
  let jElem = $("<tr class='" + (elem.created ? "positive" : "warning") + "'>" +
    "<td>" + (index + 1) + "</td>" +
    "<td>" + elem.teamName + "</td>" +
    "<td>" + elem.teamSize + "</td>" +
    "<td>" + elem.email + "</td>" +
    "<td>" + elem.firstName + " " + elem.lastName + "</td>" +
    "<td>" + elem.phoneNumber + "</td>" +
    "<td>" + moment(elem.creationDate).fromNow() + "</td>" +
    "<td>" + (elem.created ? "<button class='ui primary button transfer'>Finalize</button>" : "<a class='creationLink ui button primary' href='" + elem.teamCreationLink + "' target='_blank'>Create</a>") + "</td>" +
    "</tr>");
  tabBody.append(jElem);
  $(".creationLink", jElem).click((e) => {
    let target = $(e.target);
    let newElem = $("<button class='ui primary button transfer'>Finalize</button>");
    let td = $(target.parent());
    let tr = $(td.parent());
    target.remove();
    td.append(newElem);
    tr.removeClass("warning");
    tr.addClass("positive");
    transferOwnerShipListener(td, elem);
  });
  transferOwnerShipListener(jElem, elem)
};

const transferOwnerShipListener = (jElem, elem) => {
  $(".transfer", jElem).click((e) => {
    e.preventDefault();
    let button = $(e.target);
    button.addClass("loading");
    ajaxHandler.post("/api/v1/admin/onboarding/transfer-ownership", {
      onboardingId: elem.id,
      teamId: elem.teamId,
      emailContent: "lala"
    }, () => {
      button.removeClass("loading");
    }, () => {
      jElem.remove()
    })
  })
};