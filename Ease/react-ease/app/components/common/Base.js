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
import extension from "../../utils/extension_api";
import WebsocketClient from './WebsocketClient';
import ModalsContainer from "./ModalsContainer";
import {showNewFeatureModal} from "../../actions/modalActions";
import GeneralLogoutModal from "./GeneralLogoutModal";

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
      api.common.bz().then(authenticated => {
        if (authenticated !== this.props.common.authenticated)
          window.location.href = '/';
      }).catch(err => {
        window.location.href = '/';
      });
    }, 30000);
  };
  componentDidMount(){
    this.checkConnection();
    this.props.dispatch(fetchMyInformation()).then(response => {
      if (this.props.common.authenticated){
        this.props.dispatch(fetchCriticalParts()).then(response => {
          /*sessionstack('identify', {
            userId: this.props.common.user.email, // Replace the USER-ID with the user id from your app
            email: this.props.common.user.email, // Not required
          });*/
          this.setState({fetching: false});
        });
        if (!response.user.new_feature_seen)
          this.props.dispatch(showNewFeatureModal({active: true}))
      }else
        this.setState({fetching: false});
    });
    extension.get_homepage().then(homepage => {
      this.props.dispatch(setHomepage({ homepage: homepage }));
    });
  }
  componentWillMount(){
    const query = queryString.parse(this.props.location.search);
    if (query.skipLanding !== undefined)
      localStorage.setItem('skipLanding', true);
  }
  render(){
    if (this.state.fetching)
      return (<LoadingScreen/>);
    else
      return (
          <div id="app-root" className={this.props.location.pathname === `/fill` ? 'fill_credentials' : null}>
            <WebsocketClient/>
            {this.props.children}
            <ModalsContainer/>
            {this.props.common.generalLogoutModal &&
            <GeneralLogoutModal/>}
          </div>
      )
  }
}

module.exports = withCookies(withRouter(Base));