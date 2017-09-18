import React, {Component} from 'react';
import WhiteModalTemplate from "../common/WhiteModalTemplate";
import {showTeamBrowsePeopleModal} from '../../actions/teamModalActions';
import ReactCSSTransitionGroup from "react-addons-css-transition-group";
import { NavLink} from 'react-router-dom';
import {teamUserRoles} from "../../utils/utils";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

class UserListItem extends Component {
  constructor(props){
    super(props);
  }
  render(){
    const item = this.props.item;
    return (
        <List.Item as={NavLink} to={`/teams/${this.props.match.params.teamId}/@${item.id}`} class="user-card">
          <List.Content>
            <Header as="h4">
              {item.first_name} {item.last_name}
            </Header>
            <p><Icon name="user"/>{item.username} ({teamUserRoles[item.role]})</p>
            <p><Icon name="mail outline"/>{item.email}</p>
          </List.Content>
        </List.Item>
    )
  }
}

@connect((store) =>
    ({
      users: store.users.users
    }))
class TeamBrowsePeopleModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      results : [],
      value: ''
    };
    this.state.results = this.props.users.slice();
  }
  handleSearchInput = (e, {value}) => {
    this.setState({value: value});
    const results = this.props.users.filter(item => {
      return (item.username.toLowerCase().match(value.toLowerCase()) !== null ||
          item.first_name.toLowerCase().match(value.toLowerCase()) !== null ||
          item.last_name.toLowerCase().match(value.toLowerCase()) !== null ||
          item.email.toLowerCase().match(value.toLowerCase()) !== null);
    });
    this.setState({results: results});
  };
  render(){
    const items = this.state.results.map(item => {
      return <UserListItem dispatch={this.props.dispatch} key={item.id} match={this.props.match} item={item}/>
    });
    return (
        <WhiteModalTemplate onClose={e => {this.props.history.replace(`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}`)}}>
          <Container class="full_height display_flex flex_direction_column">
            <Header as='h1' class="min_flex_shrink">
              Browse all members
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
                    No users found...
                  </List.Item>
              }
            </ReactCSSTransitionGroup>
          </Container>
        </WhiteModalTemplate>
    )
  }
};

module.exports = TeamBrowsePeopleModal;