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
import api from "../../utils/api";
import {connect} from "react-redux";
import {Icon, Loader} from 'semantic-ui-react';
import {showExtensionDownloadModal,
  showMagicLinkChooserModal,
  showConnectionDurationChooserModal} from "../../actions/modalActions";

class DownloadOwnPasswordsDiv extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  download = () => {
    this.setState({loading: true});
    api.common.exportOwnPasswords().then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  renderLink = () => {
    return (
        <Fragment>
          {this.state.loading ?
              <Loader active size='mini' inline/> :
              <a class='simple_link' onClick={this.download}>here</a>
          }
        </Fragment>
    )
  };
  render(){
    const link = this.renderLink();
    return (
        <div id="download_passwords">
          <i className="em-svg em-warning"/> Ease.space will shutdown definitely on 2018, June 1st. Click {link} to download all your passwords.
        </div>
    )
  }
}

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
          <DownloadOwnPasswordsDiv/>
          <CustomDragLayer/>
          <NotificationBoxContainer/>
        </div>
    )
  }
}

module.exports = DragDropContext(HTML5Backend)(MainView);