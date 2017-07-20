var React = require('react');
var ReactDOM = require('react-dom');
import {HashRouter, Layout, Route, IndexRoute, HashHistory} from 'react-router-dom';
import {Provider} from "react-redux";
import { CookiesProvider, withCookies, Cookies } from 'react-cookie';
import store from "./store"
var TeamView = require('./components/TeamView.js');
var TeamCreationView = require('./components/teams/TeamCreationView');
var SimpleTeamCreationView = require('./components/teams/SimpleTeamCreationView');
var TeamJoinView = require('./components/teams/TeamJoinView');
var Login = require('./components/common/Login');
var Base = require('./components/common/Base.js');


class App extends React.Component {
  render(){
    return (
        <HashRouter>
          <Base>
            <Route exact path={"/"} component={TeamView}/>
            <Route path="/teamCreation" component={TeamCreationView}/>
            <Route path="/simpleTeamCreation" component={SimpleTeamCreationView}/>
            <Route path="/teamJoin/:code" component={TeamJoinView}/>
            <Route path="/teams/:teamId" component={TeamView}/>
            <Route path="/login" component={Login}/>
          </Base>
        </HashRouter>
    )
  }
}

ReactDOM.render(<CookiesProvider><Provider store={store}><App/></Provider></CookiesProvider>, document.getElementById('app'));