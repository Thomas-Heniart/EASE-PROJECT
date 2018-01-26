import React from 'react';
import classnames from 'classnames';
import {connect} from "react-redux"
import {findDOMNode} from 'react-dom';
import {setTeamsTutorial} from '../../actions/commonActions';
import Joyride from "react-joyride";

@connect()
class TeamsTutorial extends React.Component {
  constructor(props){
    super(props);
  }
  tipsCallback = (action) => {
    switch (action.type) {
      case ('finished') :
        this.props.dispatch(setTeamsTutorial(true));
        break;
      default:
        break;
    }
  };
  render(){
    let steps = [];
    steps.push({
      title: '#openspace gathers all your members and their common tools.',
      isFixed: true,
      selector:".channel.default",
      position: 'right',
    });
    if (document.querySelector('#new_channel_button'))
      steps.push({
        title: 'Create new rooms here.',
        isFixed: true,
        selector:"#new_channel_button",
        position: 'right'
      });
    steps.push({
      title: 'Click on a name to see all accesses of a person.',
      isFixed: true,
      selector:"#team_users .section-header > span",
      position: 'right'
    });
    if (document.querySelector('#new_member_button'))
      steps.push({
        title: 'Add more team members here.',
        isFixed: true,
        selector:"#new_member_button",
        position: 'right'
      });
    return(
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
            callback={this.tipsCallback}
        />
    )
  }
}

module.exports = TeamsTutorial;