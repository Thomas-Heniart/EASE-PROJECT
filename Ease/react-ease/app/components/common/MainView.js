import React from 'react';
import EaseHeader from './EaseHeader';
import TeamsPreview from '../teams/TeamsPreview';
import Catalog from '../catalog/Catalog';
import Dashboard from "../dashboard/Dashboard";
import { Switch, Route } from 'react-router-dom';
import HTML5Backend from 'react-dnd-html5-backend';
import { DragDropContext } from 'react-dnd';
import CustomDragLayer from "../dashboard/CustomDragLayer";
var SimpleTeamCreationView = require('../teams/SimpleTeamCreationView');

function MainView(props){
  return (
      <div class="display-flex flex_direction_column full_flex bordered_scrollbar" style={{flexFlow: 'column-reverse'}}>
        <Switch>
          <Route path='/main/teamsPreview' component={TeamsPreview}/>
          <Route path='/main/simpleTeamCreation' component={SimpleTeamCreationView}/>
          <Route path='/main/catalog' component={Catalog} />
          <Route path='/main/dashboard' component={Dashboard}/>
        </Switch>
        <EaseHeader/>
        <CustomDragLayer/>
      </div>
  )
}

module.exports = DragDropContext(HTML5Backend)(MainView);