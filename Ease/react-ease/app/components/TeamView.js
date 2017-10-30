var React = require('react');
var classnames = require('classnames');
var ReactRouter = require('react-router-dom');
var LoadingScreen = require('./common/LoadingScreen');
var TeamMenu = require('./TeamMenu');
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
import {selectChannelFromListById, selectUserFromListById} from "../utils/helperFunctions";
import {Route, Switch, withRouter} from "react-router-dom";
import TeamsTutorial from "./teams/TeamsTutorial";
import {connect} from "react-redux";
var EaseHeader = require('./common/EaseHeader');
var api = require('../utils/api');

@connect((store)=>{
  return {
    team: store.team,
    common: store.common,
    users: store.users.users,
    channels: store.channels.channels,
    selectedItem: store.selection
  };
})
class TeamView extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loadingInfo : true
    };
    this.getSelectedItem = this.getSelectedItem.bind(this);
    this.autoSelectItem = this.autoSelectItem.bind(this);
    this.isValidTeamItemId = this.isValidTeamItemId.bind(this);
  }
  componentWillMount() {
    document.title = "Team Space"
  }
  componentWillReceiveProps(nextProps){
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
  }
  componentDidUpdate(){
    if (!this.state.loadingInfo && !this.isValidTeamItemId(this.props.match.params.itemId)) {
      this.autoSelectItem();
    }
  }
  componentDidMount(){
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
  };
  isValidTeamItemId(itemId){
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
  }
  render(){
    const selectedItem = this.getSelectedItem();
    const me = selectUserFromListById(this.props.users, this.props.team.myTeamUserId);
    return (
        <div id="teamsHandler">
          <div className="team_view" id="team_view">
            {!this.state.loadingInfo && this.props.team.payment_required &&
            <FreeTrialEndModal/>}
            {this.state.loadingInfo && <LoadingScreen/>}
            {!this.state.loadingInfo && <TeamSideBar me={me}/>}
            {this.props.team.teamMenuActive &&
            <TeamMenu
                me={me}
                team={this.props.team}
                dispatch={this.props.dispatch}/>}
            {!this.state.loadingInfo && selectedItem !== null &&
            <div className="client_main_container">
              <TeamHeader
                  item={selectedItem}
                  match={this.props.match}
                  dispatch={this.props.dispatch}
                  appsLength={this.props.selectedItem.apps.length}/>
              <div className="team_client_body bordered_scrollbar">
                <OpacityTransition appear={true}>
                  {this.props.common.user !== null && !this.props.common.user.status.team_tuto_done &&
                  <TeamsTutorial/>}
                </OpacityTransition>
                <div id="col_main">
                  <TeamAppAddingUi
                      selection={selectedItem}/>
                  <TeamAppsContainer />
                </div>
                <div id="col_flex">
                  <Route path={`${this.props.match.url}/flexPanel`} render={(props) => <FlexPanels item={selectedItem}
                                                                                                   me={me}
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