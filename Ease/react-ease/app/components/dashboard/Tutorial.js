import React, {Component} from "react";
import classnames from "classnames";
import {validateTutorial} from "../../actions/dashboardActions";
import {connect} from "react-redux";

const MainTip = ({show, skip, next}) => {
  return (
      <div class={classnames("tip display-flex flex_direction_column", show ? 'show' : null)} id="0" step="1">
        <h3>This is your Space</h3>
        <span class="content">Open it for the day with your Ease.space password. Then, just click on apps to access accounts.<br/>We pre-selected apps, you can delete or add more.<br/>To activate an app: click on it and enter your info one last time, ever.<br/></span>
        <div class="buttonHandler">
          <div class="skip">
            <span>Already know how Ease.space works?</span>
            <span class="action" onClick={skip}>Skip tutorial</span>
          </div>
          <button class="btn" onClick={next}>Got it</button>
        </div>
      </div>
  )
};

const CatalogTip = ({show, skip, next}) => {
  return (
      <div class={classnames('tip', show ? 'show' : null)} id="1" step="2">
        <div class="arrow" style={{right: '15px'}}/>
        <h3>Find more apps in our catalog</h3>
        <div class="buttonHandler">
          <div class="skip" style={{marginRight: '10px'}}>
            <span>Already know how Ease.space works?</span>
            <span class="action" onClick={skip}>Skip tutorial</span>
          </div>
          <button class="btn" onClick={next}>Got it</button>
        </div>
      </div>
  )
};

const TeamsTip = ({show, skip, next}) => {
  return (
      <div class={classnames('tip', show ? 'show' : null)} id="2" step="2">
        <div class="arrow" style={{right: '57px'}}/>
        <h3>Teams are here!</h3>
        <div class="buttonHandler">
          <div class="skip" style={{marginRight: '10px'}}>
            <span>Already know how Ease.space works?</span>
            <span class="action" onClick={skip}>Skip tutorial</span>
          </div>
          <button class="btn" onClick={skip}>Got it</button>
        </div>
      </div>
  )
};

@connect()
class Tutorial extends Component {
  constructor(props){
    super(props);
    this.state = {
      step: -1
    }
  }
  componentDidMount(){
    setTimeout(() => {
      this.setState({step: 0});
    }, 1000);
  }
  nextTip = () => {
    this.setState({step: this.state.step + 1});
  };
  validate = () => {
    this.props.dispatch(validateTutorial());
  };
  render(){
    return (
        <div id="tipsHandler">
          <MainTip
              show={this.state.step === 0}
              next={this.nextTip}
              skip={this.validate}/>
          <CatalogTip
              show={this.state.step === 1}
              next={this.nextTip}
              skip={this.validate}/>
          <TeamsTip
              show={this.state.step === 2}
              next={this.validate}
              skip={this.validate}/>
        </div>
    )
  }
}

export default Tutorial;