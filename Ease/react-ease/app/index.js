import React from "react";
import ReactDOM from "react-dom";
import {HashRouter, Layout, Route, IndexRoute, HashHistory} from "react-router-dom";
import {Provider} from "react-redux";
import {StripeProvider} from "react-stripe-elements";
import {CookiesProvider, withCookies, Cookies} from "react-cookie";
import store from "./store";
import OnBoardingJoinTeam from "./components/onBoarding/OnBoardingJoinTeam";
import TeamView from "./components/TeamView";
import MainView from "./components/common/MainView";
import Base from "./components/common/Base.js";
import Login from "./components/common/login/index";
import Root from "./components/common/Root";
import RenewPassword from "./components/common/RenewPassword";
import HomeTemporaryHeader from "./components/common/HomeTemporaryHeader";
import {requireAuthentication} from "./components/common/requireAuthentication";
import NewTeamCreationView from "./components/onBoarding/NewTeamCreationView";
import ReactGA from 'react-ga';

import FillMagicLink from "./components/teamAppAdders/FillMagicLink";

const stripe_api_key = window.location.hostname === 'ease.space' ? 'pk_live_lPfbuzvll7siv1CM3ncJ22Bu' : 'pk_test_95DsYIUHWlEgZa5YWglIJHXd';

class App extends React.Component {

  componentWillMount() {
    ReactGA.initialize('UA-75916041-7', {
      debug: true,
      titleCase: false
    });
  }

  render() {
    return (
      <HashRouter>
        <Base>
          <Route exact path={"/"} component={Root}/>
          <Route path="/fill" component={FillMagicLink}/>
          <Route path="/teamCreation" component={NewTeamCreationView}/>
          <Route path="/main" component={requireAuthentication(MainView)}/>
          <Route path="/teamJoin/:code/:access_code" component={OnBoardingJoinTeam}/>
          <Route path="/teams/:teamId/:itemId?" component={requireAuthentication(TeamView)}/>
          <Route path="/login" component={Login}/>
          <Route path="/recover/:email/:code" component={RenewPassword}/>
        </Base>
      </HashRouter>
    )
  }
}

ReactDOM.render(<CookiesProvider><Provider
  store={store}><App/></Provider></CookiesProvider>, document.getElementById('app'));