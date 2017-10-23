import React from 'react';
import EaseHeader from './EaseHeader';
import TeamsPreview from '../teams/TeamsPreview';
import Catalog from '../catalog/Catalog';
import { Switch, Route } from 'react-router-dom';
var SimpleTeamCreationView = require('../teams/SimpleTeamCreationView');

function MainView(props){
  return (
      <div class="display-flex flex_direction_column full_flex">
        <EaseHeader/>
        <Switch>
          <Route path='/main/teamsPreview' component={TeamsPreview}/>
          <Route path='/main/simpleTeamCreation' component={SimpleTeamCreationView}/>
          <Route path='/main/catalog' component={Catalog} />
        </Switch>
      </div>
  )
}

module.exports = MainView;