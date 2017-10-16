var React = require('react');
var ReactDOM = require('react-dom');
import {HashRouter, Layout, Route, IndexRoute, HashHistory} from 'react-router-dom';
import {Provider} from "react-redux";
import {StripeProvider} from 'react-stripe-elements';
import { CookiesProvider, withCookies, Cookies } from 'react-cookie';
import store from "./store";
var TeamView = require('./components/TeamView.js');
var TeamCreationView = require('./components/teams/TeamCreationView');
var MainView = require('./components/common/MainView');
var TeamJoinView = require('./components/teams/TeamJoinView');
var Login = require('./components/common/Login');
var Base = require('./components/common/Base.js');
import Registration from './components/common/Registration';
import HomeTemporaryHeader from './components/common/HomeTemporaryHeader';

const stripe_api_key = window.location.hostname === 'ease.space' ? 'pk_live_lPfbuzvll7siv1CM3ncJ22Bu' : 'pk_test_95DsYIUHWlEgZa5YWglIJHXd';
class App extends React.Component {
  render(){
    return (
        <StripeProvider apiKey={stripe_api_key}>
          <HashRouter>
            <Base>
              <Route exact path={"/"} component={HomeTemporaryHeader}/>
              <Route path="/teamCreation" component={TeamCreationView}/>
              <Route path="/main" component={MainView}/>
              <Route path="/teamJoin/:code" component={TeamJoinView}/>
              <Route exact path="/teams/:teamId" component={TeamView}/>
              <Route path="/teams/:teamId/:itemId" component={TeamView}/>
              <Route path="/login" component={Login}/>
              <Route path="/registration" component={Registration}/>
            </Base>
          </HashRouter>
        </StripeProvider>
    )
  }
}

ReactDOM.render(<CookiesProvider><Provider store={store}><App/></Provider></CookiesProvider>, document.getElementById('app'));