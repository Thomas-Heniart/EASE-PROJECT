let anchor;
let segment;
let tabBody;
let emailContentModal;
let emailContentForm;
const dataTarget = "#onboardingCustomers";

$(document).ready(() => {
  anchor = $("a.item[data-target='" + dataTarget + "']");
  segment = $(dataTarget);
  tabBody = $("#onboardingCustomersTableBody");
  emailContentModal = $("#onboardingCustomerEmailModal");
  emailContentForm = $("form", emailContentModal);
  anchor.click(() => {
    segment.removeClass("loading");
    tabBody.addClass("loading");
    $("td", tabBody).remove();
    ajaxHandler.get("/api/v1/admin/onboarding/onboarding-customer-information", null, () => {
      tabBody.removeClass("loading");
    }, (data) => {
      data.forEach(processElem);
    })
  });
  emailContentModal
    .modal({
      onHide: function () {
        emailContentForm.off("submit");
      }
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
    e.preventDefault();
    let target = $(e.target);
    target.addClass("loading");
    ajaxHandler.get(elem.teamCreationLink, null, () => {

    }, (team) => {
      target.removeClass("loadgin");
      elem.teamId = team.id;
      let newElem = $("<button class='ui primary button transfer'>Finalize</button>");
      let td = $(target.parent());
      let tr = $(td.parent());
      target.remove();
      td.append(newElem);
      tr.removeClass("warning");
      tr.addClass("positive");
      transferOwnerShipListener(td, elem);
      window.location = location.origin + "/#/teams/" + team.id;
    })
  });
  transferOwnerShipListener(jElem, elem)
};

const transferOwnerShipListener = (jElem, elem) => {
  $(".transfer", jElem).click((e) => {
    e.preventDefault();
    let header = $(".header", emailContentModal);
    header.text("Email to: " +
      elem.firstName + " " + elem.lastName +
      " (" + elem.email + ") from: " + elem.teamName);
    let emailContentField = $("textarea", emailContentForm);
    emailContentModal.modal("show");
    emailContentForm.submit((e) => {
      e.preventDefault();
      let formButton = $("button", emailContentForm);
      formButton.addClass("loading");
      formButton.addClass("disabled");
      ajaxHandler.post(emailContentForm.attr("action"), {
        onboardingId: elem.id,
        teamId: elem.teamId,
        emailContent: emailContentField.val()
      }, () => {
      }, () => {
        formButton.removeClass("loading");
        formButton.removeClass("disabled");
        emailContentModal.modal("hide");
        emailContentField.val("");
        jElem.remove()
      })
    })
  })
};