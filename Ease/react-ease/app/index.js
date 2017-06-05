var React = require('react');
var ReactDOM = require('react-dom');
import {Router, Route, IndexRoute, hashHistory} from 'react-router';
import {Provider} from "react-redux"
import store from "./store"
var TeamView = require('./components/TeamView.js');

ReactDOM.render(<Provider store={store}>
  <TeamView team_id="2"/>
    </Provider>, document.getElementById('teamsHandler'));