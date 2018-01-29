import React, {Component} from "react";
import classnames from "classnames";
import {validateTutorial} from "../../actions/dashboardActions";
import {connect} from "react-redux";
import Joyride from "react-joyride";

@connect()
class Tutorial extends Component {
  constructor(props){
    super(props);
  }
  tipsCallback = (action) => {
    switch (action.type) {
      case ('finished') :
        this.props.dispatch(validateTutorial());
        break;
      default:
        break;
    }
  };
  render(){
    let steps = [];
//    if (document.querySelector('.app_wrapper:not(.empty)'))
      steps.push({
        title: 'Just click on an App to login or access account info.',
        isFixed: true,
        selector:".app_wrapper",
        position: 'right'
      });
    steps.push({
      title: 'Here is where you access your Team(s).',
      isFixed: true,
      selector:"#teams_list",
      position: 'bottom'
    });
    steps.push({
      title: 'To add more apps, just click this button!',
      isFixed: true,
      selector:"#catalog_button",
      position: 'bottom'
    });
    return (
        <Joyride
            steps={steps}
            autoStart={true}
            locale={{ back: 'Back', close: 'Close', last: 'Got it!', next: 'Next', skip: 'Skip the tips' }}
            showSkipButton={true}
            showBackButton={false}
            disableOverlay={true}
            showStepsProgress={true}
            type="continuous"
            run={true}
            debug={true}
            callback={this.tipsCallback}
        />
    )
  }
}

export default Tutorial;