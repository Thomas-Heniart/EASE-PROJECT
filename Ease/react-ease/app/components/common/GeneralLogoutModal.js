import React, {Component, Fragment} from "react";
import {Progress} from 'semantic-ui-react';
import {setGeneralLogoutModal} from "../../actions/commonActions";
import {connect} from "react-redux";

@connect(store => ({
  teams: store.teams
}))
class GeneralLogoutModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      total: 2000,
      value: 0
    }
  }
  componentDidMount(){
    const interval = setInterval(() => {
      this.setState({value: this.state.value + 20}, () => {
        if (this.state.value >= 2000) {
          clearInterval(interval);
          this.props.dispatch(setGeneralLogoutModal({
            active: false
          }));
        }
      });
    }, 20);
  }
  render(){
    return (
        <div id="general_logout_modal">
          <span>General logout processing</span>
          <Progress total={this.state.total} value={this.state.value}/>
        </div>
    )
  }
}

export default GeneralLogoutModal;