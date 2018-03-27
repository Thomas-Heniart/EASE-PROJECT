import React, {Component, Fragment} from "react";
import {Search, Popup} from 'semantic-ui-react';
import {withRouter} from "react-router-dom";
import {askJoinChannel, addTeamUserToChannel} from "../../../actions/channelActions";
import {fetchAllTeamCards} from "../../../actions/appsActions";
import {isAdmin} from "../../../utils/helperFunctions";
import {objectToList} from "../../../utils/utils";
import {connect} from "react-redux";

const DisabledCardPopup = ({trigger, room_name, room_manager_name}) => {
  return (
      <Popup size="mini"
             position="right center"
             inverted
             hideOnScroll={true}
             trigger={trigger}
             content={`Join #${room_name} to get access. Room manager: ${room_manager_name}`}
      />
  )
};

@connect(store => ({
  team_cards: store.team_apps
}))
class TeamAppsHeaderSearch extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      value: ''
    };
  }
  shouldComponentUpdate(nextProps, nextState){
    if (!this.state.loading && !this.state.value.length && !nextState.value.length)
      return false;
    return true;
  };
  onSearchChange = (e, {value}) => {
    this.setState({value: value});
    if (!this.state.value.length && !!value.length){
      const {team} = this.props;

      this.setState({loading: true});
      this.props.dispatch(fetchAllTeamCards({
        team_id: team.id
      })).then(response => {
        this.setState({loading: false});
      }).catch(err => {
        this.setState({loading: false});
      });
    }
  };
  onResultSelect = (e, {result}) => {
    const card = result;
    const {team, me} = this.props;
    const room = team.rooms[card.channel_id];
    if (room.team_user_ids.includes(me.id)) {
      this.props.history.push(`/teams/${card.team_id}/${card.channel_id}?app_id=${card.id}`);
      this.setState({value: ''});
    }
  };
  sendJoinRoomRequest = (e, room_id) => {
    const {team, me} = this.props;
    e.preventDefault();
    e.stopPropagation();
    e.nativeEvent.stopImmediatePropagation();
    if (isAdmin(me.role)){
      this.props.dispatch(addTeamUserToChannel({
        team_id: team.id,
        channel_id: room_id,
        team_user_id: me.id
      }));
    }else {
      this.props.dispatch(askJoinChannel({
        team_id: team.id,
        room_id: room_id
      }));
    }
  };
  enterpriseCardRenderer = (card) => {
    const {team, me} = this.props;
    const {name} = card;
    const room = team.rooms[card.channel_id];
    const isInRoom = room.team_user_ids.includes(me.id);
    const isRequested = room.join_requests.includes(me.id);

    return (
        <Fragment>
          {!isInRoom &&
          <DisabledCardPopup
              trigger={<div class="overlay" onClick={e => {e.stopPropagation();e.nativeEvent.stopImmediatePropagation();}}/>}
              room_name={room.name}
              room_manager_name={team.team_users[room.room_manager_id].username}
          />}
          <img class="logo" src={!!card.logo ? card.logo : card.website.logo}/>
          <span>&nbsp;{name}</span>
          <span class="muted">&nbsp;|&nbsp;#{room.name}</span>
          <span class="muted overflow-ellipsis full_flex">,&nbsp;multiple logins</span>
          {!isInRoom &&
          (isRequested ?
              <span class="muted min_flex_shrink">&nbsp;Join request sent</span> :
              <span class="muted min_flex_shrink action_button"
                    onClick={e => {this.sendJoinRoomRequest(e, room.id)}}>&nbsp;<a>Ask to join room</a></span>)
          }
        </Fragment>
    )
  };
  singleCardRenderer = (card) => {
    const {team, me} = this.props;
    const {name, empty} = card;
    const room = team.rooms[card.channel_id];
    const isInRoom = room.team_user_ids.includes(me.id);
    const isRequested = room.join_requests.includes(me.id);

    return (
        <Fragment>
          {!isInRoom &&
          <DisabledCardPopup
              trigger={<div class="overlay" onClick={e => {e.stopPropagation();e.nativeEvent.stopImmediatePropagation();}}/>}
              room_name={room.name}
              room_manager_name={team.team_users[room.room_manager_id].username}
          />}
          <img class="logo" src={!!card.logo ? card.logo : card.website.logo}/>
          <span>&nbsp;{name}</span>
          <span class="muted">&nbsp;|&nbsp;#{room.name}</span>
          <span class="muted overflow-ellipsis full_flex">,&nbsp;{empty ? 'waiting for login' : card.account_information.login}</span>
          {!isInRoom &&
          (isRequested ?
              <span class="muted min_flex_shrink">&nbsp;Join request sent</span> :
              <span class="muted min_flex_shrink action_button"
                    onClick={e => {this.sendJoinRoomRequest(e, room.id)}}>&nbsp;<a>Ask to join room</a></span>)
          }
        </Fragment>
    )
  };
  linkCardRenderer = (card) => {
    const {team, me} = this.props;
    const {name} = card;
    const room = team.rooms[card.channel_id];
    const isInRoom = room.team_user_ids.includes(me.id);
    const isRequested = room.join_requests.includes(me.id);

    return (
        <Fragment>
          {!isInRoom &&
          <DisabledCardPopup
              trigger={<div class="overlay" onClick={e => {e.stopPropagation();e.nativeEvent.stopImmediatePropagation();}}/>}
              room_name={room.name}
              room_manager_name={team.team_users[room.room_manager_id].username}
          />}
          <img class="logo" src={card.logo}/>
          <span>&nbsp;{name}</span>
          <span class="muted">&nbsp;|&nbsp;#{room.name}</span>
          <span class="muted overflow-ellipsis full_flex">,&nbsp;{card.url}</span>
          {!isInRoom &&
          (isRequested ?
              <span class="muted min_flex_shrink">&nbsp;Join request sent</span> :
              <span class="muted min_flex_shrink action_button"
                    onClick={e => {this.sendJoinRoomRequest(e, room.id)}}>&nbsp;<a>Ask to join room</a></span>)
          }
        </Fragment>
    )
  };
  teamCardRenderer = (card) => {
    switch (card.type) {
      case 'teamSingleCard':
        return this.singleCardRenderer(card);
      case 'teamEnterpriseCard':
        return this.enterpriseCardRenderer(card);
      case 'teamLinkCard':
        return this.linkCardRenderer(card);
      default:
        return null;
    }
  };
  render(){
    const {loading, value} = this.state;
    const {team, team_cards} = this.props;
    let team_cards_results = [];

    if (!!value.length) {
      const searchValue = value.replace(/\s+/g, '').toLowerCase();
      team_cards_results = objectToList(team_cards).filter(item => {
        const cardName = item.name.replace(/\s+/g, '').toLowerCase();
        return item.team_id === team.id && cardName.includes(searchValue);
      }).map(item => {
        return {
            ...item,
          key: item.id
        }
      });
    }
    return (
        <div id="team_header_apps_search">
          <Search loading={loading}
                  placeholder="Search for an app"
                  size="mini"
                  fluid
                  noResultsMessage={loading ? 'Searching...' : 'No apps found...'}
                  onResultSelect={this.onResultSelect}
                  resultRenderer={this.teamCardRenderer}
                  results={team_cards_results}
                  onSearchChange={this.onSearchChange}
                  value={value}/>
        </div>
    )
  }
}

export default withRouter(TeamAppsHeaderSearch);