import React, {Component, Fragment} from "react";
import SimpleTeamApp from "./teamAppAdders/SimpleTeamApp";
import SimpleTeamSoftwareApp from "./teamAppAdders/SimpleTeamSoftwareApp";
import SimpleTeamAnyApp from "./teamAppAdders/SimpleTeamAnyApp";
import TeamLinkApp from "./teamAppAdders/LinkTeamApp";
import EnterpriseTeamApp from "./teamAppAdders/EnterpriseTeamApp";
import EnterpriseTeamAnyApp from "./teamAppAdders/EnterpriseTeamAnyApp";
import EnterpriseTeamSoftwareApp from "./teamAppAdders/EnterpriseTeamSoftwareApp";
import queryString from "query-string";
import {fetchTeamAppList} from "../actions/teamActions";
import {connect} from "react-redux";
import { Icon, Segment, Loader } from 'semantic-ui-react';
import {reflect} from "../utils/utils";
import {sendTeamUserInvitation} from "../actions/userActions";
import {withRouter, NavLink} from "react-router-dom";
import TeamUserInviteIndicators from "./teams/TeamUserInviteIndicators";
import {isAdmin} from "../utils/helperFunctions";
import BannerTeams from "./teams/BannerTeams";

@connect(store => ({
  team_apps: store.team_apps,
  teams: store.teams
}))
class TeamAppsContainer extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      loading: true
    }
  }
  componentWillReceiveProps(nextProps){
    if (this.props !== nextProps){
      if (this.props.item.id !== nextProps.item.id){
        this.setState({loading: true});
        const {item, team} = nextProps;
        this.props.dispatch(fetchTeamAppList({
          team_id: team.id,
          ids: item.team_card_ids
        })).then(response => {
          this.setState({loading: false});
        }).catch(err => {
          this.setState({loading: false});
        })
      }
    }
  }
  componentDidMount(){
    const {item, team} = this.props;
    this.props.dispatch(fetchTeamAppList({
      team_id: team.id,
      ids: item.team_card_ids
    })).then(response => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false});
    })
  }
  componentDidUpdate(prevProps, prevState) {
    if (prevProps !== this.props || prevState !== this.state) {
      if ((!this.state.loading && prevState.loading !== this.state.loading) || this.props.location.search !== prevProps.location.search) {
        const query = queryString.parse(this.props.location.search);
        if (query.app_id !== undefined && query.app_id.length !== 0) {
          const el = document.getElementById(`app_${query.app_id}`);
          if (el) {
            el.scrollIntoView(true);
            el.classList.add('blink');
            window.setTimeout(() => {
              el.classList.remove('blink')
            }, 3000);
          }
        }
      }
    }
  }
  render() {
    const item = this.props.item;
    const {team} = this.props;
    const users = Object.keys(team.team_users).map(id => {
      return team.team_users[id];
    });
    const channels = Object.keys(team.rooms).map(id => {
      return team.rooms[id];
    });
    const me = team.team_users[team.my_team_user_id];
    const plan_id = team.plan_id;
    let team_cards = [];
    if (!this.props.loading)
      team_cards = item.team_card_ids.map(id => {
        return this.props.team_apps[id];
      }).sort((a,b) => {
        return a.name.localeCompare(b.name);
      });
    return (
        <div class="apps_container">
          <div class="apps_scroller_div" id="team_apps_container">
            {!!item.username && isAdmin(me.role) && item.state === 0 &&
                <TeamUserInviteIndicators team_user={item}/>}
            {isAdmin(me.role) && item.join_requests &&
                <BannerTeams room={team.rooms[item.id]}
                             team={team}
                             me={me}
                             dispatch={this.props.dispatch}/>}
            {!this.state.loading ?
                team_cards.map(item => {
                  if (item.type === 'teamSingleCard') {
                    if (item.sub_type === 'software')
                      return (
                      <SimpleTeamSoftwareApp
                        app={item}
                        users={users}
                        channels={channels}
                        me={me}
                        key={item.id}
                        plan_id={plan_id}
                        team_id={team.id}
                        dispatch={this.props.dispatch}/>
                      );
                    else if (item.sub_type === 'any')
                      return (
                        <SimpleTeamAnyApp
                          app={item}
                          users={users}
                          channels={channels}
                          me={me}
                          key={item.id}
                          plan_id={plan_id}
                          team_id={team.id}
                          dispatch={this.props.dispatch}/>
                      );
                    else
                      return (
                        <SimpleTeamApp
                          app={item}
                          users={users}
                          channels={channels}
                          me={me}
                          key={item.id}
                          plan_id={plan_id}
                          team_id={team.id}
                          dispatch={this.props.dispatch}/>
                      );
                  }
                  if (item.type === 'teamLinkCard')
                    return (
                        <TeamLinkApp
                            app={item}
                            users={users}
                            channels={channels}
                            me={me}
                            key={item.id}
                            team_id={team.id}
                            dispatch={this.props.dispatch}/>
                    );
                  if (item.type === 'teamEnterpriseCard') {
                    if (item.sub_type === 'software')
                      return (
                        <EnterpriseTeamSoftwareApp
                          app={item}
                          users={users}
                          channels={channels}
                          me={me}
                          key={item.id}
                          plan_id={plan_id}
                          team_id={team.id}
                          dispatch={this.props.dispatch}/>
                      );
                    else if (item.sub_type === 'any')
                      return (
                        <EnterpriseTeamAnyApp
                          app={item}
                          users={users}
                          channels={channels}
                          me={me}
                          key={item.id}
                          plan_id={plan_id}
                          team_id={team.id}
                          dispatch={this.props.dispatch}/>
                      );
                    else
                      return (
                        <EnterpriseTeamApp
                          app={item}
                          users={users}
                          channels={channels}
                          me={me}
                          key={item.id}
                          plan_id={plan_id}
                          team_id={team.id}
                          dispatch={this.props.dispatch}/>
                      );
                  }
                }) :
                <Loader active />
            }
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamAppsContainer);
