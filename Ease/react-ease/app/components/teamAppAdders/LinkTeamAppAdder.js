import React from "react";
import {getClearbitLogo, dashboardAndTeamAppSearch, fetchWebsiteInfo} from "../../utils/api";
import {handleSemanticInput,
    transformWebsiteInfoIntoList,
    passwordChangeOptions,
    credentialIconType} from "../../utils/utils";
import {selectUserFromListById, newSelectUserFromListById} from "../../utils/helperFunctions";
import {closeAppAddUI} from "../../actions/teamAppsAddUIActions";
import {teamCreateLinkAppNew} from "../../actions/appsActions";
import {requestWebsite, showPinTeamAppToDashboardModal} from "../../actions/teamModalActions";
import {connect} from "react-redux";
import { Label, Container, Icon, Segment, Input, Button, Dropdown } from 'semantic-ui-react';
import {reduxActionBinder} from "../../actions/index";
import { setUserDropdownText, renderLinkAppAddUserLabel} from "./common";

@connect(store => ({
  team_id: store.team.id,
  teams: store.teams,
  selectedItem: store.selection,
  card: store.teamCard
}), reduxActionBinder)
class LinkTeamAppAdder extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      appName: this.props.card.name,
      url: this.props.card.url,
      logoSrc: '/resources/icons/link_app.png',
      img_url: '',
      comment: '',
      selectedUsers: [],
      loading: false,
      users: [],
      selected_users: []
    };
    this.handleAppNameChange = this.handleAppNameChange.bind(this);
    this.handleUrlInput = this.handleUrlInput.bind(this);
    this.handleComment = this.handleComment.bind(this);
  }
  componentWillMount() {
    let users = this.props.item.team_user_ids.map(item => {
      const user = newSelectUserFromListById(this.props.teams[this.props.card.team_id].team_users, item);
      return {
        key: item,
        text: setUserDropdownText(user),
        value: item,
        id: item,
        role: user.role,
        username: user.username,
        can_see_information: true
      }
    });
    const room_manager_name = this.props.teams[this.props.card.team_id].team_users[this.props.item.room_manager_id].username;
    this.setState({users: users, room_manager_name: room_manager_name});
  };
  componentDidMount() {
    this.getLogo();
    this.chooseAllUsers();
  };
  chooseAllUsers = () => {
    let selected = [];
    this.state.users.map(user => {
      if (selected.length) {
        selected.splice(selected.length + 1, 0, user.id);
      }
      else {
        selected.splice(0, 0, user.id);
      }
    });
    this.setState({ selected_users: selected });

  };
  handleInput = handleSemanticInput.bind(this);
  getLogo = () => {
    getClearbitLogo(this.state.url).then(response => {
      this.setState({img_url: response});
    });
  };
  changeUrl = (e, {value}) => {
    this.setState({url: value}, this.getLogo);
  };
  send = (e) => {
    e.preventDefault();
    this.setState({loading: true});
    this.props.dispatch(teamCreateLinkAppNew({
      team_id: this.props.card.team_id,
      channel_id: this.props.card.channel_id,
      name: this.state.appName,
      description: this.state.comment,
      url: this.state.url,
      receivers: this.state.selected_users,
      img_url: this.state.img_url ? this.state.img_url : this.state.logoSrc
    })).then(response => {
      this.setState({loading: false});
      this.close();
    });
  };
  close = () => {
    this.props.resetTeamCard();
    // this.props.dispatch(closeAppAddUI());
  };
  handleComment(event) {
    this.setState({comment: event.target.value});
  }
  handleAppNameChange(event) {
    this.setState({appName: event.target.value});
  }
  handleUrlInput(event) {
    this.setState({url: event.target.value});
  }
  render() {
    return (
      <Container fluid id="simple_team_app_add" class="team-app mrgn0" as="form" onSubmit={this.send}>
          <Segment>
              <Button icon="delete" type="button" style={{margin: '0 0 0 .6rem'}} size="mini" class="close"
                      onClick={this.close} color="grey"/>
              <div className="display_flex margin_b5rem">
                  <div>
                      <Input className="team-app-input"
                             placeholder="Name your link"
                             name="app_name"
                             value={this.state.appName}
                             autoComplete="off"
                             onChange={this.handleAppNameChange}
                             size="mini"
                             label={<Label><Icon name="home"/></Label>}
                             labelPosition="left"
                             required/>
                  </div>
              </div>
              <div class="display_flex">
                  <div class="logo_column">
                      <div class="logo">
                          <img src={this.state.img_url ? this.state.img_url : this.state.logoSrc} alt="website logo"/>
                      </div>
                  </div>
                  <div class="main_column">
                      <div class="display-inline-flex width48">
                          <Input placeholder="Paste or type url"
                                 className="team-app-input width100"
                                 autoComplete="off"
                                 type="text"
                                 name="url"
                                 value={this.state.url}
                                 onChange={this.changeUrl}
                                 size="mini"
                                 label={<Label><Icon name="linkify"/></Label>}
                                 labelPosition="left"
                                 required/>
                      </div>
                    <div>
                      <Dropdown
                        class="mini"
                        search
                        fluid
                        name="selected_users"
                        options={this.state.users}
                        onChange={this.handleInput}
                        value={this.state.selected_users}
                        selection
                        renderLabel={renderLinkAppAddUserLabel}
                        multiple
                        noResultsMessage='No more results found'
                        placeholder="Tag your team members here..."/>
                    </div>
                      <div>
                          <Input size="mini"
                                 fluid
                                 class="team-app-input"
                                 onChange={this.handleComment}
                                 name="comment"
                                 value={this.state.comment}
                                 placeholder="What is this about? Any comment?"
                                 type="text"
                                 label={<Label><Icon name="sticky note"/></Label>}
                                 labelPosition="left"/>
                      </div>
                  </div>
              </div>
          </Segment>
          <div>
              <Button positive
                      size="mini"
                      floated="right"
                      type='submit'
                      loading={this.state.loading}
                      disabled={this.state.loading}>
                  <Icon name="send"/>
                  Send
              </Button>
          </div>
      </Container>
    )
  }
}

module.exports = LinkTeamAppAdder;