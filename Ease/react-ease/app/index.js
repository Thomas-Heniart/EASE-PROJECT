var React = require('react');
var ReactDOM = require('react-dom');
import {Router, Route, IndexRoute, hashHistory} from 'react-router';

var TeamView = require('./components/TeamView.js');

ReactDOM.render(<TeamView team_id="2"/>, document.getElementById('teamsHandler'));