var React = require('react');
var classnames = require('classnames');
var ReactRouter = require('react-router-dom');
var LoadingScreen = require('./common/LoadingScreen');
var TeamMenu = require('./TeamMenu');
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
var TeamSideBar = require('./TeamSideBar');
var TeamHeader = require('./TeamHeader');
var FlexPanels = require('./TeamFlexPanels');
var TeamAppAdderButtons = require('./TeamAppAdderButtons');
var TeamAppAddingUi = require('./TeamAppAddingUi');
var TeamAppsContainer = require('./TeamAppsContainer');
var TeamSettings = require('./teamModals/TeamSettings');
import TeamBrowsePeopleModal from "./teamModals/TeamBrowsePeopleModal";
import TeamBrowseRoomsModal from "./teamModals/TeamBrowseRoomsModal";
import FreeTrialEndModal from "./teamModals/FreeTrialEndModal";
import StaticUpgradeTeamPlanModal from "./teamModals/StaticUpgradeTeamPlanModal";
import * as teamActions from "../actions/teamActions"
import * as modalActions from "../actions/teamModalActions"
import {OpacityTransition} from "../utils/transitions";
import {selectChannelFromListById, selectUserFromListById, selectItemFromListById} from "../utils/helperFunctions";
import {Route, Switch, withRouter} from "react-router-dom";
import TeamsTutorial from "./teams/TeamsTutorial";
import {connect} from "react-redux";
var EaseHeader = require('./common/EaseHeader');
var api = require('../utils/api');

@connect((store)=>({
  teams: store.teams,
  common: store.common
}))
class TeamView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loadingInfo : false,
      addAppView: 'none',
      teamMenuActive: false
    };
  }
  setTeamMenu = (state) => {
    this.setState({teamMenuActive: state})
  };
  setAddAppView = (view) => {
    this.setState({addAppView: view});
  };
  componentWillMount() {
    document.title = "Team Space"
  }
  /*  componentWillReceiveProps(nextProps){
      const itemId = nextProps.match.params.itemId;
      if (this.props !== nextProps) {
        if (this.props.match.params.teamId !== nextProps.match.params.teamId) {
          this.setState({loadingInfo: true});
          this.props.dispatch(teamActions.fetchTeamAndUsersAndChannels(nextProps.match.params.teamId)).then(() => {
            this.setState({loadingInfo: false});
            if (this.isValidTeamItemId(itemId)) {
              this.props.dispatch(teamActions.fetchTeamItemApps(itemId));
            } else
              this.autoSelectItem();
          }).catch(err => {
            window.location.href = '/';
          });
        }
        else if (this.props.match.params.itemId !== itemId) {
          if (this.isValidTeamItemId(itemId)) {
            this.props.dispatch(teamActions.fetchTeamItemApps(itemId));
          } else
            this.autoSelectItem();
        }
      }
    }*/
  /*  componentDidUpdate(){
      if (!this.state.loadingInfo && !this.isValidTeamItemId(this.props.match.params.itemId)) {
        this.autoSelectItem();
      }
    }*/
  /*  componentDidMount(){
      const itemId = this.props.match.params.itemId;
      if (!this.props.common.authenticated){
        this.props.history.push('/login');
        return;
      }
      this.props.dispatch(teamActions.fetchTeamAndUsersAndChannels(this.props.match.params.teamId)).then(()=>{
        this.setState({loadingInfo: false});
        const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
        if (me.phone_number === null && me.role === 3){
          this.props.dispatch(modalActions.showTeamPhoneNumberModal(true));
        }
        if (this.isValidTeamItemId(itemId)) {
          this.props.dispatch(teamActions.fetchTeamItemApps(itemId));
        }else
          this.autoSelectItem();
      }).catch(err => {
        console.log('error !');
        console.log(err);
        window.location.href = '/';
      });
    };*/
  /*  isValidTeamItemId(itemId){
      if (itemId === undefined)
        return false;
      const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
      if (itemId[0] !== '@' && me.channel_ids.indexOf(Number(itemId)) !== -1)
        return true;
      if (selectUserFromListById(this.props.users, Number(itemId.slice(1, itemId.length))) !== null)
        return true;
      return false;
    }
    autoSelectItem(){
      const teamId = this.props.match.params.teamId;

      const defaultRoom = this.props.channels.find(item => (item.default));
      this.props.history.replace(`/teams/${teamId}/${defaultRoom.id}`);
    }
    getSelectedItem(){
      const itemId = this.props.match.params.itemId;
      if (itemId === undefined)
        return null;
      let item = (itemId[0] === '@') ?
          selectUserFromListById(this.props.users, Number(itemId.slice(1, itemId.length)))
          :
          selectChannelFromListById(this.props.channels, Number(itemId));
      return item;
    }*/
  componentDidUpdate(){
    if (!this.state.loadingInfo && !this.isValidTeamItemId(this.props.match.params.itemId)) {
      this.autoSelectItem();
    }
  }
  componentWillReceiveProps(nextProps){
    if (this.props !== nextProps){
      if (this.props.match.params.teamId !== nextProps.match.params.teamId || this.props.match.params.itemId !== nextProps.match.params.itemId)
        this.setAddAppView('none');
    }
  }
  componentDidMount() {
    const teamId = this.props.match.params.teamId;
    const team = this.props.teams[Number(teamId)];

    if (!this.props.common.authenticated){
      this.props.history.push('/login');
      return;
    }
    if (!this.isValidTeamItemId()){
      this.autoSelectItem();
      return;
    }
    const me = team.team_users[team.my_team_user_id];
    if (me.phone_number === null && me.role === 3){
      this.props.dispatch(modalActions.showTeamPhoneNumberModal({
        active: true,
        team_id: team.id,
        team_user_id: me.id
      }));
    }
  }
  isValidTeamItemId = () => {
    const teamId = Number(this.props.match.params.teamId);
    const team = this.props.teams[teamId];
    if (team === undefined) {
      window.location.href = '/';
      return;
    }
    const itemId = this.props.match.params.itemId;
    if (itemId === undefined)
      return false;
    const me = team.team_users[team.my_team_user_id];
    if (itemId[0] !== '@' && me.room_ids.indexOf(Number(itemId)) !== -1)
      return true;
    if (team.team_users[Number(itemId.slice(1, itemId.length))] !== undefined)
      return true;
    return false;
  };
  autoSelectItem = () => {
    const teamId = this.props.match.params.teamId;

    const rooms = this.props.teams[teamId].rooms;
    const defaultRoom = Object.keys(rooms).map(id => {
      return rooms[id];
    }).find(item => (item.default));
    this.props.history.replace(`/teams/${teamId}/${defaultRoom.id}`);
  };
  getSelectedItem = () => {
    const team = this.props.teams[this.props.match.params.teamId];
    const itemId = this.props.match.params.itemId;
    if (itemId === undefined){
      return null;
    }
    let item = (itemId[0] === '@') ?
        team.team_users[Number(itemId.slice(1, itemId.length))]
        :
        team.rooms[Number(itemId)];
    return item !== undefined ? item : null;
  };
  render(){
    const team = this.props.teams[this.props.match.params.teamId];
    const selectedItem = this.getSelectedItem();
    const me = team.team_users[team.my_team_user_id];
    return (
        <div id="teamsHandler">
          <div className="team_view" id="team_view">
            {!this.state.loadingInfo && team.payment_required &&
            <FreeTrialEndModal/>}
            {this.state.loadingInfo && <LoadingScreen/>}
            {!this.state.loadingInfo &&
            <TeamSideBar team={team} me={me} openMenu={this.setTeamMenu.bind(null, true)}/>}
            {this.state.teamMenuActive &&
            <TeamMenu
                closeMenu={this.setTeamMenu.bind(null, false)}
                me={me}
                team={team}
                dispatch={this.props.dispatch}/>}
            {!this.state.loadingInfo && selectedItem !== null &&
            <div className="client_main_container">
              <TeamHeader
                  item={selectedItem}
                  setAddAppView={this.setAddAppView}
                  match={this.props.match}
                  dispatch={this.props.dispatch}/>
              <div className="team_client_body bordered_scrollbar">
                <OpacityTransition appear={true}>
                  {this.props.common.user !== null && !this.props.common.user.status.team_tuto_done &&
                  <TeamsTutorial/>}
                </OpacityTransition>
                <div id="col_main">
                  <TeamAppAddingUi
                      addAppView={this.state.addAppView}
                      item={selectedItem}/>
                  <TeamAppsContainer team={team} item={selectedItem}/>
                </div>
                <div id="col_flex">
                  <Route path={`${this.props.match.url}/flexPanel`} render={(props) => <FlexPanels item={selectedItem}
                                                                                                   me={me}
                                                                                                   team={team}
                                                                                                   backLink={this.props.match.url}
                                                                                                   {...props}/>} />
                </div>
              </div>
            </div>}
            {!this.state.loadingInfo &&
            <Switch>
              <Route path={`${this.props.match.path}/settings`}
                     component={TeamSettings}/>
              <Route path={`${this.props.match.path}/members`}
                     component={TeamBrowsePeopleModal}/>
              <Route path={`${this.props.match.path}/rooms`}
                     component={TeamBrowseRoomsModal}/>
              <Route path={`${this.props.match.path}/upgrade`}
                     component={StaticUpgradeTeamPlanModal}/>
            </Switch>}
          </div>
        </div>
    )
  }
}

module.exports = TeamView;