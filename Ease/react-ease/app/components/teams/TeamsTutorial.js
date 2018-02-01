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
    this.state = {
      run: false,
      steps: []
    }
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
  componentDidMount(){
    let steps = [];
    steps.push({
      title: '#openspace gathers all your members and their common tools.',
      isFixed: true,
      selector:".channel.default",
      position: 'right',
      style: {
        beacon: {
          inner: '#45C997',
          outer: '#45C997'
        }
      }
    });
    if (document.querySelector('#new_channel_button'))
      steps.push({
        title: 'Create new rooms here.',
        isFixed: true,
        selector:"#new_channel_button",
        position: 'right',
        style: {
          beacon: {
            inner: '#45C997',
            outer: '#45C997'
          }
        }
      });
    steps.push({
      title: 'Click on a name to see all accesses of a person.',
      isFixed: true,
      selector:"#team_users > .section-list",
      position: 'right',
      style: {
        beacon: {
          inner: '#45C997',
          outer: '#45C997'
        }
      }
    });
    if (document.querySelector('#new_member_button'))
      steps.push({
        title: 'Add more team members here.',
        isFixed: true,
        selector:"#new_member_button",
        position: 'right',
        style: {
          beacon: {
            inner: '#45C997',
            outer: '#45C997'
          }
        }
      });
    this.setState({run: true, steps: steps});
  }
  render(){
    return(
        <Joyride
            steps={this.state.steps}
            autoStart={true}
            locale={{ back: 'Back', close: 'Close', last: 'Got it!', next: 'Next', skip: 'Skip the tips' }}
            showSkipButton={true}
            showBackButton={false}
            disableOverlay={true}
            showStepsProgress={true}
            allowClicksThruHole={true}
            type="continuous"
            run={this.state.run}
            callback={this.tipsCallback}
        />
    )
  }
}

module.exports = TeamsTutorial;