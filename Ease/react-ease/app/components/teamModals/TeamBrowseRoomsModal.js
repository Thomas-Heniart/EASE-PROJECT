import React, {Component} from 'react';
import WhiteModalTemplate from "../common/WhiteModalTemplate";
import classnames from 'classnames';
import {handleSemanticInput, objectToList} from "../../utils/utils";
import {showTeamBrowseChannelsModal} from '../../actions/teamModalActions';
import {isUserInChannel, isAdmin} from "../../utils/helperFunctions";
import {askJoinChannel, addTeamUserToChannel} from "../../actions/channelActions";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

class RoomListItem extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false
    }
  }
  addTeamUserToChannel = () => {
    this.setState({loading: true});
    this.props.dispatch(addTeamUserToChannel({
      team_id: this.props.team_id,
      channel_id: this.props.item.id,
      team_user_id: this.props.me.id
    })).then(response => {
      this.setState({loading: false});
    });
  };
  askJoin = () => {
    this.setState({loading: true});
    this.props.dispatch(askJoinChannel({
      team_id: this.props.team_id,
      room_id: this.props.item.id
    })).then(response => {
      this.setState({loading: false});
    });
  };
  render(){
    const item = this.props.item;
    const me = this.props.me;
    const inChannel = item.team_user_ids.indexOf(me.id) !== -1;
    const isAsked = item.join_requests.indexOf(me.id) !== -1;

    return (
        <List.Item>
          <List.Content class="display-flex flex_direction_column room-card">
            <div class="display-flex align_items_center room-name">
                <span class="full_flex">
                  <Icon name="hashtag"/>{item.name}
                </span>
              <Icon name="user outline"/>
              {item.team_user_ids.length}
            </div>
            <div class="display-flex room-purpose">
                <span class="full_flex">
                  {item.purpose}
                </span>
              <div>
                {!inChannel && isAdmin(me.role) &&
                <Button size="mini" primary loading={this.state.loading} onClick={this.addTeamUserToChannel}>Join this room</Button>}
                {!inChannel && !isAsked && !isAdmin(me.role) &&
                <Button size="mini" loading={this.state.loading} onClick={this.askJoin} primary>Ask to join</Button>}
                {!inChannel && isAsked && !isAdmin(me.role) &&
                <Button size="mini" disabled>Join request sent</Button>}
                {inChannel &&
                <Button size="mini" disabled>Member</Button>}
              </div>
            </div>
          </List.Content>
        </List.Item>
    )
  }
}

@connect((store) => ({
  teams: store.teams
}))
class TeamBrowseRoomsModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      results : [],
      value: ''
    };
    const team = this.props.teams[this.props.match.params.teamId];
    this.state.results = objectToList(team.rooms);
  }
  handleSearchInput = (e, {value}) => {
    this.setState({value: value});
    const team = this.props.teams[this.props.match.params.teamId];
    const results = objectToList(team.rooms).filter(item => {
      return (item.name.toLowerCase().match(value.toLowerCase()) !== null ||
          item.purpose.toLowerCase().match(value.toLowerCase()) !== null);
    });
    this.setState({results: results});
  };
  render(){
    const team = this.props.teams[this.props.match.params.teamId];
    const me = team.team_users[team.my_team_user_id];
    const items = this.state.results.map(item => {
      return <RoomListItem
          dispatch={this.props.dispatch}
          key={item.id}
          team_id={team.id}
          item={team.rooms[item.id]}
          me={me}/>
    });
    return (
        <WhiteModalTemplate onClose={e => {this.props.history.replace(`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}`)}}>
          <Container class="full_height display_flex flex_direction_column">
            <Header as='h1' class="min_flex_shrink">
              Browse all rooms
            </Header>
            <Divider hidden/>
            <Input fluid
                   class="max-border-radius min_flex_shrink"
                   value={this.state.value}
                   onChange={this.handleSearchInput}
                   placeholder="Search room here..."
                   iconPosition='left'
                   icon="search"/>
            <ReactCSSTransitionGroup
                component={List}
                class="full_flex"
                style={{overflow: 'auto'}}
                divided
                transitionName="opacityAnim"
                transitionEnterTimeout={200}
                transitionLeaveTimeout={200}>
              {items.length > 0 ?
                  items :
                  <List.Item key="-1" style={{padding:'10px'}}>
                    No rooms...
                  </List.Item>}
            </ReactCSSTransitionGroup>
          </Container>
        </WhiteModalTemplate>
    )
  }
}

module.exports = TeamBrowseRoomsModal;