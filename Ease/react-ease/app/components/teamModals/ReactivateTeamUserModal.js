var React = require('react');
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showReactivateTeamUserModal} from "../../actions/teamModalActions";
import {reactivateTeamUser} from "../../actions/userActions";
import {connect} from "react-redux";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

@connect((store)=>({
  team: store.teams[store.teamModals.reactivateTeamUserModal.team_id],
  team_user_id: store.teamModals.reactivateTeamUserModal.team_user_id
}))
class ReactivateTeamUserModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    };
  }
  confirm = (e) => {
    e.preventDefault();
    this.setState({errorMessage: '', loading: true});
    this.props.dispatch(reactivateTeamUser({
      team_user_id: this.props.team_user_id,
      team_id: this.props.team.id
    })).then(response => {
      this.close();
    }).catch(err => {
      this.setState({errorMessage:err, loading:false});
    });
  };
  close = () => {
    this.props.dispatch(showReactivateTeamUserModal({active: false}));
  };
  render(){
    const user = this.props.teams.team_users[this.props.team_user_id];

    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={`${user.username}'s password has been lost`}>
          <Form className="container" onSubmit={this.confirm} error={!!this.state.errorMessage.length}>
            <Form.Field>
              <p>
                <strong class="first_word_capitalize">{user.username}</strong>’s access to company apps has been blocked. For safety reasons, you need to validate <strong>{user.username}</strong>’s access again.
              </p>
              <p>
                We advise you to call or meet with <strong>{user.username}</strong> to make sure the password renewal is authentic.
              </p>
              <p>
                If <strong>{user.username}</strong>’s password renewal request does not seem authentic, please <a onClick={e => {this.props.dispatch(showReactivateTeamUserModal(false))}}>click here</a> to remain the account blocked.
              </p>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                loading={this.state.loading}
                positive
                className="modal-button"
                type="submit"
                content="OK, I GIVE THE ACCESS"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = ReactivateTeamUserModal;