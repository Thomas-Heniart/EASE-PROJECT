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
import NotificationBoxContainer from "../common/NotificationBoxContainer";
var NewSimpleTeamCreationView = require('../onBoarding/NewSimpleTeamCreationView');
import {connect} from "react-redux";
import {showExtensionDownloadModal, showConnectionDurationChooserModal} from "../../actions/modalActions";

@connect(store => ({
  common: store.common
}))
class MainView extends Component {
  componentDidMount(){
    setTimeout(() => {
      const user = this.props.common.user;
      const extensionInstalled = !!document.querySelector('#new_ease_extension');
      if (!user.new_feature_seen)
        return;
      if (!user.status.popup_choose_connection_lifetime_seen){
        this.props.dispatch(showConnectionDurationChooserModal({
          active: true
        }));
        return;
      }
      if (!extensionInstalled){
        this.props.dispatch(showExtensionDownloadModal({active: true}));
      }
    }, 500);
  }
  render() {
    return (
        <div class="display-flex flex_direction_column full_flex" style={{flexFlow: 'column-reverse'}}>
          <Switch>
            <Route path='/main/teamsPreview' component={TeamsPreview}/>
            <Route path='/main/simpleTeamCreation' component={NewSimpleTeamCreationView}/>
            <Route path='/main/catalog' component={Catalog}/>
            <Route path='/main/settings' component={Settings}/>
            <Route path='/main/dashboard' component={Dashboard}/>
          </Switch>
          {this.props.location.pathname.split("/")[2] !== "simpleTeamCreation" &&
          <EaseHeader history={this.props.history}/>}
          <CustomDragLayer/>
          <NotificationBoxContainer/>
        </div>
    )
  }
}

module.exports = DragDropContext(HTML5Backend)(MainView);