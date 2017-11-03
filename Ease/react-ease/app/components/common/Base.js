var React = require('react');
var LoadingScreen = require('./LoadingScreen');
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import {fetchNotifications} from "../../actions/notificationsActions";
import {fetchMyInformation} from "../../actions/commonActions";
import {fetchDashboard} from "../../actions/dashboardActions";
import {fetchTeams} from "../../actions/teamActions";
import api from "../../utils/api";
import ReactTooltip from 'react-tooltip';
import WebsocketClient from './WebsocketClient';
import ModalsContainer from "./ModalsContainer";

@connect((store)=>{
  return {
    common: store.common
  };
})
class Base extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      fetching : true
    };
    window.refs = {};
  }
  checkConnection = () => {
    window.setInterval(() => {
      if (this.props.common.authenticated){
        api.common.bz().then(connected => {
          if (!connected)
            window.location.href = '/';
        }).catch(err => {
          window.location.href = '/';
        });
      }
    }, 30000);
  };
  componentDidMount(){
    this.checkConnection();
    if (!this.props.common.authenticated){
      this.props.dispatch(fetchMyInformation()).then(response => {
        if (this.props.common.authenticated){
          this.props.dispatch(fetchNotifications(0));
          Promise.all([
            this.props.dispatch(fetchTeams()),
            this.props.dispatch(fetchDashboard())
          ]).then(response => {
            this.setState({fetching: false});
          });
        }else
          this.setState({fetching: false});
      });
    }else {
      this.props.dispatch(fetchNotifications(0));
      Promise.all([
        this.props.dispatch(fetchTeams()),
        this.props.dispatch(fetchDashboard())
      ]).then(response => {
        this.setState({fetching: false});
      });
    }
  }
  render(){
    if (this.state.fetching)
      return (<LoadingScreen/>);
    else
      return (
          <div id="app-root">
            <ReactTooltip place="bottom"
                          type="dark"
                          globalEventOff="click"
                          effect="solid"
                          class="ease_tooltip"
                          multiline={true}
                          delayShow={300}/>
            <ReactTooltip effect="solid"
                          class="teams_tutorial_tooltip"
                          type="warning"
                          multiline={true}
                          place="bottom"
                          event="dblclick"
                          eventOff="dblclick"/>
            <WebsocketClient/>
            {this.props.children}
            <ModalsContainer/>
          </div>
      )
  }
}

module.exports = withRouter(Base);