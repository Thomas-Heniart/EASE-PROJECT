var React = require('react');
import {connect} from "react-redux";
import {showAddTeamChannelModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import * as channelActions from "../../actions/channelActions";
import {renderUserLabel} from "../../utils/renderHelpers";
import {reflect} from "../../utils/utils";
import {withRouter} from "react-router-dom";
import { Header, Container, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

@connect((store)=>{
  return {
    users: store.users.users,
    myId: store.team.myTeamUserId,
    channels: store.channels.channels,
    plan_id: store.team.plan_id
  };
})
class TeamAddChannelModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      name: '',
      purpose: '',
      options: [],
      value: [],
      errorMessage: ''
    };
    this.state.options = this.props.users.map(item => {
      return {
        key: item.id,
        text: item.username + ' - ' + item.first_name + ' ' + item.last_name,
        username: item.username,
        value: item.id
      }
    });
    this.state.value.push(this.props.myId);
    this.validateChannelCreation = this.validateChannelCreation.bind(this);
    this.dropdownChange = this.dropdownChange.bind(this);
    this.inputChange = this.inputChange.bind(this);
  }
  validateChannelCreation(e){
    e.preventDefault();
    if (this.props.channels.length > 3 && this.props.plan_id === 0){
      this.props.dispatch(showUpgradeTeamPlanModal(true, 0));
      return;
    }
    const name = this.state.name;
    const purpose = this.state.purpose;
    const selectedUsers = this.state.value;

    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(channelActions.createTeamChannel(name, purpose)).then(response => {
      const channel_id = response.id;
      var addUserActions = selectedUsers.map(function(item){
        return this.props.dispatch(channelActions.addTeamUserToChannel(channel_id, item));
      }, this);
      Promise.all(addUserActions.map(reflect)).then(() => {
        this.props.dispatch(showAddTeamChannelModal(false));
        this.props.history.push(`/teams/${this.props.match.params.teamId}/${channel_id}`);
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err})
    })
  }
  inputChange(e, {value}){
    this.setState({[e.target.name] : value});
  }
  dropdownChange(e, {value}){
    if (value.indexOf(this.props.myId) === -1)
      return;
    this.setState({value: value});
  }
  render(){
    return (
        <div className="ease_modal" id="add_channel_modal">
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={e => {this.props.dispatch(showAddTeamChannelModal(false))}}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div className="contents">
              <Container>
                <Header as="h1">
                  Create a room
                  <Header.Subheader>
                    Rooms are where your team members securely exchange tools and credentials. They can be organized by topic, working group, or specific project.
                  </Header.Subheader>
                </Header>
                <Form error={this.state.errorMessage.length > 0} onSubmit={this.validateChannelCreation}>
                  <Form.Input label="Name" onChange={this.inputChange} icon="hashtag" iconPosition="left" placeholder="Name" type="text" name="name"/>
                  <Form.Input label="Purpose" onChange={this.inputChange} placeholder="Purpose" type="text" name="purpose"/>
                  <Form.Dropdown
                      search={true}
                      options={this.state.options}
                      value={this.state.value}
                      onChange={this.dropdownChange}
                      fluid
                      selection={true}
                      multiple
                      renderLabel={renderUserLabel}
                      placeholder="Tag users here..."
                      label="Members : people who will have access to this group"/>
                  <Message
                      error
                      content={this.state.errorMessage}/>
                  <Form.Field>
                    <Button
                        positive
                        floated='right'
                        loading={this.state.loading}
                        type="submit"
                        disabled={this.state.name.length === 0}>
                      Next
                    </Button>
                    <Button floated='right' onClick={e => {this.props.dispatch(showAddTeamChannelModal(false))}}>Cancel</Button>
                  </Form.Field>
                </Form>
              </Container>
            </div>
          </div>
        </div>
    )
  }
}

module.exports = withRouter(TeamAddChannelModal);