import React, {Component} from 'react';
import EaseHeader from './EaseHeader';
import TeamsPreview from '../teams/TeamsPreview';
import Catalog from '../catalog/Catalog';
import Dashboard from "../dashboard/Dashboard";
import Settings from '../settings/Settings';
import {Switch, Route} from 'react-router-dom';
import HTML5Backend from 'react-dnd-html5-backend';
import {DragDropContext} from 'react-dnd';
import CustomDragLayer from "../dashboard/CustomDragLayer";
import NotificationBoxContainer from "../common/NotificationBoxContainer";
import Joyride from 'react-joyride';

var SimpleTeamCreationView = require('../teams/SimpleTeamCreationView');
import {connect} from "react-redux";
import {showExtensionDownloadModal} from "../../actions/modalActions";

@connect()
class MainView extends Component {
  constructor(props) {
    super(props);

    this.state = {
      joyrideOverlay: true,
      joyrideType: 'continuous',
      isReady: true,
      isRunning: true,
      stepIndex: 0,
      steps: [],
      selector: '',
    };
  }

  addSteps(steps) {
    let newSteps = steps;

    if (!Array.isArray(newSteps)) {
      newSteps = [newSteps];
    }

    if (!newSteps.length) {
      return;
    }

    // Force setState to be synchronous to keep step order.
    this.setState(currentState => {
      currentState.steps = currentState.steps.concat(newSteps);
      return currentState;
    });
  }

  addTooltip(data) {
    this.refs.joyride.addTooltip(data);
  }

  next() {
    this.refs.joyride.next();
  }

  callback(data) {
    console.log('%ccallback', 'color: #47AAAC; font-weight: bold; font-size: 13px;'); //eslint-disable-line no-console
    console.log(data); //eslint-disable-line no-console

    this.setState({
      selector: data.type === 'tooltip:before' ? data.step.selector : '',
    });
  }

  componentDidMount() {
    if (!document.querySelector('#new_ease_extension')) {
      this.props.dispatch(showExtensionDownloadModal({active: true}));
    }
  }

  render() {
    const {
      isReady,
      isRunning,
      joyrideOverlay,
      joyrideType,
      selector,
      stepIndex,
      steps,
    } = this.state;

    return (
      <div class="display-flex flex_direction_column full_flex" style={{flexFlow: 'column-reverse'}}>
        <Joyride
          ref="joyride"
          callback={this.callback}
          debug={false}
          disableOverlay={false}
          locale={{
            back: (<span>Back</span>),
            close: (<span>Close</span>),
            last: (<span>Last</span>),
            next: (<span>Next</span>),
            skip: (<span>Skip</span>),
          }}
          run={isRunning}
          showOverlay={joyrideOverlay}
          showSkipButton={true}
          showStepsProgress={true}
          stepIndex={stepIndex}
          steps={steps}
          type={joyrideType}
        />
        <Switch>
          <Route path='/main/teamsPreview' component={TeamsPreview}/>
          <Route path='/main/simpleTeamCreation' component={SimpleTeamCreationView}/>
          <Route path='/main/catalog' component={Catalog}/>
          <Route path='/main/settings' component={Settings}/>
          <Route path='/main/dashboard' render={(props) => <Dashboard joyrideType={joyrideType}
                                                                      joyrideOverlay={joyrideOverlay}
                                                                      addSteps={this.addSteps}
                                                                      addTooltip={this.addTooltip}
                                                                      {...this.props}/>}/>
        </Switch>
        <EaseHeader/>
        <CustomDragLayer/>
        <NotificationBoxContainer/>
      </div>
    )
  }
}

module.exports = DragDropContext(HTML5Backend)(MainView);