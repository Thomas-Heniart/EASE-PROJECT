import React, {Component} from 'react';
import EaseHeader from './EaseHeader';
import TeamsPreview from '../teams/TeamsPreview';
import Catalog from '../catalog/Catalog';
import Dashboard from "../dashboard/Dashboard";
import Settings from '../settings/Settings';
import { Switch, Route } from 'react-router-dom';
import HTML5Backend from 'react-dnd-html5-backend';
import { DragDropContext } from 'react-dnd';
import CustomDragLayer from "../dashboard/CustomDragLayer";
var SimpleTeamCreationView = require('../teams/SimpleTeamCreationView');
import {connect} from "react-redux";
import {showExtensionDownloadModal} from "../../actions/modalActions";

@connect()
class MainView extends Component {
  componentDidMount(){
    if (!document.querySelector('#new_ease_extension')){
      this.props.dispatch(showExtensionDownloadModal({active: true}));
    }
  }
  render() {
    return (
        <div class="display-flex flex_direction_column full_flex" style={{flexFlow: 'column-reverse'}}>
          <Switch>
            <Route path='/main/teamsPreview' component={TeamsPreview}/>
            <Route path='/main/simpleTeamCreation' component={SimpleTeamCreationView}/>
            <Route path='/main/catalog' component={Catalog}/>
            <Route path='/main/settings' component={Settings}/>
            <Route path='/main/dashboard' component={Dashboard}/>
          </Switch>
          <EaseHeader/>
          <CustomDragLayer/>
        </div>
    )
  }
}

module.exports = DragDropContext(HTML5Backend)(MainView);