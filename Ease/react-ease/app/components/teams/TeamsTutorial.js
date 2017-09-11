import React from 'react';
import classnames from 'classnames';
import {connect} from "react-redux"
import {findDOMNode} from 'react-dom';
import {setTeamsTutorial} from '../../actions/commonActions';
import ReactTooltip from 'react-tooltip';
import ReactCSSTransitionGroup from "react-addons-css-transition-group";

function TeamsTutorialTip(props){
  return (
      <div class={classnames("display-flex flex_direction_column min_flex teamsTutorialTip", props.step === 0 ? 'first': null)}>
        <span class="stepCounter">{props.step + 1}/{props.maxStep + 1}</span>
        <h2>{props.title}</h2>
        {props.body !== undefined &&
        <span class="body">{props.body}</span>}
        <div class="display-flex">
          {props.skip !== undefined &&
          <span style={{fontSize: '14px'}}>
            Already know how Ease.space works?<br/>
            <button class="button-unstyle" onClick={props.skip}><u>Skip tutorial</u></button>
          </span>}
          <div class="full_flex"/>
          <button class="button-unstyle big-blue-button validate_button" onClick={props.validateStep}>
            {props.button_text}
          </button>
        </div>
      </div>
  )
}

@connect((store)=>{
  return {
    teamName: store.team.name
  };
})
class TeamsTutorial extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      maxStep: 3,
      step: 0,
      enabledTooltip: null
    };
    this.getRef = this.getRef.bind(this);
    this.skipTutorial = this.skipTutorial.bind(this);
    this.incrementStep = this.incrementStep.bind(this);
  }
  getRef(step){
    switch (step) {
      case 1:
        return (window.refs.rooms);
      case 2:
        return (window.refs.roomAdd);
      case 3:
        return (window.refs.shareAppsButton);
      default:
        return null;
    }
  }
  skipTutorial(){
    if (this.state.enabledTooltip !== null)
      ReactTooltip.hide(this.getRef(this.state.step));
    this.props.dispatch(setTeamsTutorial(true));
  }
  incrementStep(){
    var state = {...this.state};
    if (state.enabledTooltip !== null)
      ReactTooltip.hide(this.getRef(state.step));
    state.enabledTooltip = null;
    state.step++;
    if (this.getRef(state.step) !== null){
      state.enabledTooltip = state.step;
      setTimeout(function(){ReactTooltip.show(this.getRef(state.step))}.bind(this), 100);
    }
    this.setState(state);
  }
  render(){
    var steps = [];
    steps.push(<TeamsTutorialTip
        key="0"
        skip={this.skipTutorial}
        validateStep={this.incrementStep}
        title={'This is where the ' + this.props.teamName + ' web accounts will be shared and organized'}
        step={this.state.step}
        button_text={'Next tip'}
        maxStep={this.state.maxStep}/>);
    steps.push(<TeamsTutorialTip
        key="1"
        skip={this.skipTutorial}
        validateStep={this.incrementStep}
        title="Rooms"
        body="Rooms can be teams, topics or anything. They gather your company tools and the people to access them. #general is the one gathering everyone."
        step={this.state.step}
        button_text={'Next tip'}
        maxStep={this.state.maxStep}/>);
    steps.push(<TeamsTutorialTip
        key="2"
        skip={this.skipTutorial}
        validateStep={this.incrementStep}
        title="Create Rooms"
        body="Admins can create rooms. Any member can send tools and web accounts in it."
        step={this.state.step}
        button_text={'Next tip'}
        maxStep={this.state.maxStep}/>);
    steps.push(<TeamsTutorialTip
        key="3"
        validateStep={this.skipTutorial}
        title="Sharing accounts"
        body="You can send multiple types of accounts regarding your needs. Just hit the + button."
        step={this.state.step}
        button_text={'Done!'}
        maxStep={this.state.maxStep}/>);
    return(
        <div id="teams_tutorial" class="display-flex align_items_center flex_direction_column full_flex">
            {
              steps.map(function (item,idx) {
                if (idx === this.state.step)
                  return(item);
                return null;
              }, this)
            }
        </div>
    )
  }
}

module.exports = TeamsTutorial;