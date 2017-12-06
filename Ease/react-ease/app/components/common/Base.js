var React = require('react');
var LoadingScreen = require('./LoadingScreen');
import {connect} from "react-redux";
import queryString from "query-string";
import {withRouter} from "react-router-dom";
import {withCookies, Cookies } from 'react-cookie';
import {fetchNotifications} from "../../actions/notificationsActions";
import {fetchDashboard} from "../../actions/dashboardActions";
import {fetchTeams} from "../../actions/teamActions";
import {fetchMyInformation, setHomepage, fetchCriticalParts} from "../../actions/commonActions";
import api from "../../utils/api";
import ReactTooltip from 'react-tooltip';
import WebsocketClient from './WebsocketClient';
import ModalsContainer from "./ModalsContainer";
import {showNewFeatureModal} from "../../actions/modalActions";

@connect((store)=>{
  return {
    common: store.common,
    listener: store.listener
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
      api.common.bz().then(authenticated => {
        if (authenticated !== this.props.common.authenticated)
          window.location.href = '/';
      }).catch(err => {
        window.location.href = '/';
      });
    }, 30000);
  };
  eventListener = (event) => {
    this.props.dispatch(setHomepage({ homepage: event.detail }))
  };
  componentDidMount(){
    this.checkConnection();
    this.props.dispatch(fetchMyInformation()).then(response => {
      if (this.props.common.authenticated){
        this.props.dispatch(fetchCriticalParts()).then(response => {
          this.setState({fetching: false});
        });
        if (!response.user.new_feature_seen)
          this.props.dispatch(showNewFeatureModal({active: true}))
      }else
        this.setState({fetching: false});
    });
    document.addEventListener("GetSettingsDone", this.eventListener);
    setTimeout(() => {
      document.dispatchEvent(new CustomEvent("GetSettings", {bubbles: true}))
    }, 5);
  }
  componentWillUnmount() {
    setTimeout(() => {
      document.removeEventListener("GetSettingsDone", this.eventListener);
    }, 1000);
  }
  componentWillMount(){
    const query = queryString.parse(this.props.location.search);
    if (query.skipLanding !== undefined)
      this.props.cookies.set('skipLanding', true, {maxAge: 9999999, path: '/'});
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

module.exports = withCookies(withRouter(Base));