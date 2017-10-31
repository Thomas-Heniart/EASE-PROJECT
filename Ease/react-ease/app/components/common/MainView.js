import React from 'react';
import EaseHeader from './EaseHeader';
import TeamsPreview from '../teams/TeamsPreview';
import Catalog from '../catalog/Catalog';
import Dashboard from "../dashboard/Dashboard";
import { Switch, Route } from 'react-router-dom';
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
      </div>
  )
}

module.exports = MainView;