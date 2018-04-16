import React, {Component, Fragment} from 'react';
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
import Footer from "./Footer";
import HeaderSidebar from "./HeaderSidebar";
import {showExtensionDownloadModal, showMagicLinkChooserModal, showConnectionDurationChooserModal} from "../../actions/modalActions";

@connect(store => ({
  common: store.common
}))
class MainView extends Component {
  componentDidMount(){
    setTimeout(() => {
      const user = this.props.common.user;
      if (!user.new_feature_seen)
        return;
      if (!user.status.popup_choose_connection_lifetime_seen){
        this.props.dispatch(showConnectionDurationChooserModal({
          active: true
        }));
        return;
      }
      if (!user.status.popup_choose_magic_apps_seen){
        this.props.dispatch(showMagicLinkChooserModal({
          active: true
        }));
        return;
      }
      const extensionInstalled = !!document.querySelector('#new_ease_extension');
      if (!extensionInstalled){
        this.props.dispatch(showExtensionDownloadModal({active: true}));
      }
    }, 500);
  }
  render() {
    return (
        <div class="display_flex">
          {this.props.location.pathname.split("/")[2] !== "simpleTeamCreation" &&
          <HeaderSidebar/>}
          <div class="display-flex full_flex lite_scrollbar" style={{flexFlow: 'column-reverse', overflowX:'hidden'}}>
            <Switch>
              <Route path='/main/teamsPreview' component={TeamsPreview}/>
              <Route path='/main/simpleTeamCreation' component={NewSimpleTeamCreationView}/>
              <Route path='/main/catalog' component={Catalog}/>
              <Route path='/main/settings' component={Settings}/>
              <Route path='/main/dashboard' component={Dashboard}/>
            </Switch>
            <CustomDragLayer/>
            <NotificationBoxContainer/>
          </div>
          <Footer/>
        </div>
    )
  }
}

module.exports = DragDropContext(HTML5Backend)(MainView);