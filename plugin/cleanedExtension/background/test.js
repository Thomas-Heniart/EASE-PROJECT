function executeSteps() {
    var step = 0;
    function sendActions() {
        console.log(this);
        console.log(sendActions);
    }
   sendActions();
}

executeSteps();