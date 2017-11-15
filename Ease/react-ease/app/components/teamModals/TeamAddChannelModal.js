var React = require('react');
import {connect} from "react-redux";
import {showAddTeamChannelModal, showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import * as channelActions from "../../actions/channelActions";
import {renderUserLabel} from "../../utils/renderHelpers";
import {reflect} from "../../utils/utils";
import {withRouter} from "react-router-dom";
import { Header, Container, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';

@connect((store)=>({
  team: store.teams[store.teamModals.addChannelModal.team_id]
}))
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
    const team = this.props.team;
    this.state.options = Object.keys(team.team_users).map(id => {
      const item = team.team_users[id];
      return {
        key: item.id,
        text: item.username + ' - ' + item.first_name + ' ' + item.last_name,
        username: item.username,
        value: item.id
      }
    });
    this.state.value.push(team.my_team_user_id);
  }
  validateChannelCreation = (e) => {
    e.preventDefault();
    const team = this.props.team;

    if (Object.keys(team.rooms).length > 3 && team.plan_id === 0){
      this.props.dispatch(showUpgradeTeamPlanModal({
        active: true,
        feature_id: 0,
        team_id: team.id
      }));
      return;
    }
    const name = this.state.name;
    const purpose = this.state.purpose;
    const selectedUsers = this.state.value;

    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(channelActions.createTeamChannel({
      team_id: team.id,
      name:name,
      purpose: purpose
    })).then(response => {
      const channel_id = response.id;
      let addUserActions = selectedUsers.map(function(item){
        return this.props.dispatch(channelActions.addTeamUserToChannel({
          team_id: team.id,
          channel_id: channel_id,
          team_user_id: item
        }));
      }, this);
      Promise.all(addUserActions.map(reflect)).then(() => {
        this.close();
        this.props.history.push(`/teams/${team.id}/${channel_id}`);
      });
    }).catch(err => {
      this.setState({loading: false, errorMessage: err})
    })
  };
  close = () => {
    this.props.dispatch(showAddTeamChannelModal({active: false}));
  };
  inputChange = (e, {value}) => {
    this.setState({[e.target.name] : value});
  };
  nameChange = (e) => {
    e.target.value = e.target.value.replace(" ", "_").toLowerCase();
    this.setState({[e.target.name] : e.target.value});
  };
  dropdownChange = (e, {value}) => {
    if (value.indexOf(this.props.team.my_team_user_id) === -1)
      return;
    this.setState({value: value});
  };
  render(){
    return (
        <div className="ease_modal" id="add_channel_modal">
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={this.close}>
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
                  <Form.Field>
                    <label>
                      Name
                    </label>
                    <Input onChange={this.nameChange} icon="hashtag" iconPosition="left" placeholder="name" type="text" name="name"/>
                    <p className="advice">Ex: marketing, dev, sales, administrators, social_media, product</p>
                  </Form.Field>
                  <Form.Input label="Purpose (optional)" onChange={this.inputChange} placeholder="Purpose" type="text" name="purpose"/>
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
                    <Button floated='right' onClick={this.close}>Cancel</Button>
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