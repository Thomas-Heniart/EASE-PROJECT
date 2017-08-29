var React = require('react');
var classnames = require('classnames');
import {connect} from "react-redux";
import {showAddTeamChannelModal} from "../../actions/teamModalActions";
import * as channelActions from "../../actions/channelActions";
import {renderUserLabel} from "../../utils/renderHelpers";
import { Header, Container, Segment, Checkbox, Form, Input, Select, Dropdown, Button } from 'semantic-ui-react';

@connect((store)=>{
  return {
    users: store.users.users
  };
})
class TmpModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      name: '',
      purpose: '',
      options: [],
      value: []
    };
    this.state.options = this.props.users.map(item => {
      return {
        key: item.id,
        text: item.username + ' - ' + item.first_name + ' ' + item.last_name,
        username: item.username,
        value: item.id
      }
    });
    this.validateChannelCreation = this.validateChannelCreation.bind(this);
    this.dropdownChange = this.dropdownChange.bind(this);
    this.inputChange = this.inputChange.bind(this);
  }
  componentWillReceiveProps(nextProps){
    if (this.props.users !== nextProps.users){
      var options = nextProps.users.map(item => {
        return {
          key: item.id,
          text: item.username + ' - ' + item.first_name + ' ' + item.last_name,
          username: item.username,
          value: item.id
        }
      });
      this.setState({options: options});
    }
  }
  validateChannelCreation(){
    const name = this.state.name;
    const purpose = this.state.purpose;
    const selectedUsers = this.state.value;

    this.props.dispatch(channelActions.createTeamChannel(name, purpose)).then(response => {
      const channel_id = response.id;
      var addUserActions = selectedUsers.map(function(item){
        return this.props.dispatch(channelActions.addTeamUserToChannel(channel_id, item));
      }, this);
      Promise.all(addUserActions).then(() => {
        this.props.dispatch(showAddTeamChannelModal(false));
      });
    })
  }
  inputChange(e, {value}){
    this.setState({[e.target.name] : value});
  }
  dropdownChange(e, {value}){
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
                  Create a group
                  <Header.Subheader>
                    Groups are where your team members securely exchange tools and credentials. They can be organized by topic, working group, or specific project.
                  </Header.Subheader>
                </Header>
                <Form>
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
                  <Form.Field>
                    <Button positive floated='right' disabled={this.state.name.length === 0} onClick={this.validateChannelCreation}>Next</Button>
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

module.exports = TmpModal;